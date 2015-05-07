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

import grame.elody.editor.treeeditor.TreePanel;
import grame.elody.lang.texpression.expressions.TBeginExp;
import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.util.TExpRenderer;

import java.awt.Color;

public class TBeginEditor extends TExpEditor
{
	TBeginExp		fExp;
	
	TBeginEditor (TBeginExp exp)
		{ fExp = exp; }
	
	public void addSonsTo(TreePanel t) {
		t.add(new TreePanel(t, 1, fExp.arg2, Color.green));		// expression dui sert de durée en premier
		t.add(new TreePanel(t, 0, fExp.arg1));
	}
	
	public TExp modifySubExpression(TExp subexp, int norder) {
		if (norder == 0) {
			fExp = new TBeginExp(subexp, fExp.arg2);
		} else if (norder == 1) {
			fExp = new TBeginExp(fExp.arg1, subexp);
		}
		return fExp;
	}
	
	public String getKindName () 
		//{ return "BEGIN"; }
		{ return TExpRenderer.gExpRenderer.getText(fExp); }
		
	public TExp modifySubString (String substr, int norder)
		{ return fExp; }
		
	public TExp modifySubInt (int subint, int norder)
		{ return fExp; }
		
	public TExp modifySubFloat (float subfloat, int norder)
		{ return fExp; }
		
	public TExp modifySubColor (Color subcolor, int norder)
		{ return fExp; }
}	