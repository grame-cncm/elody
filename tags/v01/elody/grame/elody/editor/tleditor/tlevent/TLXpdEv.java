package grame.elody.editor.tleditor.tlevent;

import grame.elody.lang.TExpMaker;
import grame.elody.lang.texpression.expressions.TExp;

import java.awt.Color;


/*******************************************************************************************
*
*	 TLExpanded (d'une note)
*
*******************************************************************************************/

public class TLXpdEv extends TLSound {
	final TLExpEv	fXpdEv;
	
	private TLXpdEv(int d, String str, TLExpEv e)  	
		{ super(d, str, 1); this.fXpdEv = e; }
	
	public TLXpdEv(int d, TLExpEv e)  	
		{ this(d, e.fName + "*" + (((float)d) / ((float)e.fDur)), e); }
	
	public TLEvent makeCopy() 	
		{ return new TLXpdEv(fDur, fName, fXpdEv); }
	
	public TLEvent makeResizedCopy(int d)
		{ return (d != fDur) ? new TLXpdEv(d,fXpdEv) : makeCopy(); }
		
	public TExp	getExp(){ return TExpMaker.gExpMaker.createExpv(fXpdEv.getExp(),((float)fDur) / (float)fXpdEv.fDur);}
	public Color	getColor()	{ return fXpdEv.fExpColor; }
}
