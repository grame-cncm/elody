package grame.elody.editor.expressions;

import grame.elody.editor.misc.BiGraph;
import grame.elody.editor.misc.Define;
import grame.elody.editor.misc.Offscreen;
import grame.elody.editor.misc.TGlobals;
import grame.elody.editor.misc.draganddrop.DragAble;
import grame.elody.editor.misc.draganddrop.DragHolder;
import grame.elody.editor.misc.draganddrop.TExpContent;
import grame.elody.lang.TExpMaker;
import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.util.MsgNotifier;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

public class ExprHolder extends DragHolder implements Observer {
	protected boolean uptodate, refresh;
    protected Offscreen offscreen;
	protected TGraphVisitor visitor;
	protected MsgNotifier notifier;
	protected TExp expr; 
	
	public 	ExprHolder (TExp e, TGraphVisitor v, boolean accept) {
    	super (accept);
    	offscreen 	= new Offscreen(this);
    	init (e, v);
    }   
  	protected void init (TExp e, TGraphVisitor v) {
    	expr = (e == null) ? TExpMaker.gExpMaker.createNull() : e;
     	visitor = v;
   		notifier	= new MsgNotifier (Define.ExprHolderMsg);
    	uptodate = false;
    	refresh = true;
    }
  	public void 	update (Observable o, Object arg) { }
	public Object 	getObject () 		{ return getExpression(); }
	public TExp 	buildExpression() 	{ return expr; }
	public void 	changed () { 
		uptodate = false;
		repaint();
		notifyObservers ();
	}

    public void paint(Graphics g, Point p, Dimension d) {
    	if (offscreen.update(d) || !uptodate || refresh) {
    		Graphics og = new BiGraph (offscreen.getGraphics(), g, p, d);
			og.setColor (Define.nullExpColor);
			og.fillRect (0, 0, d.width, d.height);
    		if (visitor!=null) {
    			visitor.renderExp (getExpression(), og, d);
    		}
    		refresh = false;
    	}
    	offscreen.flush (g, p);
	}
   
	public TExp getExpression() {
		if (!uptodate) {
			try {
				expr = buildExpression ();
				uptodate = true;
			}
			catch (Exception e) {
				System.err.println( this + " getExpression exception : " + e);
				e.printStackTrace();
			}
		}
		return expr;
	}
	public void setExpression (TExp e) { 
		expr = e;
		refresh = uptodate = true;
		repaint();
		notifyObservers ();
	}
	public boolean accept (Object o) {
		if (accept) {
//			if (o instanceof TExp) return true;
			if (o instanceof TExpContent) return true;
		}
		return false;
	}
    public void handleControlClick (MouseEvent e) {
   		TGlobals.player.startPlayer (getExpression());
    }
    public void handleDoubleClick (MouseEvent e) {
    	setCursor (new Cursor(Cursor.WAIT_CURSOR));
		new ExprDecomposer (getExpression());
    	setCursor (new Cursor(Cursor.DEFAULT_CURSOR));
    }
    public void mouseClicked(MouseEvent e) {
    	if (e.isControlDown ()) {
    		handleControlClick (e);
    	}
    	else if (e.getClickCount() > 1) {
    		handleDoubleClick (e);
     	}
    	else super.mouseClicked (e);
    }

	public void drop (Object o, Point where) {
//		if (o instanceof TExp)  setExpression ((TExp)o);
		if (o instanceof TExpContent) setExpression (((TExpContent)o).getExpression());
	}
	public DragAble getDragObject (int x, int y) {
		return (contains(x,y) && (getExpression()!=null)) ? this : null;
    }
    public void addObserver (Observer o) 		{ notifier.addObserver (o); }
    public void deleteObserver (Observer o)		{ notifier.deleteObserver (o); }
    public void notifyObservers () 				{ notifier.notifyObservers (this);}
    public void setMessage (int newMsg) 		{ notifier.setMessage(newMsg); }
    public void setVisitor (TGraphVisitor v) 	{ visitor = v; }
    public String toString() 	{ return "ExprHolder"; }
}
