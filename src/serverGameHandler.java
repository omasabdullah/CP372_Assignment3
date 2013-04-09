import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.Socket;
import java.util.*;

public class serverGameHandler extends Thread
{
	// States for the game
	static final int NOT_STARTED 	= 0;
	static final int IN_PROGRESS 	= 1;
	
	// OPcodes to be sent to server/client
	static final int CONNECT 		= 0;
	static final int COMMAND 		= 1;
	static final int QUIT 			= 2;
	static final int UPDATE			= 3;
	static final int CHAT			= 4;
	static final int WIN			= 5;
	
    Vector<String[]> receivedStrings = new Vector<String[]>();
    Vector<String[]> connectedPlayers = new Vector<String[]>();		//connectedPlayers setup as 0 - name, 1 - score.
    
    String[] scrambledWords = new String[20];
    String currentWord = new String();
    int gameState = NOT_STARTED;

	public serverGameHandler(Vector<String[]> myString, Vector<String[]> myPlayers) throws IOException
	{
		this("serverGameHandler");
		System.out.println("GameHandler Running...");

		receivedStrings = myString;
		connectedPlayers = myPlayers;
	}
	
	public serverGameHandler(String name) throws IOException
	{
		super(name);
	}
	
	public void run()
    {
		while (true)
		{
			try
			{
				handleConnectionStrings();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			switch (gameState)
			{
				// Preparing game puzzle
				case NOT_STARTED:
				{
	
				} break;
				// Game in Progress
				case IN_PROGRESS:
				{
					
				} break;
			}
		}
    }
	
	public void handleConnectionStrings() throws IOException
	{
		while (receivedStrings.isEmpty() == false)
		{
			String[] parseString = receivedStrings.firstElement();
			
			switch (Integer.parseInt(parseString[1]))
			{
				case CONNECT:	handlePlayer(parseString[1], parseString[2], false);		break;
				case COMMAND:	handleChat(parseString[1], parseString[2]);					break;
				case QUIT:		handlePlayer(parseString[1], parseString[2], true);			break;
			}
			
			receivedStrings.remove(0);
		}
	}
	
	public void handlePlayer(String ipAddress, String playerName, boolean removePlayer)
	{
		if (removePlayer == true)
		{
			System.out.println("Attempting to remove following player from session: " + playerName);
			boolean playerFound = false;
			
			for (int i = 0; i < connectedPlayers.size(); i++)
			{
				if (connectedPlayers.get(i)[1].equals(playerName))
				{
					connectedPlayers.remove(i);
					playerFound = true;
					System.out.println("Player: " + playerName + " found, removing from session.");
					break;
				}
			}
			
			if (playerFound == false)
				System.out.println("Player: " + playerName + " not found.");
		}
		else
		{
			System.out.println("Adding following player to session: " + playerName);
			String[] newPlayer = {ipAddress, playerName, "0"};
			connectedPlayers.add(newPlayer);
		}
	}
	
	public void endGame(String winningPlayer) throws IOException
	{
		gameState = NOT_STARTED;
		
		for (int i = 0; i < connectedPlayers.size(); i++)
		{
			if (connectedPlayers.get(i)[1] == winningPlayer)
			{
				System.out.println("Awarding following player a point: " + winningPlayer);
				
				String[] myPlayer = {connectedPlayers.get(i)[1], connectedPlayers.get(i)[2]}; 
				int currentScore = Integer.parseInt(myPlayer[1]);
				currentScore = currentScore + 1;
				myPlayer[1] = Integer.toString(currentScore);
				
				connectedPlayers.remove(i);
				connectedPlayers.add(myPlayer);
			}
		}
		
		String sendString = "5 " + winningPlayer;
		
		sendStringToAllClients(sendString);
	}
	
	public void startGame()
	{
		gameState = IN_PROGRESS;
	}
	
	public void handleChat(String playerName, String chatString) throws IOException
	{
		
		if (gameState == IN_PROGRESS)
			if (chatString == currentWord)
				endGame(playerName);
		
		String sendString = "4 " + playerName + " " + chatString;
		
		sendStringToAllClients(sendString);
	}
	
	public void sendStringToAllClients(String sendString) throws IOException
	{
		for (int i = 0; i < connectedPlayers.size(); i++)
		{
			String clientIP = connectedPlayers.get(i)[0];
			
			SocketAddress clientSocket = new InetSocketAddress(clientIP,6001);
			Socket mySocket = new Socket();
			
			mySocket.connect(clientSocket);
			
			DataOutputStream outToClient = new DataOutputStream(mySocket.getOutputStream());
			
			outToClient.writeBytes(sendString);
			mySocket.close();
		}
	}
}





