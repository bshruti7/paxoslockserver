package com.uw.paxos.locks;

/**
 * Class containing array of locks. 
 * 
 * @author SamikshaSharma
 *
 */
public class DistributedLocks {
	
	// Provide maximum of 20 locks
	private final static int MAX_LOCKS = 20;
	private Lock[] locks;
	
	public DistributedLocks() {
		this.locks = new Lock[MAX_LOCKS];
		for (int i = 0; i < locks.length; i++) {
			locks[i] = new Lock();
		}
	}
	
	public Lock getLock(int id) {
		assert(id >= 0 && id < 20);
		return locks[id];
	}
}
