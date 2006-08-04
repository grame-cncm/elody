package grame.elody.editor.constructors;

import grame.elody.editor.constructors.colors.ColorHolder;
import grame.elody.editor.constructors.colors.ColorNotifier;
import grame.elody.editor.misc.Define;
import grame.elody.editor.misc.TGlobals;
import grame.elody.editor.misc.applets.Singleton;
import grame.elody.editor.misc.draganddrop.DragHolder;
import grame.elody.editor.misc.draganddrop.TColorContent;
import grame.elody.lang.TExpMaker;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.util.Observer;

public class Colors extends Singleton {
	public Colors() {
		super(TGlobals.getTranslation("Colors"));
	}
 
 	public void init() {
		int n = Define.getExpColors().length + 1;
		setLayout(new GridLayout(1, n, 4, 4));
		setSize((n * 32) + 6, 40);
		
		for (int i=0; i<n-1; i++) {
			add( new ColorHolder (Define.getExpColors()[i], false));
		}
		
		add( new InputHolder ());
		moveFrame (330, 280);
	}
}

class InputHolder extends DragHolder
{
	ColorNotifier notifier;
	    
     public InputHolder () {
     	super(false);
    	setSize (getPreferredSize());
    }    
    
	public Dimension getPreferredSize () 	{ return new Dimension (20, 20); }
	public Dimension getMinimumSize () 	{ return getPreferredSize (); }
	public void addObserver (Observer o) {
		notifier.addObserver(o);
	}
    public void paint(Graphics g, Point p, Dimension d) {
       	Point origin = p;
    	Point rect = new Point(d.width,d.height);
    	Point margin = new Point (rect.x/20, rect.y/20);
    	g.setColor(Color.black);
    	g.fillRect(origin.x, origin.y, rect.x, rect.y);
    	g.setColor(Color.white);
    	g.fillOval(origin.x+margin.x, origin.y+margin.y, rect.x-2*margin.x, rect.y-2*margin.y);
    	Point smallRect = new Point (rect.x/5, rect.y/5);
    	Point r = new Point (2*rect.x/7, 2*rect.y/7);
    	g.setColor(Color.black);
    	g.fillRect(origin.x+rect.x-margin.x/2-smallRect.x, origin.y+rect.y/2-smallRect.y/2, smallRect.x, smallRect.y);
    	Point P1 = new Point(origin.x+rect.x/2,origin.y+rect.y/2-r.y);
    	Point P2 = new Point((int)(origin.x+rect.x/2-r.x*Math.sqrt(2)/2),(int)(origin.y+rect.y/2-r.y*Math.sqrt(2)/2));
    	Point P3 = new Point(origin.x+rect.x/2-r.x,origin.y+rect.y/2);
    	Point P4 = new Point((int)(origin.x+rect.x/2-r.x*Math.sqrt(2)/2),(int)(origin.y+rect.y/2+r.y*Math.sqrt(2)/2));
    	Point P5 = new Point(origin.x+rect.x/2,origin.y+rect.y/2+r.y);
    	smallRect = new Point (rect.x/7, rect.y/7);
    	g.fillOval(P1.x-smallRect.x/2, P1.y-smallRect.y/2, smallRect.x, smallRect.y);
    	g.fillOval(P2.x-smallRect.x/2, P2.y-smallRect.y/2, smallRect.x, smallRect.y);
    	g.fillOval(P3.x-smallRect.x/2, P3.y-smallRect.y/2, smallRect.x, smallRect.y);
    	g.fillOval(P4.x-smallRect.x/2, P4.y-smallRect.y/2, smallRect.x, smallRect.y);
    	g.fillOval(P5.x-smallRect.x/2, P5.y-smallRect.y/2, smallRect.x, smallRect.y);
   	}
   	
	public Object getObject () 		 { 
		return TExpMaker.gExpMaker.createInput();
	}
	public boolean accept (Object o) {
		return accept ? (o instanceof TColorContent) : false;
	}
	public void notify (Object o) {
		notifier.notifyObservers (o);
	}
	
	
	public void drop (Object o, Point where) {}
	
    public String toString() 	{ return "ColorHolder"; }
}