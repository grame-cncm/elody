package grame.elody.lang.texpression.expressions;

import grame.elody.lang.texpression.TEnv;
import grame.elody.lang.texpression.suspensions.TEvalSusp;
import grame.elody.lang.texpression.valeurs.TClosure;
import grame.elody.lang.texpression.valeurs.TValue;
import grame.elody.util.Debug;

public final class TYAbstrExp extends TExp {
  	public float val;
 	public TExp ident;
 	public TExp body;
 	transient TValue cache = null;
 	
 	
 	public TYAbstrExp (float val, TExp ident, TExp body) { 
 		this.val = val; this.ident = ident; this.body = body;
 	}
 	/*
 	 public TValue 	Eval (TEnv env) {
 		return new TClosure(val, ident, body, env).Apply(Suspend(env));
 	}
 	*/
 	
 	
 	 public TValue 	Eval (TEnv env) {
 		return (cache != null) ? cache : new TClosure(val, ident, body, env).Apply(Suspend(env));
 	}
 	
 	/*
 	public TValue 	Eval (TEnv env) { 
 		if (closed) {
 			//System.out.println("CacheYAbstr");
 			if (cache ==  null) cache =  new TClosure(val, ident, body, env).Apply(Suspend(env));
 			return  cache;
 		}else {
 			return new TClosure(val, ident, body, env).Apply(Suspend(env));
 		}
 	}
 	*/
 	
  	public TValue 	Suspend(TEnv env){ 
 		return new TEvalSusp (this,env);
 	}
 	
 	
 	public boolean equals(Object obj) {
		return (this == obj) ||  ((obj instanceof TYAbstrExp) 
			&& val ==((TYAbstrExp)obj).val
			&& ident.equals(((TYAbstrExp)obj).ident)
			&& body.equals(((TYAbstrExp)obj).body));
	}
 	
	public int hashCode() {
		if (hashcode == 0)  hashcode = ident.hashCode()+ body.hashCode();
		return  hashcode;
	}
	
	TExp  createNew(TExp a1,TExp a2) {return new TYAbstrExp(val,ident,a2);}
 	public Object 	Accept(TExpVisitor v, Object arg) {return v.Visite(this, arg);}
	
	public  TExp getArg1() {return ident;}
	public  TExp getArg2() {return body;}
	
	public TExp convertYAbstrExp () {return  this;}
	
	public void setCache(TValue value){ cache = value;}
	public TValue getCache(){return cache;}
	
	public TExp Format(float val) {return new TYAbstrExp(val,ident,body);}
	
	/*
	 Rebuild ( \x:arg.body , id) 
	 	==> \x:arg . body si x == id 
	 	==> \x:arg .Rebuild (body, id)  si x != id
	
	 une abstraction avec un meme ident (au sens pointeur) "cache" l'ident ==>
	 on de descent pas dans le corps
	 
	*/

	  	
  	public TExp Rebuild(TExp e , TIdent id) { 
 		Debug.Trace( "Rebuild", this);
 		return ((ident == id) ? this : super.Rebuild(e,id)); 
  	}
}
