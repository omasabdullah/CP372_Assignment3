import java.io.*;
import java.net.*;
import java.util.*;

public class clientSend extends Thread
{
	static final int NOT_SENT = 0;
	static final int SENT = 1;
	static final int ACKD = 2;
	
    protected DatagramSocket socket = null;
    InetAddress hostAddress = null;
    int destinationPort;
    
    int currentPacket;
    Vector<String> sendVector = new Vector<String>();
    Vector<Integer> ackPackets = new Vector<Integer>();
	
	public clientSend(Vector<String> myVector, Vector<Integer> myAckPackets, InetAddress sendIP, int sendPort) throws IOException
	{
		this("clientThread");
		socket = new DatagramSocket();
		destinationPort = sendPort;
		hostAddress = sendIP;
		sendVector = myVector;
		currentPacket = 0;
		ackPackets = myAckPackets;
	}
	
	public clientSend(String name) throws IOException
	{
		super(name);
	}

	long preparePacketForSend(int packetNumber)
	{	
		String myString = "" + String.format("%04d", packetNumber) + sendVector.get(packetNumber);
		byte[] packetBody = myString.getBytes();
		DatagramPacket packet = new DatagramPacket(packetBody, packetBody.length, hostAddress, destinationPort);
		long startTime = System.currentTimeMillis();
		
		try
		{
			socket.send(packet);
			ackPackets.set(currentPacket, SENT);
			System.out.println("Packet: " + currentPacket + " sent at: " + startTime + "\n");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return startTime;
	}
	
	public void run()
    {	
        try
        {
        	long timeSent = System.currentTimeMillis();
        	
        	while (true)
        	{
        		boolean packetSent = false;
        		
        		if (ackPackets.get(ackPackets.size()-1) == ACKD)
        		{
        			System.out.println("File transfer Completed");
        			this.stop();
        		}
        		
        		for (int i = 0; i < ackPackets.size(); i++)
        		{
        			if (ackPackets.get(i) == SENT)
        				packetSent = true;
        		}
        		
        		if (packetSent == false)
        		{
        			for (int i = 0; i < ackPackets.size(); i++)
            		{
            			if (ackPackets.get(i) == NOT_SENT)
            			{
            				currentPacket = i;
            				break;
            			}
            		}
        			
        			System.out.println("Sending packet: " + currentPacket);
        			timeSent = preparePacketForSend(currentPacket);
        			
        		}
        		else
        		{
        			long timeNow = System.currentTimeMillis();
        			
        			if (timeNow - timeSent > 2000)
            		{
            			System.out.println("Timeout on packet: " + currentPacket + "...Resending");
            			timeSent = preparePacketForSend(currentPacket);
            		}
        		}
        	}
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}