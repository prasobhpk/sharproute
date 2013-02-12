package com.sharproute.fix;

import javax.inject.Named;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import quickfix.Application;
import quickfix.ConfigError;
import quickfix.Connector;
import quickfix.DefaultMessageFactory;
import quickfix.LogFactory;
import quickfix.MemoryStoreFactory;
import quickfix.MessageFactory;
import quickfix.MessageStoreFactory;
import quickfix.ScreenLogFactory;
import quickfix.SessionID;
import quickfix.SessionSettings;
import quickfix.ThreadedSocketAcceptor;
import quickfix.ThreadedSocketInitiator;
import quickfix.field.BeginString;
import quickfix.field.SenderCompID;
import quickfix.field.TargetCompID;

import com.sharproute.common.object.ConnectionType;
import com.sharproute.common.object.FixSession;

@Named
public class FixSessionManager implements ApplicationContextAware {
	
	private ApplicationContext applicationContext;
	
	public Application createApplication(){
    	return applicationContext.getBean("defaultApplication", DefaultApplication.class);
    }
	
	public SessionID createSessionID(FixSession fixSession){
		SessionID sessionID = new SessionID(fixSession.getBeginString(), fixSession.getSenderCompId(), fixSession.getTargetCompId());
		return sessionID;
	}
	
	public Connector createConnector(FixSession fixSession) throws ConfigError {
		Application application = createApplication();
		MessageStoreFactory messageStoreFactory = new MemoryStoreFactory();
		SessionSettings sessionSettings = createSessionSettings(fixSession);
		LogFactory logFactory = new ScreenLogFactory();
		MessageFactory messageFactory = new DefaultMessageFactory();
		Connector connector;
		if (fixSession.getConnectionType().equals(ConnectionType.ACCEPTOR)){
			connector = new ThreadedSocketAcceptor(application, messageStoreFactory, sessionSettings, logFactory, messageFactory);
		} else {
			connector = new ThreadedSocketInitiator(application, messageStoreFactory, sessionSettings, logFactory, messageFactory);
		}
		return connector;
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
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
