import java.io.*;
import java.util.Random;
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
    
    String[] scrambledWords = {"amiz", "rowbn", "leham", "iahco", "reggin"};
    String[] solvedWords = {"zima", "brown", "hamel", "chiao", "nigger"};
    int currentWord;
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
				e.printStackTrace();
			}
			
			switch (gameState)
			{
				// Preparing game puzzle
				case NOT_STARTED:
				{
					if (connectedPlayers.size() > 1)
					{
						try {startGame();}
						catch (IOException e) {e.printStackTrace();}
					}
				} break;
				// Game in Progress
				case IN_PROGRESS:
				{
					if (connectedPlayers.size() < 2)
					{
						try {endGame("");}
						catch (IOException e) {e.printStackTrace();}
					}
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
				case CONNECT:	handlePlayer(parseString[0], parseString[2], false);		break;
				case COMMAND:	handleChat(parseString[2], parseString[3]);					break;
				case QUIT:		handlePlayer(parseString[0], parseString[2], true);			break;
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
			String[] newPlayer = {ipAddress, playerName, "0"};
			connectedPlayers.add(newPlayer);
			System.out.println("Adding following player to session: " + connectedPlayers.lastElement()[1]);
		}
	}
	
	public void endGame(String winningPlayer) throws IOException
	{
		System.out.println("Game Over " + winningPlayer + " wins.");
		
		for (int i = 0; i < connectedPlayers.size(); i++)
		{
			if (connectedPlayers.get(i)[1].equals(winningPlayer))
			{
				System.out.println("Awarding following player a point: " + winningPlayer);

				int currentScore = Integer.parseInt(connectedPlayers.get(i)[2]);
				currentScore = currentScore + 1;
				connectedPlayers.get(i)[2] = Integer.toString(currentScore);
			}
		}
		
		String sendString;
		
		if (winningPlayer == "")
			sendString = WIN + " " + "NULL";
		else
			sendString = WIN + " " + winningPlayer;
		
		sendStringToAllClients(sendString);
		
		gameState = NOT_STARTED;
	}
	
	public void startGame() throws IOException
	{
		gameState = IN_PROGRESS;
		Random randomGenerator = new Random();
		currentWord = randomGenerator.nextInt(scrambledWords.length);
		
		String sendString = UPDATE + " " + scrambledWords[currentWord];
		
		for (int i = 0; i < connectedPlayers.size(); i++)
		{
			sendString = sendString + " " + connectedPlayers.get(i)[1] + " " + connectedPlayers.get(i)[2];
		}
		
		sendStringToAllClients(sendString);
	}
	
	public void handleChat(String playerName, String chatString) throws IOException
	{	
		if (gameState == IN_PROGRESS && chatString.equals(solvedWords[currentWord]))
			endGame(playerName);

		String sendString = CHAT + " " + playerName + " " + chatString;
		
		sendStringToAllClients(sendString);
	}
	
	public void sendStringToAllClients(String sendString) throws IOException
	{
		for (int i = 0; i < connectedPlayers.size(); i++)
		{
			String[] clientIPArray = connectedPlayers.get(i)[0].split(":");
			String clientIP = clientIPArray[0];

			SocketAddress clientSocket = new InetSocketAddress(clientIP,6001);
			Socket mySocket = new Socket();
			
			mySocket.connect(clientSocket);
			
			DataOutputStream outToClient = new DataOutputStream(mySocket.getOutputStream());
			
			outToClient.writeBytes(sendString);
			mySocket.close();
		}
	}
}