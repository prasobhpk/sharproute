package com.sharproute.web.client;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hazelcast.core.HazelcastInstance;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-sharproute-web.xml")
public class HazelcastClientTest {
	
	@Inject
	HazelcastInstance hazelcastInstance;
	

	@Test
	public void testCrud() {
		while (true){
			hazelcastInstance.getMap("test").put(1, 1);
			hazelcastInstance.getMap("test").get(1);
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
			}
		}
		
	}

}
