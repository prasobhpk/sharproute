package com.sharproute.web.websocket;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.google.gson.Gson;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.EntryListener;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.sharproute.common.object.FixServer;
import com.sharproute.web.object.Event;

@SuppressWarnings("serial")
public class FixServerStatusServlet extends WebSocketServlet {
	
	private HazelcastInstance hazelcastInstance;
	
	@Override
	public void init() throws ServletException {
		super.init();
		ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
		hazelcastInstance = applicationContext.getBean("hazelcastInstance", HazelcastInstance.class);
		hazelcastInstance.getMap(FixServer.class.getSimpleName()).containsKey(3);
	}

	@Override
	public WebSocket doWebSocketConnect(HttpServletRequest request, String aString) {
		return new FixServerStatusWebSocket();
	}
	
	class FixServerStatusWebSocket implements WebSocket, EntryListener<Integer, FixServer> {
		
		private Connection connection;
		Gson gson = new Gson();
		
		@Override
		public void onOpen(Connection connection) {
			this.connection = connection;
			IMap<Integer, FixServer> fixServerMap = hazelcastInstance.getMap(FixServer.class.getSimpleName());
			fixServerMap.addEntryListener(this, true);
		}
		
		@Override
		public void onClose(int arg0, String arg1) {
			IMap<Integer, FixServer> fixServerMap = hazelcastInstance.getMap(FixServer.class.getSimpleName());
			fixServerMap.removeEntryListener(this);
		}

		@Override
		public void entryAdded(EntryEvent<Integer, FixServer> event) {
			try {
				Event e = new Event(event);
				String json = gson.toJson(e);
				connection.sendMessage(json);
			} catch (IOException e) {
				connection.disconnect();
			}
		}

		@Override
		public void entryEvicted(EntryEvent<Integer, FixServer> event) {
			try {
				String json = gson.toJson(event);
				connection.sendMessage(json);
			} catch (IOException e) {
				connection.disconnect();
			}
		}

		@Override
		public void entryRemoved(EntryEvent<Integer, FixServer> event) {
			try {
				String json = gson.toJson(event);
				connection.sendMessage(json);
			} catch (IOException e) {
				connection.disconnect();
			}
		}

		@Override
		public void entryUpdated(EntryEvent<Integer, FixServer> event) {
			try {
				Event e = new Event(event);
				String json = gson.toJson(e);
				connection.sendMessage(json);
			} catch (IOException e) {
				connection.disconnect();
			}
		}
	}

}
