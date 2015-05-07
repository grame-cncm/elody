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

import java.awt.AWTEvent;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

//===========================================================================
//OpenButton : bouton triangle d'ouverture fermeture
//===========================================================================

public class OpenButton extends Canvas {
	Openable	fModel;			// l'objet "ouvrable" dont le bouton va controler l'ouverture
	boolean 	fOpenState;		// l'état ouvert/fermé du bouton
	Dimension	fBestSize;		// la taille idéale du bouton

    public OpenButton(Openable model, boolean openState) 
    {
		fModel = model;
		fOpenState = openState;
		fBestSize = new Dimension(16, 16);
		setSize(fBestSize);
		enableEvents(AWTEvent.MOUSE_EVENT_MASK);
	}

    public void paint (Graphics g) 
    {
    	Dimension d = getSize();
    	int c = Math.min(d.width, d.height);
		if (fOpenState) {
    		int xs[] = {0, c, c/2};
    		int ys[] = {c/4, c/4, 3*c/4  };
    		g.fillPolygon (xs, ys, 3);
    	} else {
    		int xs[] = {c/4, 3*c/4, c/4};
    		int ys[] = {0, c/2, c};
    		g.fillPolygon (xs, ys, 3);
    	}    
    }
    
    public void processMouseEvent(MouseEvent e)
    {
    	if ( (e.getID() == MouseEvent.MOUSE_PRESSED)) {
    		fOpenState = !fOpenState;
    		repaint();
    		if (fOpenState) fModel.open(); else fModel.close();
    	}
    } 

    public Dimension getPreferredSize() 	{ return fBestSize;  }
    public Dimension getMinimumSize() 		{ return fBestSize; }
    public boolean isOpen ()			{ return fOpenState; }
}
