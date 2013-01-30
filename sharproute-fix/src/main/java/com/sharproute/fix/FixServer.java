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
import com.sharproute.common.object.FixEngine;
import com.sharproute.common.object.FixSession;

@Component
public class FixServer implements ApplicationContextAware, EntryListener<Integer, FixSession> { 
	
	private static Logger logger = LoggerFactory.getLogger(FixServer.class);
	
	@Resource
	private FixSessionManager fixSessionManager;
	
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
    
    public void initialize() throws ConfigError {
    	verifyServerStartupState();
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
    
    private void verifyServerStartupState(){
    	IMap<Integer, FixEngine> fixEngineMap = hazelcastInstance.getMap(FixEngine.class.getSimpleName());
    	if (!fixEngineMap.containsKey(fixEngineUid)){
    		throw new RuntimeException("FixEngine not found in map for " + fixEngineUid);
    	}
    }
    
    private void initializeFixConnections() throws ConfigError {
    	IMap<Integer, FixSession> fixSessionMap = hazelcastInstance.getMap(FixSession.class.getSimpleName());
    	fixSessionMap.addEntryListener(this, true);
    	for (Map.Entry<Integer, FixSession> entry : fixSessionMap.entrySet(new SqlPredicate("fixEngine.uid = " + fixEngineUid))) {
    		FixSession fixSession = entry.getValue();
			Connector connector = fixSessionManager.createConnector(fixSession);
			SessionID sessionID = fixSessionManager.createSessionID(fixSession);
			connectorMap.put(sessionID, connector);
		} 
    }
    
    private void addNewConnection(FixSession fixSession) throws ConfigError {
		Connector connector = fixSessionManager.createConnector(fixSession);
		connectorMap.put(connector.getSessions().get(0), connector);
    }
    
    private void removeConnection(FixSession fixSession){
    	SessionID sessionID = new SessionID(fixSession.getBeginString(), fixSession.getSenderCompId(), fixSession.getTargetCompId());
    	Connector connector = connectorMap.remove(sessionID);
    	connector.stop();
    }
    
    private void updateConnection(FixSession fixSession) throws ConfigError {
    	SessionID sessionID = new SessionID(fixSession.getBeginString(), fixSession.getSenderCompId(), fixSession.getTargetCompId());
    	Connector connector = connectorMap.get(sessionID);
    	connector.stop();
    	connector = fixSessionManager.createConnector(fixSession);
    	try {
			connector.start();
		} catch (RuntimeError | ConfigError e) {
			//TODO Handle configerror better
			logger.error(e.getMessage());
		}
    	connectorMap.put(sessionID, connector);
    }
    
	@Override
	public void entryAdded(EntryEvent<Integer, FixSession> event) {
		try {
			addNewConnection(event.getValue());
		} catch (ConfigError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void entryRemoved(EntryEvent<Integer, FixSession> event) {
		removeConnection(event.getOldValue());
	}

	@Override
	public void entryUpdated(EntryEvent<Integer, FixSession> event) {
		try {
			updateConnection(event.getValue());
		} catch (ConfigError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
