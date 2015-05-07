/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.file.parser;

import grame.elody.file.html.parser.THtmlParser1;
import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.lang.texpression.expressions.TNullExp;

/*******************************************************************************************
*
*	 ParseOperator (classe) : opération de parsing HTML
* 
*******************************************************************************************/

public abstract class ParseOperator {
	 public abstract  TExp parseExp(THtmlParser1 parser); 
	 public final boolean  checkNull(TExp res) {return (res instanceof TNullExp);}
}
