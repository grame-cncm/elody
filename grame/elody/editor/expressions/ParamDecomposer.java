package grame.elody.editor.expressions;

import grame.elody.lang.texpression.expressions.TEvent;
import grame.elody.lang.texpression.expressions.TExp;

public class ParamDecomposer {
	float pitch, vel, chan;
	protected float stretch;
	TExp body;
	
	public ParamDecomposer () {
		pitch = vel = chan = 0;
		stretch = 1;
		body = null;
//		body = exp;
//		getParam (exp);
	}
	public void scan (TExp exp) {
		try {
			int n = 4;
			for (int i=0;  (i < n) && (exp != null); i++) {
				exp = scanPitch (body = exp);
				if (exp != null) {
					exp = scanVel (body = exp);
				}
				if (exp != null) {
					exp = scanStrech (body = exp);
				}
				if (exp != null) {
					exp = scanChan (body = exp);
				}
			}
		}
		catch (Exception e) { System.err.println ("ParamDecomposer : getParam : " + e); }
	}
	/*
	public final float getValArg2 (TExp exp, int sel) {
		TVector val = ((TEvent)exp.getArg2()).val;
		return val.getVal(sel);
	}
	*/
	// modif steph le 21/07/98
	public final float getValArg2 (TExp exp, int sel) {
		TEvent ev = (TEvent)exp.getArg2();
		return ev.getField(sel);
	}
	//________________________________________________________________
	// les fonctions de scan extraient la valeur dont elles ont besoin, elles renvoient :
	// el'expression telle quelle si la valeur ne peut etre extraite
	// null si la valeur a déjà été extraite (fin de décomposition)
	// et sinon la partie de l'expression qui ne contient pas la valeur 
	public TExp scanPitch (TExp exp) {
		TExp converted = exp.convertModifyExp();
		if (converted != null) {
			float val = getValArg2 (converted, TEvent.PITCH);
			if (val != 0) {
				if (pitch != 0) return null;
				pitch = val;
				exp = converted.getArg1();
			}
		}
		return exp;
	}
	public TExp scanVel (TExp exp) {
		TExp converted = exp.convertModifyExp();
		if (converted != null) {
			float val = getValArg2 (converted, TEvent.VEL);
			if (val != 0) {
				if (vel != 0) return null;
				vel = val;
				exp = converted.getArg1();
			}
		}
		return exp;
	}
	public TExp scanStrech (TExp exp) {
		TExp converted = exp.convertModifyExp();
		if (converted != null) {
			float val = getValArg2 (converted, TEvent.DURATION);
			if (val != 1) {
				if (stretch != 1) return null;
				stretch = val;
				exp = converted.getArg1();
			}
		}
		return exp;
	}
	public TExp scanChan (TExp exp) {
		TExp converted = exp.convertModifyExp();
		if (converted != null) {
			float val = getValArg2 (converted, TEvent.CHAN);
			if (val != 0) {
				if (chan != 0) return null;
				chan = val;
				exp = converted.getArg1();
			}
		}
		return exp;
	}
	public float getPitch () 	{ return pitch; }
	public float getVel () 		{ return vel; }
	public float getStrech () 	{ return stretch; }
	public float getChan () 	{ return chan; }
	public TExp  getExpr () 	{ return body; }

    public String toString() {
    	return "ParamDecomposer [" + pitch + "," + vel + "," + stretch + "," + chan + "] " + body;
    }
}
