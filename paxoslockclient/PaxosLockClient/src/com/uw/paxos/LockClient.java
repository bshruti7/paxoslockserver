package com.uw.paxos;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.uw.paxos.connection.UDPClient;
import com.uw.paxos.messages.ClientMessage;
import com.uw.paxos.messages.ClientMessageType;
import com.uw.paxos.messages.Request;
import com.uw.paxos.messages.Response;
import com.uw.paxos.utils.Utils;

public class LockClient {
	
	private UDPClient udpClient;
	
	private InetAddress serverAddress;
	private int serverPort;
	
	
	public LockClient(String hostname, int port) {
		udpClient = new UDPClient();
		udpClient.connect();
		serverPort = port;
		
		try {
			serverAddress = InetAddress.getByName(hostname);
		} catch (UnknownHostException ex) {
			Utils.logError("Error occured while fetching server's address. Error : " + ex.getMessage());
		}
	}
	
	public boolean acquire(int lockId) {
		ClientMessage clientMessage = new ClientMessage();
		clientMessage.setMessageType(ClientMessageType.LOCK_REQUEST);
		clientMessage.setLockId(lockId);
		
		// Construct request object
		Request request = new Request();
		request.setServerIpAddress(serverAddress);
		request.setServerPort(serverPort);
		request.setMessage(clientMessage.toString());
		
		udpClient.sendRequest(request);
		Response response = udpClient.receiveResponse();
		ClientMessage receivedMessage = ClientMessage.fromString(response.getMessage());
		
		// Verify that lock request was successful
		if (receivedMessage.getMessageType() == ClientMessageType.LOCK_GRANTED) {
			return true;
		}
		
		return false;
	}
	
	public boolean release(int lockId) {
		ClientMessage clientMessage = new ClientMessage();
		clientMessage.setMessageType(ClientMessageType.UNLOCK_REQUEST);
		clientMessage.setLockId(lockId);
		
		// Construct request object
		Request request = new Request();
		request.setServerIpAddress(serverAddress);
		request.setServerPort(serverPort);
		request.setMessage(clientMessage.toString());
		
		udpClient.sendRequest(request);
		Response response = udpClient.receiveResponse();
		ClientMessage receivedMessage = ClientMessage.fromString(response.getMessage());
		
		// Verify that lock request was successful
		if (receivedMessage.getMessageType() == ClientMessageType.LOCK_RELEASED) {
			return true;
		}
		
		return false;
	}

}
