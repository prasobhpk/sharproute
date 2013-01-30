package com.sharproute.fix.test;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sharproute.common.object.FixSession;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-sharproute-fix.xml")
public class TestMessaging {

	@Test
	public void testSimpleMessaging() {
		ConnectionFactory connectionFactory = new CachingConnectionFactory();

		AmqpAdmin admin = new RabbitAdmin(connectionFactory);
		admin.declareQueue(new Queue("myqueue"));

		AmqpTemplate template = new RabbitTemplate(connectionFactory);
		template.convertAndSend("myqueue", "foo");

		String foo = (String) template.receiveAndConvert("myqueue");
	}

}
