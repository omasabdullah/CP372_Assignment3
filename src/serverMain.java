import java.util.Vector;

public class serverMain
{
    public static void main(String[] args) throws Exception
    {
		/*if (args.length != 1)
		{
            System.out.println("Wrong paramaters: Specify port number");
            return;
		}*/
		
		int receivePort = 6000;//Integer.parseInt(args[0]);
    	
    	Vector<String[]> receivedStrings = new Vector<String[]>();
    	Vector<String[]> connectedPlayers = new Vector<String[]>();
    	
        serverClientHandler clientHandler = new serverClientHandler(receivePort, receivedStrings);
        serverGameHandler gameHandler = new serverGameHandler(receivedStrings, connectedPlayers);
        
        clientHandler.start();
        gameHandler.start();
    }
}