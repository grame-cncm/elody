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

import grame.elody.lang.texpression.expressions.TEvent;
import grame.elody.lang.texpression.valeurs.TClosure;
import grame.elody.lang.texpression.valeurs.TSequenceVal;

public class TStatVisitor extends TGraphVisitor {
	public void Visite (TEvent val, int date, Object arg) {
	}
	public void Visite (TSequenceVal val, int date, Object arg) {
		val.getValArg1().Accept(this, date, arg);
		date += (int) val.getValArg1().Duration();
		val.getValArg2().Accept(this, date, arg);
	}
	public void Visite (TClosure val, int date, Object arg) {
	}
    public String toString() 		{ return "TStatVisitor"; }
}
