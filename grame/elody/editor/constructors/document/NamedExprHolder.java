/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.editor.constructors.document;

import grame.elody.editor.expressions.ExprHolder;
import grame.elody.editor.expressions.TGraphVisitor;
import grame.elody.lang.TExpMaker;
import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.lang.texpression.expressions.TNamedExp;
import grame.elody.lang.texpression.expressions.TNullExp;

import java.awt.TextField;

public class NamedExprHolder extends ExprHolder {
	TextField nameHolder;	
	
	public NamedExprHolder (TExp e, TGraphVisitor v, boolean accept) {
		super (e, v, accept);
	}
	public String getName() 			{ return nameHolder.getText (); }
	public void showName(String name) 	{ nameHolder.setText (name); }
	public TExp getExpression () {
		TExp e = super.getExpression();
		String name = getName();
		if (name.length() == 0) {
			return e;
		} else {
			return TExpMaker.gExpMaker.createNamed (e, getName());
		}
	}
	
	public void setExpression (TExp exp) {
		if ((exp instanceof TNullExp) && (super.getExpression() instanceof TNullExp))
			return;
	
		if (exp instanceof TNamedExp) {
			TNamedExp named = (TNamedExp)exp;
			//showName (new String(named.getName().toCharArray()));		
			showName (named.getName());			
			super.setExpression (exp.getArg1());
		}else {
			showName ("");
			super.setExpression (exp);
		}
	}
}
