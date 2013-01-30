package com.sharproute.fix;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import quickfix.Acceptor;
import quickfix.Application;
import quickfix.ConfigError;
import quickfix.Connector;
import quickfix.DefaultMessageFactory;
import quickfix.LogFactory;
import quickfix.MemoryStoreFactory;
import quickfix.MessageFactory;
import quickfix.MessageStoreFactory;
import quickfix.RuntimeError;
import quickfix.ScreenLogFactory;
import quickfix.SessionID;
import quickfix.SessionSettings;
import quickfix.ThreadedSocketAcceptor;
import quickfix.field.BeginString;
import quickfix.field.SenderCompID;
import quickfix.field.TargetCompID;

import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.EntryListener;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.query.SqlPredicate;
import com.sharproute.common.object.ConnectionType;
import com.sharproute.common.object.FixSession;

@Component
public class FixServer implements ApplicationContextAware, EntryListener<Integer, FixSession> { 
	
	private static Logger logger = LoggerFactory.getLogger(FixServer.class);
	
	private ApplicationContext applicationContext;
	
	@Value("${fixEngine.uid}")
	private Integer fixEngineUid;
	
	@Resource
	private HazelcastInstance hazelcastInstance;
	
	private boolean isRunning;
	private Map<SessionID, Connector> connectorMap = new HashMap<>();
	
    public static void main(String[] args) throws ConfigError {
    	ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-sharproute-fix.xml");
        FixServer server = applicationContext.getBean("fixServer", FixServer.class);
        server.initialize();
        server.start();
    }
    
    public void initialize() throws ConfigError{
    	initializeFixConnections();
    }
    
    public void start() throws ConfigError {
    	for (Connector connector : connectorMap.values()) {
			connector.start();
		}
    	isRunning = true;
    }
    
    public void stop(){
    	for (Connector connector : connectorMap.values()) {
			connector.stop();
		}
    	isRunning = false;
    }
    
    private void initializeFixConnections() throws ConfigError {
    	IMap<Integer, FixSession> fixSessionMap = hazelcastInstance.getMap(FixSession.class.getSimpleName());
    	fixSessionMap.addEntryListener(this, true);
    	for (Map.Entry<Integer, FixSession> entry : fixSessionMap.entrySet(new SqlPredicate("fixEngine.uid = " + fixEngineUid))) {
			FixSession fixSession = entry.getValue();
			SessionID sessionID = new SessionID(fixSession.getBeginString(), fixSession.getSenderCompId(), fixSession.getTargetCompId());
			Application application = createApplication();
			MessageStoreFactory messageStoreFactory = createMessageStoreFactory();
			SessionSettings sessionSettings = createSessionSettings(fixSession);
			LogFactory logFactory = new ScreenLogFactory();
			MessageFactory messageFactory = new DefaultMessageFactory();
			Acceptor acceptor = new ThreadedSocketAcceptor(application, messageStoreFactory, sessionSettings, logFactory, messageFactory);
			connectorMap.put(sessionID, acceptor);
		} 
    }
    
    private Application createApplication(){
    	return applicationContext.getBean("defaultApplication", DefaultApplication.class);
    }
    
    private MessageStoreFactory createMessageStoreFactory(){
    	return new MemoryStoreFactory();
    }
    
