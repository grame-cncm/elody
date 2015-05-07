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
import grame.elody.editor.expressions.TNotesVisitor;
import grame.elody.editor.misc.TGlobals;
import grame.elody.lang.TEvaluator;
import grame.elody.lang.TExpMaker;
import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.util.MsgNotifier;

import java.awt.Checkbox;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Observable;

public class ChordExpressionHolder extends ExprHolder {
	NotesManager nMgr; Checkbox playIt;
	public int vel, pitch, dur, chan;
	String name;

	
	public ChordExpressionHolder (Checkbox playIt) {
		super (null, new TChordVisitor(), false);
		this.playIt = playIt;
	}
  	
  	public void update (Observable o, Object arg) {
  		MsgNotifier mn = (MsgNotifier)o;
  		boolean changed = true;
  		switch (mn.message()) {
			case ChordConstructor.pitchMsg:
				pitch = ((Integer)arg).intValue();
				break;
			case ChordConstructor.velMsg:
				vel = ((Integer)arg).intValue();
				uptodate = changed = false;
				break;
			case ChordConstructor.durMsg:
				dur = (int)(1000 * ((Float)arg).floatValue());
				break;
			case ChordConstructor.chanMsg:
				chan = ((Integer)arg).intValue() - 1;
				uptodate = changed = false;
				break;
			default:
				changed = false;
  		}
  		if (changed)  changed ();
	}
	public TExp buildExpression(TExpMaker maker, Note n) {
		TExp exp = maker.createNote(n.color, n.pitch+pitch, vel,chan,dur);
   		return (n.next == null) ? exp : maker.createMix (exp, buildExpression(maker, n.next));
	}
	public TExp buildExpression() {
		TExpMaker maker = TExpMaker.gExpMaker; 
		TExp res = null;
    	Note n = nMgr.next;
     	if (n!=null) res = buildExpression (maker, n);
    	else res = maker.createSilence( nMgr.color, 0, 0, 0, (float)dur);
		return res; 
	}
	public boolean playIt() { return playIt.getState(); }
	public Object getObject () {
		TExp e = (TExp)super.getObject();
		if (playIt()) TGlobals.player.startPlayer(e);
		TExpMaker.gExpMaker.updateHistory (e);
		return e;
	}
}

class TChordVisitor extends TNotesVisitor
{
	public synchronized void renderExp (TExp exp, Graphics g, Dimension d) {
		if (initialize (exp, g, d)) {
			TEvaluator.gEvaluator.Render (expression, this, 0, null);
		}
	}
    public String toString() 		{ return "TChordVisitor"; }
}