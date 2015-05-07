/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

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
