package com.uw.paxos.roles;

import java.io.IOException;
import com.uw.paxos.connection.Request;
import com.uw.paxos.connection.Server;
import com.uw.paxos.connection.UDPServer;
import com.uw.paxos.utils.Utils;

/**
 * Acceptor class that can be run as single thread.
 * This class starts a UDP server and listens to requests from proposers
 * on the port specified in constructor.
 * 
 * @author Samiksha Sharma
 *
 */
public class AcceptorThread extends StoppableLoopThread {
	
	private Server server;
	
	public AcceptorThread(int portNumber) {
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
	    	// Do something with received request
	    	Utils.logMessage(this.getClass().getSimpleName() + " Received Command: " + request.getRequestData());
		}
    }
}
