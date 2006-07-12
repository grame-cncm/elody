package grame.elody.editor.constructors.operateurs;

import grame.elody.lang.TExpMaker;
import grame.elody.lang.texpression.expressions.TExp;

import java.awt.Image;


public class AbstractionOp extends Operateur {
	public AbstractionOp (Image img) 	{ super (img); name = "Abstr"; }
   	public TExp compose(TExpMaker maker, TExp e1, TExp e2) {
  		return maker.createAbstr(e1, e2);
    }
}
