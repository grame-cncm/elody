package grame.elody.editor.player;

import grame.elody.editor.misc.TGlobals;
import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.lang.texpression.expressions.TNullExp;
import grame.midishare.Midi;
import grame.midishare.MidiAppl;
import grame.midishare.MidiException;
import grame.midishare.MidiTask;
import grame.midishare.player.PlayerState;

import java.util.Observable;
import java.util.Observer;

/*******************************************************************************************
*
*	TSeqRealTimePlayer (classe) : sequenceur d'expressions
* 
*******************************************************************************************/
public final class TSeqRealTimePlayer implements Observer {
	static final int ADVANCE = 500;
	int max = 8;
	int curIndex = 0;
	ExpObserver observer;
	TPlayerInterface evalPlayer;
	MsPlayer1 player;
	boolean startLine = true;
	int offset = 0;   // différence entre temps réel et temps dans la séquence 
	TExpTask task = null;
	TDispExpTask task1 = null;
	TExp nextExp;
	int contDate;

	public TSeqRealTimePlayer (ExpObserver observer, int max) {
		this.max = max;
		this.observer = observer;
	}
	
	public void Open (String name) throws MidiException  {
		player = new MsPlayer1();
		player.Open(name);
		evalPlayer = new TEvalPlayer(player);
		((TEvalPlayer)evalPlayer).addObserver (this);
	}
	
	public void Close(){ player.Close(); }
	
	public synchronized void startPlayer() {
		curIndex = -1;
		startLine = true;
		offset  = Midi.GetTime();
		contDate = 0;	
		nextExp = getNextExp();	
		playNextExp ();
		displayNextExp(0); // affiche la case 0
		//System.out.println("startPlayer");
	}
	
	public synchronized void stopPlayer () {
		if (task != null) {
			task.Forget();
			task = null;
		}
		
		if (task1 != null) {
			task1.Forget();
			task1 = null;
		}
		evalPlayer.stopPlayer();
		//System.out.println("stopPlayer");
	}
	
	public synchronized void contPlayer() {evalPlayer.contPlayer();}
	
	final int nextIndex(int index) {
		if (index == max - 1) {
			return 0;
		}else{ 
			return index + 1;
		}
	}
	
	final TExp getNextExp(){
		TExp exp; 
		
		do {curIndex = nextIndex(curIndex);}
			while ((exp = observer.getExpression(curIndex)) instanceof TNullExp);
		
		return exp;
	}
	
	synchronized void playNextExp() {
		if (startLine){
			startLine = false;
			evalPlayer.startPlayer(nextExp);
		}else{
			evalPlayer.insertPlayer(nextExp);
		}
	}
	
	void  displayNextExp(int index) {
	 	observer.startExpression(index); // signale le debut du jeu d'une expression
	}	
  
    public void update(Observable o, Object arg) {
    	int contDate1 = ((Integer)arg).intValue();
    	int newDate = offset + contDate1;		
		nextExp = getNextExp();   	
    	task =  new TExpTask(this);
		task1 = new TDispExpTask(this,curIndex);			
		TGlobals.midiappl.ScheduleTask(task, newDate - ADVANCE);
		TGlobals.midiappl.ScheduleTask(task1, newDate);
 	}
    
    public PlayerState getState() {return player.GetState();}
	  
	public int getRefnum() {return player.refnum;}
}

/*******************************************************************************************
*
*	TExpTask (classe) : tache de jeu de l'expression suivante
* 
*******************************************************************************************/

class TExpTask extends MidiTask {
	
	TSeqRealTimePlayer player; 
	
	public TExpTask (TSeqRealTimePlayer player) {this.player = player;}
	public void Execute (MidiAppl appl, int date) { 
		try {
			int delta = Midi.GetTime() - (date + TSeqRealTimePlayer.ADVANCE + 100);
			if (delta > 0) player.offset += delta;
			player.playNextExp();
		}catch (Throwable e) {
			e.printStackTrace();
		}
	}
}

/*******************************************************************************************
*
*	TDispExpTask (classe) : tache d'affichage de l'expression en cours de jeu
* 
*******************************************************************************************/

class TDispExpTask extends MidiTask {
	
	TSeqRealTimePlayer player; 
	int index;
	
	public TDispExpTask (TSeqRealTimePlayer player, int index) {this.player = player; this.index = index;}
	public void Execute (MidiAppl appl, int date) { 
		try {
			int delta = Midi.GetTime() - (date + 100);
			if (delta > 0) player.offset += delta;
			player.displayNextExp(index);
		}catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
