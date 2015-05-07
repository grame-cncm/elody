/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.lang.texpression.expressions;

import grame.elody.lang.texpression.TInput;

/*******************************************************************************************
*
*	 TExpVisitor (classe) : � impl�menter dans des visiteur d'expression concr�t
*
*******************************************************************************************/

public class TExpVisitor {
	public Object Visite( TExp s ,Object arg) {return null;}
	
	public Object Visite( TEvent s ,Object arg) {return null;}
	public Object Visite( TInput s ,Object arg) {return null;}
	public Object Visite( TApplExp s ,Object arg){return null;}
	public Object Visite( TAbstrExp s ,Object arg){return null;}
	public Object Visite( TNamedExp s ,Object arg){return null;}
	public Object Visite( TSequenceExp s ,Object arg){return null;}
	public Object Visite( TMixExp s ,Object arg){return null;}
	public Object Visite( TModifyExp s ,Object arg){return null;}
	public Object Visite( TExpandExp s ,Object arg){return null;}
	public Object Visite( TRestExp s ,Object arg){return null;}
	public Object Visite( TBeginExp s ,Object arg){return null;}
	public Object Visite( TIdent s ,Object arg){return null;}
	public Object Visite( TUrlExp s ,Object arg){return null;}
	public Object Visite( TNullExp s ,Object arg){return null;}
	public Object Visite( TYAbstrExp s ,Object arg){return null;}
	public Object Visite( TMuteExp s ,Object arg){return null;}
	public Object Visite( TArrayExp s ,Object arg){return null;}
}
