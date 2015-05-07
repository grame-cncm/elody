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
import grame.elody.lang.texpression.TInput;
import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.util.TExpRenderer;

import java.awt.Color;

public class TInputEditor extends TExpEditor {
		TExp		fExp;
		
		TInputEditor (TInput exp)
			{ fExp = exp; }
		
		public String getKindName () 
			{ return TExpRenderer.gExpRenderer.getText(fExp); }
		
		public void addSonsTo(TreePanel t) { }
		
		public TExp modifySubExpression(TExp subexp, int norder)
			{ return fExp; }
			
		public TExp modifySubString (String substr, int norder)
			{ return fExp; }
			
		public TExp modifySubInt (int subint, int norder)
			{ return fExp; }
			
		public TExp modifySubFloat (float subfloat, int norder)
			{ return fExp; }
			
		public TExp modifySubColor (Color subcolor, int norder)
			{ return fExp; }
}
