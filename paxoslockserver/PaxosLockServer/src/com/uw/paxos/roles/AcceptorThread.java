package com.uw.paxos.roles;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import com.uw.paxos.connection.Server;

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
	// private MulticastReceiver multicastReceiver;
	
	public AcceptorThread(int portNumber) {
		try {
	        //server = new UDPServer(portNumber);
			
			// Similarly, create 
			// multicastReceiver = new MulticastReceiver("123.0.0.2", 8888);
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
		        
		        // Stays in doProcessing 
		        socket.receive(inPacket);
		        
		        String msg = new String(inBuf, 0, inPacket.getLength());
		        System.out.println("From " + inPacket.getAddress() + "@AcceptorListenerThread Msg : " + msg);
		      //}
		        System.out.println("Packet Data:"+inPacket.getData());
		        System.out.println("Packet Length:"+inPacket.getLength());
		        System.out.println("Client address:"+inPacket.getAddress());
		        System.out.println("Client port:"+inPacket.getPort());
		        
		        byte[] replyContent="ReplyContent".getBytes();
		        /*
		        DatagramPacket reply = new DatagramPacket(inPacket.getData(),
		        		inPacket.getLength(),inPacket.getAddress(),
		        		inPacket.getPort());
		        	*/
		        
		        DatagramPacket reply = new DatagramPacket(replyContent,
		        		replyContent.length,inPacket.getAddress(),
		        		inPacket.getPort());
		        		socket.send(reply);
		        		
		      //}
		    } catch (IOException ioe) {
		      System.out.println(ioe);
		    }
		}		    
//    }
//}
	
}