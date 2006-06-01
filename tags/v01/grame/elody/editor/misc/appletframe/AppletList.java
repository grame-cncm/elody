package grame.elody.editor.misc.appletframe;

import java.applet.Applet;
import java.util.Enumeration;
import java.util.Vector;

public class AppletList extends Vector {
	public synchronized void  add (AppletFrame f) {
		insertElementAt (f, 0);
	}

	public synchronized void  remove (AppletFrame f) {
		removeElement (f);
	}

	public synchronized void  toFront (AppletFrame f) {
		removeElement (f);
		insertElementAt (f, 0);
	}
	public synchronized Applet  getApplet (java.awt.Point c) {
		Enumeration en = elements();
		if (en==null) return null;
		while (en.hasMoreElements()) {
			AppletFrame ddf = (AppletFrame)en.nextElement();
			java.awt.Point floc = ddf.getLocation();
			//System.out.printl("floc = "+ floc + ", " + ddf.getLocationOnScreen());
			if (ddf.contains (c.x - floc.x, c.y - floc.y)) {
				java.awt.Point aloc = ddf.applet.getLocation();
				c.x -= floc.x + aloc.x;
				c.y -= floc.y + aloc.y;
				return ddf.applet;
			}
		}
		return null;
	}
	public synchronized Applet  getApplet (String className) throws ClassNotFoundException {
		Enumeration en = elements();
		if (en==null) return null;
		while (en.hasMoreElements()) {
			AppletFrame a = (AppletFrame)en.nextElement();
			if (a.applet.getClass().equals(Class.forName(className)))
				return a.applet;
		}
		return null;
	}

	public Applet frontApplet () {
		AppletFrame a = (AppletFrame)firstElement();
		return (a!=null) ? a.applet : null;
	}
	public boolean frontApplet (Applet a) {
		Applet front = frontApplet();
		return front == a;
	}
}
