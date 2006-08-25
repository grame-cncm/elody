package grame.elody.editor.constructors;

import grame.elody.editor.expressions.ExprHolder;
import grame.elody.editor.expressions.TNotesVisitor;
import grame.elody.editor.misc.Define;
import grame.elody.editor.misc.applets.BasicApplet;
import grame.elody.file.parser.TFileContent;
import grame.elody.file.parser.TFileParser;
import grame.elody.file.saver.TFileSaver;
import grame.elody.lang.TExpMaker;
import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.lang.texpression.expressions.TNullExp;
import grame.elody.util.MsgNotifier;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Choice;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Observable;
import java.util.StringTokenizer;

public class TPublisher extends BasicApplet implements Runnable, ActionListener
{
	static String publishCommand = "Publish";
	static String fetchCommand = "Fetch";
	static String connectCommand = "Connect";

	TextField port,host,title,author;
	TextArea destext;
	ExprHolder  displayer;
	Choice  filelist;
	Button publish, fetch,connect;
	boolean connected = false;
	 
	Socket socket = null;

	Thread 	client;
	boolean stopClient = false;
	BufferedReader in = null;
	PrintStream out = null;
//	PrintWriter out = null;
	Hashtable<String, String> urlTable;
	Panel bottomPanel1;
		
	public TPublisher (){	
		super ("Publisher");
		urlTable = new Hashtable<String, String>();
		setSize(270,300);
	}
	
	public void init(){
		Panel topPanel= new Panel();
		Panel bottomPanel= new Panel();
	    bottomPanel1= new Panel();
		Panel botPanel= new Panel();
		Panel centerPanel= new Panel();
		Panel titlePanel = new Panel();
		Panel authorPanel = new Panel();
		Panel desPanel = new Panel();
	
		setLayout(new BorderLayout(2, 5));
		
		displayer = new ExprHolder(null, new TNotesVisitor(),true);
		displayer.addObserver (this);
		filelist = new Choice();
		
		publish = CreateCommand (publishCommand);
		fetch 	= CreateCommand (fetchCommand);
		connect = CreateCommand (connectCommand);
	
		port = new TextField(new Integer(TProtocol.port).toString(),10);
		port.setEditable(false);
		host = new TextField("java.grame.fr", 15);
  				
  		topPanel.setLayout(new GridLayout(2,2,1,1));
  		
  		topPanel.add(new Label ("Port"));
  		topPanel.add(port);
  		
  		topPanel.add (new Label ("Server host"));
  		topPanel.add (host);
  	
  		add("North",topPanel);  
  		
 		/// TITLE
		titlePanel.setLayout(new GridLayout(2,1,1,1));
		titlePanel.add(new Label ("Title"));
		title =  new TextField("", 15);
		titlePanel.add(title);
		
		
		/// AUTHOR
		authorPanel.setLayout(new GridLayout(2,1,1,1));
		authorPanel.add(new Label ("Author"));
		author =  new TextField("", 15);
		authorPanel.add(author);
		
		Panel titleAuthorPanel = new Panel();
		titleAuthorPanel.setLayout(new GridLayout(2,1,1,1));
		titleAuthorPanel.add(titlePanel);
		titleAuthorPanel.add(authorPanel);
		
		/// DESRIPTION
		destext = new TextArea(1,1);
		desPanel.setLayout(new BorderLayout());
		desPanel.add("West",new Label ("Description"));
		desPanel.add("Center",destext);
		
		
		centerPanel.setLayout(new GridLayout(3,1,1,1));
		centerPanel.add(displayer);
		centerPanel.add(titleAuthorPanel);
		centerPanel.add(desPanel);
		
		add("Center",centerPanel);
  		
  		bottomPanel.setLayout(new GridLayout(1,2,5,5));
  		bottomPanel1.setLayout(new GridLayout(1,2,5,5));      		  		
  		botPanel.setLayout(new GridLayout(2,1,4, 4));

  		bottomPanel.add(publish);
  		bottomPanel.add(fetch);
  		
  		bottomPanel1.add(connect);
  		bottomPanel1.add(filelist);

  		botPanel.add(bottomPanel1);
  		botPanel.add(bottomPanel);     		
  		add("South",botPanel);
  		updateCommands ();
	}
       	
    private Button CreateCommand (String command) {
    	Button button = new Button (command);
    	button.setActionCommand (command);
    	button.addActionListener (this);
    	return button;
    }

	public void update (Observable o, Object arg) {
  		MsgNotifier msg = (MsgNotifier)o;
  		if (msg.message() == Define.ExprHolderMsg) {
  			updateCommands ();
  		}
	}
       	
