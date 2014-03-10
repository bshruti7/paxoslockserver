package com.uw.paxos;

import java.util.HashMap;

/**
 * This class act as the lock server. It holds the details of which locks are granted to which client.
 * @author Administrator
 *
 */

//We may may not need this class. Not sure as we need to maintain a single copy of this hashmap using singleton pattern.
//Needs investigation
public class LockServerMapping {
	HashMap <String,String> lockToClientMapping = new HashMap<String,String>();

	public HashMap<String, String> getLockToClientMapping() {
		return lockToClientMapping;
	}

	public void setLockToClientMapping(HashMap<String, String> lockToClientMapping) {
		this.lockToClientMapping = lockToClientMapping;
	}
}
