/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.editor.treeeditor.texpeditor;

import grame.elody.editor.misc.TGlobals;
import grame.elody.editor.treeeditor.ColorPanel;
import grame.elody.editor.treeeditor.StringPanel;
import grame.elody.editor.treeeditor.TreePanel;
import grame.elody.lang.TExpMaker;
import grame.elody.lang.texpression.expressions.TEvent;
import grame.elody.lang.texpression.expressions.TExp;

import java.awt.Color;

public class TEventEditor extends TExpEditor {
	boolean	fNoteType;
	int		fPitch;
	int		fVel;
	int		fChan;
	float	fDur;
	Color	fColor;
	
	TEventEditor (TEvent exp)
	{ 
		fNoteType = exp.getType() == TExp.SOUND; 
		if (fNoteType) {
			fPitch 	= (int) exp.getPitch();
			fVel 	= (int) exp.getVel();
			fChan 	= (int) exp.getChan();
		}
		fDur 	= exp.getDur() / 1000;
		fColor	= exp.getColor();
	}
	
	public void addSonsTo(TreePanel t) {
		if (fNoteType) {
			t.add(new StringPanel(t, TExp.PITCH, 		TGlobals.getTranslation("key"), String.valueOf(fPitch)));
			t.add(new StringPanel(t, TExp.VEL, 		TGlobals.getTranslation("vel"), String.valueOf(fVel)));
			t.add(new StringPanel(t, TExp.CHAN, 		TGlobals.getTranslation("chan"), String.valueOf(fChan)));
			t.add(new StringPanel(t, TExp.DURATION,	TGlobals.getTranslation("dur"), String.valueOf(fDur)));
			t.add(new ColorPanel(t, 0, TGlobals.getTranslation("col"), fColor));
		} else {
			t.add(new StringPanel(t, TExp.DURATION,	TGlobals.getTranslation("dur"), String.valueOf(fDur)));
			t.add(new ColorPanel(t, 0, TGlobals.getTranslation("col"), fColor));
		}
	}
	
	public TExp modifySubExpression(TExp subexp, int norder) {
		return null;
	}
	
	public String getKindName () 
	{ 
		if (fNoteType) {
			return TGlobals.getTranslation("NOTE")+" (" + String.valueOf (fPitch)
					 + ", " + String.valueOf (fVel)
					 + ", " + String.valueOf (fChan)
					 + ", " + String.valueOf (fDur)
					 + ")";
		} else {
			return TGlobals.getTranslation("REST")+" (" + String.valueOf(fDur) + ")"; 
		}
	}
		
	public TExp modifySubString (String substr, int norder) {
		try {
			if (norder == TExp.PITCH) {
				fPitch = Integer.parseInt(substr);
			} else if (norder == TExp.VEL) {
				fVel = Integer.parseInt(substr);
			} else if (norder == TExp.CHAN) {
				fChan = Integer.parseInt(substr);
			} else if (norder == TExp.DURATION) {
				fDur = (new Float(substr)).floatValue();
			}
		} catch (NumberFormatException e) {
			System.err.print(e);
		}
		if (fNoteType) {
			return TExpMaker.gExpMaker.createNote( fColor, fPitch,  fVel,  fChan, (int)(fDur*1000));
		} else {
			return TExpMaker.gExpMaker.createSilence( fColor,  fPitch,  fVel,  fChan, (int)(fDur*1000));
		}
	}
		
	public TExp modifySubInt (int subint, int norder)
		{ return null; }
		
	public TExp modifySubFloat (float subfloat, int norder)
		{ return null; }
		
	public TExp modifySubColor (Color subcolor, int norder)
	{
		fColor = subcolor;
		if (fNoteType) {
			return TExpMaker.gExpMaker.createNote(fColor, fPitch,  fVel,  fChan, (int)(fDur*1000));
		} else {
			return TExpMaker.gExpMaker.createSilence( fColor, fPitch, fVel, fChan, (int)(fDur*1000));
		}
	}
}
