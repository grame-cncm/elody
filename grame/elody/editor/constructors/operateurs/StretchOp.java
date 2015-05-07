/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.editor.constructors.operateurs;

import grame.elody.lang.TExpMaker;
import grame.elody.lang.texpression.expressions.TExp;

import java.awt.Image;

public class StretchOp extends Operateur {
	public StretchOp (Image img) 	{ super (img); name = "Stretch"; }
   	public TExp compose(TExpMaker maker, TExp e1, TExp e2) {
  		return maker.createDilate(e1, e2);
    }
}
