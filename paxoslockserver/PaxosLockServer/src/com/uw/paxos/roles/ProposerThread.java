package com.uw.paxos.roles;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import com.uw.paxos.Proposer;
import com.uw.paxos.connection.Response;
import com.uw.paxos.connection.Server;
import com.uw.paxos.connection.UDPServer;
import com.uw.paxos.locks.DistributedLocks;
import com.uw.paxos.locks.Lock;
import com.uw.paxos.messages.ClientId;
import com.uw.paxos.messages.ClientMessage;
import com.uw.paxos.messages.ClientMessageType;
import com.uw.paxos.utils.Utils;

/**
 * Proposer class that can be run as single thread.
 * This class starts a UDP server and listens to requests from clients
 * on the port specified in constructor.
 * This class handles following requests from clients:
 * - AcquireLock
 * - ReleaseLock
 * 
 * @author Samiksha Sharma
 *
 */
public class ProposerThread extends StoppableLoopThread {
	
	private BlockingQueue<ClientMessage> clientRequestQueue;
	private DistributedLocks locks;
	private Server server;
	private int proposalNumber;
	private int proposalID;
	
	public ProposerThread(BlockingQueue<ClientMessage> clientRequestQueue, DistributedLocks locks, int portNumber) {
		this.locks = locks;
		this.clientRequestQueue = clientRequestQueue;
		try {
	        server = new UDPServer(portNumber);
        } catch (IOException ex) {
	        Utils.logError("Unable to bind to port : " + portNumber + ". Exception: " + ex.getMessage());
        }
	}
	
	@Override
    public void doProcessing() {
		ClientMessage message = null;
		try {
			// Blocking call until an element is available in the queue.
			// ClientRequestAcceptorThread puts a dummy element in this queue
			// to wake ProposerThread up after maximum of 15 seconds.
			message = clientRequestQueue.take();
		} catch (InterruptedException ex) {
			Utils.logError(this.getClass().getSimpleName() + " encountered error while fetching client request from queue. \nError : " + ex.getMessage());
		}
		
		if (message.getMessageType() != ClientMessageType.DUMMY_REQUEST) {
			Utils.logMessage(this.getClass().getSimpleName() + " picked up request from queue : " + message.toString());
	    	processRequest(message);
		}
    }
	
	private void processRequest(ClientMessage clientMessage) {
		// Valid Request, process accordingly
		if (clientMessage.getMessageType() == ClientMessageType.UNLOCK_REQUEST) {
			// Check if lock is acquired and is acquired by this client
			Lock lock = locks.getLock(clientMessage.getLockId());
			
			if (lock.isAcquired() && lock.getAcquiredBy().equals(clientMessage.getClientId())) {
				lock.release(clientMessage.getClientId()); // This code will go to learner
				
				// Multicast to Learners with unlock command
				sendSuccess(clientMessage, false);
				
				// Check for other lock in the lock queue
				// ClientId clientId = lock.getNextWaitingClient();
				
				//if (clientId != null) {
					// Run Paxos on clientId as the lock is available now
				//}
			} else {
				sendFailure(clientMessage);
			}
		} else {
			//The client has sent out a lock request.
			//Check if the requested lock is  available in the distributed lock table.
			Lock lock = locks.getLock(clientMessage.getLockId());
			
			if(lock.isAcquired()){
				// Add to lock waiting queue and conclude this request. 
				// This client's request will be taken care of when client 
				// holding this lock unlocks.
				lock.addWaitingClient(clientMessage.getClientId());
			}
			else {
				// Parse and act on request
				// Proposer proposer = new Proposer();
				// proposalNumber = proposalNumber+1;
				// proposer.startPaxosAlgorithm(clientMessage,proposalNumber);
				
				lock.acquire(clientMessage.getClientId()); // This code will go to learner
				
				sendSuccess(clientMessage, true);
			}
		}
	}
	
	private void sendSuccess(ClientMessage clientMessage, boolean locked) {
		ClientMessage confirmationMessage = new ClientMessage();
		confirmationMessage.setLockId(clientMessage.getLockId());
		if (locked) {
			confirmationMessage.setMessageType(ClientMessageType.LOCK_GRANTED);
		} else {
			confirmationMessage.setMessageType(ClientMessageType.LOCK_RELEASED);
		}
		
		Response response = new Response();
		response.setReceiverIpAddress(clientMessage.getClientId().getClientAddress());
		response.setReceiverPort(clientMessage.getClientId().getClientPort());
		response.setMessage(confirmationMessage.toString());
		server.sendResponse(response);
	}
	
	private void sendFailure(ClientMessage clientMessage) {
		ClientMessage confirmationMessage = new ClientMessage();
		confirmationMessage.setLockId(clientMessage.getLockId());
		confirmationMessage.setMessageType(ClientMessageType.REQUEST_DENIED);
		
		Response response = new Response();
		response.setReceiverIpAddress(clientMessage.getClientId().getClientAddress());
		response.setReceiverPort(clientMessage.getClientId().getClientPort());
		response.setMessage(confirmationMessage.toString());
		server.sendResponse(response);
	}
}
