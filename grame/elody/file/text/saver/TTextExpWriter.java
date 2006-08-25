package grame.elody.file.text.saver;

import grame.elody.file.TNoteConverter;
import grame.elody.lang.texpression.TInput;
import grame.elody.lang.texpression.expressions.TAbstrExp;
import grame.elody.lang.texpression.expressions.TApplExp;
import grame.elody.lang.texpression.expressions.TArrayExp;
import grame.elody.lang.texpression.expressions.TBeginExp;
import grame.elody.lang.texpression.expressions.TEvent;
import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.lang.texpression.expressions.TExpVisitor;
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

import java.io.PrintWriter;
import java.io.Writer;
import java.util.Hashtable;
import java.util.Stack;

/*******************************************************************************************
*
*	 TTextExpWriter (classe) : visiteur d'expression
* 
*******************************************************************************************/

public final class TTextExpWriter extends TExpVisitor {
	static	final Object stateSEQ 			= new Object();
	static	final Object stateMIX 			= new Object();
	static	final Object stateAPPL 			= new Object();
	static	final Object stateEXPAND	 	= new Object();
	static	final Object stateBEGIN 		= new Object();
	static	final Object stateREST 			= new Object();
	static	final Object stateDEFAULT 		= new Object();
	static	final Object stateLETCLAUSE 	= new Object();
	
	Hashtable<TExp, String> identTable;   // table d'association ident : name
	
	Stack<String> stringTable;		// table de noms disponible 
	String curName = "z";   // utilisé pour générer des noms supplémentaires si la stringTable est vide

	StringBuffer buffer;
	PrintWriter out = null;
	TNoteConverter noteConverter;
	
	int octaveOffset = 0; 
	static final int wholeDur = 4000;

		
	public TTextExpWriter (Writer  out) {
		this.out = new PrintWriter(out);
		noteConverter = new TNoteConverter(octaveOffset, wholeDur, wholeDur, false); 
		identTable = new Hashtable<TExp, String>();
		stringTable = new Stack<String>();
		
		// liste de noms d'identifier
		stringTable.push("w");
		stringTable.push("v");
		stringTable.push("u");
		stringTable.push("t");
		stringTable.push("s");
		stringTable.push("r");
		stringTable.push("z");
		stringTable.push("y");
		stringTable.push("x");
	}
	
	String popName() {
		if (stringTable.empty()) {  	// si table de noms disponible vide ==> génère un nom 
			curName = curName + "z";	
			return curName;
		}else {
			return stringTable.pop();  // recupère le prochain nom disponible
		}
	}
	
	void pushName(String name) { stringTable.push(name);} 
	
	public void  writeFileHeader() {
		out.println ("% Generated from Elody");
		out.println ("% Web : http://www.grame.fr/Elody/");
	}
	
	public void  writeTitle(String title) {
	 	out.println ( "% Title: " + title);
	}
	
	public void  writeAuthor(String author) {
	 	out.println ("% Author: " + author);
	}
	
	public void  writeDescription(String dec) {
	 	out.println ("% Description: " + dec);
	 	out.println("");
	}
	// VERIFIER LE PB DU DERNIER RETOUR CHARIOT
	public void  writeFileEnd() {
		out.flush();
	}

	public Object Visite( TExp s ,Object arg) {return null;}
	
	public Object Visite( TEvent s ,Object arg) {
	
		if (s.getType() == TExp.SOUND) {
			out.print(noteConverter.convertNote(s.getPitch(),  s.getDur()));
			if ( s.getVel() > 100) {
				out.print (">" + (int) (s.getVel() - 100));
			}else if ( s.getVel() < 100){
				out.print ("<" + (int)(100 -  s.getVel()));
			}
		}else{
		
			//System.out.println("Silence" + noteConverter.convertSilence(s.getDur()));
			out.print(noteConverter.convertSilence(s.getDur()));
		}
		return null;
	}
	
	public Object Visite( TApplExp s ,Object arg){
			if (arg == stateAPPL){
			out.print ("(");
			s.getArg1().Accept(this,stateDEFAULT);
			out.print (" ");
			s.getArg2().Accept(this,stateAPPL);
			out.print (")");
		}else {
			s.getArg1().Accept(this,stateDEFAULT);
			out.print (" ");
			s.getArg2().Accept(this,stateAPPL);
		}
 		return null;
	}
		
	public Object Visite( TAbstrExp s ,Object arg){
	
		// Associe l'ident avec le prochain nom disponible dans le Hastable
		String name = popName();
		TExp ident = s.getArg1();
		identTable.put(ident, name);
	
		out.print ("\\");
		out.print (name + ":");
		s.getArg1().getArg1().Accept(this,stateDEFAULT);
		out.print ("=>");
		
		
		if (needParenthesis (s)) {
		//if (s.getArg2().getPriority() > s.getPriority()) {
			out.print ("(");
			s.getArg2().Accept(this,stateDEFAULT);
			out.print (")");
		}else {
			s.getArg2().Accept(this,stateDEFAULT);
		}
		
		// Restore la Hastable
		identTable.remove(ident); // efface l'association ident : nom
		pushName(name);  		  // remet le nom 
		
 		return null;
	}
	
