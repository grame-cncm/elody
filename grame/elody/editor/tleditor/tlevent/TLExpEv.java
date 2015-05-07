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

import grame.elody.lang.TEvaluator;
import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.util.TExpRenderer;

import java.awt.Color;


/*******************************************************************************************
*
*	 TLElodyExp (une expression elody)
*
*******************************************************************************************/

public class TLExpEv extends TLSound {
	final TExp fElodyExp;
	final Color	fExpColor;
	
	private static int limitDur(float d) { return (d == Float.POSITIVE_INFINITY) ? 600000 : (int) d; }
	private TLExpEv(int d, String str, Color c, TExp x)  	
	{
		super(d, str, 1);
		fElodyExp = x;
		fExpColor = c;
	}
	
	public TLExpEv(TExp x)  	
	{
		this( limitDur(TEvaluator.gEvaluator.Duration(x)), 
			TExpRenderer.gExpRenderer.getText(x), 
			TExpRenderer.gExpRenderer.getColor(x), 
			x);
	}
	
	public TLEvent makeCopy() 	
		{ return new TLExpEv(fDur, fName, fExpColor, fElodyExp); }
	
	public TLEvent makeResizedCopy (int d) 	
		{ return (d != fDur) ? new TLXpdEv(d,this) : makeCopy(); }
	
	public TExp		getExp()	{ return fElodyExp; }
	public Color	getColor()	{ return fExpColor; }
}
