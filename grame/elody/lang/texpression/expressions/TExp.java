package grame.elody.lang.texpression.expressions;

import grame.elody.lang.TEvaluator;
import grame.elody.lang.texpression.TEnv;
import grame.elody.lang.texpression.suspensions.TEvalSusp;
import grame.elody.lang.texpression.valeurs.TValue;
import grame.elody.util.Debug;

import java.io.Serializable;
import java.util.Hashtable;


/***********************************/
//Expressions
/***********************************/

public abstract class TExp implements Serializable {
	public static final int TYPE  		= 0;
	public static final int PITCH  		= 1;
	public static final int VEL  		= 2;
	public static final int CHAN  		= 3;
	public static final int DURATION  	= 4;
	public static final int DELAY  		= 5;
	public static final int EXPAND  	= 6;
	
	public static final int SOUND  	 = 0;
	public static final int SILENCE  = 1;
	

	public static final int MODF  = 0;
	public static final int TRSP  = 1;
	public static final int ATTN  = 2;
	public static final int INST  = 3;
	public static final int XPND  = 4;
	
	protected static Hashtable absTable = new Hashtable(50); 	// hashtable por l'abstraction
	
	protected transient int hashcode = 0;						// hashcode utilisé dans les tables de hashage
   	
  	public void setCache(TValue value){}			// Accès au cache interne d'une expression
  	public TValue getCache(){return null;}
  	
   	public static TEnv globalEnv  = new TEnv();		// environnement null pour les évaluation dans le contexte global
	
 
 	public abstract TValue 	Eval(TEnv env);			// La méthode d'évaluation
 	
 	public TValue 	Suspend( TEnv env){ return new TEvalSusp(this,env);}
 	
 	
	public   TExp 	getArg1() {return TNullExp.instance ;}	// Accès aux champs
	public   TExp 	getArg2() {return TNullExp.instance ;}
	
	final public boolean isLeaf(){ return (this instanceof TEvent) || (this instanceof TNullExp);}
	
	
	public Object 	Accept(TExpVisitor v, Object arg) {return v.Visite(this, arg);}
	
	public  TExp 	Format(float val) {return this;}
		
	/*******************************************************************************************
	unNameExp:
 		- supprime tous les noms et Url des sous-expressions nommées de l'arbre syntaxique de 
 		l'expression courante
 		- doit etre redifini pour les feuilles, fonctionne por les constructure a 1 ou 2 arguements
 	/*******************************************************************************************/

 	
 	public TExp unNameExp()
 	{
 		return createNew(getArg1().unNameExp(),getArg2().unNameExp());
 	} 
 	
	/*******************************************************************************************
	// e: expression à abstraire dans 'this'
 	/*******************************************************************************************/

	/*
 	public TExp Abstract(TExp e) { 
 		
 		absTable.clear();
 		TIdent id = new TIdent (e);
 		
 		TExp aux1 = e.unNameExp();
 		TExp aux2 = this.unNameExp();
 		
 			 
 		try {
 			float dur = TEvaluator.gEvaluator.Duration(e) /*+ TEvaluator.gEvaluator.Duration(this);
 			TExp res =  new TAbstrExp (dur, id, aux2.Replace(aux1,id));  // denommage du pattern
 			absTable.clear();
 			return res;
 		}catch (Exception ex) {
 			absTable.clear();
 			return null;
 		}
 	}
 	*/
 	
 	
 	
 	
 	public TExp Abstract(TExp e) { 
 		
 		absTable.clear();
 		TIdent id = new TIdent (e);
 		
 		try {
 			float dur = TEvaluator.gEvaluator.Duration(e) /* TEvaluator.gEvaluator.Duration(this)*/;
 			TExp res =  new TAbstrExp (dur, id, this.Replace(e,id)); 
 			absTable.clear();
 			return res;
 		}catch (Exception ex) {
 			absTable.clear();
 			return null;
 		}
 	}
 	
 	public TExp YAbstract(TExp e) { 
 		
 		absTable.clear();
 		TIdent id = new TIdent (e);
 		
 		TExp aux1 = e.unNameExp();
 		TExp aux2 = this.unNameExp();
 		
 		try {
 			float dur = TEvaluator.gEvaluator.Duration(e) /*+ TEvaluator.gEvaluator.Duration(this)*/;
 			TExp res =  new TYAbstrExp (dur, id, aux2.Replace(aux1,id));  // denommage du pattern 
 			absTable.clear();
 			return res;
 		}catch (Exception ex) {
 			absTable.clear();
 			return null;
 		}
 	}
 	
 	
 	public TExp RebuildBody(TExp e,TExp id) { 
 		absTable.clear();
 	 	return Rebuild(e, (TIdent)id);
 	}
 	
 	
 	/*******************************************************************************************
 	// REBUILD CAS GENERAL 
 	// ATTENTION cas particulier pour TEvent et TIdent => la mth Rebuild est
 	// redéfinies dans ces deux classes
  	/*******************************************************************************************/
  	
 	public TExp Rebuild(TExp e, TIdent id) { 
 		Debug.Trace( "Rebuild", this);
 		TExp res;
 		if ((res = (TExp)absTable.get(this)) != null) {
 			return res;
 		}else{
 			return  makeNewTree1 (e,id);
 		}
 	}
 	
 	/*******************************************************************************************/
 	// REPLACE CAS GENERAL 
 	// ATTENTION cas particulier pour TEvent et TIdent => la mth Replace est
 	// redéfinies dans ces deux classes
 	/*******************************************************************************************/	
 	
	public TExp Replace(TExp e, TIdent id) {  
 		Debug.Trace( "Replace", this);
 		TExp res;
 		if (this.equals(e)) {
 			return id;
 		}else if ((res = (TExp)absTable.get(this)) != null) {
 			return res;
 		}else{
 			return makeNewTree (e,id);
 		}
 	}
  	 		
	TExp makeNewTree  (TExp e, TIdent id) { 
		TExp a1 = getArg1().Replace(e,id);
		TExp a2 = getArg2().Replace(e,id);
		TExp res;
		
		boolean b1 = getArg1().equals(a1);
		boolean b2 = getArg2().equals(a2);
			
		if (b1) {
			res = (b2) ? this : createNew(getArg1(),a2);
		}else {
			res = (b2) ? createNew(a1, getArg2()) : createNew(a1,a2);
		}
		absTable.put(this,res);
		return res;
 	}
 	
 	TExp makeNewTree1  (TExp e, TIdent id) { 
		TExp a1 = getArg1().Rebuild(e,id);
		TExp a2 = getArg2().Rebuild(e,id);
		TExp res;
		
		boolean b1 = getArg1().equals(a1);
		boolean b2 = getArg2().equals(a2);
			
		if (b1) {
			res = (b2) ? this :  createNew(getArg1(),a2);
		}else {
			res = (b2) ? createNew(a1, getArg2()) : createNew(a1,a2);
		}
 		absTable.put(this,res);
 		return res;
 	}

  	TExp  createNew(TExp a1,TExp a2){return TNullExp.instance;}
  	
  	public TExp convertMixExp () {return null;}
  	public TExp convertSeqExp () {return  null;} 	
  	public TExp convertApplExp () {return  null;}
  	public TExp convertAbstrExp () {return  null;}
  	public TExp convertYAbstrExp () {return  null;}
  	public TExp convertBeginExp () {return null;}
  	public TExp convertRestExp () {return  null;} 	
  	public TExp convertDilateExp () {return  null;} 	
  	public TExp convertModifyExp () {return  null;} 	
}
