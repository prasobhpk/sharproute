package com.sharproute.web.object;

import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.EntryEventType;

public class Event {
	
	private String objectType;
	private String eventType;
	private Object object;
	
	public Event(String objectType, String eventType, Object object){
		this.objectType = objectType;
		this.eventType = eventType;
		this.object = object;
	}
	
	public Event(EntryEvent<?, ?> event) {
		this.objectType = event.getValue().getClass().getSimpleName();
		EntryEventType entryEventType = event.getEventType();
		this.eventType = entryEventType.toString();
		this.object = event.getValue();
	}

}
