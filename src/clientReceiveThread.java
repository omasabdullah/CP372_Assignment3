import java.io.*;
import java.net.*;
import java.util.*;

public class clientReceiveThread extends Thread
{
	Vector<String[]> serverCommands = new Vector<String[]>();
	
	public clientReceiveThread(Vector<String[]> serverCommands) throws IOException
	{
		this("clientReceiveThread");
		System.out.println("clientReceiveThread Running...");
		
		this.serverCommands = serverCommands;
	}
	
	public clientReceiveThread(String name) throws IOException
	{
		super(name);
	}
	
	public void run()
    {
		while (true)
        {
            try
            {
            	ServerSocket welcomeSocket = new ServerSocket(6001);
            	Socket connectionSocket = welcomeSocket.accept();
            	String ipAddress = connectionSocket.getRemoteSocketAddress().toString();
            	ipAddress = ipAddress.substring(1);
            	
            	BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            	String clientInput = inFromClient.readLine();
            	System.out.println("Received from server: " + clientInput);
            	String singleServerCommand[] = clientInput.split(" ",2);
            	serverCommands.add(singleServerCommand);
            	welcomeSocket.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}