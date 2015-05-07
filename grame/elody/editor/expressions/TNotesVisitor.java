/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.editor.expressions;

import grame.elody.lang.TExpMaker;
import grame.elody.lang.texpression.TInput;
import grame.elody.lang.texpression.expressions.TEvent;
import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.lang.texpression.expressions.TNullExp;
import grame.elody.lang.texpression.valeurs.TApplVal;
import grame.elody.lang.texpression.valeurs.TClosure;
import grame.elody.lang.texpression.valeurs.TSequenceVal;
import grame.elody.lang.texpression.valeurs.TValue;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;


public class TNotesVisitor extends TGraphVisitor
{
	NotesBox 	nb;
	int 		limit;

	public boolean stopVisite (int date, float offset) { return (date > limit) || ((offset<1) && (offset!=0)); }
	
	public int getLimit (NotesBox b, int w) {
		int limite = (int)Math.min(b.posToDate (w-1), 3600000);
		return limite;
	}
		
	
	public boolean initialize (TExp exp, Graphics g, Dimension d) {
		if (super.initialize (exp, g, d)) {
			if (exp instanceof TNullExp) return false;
			int factor = (nb!=null)	? nb.factor : 3000 + Math.max (d.width-64, 0) * 70;
			nb = new NotesBox (g, size, factor);
			limit = getLimit (nb, d.width);
			nb.drawGrid();
			return true;
		}
		return false;
	}
	/*
	public void Visite (TEvent ev, int date, Object arg) {
		TVector val = ev.val;
		if (val.getVal(TEvent.TYPE) != TEvent.SILENCE) {
			int pitch = (int)val.getVal(TEvent.PITCH);
			int dur   = (int)val.getVal(TEvent.DURATION);
	 		nb.drawNote (date, pitch, dur, ev.color);
 		}
 		//super.Visite (ev, date, arg); // ajout Steph 20/07/98
	}
	*/
	
	// modif steph le 21/07/98
	
	public void Visite (TEvent ev, int date, Object arg) {
		if (ev.getType() != TExp.SILENCE) {
			nb.drawNote (date, (int)ev.getPitch(), (int)ev.getDur(), ev.getColor());
 		}
 	}
 	
 	
 	public void Visite(TInput ev,int date, Object arg) {
		if(ev.Duration() > 0) nb.drawNote (date, 60+(int)ev.getPitch(), Math.min((int)ev.Duration(),100000), Color.black);
	}

	
	public void Visite (TValue ident, TValue body, int date, TClosureVisitor cv, float durId, float durBody) {
		ident.Accept (cv, date, cv);
		yield();
		if (!stopVisite(date, durId))
			body.Accept(cv, (int)(date+durId), cv);
		int low  = Math.max(0, cv.getLow() - 1);
		int high = Math.min(127, cv.getHigh() + 1);
		nb.drawFrame (date, low, high, durId, Color.cyan, true);
		nb.drawFrame (date+(int)durId, low, high, durBody, Color.black, false);
	}
	public void Visite (TClosure val, int date, Object arg) {
    	TValue ident = val.getValArg1();
    	TValue body = val.getValArg2();
		float bdur = body.Duration();
		float idur = ident.Duration(); 
		float coef = (Float.isInfinite(idur) || Float.isInfinite(bdur)) ? 1 : val.Duration() / (idur + bdur);
		if (coef != 1) {
			TExpMaker maker = TExpMaker.gExpMaker;
			maker.expandVal (ident, coef);
			maker.expandVal (body, coef);
		}
		BoundsTracker bt;
		if (arg instanceof TClosureVisitor) {
			TClosureVisitor cv = (TClosureVisitor)arg;
			bt = cv.getTracker ();
			coef *= cv.coef;
		}
		else bt = new BoundsTracker (127,0);
		idur *= coef; bdur *= coef;
		TClosureVisitor ncv = new TClosureVisitor (nb, limit, bt, coef);
		Visite (ident, body, date, ncv, idur, bdur);
	}

	public void Visite (TApplVal val, int date, Object arg) { 
		BoundsTracker bt = (arg instanceof TrackPitchVisitor) ?
							((TrackPitchVisitor)arg).getTracker() : new BoundsTracker (127,0);
		TApplicationVisitor appVis = new TApplicationVisitor(nb, limit, bt);
		appVis.Visite (val, date, arg);
	}
	public int dateToPos (int date) { return (nb != null) ? nb.dateToPos (date) : 0; }
	public int posToDate (int x) 	{ return (nb != null) ? nb.posToDate (x) : 0; }
    public String toString() 		{ return "TNotesVisitor"; }
}

