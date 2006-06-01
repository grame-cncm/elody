package grame.elody.editor.misc.draganddrop;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

public class DragAndDropTool {
	public static void border (Graphics g, Point p, Dimension d) {
    	g.setXORMode (Color.white);
		g.drawRect (p.x, p.y, d.width-1, d.height-1);
    	g.setPaintMode ();
	}
    public String toString() 			{ return "DragAndDropTool";}
}
