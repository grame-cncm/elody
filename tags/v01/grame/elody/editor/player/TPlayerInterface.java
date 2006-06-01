package grame.elody.editor.player;

import grame.elody.lang.texpression.expressions.TExp;

public interface TPlayerInterface {
	 
 	public void setSynchroPlayer(int synchro); 
 	public void startPlayer(TExp exp);
 	public void insertPlayer(TExp exp); // ins?re à la position courante
	public void stopPlayer(); 
	public void contPlayer();
	public void freePlayer(); 
	public void setPosMsPlayer(int date_ms); 
	public void setBufferPlayer(int val );
	
}
