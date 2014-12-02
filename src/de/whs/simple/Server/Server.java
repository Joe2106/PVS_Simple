package de.whs.simple.Server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;



import de.whs.simple.Content;
import de.whs.simple.Simple;

public class Server {

	public static void main(String[] args) {
		try {
			(new Server(4711)).loop();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	private final static long TIMEOUT = 60 * 1000;
	private static ExecutorService executorService;
	
	//String -> Username
	Map<String, User> users = new HashMap<String, User>();
	Selector events = null;
	ServerSocketChannel listenchannel;
	
	public Server (int port) throws IOException{
		
		events = Selector.open();
		listenchannel = ServerSocketChannel.open();
		listenchannel.configureBlocking(false);
		listenchannel.socket().bind(new InetSocketAddress(port));
		listenchannel.register(events, SelectionKey.OP_ACCEPT);
		executorService = Executors.newFixedThreadPool(1);
		executorService.submit(new Runnable(){
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(1000);
						List<String> inactiveUsers = users
								.entrySet().stream()
								.filter(x -> Duration.between(x.getValue().getLastMessage(),
																Instant.now()).toMillis() >= TIMEOUT)
								.map(x -> x.getKey())
								.collect(Collectors.toList());
						for (String key : inactiveUsers)
							kickUser(users.get(key).getChannel(),key);
						}
						
					 catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
	}

	public void loop() throws IOException{
		Iterator<SelectionKey> selKeys;
		System.out.println("Server started...");
		while(true){
			
			events.select(); // blocks until event occurs
			// process all pending events (might be more than 1)
			selKeys = events.selectedKeys().iterator();
			while (selKeys.hasNext()) {
				SelectionKey selKey = selKeys.next();
				selKeys.remove();

				if (selKey.isReadable()) {
					processRead(selKey);
				} else if (selKey.isAcceptable()) {
					processAccept();
				} else {
					System.out.println("Unknown event occured");
				}
			}
		}
	}
	
	private void kickUser(SocketChannel channel, String name ) throws UnsupportedEncodingException, IOException{
		users.remove(name);
		Client_Connection.sendMessage(Simple.SendLogout(), channel);
		
		for(Entry<String, User> user : users.entrySet()){
			Client_Connection.sendMessage(Simple.SendNotifyUserLeft(name), user.getValue().getChannel());
		}		
	}
	
	private String getUserNameFromSocket(SocketChannel channel) {
		return users.entrySet().stream()
				.filter(x -> x.getValue().getChannel().equals(channel))
				.findFirst().get().getKey();
	}
			
	void processAccept() {

		SocketChannel talkChannel = null;
		try {
			talkChannel = listenchannel.accept();
			talkChannel.configureBlocking(false);
			talkChannel.register(events, SelectionKey.OP_READ);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			try { // always try to close talkChannel
				talkChannel.close();
			} catch (IOException ignore) {
			}
		}
	}
	
	@SuppressWarnings("incomplete-switch")
	void processRead(SelectionKey selKey) throws IOException{
		SocketChannel talkChan = null;
		// get the channel with the read event
		talkChan = (SocketChannel) selKey.channel();
		String msg = Client_Connection.readMessage(talkChan);
		List<String> otherUser;
		Content content = Simple.getContentType(msg);
		
		if(content != null){
			switch(content){
			case login:
				if(users.containsKey(Simple.ReceiveLoginResponse(msg)))
					Client_Connection.sendMessage(Simple.SendError("Username allready exist"), talkChan);
				else{
					//send Userlist
					Client_Connection.sendMessage(Simple.SendUserlistResponse((String[]) users.keySet().toArray()), talkChan);
					//put user to userlist
					users.put(Simple.ReceiveLoginResponse(msg), new User(talkChan, Instant.now()));
					
					//notify all other users
					otherUser = getAllOther(talkChan);
					for(String user : otherUser){
						Client_Connection.sendMessage(Simple.ReceiveLoginResponse(msg), users.get(user).getChannel());
					}
				}
				break;
			case logout:
				users.remove(getUserNameFromSocket(talkChan));
				for(Entry<String, User> user : users.entrySet()){
					Client_Connection.sendMessage(Simple.SendNotifyUserLeft(getUserNameFromSocket(talkChan)), user.getValue().getChannel());
				}
				break;
			case userlist:
				otherUser = getAllOther(talkChan);
				Client_Connection.sendMessage(Simple.SendUserlistResponse((String[]) otherUser.toArray()), talkChan);
				break;
				
			case send:
				otherUser = getAllOther(talkChan);
				for(String other : otherUser)
					Client_Connection.sendMessage(Simple.SendMessage(Simple.ReceiveMessage(msg)),users.get(other).getChannel());
				break;
			}
		}
	}
	
	private List<String> getAllOther(SocketChannel channel) {
		return users.entrySet().stream()
				.filter(x -> !x.getValue().getChannel().equals(channel))
				.map(x -> x.getKey()).collect(Collectors.toList());
	}
}