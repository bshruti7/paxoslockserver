package com.uw.paxos.messages;

import java.net.InetAddress;

/**
 * Data class containing request from client. 
 * This includes the requester's IP Address, port and the data received.
 * 
 * @author Samiksha Sharma
 *
 */
public class Request {
	private InetAddress serverIpAddress;
	private int serverPort;
	private String message;
	
	public InetAddress getServerIpAddress() {
		return serverIpAddress;
	}

	public void setServerIpAddress(InetAddress ServerIpAddress) {
		this.serverIpAddress = ServerIpAddress;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int ServerPort) {
		this.serverPort = ServerPort;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
