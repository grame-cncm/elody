/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.editor.treeeditor;

import grame.elody.editor.treeeditor.simplelayouts.HorizontalLayout;

import java.awt.Color;
import java.awt.Font;
import java.awt.Label;
import java.awt.LayoutManager;
import java.awt.Panel;

public class ColorPanel extends Panel implements ColorObserver {
	static LayoutManager 	gHorizontalLayout = new HorizontalLayout(4);
	static Font				gFont = new Font("Monospaced", Font.PLAIN, 10);
	
	TreePanel 		fFather;
	int				fSonNumber;
	Label			fLabel;
	ColorZone		fColorZone;
	
	public ColorPanel (TreePanel father, int sonNumber, String kind, Color color)
	{
		setLayout(gHorizontalLayout);
		setFont(gFont);
		fFather 	= father;
		fSonNumber 	= sonNumber;
		fLabel		= new Label(kind);
		fColorZone	= new ColorZone (this, color);
		add(fLabel);
		add(fColorZone);
	}
	public void dropColor(Color c)
	{
		fFather.updateSubColor (c, fSonNumber);
	}
}
