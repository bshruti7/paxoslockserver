package com.uw.paxos.roles;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.uw.paxos.connection.Request;
import com.uw.paxos.connection.Response;
import com.uw.paxos.connection.Server;
import com.uw.paxos.connection.UDPMulticastServer;
import com.uw.paxos.locks.DistributedLocks;
import com.uw.paxos.locks.Lock;
import com.uw.paxos.messages.PaxosMessage;
import com.uw.paxos.messages.PaxosMessageType;
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
	
	public LearnerThread(DistributedLocks locks) {
		this.locks = locks;
        this.server = new UDPMulticastServer(LEARNER_GROUP_ADDRESS, LEARNER_GROUP_PORT);
        ((UDPMulticastServer) this.server).joinMulticastGroup();
	}
	
	@Override
    public void doProcessing() {
		// Receive request multicasted to learner's group
		Request request = server.receiveRequest();
		//System.out.println("Request has "+request.getSenderPort()+","+request.getMessage()+",");
		
		if (request != null) {
	    	Utils.logMessage(this.getClass().getSimpleName() + " received multicast message : " + request.getMessage());
	    	PaxosMessage paxosMessage = PaxosMessage.fromString(request.getMessage());
			
	    	// Get lock
			Lock lock = locks.getLock(paxosMessage.getLockId());
			
			try { 
				// Check if Proposer ordered to acquire the lock
				if (paxosMessage.getMessageType() == PaxosMessageType.LOCK_ACQUIRE) {
					lock.acquire(paxosMessage.getClientId());
				}
				
				// Check if Proposer ordered to release the lock
				if (paxosMessage.getMessageType() == PaxosMessageType.LOCK_RELEASE) {
					lock.release(paxosMessage.getClientId());
				}
				
				Response response = createResponseForProposer(request,
                        paxosMessage, PaxosMessageType.LOCK_UPDATE_CONFIRMATION);
				
				server.sendResponse(response);
			} catch (RuntimeException ex) {
				Utils.logError(ex.getMessage());
				Response response = createResponseForProposer(request,
                        paxosMessage, PaxosMessageType.LOCK_UPDATE_FAILED);
				server.sendResponse(response);
			}
		}	    
    }

	private Response createResponseForProposer(Request request,
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
}
