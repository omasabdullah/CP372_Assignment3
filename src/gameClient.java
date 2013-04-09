import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;


class gameClient extends JFrame implements ActionListener
{
	Vector<String[]> clientCommands; 
	clientReceiveThread t; 
	
    public JTextField ipInput;
    public JTextField portInput;
    public JButton connectButton;
    public JButton submit;
    public JLabel users;
    public JTextField username;
    public JLabel word;
    
    public boolean connected = false;
    
    public JTextArea info;
    public JTextField input;
    
    public Socket clientSocket;
    public PrintWriter out;
    public Scanner in;
    
	public static void main(String argv[]) throws Exception
	{	
		new gameClient();	
	}
	
	public void info(String s)
	{
		info.append(s + "\n");
		pack();
	}
	 
	public void connect(String ip, int port) throws IOException
	{
        String fromServer;
        clientSocket = null;
        out = null;
        in = null;

        try
        {
            //kkSocket = new Socket("192.219.237.39", 4444);
            clientSocket = new Socket(ip, port);
            /*out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new Scanner(clientSocket.getInputStream());*/
            
            String sentence;
    		String modifiedSentence;
      
    		BufferedReader inFromUser = new BufferedReader( new InputStreamReader(System.in));
    		
    		
    		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
    		
    		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    		
    		sentence = username.getText();
    		//outToServer.writeBytes('0 ' + sentence);
    		//modifiedSentence = inFromServer.readLine();
    		//System.out.println("FROM SERVER: " + modifiedSentence);
    		clientSocket.close();
            
            
            
            connectButton.setText("Disconnect");
            connected = true;
            input.setEditable(true);
            
            t.start();
        }
        catch (UnknownHostException ee)
        {
            //System.err.println("Don't know about host: taranis.");
            info("Don't know about host: " + ip + ":" + port);
            //System.exit(1);
        }
        catch (IOException ee)
        {
            info("Couldn't get I/O for the connection to: " + ip + ":" + port);
           // System.exit(1);
        }
    }
	
	public gameClient() throws IOException
	{
		clientCommands = new Vector<String[]>();
		t = new clientReceiveThread();
		
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

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getActionCommand().equals("Connect"))
		{
			info("Connecting to " + ipInput.getText() + ":" + portInput.getText());
            try
            {
                connect(ipInput.getText(), Integer.parseInt(portInput.getText()));
            }
            catch (NumberFormatException e1)
            {
                info("Could not connect.");
            }
            catch (IOException e1)
            {
                info("Could not connect.");
            }
        }
		else if(e.getActionCommand().equals("Send"))
		{
			String send = "1 " + username.getText() + " " + input.getText();
		}
	}
}