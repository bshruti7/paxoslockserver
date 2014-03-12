package com.uw.paxos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class Unicast {
	
	private String destinationIP;
	private String destinationPortnumber;
	private ClientMessage clientMessage;
		
	public Unicast(String destinationIP, String destinationPortnumber,
			ClientMessage clientMessage) {
		super();
		this.destinationIP = destinationIP;
		this.destinationPortnumber = destinationPortnumber;
		this.clientMessage = clientMessage;
	}



	//Make UDP connection to server
	public void establishConnection(){
	try{
			DatagramSocket newSocket = new DatagramSocket();
			/*//System.out.println("Enter the destination ip address:");
			BufferedReader br= new BufferedReader(new InputStreamReader(System.in));
			String Destination_IPAddress=br.readLine();
			System.out.println("Enter the destination PortNumber:");
			BufferedReader br1= new BufferedReader(new InputStreamReader(System.in));
			String Destination_Port=br1.readLine();
			*/	
		//	System.out.println("Destination Ip: "+Destination_IPAddress +", Destination port:"+ Destination_Port );
			
			InetAddress destinationIPAddr = InetAddress.getByName(destinationIP);
			int destinationPort = Integer.valueOf(destinationPortnumber).intValue();
			byte[] buffer = clientMessage.toString().getBytes();
			DatagramPacket request = new DatagramPacket(buffer,buffer.length, destinationIPAddr, destinationPort);
			newSocket.send(request);
			//newSocket.close();
		}
		catch (IOException e){
			e.printStackTrace();
			System.out.println("this is IO exp for client");
		}
	}
}
