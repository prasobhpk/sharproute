package com.sharproute.web.websocket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.apache.catalina.websocket.WsOutbound;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.google.gson.Gson;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.EntryListener;
import com.hazelcast.core.HazelcastInstance;
import com.sharproute.web.object.Event;

public class MapListenerWebSocketServlet extends WebSocketServlet {
	
	private static final long serialVersionUID = 4289804442973084863L;
	
	public final static String ACTION_ERROR = "ERROR";
    public final static String ACTION_SNAPSHOT = "SNAPSHOT";
    
    private String mapName;
    private HazelcastInstance hazelcastInstance;
    private Gson gson = new Gson();
    
    public MapListenerWebSocketServlet() {
		this.mapName = getServletConfig().getInitParameter("mapName");
		WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
		this.hazelcastInstance = applicationContext.getBean("hazelcastInstance", HazelcastInstance.class);
	}

    @Override
    protected StreamInbound createWebSocketInbound(String subProtocol, HttpServletRequest request) {
        return new ActionMessageInbound();
    }
    
    private final class ActionMessageInbound extends MessageInbound implements EntryListener {
    	
    	@Override
        protected void onOpen(WsOutbound outbound) {
    		hazelcastInstance.getMap(mapName).addEntryListener(this, true);
        }
    	
    	@Override
        protected void onClose(int status) {
    		hazelcastInstance.getMap(mapName).removeEntryListener(this);
        }

		@Override
		protected void onBinaryMessage(ByteBuffer message) throws IOException {
			throw new UnsupportedOperationException("Binary message not supported.");
		}

		@Override
		protected void onTextMessage(CharBuffer message) throws IOException {
			String action = message.toString();
			if (action.equals(ACTION_SNAPSHOT)){
				Set<Entry<Object, Object>> entrySet = hazelcastInstance.getMap(mapName).entrySet();
				for (Entry<Object, Object> entry : entrySet) {
					Object object = entry.getValue();
					String objectType = entry.getValue().getClass().getSimpleName();
					String eventType = ACTION_SNAPSHOT;
					Event event = new Event(objectType, eventType, object);
					sendEvent(event);
				}
			} else {
				Event event = new Event("String", ACTION_ERROR, "Unknown Action Request: " + action);
				sendEvent(event);
			}
		}
		
		protected void sendEvent(Event event){
			String json = gson.toJson(event);
			CharBuffer buffer = CharBuffer.wrap(json);
			try {
				this.getWsOutbound().writeTextMessage(buffer);
			} catch (IOException e) {
				
			}
		}

		@Override
		public void entryAdded(EntryEvent event) {
			this.sendEvent(new Event(event));
		}

		@Override
		public void entryRemoved(EntryEvent event) {
			this.sendEvent(new Event(event));
		}

		@Override
		public void entryUpdated(EntryEvent event) {
			this.sendEvent(new Event(event));
		}

		@Override
		public void entryEvicted(EntryEvent event) {
			this.sendEvent(new Event(event));
		}
    	
    }

}