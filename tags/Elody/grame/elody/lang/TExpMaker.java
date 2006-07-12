package grame.elody.lang;

import grame.elody.lang.texpression.TInput;
import grame.elody.lang.texpression.expressions.TAbstrExp;
import grame.elody.lang.texpression.expressions.TApplExp;
import grame.elody.lang.texpression.expressions.TArrayExp;
import grame.elody.lang.texpression.expressions.TBeginExp;
import grame.elody.lang.texpression.expressions.TEvent;
import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.lang.texpression.expressions.TExpandExp;
import grame.elody.lang.texpression.expressions.TIdent;
import grame.elody.lang.texpression.expressions.TMixExp;
import grame.elody.lang.texpression.expressions.TModifyExp;
import grame.elody.lang.texpression.expressions.TMuteExp;
import grame.elody.lang.texpression.expressions.TNamedExp;
import grame.elody.lang.texpression.expressions.TNullExp;
import grame.elody.lang.texpression.expressions.TRestExp;
import grame.elody.lang.texpression.expressions.TSequenceExp;
import grame.elody.lang.texpression.expressions.TUrlExp;
import grame.elody.lang.texpression.expressions.TYAbstrExp;
import grame.elody.lang.texpression.operateurs.TAdd;
import grame.elody.lang.texpression.operateurs.TMult;
import grame.elody.lang.texpression.operateurs.TSet;
import grame.elody.lang.texpression.valeurs.TValue;
import grame.elody.util.MsgNotifier;

import java.awt.Color;
import java.util.Observer;
import java.util.Vector;


/*******************************************************************************************
*
*	 TExpMaker (classe) : fabrication d'expressions abstraite
*
*    - a un champ static gExpMaker qui contient le fabricateur d'expression concrèt
* 
*    - pour changer de fabricateur d'expression, il faut redéfinir un fabricateur concrèt
*      (une classe qui derive de TExpMaker) et changer le contenu du champ gExpMaker
*	   (par exemple:  public static TExpMaker gExpMaker = new TExpMaker2(); )
*
*******************************************************************************************/

public abstract class TExpMaker {
	 public static TExpMaker gExpMaker = new TExpMaker1();   // Fabricateur d'expressions re-définissable

	 public abstract TExp createNote (Color color, int pitch, int vel, int chan, int dur);
	 public abstract TExp createSilence ( Color color, float pitch, float vel, float chan, float dur);
	 public abstract TExp createInput ();

	 public abstract TExp createSeq (TExp arg1, TExp arg2);
	 public abstract TExp createMix (TExp arg1, TExp arg2);
	 public abstract TExp createDilate(TExp arg1, TExp arg2);
	 public abstract TExp createNamed(TExp arg1, String name);
	
	 public abstract TExp createAppl (TExp arg1, TExp arg2);
	 public abstract TExp createAbstr (TExp arg1, TExp arg2);
	 public abstract TExp createYAbstr (TExp arg1, TExp arg2);
	 
	 public abstract TExp createLambda (float val,TExp ident, TExp arg, TExp body);
	 public abstract TExp createYLambda (float val,TExp id, TExp arg, TExp body);
	 public abstract TExp createLambda (TExp ident, TExp arg, TExp body);
	 public abstract TExp createYLambda (TExp id, TExp arg, TExp body);

	 public abstract TExp createTrsp (TExp arg1, float value);
	 public abstract TExp createTrsp (TExp arg1, TExp arg2);
	  
	 public abstract TExp createAttn (TExp arg1, float value);
	 public abstract TExp createExpv (TExp arg1, float value);
	 
	 public abstract TExp createTrsch (TExp arg1, float value);
	 public abstract TExp createSetch (TExp arg1, float value);
	
	 public abstract TExp createBegin (TExp arg1, TExp arg2);
	 public abstract TExp createRest(TExp arg1, TExp arg2);
	
	 public abstract TExp createUrl (TExp exp, String url);
	 public abstract TExp createIdent (TExp exp);
	 public abstract TExp createNull ();
	 public abstract TExp createBody( TExp id, TExp body);
	 public abstract TExp createMute (TExp exp);
	 public abstract TExp createArray (Vector exp);
	
	 public abstract TValue expandVal (TValue val, float coef);
	 
