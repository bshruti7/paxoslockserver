package com.uw.paxos;

import java.util.HashMap;

import com.uw.paxos.messages.PaxosMessage;

/**
 * This class performs the roles of a learner i.e. lock and unlock.
 * @author Aditi
 *
 */
// Does the object of this class need to be singleton so that the same data in the hashmap remains throughout.
public class Learner {
	HashMap <String,String> lockToClientMapping = new HashMap<String,String>();
/**
 * Grant the requested lock to the requesting client. This request
 * may be from the proposer sitting on the same server or from a multicast
 * request  from learner on other server to update learner on this sever. 
 * 
 * @param message
 * @return
 */

public void lock(PaxosMessage message){
	// lockToClientMapping.put(message.getLockId(), message.getClientID());	
	// informOtherLearners(message);
}

/**
 * Validate if the client really holds the lock ,its requesting to unlock. If yes, then
 * update the lock client mapping structure by removing the requested lock. 
 * @param message
 * @return
 */
public boolean unlock(PaxosMessage message){
	
	if(message.getClientId().equals(lockToClientMapping.containsKey(message.getLockId())))
	{
		lockToClientMapping.remove(message.getLockId());
		informOtherLearners(message);
	}
	else
	{//Client does not hold the requested lock and hence cannot unlock it.
		System.out.println("Client does not hold the requested lock and hence cannot unlock it.");
		return false;
	}
		return true;
}


/**
 * check if lock is granted to some client.
 * @param message
 * @return
 */
public boolean isLockGranted(PaxosMessage message){
	
	if(lockToClientMapping.containsKey(message.getLockId()))
  		return true;
	else 
		return false;
}

/**
 * Code to multicast lock or unlock update to all other learners in the cluster.
 * @param message
 */
private void informOtherLearners(PaxosMessage message){
	// multi casting code comes here.
}

/**
 * This method notifies the client when its request is successfully completed.
 */
public void notifyClient(PaxosMessage message){

	// String clientIPAddrese = message.getClientID();
	//estableish TCP connection with client to notify it about successful completion of request,
}

}
