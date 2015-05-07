/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.editor.constructors.parametrer;

import grame.elody.util.MsgNotifier;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Date;
import java.util.Observer;

public class ResetButton extends Panel implements MouseListener {
	MsgNotifier notifier;
	Color color;
	
	public ResetButton (int msg) {
		notifier = new MsgNotifier (msg);
		color = new Color(175,175,255);
    	addMouseListener (this);
	}
	public void addObserver (Observer obs) {
		notifier.addObserver (obs);
	}
    public long getTime () {
    	Date date = new Date ();
    	return date.getTime();
	}
    public void mouseEntered(MouseEvent e)	{}
    public void mouseExited(MouseEvent e) 	{}
    public void mousePressed(MouseEvent e)	{}
    public void mouseReleased(MouseEvent e)	{}
    public void mouseClicked(MouseEvent e) {
    	Point p = e.getPoint();
    	if (contains(p.x, p.y)) {
    		Graphics g = getGraphics();
			g.setColor (Color.red);
			Dimension d = getPreferredSize();
			g.fillOval (0,0, d.width, d.height);
    		long t = getTime();
 			notifier.notifyObservers ("reset");
 			while ( (getTime() - t) < 500);
 			paint (g);
    	}
    }
	public void paint (Graphics g) {
		g.setColor (color);
		Dimension d = getPreferredSize();
		g.fillOval (0,0, d.width, d.height);
		g.setColor (Color.darkGray);
		g.drawOval (0,0, d.width, d.height);
	}
    public Dimension getMinimumSize () 	{ return new Dimension (6,6); }
    public Dimension getPreferredSize () 	{ return getMinimumSize (); }
}
