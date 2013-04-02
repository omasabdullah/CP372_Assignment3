import java.io.*;
import java.util.*;

public class serverGameHandler extends Thread
{
	static final int NOT_STARTED = 0;
	static final int IN_PROGRESS = 1;
	static final int GAME_OVER = 2;
	
	
    Vector<String[]> receivedStrings = new Vector<String[]>();
    int gameState = NOT_STARTED;
	
	public serverGameHandler(Vector<String[]> myString) throws IOException
	{
		this("serverGameHandler");
		System.out.println("GameHandler Running...");

		receivedStrings = myString;
		
	}
	
	public serverGameHandler(String name) throws IOException
	{
		super(name);
	}
	
	public void run()
    {
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
}