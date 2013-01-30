package com.sharproute.repository;

import javax.annotation.Resource;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.hazelcast.config.MapConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.Instance;
import com.sharproute.common.object.FixEngine;
import com.sharproute.common.object.FixSession;

@Component
public class RepositoryServer implements ApplicationContextAware { 
	
	private ApplicationContext applicationContext;
	
	@Resource
	private HazelcastInstance hazelcastInstance;
	
    public static void main(String[] args) {
    	ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-sharproute-repository.xml");
    	RepositoryServer server = applicationContext.getBean("repositoryServer", RepositoryServer.class);
        server.start();
    }
    
    public void start(){
    	for (MapConfig mapConfig : hazelcastInstance.getConfig().getMapConfigs().values()) {
			hazelcastInstance.getMap(mapConfig.getName());
		} 
    }
    
    public void stop(){
    	
    }

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
    
}
