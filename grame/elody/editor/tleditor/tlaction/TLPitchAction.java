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

import grame.elody.editor.misc.TGlobals;
import grame.elody.editor.tleditor.TLActionItem;
import grame.elody.editor.tleditor.TLConverter;
import grame.elody.editor.tleditor.TLPane;
import grame.elody.editor.tleditor.TLZone;
import grame.elody.editor.tleditor.TLActionItem.Action;
import grame.elody.lang.TExpMaker;
import grame.elody.lang.texpression.expressions.TExp;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

//-------------------------------------------------
// action de transposition
//-------------------------------------------------
public class TLPitchAction extends TLDragAction
{
	int 	oldx;
	int 	oldy;
	int 	trp;
	String	msg;
	TExp	exp;
	
	public TLPitchAction(TLPane pane, int x, int y)
	{
		super(pane);
		oldx = x;
		oldy = y;
		trp = 0;
		msg = "";
		exp =  TLConverter.exp(fPane.getFSelection().copyContentToTrack()); 

	}
	public void mouseDragged(MouseEvent m) 
	{ 
		int oldtrp = trp;
		trp =  (oldy - m.getY())/3;
		if (trp != oldtrp) {
			msg = (trp != 0) ? new String("Pitch : " + trp) : "";
			TGlobals.player.startPlayer ( TExpMaker.gExpMaker.createTrsp(exp,trp) );
		}
	}
	public void mouseReleased(MouseEvent m)
	{ 
		if ( trp != 0)
		{
			fPane.toUndoStack(Action.TRACK);
			fPane.getFSelection().cmdPitch(trp);
			fPane.multiTracksChanged();
		}
	}
	public void clearVisualFeedback(Graphics g) { drawVisualFeedback(g, false); }
	public void drawVisualFeedback(Graphics g, boolean ctrlPressed)
	{
		//Graphics g = getGraphics();
		g.setXORMode(TLPane.getFArgColorBkg()); g.setColor(TLPane.getFTraitColor()); 			
		g.drawString(msg, oldx + 5, oldy - 10);
		g.setPaintMode();
	}
	public void mouseClicked(MouseEvent m) {}
}
