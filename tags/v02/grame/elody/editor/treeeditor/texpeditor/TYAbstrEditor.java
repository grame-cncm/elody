package grame.elody.editor.treeeditor.texpeditor;

import grame.elody.editor.treeeditor.TreePanel;
import grame.elody.lang.TExpMaker;
import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.lang.texpression.expressions.TYAbstrExp;
import grame.elody.util.TExpRenderer;

import java.awt.Color;

public class TYAbstrEditor extends TExpEditor {
	TExp			fExp;
	float			fDur;
	TExp			fVar;
	TExp			fBody;
	
	TYAbstrEditor (TYAbstrExp exp)
	{ 
		fExp = exp; 
		//fDur = exp.val; //on peut l'ignorer car recreation à l'identique
		fVar = exp.ident.getArg1();
		fBody = TExpMaker.gExpMaker.createBody(exp.getArg1(), exp.getArg2());
	}
	
	public void addSonsTo(TreePanel t) {
		t.add(new TreePanel(t, 0, fVar, Color.blue));
		t.add(new TreePanel(t, 1, fBody));
	}
	
	public TExp modifySubExpression(TExp subexp, int norder) {
		if (norder == 0)  {
			fVar = subexp;
		} else if (norder == 1) {
			fBody = subexp;
		}
		fExp = TExpMaker.gExpMaker.createYAbstr(fVar, fBody);
		return fExp;
	}
	
	public String getKindName () 
//		{ return "REC-ABSTR"; }
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
