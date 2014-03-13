package com.uw.paxos.connection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.Charset;

import com.uw.paxos.messages.Request;
import com.uw.paxos.messages.Response;
import com.uw.paxos.utils.*;

public class UDPClient {
	private static int MAX_BUFFER_SIZE = 1024;
	private static Charset defaultCharset = Charset.forName("US-ASCII");
	
	private DatagramSocket socket;
	
	public void connect() {
		try {
			// Bind to new UDP port
	        socket = new DatagramSocket();
	        logMessage("UDP port bound.");
        } catch (SocketException ex) {
	        logError("Error while trying to bind UDP port. Error: " + ex);
        }
	}
	
    public void close() {
		try {
			// If socket is not null and not closed
			if (this.socket != null && !this.socket.isClosed()) {
				// Close the connection
				logMessage("Unbinding UDP port.");
				this.socket.close();
				logMessage("UDP port unbound.");
			}
		} catch (Exception ex) {
			logError("Error while trying to unbind UDP port. Error: " + ex);
		}
    }
	
    public void sendRequest(Request request) {
		try {
			logMessage("Request as String: " + request.getMessage());
			
			byte []buffer = request.getMessage().getBytes(defaultCharset);
			DatagramPacket p = new DatagramPacket(buffer, buffer.length, request.getServerIpAddress(), request.getServerPort());
	        
			this.socket.send(p);
        } catch (IOException ex) {
	        ex.printStackTrace();
        }
    }
	
    public Response receiveResponse() {
		Response response = new Response();
		try {
			// Allocate buffer to receive data
			byte []buffer = new byte[MAX_BUFFER_SIZE];
			DatagramPacket p = new DatagramPacket(buffer, MAX_BUFFER_SIZE);
			
			// Receive data from server
			socket.receive(p);
			
			// Convert byte response to String
			String stringResponse = new String(p.getData(), p.getOffset(), p.getLength(), defaultCharset);
			
			logMessage("Response from Server as String: " + stringResponse);
			
			// Create Java response message
			response.setMessage(stringResponse);
			response.setServerIpAddress(p.getAddress());
			response.setServerPort(p.getPort());
        } catch (IOException ex) {
        	ex.printStackTrace();
        }
		
		return response;
    }

	public void logMessage(String message) {
		if (this.socket != null) {
			Utils.logMessage(message);
		}
	}

	public void logError(String error) {
		if (this.socket != null) {
			Utils.logError(error);
		}
	}
}
