package com.uw.paxos;

/**
 * This class resembles the accept message sent by the proposer to the acceptor.
 * It contains he proposalNumber, proposalId and value(in this instance of work we are not sending anything in value)
 * @author Administrator
 *
 */
public class AcceptMessage {
private String proposalId;
private int proposalNumber;
private String value;
public AcceptMessage(String proposalId, int proposalNumber, String value) {
	super();
	this.proposalId = proposalId;
	this.proposalNumber = proposalNumber;
	this.value = value;
}
public String getProposalId() {
	return proposalId;
}
public void setProposalId(String proposalId) {
	this.proposalId = proposalId;
}
public int getProposalNumber() {
	return proposalNumber;
}
public void setProposalNumber(int proposalNumber) {
	this.proposalNumber = proposalNumber;
}
public String getValue() {
	return value;
}
public void setValue(String value) {
	this.value = value;
}


}
