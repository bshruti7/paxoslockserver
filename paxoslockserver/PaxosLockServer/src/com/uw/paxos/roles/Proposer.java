package com.uw.paxos.roles;

import java.util.HashMap;
import java.util.Map;

import com.uw.paxos.LockServerMain;
import com.uw.paxos.connection.Request;
import com.uw.paxos.connection.Response;
import com.uw.paxos.connection.Server;
import com.uw.paxos.messages.ClientMessage;
import com.uw.paxos.messages.ProposerAcceptorMessage;
import com.uw.paxos.messages.ProposerAcceptorMessageType;
import com.uw.paxos.utils.Utils;

/**
 * Class to start Paxos and reach consensus
 * 
 * @author Aditi
 *
 */

public class Proposer{
	private Server server;
	private int proposalNumber = 0;

	public Proposer(Server server) {
		this.server = server;
	}

	/**
	 * This method initiates the paxos algorithm to get consensus if the requested lock can be granted to the
	 * requesting client.
	 * @param clientMessage 
	 */
	public void startPaxosAlgorithm(ClientMessage clientMessage) {
		boolean hasAgreement = false;
		
		// Generate proposalNumber
		proposalNumber++;
		
		while (!hasAgreement) {
			while (!hasAgreement) {
				Utils.logMessage("Starting Paxos with current proposal number : " + proposalNumber);
				
				sendMessageToAcceptors(clientMessage, ProposerAcceptorMessageType.PREPARE);
				
				// Get Acceptors decision
				ProposerAcceptorMessage agreedOnMessage = getAgreementMessage(clientMessage);
				
				if (agreedOnMessage != null) {
					if (agreedOnMessage.getMessageType() == ProposerAcceptorMessageType.PROMISE) {
						Utils.logMessage(this.getClass().getSimpleName() + " received agreement on : " + agreedOnMessage);
						hasAgreement = true;
					} else {
						incrementProposalNumberAndSleepRandomly();
					}
				} else {
					incrementProposalNumberAndSleepRandomly();
				}
			}
			
			// We have Promise from all acceptors, so send them accept message
			hasAgreement = false;
			sendMessageToAcceptors(clientMessage, ProposerAcceptorMessageType.ACCEPT);
			
			// Get Acceptors decision
			ProposerAcceptorMessage agreedOnMessage = getAgreementMessage(clientMessage);
			if (agreedOnMessage != null) {
				if (agreedOnMessage.getMessageType() == ProposerAcceptorMessageType.ACCEPT_CONFIRMATION) {
					Utils.logMessage(this.getClass().getSimpleName() + " has agreement : " + agreedOnMessage);
					hasAgreement = true;
				} else {
					incrementProposalNumberAndSleepRandomly();
				}
			} else {
				incrementProposalNumberAndSleepRandomly();
			}
		}
		
		// Consensus achieved
	}

	private void incrementProposalNumberAndSleepRandomly() {
		proposalNumber++;
		
		// Wait for random millis before making another request
		try {
			Thread.sleep((long) (Math.random() * 5000));
		} catch (InterruptedException e) {
			Utils.logError(e.getMessage());
		}
	}
	
	/**
	 * Multicast to acceptors
	 * 
	 * @param clientMessage ClientMessage for which this Paxos is running
	 * @param messageType MessageType
	 */
	private void sendMessageToAcceptors(ClientMessage clientMessage, ProposerAcceptorMessageType messageType) {
	    ProposerAcceptorMessage paxosMessage = createMessageForAcceptor(clientMessage, messageType);
	    Response response = generateResponseForAcceptor(paxosMessage);
	    
	    Utils.logMessage(this.getClass().getSimpleName() + " sending multicast message : " + paxosMessage);
	    server.sendResponse(response);
	}

	private ProposerAcceptorMessage createMessageForAcceptor(
			ClientMessage clientMessage, ProposerAcceptorMessageType messageType) {
		ProposerAcceptorMessage paxosMessage =  new ProposerAcceptorMessage();
	    paxosMessage.setProposalNumber(proposalNumber);
	    paxosMessage.setMessageType(messageType);
	    paxosMessage.setClientId(clientMessage.getClientId());
	    paxosMessage.setLockId(clientMessage.getLockId());
		return paxosMessage;
	}
	
	private Response generateResponseForAcceptor(ProposerAcceptorMessage paxosMessage) {
		Response request = new Response();
		request.setReceiverIpAddress(AcceptorThread.ACCEPTOR_GROUP_ADDRESS);
		request.setReceiverPort(AcceptorThread.ACCEPTOR_GROUP_PORT);
		request.setMessage(paxosMessage.toString());
		return request;
	}
	
	private ProposerAcceptorMessage getAgreementMessage(ClientMessage clientMessage) {
		Map<ProposerAcceptorMessage, Integer> messageMap = new HashMap<>();
		int acceptanceCriteriaCount = (LockServerMain.SERVERS_IN_QUORUM / 2) + 1;
		
		for (int i = 0; i < LockServerMain.SERVERS_IN_QUORUM; i++) {
			Request request = server.receiveRequest();
			if (request != null) {
				ProposerAcceptorMessage message = ProposerAcceptorMessage.fromString(request.getMessage());
				if (messageMap.containsKey(message)) {
					Integer count = messageMap.get(message);
					messageMap.put(message, count + 1);
				} else {
					messageMap.put(message, 1);
				}
			}
		}
		
		for (ProposerAcceptorMessage message: messageMap.keySet()) {
			if (messageMap.get(message) >= acceptanceCriteriaCount) {
				return message;
			}
		}
		
		return null;
	}	
}
