package com.uw.paxos;
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


public class Proposer implements Runnable {
	private String proposerId;
	private int proposalNumber; 
	private Learner learner ;
	private RequestMessage request;
	
	public Proposer(RequestMessage message){
		this.request = message;	
	}
	
	public Proposer(){}
	
	@Override
	public void run() {
		System.out.println("Inside Proposer thread with name: " + Thread.currentThread().getName());
		processClientRequest(request);
	}

	
	/**
	 * Server accepts the request from the client through TCP connection.Server then
	 * sends the request information to the proposer thread.This method checks if the 
	 * request is for lock(client wants to obtain some lock) or unlock(release a particular lock) and calls the relevant methods.
	 * @param request
	 * 
	 */
	public void processClientRequest(RequestMessage request) {
		// if unlock request notify learner
		if (request.getRequestType().equals(RequestType.UNLOCK_REQUEST))
			handleUnlockRequest(request);
		else
			handleLockRequest(request);
	}
	
	
	/**
	 * This method performs the paxos algorithm to decide if a lock can or cannot be 
	 * granted to a requesting client.
	 * Lock request: Call learner to check if lock available then call method startPaxosAlgorithm 
	 * If lock already granted then how to handle. Need to check how to handle that case.
	 * 
	 * @param request
	 */
	private void handleLockRequest(RequestMessage request) {
		boolean isLockAvailable = learner.isLockGranted(request);
		if (!isLockAvailable) { // if lock not available then we send reply to
								// client informing it about it.
			// code to handle case where lock is already granted.
		} else {
			startPaxosAlgorithm(request);
		}
	}

	
	/**
	 * This method initiates the paxos algorithm to get concensus if the requested lock can be granted to the
	 * requesting client.
	 * @param request
	 */
	private void startPaxosAlgorithm(RequestMessage request) {
		
		//generate proposalNumber
		proposalNumber = proposalNumber + 1 ;
		sendPrepareMessage();
		
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
				learner.lock(request);
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
		return true;
	}

	
	/**
	 * This method generates an accept message and multicasts this message to all the acceptors.
	 */
	private void sendAcceptMessage() {
		// Send out a multicast message to all acceptor with an AcceptMessage
		AcceptMessage acceptMessage = new AcceptMessage(proposerId, proposalNumber, "");
		
		
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
	 * 
	 */
	private void sendPrepareMessage() {
	  PrepareMessage prepareMessage =  new PrepareMessage(proposerId, proposalNumber);
	 //code to multicast it the other acceptors.	
	}
	
	
	
	/**
	 * This method handles the unlock request from the client. It calls the learner to
	 * unlock the requested lock.Learner code will first validate if the lockid and clientId 
	 * sent in request are valid(i.e client is not asking to release a lock it does not hold)
	 * @param request
	 */
	private void handleUnlockRequest(RequestMessage request) {
		boolean unlocked = learner.unlock(request);
		if (unlocked)
			System.out.println(request.getLockId() + " has been unlocked as requested by client "
												   + request.getClientID());
		else
			System.out.println("Unlocking failed.");
		// What to do next if unlocking failed?????

	}
}
