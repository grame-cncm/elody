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

import grame.elody.lang.texpression.expressions.TExp;

public class TDraggedExp implements TExpContent {
	TExp draggedExp;
	
	public TDraggedExp (TExp exp) 	{ draggedExp = exp; }
	public TExp		getExpression()	{ return draggedExp; }
}
