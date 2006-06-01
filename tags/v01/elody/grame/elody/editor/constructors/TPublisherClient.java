package grame.elody.editor.constructors;

import grame.elody.editor.expressions.ExprHolder;
import grame.elody.editor.expressions.TNotesVisitor;
import grame.elody.editor.misc.TDialog;
import grame.elody.editor.misc.applets.BasicApplet;
import grame.elody.file.parser.TFileContent;
import grame.elody.file.parser.TFileParser;
import grame.elody.lang.TExpMaker;
import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.lang.texpression.expressions.TNullExp;
import grame.elody.net.Message;
import grame.elody.net.TConnection;

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
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;
import java.util.StringTokenizer;
import java.util.Vector;

public class TPublisherClient extends BasicApplet implements Observer,
		ActionListener {

//	-----------//
//	 Variables //
//	-----------//	
		
		
		static String publishCommand = "Publish";	// textes et noms des 
		static String fetchCommand = "Fetch";		// actions des
		static String connectCommand = "Connect";	// boutons

		TextField port,host,title,author;
		TextArea destext;
		ExprHolder  displayer;
		Choice  filelist;
		Button publish, fetch,connect;
		boolean connected = false;
		 
		TConnection connection = null;
		
		Hashtable urlTable;
		Panel bottomPanel1;
			
		boolean receptionUrlList = false;


//	--------------------------//
//	 Initialisation du client //
//	--------------------------//	
			
		public TPublisherClient ()	// Constructeur
		{	
			super ("Publisher");
			setSize(270,380);
		}
		
		public void init() // Initialisation
		{
			affich();	// de l'affichage
			
			//protocol = new TProtocolPublisher();	// des variables
			urlTable = new Hashtable();
	  	}
	       	
//	----------------------------------------------//
//	 prise en compte des actions de l'utilisateur //
//	----------------------------------------------//	    
	    
	    public void actionPerformed (ActionEvent e) // bouton actioné
	    {
	    	String action = e.getActionCommand();
	    	
	    	if (action.equals(publishCommand))	// si PUBLISH
	    	{
	    		clientPublish ();
	    	}
	    	else if (action.equals(fetchCommand)) // si FETCH
	    	{
	    		clientFetch ();
	    	}
	    	else if (action.equals(connectCommand)) // si CONNECT / DISCONNECT
	    	{
				if (connected)
				{
					sendData(new Message ("DECONNECTION"));
					clientDisconnect(); // si DISCONNECT
				}
				else
				{
					clientConnect();	// si CONNECT
				}
	    	}
			updateCommands();
		}
		
		
		public void clientPublish ()	// publication d'une expression
		{
			try
			{
				setCursor(new Cursor(Cursor.WAIT_CURSOR));
		 		TExp  exp = displayer.getExpression();

		 		TFileContent content = new TFileContent(title.getText(), author.getText() , destext.getText(),exp);
		 		sendData(content);
		 		
		 		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		   	}
		   	catch (Exception e)
		   	{
				System.err.println ("TPublisher clientPublish : " + e);
			}
			
			finally
			{
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		}
		
		
		public void clientFetch() // chargement d'une expression
		{
			try
			{
				setCursor(new Cursor(Cursor.WAIT_CURSOR));
				TFileParser parser = new TFileParser();	

				URL url = new URL(((String)urlTable.get(filelist.getSelectedItem())));

				TFileContent content  =  parser.readFile(url);
		   		
		   		if (content != null)
		   		{
		   			clearTextZone();
		   			displayer.setExpression(content.getExp());

		   			title.setText(content.getTitle());
		   			author.setText(content.getAuthor());
		   			destext.setText(content.getDescription());
					setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		   		}		   		
		   	}
		   	catch (Exception e)
		   	{
		   		System.err.println (e);
		   	}
			finally
			{
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		}
		
		public void clientDisconnect()	// déconnexion
		{
			connection.Deconnection();
			connection = null;
					
			connected = false;
			connect.setLabel("Connect");
		}
		
		
		public void clientConnect()	// connexion
		{
			try
			{
				Socket socket = new Socket(host.getText(), Integer.parseInt(port.getText()));
				connection = new TConnection(socket,true);
				connection.addObserver(this);
		
				connected = true;
				connect.setLabel("Disconnect");
			
				//updateUrlList();
			}
			catch (NumberFormatException e) 	// Si le numéro de port est incorrect
	        {
	        	TDialog Box = new TDialog(this.getFrame(),"Invalide port number : " + port.getText());
		       	Box.setVisible(true);
			}
			catch (UnknownHostException e) 	// Si le serveur n'a pas ete trouve
	        {
	        	TDialog Box = new TDialog(this.getFrame(),"Unknowm Hostname : " + host.getText());
		       	Box.setVisible(true);
			}
	        catch (Exception e)				// Si la connexion a echouee
	        {
				TDialog Box = new TDialog(this.getFrame(),"Impossible to establish the connection");
				Box.setVisible(true);
	        }
		 
		}


		public void stop ()	// Si fermeture de la fenetre
		{
			if (connected) {
				sendData(new Message ("DECONNECTION"));
				clientDisconnect(); // déconnexion
			}
		}	




//	----------------------------------------//
//	 prise en compte des actions du serveur //
//	----------------------------------------//	    
	 
		public void update(Observable o, Object arg)	//réception d'objets
		{
			if (arg instanceof Message) //s'il s'agit d'un message  
			{
				String messageText = ((Message)arg).getValue();
				
				if (messageText.equals("DECONNECTION"))
				{
					clientDisconnect();
					TDialog Box = new TDialog(this.getFrame(),"You are no more connected with " + host.getText());
					Box.setVisible(true);
				}
			}
			if (arg instanceof Vector) //s'il s'agit d'un Vecteur ->liste des URL
			{
				Vector List = (Vector)arg;
				updateUrlTable(List);
			}
			updateCommands ();
		}
		
		private void updateUrlTable(Vector list)
		{
			String line;
			urlTable = new Hashtable();
			
			for(int count = 0; count<list.size(); count ++)
			{
				line = (String)(list.elementAt(count));
				urlTable.put(parseTitle (line), parseUrl (line));	
			}
			updateUrlList();
		}
		
		
		String parseTitle (String line)	// récupération du titre de l'expression
		{
			StringTokenizer st = new StringTokenizer(line, "<>");
			st.nextToken();
			st.nextToken();
			return st.nextToken();
		}

		String parseUrl (String line)	// récupération de l'URL
		{
			StringTokenizer st = new StringTokenizer(line, "\"");
			st.nextToken();
			return st.nextToken();
		}


//	------------------------------------------//
//	 envoi de données en direction du serveur //
//	------------------------------------------//

		private void sendData(Object Obj)
		{
			if (connection != null)
			{
				connection.sendData(Obj);

			}
		}

		
//	------------------------------------//
//	 méthodes de gestion de l'affichage //
//	------------------------------------//

		private void affich()	// Initialisation de l'affichage
		{
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
		
			publish.setEnabled(false);
			fetch.setEnabled(false);
		
			port = new TextField("4000");
			host = new TextField("java.grame.fr"); 	
	  				
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
		}

		
		private void updateCommands ()	// mise à jour de l'état des boutons
		{
	   		TExp exp = displayer.getExpression();
			
			if (exp instanceof TNullExp)
			{
				publish.setEnabled 	(false);
			}
			else
			{
				publish.setEnabled 	(connected);
			}
			filelist.setEnabled	(connected);
			fetch.setEnabled	(connected);
		}

		private Button CreateCommand (String command)	// Méthode de création d'un bouton
		{												// avec action associée	
	    	Button button = new Button (command);
	    	button.setActionCommand (command);
	    	button.addActionListener (this);
	    	return button;
	    }

		public void updateUrlList()	// mise à jour de la liste déroulante
		{
			clearChoice();
			for (Enumeration e = urlTable.keys(); e.hasMoreElements(); )
			{
				String key = (String)e.nextElement();
				filelist.add(key);
			}
			
		}

		private void  clearChoice()	// efface le contenu de la liste déroulante
		{
			bottomPanel1.removeAll();
			bottomPanel1.validate();
			filelist = new Choice();
			bottomPanel1.add(connect);
			bottomPanel1.add(filelist);
			bottomPanel1.validate();
		}

		private void clearTextZone()	// efface le contenu des zones de texte
		{ 
			//displayer.setExpression(TGlobals.maker.createNull());
			displayer.setExpression(TExpMaker.gExpMaker.createNull());
			title.setText("");
			author.setText("");
			destext.replaceRange( "" , 0 , destext.getText().indexOf( "\n" ) + 1 ) ;
		}
}
