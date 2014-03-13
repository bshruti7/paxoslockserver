package com.uw.paxos.messages;

public enum ClientMessageType {
	LOCK_REQUEST, // Client sends a lock request to PaxosServer 
	UNLOCK_REQUEST, // Client sends a unlock request to PaxosServer
	LOCK_GRANTED, // Confirmation that lock is granted to client
	LOCK_RELEASED, // Confirmation that lock is released
	REQUEST_DENIED, // Denied request
}
