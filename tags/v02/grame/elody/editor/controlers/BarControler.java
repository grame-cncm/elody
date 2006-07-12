package grame.elody.editor.controlers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

public class BarControler extends Controler
{
	private int bord, fixedW;
	Dimension inside;
	
	public BarControler (int min, int max, int dir, Color inColor) {
		super (min, max, inColor);
		direction = dir;
		bord = 1;
		fixedW = 10;
	}
    private Rectangle getRect () {
 		Dimension d = getSize();
 		int x = 0; int y = 0;
 		if ((direction==kVertical) && (d.width>fixedW)) {
 			x = (d.width-fixedW)/2;
 			d.width = fixedW;
 		}
 		else if (d.height>fixedW) {
 			y = (d.height-fixedW)/2;
  			d.height = fixedW;
		}
		return new Rectangle (x, y, d.width, d.height);
    }
    public synchronized boolean contains (int x, int y) {
		Rectangle r = getRect ();
		return r.contains (x, y);
    }
    public Dimension getMinimumSize () {
		return (direction==kVertical) ? new Dimension (fixedW,40) : new Dimension (40,fixedW);
    }
    public Dimension getPreferredSize () {
		return getMinimumSize ();
    }
    public int PointToVal(int x, int y) {
		int offset = (direction==kVertical) ? y : x;
		int val = ((offset-bord) * (max - min)) / totalPoints();
		return (direction==kVertical) ? max-val : val+min;
    }
    protected int ValToOffset(int val) {
		return totalPoints() * (val-min) / (max - min);
    }
    private int offsetToPoint(int offset) {
		return (direction==kVertical) ? getSize().height - bord - offset : bord + offset;
	}
    private int totalPoints() {
		return ((direction==kVertical) ? getSize().height : getSize().width) - (bord * 2);
    }
    private void drawVSlice(Graphics g, int v1, int v2) {
	  	Rectangle r = getRect();
    	g.fillRect (r.x+bord, v1, fixedW-bord, v2 - v1);
    }
    private void drawHSlice(Graphics g, int v1, int v2) {
	  	Rectangle r = getRect();
    	g.fillRect (v1, r.y+bord, v2 - v1, fixedW-bord);
    }

    public void paint(Graphics g) {
    	Rectangle r = getRect();
		g.setColor (Color.white);
		g.fillRect (r.x, r.y, r.width, r.height);
		g.setColor (Color.black);
		g.drawRect (r.x, r.y, r.width, r.height-=1);
		g.setColor (inColor);
		int offset = ValToOffset (getValue());
		if (direction==kVertical)	g.fillRect (r.x+1, offsetToPoint(offset), fixedW-1, offset);
		else						g.fillRect (1, r.y+1, offset, fixedW-1);
    }
   	public void update(int oldValue, int newValue) {
    	Graphics g = getGraphics ();
    	if ((g!=null) && (oldValue!=newValue)) {
			g.setColor ((newValue < oldValue) ? Color.white : inColor);
			int v1 = offsetToPoint (ValToOffset (Math.min(newValue, oldValue)));
			int v2 = offsetToPoint (ValToOffset (Math.max(newValue, oldValue)));
			if (direction==kVertical)	drawVSlice (g,v2,v1);
			else						drawHSlice (g,v1,v2);
		}
 	}
}
