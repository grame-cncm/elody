package grame.elody.editor.misc.draganddrop;

import grame.elody.editor.misc.appletframe.AppletFrame;
import grame.elody.lang.texpression.expressions.TExp;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.util.Enumeration;

public class DragAndDrop {
	// état statique
	final Component fromPanel;	// composant à l'origine du drag
	final DragAble drag;		// la source du drag dans l'applet
	final Object ddObject;		// l'objet draggé
	DropAble drop;

	public DragAndDrop (Component in, DragAble obj) {
		fromPanel = in;
		drag = obj;
     	Object o = drag.getObject();
     	ddObject = (o instanceof TExp) ? new TDraggedExp ((TExp)o) : o;
		drop = null;
     	drag.dragStart();
   		if (fromPanel!=null) fromPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}
	
	public DragAndDrop (Component in, DragAble obj, int clickx, int clicky) {
		this(in, obj);
	}

	private DropAble getDrop (Point p) {
      	DropAble d = global2DropAble (p);
    	
      	return ((d != null) && d.accept(ddObject)) ? d : null;
	}
    public final boolean doDrag (Point p) { return doDrag (p.x, p.y); }
    public boolean doDrag (int x, int y) {
    	// on va chercher l'objet DropAble sous les coordonnées courantes de la souris
    	DropAble d = getDrop (new Point (x, y));
    	if (drop!=null) {  			// il y a déja un DropAble qui a été trouvé précédemment
     		if (!drop.equals(d)) {	// si ce n'est pas le même
     			drop.dropLeave();	// on le quitte
     			drop = null;		// 
     		} else {					// c'est toujours le même
 	    		if (d instanceof ExtendedDropAble) {
 	    			if (d instanceof Component) {
	     				Point droppedAt = global2Local((Component)d, new Point (x, y));
	 	    			((ExtendedDropAble) d).feedback(droppedAt.x, droppedAt.y);
 	    			}
 	    		}
     		}
     	}
     	// on a trouvé un objet DropAble
     	else if (d!=null) {
     		// s'il est différent de celui qui initialise le Drag
     		if (!d.equals(drag)) {
     			drop = d;
     			drop.dropEnter();
     		}
     	}
		return true;
    }
    
    public final boolean stopDrag (Point p) { return stopDrag (p.x, p.y); }
    public boolean stopDrag (int x, int y) {
    	drag.dragStop();
   		if (fromPanel!=null) fromPanel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
      	try {
	  		if (drop!=null) {
	     		drop.dropLeave ();
	     		Point droppedAt = null;
	     		if (drop instanceof Component) {
		     		droppedAt = global2Local((Component)drop, new Point (x, y));
	     		}
 	     		drop.drop (ddObject, droppedAt /*new Point(x, y) droppedAt*/);
	     	}
     	} catch (Exception e) { System.err.println(e); }
      	drop = null;
   		if (fromPanel!=null) fromPanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		return true;
    }

	//___________________________________________________________________________________
	// méthodes statiques
	
	// Convertis un point local relativement à un composant c, en point global
	static public Point local2Global(Component c, final Point lp)
	{
		Point gp = c.getLocationOnScreen();
		gp.x = gp.x + lp.x;
		gp.y = gp.y + lp.y;
		return gp;
	}
     
	// Convertis un point global en local relativement à un composant c
	static public Point global2Local(Component c, final Point gp)
	{
		Point lp = c.getLocationOnScreen();
		lp.x = gp.x - lp.x;
		lp.y = gp.y - lp.y;
		return lp;
	}
	
	// trouve l'applet sous le point global gp
    static public AppletFrame global2AppletFrame (final Point gp) 
    {
    	Enumeration	en = AppletFrame.getAppletList().elements();
    	while (en.hasMoreElements()) {
    		AppletFrame af = (AppletFrame) en.nextElement();
    		if (af.contains(global2Local(af,gp))) {
    			 return af;
    		}
    	}
    	return null;
    }
    		      	
	// trouve le composant dropable sous le point global gp
    static public DropAble global2DropAble (final Point gp) {
      	Component c = global2AppletFrame (gp);
      	if (c != null) {
      		// il y a une applet
      		Component z = c.getComponentAt(global2Local(c,gp)); 
      		while ( !(c instanceof DropAble) && (z != c) ) { 
     			c = z;
      			z = c.getComponentAt(global2Local(c,gp));
      		}
      		if (!(c instanceof DropAble)) {
      			c = null;
      		}
      	} 		
     	return (DropAble)c;
	}
}
