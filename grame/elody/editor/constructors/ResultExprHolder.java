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

import java.awt.Point;

import grame.elody.editor.expressions.ExprHolder;
import grame.elody.editor.expressions.TNotesVisitor;
import grame.elody.editor.misc.draganddrop.TExpContent;
import grame.elody.lang.TExpMaker;
import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.lang.texpression.expressions.TNamedExp;

public abstract class ResultExprHolder extends ExprHolder
{
	RulePanel container;
	
	public ResultExprHolder () {
		super(null, new TNotesVisitor(), true);
	}
	public TExp extractExpr (TExp exp) {
		if (exp instanceof TNamedExp) {
			exp = ((TNamedExp)exp).getArg1();
		}
		return exp;
	}
	public void drop (Object o, Point where) {
		if (o instanceof TExpContent) decomposeExpr(((TExpContent)o).getExpression());
	}
	public abstract boolean accept (TExp exp);
	public boolean accept (Object o) {
		return (o instanceof TExpContent) ? accept(((TExpContent)o).getExpression()) : false;
	}

   	public boolean decomposeExpr(TExp exp) {
    	if (decomposeBasicExpr (exp.convertMixExp())) return true;
   		if (decomposeBasicExpr (exp.convertSeqExp())) return true;
   		if (decomposeBasicExpr (exp.convertApplExp())) return true;
  		if (decomposeBasicExpr (exp.convertBeginExp())) return true;
  		if (decomposeBasicExpr (exp.convertRestExp())) return true;
  		if (decomposeBasicExpr (exp.convertDilateExp())) return true;
   		if (decomposeAbstrExpr (exp.convertAbstrExp())) return true;
		return decomposeAbstrExpr (exp.convertYAbstrExp());
    }
    public boolean decomposeBasicExpr(TExp exp) {
		if (exp != null) {
	    	container.eh1.setExpression (exp.getArg1());
	    	container.eh2.setExpression (exp.getArg2());
			return true;
		}
		else return false;
    }
    public boolean decomposeAbstrExpr(TExp exp) { 
		if (exp != null) {
	    	container.eh1.setExpression (exp.getArg1().getArg1());
	    	container.eh2.setExpression (TExpMaker.gExpMaker.createBody(exp.getArg1(), exp.getArg2()));
			return true;
		}
		else return false;
    }
}