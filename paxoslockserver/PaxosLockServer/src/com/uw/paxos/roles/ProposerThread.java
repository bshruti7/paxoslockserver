package com.uw.paxos.roles;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.uw.paxos.Proposer;
import com.uw.paxos.RequestMessage;
import com.uw.paxos.connection.Request;
import com.uw.paxos.connection.Server;
import com.uw.paxos.connection.UDPServer;
import com.uw.paxos.locks.DistributedLocks;
import com.uw.paxos.utils.Utils;

/**
 * Proposer class that can be run as single thread.
 * This class starts a UDP server and listens to requests from clients
 * on the port specified in constructor.
 * This class handles following requests from clients:
 * - AcquireLock
 * - ReleaseLock
 * 
 * @author Samiksha Sharma
 *
 */
public class ProposerThread extends StoppableLoopThread {
	
	private BlockingQueue<Request> clientRequestQueue;
	private DistributedLocks locks;
	private Server server;
	
	public ProposerThread(BlockingQueue<Request> clientRequestQueue, DistributedLocks locks, int portNumber) {
		this.locks = locks;
		this.clientRequestQueue = clientRequestQueue;
		try {
	        server = new UDPServer(portNumber);
        } catch (IOException ex) {
	        Utils.logError("Unable to bind to port : " + portNumber + ". Exception: " + ex.getMessage());
        }
	}
	
	@Override
    public void doProcessing() {
		Request request = null;
		try {
			// Blocking call until an element is available in the queue.
			// ClientRequestAcceptorThread puts a dummy element in this queue
			// to wake ProposerThread up after maximum of 15 seconds.
			request = clientRequestQueue.take();
						
		} catch (InterruptedException ex) {
			Utils.logError(this.getClass().getSimpleName() + " encountered error while fetching client request from queue. \nError : " + ex.getMessage());
		}
		if (request.getClientPort() != 0000) {
			Utils.logMessage(this.getClass().getSimpleName() + " started working on client request : " + request.getRequestData());
	    	//Valid request
			System.out.println("Request from queue is :" + request.getRequestData() + " client PortNumber :"+request.getClientPort() + " Client IP Address"+request.getClientIpAddress());
			// Parse and act on request
			Proposer proposer = new Proposer(request);
			proposer.startPaxosAlgorithm(request);
		}
    }
	
	private void processRequest(Request request) {
		
	}
}
