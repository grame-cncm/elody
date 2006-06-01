package grame.elody.editor.constructors;

import grame.elody.editor.constructors.colors.ColorHolder;
import grame.elody.editor.constructors.colors.ColorNotifier;
import grame.elody.editor.misc.Define;
import grame.elody.editor.misc.applets.Singleton;
import grame.elody.editor.misc.draganddrop.DragHolder;
import grame.elody.editor.misc.draganddrop.TColorContent;
import grame.elody.lang.TExpMaker;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.util.Observer;

public class Colors extends Singleton {
	public Colors() {
		super("Colors");
	}
 
 	public void init() {
		int n = Define.getExpColors().length + 1;
		setLayout(new GridLayout(1, n, 4, 4));
		setSize((n * 24) + 6, 32);
		
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
		g.setFont (new Font("Dialog", Font.PLAIN, 12));
		g.drawString("IN",p.x+1, p.y+15 );
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