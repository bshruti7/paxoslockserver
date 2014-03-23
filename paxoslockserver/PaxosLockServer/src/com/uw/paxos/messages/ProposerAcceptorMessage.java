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
public class ProposerAcceptorMessage {
	private ProposerAcceptorMessageType messageType;
	private ClientId clientId;
	private int lockId;
	private int highestProposalNumberSeen;
	private int proposalNumber;
	
	/**
	 * Deserialize this Java object from json string
	 * 
	 * @param string Json formatted string
	 * @return New object of this class
	 */
	public static ProposerAcceptorMessage fromString(String jsonString) {
		Gson gson = new Gson();
		return gson.fromJson(jsonString, ProposerAcceptorMessage.class);
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
	
	public ProposerAcceptorMessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(ProposerAcceptorMessageType messageType) {
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((clientId == null) ? 0 : clientId.hashCode());
		result = prime * result + lockId;
		result = prime * result
				+ ((messageType == null) ? 0 : messageType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProposerAcceptorMessage other = (ProposerAcceptorMessage) obj;
		if (clientId == null) {
			if (other.clientId != null)
				return false;
		} else if (!clientId.equals(other.clientId))
			return false;
		if (lockId != other.lockId)
			return false;
		if (messageType != other.messageType)
			return false;
		return true;
	}
}	
