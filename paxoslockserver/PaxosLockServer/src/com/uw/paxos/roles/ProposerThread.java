package com.uw.paxos.roles;

import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;

import com.uw.paxos.connection.Request;
import com.uw.paxos.connection.Response;
import com.uw.paxos.connection.Server;
import com.uw.paxos.connection.UDPUnicastServer;
import com.uw.paxos.locks.DistributedLocks;
import com.uw.paxos.locks.Lock;
import com.uw.paxos.messages.ClientMessage;
import com.uw.paxos.messages.ClientMessageType;
import com.uw.paxos.messages.PaxosMessage;
import com.uw.paxos.messages.PaxosMessageType;
import com.uw.paxos.utils.Utils;

/**
 * Proposer class that can be run as single thread.
 * This class starts a UDP server and listens to requests from clients
 * on the port specified in constructor.
 * This class handles following requests from clients:
 * - AcquireLock
 * - ReleaseLock
 * 
 * @author Aditi
 *
 */
public class ProposerThread extends StoppableLoopThread {
	
	private BlockingQueue<ClientMessage> clientRequestQueue;
	private DistributedLocks locks;
	private Server server;
	protected int proposalNumber;
	private int proposalID;
	
	public ProposerThread(BlockingQueue<ClientMessage> clientRequestQueue, DistributedLocks locks, int portNumber) {
		this.locks = locks;
		this.clientRequestQueue = clientRequestQueue;
        this.server = new UDPUnicastServer(portNumber);
        this.proposalNumber=0;
      //  this.proposalID = this.get
	}
	
	@Override
    public void doProcessing() throws UnknownHostException {
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
	
	private void processRequest(ClientMessage clientMessage) throws UnknownHostException {
		// Valid Request, process accordingly
		if (clientMessage.getMessageType() == ClientMessageType.LOCK_RELEASE_REQUEST) {
			// Check if lock is acquired and is acquired by this client
			Lock lock = locks.getLock(clientMessage.getLockId());
			
			if (lock.isAcquired() && lock.getAcquiredBy().equals(clientMessage.getClientId())) {
				// lock.release(clientMessage.getClientId()); // This code will go to learner
				
				sendPaxosToLearner(clientMessage, PaxosMessageType.LOCK_RELEASE ); // This code will go to Proposer.java
				sendSuccessToClient(clientMessage, false);
				
				// Check for other lock in the lock queue
				// ClientId clientId = lock.getNextWaitingClient();
				
				//if (clientId != null) {
					// Run Paxos on clientId as the lock is available now
				//}
			} else {
				// Client does not own this lock, so deny lock_release request
				sendFailureToClient(clientMessage);
			}
		} else if (clientMessage.getMessageType() == ClientMessageType.LOCK_ACQUIRE_REQUEST) {
			//The client has sent out a lock request.
			//Check if the requested lock is  available in the distributed lock table.
			Lock lock = locks.getLock(clientMessage.getLockId());
			
			if(lock.isAcquired()){
				try {
					// Add to lock waiting queue and conclude this request. 
					// This client's request will be taken care of when client 
					// holding this lock unlocks.
					lock.addWaitingClient(clientMessage.getClientId());
				} catch (Exception ex) {
					Utils.logError(ex.getMessage());
					sendFailureToClient(clientMessage);
				}
			}
			else {
				// Parse and act on request (start paxos)
				 Proposer proposer = new Proposer();
				 proposalNumber = proposalNumber+1;
				 System.out.println("Client ID is "+clientMessage.getClientId());
				 proposer.startPaxosAlgorithm(clientMessage,proposalNumber,server);
				
				sendPaxosToLearner(clientMessage, PaxosMessageType.LOCK_ACQUIRE ); // This code will go to Proposer.java
				boolean isSuccessful = checkLearnerResponse();
				if (isSuccessful) {
					sendSuccessToClient(clientMessage, true);
				} else {
					sendFailureToClient(clientMessage);
					Utils.logError("Error occured while trying to secure lock for request " + clientMessage.toString());
				}
			}
		}
	}
	
	private void sendPaxosToLearner(ClientMessage clientMessage, PaxosMessageType paxosMessageType) {
		PaxosMessage paxosMessage = new PaxosMessage();
		paxosMessage.setLockId(clientMessage.getLockId());
		paxosMessage.setMessageType(paxosMessageType);
		paxosMessage.setClientId(clientMessage.getClientId());
		
		Response response = generateResponseForLearnerFromPaxosMessage(paxosMessage);
		server.sendResponse(response);
	}
	
	private void sendPaxosToAcceptor(ClientMessage clientMessage, PaxosMessageType paxosMessageType) {
		PaxosMessage paxosMessage = new PaxosMessage();
		paxosMessage.setLockId(clientMessage.getLockId());
		paxosMessage.setMessageType(paxosMessageType);
		paxosMessage.setClientId(clientMessage.getClientId());
		
		Response response = generateResponseForLearnerFromPaxosMessage(paxosMessage);
		server.sendResponse(response);
	}
	
	private void sendSuccessToClient(ClientMessage clientMessage, boolean locked) {
		ClientMessage confirmationMessage = new ClientMessage();
		confirmationMessage.setLockId(clientMessage.getLockId());
		if (locked) {
			confirmationMessage.setMessageType(ClientMessageType.LOCK_GRANTED);
		} else {
			confirmationMessage.setMessageType(ClientMessageType.LOCK_RELEASED);
		}
		
		Response response = generateResponseForClientFromClientMessage(clientMessage,
                confirmationMessage);
		server.sendResponse(response);
	}
	
	private void sendFailureToClient(ClientMessage clientMessage) {
		ClientMessage confirmationMessage = new ClientMessage();
		confirmationMessage.setLockId(clientMessage.getLockId());
		confirmationMessage.setMessageType(ClientMessageType.REQUEST_DENIED);
		
		Response response = generateResponseForClientFromClientMessage(clientMessage,
                confirmationMessage);
		server.sendResponse(response);
	}
	
	private Response generateResponseForLearnerFromPaxosMessage(
            PaxosMessage paxosMessage) {
	    Response response = new Response();
		response.setReceiverIpAddress(LearnerThread.LEARNER_GROUP_ADDRESS);
		response.setReceiverPort(LearnerThread.LEARNER_GROUP_PORT);
		response.setMessage(paxosMessage.toString());
	    return response;
    }
	/*
	private Response generateResponseForAcceptorFromPaxosMessage(
            PaxosMessage paxosMessage) {
	    Response response = new Response();
		response.setReceiverIpAddress(AcceptorThread.ACCEPTOR_GROUP_ADDRESS);
		response.setReceiverPort(AcceptorThread.ACCEPTOR_GROUP_PORT);
		response.setMessage(paxosMessage.toString());
	    return response;
    }
    */
	
	private Response generateResponseForClientFromClientMessage(
            ClientMessage clientMessage, ClientMessage confirmationMessage) {
	    Response response = new Response();
		response.setReceiverIpAddress(clientMessage.getClientId().getClientAddress());
		response.setReceiverPort(clientMessage.getClientId().getClientPort());
		response.setMessage(confirmationMessage.toString());
	    return response;
    }
	
	private boolean checkLearnerResponse() {
		Request request = server.receiveRequest();
		if (request != null) {
			PaxosMessage paxosMessage = PaxosMessage.fromString(request.getMessage());
			if (paxosMessage.getMessageType() == PaxosMessageType.LOCK_UPDATE_CONFIRMATION) {
				return true;
			}
		}
		
		return false;
    }
}
