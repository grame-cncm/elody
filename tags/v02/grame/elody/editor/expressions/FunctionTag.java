package grame.elody.editor.expressions;

import grame.elody.lang.TExpMaker;
import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.lang.texpression.expressions.TNamedExp;

public class FunctionTag {
	static final String begMarker = "@";
	static final String sep = ":";
	String name; int count;

	public FunctionTag (String fname, int argsCount) {
		name = fname;
		count = argsCount;
	}
	public String name ()	{ return name; }
	public int argsCount ()	{ return count; }
	public TExp tag (TExp exp) {
    	if (exp != null) {
			String fname = begMarker + name + sep + String.valueOf(count);
			exp = TExpMaker.gExpMaker.createNamed (exp, fname);
		}
		return exp;
	}
	public boolean unTag (TNamedExp f) {
		try {
			String fname = f.getName();
	    	if (taggedFunctionName(fname)) {
	    		String tmp = fname.substring (begMarker.length());
	    		int i = tmp.indexOf(sep);
	    		if (i > 0) {
	    			name = tmp.substring (0, i);
	    			count = Integer.parseInt(tmp.substring (i+1));
	    			return true;
	    		}
	    	}
    	}
    	catch (Exception e) { System.err.println ("FunctionTag unTag : " + e); }
    	return false;
	}

    public String toString() {
    	return "FunctionTag [" + name + "," + count + "] ";
    }

    static public boolean taggedFunctionName (String fname) {
    	return fname.startsWith (begMarker);
    }
    static public FunctionTag untagAppletFunction (TExp exp) {
		if (!(exp instanceof TNamedExp)) return null;
		FunctionTag tag = new FunctionTag (null, 0);
		return tag.unTag ((TNamedExp)exp) ? tag : null;
    }
}