    private SessionSettings createSessionSettings(FixSession fixSession){
    	SessionSettings sessionSettings = new SessionSettings();
    	SessionID sessionID = new SessionID(new BeginString(fixSession.getBeginString()), new SenderCompID(fixSession.getSenderCompId()), new TargetCompID(fixSession.getTargetCompId()));
    	sessionSettings.setString(sessionID, SessionSettings.BEGINSTRING, fixSession.getBeginString());
    	sessionSettings.setString(sessionID, quickfix.SessionSettings.SENDERCOMPID, fixSession.getSenderCompId());
    	sessionSettings.setString(sessionID, quickfix.SessionSettings.TARGETCOMPID, fixSession.getTargetCompId());
    	sessionSettings.setString(sessionID, quickfix.DefaultSessionFactory.SETTING_CONNECTION_TYPE, fixSession.getConnectionType().toString().toLowerCase());
    	if (fixSession.getConnectionType().equals(ConnectionType.ACCEPTOR)){
    		sessionSettings.setString(sessionID, quickfix.Acceptor.SETTING_SOCKET_ACCEPT_ADDRESS, fixSession.getHost());
    		sessionSettings.setLong(sessionID, quickfix.Acceptor.SETTING_SOCKET_ACCEPT_PORT, fixSession.getPort());
    	} else {
    		sessionSettings.setString(sessionID, quickfix.Initiator.SETTING_SOCKET_CONNECT_HOST, fixSession.getHost());
    		sessionSettings.setLong(sessionID, quickfix.Initiator.SETTING_SOCKET_CONNECT_PORT, fixSession.getPort());
    	}

    	//TODO Hardcodes
    	sessionSettings.setLong(sessionID, quickfix.Session.SETTING_HEARTBTINT, 30);
    	sessionSettings.setLong(sessionID, "ReconnectInterval", 60);
    	sessionSettings.setString(sessionID, quickfix.Session.SETTING_USE_DATA_DICTIONARY, "N");
    	sessionSettings.setString(sessionID, "StartTime", "19:30:00 US/Eastern");
    	sessionSettings.setString(sessionID, "EndTime", "18:33:00 US/Eastern");
    	
    	return sessionSettings;
    }
    
    private void addNewConnection(FixSession fixSession) {
		SessionID sessionID = new SessionID(fixSession.getBeginString(), fixSession.getSenderCompId(), fixSession.getTargetCompId());
		Application application = createApplication();
		MessageStoreFactory messageStoreFactory = createMessageStoreFactory();
		SessionSettings sessionSettings = createSessionSettings(fixSession);
		LogFactory logFactory = new ScreenLogFactory();
		MessageFactory messageFactory = new DefaultMessageFactory();
		Acceptor acceptor;
		try {
			acceptor = new ThreadedSocketAcceptor(application, messageStoreFactory, sessionSettings, logFactory, messageFactory);
			connectorMap.put(sessionID, acceptor);
		} catch (ConfigError e) {
			//TODO Handle configerror better
			logger.error(e.getMessage());
		}
    }
    
    private void removeConnection(FixSession fixSession){
    	SessionID sessionID = new SessionID(fixSession.getBeginString(), fixSession.getSenderCompId(), fixSession.getTargetCompId());
    	Connector connector = connectorMap.remove(sessionID);
    	connector.stop();
    }
    
    private void updateConnection(FixSession fixSession){
    	SessionID sessionID = new SessionID(fixSession.getBeginString(), fixSession.getSenderCompId(), fixSession.getTargetCompId());
    	Connector connector = connectorMap.get(sessionID);
    	connector.stop();
    	connector = createConnector(fixSession);
    	try {
			connector.start();
		} catch (RuntimeError | ConfigError e) {
			//TODO Handle configerror better
			logger.error(e.getMessage());
		}
    	connectorMap.put(sessionID, connector);
    }
    
    private Connector createConnector(FixSession fixSession){
    	Application application = createApplication();
		MessageStoreFactory messageStoreFactory = createMessageStoreFactory();
		SessionSettings sessionSettings = createSessionSettings(fixSession);
		LogFactory logFactory = new ScreenLogFactory();
		MessageFactory messageFactory = new DefaultMessageFactory();
		Acceptor acceptor = null;
		try {
			acceptor = new ThreadedSocketAcceptor(application, messageStoreFactory, sessionSettings, logFactory, messageFactory);
		} catch (ConfigError e) {
			//TODO Handle configerror better
			logger.error(e.getMessage());
		}
		return acceptor;
    }
    
	@Override
	public void entryAdded(EntryEvent<Integer, FixSession> event) {
		addNewConnection(event.getValue());
	}

	@Override
	public void entryRemoved(EntryEvent<Integer, FixSession> event) {
		removeConnection(event.getOldValue());
	}

	@Override
	public void entryUpdated(EntryEvent<Integer, FixSession> event) {
		updateConnection(event.getValue());
	}

	@Override
	public void entryEvicted(EntryEvent<Integer, FixSession> event) {
		removeConnection(event.getOldValue());
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
    
}
