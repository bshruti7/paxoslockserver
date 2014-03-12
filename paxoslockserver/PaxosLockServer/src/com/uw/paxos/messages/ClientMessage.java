package com.uw.paxos.messages;

import com.google.gson.Gson;

public class ClientMessage {

	private ClientMessageType messageType;
	private ClientId clientId;
	private int lockId;

	/**
	 * Deserialize this Java object from json string
	 * 
	 * @param string Json formatted string
	 * @return New object of this class
	 */
	public static ClientMessage fromString(String string) {
		Gson gson = new Gson();
		return gson.fromJson(string, ClientMessage.class);
	}

	/**
	 * Serialize this Java object as string
	 */
	@Override
	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
	
	public ClientMessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(ClientMessageType messageType) {
		this.messageType = messageType;
	}
	
	public ClientId getClientId() {
		return clientId;
	}

	public void setClientId(ClientId clientId) {
		this.clientId = clientId;
	}

	public int getLockId() {
		return lockId;
	}

	public void setLockId(int lockId) {
		this.lockId = lockId;
	}
}
