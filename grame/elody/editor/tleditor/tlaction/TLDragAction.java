/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.editor.tleditor.tlaction;

import grame.elody.editor.tleditor.TLPane;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

//-------------------------------------------------
//action de drag nulle
//-------------------------------------------------
public abstract class TLDragAction {
	TLPane fPane;
	
	public TLDragAction(TLPane pane) { fPane = pane; }
	
	// abstract methods to implement
	public abstract void mouseDragged(MouseEvent m);
	public abstract void mouseReleased(MouseEvent m);
	public abstract void mouseClicked(MouseEvent m);
	public abstract void clearVisualFeedback (Graphics g);
	public abstract void drawVisualFeedback (Graphics g, boolean ctrlPressed);
}
