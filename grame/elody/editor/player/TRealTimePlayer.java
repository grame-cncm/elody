/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.editor.player;

import grame.elody.lang.texpression.expressions.TExp;
import grame.midishare.MidiException;
import grame.midishare.player.MidiPlayer;
import grame.midishare.player.PlayerState;

import java.util.Observable;
import java.util.Observer;

/*******************************************************************************************
*
*	TRealTimePlayer (classe) : Player utilisé par les clients 
* 
*******************************************************************************************/

public class TRealTimePlayer implements Observer, TPlayerInterface {
	
	TPlayerInterface evalPlayer;          // player interne lorsque l'évaluation est en cours
	TPlayerInterface loopPlayer;		  // player interne lorsque l'évaluation est terminée
	TPlayerInterface curPlayer;			  // player courant
	MsPlayer1 player;					  // player C++
	TExp curExp;						  // expression à évaluer
	boolean endExp = false;
	int loopstate = MidiPlayer.kLoopOff;  // état loop du player
	
	public void setSynchroPlayer(int synchro) {
	 	PlayerState state = player.GetState();
		if (state.state != MidiPlayer.kIdle){
			curPlayer.stopPlayer();
			curPlayer.setSynchroPlayer(synchro);
			curPlayer.contPlayer();
		}else {
			curPlayer.setSynchroPlayer(synchro);
		}
		setLoopMarkers (); 
	}
	
	final void setLoopMarkers () {
 		PlayerState  state = player.GetEndScore();
    	if (state.date > 10)  {
    		player.SetLoop(loopstate);
    		player.SetLoopEndMs(state.date);
     		player.SetLoopStartMs(0);
    	}
    }
    
    public void insertPlayer(TExp exp) {curPlayer.insertPlayer(exp);}
	public void freePlayer() {curPlayer.freePlayer();}

    public void update(Observable  o, Object  arg) { // fin de l'évaluation
     	curPlayer = loopPlayer;
    	endExp = true;
    	setLoopMarkers ();
   	}
   	 
    // Methodes publiques
	
	public void Open(String name) throws MidiException  {
		player = new MsPlayer1();
		player.Open(name);
		evalPlayer = new TEvalPlayer(player);
		loopPlayer = new TLoopPlayer(player);
		curPlayer = evalPlayer;
		((TEvalPlayer)evalPlayer).addObserver (this);
	}
	
	public void Close(){
		curPlayer.freePlayer();
		player.Close(); 
	}
	
	public void startPlayer(TExp exp) {	
		if (exp.equals(curExp) && endExp) { // si meme exp et fin de l'expression atteinte
			curPlayer = loopPlayer;
		}else {
			curExp = exp;
			endExp = false;
			curPlayer = evalPlayer;
		}
		curPlayer.stopPlayer();
		curPlayer.startPlayer(exp);
	}
	
	public void stopPlayer () 	{curPlayer.stopPlayer();}
	public void contPlayer () 	{curPlayer.contPlayer();}
	
	public void setLoopPlayer(boolean state) { 
		if (state){
			loopstate = MidiPlayer.kLoopOn;
		}else{
			loopstate = MidiPlayer.kLoopOff;
		}
		player.SetLoop(loopstate);
	}
    	
	final public PlayerState getState() {return player.GetState ();}
    final public int getRefnum() {return player.refnum;}
    final public void  setBufferPlayer(int val)  { curPlayer.setBufferPlayer(val);}
    
    public  void setPosMsPlayer (int date_ms) {
		player.SetPosMs(date_ms);
	}
}

/*******************************************************************************************
*
*	TLoopPlayer (classe) : Player utilisé lorsque l'évaluation est terminée, enrobage sur 
*   un MsPlayer C++ interne.
* 
*******************************************************************************************/
 
  class TLoopPlayer  implements TPlayerInterface{

	MsPlayer1 player;	

	public TLoopPlayer (MsPlayer1 player) {this.player = player;}
	
	public void startPlayer(TExp exp) {player.Start();}
	public void stopPlayer() {player.Stop();}
	public void contPlayer() {player.Cont();}
	public void insertPlayer (TExp exp) {} 			// ne fait rien  
	public void setSynchroPlayer(int synchro) {player.SetSynchroIn(synchro);}
 
 	public  void setPosMsPlayer (int date_ms) {
		player.SetPosMs(date_ms);
	}

	public void freePlayer () {
		player.Stop();
		player = null;
	}
  	
  	public void  setBufferPlayer(int val) {}	
}
