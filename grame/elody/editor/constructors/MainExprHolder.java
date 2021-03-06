/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.editor.constructors;

import grame.elody.editor.expressions.ExprHolder;
import grame.elody.editor.misc.draganddrop.DragAble;
import grame.elody.editor.misc.draganddrop.DropAble;
import grame.elody.editor.misc.draganddrop.DropListExtender;
import grame.elody.editor.misc.draganddrop.TDraggedExp;
import grame.elody.lang.TExpMaker;

import java.awt.Point;

public class MainExprHolder extends ExprHolder {
	private SeqPlayerMgr player;
	DropListExtender extender;
	
	public MainExprHolder (SeqPlayerMgr player) {
		super (null, null, true);
		extender = new DropListExtender();
		this.player = player;
	}
	public void setList (DropAble [] list) 		{ extender.setList(list); }
	public void clear () { 
		drop (new TDraggedExp(TExpMaker.gExpMaker.createNull()), null); 
	}

	public void dropEnter() { 
		super.dropEnter ();
		extender.dropEnter ();
	}
	public void dropLeave() { 
		super.dropLeave ();
		extender.dropLeave ();
	}

	public boolean 	accept (Object o) 				{ return extender.accept(o); }
	public DragAble getDragObject (int x, int y) 	{ return null; }
	public void drop (Object o, Point where) {
		boolean playing = (player != null) ? player.playing () : false;
		if (playing) player.stop();
		extender.drop(o, where);
		if (playing) player.start ();
	}
}
