package com.uw.paxos;

import com.uw.paxos.utils.Utils;

/**
 * @author Shruti
 *
 */
public class PaxosClientMain {
	
	public static void main(String[] args) {
		LockClient client = new LockClient("localhost", 6000);
		
		for (int i = 0; i < 5; i++) {
			int lockId = getRandomLockID();
			boolean isSuccessful = client.acquire(lockId);
			
			if (isSuccessful) {
				doSomethingWithLock();
				client.release(lockId);
			}
		}
	}

	public static int getRandomLockID(){
		//Randomly generate lock number between 1 and 10
		int lockID=(int) (Math.random()*10);
		return lockID;
	}

	public static void doSomethingWithLock() {
		// Add some sleep to simulate work done using lock
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			Utils.logError("Interupted exception:"+ e.getMessage());
		}
	}
}
