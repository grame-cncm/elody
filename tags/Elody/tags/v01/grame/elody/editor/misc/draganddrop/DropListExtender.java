package grame.elody.editor.misc.draganddrop;

import java.awt.Point;

public class DropListExtender implements DropAble {
	DropAble [] realDrop;
	
	public void setList (DropAble [] list) 	{ realDrop = list; }
	public void dropEnter() { 
		for (int i=0; i < realDrop.length; i++)
			realDrop[i].dropEnter();
	}
	public void dropLeave() { 
		for (int i=0; i < realDrop.length; i++)
			realDrop[i].dropLeave();
	}

	public boolean 	accept (Object o) { 
		for (int i=0; i < realDrop.length; i++)
			if (!realDrop[i].accept(o)) return false;
		return true;
	}
	public void drop (Object o, Point where) { 
		for (int i=0; i < realDrop.length; i++)
			realDrop[i].drop(o, where);
	}
}
