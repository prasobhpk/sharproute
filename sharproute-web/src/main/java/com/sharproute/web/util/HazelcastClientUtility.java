package com.sharproute.web.util;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import com.hazelcast.core.HazelcastInstance;
import com.sharproute.common.object.FixServer;

@Named
public class HazelcastClientUtility {
	
	@Inject
	HazelcastInstance hazelcastInstance;
	
	@PostConstruct
	public void init(){
		hazelcastInstance.getMap(FixServer.class.getSimpleName()).containsKey(1);
	}

}
