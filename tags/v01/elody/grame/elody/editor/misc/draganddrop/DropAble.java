package grame.elody.editor.misc.draganddrop;

import java.awt.Point;

public interface DropAble {
	public boolean 	accept (Object o);
	public void 	dropEnter ();
	public void 	dropLeave ();
	public void 	drop (Object o, Point where);
}
