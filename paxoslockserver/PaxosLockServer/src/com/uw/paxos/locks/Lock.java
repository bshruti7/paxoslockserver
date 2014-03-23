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
	
	/**
	 * Add client as the new owner
	 * @param clientId Client who acquires the lock
	 */
	public void acquire(ClientId clientId) {
		if (isAcquired()) {
			throw new RuntimeException("Trying to acquire an already acquired lock. Request by " 
					+ clientId.getClientAddress() + ":"  + clientId.getClientPort()); // Verify that lock was held by this client
		}
		acquiredBy = clientId;
	}
	
	/**
	 * Release lock and give to next available client
	 * 
	 * @param clientId Client who is releasing the lock
	 * @return New owner of the lock
	 */
	public ClientId release(ClientId clientId) {
		if (!isAcquired() || !clientId.equals(getAcquiredBy())) {
			throw new RuntimeException("Trying to release a lock which has not been acquired by the client. Request by " 
					+ clientId.getClientAddress() + ":"  + clientId.getClientPort()); // Verify that lock was held by this client
		}
		
		acquiredBy = null;
		acquire(getNextWaitingClient());	
		
		return getAcquiredBy();
	}
	
	/**
	 * Add client to the lock wait queue
	 * @param clientId Client who is waiting for this lock
	 */
	public void addClientToQueue(ClientId clientId) {
		if (isAcquired()) {
			if (!clientId.equals(getAcquiredBy())) {
				clientsWaitingOnLock.add(clientId);
			} else {
				throw new RuntimeException("Unable to add client to the queue as client already has the lock. Request by "
						+ clientId.getClientAddress() + ":"  + clientId.getClientPort()); // Verify that lock was held by this client
			}
		} else {
			throw new RuntimeException("Trying to add lock to queue while lock is not acquired. Request by " 
					+ clientId.getClientAddress() + ":"  + clientId.getClientPort()); // Verify that lock was held by this client
		}
	}

	/**
	 * Return next client waiting for this lock
	 * @return
	 */
	private ClientId getNextWaitingClient() {
		ClientId clientId = null;
		
		if (!clientsWaitingOnLock.isEmpty()) {
			clientId = clientsWaitingOnLock.remove();
		}
			
		return clientId;
	}
}
