/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.file.html.parser;

/*******************************************************************************************
*
*	 HtmlException (classe) :exception lancer par le HTML tokeniser
* 
*******************************************************************************************/

public final class HtmlException extends Exception {
	public HtmlException(){}
	public HtmlException(String s){super(s);}
}
