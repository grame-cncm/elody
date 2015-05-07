/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.editor.treeeditor;

import grame.elody.editor.constructors.colors.DraggedColor;
import grame.elody.editor.misc.draganddrop.DragHolder;
import grame.elody.editor.misc.draganddrop.TColorContent;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

//===========================================================================
//ColorZone : zone de drag & drop pour les couleurs
//===========================================================================

public class ColorZone extends DragHolder {
	Color 			fColor;
	ColorObserver	fObserver;

    public ColorZone (ColorObserver observer, Color c) {
    	super(true);
    	fColor = c;
    	fObserver	= observer;
    	setSize (getPreferredSize());
    }    
	public Dimension getPreferredSize () 	{ return new Dimension (20, 20); }
	public Dimension getMinimumSize () 		{ return getPreferredSize (); }

    public void paint(Graphics g, Point p, Dimension d) {
		g.setColor	(fColor);
		g.fillRect (p.x, p.y, d.width, d.height);
   	}
	public Object getObject () 		 { return new DraggedColor(fColor); }
	public boolean accept (Object o) { return (o instanceof TColorContent); }

	public void setColor (Color c) {
		fColor = c;
		repaint();
	}
	public void drop (Object o, Point where) {
		if (o instanceof TColorContent) { 
			setColor(((TColorContent)o).getColor());
			informObserver();
		}
	}
	
	private void informObserver () {
		if (fObserver != null) fObserver.dropColor(fColor);
	}

    public String toString() 	{ return "ColorHolder"; }
}
