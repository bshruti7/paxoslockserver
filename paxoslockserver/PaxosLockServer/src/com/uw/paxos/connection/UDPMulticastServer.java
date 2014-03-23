package com.uw.paxos.connection;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;

import com.uw.paxos.utils.Utils;

/**
 * Class extending UDPServer and using MulticastSocket. 
 * Since MulticastSocket is derived from DatagramSocket,
 * it has same functionalities + ability to join and leave a group.
 * 
 * @author Shruti
 * 
 */
public class UDPMulticastServer extends UDPServer {
	private InetAddress receivingGroupAddress;
		
	public UDPMulticastServer(InetAddress groupAddress, int port) {
		super(port);
		try {
	        this.serverSocket = new MulticastSocket(this.getPort());
	        this.serverSocket.setSoTimeout(TIMEOUT_IN_MILLIS);
	        this.receivingGroupAddress = groupAddress;
	        Utils.logMessage("Server bound to UDP port: " + this.getPort());
        } catch (IOException ex) {
        	Utils.logError("Unable to bind UDP port. Error : " + ex.getMessage());
        }		
	}
	
	public void joinMulticastGroup() {
        try {
        	if (serverSocket != null) {
        		((MulticastSocket) serverSocket).joinGroup(receivingGroupAddress);
        	}
        } catch (IOException ex) {
	        Utils.logError("Unable to join multicast group with IP address " + receivingGroupAddress.getHostAddress() + ". Error : " + ex.getMessage());
        }
	}
	
	public void leaveMulticastGroup() {
		try {
			if (serverSocket != null) {
				((MulticastSocket) serverSocket).leaveGroup(receivingGroupAddress);
			}
        } catch (IOException ex) {
        	Utils.logError("Error occured while leaving group with IP address " + receivingGroupAddress.getHostAddress() + ". Error : " + ex.getMessage());
        }
	}
	
	@Override 
	protected void shutdownServer() throws IOException {
		// If socket is not null and not closed
		if (serverSocket != null && !serverSocket.isClosed()) {
			// Leave the multicast group and close connection
			leaveMulticastGroup();
			serverSocket.close();
		}
		
		super.shutdownServer();
	}
	
	
}
