package com.uw.paxos.roles;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

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
	        //server = new UDPServer(portNumber);
		    } catch (Exception ioe) {
		      System.out.println(ioe);
		    }
		}
		

	@Override
    public void doProcessing() {
//		Request request = server.receiveRequest();
//		
//		if (request != null) {
////	    	 Do something with received request
//	    	Utils.logMessage(this.getClass().getSimpleName() + " Received Command: " + request.getRequestData());
	    	System.out.println("Acceptor thread has started");
			MulticastSocket socket = null;
		    DatagramPacket inPacket = null;
		    byte[] inBuf = new byte[256];
		    try {
		      //Prepare to join multicast group
		      socket = new MulticastSocket(8888);
		      InetAddress address = InetAddress.getByName("224.2.2.4");
		      socket.joinGroup(address);
		 
		      //while (true) {
		        inPacket = new DatagramPacket(inBuf, inBuf.length);
		        socket.receive(inPacket);
		        String msg = new String(inBuf, 0, inPacket.getLength());
		        System.out.println("From " + inPacket.getAddress() + "@AcceptorListenerThread Msg : " + msg);
		      //}
		}catch (IOException ioe) {
		      System.out.println(ioe);
		    }
//    }
//}
	}
}