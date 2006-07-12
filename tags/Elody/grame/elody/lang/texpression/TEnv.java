package grame.elody.lang.texpression;

import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.lang.texpression.valeurs.TValue;

public final class TEnv {
	TExp ident;
 	TValue susp;
 	TEnv next;
 	
 	public TEnv() {}
 	public TEnv(TExp ident , TValue susp, TEnv next) 
 	{this.ident = ident; this.susp = susp; this.next =  next;} 
 	
 	public TEnv Bind(TExp ident, TValue susp) {
 		return new TEnv(ident, susp, this);
 	}
 	
 	public  TExp getIdent() {return ident;}
	public  TValue getSusp() {return susp;}
	public  TEnv getNext() {return next;}
	public void setIdent(TExp ident) { this.ident = ident; }
}
