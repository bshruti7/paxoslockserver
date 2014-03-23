package com.uw.paxos;
import java.io.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.uw.paxos.messages.ClientMessage;
import com.uw.paxos.roles.AcceptorThread;
import com.uw.paxos.roles.ClientRequestAcceptorThread;
import com.uw.paxos.roles.LearnerThread;
import com.uw.paxos.roles.ProposerThread;
import com.uw.paxos.roles.StoppableLoopThread;
import com.uw.paxos.utils.Utils;

/**
 * @author Shruti
 *
 */
public class LockServerMain {
	
	public static int BASE_PORT = 6000;
	public static int SERVERS_IN_QUORUM = 1;
	
	private BlockingQueue<ClientMessage> clientRequestQueue;
	
	public LockServerMain() {
		clientRequestQueue = new LinkedBlockingQueue<>();
	}

	public static void main(String args[]) throws Exception{
		
		if (args.length > 0) {
			BASE_PORT = Integer.parseInt(args[0]);
			SERVERS_IN_QUORUM = Integer.parseInt(args[1]);
		}
		
		Utils.logMessage("Starting Server. Servers in Quorum : " + SERVERS_IN_QUORUM);
		
		LockServerMain lockServer = new LockServerMain();
		lockServer.go();
	}
	
	public void go() throws Exception{
		boolean shutdownRequested = false;
		
		// Create three threads, on each for Proposer, Acceptor and Learner
		StoppableLoopThread[] threads = new StoppableLoopThread[4];
		
		// TODO: Extract port number from arguments to this process
		threads[0] = new ClientRequestAcceptorThread(clientRequestQueue, BASE_PORT);
		threads[1] = new ProposerThread(clientRequestQueue, BASE_PORT + 1);
		threads[2] = new AcceptorThread();
		threads[3] = new LearnerThread();
		
		// Start threads with different roles
		for (Thread thread : threads) {
			thread.start();
        }
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
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
