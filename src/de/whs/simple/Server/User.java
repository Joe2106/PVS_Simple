package de.whs.simple.Server;

import java.nio.channels.SocketChannel;
import java.time.Instant;

public class User {
	private SocketChannel sChannel;
	private Instant lastMessage = Instant.now();
	
	public SocketChannel getChannel() {
		return sChannel;
	}
	public void setChannel(SocketChannel channel) {
		this.sChannel = channel;
	}
	public Instant getLastMessage() {
		return lastMessage;
	}
	public void setLastMessage(Instant lastMessage) {
		this.lastMessage = lastMessage;
	}
	
	public User(SocketChannel channel, Instant lastMessage) {
		super();
		this.sChannel = channel;
		this.lastMessage = lastMessage;
	}
}