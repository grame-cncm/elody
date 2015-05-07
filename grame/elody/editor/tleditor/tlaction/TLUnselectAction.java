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
// action de deselection
//-------------------------------------------------

public class TLUnselectAction extends TLDragAction {
	public TLUnselectAction(TLPane pane){ super(pane);}
	public void mouseDragged(MouseEvent m) {}
	public void mouseReleased(MouseEvent m)
	{ 
		fPane.getFSelection().moveTo( fPane.x2time(m.getX()), fPane.y2line(m.getY()) );
		fPane.selectionChanged();
	}
	public void clearVisualFeedback(Graphics g) {}
	public void drawVisualFeedback(Graphics g, boolean ctrlPressed){}
	public void mouseClicked(MouseEvent m)
	{ 
		fPane.getFSelection().moveTo( fPane.x2time(m.getX()), fPane.y2line(m.getY()) );
		fPane.selectionChanged();
	}
}
