package com.sharproute.web.websocket;

import java.io.IOException;

import javax.annotation.Resource;
import javax.websocket.Session;
import javax.websocket.WebSocketClose;
import javax.websocket.WebSocketMessage;
import javax.websocket.WebSocketOpen;
import javax.websocket.server.DefaultServerConfiguration;
import javax.websocket.server.WebSocketEndpoint;

import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.EntryListener;
import com.hazelcast.core.HazelcastInstance;
import com.sharproute.common.object.FixServer;
import com.sharproute.web.object.Event;

@Component
@WebSocketEndpoint(value = "/FixServerStatus/*", configuration = DefaultServerConfiguration.class)
public class FixServerStatusConnection implements EntryListener<Integer, FixServer> {
	
	@Resource
	private HazelcastInstance hazelcastInstance;
	private Gson gson = new Gson();
	private Session session;
	
	@WebSocketOpen
    public void start(Session session) {
        this.session = session;
    }

    @WebSocketClose
    public void end() {
    	try {
			this.session.close();
		} catch (IOException e) {
		}
    }

    @WebSocketMessage
    public void incoming(String message) {

    }
   
	@Override
	public void entryAdded(EntryEvent<Integer, FixServer> entryEvent) {
		sendEvent(entryEvent);
	}

	@Override
	public void entryEvicted(EntryEvent<Integer, FixServer> entryEvent) {
		sendEvent(entryEvent);
	}

	@Override
	public void entryRemoved(EntryEvent<Integer, FixServer> entryEvent) {
		sendEvent(entryEvent);
	}

	@Override
	public void entryUpdated(EntryEvent<Integer, FixServer> entryEvent) {
		sendEvent(entryEvent);
	}
	
	private void sendEvent(EntryEvent<Integer, FixServer> entryEvent){
		Event event = new Event(entryEvent);
		String json = gson.toJson(event);
		try {
			this.session.getRemote().sendString(json);
		} catch (IOException e) {
			try {
				this.session.close();
			} catch (IOException e1) {
			}
		}
	}
}
