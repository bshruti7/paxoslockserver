package com.uw.paxos.connection;

import java.net.InetAddress;

/**
 * Data class containing response from server. 
 * This includes the receiver's IP Address, port and the data to be sent.
 * 
 * @author Samiksha Sharma
 *
 */
public class Response {
	private InetAddress receiverIpAddress;
	private int receiverPort;
	private String message;
	
	public InetAddress getReceiverIpAddress() {
		return receiverIpAddress;
	}
	public void setReceiverIpAddress(InetAddress receiverIpAddress) {
		this.receiverIpAddress = receiverIpAddress;
	}
	public int getReceiverPort() {
		return receiverPort;
	}
	public void setReceiverPort(int receiverPort) {
		this.receiverPort = receiverPort;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}	
}
