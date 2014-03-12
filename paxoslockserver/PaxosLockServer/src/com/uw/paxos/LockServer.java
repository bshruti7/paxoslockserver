package com.uw.paxos;
import java.io.*;
import java.net.InetAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.google.gson.Gson;
import com.uw.paxos.connection.Request;
import com.uw.paxos.locks.DistributedLocks;
import com.uw.paxos.messages.ClientMessage;
import com.uw.paxos.roles.AcceptorThread;
import com.uw.paxos.roles.ClientRequestAcceptorThread;
import com.uw.paxos.roles.LearnerThread;
import com.uw.paxos.roles.ProposerThread;
import com.uw.paxos.roles.StoppableLoopThread;

/**
 * @author Shruti
 *
 */
public class LockServer {
	
	private DistributedLocks locksTable; 
	private BlockingQueue<ClientMessage> clientRequestQueue;
	
	public LockServer() {
		locksTable = new DistributedLocks();
		clientRequestQueue = new LinkedBlockingQueue<>();
	}

	public static void main(String args[]) throws Exception{
		LockServer lockServer = new LockServer();
		lockServer.go();
	}
	
	public void go() throws Exception{
		boolean shutdownRequested = false;
		
		// Create three threads, on each for Proposer, Acceptor and Learner
		StoppableLoopThread[] threads = new StoppableLoopThread[4];
		
		// TODO: Extract port number from arguments to this process
		threads[0] = new ClientRequestAcceptorThread(clientRequestQueue, 6000);
		threads[1] = new ProposerThread(clientRequestQueue, locksTable, 6001);
		threads[2] = new AcceptorThread(6002);
		threads[3] = new LearnerThread(locksTable, 6003);
		
		// Start threads with different roles
		for (Thread thread : threads) {
			thread.start();
        }
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		Gson gson = new Gson();
		Request r = new Request();
		String jsonString = gson.toJson(r);
		Request recovered = gson.fromJson(jsonString, Request.class);		
		
		while (!shutdownRequested) {
			String command = br.readLine();
			if(0 == command.compareToIgnoreCase("shutdown")) {
				shutdownRequested = true;
				for (StoppableLoopThread thread : threads) {
					thread.requestShutdown();
		        }
			}
		}
	}
}
