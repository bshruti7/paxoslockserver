package com.uw.paxos.connection;

import java.net.InetAddress;

/**
 * Data class containing request from client. 
 * This includes the requester's IP Address, port and the data received.
 * 
 * @author Samiksha Sharma
 *
 */
public class Request {
	private InetAddress senderIpAddress;
	private int senderPort;
	private String message;
	
	public InetAddress getSenderIpAddress() {
		return senderIpAddress;
	}

	public void setSenderIpAddress(InetAddress senderIpAddress) {
		this.senderIpAddress = senderIpAddress;
	}

	public int getSenderPort() {
		return senderPort;
	}

	public void setSenderPort(int senderPort) {
		this.senderPort = senderPort;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
