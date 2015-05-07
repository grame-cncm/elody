/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

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
