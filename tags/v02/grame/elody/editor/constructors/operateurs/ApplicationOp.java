package grame.elody.editor.constructors.operateurs;

import grame.elody.lang.TExpMaker;
import grame.elody.lang.texpression.expressions.TExp;

import java.awt.Image;


public class ApplicationOp extends Operateur {
	public ApplicationOp (Image img) 	{ super (img); name = "Appl"; }
   	public TExp compose(TExpMaker maker, TExp e1, TExp e2) {
  		return maker.createAppl(e1, e2);
    }
}
