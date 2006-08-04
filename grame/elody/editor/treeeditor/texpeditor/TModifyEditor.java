package grame.elody.editor.treeeditor.texpeditor;

import grame.elody.editor.misc.TGlobals;
import grame.elody.editor.treeeditor.StringPanel;
import grame.elody.editor.treeeditor.TreePanel;
import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.lang.texpression.expressions.TModifyExp;
import grame.elody.lang.texpression.operateurs.TOperator;
import grame.elody.util.TExpRenderer;

import java.awt.Color;

public class TModifyEditor extends TExpEditor {
	static String[] gModifyName = {"bidon", "TRANSPOSE",
		"LOUDNESS", "CHANNEL","STRETCH"};
	static String[] gModifyAttr = {"bidon", TGlobals.getTranslation("pitch"), TGlobals.getTranslation("vel"),
		TGlobals.getTranslation("chan"),	TGlobals.getTranslation("dur")};
	
	TExp		fExp;
	TExp		fBody;
	int			fIndex;
	float		fValue;
	TOperator	fOperator;
	
	TModifyEditor (TModifyExp exp)
	{ 
		fExp 		= exp;
		fBody 		= exp.arg1;
		fIndex 		= exp.index;
		fValue		= exp.value;
		fOperator	= exp.op;
	}
	
	public void addSonsTo(TreePanel t) {
		t.add(new StringPanel(t, 0, gModifyAttr[fIndex], String.valueOf(fValue)));
		t.add(new TreePanel(t, 1, fBody));
	}
	
	public TExp modifySubExpression(TExp subexp, int norder) {
		if (norder == 1) {
			fBody = subexp;
		}
		return new TModifyExp(fBody, fIndex, fValue, fOperator);
	}
	
	public String getKindName () 
//		{ return gModifyName[fIndex]; }
		{ return TExpRenderer.gExpRenderer.getText(fExp); }
		
	public TExp modifySubString (String substr, int norder) {
		if (norder == 0) {
			fValue = (new Float(substr)).floatValue();
		}
		return new TModifyExp(fBody, fIndex, fValue, fOperator);
	}
		
	public TExp modifySubInt (int subint, int norder)
		{ return null; }
		
	public TExp modifySubFloat (float subfloat, int norder)
		{ return null; }
		
	public TExp modifySubColor (Color subcolor, int norder)
		{ return null; }
}
