import java.io.*;
import java.net.*;
import java.util.*;

public class clientSide
{	
	static final int NOT_SENT = 0;
	static final int SENT = 1;
	static final int ACKD = 2;
	
	public static void main(String[] args) throws Exception
    {	
		if (args.length != 5)
		{
            System.out.println("Wrong paramaters: Use Hostaddress, send port, receive port, filename");
            return;
		}
		
		InetAddress hostAddress = InetAddress.getByName(args[0]);
		int destinationPort = Integer.parseInt(args[1]);
		int receivePort = Integer.parseInt(args[2]);
		String fileName = args[3];
		int reliabilityNumber = Integer.parseInt(args[4]);
		
		File sendFile = new File(fileName);
		
		Vector<String> myVector = new Vector<String>();
		Vector<Integer> ackPackets = new Vector<Integer>();
		toStringArray(sendFile, myVector);
		
		for (int i = 0; i < myVector.size(); i++)
			ackPackets.add(NOT_SENT);
		
		clientSend sendThread = new clientSend(myVector, ackPackets, hostAddress, destinationPort );
		clientReceive receiveThread = new clientReceive(myVector, ackPackets, receivePort);

		
		sendThread.start();
		receiveThread.start();
	}
	
	public static void toStringArray(File file, Vector<String> myVector) throws FileNotFoundException, IOException
	{  
		FileInputStream fis = new FileInputStream(file);
		String myString = "";
		int count = 0;
		char current;
	    while (fis.available() > 0)
	    {
	        current = (char) fis.read();
	        myString += current;
	        count++;
	        
	        if (count == 120)
	        {
	        	myVector.add(myString);
	        	count = 0;
	        	myString = "";
	        }
	    }
	    
	    if (count != 0)
	    {
	    	myVector.add(myString);
        	count = 0;
        	myString = "";
	    }
	    fis.close();
    }
}