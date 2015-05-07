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

import grame.elody.editor.expressions.TGraphVisitor;
import grame.elody.editor.expressions.TNotesVisitor;
import grame.elody.editor.misc.BiGraph;
import grame.elody.editor.misc.Define;
import grame.elody.editor.misc.Offscreen;
import grame.elody.editor.misc.TGlobals;
import grame.elody.editor.misc.draganddrop.DragAble;
import grame.elody.editor.misc.draganddrop.DragHolder;
import grame.elody.editor.misc.draganddrop.TExpContent;
import grame.elody.lang.TExpMaker;
import grame.elody.lang.texpression.expressions.TExp;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

//===========================================================================
//ExpZone : zone de drag & drop pour les expressions
//===========================================================================

public class ExpZone extends DragHolder implements Observer {
	protected boolean 	fRefresh, fUptodate;
	TGraphVisitor 		fVisitor;
    protected Offscreen fOffscreen;
	TExp 				fExpression; 
	ExpDropObserver		fObserver;
	
	public 	ExpZone (ExpDropObserver observer, TExp e) 
	{
    	super (true);
    	fVisitor 	= new TNotesVisitor();
//fOffscreen 	= null;
     	fOffscreen 	= new Offscreen(this);
     	
   		fExpression = (e == null) ? TExpMaker.gExpMaker.createNull() : e;
    	fRefresh 	= true;
    	fObserver	= observer;
    }   

    public void paint(Graphics g, Point p, Dimension d) {
    	if (true) {
//if (fOffscreen == null) fOffscreen = new Offscreen(this);
    	if (fOffscreen.update(d) || fRefresh) {
    		Graphics og = new BiGraph (fOffscreen.getGraphics(), g, p, d);
			og.setColor (Define.nullExpColor);
			og.fillRect (0, 0, d.width, d.height);
    		if (fVisitor!=null) {
    			fVisitor.renderExp (fExpression, og, d);
    		}
    		fRefresh = false;
    	}
    	fOffscreen.flush (g, p);
    	} 
	}
   
   // ajouté pour voir
  	public void update (Observable o, Object arg) { }
	
	
	public TExp getExpression() { return fExpression; }
	
	public void setExpression (TExp e) { 		//********modif
		fExpression = e; 
		fRefresh = true;
		repaint();
	}
    
    public String toString() 	{ return "ExpZone"; }

	// pour le drag & drop
	public Object 	getObject () 		{ return fExpression; }
	public boolean accept (Object o) 
	{
		return (o instanceof TExp) || (o instanceof TExpContent);
	}
    public void mouseClicked(MouseEvent e) {
    	if (e.isControlDown ()) {
    		TGlobals.player.startPlayer (getExpression());
    	}
    	super.mouseClicked (e);
    }

	public void drop (Object o, Point where) {
		if (o instanceof TExp)  {
			setExpression ((TExp)o);
			informObserver();
		} else if (o instanceof TExpContent) {
			setExpression (((TExpContent)o).getExpression());
			informObserver();
		}
	}
	
	private void informObserver () {
		if (fObserver != null) fObserver.dropExpression(fExpression);
	}
	
	public DragAble getDragObject (int x, int y) {
		return (contains(x,y) && (getExpression()!=null)) ? this : null;
    }
    
	public Dimension getPreferredSize() { return new Dimension(25,25);}
}
