package com.uw.paxos;

import com.uw.paxos.utils.Utils;

/**
 * @author Shruti
 *
 */
public class LockClientMain {
	
	public static void main(String[] args) {
		
		String hostname = "localhost";
		
		if (args.length > 0) {
			hostname = args[0];
		}
		
		LockClient client = new LockClient(hostname, 6000);
		
		for (int i = 0; i < 2; i++) {
			int lockId = getRandomLockID();
			boolean isSuccessful = client.acquire(lockId);
			
			if (isSuccessful) {
				doSomethingWithLock();
				client.release(lockId);
			}
		}
	}

	public static int getRandomLockID(){
		//Randomly generate lock number between 0 and 9, both inclusive
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
