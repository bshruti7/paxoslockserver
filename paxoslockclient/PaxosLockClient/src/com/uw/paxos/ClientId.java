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
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClientId other = (ClientId) obj;
		if (clientAddress == null) {
			if (other.clientAddress != null)
				return false;
		} else if (!clientAddress.equals(other.clientAddress))
			return false;
		if (clientPort != other.clientPort)
			return false;
		return true;
	}
}

