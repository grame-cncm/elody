package grame.elody.editor.constructors.operateurs;

import grame.elody.lang.TExpMaker;
import grame.elody.lang.texpression.expressions.TExp;

import java.awt.Image;

public class StretchOp extends Operateur {
	public StretchOp (Image img) 	{ super (img); name = "Stretch"; }
   	public TExp compose(TExpMaker maker, TExp e1, TExp e2) {
  		return maker.createDilate(e1, e2);
    }
}
