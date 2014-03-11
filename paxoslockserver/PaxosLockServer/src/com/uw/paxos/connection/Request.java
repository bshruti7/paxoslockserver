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
	private InetAddress clientIpAddress;
	private int clientPort;
	private String requestData;
	
	public Request(InetAddress clientIpAddress, int clientPort, String requestData) {
		this.clientIpAddress = clientIpAddress;
		this.clientPort = clientPort;
		this.requestData = requestData;
	}
	
	public InetAddress getClientIpAddress() {
	    return clientIpAddress;
    }
	
	public int getClientPort() {
		return clientPort;
	}
	
	public String getRequestData() {
		return requestData;
	}	
}
