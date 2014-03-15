package com.uw.paxos.locks;

import java.util.LinkedList;
import java.util.Queue;

import com.uw.paxos.messages.ClientId;

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
		if (isAcquired()) {
			throw new RuntimeException("Trying to acquire a already acquired lock." 
					+ clientId.getClientAddress() + ":"  + clientId.getClientPort()); // Verify that lock was held by this client
		}
		acquiredBy = clientId;
	}
	
	public ClientId release(ClientId clientId) {
		if (!isAcquired() || !clientId.equals(getAcquiredBy())) {
			throw new RuntimeException("Trying to release a lock which has not been acquired by the client " 
					+ clientId.getClientAddress() + ":"  + clientId.getClientPort()); // Verify that lock was held by this client
		}
		
		ClientId lastAcquiredBy = acquiredBy;
		acquiredBy = null;
		
		return lastAcquiredBy;
	}
	
	public void addWaitingClient(ClientId clientId) {
		if (isAcquired() && clientId.equals(getAcquiredBy())) {
			throw new RuntimeException("Unable to add client to the queue as client already has the lock "
					+ clientId.getClientAddress() + ":"  + clientId.getClientPort()); // Verify that lock was held by this client
		}
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
