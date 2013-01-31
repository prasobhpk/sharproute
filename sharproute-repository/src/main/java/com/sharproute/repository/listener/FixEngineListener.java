package com.sharproute.repository.listener;

import com.hazelcast.core.Client;
import com.hazelcast.core.ClientListener;
import com.hazelcast.core.ClientType;

public class FixEngineListener implements ClientListener {

	@Override
	public void clientConnected(Client client) {
		
	}

	@Override
	public void clientDisconnected(Client client) {
		// TODO Auto-generated method stub

	}

}
