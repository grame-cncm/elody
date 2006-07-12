package grame.elody.editor.expressions;

import grame.elody.editor.misc.Offscreen;
import grame.elody.editor.misc.draganddrop.DragAble;
import grame.elody.editor.misc.draganddrop.DragPanel;
import grame.elody.lang.texpression.expressions.TExp;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

public class ExprHolderPanel extends DragPanel {
	UnanchoredHolder eHolder;
	
	public ExprHolderPanel () {
		eHolder = new UnanchoredHolder (this, null, new TNotesVisitor(), false);
	}
	public UnanchoredHolder getHolder () 	{ return eHolder; }
	public void setHolder (TExp exp) {
		Dimension d = getSize();
		UnanchoredHolder eh = new UnanchoredHolder (this, exp, new TNotesVisitor(), false);
		eh.setSize (d.width, d.height);
		setHolder (eh);
	}
	public void setHolder (UnanchoredHolder holder) {
		holder.container = this;
		eHolder = holder;
		repaint();
	}
	public void setSize(int w, int h) { 
		super.setSize(w, h); 
		eHolder.setSize (w, h); 
	}
	public void setSize (Dimension d){ setSize (d.width, d.height); }
	public void paint(Graphics g) 	{ eHolder.paint(g); }
    public DragAble getDragObject (int x, int y) {
    	return contains (x, y) ? eHolder : null;
    }
	public void 	dragStart()		{ eHolder.dragStart(); }
	public void 	dragStop()		{ eHolder.dragStop(); }
	public Object 	getObject()		{ return eHolder.getObject(); }
}

class UnanchoredHolder extends ExprHolder
{
	Component container;
	public UnanchoredHolder (Component c, TExp e, TGraphVisitor v, boolean accept) {
		super (e, v, accept);
 		container = c;
  		offscreen = new Offscreen (container);
	}
	public Graphics getGraphics () 		{ return container.getGraphics(); }
}
