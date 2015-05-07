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
import grame.elody.lang.TExpMaker;
import grame.elody.lang.texpression.expressions.TExp;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;


/*******************************************************************************************
*
*	 TLRest (silence)
*
*******************************************************************************************/

public final class TLRest extends TLEvent
{
	public TLRest(int d) 	
		{ super(d, "<" + d + ">", 1); }
	
	
	public TLRest(TExp x)  	
	{
		this((int)TEvaluator.gEvaluator.Duration(x));
	}
	
	// implémentation des méthodes abstraites

	public final boolean isRest()
		{ return true; }
		
	public final TLEvent makeCopy() 	
		{ return new TLRest(fDur); }
	
	public final TLEvent makeResizedCopy(int d)
		{ return new TLRest(d); }

	public final void draw(Graphics g, FontMetrics fm, Color dark, Color light, int x, int y, int w, int h)
	{
		// limitation de la taille des rectangles (bug de l'awt)
		if (x < 0) { w += x; x = 0; };
		if (w > 2000) w = 2000;
		
		g.setColor(dark);
		g.clearRect(x, y, w-1, h);
		g.drawRect(x, y, w-1, h);

	}
	
	public TExp	getExp(){
		return TExpMaker.gExpMaker.createSilence(Color.white,0,0,0,fDur);
	}
}
