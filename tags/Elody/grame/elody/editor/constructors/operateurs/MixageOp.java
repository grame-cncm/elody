package grame.elody.editor.constructors.operateurs;

import grame.elody.lang.TExpMaker;
import grame.elody.lang.texpression.expressions.TExp;

import java.awt.Image;

public class MixageOp extends Operateur
{
	public MixageOp (Image img) 	{ super (img); name = "Mix"; }
   	public TExp compose(TExpMaker maker, TExp e1, TExp e2) {
  		return maker.createMix(e1, e2);
    }
}
