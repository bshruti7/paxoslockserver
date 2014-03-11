package com.uw.paxos.roles;

import java.io.IOException;
import com.uw.paxos.connection.Request;
import com.uw.paxos.connection.Server;
import com.uw.paxos.connection.UDPServer;
import com.uw.paxos.locks.DistributedLocks;
import com.uw.paxos.utils.Utils;

/**
 * Learner class that can be run as single thread.
 * This class starts a UDP server and listens to requests from proposers  
 * on the port specified in constructor. 
 * 
 * @author Samiksha Sharma
 *
 */
public class LearnerThread extends StoppableLoopThread {
	
	private DistributedLocks locks;
	private Server server;
	
	public LearnerThread(DistributedLocks locks, int portNumber) {
		this.locks = locks;
		try {
	        server = new UDPServer(portNumber);
        } catch (IOException ex) {
	        Utils.logError("Unable to bind to port : " + portNumber + ". Exception: " + ex.getMessage());
        }
	}
	
	@Override
    public void doProcessing() {
		Request request = server.receiveRequest();
		
		if (request != null) {
	    	Utils.logMessage(this.getClass().getSimpleName() + " received command: " + request.getRequestData());
		}	    
    }
}
