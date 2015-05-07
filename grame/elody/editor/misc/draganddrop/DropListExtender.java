/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

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
