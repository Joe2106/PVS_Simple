package de.whs.simple.Client;

import de.whs.simple.Simple;
import java.io.IOException;

public class ProcessInput //extends Thread
{
	public static void sendMessage(String input)
	{
        String output = Simple.SendMessage(input);
        try 
        {
			Client.send(output);
	        Client.display(output);
		} catch (IOException e) 
        {
			Client.display("Connection Error");
		}
	}
	public static void login(String input)
	{
        String output = Simple.SendLoginResponse(input);
        try
        {
			Client.send(output);
	        Client.display(output);
		} catch (IOException e)
        {
	        Client.display("Connection Error");
		}
	}
	public static void logout()
	{
		String output = Simple.SendLogout();
		try
		{
			Client.send(output);
		}
		catch (IOException e)
		{
	        Client.display("Connection Problem");
		}
	}
	public static void getUser()
	{
		String output = Simple.SendUserlistRequest();
		try
		{
			Client.send(output);
		}
		catch (IOException e)
		{
			Client.display("Connection Problem");
		}
	}
    /* Fuer Konsole:
     * static Pattern send = Pattern.compile("/send +.");
    static Pattern login = Pattern.compile("/login +[A-Za-z0-9]");
    static Pattern logout = Pattern.compile("/logout");
    static Pattern getUser = Pattern.compile("/getUser");
    
    public void run()
    {
        String input;
        String output;
        while (true)
        {
            try
            {
                input = Client.getInput();
                if (send.matcher(input).matches())
                {
                    input.replace("/send ", "");
                    output = Simple.SendMessage(input);
                    Client.send(output);
                    Client.display(output);
                }
                else if(login.matcher(input).matches())
                {
                    input.replace("/login ", "");
                    output = Simple.SendLoginResponse(input);
                    Client.send(output);
                    Client.display(output);
                }
                else if(logout.matcher(input).matches())
                {
                    output = Simple.SendLogout();
                    Client.send(output);
                }
                else if(getUser.matcher(input).matches())
                {
                    output = Simple.SendUserlistRequest();
                    Client.send(output);
                }
                else
                {
                    output = "unknown command";
                    Client.display(output);
                }
            }
            catch(IOException e)
            {
                Client.display("Connection Problem");
            }
        }
    }*/	
}