	 public abstract void updateHistory(TExp exp);
	 
	 public abstract void addObserver (Observer o);
     public abstract void deleteObserver (Observer o);
}

/*******************************************************************************************
*
*	 TExpMaker1 (classe) : fabrication d'expressions concrète
*
*******************************************************************************************/

final class TExpMaker1 extends TExpMaker{

	MsgNotifier  notifier;
	TExp previousExp;
	
	public   void updateHistory(TExp exp) {
		if (!(previousExp.equals(exp))) notifier.notifyObservers(exp);
		previousExp = exp;
	}
	
	public TExpMaker1 () {
		notifier = new MsgNotifier (2000);
		previousExp = createNull();
	}
		
	boolean checkNullArg(TExp arg1, TExp arg2) {return (arg1 instanceof TNullExp || arg2 instanceof TNullExp);}
	boolean checkNull(TExp arg1) {return (arg1 instanceof TNullExp);}
	
	public void addObserver	  	(Observer o) { notifier.addObserver(o); }
    public void deleteObserver	(Observer o) { notifier.deleteObserver(o); }
   	
   	
   	 public  TExp createNote (Color color, int pitch, int vel, int chan, int dur){
   	 	return new TEvent(TExp.SOUND,color,pitch,vel,chan,dur);
   	 }
   	 
   	 public  TExp createInput ( ){ return new TInput(); }
   	 
	 public  TExp createSilence ( Color color, float pitch, float vel, float chan, float dur){
   	 	return new TEvent(TExp.SILENCE,color,pitch,vel,chan,dur);
   	 }	
   	
		
	public   TExp createNamed ( TExp arg1, String str){
		if (checkNull(arg1))
			return  arg1;
		else if (arg1 instanceof TNamedExp) { 	// si deja nommée
			TNamedExp exp = (TNamedExp) arg1;
			return 	(exp.getName().equals(str)) ? arg1 : new TNamedExp(arg1.getArg1(),str);
		}else {
			return  new TNamedExp(arg1,str);
		}
	}
	
		
	public   TExp createSeq (TExp arg1, TExp arg2) {
		if (checkNull(arg1)){
			return  arg2;
		}else if (checkNull(arg2)){
			return arg1;
		}else {
			return new TSequenceExp(arg1,arg2); 
		}
	}
	
	 public   TExp createMix (TExp arg1, TExp arg2){
		if (checkNull(arg1)){
			return  arg2;
		}else if (checkNull(arg2)){
			return arg1;
		}else {
			return new TMixExp(arg1,arg2); 
		}
	}
		
	 public   TExp createDilate (TExp arg1, TExp arg2){
		if (checkNull(arg1)){
			return  arg2;
		}else if (checkNull(arg2)){
			return arg1;
		}else {
			return new TExpandExp(arg1,arg2); 
		}
	}
	
	public TExp createAppl (TExp arg1,TExp arg2){
		if (checkNull(arg1)){
			return  arg2;
		}else if (checkNull(arg2)){
			return arg1;
		}else {
			return new TApplExp(arg1,arg2); 
		}
	}
	
	// on abstrait arg1 dans arg2
	public TExp createAbstr (TExp arg1,TExp arg2){
		return (checkNullArg(arg1,arg2) ? createNull () : arg2.Abstract(arg1));  
	}
	
	
	// on abstrait arg1 dans arg2
	public TExp createYAbstr (TExp arg1, TExp arg2){
	 	return (checkNullArg(arg1,arg2) ? createNull () : arg2.YAbstract(arg1));  
	}
	
	public TExp createLambda (float val, TExp id, TExp arg, TExp body){
		TIdent ident = (TIdent)id;
		 ident.arg = arg;
		 return new TAbstrExp(val, ident, body);  
	}
	
	// Calcule la durée
	public TExp createLambda (TExp id, TExp arg, TExp body){
		TIdent ident = (TIdent)id;
		 ident.arg = arg;
		 float val = TEvaluator.gEvaluator.Duration(arg) ;
		 //	+  body.Eval(TExp.globalEnv.Bind(ident, arg.Suspend(TExp.globalEnv))).Duration();
		 return new TAbstrExp(val, ident, body);  
	}
	
