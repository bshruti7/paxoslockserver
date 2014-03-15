package com.uw.paxos.connection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;

import com.uw.paxos.connection.Server;
import com.uw.paxos.utils.Utils;

/**
 * Helper class to open an UDP connection and send and receive responses.
 * @author Samiksha Sharma
 *
 */
public abstract class UDPServer extends Server {
	private static int MAX_BUFFER_SIZE = 1024;
	private static Charset defaultCharset = Charset.forName("US-ASCII");
	
	protected static final int TIMEOUT_IN_MILLIS = 15000;
	protected DatagramSocket serverSocket;
	
	protected UDPServer(int port) {
		super(port);
	}
	
	@Override
    public Request receiveRequest() {
		Request request = null;
		try {
			byte []buffer = new byte[MAX_BUFFER_SIZE];
			DatagramPacket p = new DatagramPacket(buffer, MAX_BUFFER_SIZE);
			serverSocket.receive(p);
			
			byte []responseBytes = new byte[p.getLength()];
			System.arraycopy(p.getData(), p.getOffset(), responseBytes, 0, p.getLength());
			
			String requestDataAsString = new String(responseBytes, defaultCharset);
			// logMessage("Request as String: " + requestDataAsString);
			
			request = new Request();
			request.setSenderIpAddress(p.getAddress());
			request.setSenderPort(p.getPort());
			request.setMessage(requestDataAsString);
        } catch (SocketTimeoutException ex) {
        	// Do nothing
        } catch (IOException ex) {
        	Utils.logError("Exception occured while reading message. Error : " + ex.getMessage());
        }
		
		return request;
    }

	@Override
    public void sendResponse(Response response) {
		
		// logMessage("Response: " + response.getMessage());
		byte []responseBuffer = response.getMessage().getBytes(defaultCharset);
		
		DatagramPacket p = new DatagramPacket(responseBuffer, responseBuffer.length, response.getReceiverIpAddress(), response.getReceiverPort());
		try {
	        this.serverSocket.send(p);
        } catch (IOException ex) {
	        ex.printStackTrace();
        }
    }
	
	@Override
	public void logMessage(String message) {
		if (this.serverSocket != null) {
			Utils.logMessage(message);
		}
	}

	@Override
	public void logError(String error) {
		if (this.serverSocket != null) {
			Utils.logError(error);
		}
	}

	protected void shutdownServer() throws IOException {
		// If socket is not null and not closed
		if (serverSocket != null && !serverSocket.isClosed()) {
			// Close the connection
			serverSocket.close();
		}
	}
}
