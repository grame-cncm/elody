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
public class TLNullAction extends TLDragAction {
	public TLNullAction(TLPane pane) { super(pane); }
	
	public void mouseDragged(MouseEvent m) {}
	public void mouseReleased(MouseEvent m) {}
	public void mouseClicked(MouseEvent m) {}
	public void clearVisualFeedback(Graphics g) {}
	public void drawVisualFeedback(Graphics g, boolean ctrlPressed) {}
}
