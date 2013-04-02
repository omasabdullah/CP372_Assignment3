import java.io.*;
import java.util.*;

public class serverGameHandler extends Thread
{
    Vector<String[]> receivedStrings = new Vector<String[]>();
	
	public serverGameHandler(Vector<String[]> myString) throws IOException
	{
		this("serverGameHandler");
		System.out.println("GameHandler Running...");
		receivedStrings = myString;
	}
	
	public serverGameHandler(String name) throws IOException
	{
		super(name);
	}
	
	public void run()
    {
		
    }
}