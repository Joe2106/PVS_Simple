package de.whs.simple.Client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

import de.whs.simple.*;

public class Receive extends Thread
{
    static Socket talkSocket;
    static BufferedReader fromServer;
    public void run()
    {
        String received;
        try
        {
            while(true)
            {
                received = fromServer.readLine();
                switch(Simple.getContentType(received))
                {
                    case send: Client.display(Simple.ReceiveMessage(received));
                    case notifyNewUser: Client.display(Simple.ReceiveNotify(received));
                    case notifyUserLeft: Client.display(Simple.ReceiveNotify(received));
                    case userlist: Client.display(Simple.ReceiveUserlist(received));
                    case error: Client.display(Simple.ReceiveError(received));
                    case login:
                    case logout:
                    default:
                }
            }
        }
        catch (IOException e)
        {
            Client.display(e.getMessage());
        }
    }
    public Receive(Socket tS)
    {
        talkSocket = tS;
        try
        {
            fromServer = new BufferedReader(new InputStreamReader(talkSocket.getInputStream(), "Cp1252"));
        }
        catch(Exception e)
        {
            Client.display("Connection error");
        }
    }
}
