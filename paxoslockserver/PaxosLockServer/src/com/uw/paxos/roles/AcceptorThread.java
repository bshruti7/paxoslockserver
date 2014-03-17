package com.uw.paxos.roles;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.uw.paxos.connection.Request;
import com.uw.paxos.connection.Response;
import com.uw.paxos.connection.Server;
import com.uw.paxos.connection.UDPMulticastServer;
import com.uw.paxos.locks.Lock;
import com.uw.paxos.messages.PaxosMessage;
import com.uw.paxos.messages.PaxosMessageType;
import com.uw.paxos.utils.Utils;

/**
 * Acceptor class that can be run as single thread.
 * This class starts a UDP server and listens to requests from proposers
 * on the port specified in constructor.
 * 
 * @author Shruti
 *
 */
public class AcceptorThread extends StoppableLoopThread {
	
	public final static String ACCEPTOR_GROUP_ADDRESS_STRING = "224.2.2.4";
	public final static int ACCEPTOR_GROUP_PORT = 8000;
	
	public static InetAddress ACCEPTOR_GROUP_ADDRESS;
	
	private Server server;
	
	// Static block to initialize static objects
	static {
		try {
			ACCEPTOR_GROUP_ADDRESS = InetAddress.getByName(ACCEPTOR_GROUP_ADDRESS_STRING);
        } catch (UnknownHostException ex) {
        	Utils.logError("Unable to get InetAddress for IP address " + ACCEPTOR_GROUP_ADDRESS_STRING + ". Error : " + ex.getMessage());
        } 
	}
	
	public AcceptorThread() {
		System.out.println("Constructor of Acceptor Thread");
		this.server = new UDPMulticastServer(ACCEPTOR_GROUP_ADDRESS, ACCEPTOR_GROUP_PORT);
		((UDPMulticastServer) this.server).joinMulticastGroup();
	}

	
	@Override
    public void doProcessing() {
		//System.out.println(" Acceptor thread doProcessing");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Receive request multicasted to acceptor's group
		Request request = server.receiveRequest();
		//System.out.println("Request has "+request.getSenderPort()+","+request.getMessage()+","+request.getSenderIpAddress());
		//System.out.println("Request in acceptor thread has "+request.getSenderPort());
		if (request != null) {
			//Utils.logMessage(this.getClass().getSimpleName() + " received command: " + request.getMessage());
			//System.out.println(" Request not null");
			Utils.logMessage(this.getClass().getSimpleName() + " received multicast message : " + request.getMessage());
			PaxosMessage paxosMessage = PaxosMessage.fromString(request.getMessage());
			// Check id an all the stuff required for Paxos and send request accordingly.
			if (paxosMessage.getMessageType() == PaxosMessageType.PROMISE) {
				
			}
		}
		else{
			//System.out.println(" Acceptor thread gets null request");
		}
	}
	
	
	/*
	 * private Response createResponseForProposer(Request request,
            PaxosMessage paxosMessage, PaxosMessageType paxosMessageType) {
	    // Send confirmation to the sender
	    PaxosMessage confirmationMessage = new PaxosMessage();
	    confirmationMessage.setClientId(paxosMessage.getClientId());
	    confirmationMessage.setLockId(paxosMessage.getLockId());
	    confirmationMessage.setMessageType(paxosMessageType);
	    
	    // Construct request
	    Response response = new Response();
	    response.setMessage(confirmationMessage.toString());
	    response.setReceiverIpAddress(request.getSenderIpAddress());
	    response.setReceiverPort(request.getSenderPort());
	    return response;
    }
    */
	 
}