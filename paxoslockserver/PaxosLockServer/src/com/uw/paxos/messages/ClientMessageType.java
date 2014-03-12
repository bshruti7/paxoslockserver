package com.uw.paxos.messages;

public enum ClientMessageType {
	LOCK_REQUEST, 
	UNLOCK_REQUEST,
	LOCK_REQUEST_GRANTED,
	DUMMY_REQUEST
}
