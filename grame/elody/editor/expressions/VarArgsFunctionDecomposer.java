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

import grame.elody.lang.texpression.expressions.TExp;

public class VarArgsFunctionDecomposer {
	int argsCount;
	TExp args[], function;
	
	public VarArgsFunctionDecomposer (TExp exp, int maxArgs) {
		args = new TExp[maxArgs];
		if ((exp != null) && (maxArgs > 0)) {
			decompose (exp, maxArgs);
		}
	}
	public void decompose (TExp exp, int max) {
		argsCount = 0;
		for (int i=0; i<max; i++) {
			TExp f = exp.convertApplExp();
			if (f == null) {
				FunctionTag tag = FunctionTag.untagAppletFunction(exp);
				if (ExprDecomposer.isAbstr (exp) && (tag != null) && (argsCount == tag.argsCount())) {
					function = exp;
					return;
				}
				else {
					argsCount = 0;
				}
			}
			else {
				exp = f.getArg1();
				args[argsCount++] = f.getArg2();
			}
		}
	}
	public int getArgsCount ()		{ return argsCount; }
	public TExp getFunction ()		{ return function; }
	public TExp getArg (int i)		{ return (i < argsCount) ? args[i] : null; }
    public String toString() {
    	return "VarArgsFunctionDecomposer [" + argsCount + "] " + function;
    }
}
