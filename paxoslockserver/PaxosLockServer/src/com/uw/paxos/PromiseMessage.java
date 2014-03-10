package com.uw.paxos;

/**
 * This class resembles the promises or the negative acknowledgements given by an acceptor
 * in reply to the prepare message.
 * @author Aditi
 *
 */
public class PromiseMessage {
	// This field when set to true will imply that the acceptors has sent a promise and a negatve acknowledgement if set to false.
private boolean isPromise;
private String highestSeenProposalNumber;
private String value;
public PromiseMessage(boolean isPromise, String highestSeenProposalNumber,
		String value) {
	super();
	this.isPromise = isPromise;
	this.highestSeenProposalNumber = highestSeenProposalNumber;
	this.value = value;
}
public boolean isPromise() {
	return isPromise;
}
public void setPromise(boolean isPromise) {
	this.isPromise = isPromise;
}
public String getHighestSeenProposalNumber() {
	return highestSeenProposalNumber;
}
public void setHighestSeenProposalNumber(String highestSeenProposalNumber) {
	this.highestSeenProposalNumber = highestSeenProposalNumber;
}
public String getValue() {
	return value;
}
public void setValue(String value) {
	this.value = value;
}


}
