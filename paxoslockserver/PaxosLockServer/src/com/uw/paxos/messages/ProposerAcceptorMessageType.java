package com.uw.paxos.messages;

public enum ProposerAcceptorMessageType {
	PREPARE, // Proposer to Acceptors (multicast)
	PROMISE, // Acceptor to Proposer
	NACK_ON_PREPARE,// Acceptor rejects prepare message
	ACCEPT, // Proposer to Acceptors (multicast)
	ACCEPT_CONFIRMATION, // Acceptor to Proposer
	NACK_ON_ACCEPT, // Acceptors NACK the Accept message
}
