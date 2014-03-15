package com.uw.paxos.messages;

import com.google.gson.Gson;
 
/**
 * 
 * This class stores the details from the client request like lockId, clientId.
 * A client request will have only the lockId and the clientId. The server code adds the
 * proposerId and the requestType details to the request.
 *
 * @author Aditi  
 *
 */
public class PaxosMessage {
	private PaxosMessageType messageType;
	private ClientId clientId;
	private String proposerId;
	private int lockId;
	private int highestProposalNumberSeen;
	private int proposalNumber;
	
	/**
	 * Deserialize this Java object from json string
	 * 
	 * @param string Json formatted string
	 * @return New object of this class
	 */
	public static PaxosMessage fromString(String jsonString) {
		Gson gson = new Gson();
		return gson.fromJson(jsonString, PaxosMessage.class);
	}

	/**
	 * Serialize this Java object as string
	 */
	@Override
	public String toString() {
		Gson gson = new Gson();
		String jsonString = gson.toJson(this);
		return jsonString;
	}
	
	public PaxosMessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(PaxosMessageType messageType) {
		this.messageType = messageType;
	}

	public int getLockId() {
		return lockId;
	}

	public void setLockId(int lockId) {
		this.lockId = lockId;
	}

	public ClientId getClientId() {
		return clientId;
	}

	public void setClientId(ClientId clientId) {
		this.clientId = clientId;
	}

	public String getProposerId() {
		return proposerId;
	}

	public void setProposerId(String proposerId) {
		this.proposerId = proposerId;
	}

	public int getHighestProposalNumberSeen() {
		return highestProposalNumberSeen;
	}

	public void setHighestProposalNumberSeen(int highestProposalNumberSeen) {
		this.highestProposalNumberSeen = highestProposalNumberSeen;
	}

	public int getProposalNumber() {
		return proposalNumber;
	}

	public void setProposalNumber(int proposalNumber) {
		this.proposalNumber = proposalNumber;
	}
}	
