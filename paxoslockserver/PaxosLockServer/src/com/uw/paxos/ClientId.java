package com.uw.paxos;

import java.net.InetAddress;

public class ClientId {

	InetAddress clientAddress;
	int clientPort;

	public ClientId(InetAddress clientAddress, int clientPort) {
		super();
		this.clientAddress = clientAddress;
		this.clientPort = clientPort;
	}
	public InetAddress getClientAddress() {
		return clientAddress;
	}
	public int getClientPort() {
		return clientPort;
	}
}

