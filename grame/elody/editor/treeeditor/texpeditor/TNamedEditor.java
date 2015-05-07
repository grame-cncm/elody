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
import grame.elody.editor.treeeditor.StringPanel;
import grame.elody.editor.treeeditor.TreePanel;
import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.lang.texpression.expressions.TNamedExp;
import grame.elody.util.TExpRenderer;

import java.awt.Color;

public class TNamedEditor extends TExpEditor {
	TNamedExp fExp;
	
	TNamedEditor (TNamedExp exp)
		{ fExp = exp; }
	
	public void addSonsTo(TreePanel t) {
		t.add(new StringPanel(t, 0, TGlobals.getTranslation("Name"), fExp.name));
		t.add(new TreePanel(t, 1, fExp.arg1));
	}
	
	public TExp modifySubExpression(TExp subexp, int norder) {
		if (norder == 1) {
			fExp = new TNamedExp(subexp, fExp.name);
		}
		return fExp;
	}
	
	public String getKindName () 
//		{ return "NAMED"; }
		{ return TExpRenderer.gExpRenderer.getText(fExp); }
		
	public TExp modifySubString (String substr, int norder) {
		fExp = new TNamedExp(fExp.arg1, substr);
		return fExp;
	}
		
	public TExp modifySubInt (int subint, int norder)
		{ return fExp; }
		
	public TExp modifySubFloat (float subfloat, int norder)
		{ return fExp; }
		
	public TExp modifySubColor (Color subcolor, int norder)
		{ return fExp; }
}
