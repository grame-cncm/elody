package grame.elody.net;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Observable;

/*****************************************************************************************/
/*   Classe TConnection
/************************
/*    permet de gérer les évènements au niveau d'une connexion :
/*		- envoi d'objet
/*		- réception d'objet
/*****************************************************************************************/

public class TConnection extends Observable implements Runnable {
//	-----------//
//	 Variables //
//	-----------//	

		Socket socket = null;		// socket de cette extrémité de la connexion
		ObjectOutputStream Sender;	// flux sur socket en sortie
		ObjectInputStream Receiver;	// flux sur socket en entree
			
		Thread Receiving = null;	// 
		boolean connected = true;	// 
		
		
//	----------------//
//	 Initialisation //
//	----------------//	

		public TConnection(Socket socket, boolean order) throws Exception	//Constructeur
		{
			this.socket = socket; // recuperation du socket
		
			if (order) {
				Sender = new ObjectOutputStream(socket.getOutputStream());
				Sender.flush();
				Receiver = new ObjectInputStream(socket.getInputStream());
			}else {
				Receiver = new ObjectInputStream(socket.getInputStream());
				Sender = new ObjectOutputStream(socket.getOutputStream());
				Sender.flush();
			}
			
			Receiving = new Thread(this, "ElodyConnection");
			Receiving.start();	
		}


//	----------------------//
//	 Réception de données //
//	----------------------//	

		public void run ()	//réception d'un paquet sur le socket
		{
			Object recept;
			
			while(connected) // Reception des donnees
			{
	        	try
	        	{
					recept = Receiver.readObject(); // lecture de l'objet
				    setChanged();	
					notifyObservers(recept); // notification de l'observateur
				} 
				catch (Exception e)
				{
					System.err.println(e);
				}
	    	}
			
			// déconnexion
			try
			{
				Sender.flush();
				if (socket != null)
				{
					Receiver.close();	//	
					Receiver = null;	//	
					Sender.close();		//
					Sender = null;		//
					socket.close(); 	// fermeture du socket
					socket = null;		// et des flux
				}
			}
			catch (Exception e)
			{
				System.err.println(e);
			}
		
		
		}

//	---------------------------//
//	 Fermeture de la connexion //
//	---------------------------//	

		public void Deconnection()	// méthode deconnection 
		{
			connected = false;
		}

//	---------------------//
//	 Emission de données //
//	---------------------//	

		public void sendData(Object Obj)	// méthode SendData
		{
			try
			{			
				Sender.writeObject(Obj);
			}
			catch(Exception E)
			{
				System.err.println(E);			
			}
			
		}

//	-----------------//
//	 Autres méthodes //
//	-----------------//	


		public Socket getSocket()
		{
			return socket;	
		}

		
		public String getName()
		{
			return( socket.getInetAddress().getHostName() );
		}
}
