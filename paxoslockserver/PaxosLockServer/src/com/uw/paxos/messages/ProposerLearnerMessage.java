package com.uw.paxos.messages;

import com.google.gson.Gson;
 
/**
 * 
 * This class stores the request and response details between Proposer and Learner.
 * 
 * @author Aditi  
 *
 */
public class ProposerLearnerMessage {
	private ProposerLearnerMessageType messageType;
	private ClientId clientId;
	private ClientId newOwnerClientId;
	private int lockId;
	
	/**
	 * Deserialize this Java object from json string
	 * 
	 * @param string Json formatted string
	 * @return New object of this class
	 */
	public static ProposerLearnerMessage fromString(String jsonString) {
		Gson gson = new Gson();
		return gson.fromJson(jsonString, ProposerLearnerMessage.class);
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
	
	public ProposerLearnerMessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(ProposerLearnerMessageType messageType) {
		this.messageType = messageType;
	}

	public ClientId getClientId() {
		return clientId;
	}

	public void setClientId(ClientId clientId) {
		this.clientId = clientId;
	}
	
	public ClientId getNewOwnerClientId() {
		return newOwnerClientId;
	}

	public void setNewOwnerClientId(ClientId nextClientId) {
		this.newOwnerClientId = nextClientId;
	}
	
	public int getLockId() {
		return lockId;
	}

	public void setLockId(int lockId) {
		this.lockId = lockId;
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
		result = prime
				* result
				+ ((newOwnerClientId == null) ? 0 : newOwnerClientId.hashCode());
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
		ProposerLearnerMessage other = (ProposerLearnerMessage) obj;
		if (clientId == null) {
			if (other.clientId != null)
				return false;
		} else if (!clientId.equals(other.clientId))
			return false;
		if (lockId != other.lockId)
			return false;
		if (messageType != other.messageType)
			return false;
		if (newOwnerClientId == null) {
			if (other.newOwnerClientId != null)
				return false;
		} else if (!newOwnerClientId.equals(other.newOwnerClientId))
			return false;
		return true;
	}
}	
