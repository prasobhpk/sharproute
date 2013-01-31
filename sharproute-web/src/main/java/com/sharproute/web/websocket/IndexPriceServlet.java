package com.sharproute.web.websocket;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.EntryListener;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

@SuppressWarnings("serial")
public class IndexPriceServlet extends WebSocketServlet {
	
	private HazelcastInstance hazelcastInstance;
	
	@Override
	public void init() throws ServletException {
		super.init();
		ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
		hazelcastInstance = applicationContext.getBean("hazelcastInstance", HazelcastInstance.class);
		Thread t = new Thread(new IndexPricePublisher("NGH12"));
		t.start();
	}
	
	@Override
	public boolean checkOrigin(HttpServletRequest request, String origin) {
		
		return super.checkOrigin(request, origin);
	}

	@Override
	public WebSocket doWebSocketConnect(HttpServletRequest request, String aString) {
		return new IndexPriceWebSocket(request.getParameter("symbol"));
	}
	
	class IndexPriceWebSocket implements WebSocket, EntryListener<String, BigDecimal> {
		
		private String symbol;
		private Connection connection;
		
		
		public IndexPriceWebSocket(String symbol) {
			this.symbol = symbol;
		}
		
		@Override
		public void onOpen(Connection connection) {
			this.connection = connection;
			IMap<String, BigDecimal> indexPriceMap = hazelcastInstance.getMap("indexPriceMap");
			indexPriceMap.addEntryListener(this, symbol, true);
		}
		
		@Override
		public void onClose(int arg0, String arg1) {
			IMap<String, BigDecimal> indexPriceMap = hazelcastInstance.getMap("indexPriceMap");
			indexPriceMap.removeEntryListener(this);
		}

		@Override
		public void entryAdded(EntryEvent<String, BigDecimal> event) {
			try {
				connection.sendMessage(event.getValue().toString());
			} catch (IOException e) {
				connection.disconnect();
			}
		}

		@Override
		public void entryEvicted(EntryEvent<String, BigDecimal> event) {
			try {
				connection.sendMessage("No Longer Available");
			} catch (IOException e) {
				connection.disconnect();
			}
		}

		@Override
		public void entryRemoved(EntryEvent<String, BigDecimal> event) {
			try {
				connection.sendMessage("No Longer Available");
			} catch (IOException e) {
				connection.disconnect();
			}
		}

		@Override
		public void entryUpdated(EntryEvent<String, BigDecimal> event) {
			try {
				connection.sendMessage(event.getValue().toString());
			} catch (IOException e) {
				connection.disconnect();
			}
		}
	}
	
	class IndexPricePublisher implements Runnable {
		
		private String symbol;
		
		public IndexPricePublisher(String symbol) {
			this.symbol = symbol;
		}
		
		@Override
		public void run() {
			Random random = new Random();
			IMap<String, BigDecimal> indexPriceMap = hazelcastInstance.getMap("indexPriceMap");
			while (true){
				indexPriceMap.put(symbol, new BigDecimal(random.nextDouble()+3));
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
				}
			}
			
		}
		
	}

}
