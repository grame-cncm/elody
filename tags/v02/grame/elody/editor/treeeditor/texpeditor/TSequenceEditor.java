package grame.elody.editor.treeeditor.texpeditor;

import grame.elody.editor.treeeditor.TreePanel;
import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.lang.texpression.expressions.TSequenceExp;
import grame.elody.util.TExpRenderer;

import java.awt.Color;

public class TSequenceEditor extends TExpEditor {
	TSequenceExp fSeqExp;

	TSequenceEditor(TSequenceExp exp) {
		fSeqExp = exp;
	}

	public void addSonsTo(TreePanel t) {
		t.add(new TreePanel(t, 0, fSeqExp.arg1));
		t.add(new TreePanel(t, 1, fSeqExp.arg2));
	}

	public TExp modifySubExpression(TExp subexp, int norder) {
		if (norder == 0) {
			fSeqExp = new TSequenceExp(subexp, fSeqExp.arg2);
		} else if (norder == 1) {
			fSeqExp = new TSequenceExp(fSeqExp.arg1, subexp);
		}
		return fSeqExp;
	}

	public String getKindName()
	//		{ return "SEQ"; }
	{
		return TExpRenderer.gExpRenderer.getText(fSeqExp);
	}

	public TExp modifySubString(String substr, int norder) {
		return fSeqExp;
	}

	public TExp modifySubInt(int subint, int norder) {
		return fSeqExp;
	}

	public TExp modifySubFloat(float subfloat, int norder) {
		return fSeqExp;
	}

	public TExp modifySubColor(Color subcolor, int norder) {
		return fSeqExp;
	}
}