final class NotesBox {
	Graphics graphic; Dimension size; 
	int factor; double wUsedCoef;
	static final double coef = Math.PI / 2;
	static final int step[] = { 1000, 5000, 10000, 30000, 60000 };
	static final Color ct[] = { Color.lightGray, Color.orange, Color.pink, Color.red, Color.darkGray };
	
	
	public NotesBox (Graphics g, Dimension d, int factor) {
		graphic = g;
		size = d;
		this.factor = factor;
//		factor = 3000 + Math.max (d.width-64, 0) * 70;
		wUsedCoef = 1; int last=lastPos();
		wUsedCoef = (double)size.width / last;
	}
	private int lastPos () {
		int x = 0;
		for (int date=60000, prev=0, w=size.width - 3; x < w; date+=60000) {
			x = dateToPos (date);
			if ((x - prev) < 4) break;
			prev = x;
		}
		return Math.min(size.width, x+2);
	}

	public final double usedWidth () 	{ return size.width * wUsedCoef; }
	public int dateToPos (int date) {
		double a = Math.atan((double)date/factor) / coef;
		return (int)(a * usedWidth());
	}
	public int posToDate (int pos) {
		double d = pos;
		d /= usedWidth();
		return (int)(Math.tan(d*coef) * factor);
	}
	public int pitchToPos (int pitch) 	{
		int h = size.height - 1;
		return h - ((h * pitch) / 127); 
	}
//	public int posToPitch (int pos) 	{ return (pos * 128) / size.height; }
	public void drawGrid () {
		int pitch = 12;
		while (pitch < 127) {
			int y = pitchToPos (pitch);
			graphic.setColor ((pitch == 60) ? new Color(175,175,255) : Color.lightGray);
			graphic.drawLine (0,y,size.width-1,y);
			pitch+=12;
		}
		//int step[] = { 1000, 5000, 10000, 30000, 60000 };
		//Color ct[] = { Color.lightGray, Color.orange, Color.pink, Color.red, Color.darkGray };
		for (int i=0, w=size.width - 3; i<5; i++) {
			graphic.setColor (ct[i]);
			int date = 0, prev = 0, x = 0;
			do {
				x = dateToPos (date + step[i]);
				date += step[i];
				graphic.drawLine (x,0,x,size.height-1);
				if ((x - prev) < 4) break;
				prev = x;
			} while (x < w);
		}
	}
	public void drawFrame (int date, int lowPitch, int highPitch, float dur, Color color, boolean arg) {
    	graphic.setColor (color);
    	int x = dateToPos (date);
    	int w = Float.isInfinite(dur) ? size.width - x : dateToPos (date + (int)dur) - x - (arg ? 1 : 0);

    	int y  = pitchToPos (highPitch);
    	int yl = pitchToPos (lowPitch);
    	int hn = Math.max(1, yl - pitchToPos (lowPitch+1));
    	int h  = yl + hn - y;
    	int n  = (arg ? 1 : 2);
		graphic.drawRect (x, y - n, w, h + n*2);
	}
	public void frameNote (int date, int pitch, int dur, Color noteColor, Color framecolor) {
    	int x = dateToPos(date);
    	int xo = (x > 1) ? 1 : 0;
    	int y = pitchToPos(pitch);
    	int yo = (y > 1) ? 1 : 0;
    	int w = Math.max(1, dateToPos (date + dur) - x);
    	int h = Math.max(1, y - pitchToPos (pitch+1));
    	graphic.setColor (noteColor);
		graphic.fillRect (x, y, w, h);
    	graphic.setColor (framecolor);
		graphic.drawRect (x-xo, y-yo, w + xo + 1, h + yo + 1);
	}
	public void drawNote (int date, int pitch, int dur, Color color) {
    	graphic.setColor (color);
    	int x = dateToPos (date);
    	int y = pitchToPos (pitch);
    	int w = Math.max(1, dateToPos (date + dur) - x);
    	int h = Math.max(1, y - pitchToPos (pitch+1));
		graphic.fillRect (x, y, w, h);
		graphic.drawLine (x, y-1, x, y+h);
	}
}

