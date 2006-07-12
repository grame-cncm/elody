package grame.elody.editor.constructors.operateurs;

import grame.elody.lang.TExpMaker;
import grame.elody.lang.texpression.expressions.TExp;

import java.awt.Image;


public class YAbstractionOp extends Operateur {
	public YAbstractionOp (Image img) 	{ super (img); name = "Y-Abstr"; }
   	public TExp compose(TExpMaker maker, TExp e1, TExp e2) {
  		return maker.createYAbstr(e1, e2);
    }
}
