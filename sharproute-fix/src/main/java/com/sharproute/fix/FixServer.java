package com.sharproute.fix;

import java.io.FileNotFoundException;

import javax.management.JMException;

import org.quickfixj.jmx.JmxExporter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import quickfix.Acceptor;
import quickfix.Application;
import quickfix.ConfigError;
import quickfix.DefaultMessageFactory;
import quickfix.FileLogFactory;
import quickfix.FileStoreFactory;
import quickfix.LogFactory;
import quickfix.MessageFactory;
import quickfix.MessageStoreFactory;
import quickfix.RuntimeError;
import quickfix.SessionSettings;
import quickfix.ThreadedSocketAcceptor;

@Component
public class Server { 
	
    public static void main(String[] args) throws RuntimeError, ConfigError, FileNotFoundException, JMException {
    	ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-demo-quickfixj.xml");
        Server server = new Server();
        server.initializeFixConnections();
        applicationContext.toString();
    }
    
    private void initializeFixConnections() throws RuntimeError, ConfigError, FileNotFoundException, JMException{
    	Application application = new DefaultApplication();
        SessionSettings settings = new SessionSettings("fix-connections.cfg");
        MessageStoreFactory storeFactory = new FileStoreFactory(settings);
        LogFactory logFactory = new FileLogFactory(settings);
        MessageFactory messageFactory = new DefaultMessageFactory();
        Acceptor acceptor = new ThreadedSocketAcceptor(application, storeFactory, settings, logFactory, messageFactory);
        JmxExporter jmxExporter = new JmxExporter();
        jmxExporter.register(acceptor);
        acceptor.start();
    }
    
}
