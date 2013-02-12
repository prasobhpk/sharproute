package com.sharproute.fix;

import javax.annotation.Resource;
import javax.inject.Named;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Scope;

import quickfix.Application;
import quickfix.DoNotSend;
import quickfix.FieldNotFound;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.Message;
import quickfix.RejectLogon;
import quickfix.SessionID;
import quickfix.UnsupportedMessageType;

@Named
@Scope("prototype")
public class DefaultApplication implements Application, MessageListener {
	
	@Resource
	AmqpTemplate amqpTemplate;
	
	@Resource
	ConnectionFactory connectionFactory;
	
	public void onCreate(SessionID sessionID) {
		// TODO Auto-generated method stub
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(sessionID.toString());
		container.setMessageListener(new MessageListenerAdapter(this));
	}

	public void onLogon(SessionID arg0) {
		// TODO Auto-generated method stub

	}

	public void onLogout(SessionID arg0) {
		// TODO Auto-generated method stub

	}

	public void fromAdmin(Message message, SessionID sessionID) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
		amqpTemplate.convertAndSend(sessionID.toString(), message);
	}

	public void fromApp(Message message, SessionID sessionID) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
		amqpTemplate.convertAndSend(sessionID.toString(), message);
	}

	public void toAdmin(Message arg0, SessionID arg1) {
		// TODO Auto-generated method stub
	}

	public void toApp(Message arg0, SessionID arg1) throws DoNotSend {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMessage(org.springframework.amqp.core.Message message) {
		message.getBody();
	}

}
