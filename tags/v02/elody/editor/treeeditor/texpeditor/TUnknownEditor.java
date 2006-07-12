package grame.elody.editor.treeeditor.texpeditor;

import grame.elody.editor.treeeditor.TreePanel;
import grame.elody.lang.texpression.expressions.TExp;

import java.awt.Color;

public class TUnknownEditor extends TExpEditor {
	TExp		fExp;
	
	TUnknownEditor (TExp exp)
		{ fExp = exp; }
	
	
	public String getKindName () 
		{ return "unknown"; }
	
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
