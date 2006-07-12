package grame.elody.editor.treeeditor.texpeditor;

import grame.elody.editor.treeeditor.TreePanel;
import grame.elody.lang.texpression.expressions.TApplExp;
import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.util.TExpRenderer;

import java.awt.Color;

public class TApplEditor extends TExpEditor {
	TApplExp		fExp;
	
	TApplEditor (TApplExp exp)
		{ fExp = exp; }
	
	public void addSonsTo(TreePanel t) {
		t.add(new TreePanel(t, 0, fExp.fun, Color.red));
		t.add(new TreePanel(t, 1, fExp.arg));
	}
	
	public TExp modifySubExpression(TExp subexp, int norder) {
		if (norder == 0) {
			fExp = new TApplExp(subexp, fExp.arg);
		} else if (norder == 1) {
			fExp = new TApplExp(fExp.fun, subexp);
		}
		return fExp;
	}
	
	public String getKindName () 
//		{ return "APPL"; }
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
