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

import grame.elody.editor.expressions.ExprHolder;
import grame.elody.editor.expressions.VarGraphExprHolder;
import grame.elody.editor.misc.TGlobals;
import grame.elody.editor.misc.applets.BasicApplet;

import java.awt.BorderLayout;

public class BigDisplay extends BasicApplet {
	public BigDisplay() {
		super(TGlobals.getTranslation("Graphic_Display"));
		setLayout(new BorderLayout());
		setSize(400, 200);
	}

	public void init() {
		ExprHolder eh = new VarGraphExprHolder(null, true);
		add("Center", eh);
		moveFrame(200, 240);
	}
}
