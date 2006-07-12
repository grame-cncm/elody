package grame.elody.editor.misc;

import grame.elody.editor.misc.appletframe.AppletFrame;
import grame.elody.editor.misc.appletframe.AppletList;
import grame.elody.editor.misc.applets.BasicApplet;
import grame.elody.util.MidiUtils;
import grame.midishare.Midi;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;

/*******************************************************************************************
*
*	TGlobalPrefs (classe) : Gestion des préférences MIDI. Un fichier de préférences ElodyPrefs.txt
*   stocke:
*
*   - le port MIDI
*   - le nombre d'événements MidiShare souhaités 
*   - la liste des connections sous la forme :  source/destination
*   - la liste des fenêtres ouvertes sous la forme : nom de la classe, position, taille
* 
*******************************************************************************************/
public class TGlobalPrefs {
	static final String fileName = Define.makeUserFile("ElodyPrefs.txt");
	static final String defaultAppl = "MidiShare";
	int portNum = 0;
	int eventNum = 30000;
	Vector loadConnections;
	Vector saveConnections;
	Vector loadApplets;
	Vector saveApplets;
	
	String inConnection = defaultAppl;
	String outConnection = defaultAppl;

	public TGlobalPrefs() {
		loadConnections = new Vector();
		saveConnections = new Vector();
		loadApplets = new Vector();
		saveApplets = new Vector();
	}
	
	public int getPort() { return portNum;}
	public void setPort(int num) { portNum = num;}
	
	public int getEvents() { return eventNum;}
	public void setEvents(int num) { eventNum = num;}
	
	
	// Lecture du fichier de preferences
	//------------------------------------
	
	public void readPrefs () {
		String  connection,events, source,destination,appletstate;
		StringTokenizer st;
		
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader( new FileInputStream(fileName)));
			
			// Read "__MidiPort__" line
			in.readLine();  
			connection = in.readLine();
			st = new StringTokenizer(connection, "/");
       		st.nextToken(); // MidiPort
	   		portNum = Integer.parseInt(st.nextToken());
	   		
	   		// Read "__MidiEvent__" line
	   		in.readLine();
	   		events = in.readLine();
			st = new StringTokenizer(events, "/");
       		st.nextToken(); // MidiEvents
	   		eventNum = Integer.parseInt(st.nextToken());
	   		
	   		// Read "__Connections__" line
	   		in.readLine();
			
			// Read MidiShare application connection 
			while (((connection = in.readLine())!= null) && !connection.equals("__Applets__")) { 
			
				st = new StringTokenizer(connection, "/");
       			source = st.nextToken();
	   			destination = st.nextToken();
	   			
				if (destination.equals("All")) {
					inConnection = source;
				}else if (source.equals("All")) {
					outConnection = destination;
				}else {
					loadConnections.addElement(connection);
				}
				
			}
			
			// Read Applets state
			while ((appletstate = in.readLine())!= null) { 
				loadApplets.addElement(appletstate);
			}
			
