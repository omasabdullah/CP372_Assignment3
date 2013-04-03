import java.net.InetAddress;
import java.util.Vector;

public class serverSide
{
    public static void main(String[] args) throws Exception
    {
		if (args.length != 1)
		{
            System.out.println("Wrong paramaters: Specify port number");
            return;
		}
		
		int receivePort = Integer.parseInt(args[0]);
    	
    	Vector<String> receivedPackets = new Vector<String>();
    	
        serverListener receiveThread = new serverListener(receivePort, receivedPackets);
        
        receiveThread.start();
    }
}