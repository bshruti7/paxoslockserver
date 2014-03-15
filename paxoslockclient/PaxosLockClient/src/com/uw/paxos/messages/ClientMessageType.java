package com.uw.paxos.messages;

public enum ClientMessageType {
	LOCK_ACQUIRE_REQUEST, // Client sends a lock request to PaxosServer 
	LOCK_RELEASE_REQUEST, // Client sends a unlock request to PaxosServer
	LOCK_GRANTED, // Confirmation that lock is granted to client
	LOCK_RELEASED, // Confirmation that lock is released
	REQUEST_DENIED, // Denied request
	DUMMY_REQUEST // Dummy request to wake up Proposer thread from queue after every 15 sec.
}