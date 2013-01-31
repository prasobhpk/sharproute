package com.sharproute.web.object;

import com.hazelcast.core.EntryEvent;

public class Event {
	
	private String type;
	private String eventType;
	private Object object;
	
	public Event(EntryEvent<?, ?> event) {
		this.type = event.getValue().getClass().getSimpleName();
		this.eventType = event.getEventType().toString();
		this.object = event.getValue();
	}

}
