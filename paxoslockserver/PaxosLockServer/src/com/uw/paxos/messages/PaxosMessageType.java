package com.uw.paxos.messages;

public enum PaxosMessageType {
	PREPARE, // Proposer to Acceptors (multicast)
	PROMISE, // Acceptor to Proposer
	NACK_ON_PROMISE,//Acceptor rejects prepare message
	ACCEPT, // Proposer to Acceptors (multicast)
	ACCEPT_CONFIRMATION, // Acceptor to Proposer
	NACK_ON_ACCEPT, //Acceptors NACK the Accept message
	LOCK_ACQUIRE, // Proposer to Learner (multicast)
	LOCK_RELEASE // Proposer to Learner (multicast)
	
}
