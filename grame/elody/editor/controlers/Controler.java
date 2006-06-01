package grame.elody.editor.controlers;

import grame.elody.editor.misc.Define;
import grame.elody.util.MathUtils;
import grame.elody.util.MsgNotifier;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Panel;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Observer;

public abstract class Controler extends Panel implements MouseListener,
		MouseMotionListener {
	protected int direction;
	protected int value, offsetValue;
	Color inColor; boolean dragStarted;
	int min, max;
	MsgNotifier notifier;
	static final int kHorizontal = 0;
	static final int kVertical = 1;
	
	public static int getKHorizontal() { return kHorizontal; }
	public static int getKVertical() { return kVertical; }
	
	public Controler (int min, int max, Color inColor) {
		this.min = min;
		this.max = max;
		this.inColor = inColor;
		dragStarted = false;
		notifier = new MsgNotifier(Define.BarControlerMsg);
    	addMouseListener (this);
		addMouseMotionListener (this);
	}
    public abstract int PointToVal(int x, int y);
   	public abstract void update(int oldValue, int newValue);

    public Dimension getMinimumSize () {
		return new Dimension (10,10);
    }

    public void setRange (int min, int max, int newValue, int home) { 
		this.min = min;
		this.max = max;
		setValue (newValue);
    }
    
   	public void setValue(int v) {
   		int old = getValue();
   		if (v!=old) {
   			int rv  = MathUtils.setRange (v, min, max);
   			value = rv;
     		offsetValue = 0;
  			if (rv != old) {
    			update (old, rv);
    			notifier.notifyObservers (new Integer(rv));
    		}
    	}
    }
    public void shiftValue (int v) {
  		if (v!=offsetValue) {
   			int old = getValue();
 		   	offsetValue = v;
   			int newVal = getValue();
			if (newVal != old) {
				update (old, newVal);
				notifier.notifyObservers (new Integer(newVal));
			}
    	}
    }
    public int  getValue		() 			{ return MathUtils.setRange(value+offsetValue,min,max); }
    public void addObserver	  	(Observer o) { notifier.addObserver(o); }
    public void deleteObserver	(Observer o) { notifier.deleteObserver(o); }

    public void click (Point p) {
    	if (contains (p.x, p.y)) {
     		dragStarted = true;
   			setValue (PointToVal (p.x, p.y));
    	}
    }
    public boolean drag (Point p) {
    	if (dragStarted) {
			setValue (PointToVal (p.x, p.y));
			return true;
		}
		return false;
    }

    public void mouseEntered(MouseEvent e)	{}
    public void mouseExited(MouseEvent e) 	{}
    public void mouseMoved(MouseEvent e) 	{}
    public void mousePressed(MouseEvent e) { click (e.getPoint()); }
   	public void mouseClicked(MouseEvent e) { click (e.getPoint()); }
    public void mouseDragged(MouseEvent e) { drag (e.getPoint()); }
    public void mouseReleased(MouseEvent e) {
    	if (drag (e.getPoint()))
	    	dragStarted = false;
    }

    public int  getDirection 	() 			{ return direction; }
    public String toString() { return new String ("Controler"); }
}
