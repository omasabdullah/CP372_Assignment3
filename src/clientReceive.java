import java.io.*;
import java.net.*;
import java.util.*;

public class clientReceive extends Thread
{
	static final int NOT_SENT = 0;
	static final int SENT = 1;
	static final int ACKD = 2;
	
    protected DatagramSocket socket = null;
    Vector<String> sendVector = new Vector<String>();
    Vector<Integer> ackPackets = new Vector<Integer>();
	
	public clientReceive(Vector<String> myVector, Vector<Integer> myAckPackets, int receivePort) throws IOException
	{
		this("clientReceive");
		System.out.println("Server up");
		socket = new DatagramSocket(receivePort);
		sendVector = myVector;
		ackPackets = myAckPackets;
	}
	
	public clientReceive(String name) throws IOException
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

        	    // display response
                String received = new String(packet.getData(), 0, packet.getLength());

                System.out.println("Received ACK: " + received + "\n");
                int ackNumber = Integer.parseInt(received);
                
                ackPackets.set(ackNumber, ACKD);
                
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}