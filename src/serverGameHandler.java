import java.io.*;
import java.util.*;

public class serverGameHandler extends Thread
{
	// States for the game
	static final int NOT_STARTED 	= 0;
	static final int IN_PROGRESS 	= 1;
	static final int GAME_OVER 		= 2;
	
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
		handleConnectionStrings();
		
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
			// Assign winner, reset scores, etc.
			case GAME_OVER:
			{
				
			} break;
		}
    }
	
	public void handleConnectionStrings()
	{
		while (receivedStrings.isEmpty() == false)
		{
			String[] parseString = receivedStrings.firstElement();
			
			switch (Integer.parseInt(parseString[0]))
			{
				case CONNECT:	handlePlayer(parseString[1], false);		break;
				case COMMAND:	handleChat(parseString[1], parseString[2]);	break;
				case QUIT:		handlePlayer(parseString[1], true);			break;
			}
			
			receivedStrings.remove(0);
		}
	}
	
	public void handlePlayer(String playerName, boolean removePlayer)
	{
		if (removePlayer == true)
		{
			System.out.println("Attempting to remove following player from session: " + playerName);
			boolean playerFound = false;
			
			for (int i = 0; i < connectedPlayers.size(); i++)
			{
				if (connectedPlayers.get(i)[0] == playerName)
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
			String[] newPlayer = {playerName, "0"};
			connectedPlayers.add(newPlayer);
		}
	}
	
	public void handleChat(String playerName, String chatString)
	{
		
	}
	
	public void sendStringToAllClients(String sendString)
	{
		
	}
}