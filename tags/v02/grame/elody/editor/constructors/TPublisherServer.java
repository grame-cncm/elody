package grame.elody.editor.constructors;

import grame.elody.editor.misc.applets.BasicApplet;
import grame.elody.file.parser.TFileContent;
import grame.elody.file.saver.TFileSaver;
import grame.elody.net.Message;
import grame.elody.net.TConnection;
import grame.elody.net.TConnectionListener;
import grame.elody.util.TLogfile;

import java.awt.Button;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.Enumeration;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

public class TPublisherServer extends BasicApplet implements ActionListener,
		Observer {
	
//	-----------//
//	 Variables //
//	-----------//	

		TConnectionListener listener;
		int port = 4000;

		static String serverUrl;
		static int maxClient = 10;
		static String filename  = "Contrib";
		
		static String resetCommand = "Reset";
		static String debugCommand = "Debug";
		
		Vector clientList;
		Vector urlList;
		
		Button resetButton, debugButton, quitButton;
		TextField clientnumber,portnumber;
		
//	--------------------------//
//	 Initialisation du server //
//	--------------------------//	
			
		public TPublisherServer()	// Constructeur
		{
			super("Publisher Server");
			setSize(200,150);
		}

		public void init()	// initialisation
		{
			affich();	// de l'affichage
			begin();	// des fonctionalités
		}

		public void begin()
		{
			try
			{
				//protocol = new TProtocolPublisher();
				
				//listener = new TConnectionListener(port);
				clientnumber.setText("0");
				listener = new TConnectionListener(Integer.parseInt(portnumber.getText()));
				listener.addObserver(this);

				clientList = new Vector();
				urlList = new Vector();
				
				parseConfig ();
				parseIndexFile ();
				
				TLogfile.printCurdate();
				TLogfile.printMessage("Start Elody Server");
			}
			catch (Exception e)
			{ 
			 	System.out.println(e);
			}      
		}



//	------------------------------//
//	 lecture du fichier de config //
//	------------------------------//

		void parseConfig ()	// Lecture du fichier de config
		{
			try	//	Format : URL du server, nombre maximum de client, par exemple:
			{
				BufferedReader in = new BufferedReader(new InputStreamReader (new FileInputStream("Config.txt")));
				serverUrl = in.readLine();						//  http://java.grame.fr/
				maxClient = Integer.parseInt(in.readLine());	//  10
				in.close();
			}
			catch (IOException e)
			{ 
		 		System.err.println(e);
		 	}	
		}

//	----------------------------//
//	 lecture du fichier d'index //
//	----------------------------//
			
		void parseIndexFile ()	// lecture du fichier d'index
		{
			try
			{
				BufferedReader in = new BufferedReader(new InputStreamReader (new FileInputStream("Index.html")));
				parseFileHeader(in);
				parseUrls(in);
				in.close();
			}
			catch (IOException e)
			{ 
		 		System.err.println(e);
		 	}	
		}
			
		void parseFileHeader(BufferedReader in) throws IOException // lecture de l'entete du fichier d'index
		{
			in.readLine();  // "<HTML><BODY>"
			in.readLine();  // "<B> Elody File Index </B> <HR>"
		}
			
		void parseUrls (BufferedReader in) throws IOException // lecture des URL contenus dans le fichier d'index
		{
			String  res;
			while (((res = in.readLine()) != null) && (!res.startsWith("</BODY></HTML>")))
			{ 
				//System.out.println(res); 
				if (!res.equals("")) urlList.addElement(res);
			}
		}
		
		
		
//	-------------------------------//
//	 Sauvegarde du fichier d'index //
//	-------------------------------//
		
		void writeIndexFile()	// Sauvegarde du fichier d'index
		{
			try
			{
				PrintStream out = new PrintStream(new BufferedOutputStream (new FileOutputStream("Index.html")));
				writeFileHeader(out);
				writeUrls(out);
				writeFileEnd(out);
				out.close();
			}
			catch (IOException e)
			{ 
		 		System.err.println("Write Index File : " + e);
		 	}
		}
		
		void writeFileHeader(PrintStream out)	// écriture du début du fichier d'index
		{
			out.println ("<HTML><BODY>");
			out.println ("<B> Elody File Index </B> <HR>");
		}
		
		void writeFileEnd(PrintStream out) // écriture de la fin du fichier d'index
		{
			out.println ("</BODY></HTML>" );
		}
		
		void writeUrls(PrintStream out) // Sauvegarde liste des URL
		{
			for (Enumeration e = urlList.elements(); e.hasMoreElements();){
		   		String url = (String)e.nextElement();
		   		//System.out.println(url);
	       		out.println(url);
	   		}
	   	}
		
		
		
//	----------------------------------------------//
//	 prise en compte des actions de l'utilisateur //
//	----------------------------------------------//
				
		public void actionPerformed (ActionEvent e)	// détecte les actions
	    {
	    	String action = e.getActionCommand();
	    	
	    	if (action.equals(resetCommand)) // Bouton RESET
	    	{
	    		destroy();
	    		begin();
				//System.out.println("Reset");
	    	}
	    }
	    
	    public void destroy ()	// appelée si QUIT ou si RESET activé
		{
			sendData(new Message ("DECONNECTION"));
			if (listener != null)
			{
		   		listener.close();	// fermeture du port d'écoute
		   		listener = null;
		   	}	
	    }

	    public void stop () // appelée si fermeture de la fen?tre
		{
			//System.out.println ("Stop");
			destroy ();			// fermeture du port d'écoute
			writeIndexFile();	// sauvegarde du fichier d'index
		}
	    
//	-------------------------------------------//
//	 prise en compte des réactions des clients //
//	-------------------------------------------//
		
		public void update(Observable Obs,Object arg)	// réagit aux changements d'état des observables
		{
			if (Obs == listener) // Nouvelle connexion
			{
				Socket temp = (Socket)arg;
				
				try
				{
					addClient(temp);
				}
				catch (Exception E)	
				{
					System.err.println(E);
				}
			}
			
			if (Obs instanceof TConnection)
			{
				
				TConnection client = (TConnection)Obs;
							
				if (arg instanceof TFileContent)	// Réception nouvelle expression
				{
					parseFile(client, (TFileContent)arg);
				}

				else if (arg instanceof Message)	// Réception message de gestion
				{
					String mess = ((Message)arg).getValue();
					
					if (mess.equals("DECONNECTION"))
					{
						removeClient (client);
					}
				}
			}
		}
		
		
		synchronized void addClient(Socket socket) throws Exception	// ajoute un client dans la liste des clients
		{
					
			TConnection client = new TConnection(socket,false);
			client.addObserver(this);			

			TLogfile.printCurdate();
			TLogfile.printMessage("New client: "+ client.getName());
									
			clientList.addElement(client);
			displayClients (clientList.size());

	       	sendUrlList(client);
		}
		
	 
	    synchronized void removeClient (TConnection client)	// Enl?ve un client dans la liste des clients
	    {
	   		TLogfile.printCurdate();
	   	    TLogfile.printMessage("Client quit: " + client.getName()); 
		    
			client.Deconnection();
		    clientList.removeElement(client);
	        displayClients (clientList.size());
	     }
		
		
		void parseFile(TConnection client, TFileContent content)	// réception d'une nouvelle expressions
		{
			try
			{
				TFileSaver saver = new TFileSaver();
				content.title = makeTitle(content.title);
				URL url = generateUrl ();
					
				TLogfile.printCurdate();
	   	   	 	TLogfile.printMessage("Client contribution: " + client.getName() + "\n"); 
		   		TLogfile.printMessage(content.title + "  " +  content.author + "\n"); 
				
				String line = new String ("<BR><A HREF=" + "\"" + url + "\"" +  "TARGET="  + "\"" 
					+ "Page" + "\"" + ">" + content.title +"</A>");
					
				// Ecriture du fichier sur le serveur
				saver.writeFile(content, new File(filename + urlList.size() +".html"), "HTML");
				
				// Mise à jour de la liste des URL
				urlList.addElement(line);
				
				// Ecriture de la liste des URL sur le serveur
				writeIndexFile();
				
				// Envoie à tous les clients la liste des URL
				updateClientUrlList();
				 
			}
			catch (Exception e)
			{
				System.err.println(e);
			}
		}


		URL generateUrl() throws MalformedURLException	// Création d'un URL
		{
			return new URL (serverUrl + "Contrib" + urlList.size() +".html");
		}



		String makeTitle(String str)	// Creation d'un titre pour le fichier re?u
		{
			if (str.equals(""))
			{
				return  new String ("Contrib" + urlList.size()); 
			}
			else
			{
				return str + "_" + urlList.size();
			}	
		}
		
		
		
//	------------------------------------------------------//
//	 méthodes d'envoi de données en direction des clients //
//	------------------------------------------------------//
			
		void updateClientUrlList()	//Envoi de la liste des URL à tous les clients 
		{
			TLogfile.printCurdate();
			TLogfile.printMessage("Send URL list to all clients");
			
			for (Enumeration e = clientList.elements(); e.hasMoreElements();)
			{
		   		TConnection client = (TConnection)e.nextElement();
	       		sendUrlList(client);
	   		}
		}
		
		void sendUrlList(TConnection client)	// envoi de la liste des URL à un client "client"
		{
			//il faut envoyer une COPIE de la liste (nll object sinon la serialisation  ne marche pas)
			sendData(client, urlList.clone());
		}
		
			
		private void sendData(TConnection client, Object obj)	// envoi d'un objet "obj" à un client "client"
		{
			client.sendData(obj);
		}

		private void sendData(Object obj)	// envoi d'un objet "obj" à tous les clients
		{
			for (Enumeration e = clientList.elements(); e.hasMoreElements();)
			{
				TConnection client = (TConnection)e.nextElement();
				client.sendData(obj);
			}
		}

			
//	------------------------------------//
//	 méthodes de gestion de l'affichage //
//	------------------------------------//
		
		private Button CreateCommand (String command)	// Méthode de création d'un bouton
		{												// avec action associée	
	    	Button button = new Button (command);
	    	button.setActionCommand (command);
	    	button.addActionListener (this);
	    	return button;
	    }


		void affich()	// Initialisation de l'affichage
		{
			Panel topPanel= new Panel();
			clientnumber = new TextField("0",10);
			portnumber = new TextField(new Integer(port).toString(),10);
			resetButton = CreateCommand(resetCommand);
			debugButton = CreateCommand(debugCommand);
		
			topPanel.setLayout(new GridLayout(2,2,1,1));
			topPanel.add(new Label ("Port"));
	      	topPanel.add(portnumber);
			topPanel.add(new Label ("Clients"));
	      	topPanel.add(clientnumber);
	      
	      	
	      	setLayout(new GridLayout(3,1,5,5));
	      	add(topPanel); 	
			add(resetButton);	
			add(debugButton);
		}  

		public void displayClients (int val)	// affiche le nombre des clients
	 	{
	 		clientnumber.setText(new Integer(val).toString());
	 	}
}
