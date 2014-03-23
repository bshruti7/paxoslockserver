package com.uw.paxos.messages;

public enum ProposerLearnerMessageType {
	LOCK_ACQUIRE, // Proposer to Learner (multicast)
	LOCK_RELEASE, // Proposer to Learner (multicast)
	
	LOCK_ACQUIRED, // Learner to Proposer
	LOCK_RELEASED, // Learner to Proposer
	LOCK_QUEUED, // Learner to Proposer
	LOCK_UPDATE_FAILED // Learner to Proposer
}
