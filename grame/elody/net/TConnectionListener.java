package grame.elody.net;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;

/*****************************************************************************************/
/*   Classe TConnectionListener
/********************************
/*    permet de surveiller les demandes de nouvelle connexion
/*****************************************************************************************/

public class TConnectionListener extends Observable implements Runnable {
//	-----------//
//	 Variables //
//	-----------//	

		ServerSocket Server = null;		// socket d'ecoute
		Socket Client = null;			// socket créé à chaque nouvelle connexion
		Thread Watching = null;
		boolean stopWatching = false;


//	----------------//
//	 Initialisation //
//	----------------//	

		public TConnectionListener(int Port) throws Exception// Constructeur
		{
			Server = new ServerSocket(Port);				
			Watching = new Thread (this, "ElodyClient");
			stopWatching = false;
			Watching.start();
		}



//	----------------------------------//
//	 Gestion des nouvelles connexions //
//	----------------------------------//	

		public void run()
		{
			while(!stopWatching)
			{
				try
				{
					Client = Server.accept(); 	// attente de nouvelles connexions
					setChanged();				// notification du client
					notifyObservers(Client);	// qui récupère le socket
				}
				catch(Exception e)
				{
					System.err.println(e);
				}
			}
			try
			{
				Server.close();
				Server = null;
			}
			catch (Exception e)
			{
				System.err.println("Exception TConnectionListener, close : " + e);
			}
		}

		public void close ()
		{
			stopWatching = true;		
		}
}