			in.close();
		} catch (Exception e) { 
	 		//System.out.println(e);
	 	}	
	}
	
	// Ecriture du fichier de preferences
	//------------------------------------------
	
	public  void writePrefs () {
		try {
			if (!outConnection.equals(defaultAppl)) saveConnections.addElement("All/" + outConnection);
			if (!inConnection.equals(defaultAppl)) saveConnections.addElement(inConnection + "/All");
			
			PrintWriter out = new PrintWriter(new BufferedOutputStream (new FileOutputStream(fileName)));
			
			// Save port state
			out.println("__MidiPort__");
			out.println ("MidiPort/" + portNum);
			
			// Save event state
			out.println("__MidiEvent__");
			out.println ("MidiEvents/" + eventNum);
			
			// Save connection state
			out.println("__Connections__");
			for (Enumeration e = saveConnections.elements(); e.hasMoreElements();){
	   			out.println((String)e.nextElement());
	   		}
	   		
	   		// Save applets state
	   		out.println("__Applets__");
	   		for (Enumeration e = saveApplets.elements(); e.hasMoreElements();){
	   			out.println((String)e.nextElement());
	   		}
	   		
			out.close();
		} catch (Exception e) { 
	 		System.err.println(e);
	 	}
	}


	// Chargement des connections d'une appli
	//------------------------------------------
	
	public void restoreConnections(String name) {
		String connection,source,destination;
		StringTokenizer st;
		
		// supprime toutes les connections
		
		int ref = Midi.GetNamedAppl(name);
		for (int i = 1 ; i <= Midi.CountAppls(); i++) {
			Midi.Connect(ref,Midi.GetIndAppl(i), 0);
			Midi.Connect(Midi.GetIndAppl(i), ref, 0);
		}
		
		// restaure les connections du fichier de Prefs
		
		try {
			for (Enumeration e = loadConnections.elements(); e.hasMoreElements();){
		   		connection = (String)e.nextElement();
		   		st = new StringTokenizer(connection, "/");
	       		source = st.nextToken();
		   		destination = st.nextToken();
		   		if (name.equals(source)) MidiUtils.connect(name,destination,1);
		   		if (name.equals(destination))  MidiUtils.connect(source, name,1);
		     }
		    MidiUtils.connect(name,outConnection,1);
		   	MidiUtils.connect(inConnection,name,1);
		   	
		 }catch(Exception e) {
		 	System.err.println("Can not restore the Connections for: " + name);
		 }
		 
	   
	}

	// Sauvegarde des connections d'une appli
	//------------------------------------------
	
	public void saveConnections(String name) {
		int refCount = Midi.CountAppls();
		String name1, res;
		for (int i = 1; i <= refCount; i++) {
			name1 = Midi.GetName( Midi.GetIndAppl(i));
			
			res = name + "/" + name1;
			if (MidiUtils.isConnected(name, name1) && !saveConnections.contains(res)) 
				saveConnections.addElement(res);
				
			res = name1 + "/" + name;
			if (MidiUtils.isConnected(name1, name) && !saveConnections.contains(res))
				saveConnections.addElement(res);
		}
	}
	
	// Restauration de l'état des fenetres
	//--------------------------------------
	
	public void restoreAppletsState () {
		
	   	for (Enumeration e = loadApplets.elements(); e.hasMoreElements();){
	   			String appletstate = (String)e.nextElement();
	   			StringTokenizer st = new StringTokenizer(appletstate, "/");
       			String name = st.nextToken();
       			String x = st.nextToken();
	   			String y = st.nextToken();
	   			String width = st.nextToken();
	   			String heigth = st.nextToken();
	   		/*	BasicApplet applet = */AppletFrame.startApplet(name,Integer.parseInt(x),Integer.parseInt(y),
	   				Integer.parseInt(width),Integer.parseInt(heigth));
	   	}
	}
	
	// Sauvegarde de l'état des fenetres
	//------------------------------------
	
	public void saveAppletsState () {	
		AppletList appletList = AppletFrame.getAppletList();
		
		// Save applets state
	   	for (Enumeration e = appletList.elements(); e.hasMoreElements();){
	   		try {
	   			AppletFrame frame = (AppletFrame)e.nextElement();
	   			BasicApplet applet = (BasicApplet)frame.applet;
	   			Point pt = frame.getLocationOnScreen();
				Rectangle rec = applet.getBounds();
				String className = applet.getClass().toString().substring(6);
				if (!className.equals("grame.elody.editor.main.Elody")){ // does not save the applet launcher
	   				String str = className +"/" + ((pt.x < 0) ? 0 : pt.x) +  "/"+ ((pt.y < 0) ? 0 : pt.y) + "/" + rec.width +  "/"+ rec.height;
	   				saveApplets.addElement(str);
	   			}
	   		
	   		}catch(Exception ex) {
				ex.printStackTrace();
			}
	   	}
	}
	
	
	public void setOutConnection(String connection) { outConnection = connection;}
	public void setInConnection(String connection) { inConnection = connection;}
	
	public String getOutConnection() { return outConnection; }
	public String getInConnection() { return inConnection; }
}
