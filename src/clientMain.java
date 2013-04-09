import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.Socket;


class clientMain extends JFrame implements ActionListener
{
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
    
	public static void main(String argv[]) throws Exception
	{	
		new clientMain();
	}
	
	public clientMain() throws IOException
	{		
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