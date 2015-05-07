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
