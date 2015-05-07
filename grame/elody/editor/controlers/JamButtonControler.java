/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.editor.controlers;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;

public class JamButtonControler extends ButtonControler
{
	protected int val;
	protected boolean firstGetValue = true;
	protected boolean firstCtrlDown = true;
	
	public JamButtonControler (int min, int max, int home, Color inColor, Image img) {
		super (min, max, home, inColor, img);
	}
    public void mouseDragged(MouseEvent e) {
    	if (dragStarted) { 
    		if (e.isControlDown()&&firstCtrlDown)
    		{
    			val = getValue();
    			firstCtrlDown = false;
    		}
    		if (!(e.isControlDown()||firstCtrlDown))
    				firstCtrlDown = true;
    		if (firstGetValue)
    		{
    			val = getValue();
    			firstGetValue = false;
    		}
    		int zoneHeight = e.isControlDown() ? 400 : 50;		
    		
    		Point p = e.getPoint();
	    	int n = val + (p.y-12)*(max-min)/zoneHeight;
	    	
	    /*	int n 	= PointToVal (p.x, p.y);
	    	double arc = ((val - n) * 2 * Math.PI) / (max - min + 1);
	    	if (Math.abs(arc) >= Math.PI/2)
	    		n = arc > 0 ? val +1 : val -1;*/
			setValue (n);
    	}
    }
    public void mouseReleased(MouseEvent e) {
    	if (dragStarted) {
    		dragStarted = false;
    		firstGetValue = true;
    		firstCtrlDown = true;
    	}
    }
}
