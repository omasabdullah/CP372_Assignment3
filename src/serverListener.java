import java.io.*;
import java.net.*;
import java.util.*;

public class serverListener extends Thread
{
    protected DatagramSocket socket = null;
    int destinationPort;
    Vector<String> receivedStrings = new Vector<String>();
    InetAddress hostAddress;
	
	public serverListener(int receivePort, Vector<String> myString) throws IOException
	{
		this("serverReceive");
		System.out.println("Server up");
		socket = new DatagramSocket(receivePort);
		receivedStrings = myString;
	}
	
	public serverListener(String name) throws IOException
	{
		super(name);
	}
	
	public void run()
    {
        while (true)
        {
            try
            {
                // send request
                byte[] buf = new byte[124];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                String received = new String(packet.getData(), 0, packet.getLength());
                receivedStrings.add(received);
                
                byte[] ack = new byte[124];
                
                String ackNumber = new String();
                
                for (int i = 0; i < 4; i++)
                	ackNumber += received.charAt(i);
                
                System.out.println("Received packet: " + ackNumber + ". Sending ACK");
                ack = ackNumber.getBytes();
                
                packet = new DatagramPacket(ack, ack.length, hostAddress, destinationPort);
                socket.send(packet);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}