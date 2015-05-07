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
