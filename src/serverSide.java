import java.net.InetAddress;
import java.util.Vector;

public class serverSide
{
    public static void main(String[] args) throws Exception
    {
		if (args.length != 3)
		{
            System.out.println("Wrong paramaters: Use Hostaddress, receive port, send port");
            return;
		}
		
		InetAddress hostAddress = InetAddress.getByName(args[0]);
		int receivePort = Integer.parseInt(args[1]);
		int sendPort = Integer.parseInt(args[2]);
    	
    	Vector<String> receivedPackets = new Vector<String>();
    	
        serverReceive receiveThread = new serverReceive(hostAddress, receivePort, sendPort, receivedPackets);
        
        receiveThread.start();
    }
}