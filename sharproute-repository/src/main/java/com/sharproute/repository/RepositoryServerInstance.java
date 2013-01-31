package com.sharproute.repository;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.hazelcast.config.MapConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.Instance;
import com.sharproute.common.object.FixServer;
import com.sharproute.common.object.FixSession;

@Component
public class RepositoryServerInstance implements ApplicationContextAware { 
	
	private static Logger logger = LoggerFactory.getLogger(RepositoryServerInstance.class);
	
	private ApplicationContext applicationContext;
	
	@Resource
	private HazelcastInstance hazelcastInstance;
	
    public static void main(String[] args) {
    	try {
			ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-sharproute-repository.xml");
			RepositoryServerInstance server = applicationContext.getBean("repositoryServerInstance", RepositoryServerInstance.class);
			server.start();
		} catch (Exception e) {
			logger.error("Fatal Exception has occurred. Unable to start.");
			logger.error(e.getStackTrace().toString());
			System.exit(-1);
		}
    }
    
    public void start(){
    	for (MapConfig mapConfig : hazelcastInstance.getConfig().getMapConfigs().values()) {
			hazelcastInstance.getMap(mapConfig.getName());
		} 
    	logger.info(RepositoryServerInstance.class.getSimpleName() +" Start Complete");
    }
    
    public void stop(){
    	logger.info("RepositoryServerInstance Stop Complete");
    }

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
    
}
