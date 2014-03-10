package com.uw.paxos.roles;

import java.io.IOException;

import com.uw.paxos.connection.ClientRequest;
import com.uw.paxos.connection.Server;
import com.uw.paxos.connection.UDPServer;
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
	
	Server server;
	
	public LearnerThread(int portNumber) {
		try {
	        server = new UDPServer(portNumber);
        } catch (IOException ex) {
	        Utils.logError("Unable to bind to port : " + portNumber + ". Exception: " + ex.getMessage());
        }
	}
	
	@Override
    public void doProcessing() {
		ClientRequest request = server.receiveRequest();
		
		if (request != null) {
	    	// Do something with received request
	    	Utils.logMessage(this.getClass().getSimpleName() + " Received Command: " + request.getRequestData());
		}	    
    }
}
