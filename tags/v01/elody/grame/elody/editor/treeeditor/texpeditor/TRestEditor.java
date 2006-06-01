package grame.elody.editor.treeeditor.texpeditor;

import grame.elody.editor.treeeditor.TreePanel;
import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.lang.texpression.expressions.TRestExp;
import grame.elody.util.TExpRenderer;

import java.awt.Color;

public class TRestEditor extends TExpEditor {
	TRestExp		fExp;
	
	TRestEditor (TRestExp exp)
		{ fExp = exp; }
	
	public void addSonsTo(TreePanel t) {
		t.add(new TreePanel(t, 1, fExp.arg2, Color.green));		// expression dui sert de durée en premier
		t.add(new TreePanel(t, 0, fExp.arg1));
	}
	
	public TExp modifySubExpression(TExp subexp, int norder) {
		if (norder == 0) {
			fExp = new TRestExp(subexp, fExp.arg2);
		} else if (norder == 1) {
			fExp = new TRestExp(fExp.arg1, subexp);
		}
		return fExp;
	}
	
	public String getKindName () 
//		{ return "REST"; }
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
