package grame.elody.editor.constructors;

import grame.elody.editor.misc.TGlobals;
import grame.elody.editor.misc.applets.BasicApplet;
import grame.elody.editor.player.TRealTimePlayer;
import grame.elody.file.parser.TFileContent;
import grame.elody.lang.TExpMaker;
import grame.elody.net.Message;
import grame.elody.net.TConnection;
import grame.elody.net.TConnectionListener;
import grame.elody.util.MidiUtils;
import grame.midishare.Midi;
import grame.midishare.MidiAppl;
import grame.midishare.MidiTask;
import grame.midishare.player.MidiPlayer;
import grame.midishare.player.PlayerState;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

public class TPlayerServer extends BasicApplet implements Observer,
		ActionListener {

	//-----------//
	// Variables //
	//-----------//

	static final int maxChan = 8;
	static final int port = 4001;
	static final String stopCommand = "Arreter";
	static final String nextCommand = "Prochain";
	static final String resetCommand = "RESET";

	TConnectionListener listener;
	
	SendTask sendTask = null;
	TRealTimePlayer player = null;
	Vector clientList, expressionList;
	TextField[] adresses,ports;
	String serverName;
	Button stop,reset,next;
	
	public TPlayerServer() {
		super("Player Server");
		
		try{
			listener = new TConnectionListener(port);
			listener.addObserver(this);

			clientList = new Vector();
			expressionList = new Vector();
			adresses = new TextField[maxChan];
			ports = new TextField[maxChan];
			sendTask = new SendTask(this);
			
		}catch (Exception e){ 
		 	System.out.println(e);
		}     
		 
		setSize(500,250);
	}
	
	private Button CreateCommand (String command) {
		Button button = new Button (command);
		button.setActionCommand (command);
     	button.addActionListener (this);
     	return button;
	}
	
	public void init()	// initialisation
	{
		setLayout(new BorderLayout());
		Panel panel1 = new Panel();
		Panel buttonpanel = new Panel();
		panel1.setLayout(new GridLayout(8,2,1,1));
		
		stop  = CreateCommand ( stopCommand ) ;
		reset = CreateCommand ( resetCommand ) ;
		next  = CreateCommand ( nextCommand ) ;
		buttonpanel.add(stop);
		buttonpanel.add(next);
		buttonpanel.add(reset);
		
		for (int i = 0 ; i<maxChan; i++){
		
			Panel panel2 = new Panel();
			Panel panel3 = new Panel();
			panel2.setLayout(new BorderLayout(10,10));
			panel3.setLayout(new GridLayout(1,2,10,10));
			
			Label label = new Label("Adresse " + (i+1));
			//label.setBackground(Color.red);
	
			panel2.add("West",label);
			adresses[i] = new TextField("");
			ports[i] = new TextField(new Integer(i+1).toString());
			
			panel2.add("Center", adresses[i]);
			panel3.add("West",new Label("Canal Midi"));
			panel3.add("Center",ports[i]);
			panel1.add(panel2);
			panel1.add(panel3);
			
		}
		add("Center",panel1);
		add("South", buttonpanel);
	}
	
	public void start () {
		try {
			if (player == null) {
				serverName = MidiUtils.availableName("ElodyServerPlayer");
				player = new TRealTimePlayer();
				player.Open(serverName);
				TGlobals.context.restoreConnections(serverName);
			}
		}catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public  void stop(){
		if (player != null) {
			player.stopPlayer ();
			player.Close();
			player = null;
			TGlobals.context.saveConnections(serverName);
		}
	}

	int getNextAdress() {
		for (int i = 0 ; i<maxChan; i++){ if (adresses[i].getText().equals("")) return i;}
		return -1;
	}

	synchronized void addClient(Socket socket) throws Exception	// ajoute un client dans la liste des clients
	{
		int index = getNextAdress();
		
		if (index >= 0 && !isPlaying ()) {
				
			TPlayerConnection client = new TPlayerConnection(socket,false,index);
			client.addObserver(this);
			clientList.addElement(client);
			adresses[index].setText(socket.getInetAddress().toString());
			
		}else{
			System.out.println("Plus de client disponible");	
			socket.close();		
		}
	}
	
 
    synchronized void removeClient (TPlayerConnection client)	// Enl?ve un client dans la liste des clients
    {
  		client.Deconnection();
	    clientList.removeElement(client);
	    adresses[client.index].setText("");
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
	
	
	//-------------------------------------------//
	// prise en compte des réactions des clients //
	//-------------------------------------------//
	
	public void update(Observable Obs,Object arg)	// réagit aux changements d'état des observables
	{
		if (Obs == listener) // Nouvelle connexion
		{
			Socket temp = (Socket)arg;
			
			try
			{
				addClient(temp);
			}
			catch (Exception e)	
			{
				System.err.println(e);
			}
		}
		
		if (Obs instanceof TPlayerConnection)
		{
			
			TPlayerConnection client = (TPlayerConnection)Obs;
						
			if (arg instanceof TFileContent)	// Réception nouvelle expression
			{
				
				synchronized(this) {
					addFile(client, (TFileContent)arg);
					if (!isPlaying ()) {
						playNextFile();
					}
				}
			}

			else if (arg instanceof Message)	// Réception message de gestion
			{
				String mess = ((Message)arg).getValue();
				
				if (mess.equals("DECONNECTION")){
					removeClient (client);
				}else if (mess.equals("STOP")){
					stopFile();
					playNextFile();
				}
			}
		}
	}
	
	void addFile(TPlayerConnection client, TFileContent file) {
		file.title = ports[client.index].getText() ; // codage du chan dans le titre
		expressionList.addElement(file);
	}
	
	
	void playNextFile() {
		try {
			TFileContent nextFile = (TFileContent)expressionList.firstElement();
			System.out.println("Play file");
			expressionList.removeElement(nextFile);
			sendData(nextFile);
			player.startPlayer (TExpMaker.gExpMaker.createSetch(nextFile.getExp(),Integer.parseInt(nextFile.getTitle())-1));
			sendTask.Forget();
			sendTask = new SendTask(this);
			TGlobals.midiappl.ScheduleTask(sendTask, Midi.GetTime());
		}catch (Exception e) {
			System.out.println("No more file");
		}
	}
	
	void stopFile() { 
		player.stopPlayer();
		sendTask.Forget();
	}
	
	void sendDate(int date) { 
		PlayerState state = player.getState();
		if (isPlaying ()) {
			sendData(new Integer(getDate())); 
			TGlobals.midiappl.ScheduleTask(sendTask, date + 1000);
		}else {
			stopFile();
			playNextFile();
		}
	}
	
	void resetAll(){
		stopFile();
		expressionList.removeAllElements();
		sendData(new Message("RESET")); 
	}
	
	
	boolean isPlaying () {
		PlayerState state = player.getState();
		return (state.state == MidiPlayer.kPlaying);
	}
	
	int getDate () {
		PlayerState state = player.getState();
		return state.date;
	}
	
    public void actionPerformed (ActionEvent e) {
    	String action = e.getActionCommand();
    	if (action.equals (stopCommand)) {
    		stopFile();
     	}else if (action.equals (resetCommand)) {
    		resetAll();
	 	}else if (action.equals (nextCommand)) {
    		stopFile();
    		playNextFile();
	   	}
	}
}

class TPlayerConnection extends TConnection{

	int index;

	TPlayerConnection (Socket socket, boolean flag, int index)  throws Exception{
		super(socket,flag);
		this.index = index;
	}

}

/*******************************************************************************************
*
*	SendTask (classe) : tache d'affichage du temps
* 
*******************************************************************************************/

final class SendTask extends MidiTask {

	TPlayerServer player;
	
	SendTask (TPlayerServer player) { this.player = player;}
	
	public void Execute (MidiAppl appl, int date) { player.sendDate(date);}
}
