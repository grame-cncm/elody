package grame.elody.editor.misc.draganddrop;

import java.awt.Panel;
import java.awt.Point;

public class DropExtender extends Panel implements DropAble {
	
	DropAble realDrop;
	
	public DropExtender (DropAble c) 		{ realDrop = c; }
	public void dropEnter() 				{ realDrop.dropEnter(); }
	public void dropLeave() 				{ realDrop.dropLeave(); }
	public boolean 	accept (Object o) 		{ return realDrop.accept(o); }
	public void drop (Object o, Point where){ realDrop.drop (o, where); }
}
