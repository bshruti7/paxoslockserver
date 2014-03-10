package com.uw.paxos;

 
/**
 * 
 * This class stores the details from the client request like lockId,clientId.
 * A client request will have only the lockId and the clientId. The server code adds the
 * proposerId and the requestType details to the request.
 *
 * @author Aditi  
 *
 */
public class RequestMessage {
	
	public RequestMessage(String lockId, String clientID, String proposerID,
			RequestType requestType) {
		super();
		this.lockId = lockId;
		this.clientID = clientID;
		this.proposerID = proposerID;
		this.requestType = requestType;
	}
	private String lockId;
	private String clientID;
	private String proposerID;
	private RequestType requestType;
	
	public RequestMessage(String lockId, String clientID) {
		super();
		this.lockId = lockId;
		this.clientID = clientID;
	}
	public String getLockId() {
		return lockId;
	}
	public void setLockId(String lockId) {
		this.lockId = lockId;
	}
	public String getClientID() {
		return clientID;
	}
	public void setClientID(String clientID) {
		this.clientID = clientID;
	}
	public String getProposerID() {
		return proposerID;
	}
	public void setProposerID(String proposerID) {
		this.proposerID = proposerID;
	}
	public RequestType getRequestType() {
		return requestType;
	}

	
}