   	public void updateCommands () {
   		TExp exp = displayer.getExpression();
		if (exp instanceof TNullExp)
			publish.setEnabled 	(false);
		else
			publish.setEnabled 	(connected);
		filelist.setEnabled	(connected);
		fetch.setEnabled	(connected);
	}
   	
   	public void stop () {
		clientDisconnect();
	}
     
    public void actionPerformed (ActionEvent e) {
    	String action = e.getActionCommand();
    	if (action.equals(publishCommand)) {
    		clientPublish ();
    	}
    	else if (action.equals(fetchCommand)) {
    		clientFetch ();
    	}
    	else if (action.equals(connectCommand)) {
			if (connected) {
				clientDisconnect();
			}else{
				clientConnect();
			}
    	}
		updateCommands();
	}
	
	public void clientConnect () {
	   try {
         	socket = new Socket(host.getText(), TProtocol.port);
         	
         	in =  new BufferedReader (new InputStreamReader(socket.getInputStream()));
    		out =  new PrintStream (new BufferedOutputStream (socket.getOutputStream()));
//    		out =  new PrintWriter (new BufferedOutputStream (socket.getOutputStream()));
	
     	  	if (client == null) { 
		  		client = new Thread (this, "ElodyClient");
		  		stopClient = false;
				client.start();
		  	}
		    
        	connected = true;
			connect.setLabel("Disconnect");
		}catch (Exception e) {
			System.err.println (e);
		}
	}
		
		
	public void clientDisconnect() {
		try {
			connect.setEnabled(false);
			if (client != null) {
				out.println(TProtocol.close);
				out.flush();
				in.close ();
				out.close ();
				stopClient = true;
			}
			if (socket!= null) socket.close();
			socket = null;
			connected = false;
	 		connect.setEnabled(true);
			connect.setLabel("Connect");
			clearChoice();
		}catch (Exception e) {}
	}

	String parseUrl (String line) {
		StringTokenizer st = new StringTokenizer(line, "\"");
		st.nextToken();
		return st.nextToken();
	}

	String parseTitle (String line) {
		StringTokenizer st = new StringTokenizer(line, "<>");
		st.nextToken();
		st.nextToken();
		return st.nextToken();
	}
	  

	public void run() {
		String res;
		int i,length;
	    
	    try {    
	    	while (!stopClient) {
	   			res = in.readLine();  // lit la taille de la liste
	   			if (res == null) break;
	    		urlTable.clear();
	    		length = Integer.parseInt(res);
	    		for (i=0; i<length; i++) {
	    			res = in.readLine();
	    			if (res == null) break;
	    			urlTable.put(parseTitle (res), parseUrl (res));
	    		}
	    		updateUrlList();
	    	}
	    } catch (IOException e) {
	         System.err.println("TPublisher run : " + e);
	    }
		in = null;
		out = null;
		client = null;
	}


	public void clientPublish () {

		try {
			setCursor(new Cursor(Cursor.WAIT_CURSOR));
	 		TExp  exp = displayer.getExpression();
	 		TFileContent content = new TFileContent(title.getText(), author.getText() , destext.getText(),exp);
	 		TFileSaver saver = new TFileSaver();
			out.println(TProtocol.begin);
	   		saver.writeFile(content,out, "HTML");
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	   	}catch (Exception e) {
			System.err.println ("TPublisher clientPublish : " + e);
		}
		
		finally {
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}
	   
	public void clientFetch () {
		try {
			setCursor(new Cursor(Cursor.WAIT_CURSOR));
			TFileParser parser = new TFileParser();	
			URL url = new URL(urlTable.get(filelist.getSelectedItem()));
			TFileContent content  =  parser.readFile(url);
	   		if (content != null) {
	   			clearTextZone();
	   			displayer.setExpression(content.getExp());
	   			
	   			
	   			title.setText(content.getTitle());
	   			author.setText(content.getAuthor());
	   			destext.setText(content.getDescription());
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	   		}		   		
	   	}catch (Exception e) { System.err.println (e); }
		
		finally {
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}
	   
	void  clearChoice() {
		bottomPanel1.removeAll();
		bottomPanel1.validate();
		filelist = new Choice();
		bottomPanel1.add(connect);
		bottomPanel1.add(filelist);
		bottomPanel1.validate();
	}

	void clearTextZone() { 
		displayer.setExpression(TExpMaker.gExpMaker.createNull());
		title.setText("");
		author.setText("");
		destext.replaceRange( "" , 0 , destext.getText().indexOf( "\n" ) + 1 ) ;
	}
	   
	public void updateUrlList(){
		clearChoice();
		for (Enumeration<String> e = urlTable.keys(); e.hasMoreElements(); ){
			String key = e.nextElement();
			filelist.add(key);
		}
	}
}

class TProtocol  {

	public static String begin = "beginFile";
	public static String close = "endConnection";
	public static int port = 4646;
	
}
