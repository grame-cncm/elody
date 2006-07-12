package grame.elody.editor.constructors.operateurs;

import grame.elody.lang.TExpMaker;
import grame.elody.lang.texpression.expressions.TExp;

import java.awt.Image;


public class RestOp extends Operateur {
	public RestOp (Image img) 	{ super (img); name = "End"; }
   	public TExp compose(TExpMaker maker, TExp e1, TExp e2) {
  		return maker.createRest(e1, e2);
    }
}
