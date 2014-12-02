package de.whs.simple.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Client {

	static Socket talkSocket;
	static OutputStreamWriter toServer;
	static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

	static Client_Mainframe mainframe;
	
	public static void main(String[] args)
	{
		try
        {
            talkSocket = new Socket ("localhost", 4711);
            toServer = new OutputStreamWriter(talkSocket.getOutputStream(), "Cp1252");
            Receive rE = new Receive(talkSocket);
            rE.start();
            
            try {
            	mainframe = new Client_Mainframe();
				mainframe.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
		
		
	}
	
	public static void display(String s)
	{
		mainframe.outline(s);
		//System.out.println(s);
	}
	
	public static void display(String[] sa)
	{
	    for(String s: sa)
	    {
	        display(s);
	    }
	}
	
	public static void send(String s) throws IOException
	{
		toServer.write(s);
		toServer.flush();
	}
	
	public static String getInput() throws IOException
	{
	       String input;
	       input = in.readLine();
	       return input;
	}
}