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
import grame.elody.lang.texpression.expressions.TExp;

import java.awt.Color;
import java.awt.Label;
import java.awt.LayoutManager;
import java.awt.Panel;

//===========================================================================
//HeaderPanel : affiche le bouton, la zone de l'expression et son genre
//===========================================================================

public class HeaderPanel extends Panel {
	static LayoutManager gHorizontalLayout = new HorizontalLayout(4);

	OpenButton	fOpenButton;
	ExpZone		fExpZone;
	Label		fLabel;
	
	HeaderPanel (TreePanel root, boolean openState, TExp exp, String kindName, Color color)
	{
		super(gHorizontalLayout);
		fExpZone	= new ExpZone (root, exp);
		fLabel		= new Label(kindName);
		fOpenButton = new OpenButton (root, openState);
		fOpenButton.setForeground(color);
		add(fOpenButton);
		add(fExpZone);
		add(fLabel);
	}
	
	void updateHeader(TExp exp, String kindName)
	{
		fExpZone.setExpression(exp);
		fLabel.setText(kindName);
	}
}
