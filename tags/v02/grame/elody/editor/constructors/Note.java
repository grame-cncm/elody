package grame.elody.editor.constructors;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

public class Note {
	Note next;
    int pitch;
   	int x, y, w;
   	Color color;

    Note (int h, Color c) { pitch = h; color = c; }
    
    public int pitch () { return pitch; }
    public Rectangle area () { return new Rectangle (x, y, w, w); }
    public boolean inside (int px, int py) {
     	return (px >= x) && (px <= x + w) && (py >= y) && (py <= y + w);
    }
    public void paintOval(Graphics g, Point where, int size) {
    	int r = size / 2;
    	x = where.x - r;
    	y = where.y - r;
    	w = size;
    	g.setColor (color);
		g.fillOval (x, y, size, size);
    	if (color.equals(Color.white)) {
    		g.setColor (Color.black);
			g.drawOval (x, y, size, size);
		}
    }
 
     public String toString() {
    	return new StringBuffer().append("Note [").append(pitch).append("]").toString();
    }
}
