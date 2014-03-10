package com.uw.paxos;
import java.io.*;

import com.uw.paxos.roles.AcceptorThread;
import com.uw.paxos.roles.LearnerThread;
import com.uw.paxos.roles.ProposerThread;
import com.uw.paxos.roles.StoppableLoopThread;

/**
 * @author Shruti
 *
 */
public class LockServer {

	public static void main(String args[]) throws Exception{
		LockServer lockServer = new LockServer();
		lockServer.go();
	}
	
	public void go() throws Exception{
		boolean shutdownRequested = false;
		
		// Create three threads, on each for Proposer, Acceptor and Learner
		StoppableLoopThread[] roleThreads = new StoppableLoopThread[3];
		
		// TODO: Extract port number from arguments to this process
		roleThreads[0] = new ProposerThread(6000);
		roleThreads[1] = new AcceptorThread(6001);
		roleThreads[2] = new LearnerThread(6002);
		
		// Start threads with different roles
		for (Thread thread : roleThreads) {
			thread.start();
        }
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		while (!shutdownRequested) {
			String command = br.readLine();
			if(0 == command.compareToIgnoreCase("shutdown")) {
				shutdownRequested = true;
				for (StoppableLoopThread thread : roleThreads) {
					thread.requestShutdown();
		        }
			}
		}
	}
}
