package grame.elody.net;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Observable;

/*****************************************************************************************/
/*   Classe TConnection
/************************
/*    permet de g�rer les �v�nements au niveau d'une connexion :
/*		- envoi d'objet
/*		- r�ception d'objet
/*****************************************************************************************/

public class TConnection extends Observable implements Runnable {
//	-----------//
//	 Variables //
//	-----------//	

		Socket socket = null;		// socket de cette extr�mit� de la connexion
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
//	 R�ception de donn�es //
//	----------------------//	

		public void run ()	//r�ception d'un paquet sur le socket
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
			
			// d�connexion
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

		public void Deconnection()	// m�thode deconnection 
		{
			connected = false;
		}

//	---------------------//
//	 Emission de donn�es //
//	---------------------//	

		public void sendData(Object Obj)	// m�thode SendData
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
//	 Autres m�thodes //
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
