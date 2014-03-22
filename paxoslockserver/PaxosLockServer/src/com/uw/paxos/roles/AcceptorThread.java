package com.uw.paxos.roles;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.uw.paxos.connection.Request;
import com.uw.paxos.connection.Response;
import com.uw.paxos.connection.Server;

import com.uw.paxos.connection.UDPMulticastServer;

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
	private int highestProposalNumberSeenByAcceptor=0;
	
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
			
			e.printStackTrace();
		}
		
		// Receive request multi-casted to acceptor's group
		Request request = server.receiveRequest();
		
		if (request != null) {
			Utils.logMessage(this.getClass().getSimpleName() + " received multicast message : " + request.getMessage());
			PaxosMessage paxosMessage = PaxosMessage.fromString(request.getMessage());
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
			//(paxosMessage.getMessageType() == PaxosMessageType.PREPARE) && 
			//highestProposalNumberSeenByAcceptor=100; use this command to check the case of nack
			if ((highestProposalNumberSeenByAcceptor < paxosMessage.getProposalNumber())){
				highestProposalNumberSeenByAcceptor=paxosMessage.getProposalNumber();
			System.out.println("creating the promise message...");
			createPromiseForProposer(request, paxosMessage);
			}
			else{
				System.out.println("creating the nack message...");
				createNackForProposer(request, paxosMessage);
			}
		
		}
		
		else{
			System.out.println(" Acceptor thread gets null request");
		}
	}
	
	
	
	 private void createNackForProposer(Request request,
			PaxosMessage paxosMessage) {
		 	
		 PaxosMessage confirmationMessage = new PaxosMessage();
		    confirmationMessage.setClientId(paxosMessage.getClientId());
		    confirmationMessage.setLockId(paxosMessage.getLockId());
		    confirmationMessage.setMessageType(PaxosMessageType.NACK_ON_PREPARE);
		    confirmationMessage.setProposalNumber(highestProposalNumberSeenByAcceptor);	
		    Response responseFromAcceptor = new Response();
		    responseFromAcceptor.setMessage(confirmationMessage.toString());
		    responseFromAcceptor.setReceiverIpAddress(request.getSenderIpAddress());
		    responseFromAcceptor.setReceiverPort(request.getSenderPort());
		    server.sendResponse(responseFromAcceptor);
		    
		
	}


	private void createPromiseForProposer(Request request,PaxosMessage paxosMessage) {
	    // Send confirmation to the sender
	    PaxosMessage confirmationMessage = new PaxosMessage();
	    confirmationMessage.setClientId(paxosMessage.getClientId());
	    confirmationMessage.setLockId(paxosMessage.getLockId());
	    confirmationMessage.setMessageType(PaxosMessageType.PROMISE);
	    confirmationMessage.setProposalNumber(highestProposalNumberSeenByAcceptor);	
	   /*
	    Response responseFromAcceptor = new Response();
	    responseFromAcceptor.setMessage(paxosMessage.toString());
	    responseFromAcceptor.setReceiverIpAddress(request.getSenderIpAddress());
	    responseFromAcceptor.setReceiverPort(request.getSenderPort());
	   */
	    Response responseFromAcceptor = new Response();
	    responseFromAcceptor.setMessage(confirmationMessage.toString());
	    responseFromAcceptor.setReceiverIpAddress(request.getSenderIpAddress());
	    responseFromAcceptor.setReceiverPort(request.getSenderPort());
	    //UDPClient udpClient=new UDPClient();
	   server.sendResponse(responseFromAcceptor);
    }
    
	 
}