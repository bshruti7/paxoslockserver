package com.uw.paxos;

/**
 * Class stores the prepare message. It contains the proposalId of the sender 
 * and a proposalNumber.
 * @author Administrator
 *
 */
public class PrepareMessage {
private String proposalId;
private int proposalNumber;
public PrepareMessage(String proposalId, int proposalNumber) {
	super();
	this.proposalId = proposalId;
	this.proposalNumber = proposalNumber;
}

}
