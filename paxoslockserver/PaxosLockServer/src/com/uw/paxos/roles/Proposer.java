//This file is not being used any more

package com.uw.paxos.roles;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.uw.paxos.connection.Request;
import com.uw.paxos.connection.Response;
import com.uw.paxos.connection.Server;
import com.uw.paxos.connection.UDPClient;
import com.uw.paxos.connection.UDPUnicastServer;
import com.uw.paxos.messages.ClientMessage;
import com.uw.paxos.messages.PaxosMessage;
import com.uw.paxos.messages.PaxosMessageType;

/**
 * This class performs the roles of a proposer and a learner
 * 
 * @author Aditi
 *
 */

//Questions: 
//proposalId: can be the server ip address. A server controller may be able to set it. But how and where?

// proposalNnumber : what should the proposalNumber be initialized to? May be to 0 or say 100?
//Should this be some random value or start sequentially incrementing from some number say 1.

//What if unlocking fails? how to handle it ?

//if lock not available then we send reply to client informing it about it?

//How to simulate this condition where this proposer gets replies from all the acceptors and then checks if quorum obtained or not?
//especially from the connectivity point of view. Not able to think of right way to capture promises and then check quorum

//anyu format for ACK OR NACK as a reply to accept message.

//Hoo to handles nack on accept request being rejected.


public class Proposer{
//	private String proposerId;
//	private int proposalNumber=0; 
//	private Learner learner ;
////	private Request request;
	PaxosMessage paxosMessage ;
	private UDPClient serverListeningForAcceptor;
	
	
	
		public Proposer(Server serverListeningForAcceptor) {
		this.serverListeningForAcceptor = new UDPClient();
		
	}


		/**
	 * This method initiates the paxos algorithm to get consensus if the requested lock can be granted to the
	 * requesting client.
		 * @param server 
		 * @param server2 
	 * @param request
		 * @throws UnknownHostException 
	 */
	public void startPaxosAlgorithm(ClientMessage clientMessage, int proposalNumber, Server server) throws UnknownHostException {
		
		//generate proposalNumber
		//proposalNumber = proposalNumber + 1 ;
		System.out.println("The proposal number currently is "+proposalNumber);
		sendPrepareMessageToAcceptor(proposalNumber,clientMessage,server);
		
		checkQuorumForAcceptMessages();
		//How to simulate this condition where this proposer gets replies from all the acceptors and then checks if quorum obtained or not?
		//especially from the connectivity point of view. Not able to think of right way to capture promises and then check quorum
		boolean isQuorumObtained = checkQuoroum();
		if(!isQuorumObtained){//If quorum not obtained what do we do? 
			//send client the reponse that his rquest cannot be satisfied at this time. Please try later?
		}
		else{
			sendAcceptMessage();
			boolean isaAcceptedByQuorum = checkQuorumForAcceptMessages();
			if(isaAcceptedByQuorum)
				{
				//learner.lock(request2);
				}
			else{
				//Hoo to handles nack on accept request being rejected.
					}
			
		}
	}
	
	
	/**
	 * This method checks if a quorum has been achieved for the replies to the accept messages.
	 */
	private boolean checkQuorumForAcceptMessages() {
		//Check quorum for dummy messages.
		
		Response receivedFromAcceptor=new Response();
		receivedFromAcceptor = serverListeningForAcceptor.receiveResponse();		  
		System.out.println("GOt this from acceptor at proposer:"+receivedFromAcceptor);  
		
		
		return true;
	}

	
	/**
	 * This method generates an accept message and multicasts this message to all the acceptors.
	 */
	private void sendAcceptMessage() {
		// Send out a multicast message to all acceptor with an AcceptMessage
		//AcceptMessage acceptMessage = new AcceptMessage(proposerId, proposalNumber, "");
		
	
	}
	
	/**
	 * This method receives promises or NACKS from the acceptors and checks if a quorm is achieved.
	 */
	private boolean checkQuoroum() {
		//Code to check quorum
		return true;
	}

	/**
	 * Multicasts prepare messages to all the acceptors. 
	 * @param clientMessage 
	 * @param clientMessage 
	 * @param server 
	 * @throws UnknownHostException 
	 * 
	 */
	private void sendPrepareMessageToAcceptor(int proposalNumber, ClientMessage clientMessage, Server server) throws UnknownHostException {
	  PaxosMessage paxosMessage =  new PaxosMessage();
	  paxosMessage.setProposalNumber(proposalNumber);
	  paxosMessage.setMessageType(PaxosMessageType.PREPARE);
	  paxosMessage.setClientId(clientMessage.getClientId());
	  paxosMessage.setLockId(clientMessage.getLockId());
	  paxosMessage.setProposerId(InetAddress.getLocalHost());
	  Response response=generateResponseForAcceptor(paxosMessage);
	  System.out.println(" Proposer has to send to Acceptor at  " + response.getReceiverIpAddress()+","+response.getReceiverPort()+","+response.getMessage());
	  server.sendResponse(response);
	 // Response response = generateResponseForLearnerFromPaxosMessage(paxosMessage);
	  //server2.sendResponse(response);
	  // multicast.sendMulticast(paxosMessage);
	  
	}


	private Response generateResponseForAcceptor(PaxosMessage paxosMessage2) {
		// TODO Auto-generated method stub
		
		Response response = new Response();
		response.setReceiverIpAddress(AcceptorThread.ACCEPTOR_GROUP_ADDRESS);
		response.setReceiverPort(AcceptorThread.ACCEPTOR_GROUP_PORT);
		response.setMessage(paxosMessage2.toString());
		
		System.out.println(" resposne has all these " + response.getReceiverIpAddress()+","+response.getReceiverPort()+","+response.getMessage());
	    return response;
	}
	
	
	
	
}