final class TApplicationVisitor extends TrackPitchVisitor
{
	Color color;

	public TApplicationVisitor (NotesBox nb, int limit, BoundsTracker bt) {
		super (nb, limit, bt);
	}
	/*
	public void Visite (TEvent ev, int date, Object arg) {
		TVector val = ev.val;
		int pitch = (val.getVal(TEvent.TYPE) != TEvent.SILENCE) ? (int)val.getVal(TEvent.PITCH) : 60;
		int dur   = (int)val.getVal(TEvent.DURATION);
 		nb.frameNote (date, pitch, dur, ev.color, color);
		bounds.track ((int)ev.val.getVal(TEvent.PITCH));
	}
	*/
	// modif steph le 21/07/98
	
	public void Visite (TEvent ev, int date, Object arg) {
		int pitch = (ev.getType() != TExp.SILENCE) ? (int)ev.getPitch() : 60;
		int dur   = (int)ev.getDur();
 		nb.frameNote (date, pitch, dur, ev.getColor(), color);
		bounds.track ((int)ev.getPitch());
	}
	
	public void Visite (TApplVal val, int date, Object arg) {
		color = Color.white;
		TValue a1 = val.getValArg1();
		a1.Accept(this, date, arg);
		float offset = a1.Duration();
		yield();
		if (!stopVisite (date, offset)) {
			color = Color.yellow;
			TValue a2 = val.getValArg2();
			a2.Accept(this, (int)(date + offset), arg);
		}
	}
}

final class TClosureVisitor extends TrackPitchVisitor
{
	float coef;
	
	public TClosureVisitor (NotesBox nb, int limit, BoundsTracker bt, float coef) {
		super (nb, limit, bt);
		this.coef = coef;
	}
	
	/*
	public void Visite (TEvent ev, int date, Object arg) {
		TVector val = ev.val;
		if (val.getVal(TEvent.TYPE) != TEvent.SILENCE) {
			int pitch = (int)val.getVal(TEvent.PITCH);
			int dur   = (int)(val.getVal(TEvent.DURATION) * coef);
	 		nb.drawNote (date, pitch, dur, ev.color);
			bounds.track ((int)ev.val.getVal(TEvent.PITCH));
 		}
	}
	*/
	
	// modif steph le 21/07/98
	
	
	public void Visite (TEvent ev, int date, Object arg) {
		if (ev.getType() != TExp.SILENCE) {
			int pitch = (int)ev.getPitch();
			int dur   = (int)(ev.getDur() * coef);
	 		nb.drawNote (date, pitch, dur, ev.getColor());
			bounds.track ((int)ev.getPitch());
 		}
	}
	
	
	public void Visite (TSequenceVal val, int date, Object arg) {
		val.getValArg1().Accept(this, date, arg);
		float offset = val.getValArg1().Duration() * coef;
		yield();
		if (!stopVisite (date, offset)) {
			val.getValArg2().Accept(this, (int)(date + offset), arg);
		}
	}
	public void Visite (TClosure val, int date, Object arg) {
		super.Visite (val, date, this);
	}
	public void Visite (TApplVal val, int date, Object arg) { 
		super.Visite (val, date, this);
	}
}

class BoundsTracker
{
	int low; int high;
	
	public BoundsTracker (int low, int high) {
		this.low = low;
		this.high = high;
	}
	public void track (int value) {
 		if (value < low) low = value;
 		if (value > high) high = value;
	}
	public int getLow ()	{ return low; }
	public int getHigh ()	{ return high; }
}

class TrackPitchVisitor extends TNotesVisitor
{
	BoundsTracker bounds;

	public TrackPitchVisitor (NotesBox nb, int limit, BoundsTracker bt) {
		this.nb = nb;
		this.limit = limit;
		bounds = bt;
	}
	/*
	public void Visite (TEvent ev, int date, Object arg) {
		super.Visite (ev, date, arg);
		bounds.track ((int)ev.val.getVal(TEvent.PITCH));
	}
	*/
	
	// modif steph le 21/07/98
	
	public void Visite (TEvent ev, int date, Object arg) {
		super.Visite (ev, date, arg);
		bounds.track ((int)ev.getPitch());
	}
	
	public int getLow ()	{ return bounds.getLow(); }
	public int getHigh ()	{ return bounds.getHigh(); }
	public BoundsTracker getTracker()	{ return bounds; }
}
