package grame.elody.editor.misc.draganddrop;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

public abstract class DragHolder extends DragPanel implements DragAble, DropAble {
	private int border;
	public Point pos;
	public boolean accept;

    public 	DragHolder (boolean doAccept)  { 
    	pos = new Point (2,2);
    	border = 3;
    	accept = doAccept;
    }
    public abstract void paint(Graphics g, Point p, Dimension d);
    public int border() { return border; }
    
    public final void paint(Graphics g) {
    	Dimension d = getSize();
		g.setColor	(Color.black);
		g.drawRect (pos.x-1, pos.y-1, d.width+1, d.height+1);
		paint (g, pos, d);
   	}
   	
   	public Dimension getPreferredSize() { return new Dimension(40,40);}

	public Dimension getSize() { 
    	Dimension d = super.getSize();
    	d.width  -= border; 
    	d.height -= border;
    	return d;
	}
    public void dragStart() {
		DragAndDropTool.border(getGraphics(), new Point(0,0), super.getSize());
    }
    public void dragStop() {
		DragAndDropTool.border(getGraphics(), new Point(0,0), super.getSize());
    }
	public void dropEnter() {
		DragAndDropTool.border(getGraphics(), new Point(0,0), super.getSize());
    }
	public void dropLeave() {
		DragAndDropTool.border(getGraphics(), new Point(0,0), super.getSize());
    }
	public DragAble getDragObject (int x, int y) {
		return contains(x,y) ? this : null;
    }
    public String toString() 	{ return "DragHolder"; }
}
