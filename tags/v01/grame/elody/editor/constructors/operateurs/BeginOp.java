package grame.elody.editor.constructors.operateurs;

import grame.elody.lang.TExpMaker;
import grame.elody.lang.texpression.expressions.TExp;

import java.awt.Image;


public class BeginOp extends Operateur {
	public BeginOp (Image img) 	{ super (img); name = "Begin"; }
   	public TExp compose(TExpMaker maker, TExp e1, TExp e2) {
  		return maker.createBegin(e1, e2);
    }
}
