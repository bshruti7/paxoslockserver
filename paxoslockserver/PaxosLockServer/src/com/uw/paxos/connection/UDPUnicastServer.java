package com.uw.paxos.connection;

import java.io.IOException;
import java.net.DatagramSocket;

import com.uw.paxos.utils.Utils;

public class UDPUnicastServer extends UDPServer {

	public UDPUnicastServer(int port) {
	    super(port);
	    try {
			this.serverSocket = new DatagramSocket(super.getPort());
			this.serverSocket.setSoTimeout(TIMEOUT_IN_MILLIS);
	        Utils.logMessage("Server bound to UDP port: " + this.getPort());
        } catch (IOException ex) {
        	Utils.logError("Unable to UDP port. Error : " + ex.getMessage());
        }
    }
}