	public Object Visite( TYAbstrExp s ,Object arg){
		String name = popName();
		TExp ident = s.getArg1();
		identTable.put(ident, name);
		
		out.print ("Y");
		out.print (name + ":");
		s.getArg1().getArg1().Accept(this,stateDEFAULT);
		out.print ("=>");
		
		if (needParenthesis (s)) {
		//if (s.getArg2().getPriority() > s.getPriority()) {
			out.print ("(");
			s.getArg2().Accept(this,stateDEFAULT);
			out.print (")");
		}else {
			s.getArg2().Accept(this,stateDEFAULT);
		}
		
		identTable.remove(ident); 
		pushName(name);  		  
		
 		return null;
	}
	
	public Object Visite( TNamedExp s ,Object arg){
		//s.getArg1().Accept(this,stateDEFAULT);
		out.print (s.getName());
		return null;
	}
	
	public Object Visite( TSequenceExp s ,Object arg){
		if (arg == stateSEQ){
			s.getArg1().Accept(this,stateDEFAULT);
			out.print (",");
			s.getArg2().Accept(this,stateSEQ);
		}else {
			out.print ("[");
			s.getArg1().Accept(this,stateDEFAULT);
			out.print (",");
			s.getArg2().Accept(this,stateSEQ);
 			out.print ("]");
 		}
 		return null;
	
	}
	public Object Visite( TMixExp s ,Object arg){
		if (arg == stateMIX){
			s.getArg1().Accept(this,stateDEFAULT);
			out.print (",");
			s.getArg2().Accept(this,stateMIX);
		}else {
			out.print ("{");
			s.getArg1().Accept(this,stateDEFAULT);
			out.print (",");
			s.getArg2().Accept(this,stateMIX);
 			out.print ("}");
 		}
 		return null;
	}
	public Object Visite( TModifyExp s ,Object arg){
	
		if (needParenthesis (s)) {
		//if (s.getArg1().getPriority() > s.getPriority()) {
			out.print ("(");
			s.getArg1().Accept(this,stateDEFAULT);
			out.print (")");
		}else {
			s.getArg1().Accept(this,stateDEFAULT);
		}
		
		switch (s.index) {
			case TExp.PITCH:
				if ( s.value > 0) 
					out.print ("+" + (int)s.value);
				else
					out.print ((int)s.value);
				break;
			case TExp.VEL:
				if ( s.value > 0) 
					out.print (">" + (int)s.value);
				else
					out.print ("<" + (int)(-s.value));
				break;
			case TExp.DURATION:
				out.print ("*" + s.value);
				break;
			case TExp.CHAN:
				out.print ("@" + (int)s.value);
				break;
		}
		return null;
	}
	
		
	public Object Visite( TExpandExp s ,Object arg){
		if (arg == stateEXPAND){
			out.print ("(");
			s.getArg1().Accept(this,stateDEFAULT);
			out.print ("<>");
			s.getArg2().Accept(this,stateEXPAND);
			out.print (")");
		}else {
			s.getArg1().Accept(this,stateDEFAULT);
			out.print ("<>");
			s.getArg2().Accept(this,stateEXPAND);
		}
 		return null;
	}
	
	public Object Visite( TBeginExp s ,Object arg){
		if (arg == stateBEGIN){
			out.print ("(");
			s.getArg1().Accept(this,stateDEFAULT);
			out.print ("<|");
			s.getArg2().Accept(this,stateBEGIN);
			out.print (")");
		}else {
			s.getArg1().Accept(this,stateDEFAULT);
			out.print ("<|");
			s.getArg2().Accept(this,stateBEGIN);
		}
 		return null;
	}
	
	public Object Visite( TRestExp s ,Object arg){
		if (arg == stateREST){
			out.print ("(");
			s.getArg1().Accept(this,stateDEFAULT);
			out.print ("|>");
			s.getArg2().Accept(this,stateREST);
			out.print (")");
		}else {
			s.getArg1().Accept(this,stateDEFAULT);
			out.print ("|>");
			s.getArg2().Accept(this,stateREST);
		}
 		return null;
	}
	
	public Object Visite( TIdent s ,Object arg){
		String name = identTable.get(s);
		if (name == null) System.err.println("Error write Ident in TTextExpWriter");
		out.print (name);
		return null;
	}
	
	
	public Object Visite( TUrlExp s ,Object arg){
		System.out.println("TTextExpWriter " + s);
		return null;
	}
	
	public Object Visite( TNullExp s ,Object arg){
		s.getArg1().Accept(this,stateDEFAULT);
		return null;
	}
	
		
	public Object Visite( TMuteExp s ,Object arg){
		out.print ("<<");
		s.getArg1().Accept(this,stateDEFAULT);
		out.print (">>");
		return null;
	}
	
	
	public Object Visite( TArrayExp s ,Object arg){
		System.out.println("TArrayExp " + s);
		return null;
	}
	
	public Object Visite( TInput s ,Object arg){
		out.print ("Input");
		return null;
	}
	
	
	boolean needParenthesis (TExp exp) {
		return     (exp instanceof TAbstrExp) 
				|| (exp instanceof TYAbstrExp)
				|| (exp instanceof TApplExp)
				|| (exp instanceof TBeginExp)
				|| (exp instanceof TRestExp)
				|| (exp instanceof TExpandExp)
				|| (exp instanceof TMuteExp);
	}
}
