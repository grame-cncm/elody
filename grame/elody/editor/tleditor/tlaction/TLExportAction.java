/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.editor.tleditor.tlaction;

import grame.elody.editor.misc.applets.BasicApplet;
import grame.elody.editor.misc.draganddrop.DragAble;
import grame.elody.editor.misc.draganddrop.DragAndDrop;
import grame.elody.editor.tleditor.TLPane;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseEvent;

//-------------------------------------------------
// action d'exportation du contenu de l'editeur
//-------------------------------------------------

public class TLExportAction extends TLDragAction implements DragAble {
	DragAndDrop fDrag;
	BasicApplet	fApplet;
	DragAble fAction;
	
	
	public TLExportAction (TLPane pane, MouseEvent m, BasicApplet aplt, DragAble action){
		super(pane);
		fApplet = aplt;
		fPane.setFAutoScroll(false);
		fAction = action;
		fDrag = new DragAndDrop (aplt, this, m.getX(), m.getY());
		
	}
	
	// Probleme d'offset
	
	public void mouseDragged(MouseEvent m) {
		Point p = globalOffset(fApplet);
		fDrag.doDrag (m.getX()+p.x, m.getY()+p.y);
	} 
   	
    private Point globalOffset (BasicApplet aplt) {
    	Container c=aplt;
    	Point offset = new Point(0,0);
    	while (c!=null) {
    		Point p = c.getLocation();
    		offset.x += p.x;
    		offset.y += p.y;
    		if (c instanceof BasicApplet) {
	    		BasicApplet a = (BasicApplet)c;
	    		Insets in = a.getFrame().getInsets();
    			offset.x -= in.left;
    			offset.y -= in.top;
    			break;
    		}
    		c = c.getParent();
    	}
    	//System.out.println ("offset = " + offset + ", loconscreen = " + this.getLocationOnScreen());
    	return offset;
    }

	public void mouseReleased(MouseEvent m) {
		Point p = globalOffset(fApplet);
		fDrag.stopDrag (m.getX()+p.x, m.getY()+p.y);
		fPane.setFAutoScroll(true);
	}
	
	public void mouseClicked(MouseEvent m) {}
	public void clearVisualFeedback(Graphics g) { drawVisualFeedback(g, false); }
	public void drawVisualFeedback(Graphics g, boolean ctrlPressed) {}
	
	public void 	dragStart(){}
	public void 	dragStop(){}
	
	public Object 	getObject() {return fAction.getObject();}
}
