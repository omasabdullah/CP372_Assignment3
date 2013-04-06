import java.io.*;
import java.net.*;
import java.util.*;

public class serverClientHandler extends Thread
{
    int receivePort;
    Vector<String[]> clientCommands = new Vector<String[]>();
    InetAddress hostAddress;
	
	public serverClientHandler(int receivePort, Vector<String[]> myString) throws IOException
	{
		this("serverClientHandler");
		System.out.println("ClientHandler Running...");
		
		this.receivePort = receivePort;
		clientCommands = myString;
	}
	
	public serverClientHandler(String name) throws IOException
	{
		super(name);
	}
	
	public void run()
    {
        while (true)
        {
            try
            {
            	
            	ServerSocket welcomeSocket = new ServerSocket(receivePort);
            	Socket connectionSocket = welcomeSocket.accept();
            	
            	BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            	String clientInput = inFromClient.readLine();
            	String[] singleClientCommand = clientInput.split(" ");
            	clientCommands.add(singleClientCommand);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}