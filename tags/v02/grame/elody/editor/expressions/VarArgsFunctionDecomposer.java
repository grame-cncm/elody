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
