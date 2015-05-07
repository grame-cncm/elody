/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

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
