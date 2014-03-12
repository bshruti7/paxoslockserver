package com.uw.paxos.messages;

public enum PaxosMessageType {
	PREPARE, // Proposer to Acceptors (multicast)
	PROMISE, // Acceptor to Proposer
	ACCEPT, // Proposer to Acceptors (multicast)
	CONFIRMATION, // Acceptor to Proposer
	LOCK_ACQUIRE, // Proposer to Learner (multicast)
	LOCK_RELEASE // Proposer to Learner (multicast)
}
