/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.editor.constructors;

import grame.elody.editor.misc.TGlobals;
import grame.elody.editor.misc.applets.BasicApplet;
import grame.elody.editor.treeeditor.TreePanel;
import grame.elody.lang.TExpMaker;

import java.awt.BorderLayout;
import java.awt.ScrollPane;

//===========================================================================
//TreeApplet : applet visualisant un TreePanel
//===========================================================================

public class TreeApplet extends BasicApplet {
	public TreeApplet() {
		super(TGlobals.getTranslation("Structured_Editor"));
		setLayout(new BorderLayout());
		setSize (250, 500);
	} 
    public void init() {
		//Define.getButtons(this);
    	ScrollPane p = new ScrollPane();
    	add("Center", p);
		p.add(new TreePanel ( null, 0, TExpMaker.gExpMaker.createNull() ));
		moveFrame (200, 240);
	}   
}
