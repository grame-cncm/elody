/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.editor.constructors.colors;

import grame.elody.editor.misc.draganddrop.TColorContent;
import grame.elody.editor.misc.draganddrop.TExpContent;
import grame.elody.lang.TExpMaker;
import grame.elody.lang.texpression.expressions.TExp;

import java.awt.Color;

public class DraggedColor implements TColorContent, TExpContent {
	private final Color color;
	
	public DraggedColor (Color c) {
		color = c;
	}
	public TExp getExpression() {
		return  TExpMaker.gExpMaker.createNote(color, 60, 100, 0, 1000);
	}
	public Color getColor() {
		return color;
	}
}
