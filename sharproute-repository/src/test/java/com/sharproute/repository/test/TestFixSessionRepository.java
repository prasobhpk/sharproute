package com.sharproute.repository.test;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sharproute.common.object.FixSession;
import com.sharproute.repository.FixSessionRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-sharproute-repository.xml")
@Transactional
public class TestFixSessionRepository {
	
	@Resource
	FixSessionRepository fixSessionRepository;

	@Test
	public void testCrud() {
		FixSession fixSession = new FixSession();
		fixSession.setSenderCompId("TESTSENDER");
		fixSession.setTargetCompId("TESTTARGET");
		fixSessionRepository.save(fixSession);
	}

}
