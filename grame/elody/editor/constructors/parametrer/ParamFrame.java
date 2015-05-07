/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.editor.constructors.parametrer;

import grame.elody.editor.expressions.ExprHolder;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Panel;

public class ParamFrame extends Panel {
	protected Color borderColor;

	public ParamFrame (Color color) {
		super ();
		borderColor = color;
		setLayout(new GridBagLayout());
	}
	//----------------------------------------------
	public void init (ExprHolder eh, int w, int h) {
   		GridBagLayout gbl = (GridBagLayout)getLayout();
		GridBagConstraints c = new GridBagConstraints();
		setConstraints (c, GridBagConstraints.REMAINDER, GridBagConstraints.NONE, 
							GridBagConstraints.NORTH,
							w, h, 0,0, 5,0,0,5);
		gbl.setConstraints (eh, c);
		add (eh);
	}
	public void paint (Graphics g, Color color) {
		Dimension d = getSize ();
		g.setColor (color);
		g.drawRect (0,0,d.width-1, d.height-1);
	}
	public void paint (Graphics g) {
		paint (g, borderColor);
	}
	//----------------------------------------------
	protected void setConstraints (GridBagConstraints c, int gridw, int fill, int anchor, 
	                  				int ipadx, int ipady, double wgtx, double wgty,
	                  				int top, int left, int right, int bottom )
	{
		c.gridx = c.gridy = GridBagConstraints.RELATIVE;
		c.gridwidth = gridw; c.gridheight = 1;
		c.fill = fill; c.anchor = anchor;
		c.ipadx = ipadx; c.ipady = ipady;
		c.weightx = wgtx; c.weighty = wgty;
		c.insets = new Insets(top, left, bottom, right);
	}
}
