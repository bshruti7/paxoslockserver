package com.uw.paxos;
import java.net.*;
import java.io.*;
/**
 * 
 */

/**
 * @author Shruti
 *
 */
public class Server {

	public static void main(String args[]) throws Exception{
		
		
		Server s1=new Server();
		s1.go();
		
		
	}
	
	
	public void go() throws Exception{
		String receivedFromClient;
					
		ServerSocket serverSock = new ServerSocket(6000);
		
		
		
		
		while(true){
			Socket sock=serverSock.accept();
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			receivedFromClient = inFromClient.readLine();
			System.out.println("Received this from client: " + receivedFromClient);
			
			
		}
		
		
		//serverSock.close();
		
		
	}
	
	
}
