package com.uw.paxos.connection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.uw.paxos.messages.PaxosMessage;

public class Multicast {

	public void sendMulticast(PaxosMessage paxosMessage)
	{
		DatagramSocket socket1 = null;
		DatagramPacket outPacket1 = null;
		final int PORT = 8888;
		byte[] outBuf = null;
		try {
			socket1 = new DatagramSocket();
			InetAddress address = InetAddress.getByName("224.2.2.4");
			outBuf = paxosMessage.toString().getBytes();

			String string1 = new String(outBuf);
			System.out.println("String sending is " + string1);
			outPacket1 = new DatagramPacket(outBuf, outBuf.length, address,
					PORT);
			// outBuf=Integer.toString(proposalNumber).getBytes();
			socket1.send(outPacket1);
			System.out.println("Sent:" + string1);
			// socket1.close();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
