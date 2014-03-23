package com.uw.paxos.roles;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.uw.paxos.connection.Request;
import com.uw.paxos.connection.Response;
import com.uw.paxos.connection.Server;

import com.uw.paxos.connection.UDPMulticastServer;
import com.uw.paxos.messages.ProposerAcceptorMessage;
import com.uw.paxos.messages.ProposerAcceptorMessageType;
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
	private int highestProposalNumberSeen;
	
	// Static block to initialize static objects
	static {
		try {
			ACCEPTOR_GROUP_ADDRESS = InetAddress.getByName(ACCEPTOR_GROUP_ADDRESS_STRING);
        } catch (UnknownHostException ex) {
        	Utils.logError("Unable to get InetAddress for IP address " + ACCEPTOR_GROUP_ADDRESS_STRING + ". Error : " + ex.getMessage());
        } 
	}
	
	public AcceptorThread() {
		this.highestProposalNumberSeen = 0;
		this.server = new UDPMulticastServer(ACCEPTOR_GROUP_ADDRESS, ACCEPTOR_GROUP_PORT);
		((UDPMulticastServer) this.server).joinMulticastGroup();
	}

	
	@Override
    public void doProcessing() {
		// Receive request multicasted to acceptor's group
		Request request = server.receiveRequest();
		
		if (request != null) {
			Utils.logMessage(this.getClass().getSimpleName() + " received multicast message : " + request.getMessage());
			processRequest(request);
		}
	}
	
    private void processRequest(Request request) {
    	Response response = null;
    	
    	ProposerAcceptorMessage message = ProposerAcceptorMessage.fromString(request.getMessage());
    	
    	switch (message.getMessageType()) {
    	case PREPARE:
    		if (message.getProposalNumber() > highestProposalNumberSeen) {
    			highestProposalNumberSeen = message.getProposalNumber();
    			response = createResponseForProposer(request, message, ProposerAcceptorMessageType.PROMISE);
    		} else {
    			response = createResponseForProposer(request, message, ProposerAcceptorMessageType.NACK_ON_PREPARE);
    		}
    		break;
    	case ACCEPT:
    		if (message.getProposalNumber() == highestProposalNumberSeen) {
    			response = createResponseForProposer(request, message, ProposerAcceptorMessageType.ACCEPT_CONFIRMATION);
    		} else {
    			response = createResponseForProposer(request, message, ProposerAcceptorMessageType.NACK_ON_ACCEPT);
    		}
    		break;
		default:
			// Do nothing
			break;
    	}
    	
    	if (response != null) {
    		Utils.logMessage(this.getClass().getSimpleName() + " sending reply to proposer : " + response.getMessage());
    		server.sendResponse(response);
    	}
	}

	private Response createResponseForProposer(Request request,
            ProposerAcceptorMessage message, ProposerAcceptorMessageType paxosMessageType) {
	    // Send confirmation to the sender
	    ProposerAcceptorMessage confirmationMessage = new ProposerAcceptorMessage();
	    confirmationMessage.setClientId(message.getClientId());
	    confirmationMessage.setLockId(message.getLockId());
	    confirmationMessage.setProposalNumber(message.getProposalNumber());
		confirmationMessage.setHighestProposalNumberSeen(highestProposalNumberSeen);
		confirmationMessage.setMessageType(paxosMessageType);

	    // Construct request
	    Response response = new Response();
	    response.setMessage(confirmationMessage.toString());
	    response.setReceiverIpAddress(request.getSenderIpAddress());
	    response.setReceiverPort(request.getSenderPort());
	    return response;
    }
}