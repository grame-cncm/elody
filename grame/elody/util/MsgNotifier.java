package grame.elody.util;

import java.util.Observable;

public class MsgNotifier extends Observable {
	/*public*/ protected int msgValue;
	
	public MsgNotifier(int msg) { msgValue = msg; }
	public int message() 		{ return msgValue; }
	public void setMessage(int msg) 		{ msgValue = msg; }
	public void notifyObservers (Object o) {
		setChanged();
		super.notifyObservers (o);
	}
}
