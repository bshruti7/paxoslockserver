package com.uw.paxos;
/**
 * 
 */
import java.io.*;
import java.net.*;
//import java.util.*;

/**
 * @author Shruti
 *
 */
public class PaxosClient extends Client {

	public PaxosClient() {}
	
	public int getLockID(){
		//Randomly generate lock number between 1 and 10
		int lockID=(int) (Math.random()*10);
		return lockID;
	}

	//Keep a sleep it simulate that client is using the lock/
	public void doSomethingWithLock() {
		try {
			Thread.sleep(1000);//take a random value for sleep
		} catch (InterruptedException e) {
			System.out.println("Interupted exception:"+ e.getMessage());
		}
	}
	
	
	public String releaseLock(){
		return "Hello";
	}

	
	
	public static void main(String[] args) {
		PaxosClient c1=new PaxosClient();
		getlock(c1);
	
	}

	
	private static void getlock(PaxosClient c1) {
		int requiredLockId = c1.getLockID();
		ClientId clientId = null;
		try {
			clientId = new ClientId(InetAddress.getByName("localhost"),9000);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		ClientMessage clientMessage = new ClientMessage(ClientMessageType.LOCK_REQUEST,clientId, requiredLockId);
		Unicast unicastObj = new Unicast("localhost", "6000", clientMessage);
		unicastObj.establishConnection();
	}

}
