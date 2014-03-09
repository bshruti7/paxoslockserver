package com.uw.paxos;
/**
 * 
 */
/**
 * @author Shruti
 *
 */
public abstract class Client {

	/**
	 * @param args
	 */
	
	public abstract int acquireLock();
	public void doSomethingWithLock() {
		
		// add code to make function sleep() here
	}
	
	public abstract String releaseLock();
	
}
