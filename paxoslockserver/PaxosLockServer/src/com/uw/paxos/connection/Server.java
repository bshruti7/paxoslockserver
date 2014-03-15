package com.uw.paxos.connection;

import java.io.IOException;

import com.uw.paxos.utils.*;

/**
 * Abstract class to represent a server and its functions
 * @author Samiksha Sharma
 *
 */
public abstract class Server {
	private final int port;
	
	protected Server(int port) {
		this.port = port;
	}

	public int getPort() {
		return port;
	}
	
	/**
	 * Waits for a request from client.
	 * This function has a 15 second timeout, and returns null after that.
	 * @return
	 */
	public abstract Request receiveRequest();
	
	public abstract void sendResponse(Response response);
	
	protected abstract void shutdownServer() throws IOException;
	
	public abstract void logMessage(String message);
	
	public abstract void logError(String message);
	
	protected void finalize() throws IOException {
		// If program ends, gracefully close the connection
		Utils.logMessage("Shutting down server.");
		this.shutdownServer();
	}
}