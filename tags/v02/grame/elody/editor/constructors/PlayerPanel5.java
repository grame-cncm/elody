package grame.elody.editor.constructors;

import grame.elody.editor.expressions.PlayExprHolder;
import grame.elody.editor.misc.TGlobals;
import grame.elody.editor.misc.applets.BasicApplet;
import grame.elody.editor.player.TRealTimePlayer;
import grame.elody.util.MidiUtils;
import grame.midishare.Midi;
import grame.midishare.MidiException;
import grame.midishare.player.MidiPlayer;
import grame.midishare.player.PlayerState;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Choice;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Observable;
import java.util.Observer;

public final class PlayerPanel5 extends BasicApplet implements Observer,
		ActionListener, ItemListener {
	static final String startCommand = "Start";
	static final String stopCommand = "Stop";
	static final String playCommand = "Play";

	Button start, stop ,cont;
	Checkbox loopbox;
	Choice synchro;
	TextField dateMS, dateBBU;

	DisplayTask task = null;
	TRealTimePlayer msplayer = null;
	PlayExprHolder  displayer;
	String name;

	public PlayerPanel5 ()
	{
		super ("Player");
		setSize(400,180);
		//setSize(getPreferredSize());
		     
		start = CreateCommand ( startCommand ) ;
		stop  = CreateCommand ( stopCommand ) ;
		cont  = CreateCommand ( playCommand ) ;
		  
		dateMS = new TextField("0 : 0 : 0", 10);
		dateBBU = new TextField("1 : 1 : 1", 10);
		displayer = new PlayExprHolder();
		displayer.addObserver(this);
	}

	private Button CreateCommand (String command) {
		Button button = new Button (command);
		button.setActionCommand (command);
     	button.addActionListener (this);
     	return button;
	}
	


	public void start(){
		super.start();
		try {
			if (msplayer == null) {
		 		msplayer = new TRealTimePlayer() ;
		 		name = MidiUtils.availableName("ElodyPlayer");
		 		msplayer.Open (name);
		 		msplayer.setLoopPlayer(true);
		 		TGlobals.context.restoreConnections(name);
		 		startTask();
		 	}
		}catch (MidiException e) {
		 	System.err.println("PlayerPanel5 start : " + e);
		}
	}

	public  void stop(){
		if (msplayer != null) {
			stopTask();
			stopPlayer ();
			TGlobals.context.saveConnections(name);
			msplayer.Close();
			msplayer = null;
		}
	}

	public void init()
	{
		setLayout (new BorderLayout(5,5)) ;

		Panel transportpanel = new Panel() ;	
		Panel buttonpanel = new Panel();
		Panel textpanel = new Panel();

		buttonpanel.setLayout( new GridLayout(1,5,2,2)) ;
		textpanel.setLayout(new GridLayout(1,4,2,2));  
		transportpanel.setLayout( new GridLayout(2,1,2,2)) ;

		loopbox = new  Checkbox("Loop",null, true);
		
		synchro = new Choice()  ;
    	synchro.add("Internal");
    	synchro.add("Clock");
    	synchro.addItemListener (this);
    
		loopbox.addItemListener (this);
		buttonpanel.add( loopbox );
		buttonpanel.add( synchro );
		  	
		buttonpanel.add(start) ;
		buttonpanel.add(stop) ;
		buttonpanel.add(cont) ;

		TextField text = new TextField("Min  Sec  Ms");
		text.setEditable(false);
		 
		textpanel.add(text);
		textpanel.add(dateMS);

		text = new TextField("Bar Beat Unit");
		text.setEditable(false);
		textpanel.add(text);
		textpanel.add(dateBBU);

		transportpanel.add( textpanel ) ;
		transportpanel.add( buttonpanel ) ;

		add("North", transportpanel) ;
		add("Center", displayer);

		moveFrame (100, 240);
	}

	void updateText(int date, int bar, int beat, int unit){
		int min,sec,ms;

		min = date/60000;
		date = date - min*60000;
		sec = date/1000;
		ms = date - sec*1000;
			
		dateMS.setText(String.valueOf (min) + " : " + String.valueOf(sec) + " : " + String.valueOf(ms));
		dateBBU.setText(String.valueOf (bar) + " : " + String.valueOf(beat) + " : " + String.valueOf(unit));
	}
		
	boolean  updateState (){
	 	PlayerState state = msplayer.getState();
		if (state.state == MidiPlayer.kPlaying){
			updateText(state.date, state.bar, state.beat, state.unit);
			displayer.showDate(state.date);
		}
		return true;
	}	


	public  void updateInt (Observable o, Object arg) {startPlayer ();}

	public  void update  (Observable o, Object arg) {
		if (task != null)
			synchronized(task) {updateInt (o,arg);}
		else
			updateInt (o,arg);
	}

	void clearTask() { task = null;}

	void startTask() {
		if (task ==  null) {
	       	task = new DisplayTask(this);
	     	TGlobals.midiappl.ScheduleTask(task, Midi.GetTime());
	    }
	}

	void stopTask() {
		if (task!= null){
			task.Forget();
			task =  null;
		}
	}

	void startPlayerInt () {
	 	msplayer.startPlayer(displayer.getExpression());
	}

	void startPlayer () {
		if (task != null)
			synchronized(task) {startPlayerInt ();}
		else
			startPlayerInt ();
	}

	void contPlayerInt () {
	 	msplayer.contPlayer();
	}

	void contPlayer () {
		if (task != null)
			synchronized(task) {contPlayerInt ();}
		else
			contPlayerInt ();
	}

	void stopPlayerInt () {
	 	msplayer.stopPlayer() ;
	}

	void stopPlayer () {
		if (task != null){
			synchronized(task) {stopPlayerInt ();}
		}else{
			stopPlayerInt ();
		}
	}

    public void actionPerformed (ActionEvent e) {
    	String action = e.getActionCommand();
    	if (action.equals (startCommand)) {
			startPlayer ();
	   	}
    	else if (action.equals (stopCommand)) {
    		stopPlayer ();
    	}
    	else if (action.equals (playCommand)) {
    		contPlayer ();
    	}
	}
	
    public void itemStateChanged (ItemEvent e) {
    	if (e.getItemSelectable().equals(loopbox)) {
 			msplayer.setLoopPlayer(loopbox.getState());
 		}else if (e.getItemSelectable().equals(synchro)) {
			if (synchro.getSelectedItem().equals ("Internal"))
				msplayer.setSynchroPlayer(MidiPlayer.kInternalSync);
			if( synchro.getSelectedItem().equals ("Clock"))
				msplayer.setSynchroPlayer(MidiPlayer.kClockSync);
		}
    }
}
