package com.uw.paxos.connection;

import java.net.InetAddress;

/**
 * Data class containing response from server. 
 * This includes the receiver's IP Address, port and the data to be sent.
 * 
 * @author Samiksha Sharma
 *
 */
public class ServerResponse {
	private InetAddress receiverIpAddress;
	private int receiverPort;
	private String responseData;
	
	public ServerResponse(InetAddress clientIpAddress, int clientPort, String data) {
		this.receiverIpAddress = clientIpAddress;
		this.receiverPort = clientPort;
		this.responseData = data;
	}
	
	public InetAddress getReceiverIpAddress() {
	    return receiverIpAddress;
    }
	
	public int getReceiverPort() {
		return receiverPort;
	}
	
	public String getResponseData() {
		return responseData;
	}	
}
