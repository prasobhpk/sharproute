package com.sharproute.fix.management;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.sharproute.common.object.FixServer;

@Component
public class FixEngineStatusUpdateTask implements Runnable {
	
	private static Logger logger = LoggerFactory.getLogger(FixEngineStatusUpdateTask.class);
	
	@Resource
	HazelcastInstance hazelcastInstance;
	
	@Value("${fixServer.uid}")
	private Integer fixServerUid;

	@Override
	public void run() {
		IMap<Integer, FixServer> fixServerMap = hazelcastInstance.getMap(FixServer.class.getSimpleName());
		while (true){
			FixServer fixServer = fixServerMap.get(fixServerUid);
			fixServerMap.put(fixServerUid, fixServer);
			try { Thread.sleep(5000); } catch (InterruptedException e) {}
		}
	}
	
	@PostConstruct
	public void start(){
		logger.debug("Starting");
		Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(this, 5, 5, TimeUnit.SECONDS);
	}

}
