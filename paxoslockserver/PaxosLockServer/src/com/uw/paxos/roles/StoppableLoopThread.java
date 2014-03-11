package com.uw.paxos.roles;

import com.uw.paxos.utils.Utils;

/**
 * This class creates a thread and keeps it running infinitely until a stop is requested. 
 * Any task that needs to be done in a single loop should be done in doProcessing method.
 * 
 * @author Samiksha Sharma
 *
 */
public abstract class StoppableLoopThread extends Thread{
	
	private boolean shutdownRequested;
	
	/** 
	 * Protected constructor to allow derived class to call this one.
	 * 
	 * @param locks DistributedLocks used by current server
	 */
	protected StoppableLoopThread() {
		shutdownRequested = false;
	}
	
	/**
	 * Call this function to request shutdown of this thread
	 */
	public void requestShutdown() {
		Utils.logMessage("Stopping " + this.getClass().getSimpleName() + ".");
		shutdownRequested = true;
	}
	
	/**
	 * Function where processing is done.
	 */
	public abstract void doProcessing();

	@Override
    public void run() {
		Utils.logMessage(this.getClass().getSimpleName() + " started.");
	    while (!shutdownRequested) {
	    	doProcessing();
	    }
	    Utils.logMessage(this.getClass().getSimpleName() + " stopped.");
    }
}
