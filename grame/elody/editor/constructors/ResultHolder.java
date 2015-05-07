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

import grame.elody.editor.expressions.DelayedExprHolder;
import grame.elody.editor.expressions.ExprHolder;
import grame.elody.editor.expressions.FunctionTag;
import grame.elody.editor.expressions.TNotesVisitor;
import grame.elody.editor.misc.Define;
import grame.elody.lang.TExpMaker;
import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.lang.texpression.expressions.TNullExp;
import grame.elody.util.MsgNotifier;

import java.util.Observable;

public class ResultHolder extends DelayedExprHolder {
	ExprHolder exprHolders[];
	
	public ResultHolder (ExprHolder exprHolders[]) {
		super(null, new  TNotesVisitor(), false, 500);
		this.exprHolders = exprHolders;
	}
	public void dragStart() {
		for (int i=0; i < exprHolders.length; i++) exprHolders[i].dragStart ();
		super.dragStart();
	}
	public void dragStop() {
		for (int i=0; i < exprHolders.length; i++) exprHolders[i].dragStop ();
		super.dragStop();
	}
  	public void update (Observable o, Object arg) { 
  		MsgNotifier msg = (MsgNotifier)o;
  		if (msg.message() == Define.ExprHolderMsg) {
  			changed ();
 		}
  	}
	public int countExpr() { 
		int max = exprHolders.length, n=0;
		for (int i=0; i< max; i++) {
   			if (!(exprHolders[i].getExpression() instanceof TNullExp)) n++;
		}
		return n;
	}
	public TExp buildExpression (TExp f) {
		if (f instanceof TNullExp) return f;
		TExpMaker maker = TExpMaker.gExpMaker;
		int max = exprHolders.length;
		for (int i=0; i< max; i++) {
   			if (!(exprHolders[i].getExpression() instanceof TNullExp)) {
   				f = maker.createAppl (f, exprHolders[i].getExpression());
   			}
		}
		return f;
	}
	public TExp buildFunction() { 
		TExpMaker maker = TExpMaker.gExpMaker;
		int n = countExpr();
		TExp note[] = new TExp[n];
		TExp f = maker.createNull();
		for (int i=0; i< n; i++) {
			note[i] = maker.createNote( Define.expColors[i], 60, 100, 0, 1000);
			f = maker.createSeq (note[i], f);
		}
		for (int i=0; i< n; i++) {
			f = maker.createAbstr (note[i], f);
		}
		FunctionTag tagger = new FunctionTag (SeqConstructor.appletName, n);
		return tagger.tag (f);
	}
	public TExp buildExpression() { 
		TExp e = buildExpression (buildFunction());
		TExpMaker.gExpMaker.updateHistory (e);
		return e;
	}
    public String toString() 	{ return "ResultHolder"; }
}
