/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.editor.expressions;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

public class PlayExprHolder extends VarGraphExprHolder
{
	boolean dateOn; int oldDate;
	
	//------------------------------------------
	public 	PlayExprHolder () {
		super (null, true);
	}
	
	//------------------------------------------
	public 	PlayExprHolder (boolean accept) {
		super (null, accept);
	}
	
	//------------------------------------------
	private void drawDate (Graphics g, int x, int h) {
		int y = pos.y;
		g.drawLine (x, y, x, y+h);
	}
	//------------------------------------------
	private void drawDate (int x) {
		Graphics g = getGraphics();
    	g.setXORMode (Color.white);
    	int h = getSize().height;
		if (dateOn) drawDate (g, oldDate, h);
		drawDate (g, x, h);
		oldDate = x;
		dateOn = true;
    	g.setPaintMode ();
	}
	//------------------------------------------
	public void hideDate() {
		if (dateOn) {
			drawDate (oldDate);
			oldDate = 0;
			dateOn = false;
		}
	}
	//------------------------------------------
	public void showDate (int date) {
		int x = dateToPos (date);
		if (x != oldDate) {
			drawDate (x);
		}
	}	
	//------------------------------------------
    public void paint(Graphics g, Point p, Dimension d) {
    	super.paint (g, p, d);
		if (dateOn)  drawDate (g, oldDate, d.height);
    }
}
