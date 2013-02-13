package com.sharproute.repository;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.hazelcast.config.MapConfig;
import com.hazelcast.core.HazelcastInstance;

@Named
public class RepositoryServerInstance implements ApplicationContextAware { 
	
	private static Logger logger = LoggerFactory.getLogger(RepositoryServerInstance.class);
	
	private ApplicationContext applicationContext;
	
	@Inject
	private HazelcastInstance hazelcastInstance;
	
    public static void main(String[] args) {
    	try {
			ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-sharproute-repository.xml");
			RepositoryServerInstance server = applicationContext.getBean("repositoryServerInstance", RepositoryServerInstance.class);
			server.start();
		} catch (Exception e) {
			logger.error("Fatal Exception has occurred. Unable to start.");
			logger.error(e.getMessage(), e);
			System.exit(-1);
		}
    }
    
    public void start(){
    	applicationContext.toString();
    	for (MapConfig mapConfig : hazelcastInstance.getConfig().getMapConfigs().values()) {
			hazelcastInstance.getMap(mapConfig.getName());
		} 
    	logger.info(this.getClass().getSimpleName() +" Start Complete");
    }
    
    public void stop(){
    	logger.info(this.getClass().getSimpleName() + " Stop Complete");
    }

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
    
}
