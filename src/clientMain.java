import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.Timer;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.Socket;

class clientMain extends JFrame implements ActionListener
{
	static final int UPDATE			= 3;
	static final int CHAT			= 4;
	static final int WIN			= 5;
	
	// Networking Variables
	Vector<String[]> serverCommands = new Vector<String[]>();
	clientReceiveThread receiveThread = new clientReceiveThread(serverCommands); 
	boolean threadStarted = false;
	
	//GUI
	public JTextField username;
    public JTextField ipInput;
    public JTextField portInput;
    public JButton connectButton;
    public JButton submit;
    public JLabel users;
    public JLabel word;
    
    public boolean connected = false;
    
    public JTextArea info;
    public JTextField input;
    
    Timer timer = new Timer();
    boolean restart;
    
	public static void main(String argv[]) throws Exception
	{	
		new clientMain();
	}
	
	class RemindTask extends TimerTask
	{
        public void run()
        {
        	timer.schedule(new RemindTask(), 500);
            
            while (serverCommands.isEmpty() == false)
    		{
    			String[] parseString = serverCommands.firstElement();
    			
    			switch (Integer.parseInt(parseString[0]))
    			{
    				case UPDATE:
    					handleNewRound(parseString);
    					break;
    				case CHAT:
    					info(parseString[1] + ": " + parseString[2]);
    					break;
    				case WIN:
    					info("============================");
    					if (parseString[1] == "NULL")
    						info("Not enough players, game is ending!");
    					else
    					{
    						info(parseString[1] + " HAS WON!");
    						info("New Round Starting!");
    					}
    					info("============================");
    					break;
    			}
    			
    			serverCommands.remove(0);
    		}
        }
	}
	
	public clientMain() throws IOException
	{
		timer.schedule(new RemindTask(), 500);
		JFrame frame = new JFrame("Guess the Word!");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(700, 450));
        
        Container content = frame.getContentPane();
        content.setBackground(Color.white);
        content.setLayout(new BorderLayout());
        
        JPanel connectBar = new JPanel(new FlowLayout());
        
        word = new JLabel("THE WORD");
        word.setPreferredSize(new Dimension(50, 200));
        content.add(word, BorderLayout.EAST);
        
        content.add(connectBar, BorderLayout.PAGE_START);
        
        users = new JLabel("Users");
        users.setVerticalAlignment(SwingConstants.TOP);
        users.setPreferredSize(new Dimension(150, 300));
        content.add(users, BorderLayout.WEST);
        
        info = new JTextArea("");
        JScrollPane scroller = new JScrollPane(info);
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroller.setPreferredSize(new Dimension(500, 200));
        
        input = new JTextField();
        input.setEditable(false);
        input.setPreferredSize(new Dimension(347,30));
        
        content.add(scroller, BorderLayout.CENTER);
        
        JPanel inputBar = new JPanel(new FlowLayout());
        
        submit = new JButton("Send");
        submit.addActionListener(this);
        
        inputBar.add(input);
        inputBar.add(submit);
        content.add(inputBar, BorderLayout.PAGE_END);
        
        JLabel unLabel = new  JLabel("Username:");
        username = new JTextField(11);
        username.setPreferredSize(new Dimension(250,30));
        connectButton = new JButton("Connect");
        connectButton.setPreferredSize(new Dimension(110, 30));
        connectButton.addActionListener(this);
        JLabel ipLabel = new JLabel("Host Address:");
        JLabel portLabel = new JLabel("Port:");
        ipInput = new JTextField(11);
        ipInput.setPreferredSize(new Dimension(250,30));
        portInput = new JTextField(4);
        portInput.setPreferredSize(new Dimension(75, 30));
        
        connectBar.add(unLabel);
        connectBar.add(username);
        connectBar.add(ipLabel);
        connectBar.add(ipInput);
        connectBar.add(portLabel);
        connectBar.add(portInput);
        connectBar.add(connectButton);
        
        frame.pack();
        frame.setVisible(true);
	}
	
	public void info(String s)
	{
		info.append(s + "\n");
		pack();
	}
	public void handleNewRound(String[] parseString)
	{
		info("NEW ROUND STARTING!");
		info("The scrambled word is: " + parseString[1]);
		
		String[] myUsers = parseString[2].split(" ");
		String userList = "";
		for (int i = 0; i < myUsers.length/2; i = i + 2)
		{
			userList = userList + myUsers[i] + " " + myUsers[i+1] + "\n";
		}
		
		users.setText(userList);
		word.setText(parseString[1]);
	}
	 
	public void connectionToServer(String ip, int port, boolean connect) throws IOException
	{
		if (threadStarted == false)
		{
			receiveThread.start();
			threadStarted = true;
		}

		String connectString = new String();
		
		if (connect == true)
		{
			connectString = "0 " + username.getText();
			connectButton.setText("Disconnect");
	        connected = true;
	        input.setEditable(true);
		}
		else
		{
			connectString = "2 " + username.getText();
			connectButton.setText("Connect");
	        connected = false;
	        input.setEditable(false);
		}
		
    	sendStringtoServer(connectString);
    }
	
	public void sendStringtoServer(String sendString) throws IOException
	{
		SocketAddress clientSocket = new InetSocketAddress(ipInput.getText(),Integer.parseInt(portInput.getText()));
    	Socket mySocket = new Socket();
    	mySocket.connect(clientSocket);
    	DataOutputStream outToClient = new DataOutputStream(mySocket.getOutputStream());
    				
    	outToClient.writeBytes(sendString);
    	mySocket.close();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getActionCommand().equals("Connect"))
		{
			info("Connecting to " + ipInput.getText() + ":" + portInput.getText());
        	
        	try {connectionToServer(ipInput.getText(), Integer.parseInt(portInput.getText()),true);}
        	catch (NumberFormatException | IOException e1) {e1.printStackTrace();}
        }
		else if(e.getActionCommand().equals("Disconnect"))
		{
        	try {connectionToServer(ipInput.getText(), Integer.parseInt(portInput.getText()),false);}
        	catch (NumberFormatException | IOException e1) {e1.printStackTrace();}
		}
		else if(e.getActionCommand().equals("Send"))
		{
			String sendString = "1 " + username.getText() + " " + input.getText();
			try {sendStringtoServer(sendString);}
			catch (IOException e1) {e1.printStackTrace();}
		}
	}
}