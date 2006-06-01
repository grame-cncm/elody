package grame.elody.editor.misc;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

public class Offscreen {
	Component	comp;
    Image 		image;
    Dimension 	size;
    Graphics 	graphics;

	public Offscreen (Component c) 					{ comp = c; }
	public Graphics getGraphics () 					{ return graphics; }
	public void 	flush (Graphics g, Point p) 	{ g.drawImage(image, p.x, p.y, null); }
	public boolean 	update (Dimension d) {
		if ((image == null) || (d.width != size.width) || (d.height != size.height)) {
		    image = comp.createImage(d.width, d.height);
		    size = d;
		    graphics = image.getGraphics();
		    graphics.setFont(comp.getFont());
		    return true;
		}
		return false;
	}
}
