package com.uw.paxos.locks;

import java.util.LinkedList;
import java.util.Queue;
import com.uw.paxos.ClientId;

/**
 * Class representing a single distributed lock.
 * 
 * @author SamikshaSharma
 *
 */
public class Lock {
	private ClientId acquiredBy;
	private Queue<ClientId> clientsWaitingOnLock;
	
	public Lock() {
		acquiredBy = null;
		clientsWaitingOnLock = new LinkedList<>();
	}

	public boolean isAcquired() {
		return (getAcquiredBy() != null);
	}

	public ClientId getAcquiredBy() {
		return acquiredBy;
	}
	
	public void acquire(ClientId clientId) {
		assert(!isAcquired());
		acquiredBy = clientId;
	}
	
	public ClientId release(ClientId clientId) {
		assert(clientId == getAcquiredBy()); // Verify that lock was held by this client
		
		ClientId lastAcquiredBy = acquiredBy;
		acquiredBy = null;
		
		return lastAcquiredBy;
	}
	
	public void addWaitingClient(ClientId clientId) {
		clientsWaitingOnLock.add(clientId);
	}

	public ClientId getNextWaitingClient() {
		ClientId clientId = null;
		
		if (clientsWaitingOnLock.isEmpty()) {
			clientsWaitingOnLock.remove();
		}
			
		return clientId;
	}
}
