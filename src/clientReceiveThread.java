import java.io.*;
import java.net.*;
import java.util.*;

public class clientReceiveThread extends Thread
{
	public clientReceiveThread() throws IOException
	{
		this("serverClientHandler");
		System.out.println("ClientHandler Running...");
	}
	
	public clientReceiveThread(String name) throws IOException
	{
		super(name);
	}
	
	public void run()
    {
    }
}