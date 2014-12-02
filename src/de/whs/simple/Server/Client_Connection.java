package de.whs.simple.Server;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Client_Connection {

	public static void sendMessage(String msg, SocketChannel client) throws UnsupportedEncodingException, IOException{
		String nachricht = msg + "\n";
		client.write(ByteBuffer.wrap(nachricht.getBytes("Cp1252")));
	}
	
	public static String readMessage(SocketChannel sChannel) throws IOException{
		ByteBuffer recvBuffer = ByteBuffer.allocate(1024);
		int numBytesRead = sChannel.read(recvBuffer);

		switch (numBytesRead) {
			case -1:
				return null;
			case 0:
				return null;
			default:
				if (recvBuffer.get(numBytesRead - 1) != 10)
					throw new IOException("Message Frame error");
				String msg = new String(recvBuffer.array(), "Cp1252").trim();
				return msg;
		}
	}
}