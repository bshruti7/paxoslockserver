package com.uw.paxos.connection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.util.Arrays;

import com.uw.paxos.connection.Server;
import com.uw.paxos.utils.Utils;

/**
 * Helper class to open an UDP connection and send and receive responses.
 * @author Samiksha Sharma
 *
 */
public class UDPServer extends Server {
	private static int MAX_BUFFER_SIZE = 1024;
	public static int TIMEOUT_IN_MILLIS = 15000;
	private static Charset defaultCharset = Charset.forName("US-ASCII");
	
	private DatagramSocket serverSocket;
	
	public UDPServer(int port) throws IOException {
		super(port);
		this.serverSocket = new DatagramSocket(super.getPort());
		this.serverSocket.setSoTimeout(TIMEOUT_IN_MILLIS);
        Utils.logMessage("Server bound to UDP port: " + this.getPort());
	}
	
	@Override
    public ClientRequest receiveRequest() {
		ClientRequest clientRequest = null;
		try {
			byte []buffer = new byte[MAX_BUFFER_SIZE];
			DatagramPacket p = new DatagramPacket(buffer, MAX_BUFFER_SIZE);
			serverSocket.receive(p);
			
			byte []responseBytes = new byte[p.getLength()];
			System.arraycopy(p.getData(), p.getOffset(), responseBytes, 0, p.getLength());
			
			logMessage("Client Request: " + Arrays.toString(responseBytes));
			String requestDataAsString = new String(responseBytes, defaultCharset);
			logMessage("Client Request as String: " + requestDataAsString);
			
			clientRequest = new ClientRequest(p.getAddress(), p.getPort(), requestDataAsString);
        } catch (SocketTimeoutException ex) {
        	// Do nothing
        } catch (IOException ex) {
        	ex.printStackTrace();
        }
		
		return clientRequest;
    }

	@Override
    public void sendResponse(ServerResponse serverResponse) {
			
		logMessage("Response: " + serverResponse.getResponseData());
		byte []responseBuffer = serverResponse.getResponseData().getBytes(defaultCharset);
		
		DatagramPacket p = new DatagramPacket(responseBuffer, responseBuffer.length, serverResponse.getReceiverIpAddress(), serverResponse.getReceiverPort());
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
