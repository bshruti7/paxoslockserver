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
public class Client1 extends Client {

	/**
	 * 
	 */
	public Client1() {
		// TODO Auto-generated constructor stub
	}
	public int acquireLock(){
		//Randomly generate lock number between 1 and 10
		int lockID=(int) (Math.random()*10);
		return lockID;
	}
	public void doSomethingWithLock() {
		
		// add code to make function sleep() here
	}
	
	public String releaseLock(){
		return "Hello";
	}
	
	public boolean checkLockTable(String LockName,String ClientID) {
	/*
		ArrayList<String> LockTable = new ArrayList<String>();
		
		
		if(LockTable.size()==0) { 
			return false; 
			}
		else{
			LockTable.get(LockTable.indexOf(LockName));
		}
		*/
		return true;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		Client1 c1=new Client1();
		System.out.println("Lock required is "+c1.acquireLock());
		try{
		//Make TCP connection to server
		Socket newSocket=new Socket("127.0.0.1",6000);
		
		/*
		//For output stream to Server
		PrintWriter writer=new PrintWriter(newSocket.getOutputStream());
		writer.println("Lock"+c1.acquireLock());
		*/
		
		
		DataOutputStream outToServer = new DataOutputStream(newSocket.getOutputStream());
		outToServer.writeBytes("Lock Number:"+c1.acquireLock());
		
		/*
		//For the input stream from server
		InputStreamReader stream=new InputStreamReader(newSocket.getInputStream());
		BufferedReader reader=new BufferedReader(stream);
		String message=reader.readLine();
	*/
		newSocket.close();
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}

}
