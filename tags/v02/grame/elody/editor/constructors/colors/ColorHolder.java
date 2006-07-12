package grame.elody.editor.constructors.colors;

import grame.elody.editor.misc.draganddrop.DragHolder;
import grame.elody.editor.misc.draganddrop.TColorContent;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Observer;

public class ColorHolder extends DragHolder {
	Color color;
	ColorNotifier notifier;

    public ColorHolder (Color c, boolean acceptColor) {
    	super(acceptColor);
    	notifier = new ColorNotifier();
    	color = c;
    	setSize (getPreferredSize());
    }    
	public Dimension getPreferredSize () 	{ return new Dimension (20, 20); }
	public Dimension getMinimumSize () 	{ return getPreferredSize (); }
	public void addObserver (Observer o) {
		notifier.addObserver(o);
	}
    public void paint(Graphics g, Point p, Dimension d) {
		g.setColor	(color);
		g.fillRect (p.x, p.y, d.width, d.height);
   	}
	public Object getObject () 		 { return new DraggedColor(color); }
	public boolean accept (Object o) {
		return accept ? (o instanceof TColorContent) : false;
	}
	public void notify (Object o) {
		notifier.notifyObservers (o);
	}
	public void setColor (Color c) {
		color = c;
		repaint();
		notify (color);
	}
	public void drop (Object o, Point where) {
		if (o instanceof TColorContent) { 
			setColor(((TColorContent)o).getColor());
		}
	}
    public String toString() 	{ return "ColorHolder"; }
}

