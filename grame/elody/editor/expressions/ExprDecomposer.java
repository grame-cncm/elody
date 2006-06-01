package grame.elody.editor.expressions;

import grame.elody.editor.misc.Define;
import grame.elody.editor.misc.appletframe.AppletFrame;
import grame.elody.lang.texpression.expressions.TExp;

public class ExprDecomposer {
	TExp expr;
	
	public ExprDecomposer (TExp exp) {
		expr = exp;
		try {
			if (!decomposeNamed (exp, 0))
				decomposeBasic (exp);
		}
		catch (Exception e) { System.err.println ("ExprDecomposer : " + e); }
	}
	
 	public boolean launchApplet (String appletName, TExp exp) {
		if (appletName != null) {
			AppletFrame.startApplet ("grame.elody.editor.constructors.".concat(appletName), exp);
		}
		else return false;
		return true;
	}
	public boolean decomposeNamed (TExp exp, int i) {
		if (isAbstr (exp)) {
			FunctionTag tag = FunctionTag.untagAppletFunction (exp);
			if (tag != null) {
				if (tag.argsCount() == i)
					return launchApplet (tag.name(), expr);
			}
		}
		TExp f = exp.convertApplExp();
		if (f != null) {
			return decomposeNamed (f.getArg1(), i+1);
		} 
		return false;
	}
	
	public boolean decomposeBasic (TExp exp) {
		String appletName = null;
		if (isSeq(exp)) 		appletName = Define.SeqApplet;
		else if (isMix(exp))	appletName = Define.MixApplet;
		else if (isAppl(exp))	appletName = Define.ApplyApplet;
		else if (isAbstr(exp))	appletName = Define.AbstractApplet;
		else if (isYAbstr(exp))	appletName = Define.YAbstractApplet;
		else if (isBeg(exp))	appletName = Define.BeginApplet;
		else if (isEnd(exp))	appletName = Define.EndApplet;
		else if (isStretch(exp)) appletName = Define.StretchApplet;
		else if (isModif(exp)) 	appletName = Define.ParamApplet;
		else return false;
		return launchApplet (appletName, exp);
	}

 	public static boolean isMix 	(TExp exp) { return exp.convertMixExp() != null; }
	public static boolean isSeq 	(TExp exp) { return exp.convertSeqExp() != null; }
	public static boolean isAppl 	(TExp exp) { return exp.convertApplExp() != null; }
	public static boolean isAbstr 	(TExp exp) { return exp.convertAbstrExp() != null; }
	public static boolean isYAbstr	(TExp exp) { return exp.convertYAbstrExp() != null; }
	public static boolean isBeg 	(TExp exp) { return exp.convertBeginExp() != null; }
	public static boolean isEnd 	(TExp exp) { return exp.convertRestExp() != null; }
	public static boolean isStretch (TExp exp) { return exp.convertDilateExp() != null; }
	public static boolean isModif 	(TExp exp) { return exp.convertModifyExp() != null; }
}
