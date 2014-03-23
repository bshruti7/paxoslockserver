package com.uw.paxos.roles;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.uw.paxos.connection.Request;
import com.uw.paxos.connection.Response;
import com.uw.paxos.connection.Server;
import com.uw.paxos.connection.UDPMulticastServer;
import com.uw.paxos.locks.DistributedLocks;
import com.uw.paxos.locks.Lock;
import com.uw.paxos.messages.ClientId;
import com.uw.paxos.messages.ProposerLearnerMessage;
import com.uw.paxos.messages.ProposerLearnerMessageType;
import com.uw.paxos.utils.Utils;

/**
 * Learner class that can be run as single thread.
 * This class starts a UDP server and listens to requests from proposers  
 * on the port specified in constructor. 
 * 
 * @author Samiksha Sharma
 *
 */
public class LearnerThread extends StoppableLoopThread {
	
	public final static String LEARNER_GROUP_ADDRESS_STRING = "224.2.2.5";
	public final static int LEARNER_GROUP_PORT = 8001;
	
	public static InetAddress LEARNER_GROUP_ADDRESS;
	
	private DistributedLocks locks;
	private Server server;
	
	// Static block to initialize static objects
	static {
		try {
	        LEARNER_GROUP_ADDRESS = InetAddress.getByName(LEARNER_GROUP_ADDRESS_STRING);
        } catch (UnknownHostException ex) {
        	Utils.logError("Unable to get InetAddress for IP address " + LEARNER_GROUP_ADDRESS_STRING + ". Error : " + ex.getMessage());
        } 
	}
	
	public LearnerThread() {
		this.locks = new DistributedLocks();
        this.server = new UDPMulticastServer(LEARNER_GROUP_ADDRESS, LEARNER_GROUP_PORT);
        ((UDPMulticastServer) this.server).joinMulticastGroup();
	}
	
	@Override
    public void doProcessing() {
		// Receive request multicasted to learner's group
		Request request = server.receiveRequest();
		
		if (request != null) {
			Utils.logMessage(this.getClass().getSimpleName() + " received multicast message : " + request.getMessage());
			processRequest(request);
		}	    
    }

	private void processRequest(Request request) {
		Response response = null;
		
		ProposerLearnerMessage proposerLearnerMessage = ProposerLearnerMessage.fromString(request.getMessage());
		Lock lock = locks.getLock(proposerLearnerMessage.getLockId());
		
		try { 
			// Check if Proposer ordered to acquire the lock
			switch(proposerLearnerMessage.getMessageType()) {
			case LOCK_ACQUIRE:
				ProposerLearnerMessageType returnMessageType = 
						ProposerLearnerMessageType.LOCK_UPDATE_FAILED; // Default		
				if (!lock.isAcquired()) {
					lock.acquire(proposerLearnerMessage.getClientId());
					returnMessageType = ProposerLearnerMessageType.LOCK_ACQUIRED;
				} else {
					lock.addClientToQueue(proposerLearnerMessage.getClientId());
					returnMessageType = ProposerLearnerMessageType.LOCK_QUEUED;
				}
				response = createResponseForProposer(request, proposerLearnerMessage, 
						null, returnMessageType);
				break;
				
			case LOCK_RELEASE:
				ClientId newOwnerClient = lock.release(proposerLearnerMessage.getClientId());
				response = createResponseForProposer(request, proposerLearnerMessage, 
						newOwnerClient, ProposerLearnerMessageType.LOCK_RELEASED);
				break;
			default:
				// Do nothing
				break;
			}
		} catch (RuntimeException ex) {
			Utils.logError(ex.getMessage());
		}
		if (response != null) {
			Utils.logMessage(this.getClass().getSimpleName() + " sending reply to proposer : " + response.getMessage());
			server.sendResponse(response);
		}
	}

	private Response createResponseForProposer(Request request, ProposerLearnerMessage proposerLearnerMessage, 
			ClientId newOwnerClient, ProposerLearnerMessageType paxosMessageType) {
	    // Send confirmation to the sender
	    ProposerLearnerMessage confirmationMessage = new ProposerLearnerMessage();
	    confirmationMessage.setClientId(proposerLearnerMessage.getClientId());
	    confirmationMessage.setLockId(proposerLearnerMessage.getLockId());
	    confirmationMessage.setMessageType(paxosMessageType);
	    confirmationMessage.setNewOwnerClientId(newOwnerClient);
	    
	    // Construct request
	    Response response = new Response();
	    response.setMessage(confirmationMessage.toString());
	    response.setReceiverIpAddress(request.getSenderIpAddress());
	    response.setReceiverPort(request.getSenderPort());
	    return response;
    }
}
