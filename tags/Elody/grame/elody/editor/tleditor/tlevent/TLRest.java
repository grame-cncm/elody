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
