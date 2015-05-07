/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.editor.misc.draganddrop;

import java.awt.Panel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public abstract class DragPanel extends Panel implements MouseListener,
		MouseMotionListener {
	
	// le constructeur
	public DragPanel () {
		addMouseListener (this);
		addMouseMotionListener (this);
	}
	
	// gestion de la souris
    public void mouseEntered(MouseEvent e)	{}
    public void mouseExited(MouseEvent e) 	{}
    public void mouseMoved(MouseEvent e) 	{}
    public void mouseClicked(MouseEvent e)	{}
    
    public void mousePressed(MouseEvent e) {
		DragManager.startDrag (getDragObject(e.getX(), e.getY()), this /*getApplet()*/);
    }
    public void mouseDragged(MouseEvent e) {
		DragManager.mouseDragged (DragAndDrop.local2Global(this, e.getPoint()));
     }
    public void mouseReleased(MouseEvent e) { 
		DragManager.stopDrag (DragAndDrop.local2Global(this, e.getPoint()));
    }
    
    // methode abstraite donnant le "contenu" du drag
    // initié par cet objet 
    public abstract DragAble getDragObject (int x, int y);


	// méthodes privées pour gerer les conversion de coord de 
	// la souris
/*   	private BasicApplet getApplet () {
    	Component c=this;
    	while (c!=null) {
    		if (c instanceof BasicApplet){
    			return (BasicApplet)c;
    		}
    		c = c.getParent();
    	}
    	return null;
   	}
*/
    public String toString() { return "DragPanel"; }

}
