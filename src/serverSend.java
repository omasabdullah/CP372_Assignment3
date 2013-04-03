import java.io.*;
import java.net.*;
import java.util.*;

public class serverSend extends Thread
{
	
	static final int STOP = 0;
	static final int GOBACK = 1;
	
    protected DatagramSocket socket = null;
    protected BufferedReader in = null;
    protected boolean testing = true;
    protected int state = 0;
	
	public serverSend() throws IOException
	{
		this("clientThread");
	}
	
	public serverSend(String name) throws IOException
	{
		super(name);

        socket = new DatagramSocket(4445);

        try
        {
            in = new BufferedReader(new FileReader("one-liners.txt"));
        }
        catch (FileNotFoundException e)
        {
            System.err.println("Could not open file.");
        }
	}
	
	public void run()
    {
        while (testing)
        {
            try
            {
                byte[] buf = new byte[128];

                // receive request
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                // figure out response
                String dString = null;
                
                buf = dString.getBytes();

                //send the response to the client at "address" and "port"
                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                packet = new DatagramPacket(buf, buf.length, address, port);
                socket.send(packet);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        socket.close();
    }
	
    public static void main(String[] args) throws IOException
    {
    	/*int connectionType = 0;
    	
        if (args.length == 5) 		{connectionType = STOP;}
        else if (args.length == 6) 	{connectionType = GOBACK;}
        else
        {
        	System.out.println("Usage: java QuoteClient <hostname>");
            return;
        }
    	
    	String[] connectionInfo = new String[6];
    	
    	for (int i = 0; i < args.length; i++)
    	{
    		connectionInfo[i] = args[i];
    		System.out.print(connectionInfo[i]);
    	}
    	*/

            // get a datagram socket
        DatagramSocket socket = new DatagramSocket();

            // send request
        byte[] buf = new byte[124];
        //InetAddress address = InetAddress.getByName(args[0]);
        InetAddress address = InetAddress.getByName("192.219.237.50");
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 4445);
        socket.send(packet);
    
            // get response
        packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);

	    // display response
        String received = new String(packet.getData(), 0, packet.getLength());
        System.out.println("Quote of the Moment: " + received);
    
        socket.close();
    }
}