	public TExp createYLambda (float val,TExp id, TExp arg, TExp body){
		TIdent ident = (TIdent)id;
		 ident.arg = arg;
		 return new TYAbstrExp(val, ident, body);  
	}
	
	// Calcule la durée
	public TExp createYLambda (TExp id, TExp arg, TExp body){
		TIdent ident = (TIdent)id;
		 ident.arg = arg;
		 float val = TEvaluator.gEvaluator.Duration(arg) ;
		 //	+  body.Eval(TExp.globalEnv.Bind(ident,arg.Suspend(TExp.globalEnv))).Duration();
		 return new TYAbstrExp(val, ident, body);  
	}
	
	public TExp createTrsp (TExp arg1, float value) {
		if (arg1 instanceof TEvent) {
			TEvent ev = (TEvent)arg1;
			return ev.setPitch(ev.getPitch() + value);
		}else {
			return checkNull(arg1) ? arg1 :  new TModifyExp(arg1,TExp.TRSP,value ,TAdd.op);
		}
	}
	
	public TExp createTrsp (TExp arg1, TExp arg2) {
		float value = ((TEvent)arg2).getPitch();
		if (arg1 instanceof TEvent) {
			TEvent ev1 = (TEvent)arg1;
			return ev1.setPitch(ev1.getPitch() + value);
		}else {
			return checkNull(arg1) ? arg1 :  new TModifyExp(arg1,TExp.TRSP,value ,TAdd.op);
		}
	}
	
	public TExp createAttn (TExp arg1, float value){
		if (arg1 instanceof TEvent) {
			TEvent ev = (TEvent)arg1;
			return ev.setVel(ev.getVel() + value);
		}else {
			return checkNull(arg1) ? arg1 : new TModifyExp(arg1,TExp.ATTN,value ,TAdd.op);
		}
	}
	
	public TExp createExpv  (TExp arg1, float value) {
		if (arg1 instanceof TEvent) {
			TEvent ev = (TEvent)arg1;
			return ev.setDur(ev.getDur() * value);
		}else {
			return checkNull(arg1) ? arg1 :  new TModifyExp(arg1,TExp.XPND,value,TMult.op);
		}
	}
	
	public TExp createTrsch  (TExp arg1, float value) {
		if (arg1 instanceof TEvent) {
			TEvent ev = (TEvent)arg1;
			return ev.setChan(ev.getChan() + value);
		}else {
			return checkNull(arg1) ? arg1 :  new TModifyExp(arg1,TExp.INST,value,TAdd.op);
		}
	}
	
	public TExp createSetch  (TExp arg1, float value) {
		if (arg1 instanceof TEvent) {
			TEvent ev = (TEvent)arg1;
			return ev.setChan(value);
		}else {
			return checkNull(arg1) ? arg1 :  new TModifyExp(arg1,TExp.INST,value,TSet.op);
		}
	}
			
	public TExp createBegin  (TExp arg1, TExp arg2) {
		if (checkNull(arg1)){
			return  arg2;
		}else if (checkNull(arg2)){
			return arg1;
		}else {
			return new TBeginExp(arg1,arg2); 
		}
	}
	
	public TExp createRest  (TExp arg1, TExp arg2) {
		if (checkNull(arg1)){
			return  arg2;
		}else if (checkNull(arg2)){
			return arg1;
		}else {
			return new TRestExp(arg1,arg2); 
		}
	}
	
	public TExp createUrl(TExp arg1,String url) {
		return checkNull(arg1) ? arg1 : new TUrlExp(arg1, url);
	}
	
	public TExp createMute(TExp arg1) {
		return checkNull(arg1) ? arg1 : new TMuteExp(arg1);
	}
	
	public TExp createArray(Vector arg1) {
		return  new TArrayExp(arg1);
	}
	
	public TExp createIdent (TExp val) {return new TIdent(val);}
	
	public TExp createNull () {
		return TNullExp.instance;
	}
	
	public TExp createBody( TExp id, TExp body) { 
		TIdent ident = (TIdent)id;
		return (checkNullArg(body,id) ? createNull () :  body.RebuildBody(ident.arg,ident));
	}
	
	public TValue expandVal (TValue val, float coef) {
		return val.Modify(TExp.XPND, coef, TMult.op);
	}	
}