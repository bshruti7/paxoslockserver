package com.uw.paxos.messages;

import java.net.InetAddress;

/**
 * Data class containing response from server. 
 * This includes the receiver's IP Address, port and the data to be sent.
 * 
 * @author Samiksha Sharma
 *
 */
public class Response {
	private InetAddress serverIpAddress;
	private int serverPort;
	private String message;
	
	public InetAddress getServerIpAddress() {
		return serverIpAddress;
	}
	public void setServerIpAddress(InetAddress receiverIpAddress) {
		this.serverIpAddress = receiverIpAddress;
	}
	public int getServerPort() {
		return serverPort;
	}
	public void setServerPort(int receiverPort) {
		this.serverPort = receiverPort;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}	
}
