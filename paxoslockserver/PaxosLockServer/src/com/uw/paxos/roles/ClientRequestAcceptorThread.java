package com.uw.paxos.roles;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import com.uw.paxos.connection.Request;
import com.uw.paxos.connection.Server;
import com.uw.paxos.connection.UDPServer;
import com.uw.paxos.messages.ClientId;
import com.uw.paxos.messages.ClientMessage;
import com.uw.paxos.messages.ClientMessageType;
import com.uw.paxos.utils.Utils;

/**
 * ClientRequestAcceptor class that can be run as single thread.
 * This class starts a UDP server and listens to requests from clients
 * on the port specified in constructor. All requests from clients are 
 * placed on synchronized blocking queue. This queue notifies Proposer 
 * thread to act on the request. *
 * This class handles following requests from clients:
 * - AcquireLock
 * - ReleaseLock
 * 
 * @author Samiksha Sharma
 *
 */
public class ClientRequestAcceptorThread extends StoppableLoopThread {

	BlockingQueue<ClientMessage> clientRequestQueue;
	private Server server;
	
	public ClientRequestAcceptorThread(BlockingQueue<ClientMessage> clientRequestQueue, int portNumber) {
		this.clientRequestQueue = clientRequestQueue;
		try {
	        server = new UDPServer(portNumber);
        } catch (IOException ex) {
	        Utils.logError("Unable to bind to port : " + portNumber + ". Exception: " + ex.getMessage());
        }
	}
	
	@Override
    public void doProcessing() {
		Request request = server.receiveRequest();
		ClientMessage clientMessage = null;
		
		if (request == null) {
			// Send dummy request to ProposerThread
			// This would ensure that ProposerThread is not blocked on queue.take() forever. 
			// Doing this would wake up ProposerThread so that it can check for shutdownRequest if any.
			clientMessage = new ClientMessage();
			clientMessage.setMessageType(ClientMessageType.DUMMY_REQUEST);
		} else {
			Utils.logMessage(this.getClass().getSimpleName() + " received request: " + request.getMessage());
			clientMessage = ClientMessage.fromString(request.getMessage());
			ClientId clientId = new ClientId(request.getSenderIpAddress(), request.getSenderPort());
			clientMessage.setClientId(clientId);
		}
		
		try {
			clientRequestQueue.put(clientMessage);
		} catch (InterruptedException ex) {
			Utils.logError(this.getClass().getSimpleName() + " encountered error while placing client request on queue. \nError : " + ex.getMessage());
		}
    }
}
