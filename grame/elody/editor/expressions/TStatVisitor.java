package grame.elody.editor.expressions;

import grame.elody.lang.texpression.expressions.TEvent;
import grame.elody.lang.texpression.valeurs.TClosure;
import grame.elody.lang.texpression.valeurs.TSequenceVal;

public class TStatVisitor extends TGraphVisitor {
	public void Visite (TEvent val, int date, Object arg) {
	}
	public void Visite (TSequenceVal val, int date, Object arg) {
		val.getValArg1().Accept(this, date, arg);
		date += (int) val.getValArg1().Duration();
		val.getValArg2().Accept(this, date, arg);
	}
	public void Visite (TClosure val, int date, Object arg) {
	}
    public String toString() 		{ return "TStatVisitor"; }
}
