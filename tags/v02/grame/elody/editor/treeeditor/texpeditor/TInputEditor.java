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
