package com.uw.paxos.roles;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import com.uw.paxos.LockServerMain;
import com.uw.paxos.connection.Request;
import com.uw.paxos.connection.Response;
import com.uw.paxos.connection.Server;
import com.uw.paxos.connection.UDPUnicastServer;
import com.uw.paxos.messages.ClientMessage;
import com.uw.paxos.messages.ClientMessageType;
import com.uw.paxos.messages.ProposerLearnerMessage;
import com.uw.paxos.messages.ProposerLearnerMessageType;
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
	private Server server;
	private Proposer proposer;
	
	public ProposerThread(BlockingQueue<ClientMessage> clientRequestQueue, int portNumber) {
		this.clientRequestQueue = clientRequestQueue;
        this.server = new UDPUnicastServer(portNumber);
        this.proposer = new Proposer(server);
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
			
			// Valid request, process accordingly
	    	processRequest(message);
		}
    }
	
	@Override
	public void requestShutdown() {
		proposer.requestShutdown();
		super.requestShutdown();
	}
	
	private void processRequest(ClientMessage clientMessage) {
		switch (clientMessage.getMessageType()) {
		case LOCK_ACQUIRE_REQUEST:
			// Start Paxos to acquire the lock
			// Blocking call until consensus is reached			
			proposer.startPaxosAlgorithm(clientMessage);
			
			sendMessageToLearners(clientMessage, ProposerLearnerMessageType.LOCK_ACQUIRE);
			receiveAndHandleLearnerResponse(clientMessage);
			break;
		case LOCK_RELEASE_REQUEST:
			sendMessageToLearners(clientMessage, ProposerLearnerMessageType.LOCK_RELEASE);
			receiveAndHandleLearnerResponse(clientMessage);
			break;
		default:
			// Do nothing
			break;
		}
	}
	
	private void sendMessageToLearners(ClientMessage clientMessage, ProposerLearnerMessageType messageType) {
        ProposerLearnerMessage message = new ProposerLearnerMessage();
        message.setLockId(clientMessage.getLockId());
        message.setMessageType(messageType);
        message.setClientId(clientMessage.getClientId());
        
        Response response = generateResponseForLearner(message);
        
        Utils.logMessage(this.getClass().getSimpleName() + " sending multicast to learners : " + message);
        
        server.sendResponse(response);
	}
	
	private Response generateResponseForLearner(
            ProposerLearnerMessage message) {
	    Response response = new Response();
		response.setReceiverIpAddress(LearnerThread.LEARNER_GROUP_ADDRESS);
		response.setReceiverPort(LearnerThread.LEARNER_GROUP_PORT);
		response.setMessage(message.toString());
	    return response;
    }
	
	private void receiveAndHandleLearnerResponse(ClientMessage clientMessage) {
		try {
			// Confirm that all learners has agreed
			ProposerLearnerMessage agreedOnMessage = getAgreementMessage();
			Utils.logMessage(this.getClass().getSimpleName() + " received agreement on : " + agreedOnMessage);
			
			switch (agreedOnMessage.getMessageType()) {
			case LOCK_ACQUIRED:
				sendClientMessage(clientMessage, ClientMessageType.LOCK_GRANTED);
				break;
			case LOCK_RELEASED:
				sendClientMessage(clientMessage, ClientMessageType.LOCK_RELEASED);
				
				// Check if lock has new owner
				if (agreedOnMessage.getNewOwnerClientId() != null) {
					// Send confirmation to new lock owner
					ClientMessage newOwnerMessage = new ClientMessage();
					newOwnerMessage.setClientId(agreedOnMessage.getNewOwnerClientId());
					newOwnerMessage.setLockId(agreedOnMessage.getLockId());
					sendClientMessage(newOwnerMessage, ClientMessageType.LOCK_RELEASED);
				}
				break;
			case LOCK_QUEUED:
				// Do nothing
				break;
			case LOCK_UPDATE_FAILED:
				sendClientMessage(clientMessage, ClientMessageType.REQUEST_DENIED);
				break;
			default:
				break;
			}
		} catch (RuntimeException ex) {
			Utils.logError("Error while updating lock. Learners are in disagreement for lock " + clientMessage.getLockId()
					+ " request by : " + clientMessage);
			sendClientMessage(clientMessage, ClientMessageType.REQUEST_DENIED);
		}
    }
	
	private ProposerLearnerMessage getAgreementMessage() {
		Map<ProposerLearnerMessage, Integer> messageMap = new HashMap<>();
		int acceptanceCriteriaCount = (LockServerMain.SERVERS_IN_QUORUM / 2) + 1;
		
		for (int i = 0; i < LockServerMain.SERVERS_IN_QUORUM; i++) {
			Request request = server.receiveRequest();
			if (request != null) {
				Utils.logError(request.getMessage());
				ProposerLearnerMessage message = ProposerLearnerMessage.fromString(request.getMessage());
				if (messageMap.containsKey(message)) {
					Integer count = messageMap.get(message);
					messageMap.put(message, count + 1);
				} else {
					messageMap.put(message, 1);
				}
			}
		}
		
		for (ProposerLearnerMessage message: messageMap.keySet()) {
			if (messageMap.get(message) >= acceptanceCriteriaCount) {
				return message;
			}
		}
		
		throw new RuntimeException();
	}

	private void sendClientMessage(ClientMessage clientMessage, ClientMessageType messageType) {
		ClientMessage confirmationMessage = new ClientMessage();
		confirmationMessage.setLockId(clientMessage.getLockId());
		confirmationMessage.setMessageType(messageType);
		
		Response response = generateResponseForClientFromClientMessage(clientMessage,
                confirmationMessage);
		server.sendResponse(response);
	}
	
	private Response generateResponseForClientFromClientMessage(
            ClientMessage clientMessage, ClientMessage confirmationMessage) {
	    Response response = new Response();
		response.setReceiverIpAddress(clientMessage.getClientId().getClientAddress());
		response.setReceiverPort(clientMessage.getClientId().getClientPort());
		response.setMessage(confirmationMessage.toString());
	    return response;
    }
}
