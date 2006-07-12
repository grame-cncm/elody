package grame.elody.editor.misc.applets;

import grame.elody.editor.misc.appletframe.PersistentFrame;


public class Persistent extends Singleton {
	protected Persistent () { }
	public Persistent (String title) { 
		frame = new PersistentFrame (title, this);
	}
}
