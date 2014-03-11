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

	/**
	 * 
	 */
	public PaxosClient() {
		// TODO Auto-generated constructor stub
	}
	public int acquireLock(){
		//Randomly generate lock number between 1 and 10
		int lockID=(int) (Math.random()*10);
		return lockID;
	}
	public void doSomethingWithLock() {
		
		//Keep a lseep ti simulate that client is using the lock/
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			System.out.println("Interupted exception:"+ e.getMessage());
		}
	}
	
	public String releaseLock(){
		return "Hello";
	}

		/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		PaxosClient c1=new PaxosClient();
		String lockRequired = "Lock"+c1.acquireLock();
		System.out.println("Lock required is "+lockRequired) ;
		try{
		//Make TCP connection to server
			DatagramSocket newSocket = new DatagramSocket();
			System.out.println("Enter the destination ip address:");
			BufferedReader br= new BufferedReader(new InputStreamReader(System.in));
			String Destination_IPAddress=br.readLine();
			System.out.println("Enter the destination PortNumber:");
			BufferedReader br1= new BufferedReader(new InputStreamReader(System.in));
			String Destination_Port=br1.readLine();
			
			System.out.println("Destination Ip: "+Destination_IPAddress +", Destination port:"+ Destination_Port );
			InetAddress aHost = InetAddress.getByName(Destination_IPAddress);
			int serverPort = Integer.valueOf(Destination_Port).intValue();
			byte[] b = lockRequired.getBytes();
			DatagramPacket request = new DatagramPacket(b,lockRequired.length(), aHost, serverPort);
			newSocket.send(request);
			newSocket.close();
	
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}

}
