package grame.elody.file.html.saver;

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
import grame.elody.lang.texpression.operateurs.TAdd;
import grame.elody.util.AwtUtils;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Hashtable;


/*******************************************************************************************
SAUVEGARDE: en 3 étapes:

¥ 1¡ étape : détection des sous-arbre partagés, construction d'une hashtable qui contient un nombre 
de référence pour chaque noeud de l'arbre.
firstRefExp : gére le compteur de référence, met a jour le flag de LETCLAUSE

¥ 2¡ étape : sauvegarde des LETCLAUSE si nécessaire dans un ordre particulier (pour la restitution)

¥ 3¡ étape : sauvegarde de l'expression

Remarque: des abstractions contenant les mêmes identifiers (c'est à dire syntaxiquement 
egaux) auront des identifiers textuels différents, ce qui évite les problemes de conflits 
de nom lors la restauration.

*******************************************************************************************/


/*******************************************************************************************
*
*	 TExpSaver (classe) : sauvegarde des expressions au format HTML (interne)
* 
*******************************************************************************************/

public final class TExpSaver {
	Hashtable expTable;
	Hashtable identTable;
	TExpWriter streamwriter;
	THashtableWriter tablewriter;
	TExpChecker  expchecker;
	
	public TExpSaver (OutputStream out) { 
		expTable = new Hashtable();
		identTable = new Hashtable();
		tablewriter = new THashtableWriter(expTable,identTable);
		expchecker  = new TExpChecker(expTable);
		streamwriter = new TExpWriter (out, expTable,identTable);
	}
	
	/*
	rend la prochaine LETCLAUSE qui peut être sauvée
	*/
	
	TExp getLetClause () {
		TExp key;
		
		for (Enumeration e = expTable.keys(); e.hasMoreElements();) {
			key = (TExp)e.nextElement();
	      	if (expchecker.checkExp(key)) {return key;}
	    } 
	    return null;
	}
	
	void  clearTable () {
		TExp key;
		TRefExp ref;
		
		for (Enumeration e = expTable.keys(); e.hasMoreElements();) {
			key = (TExp)e.nextElement();
			ref = (TRefExp)expTable.get(key);
			if (ref.count == 1) {expTable.remove (key);}
			 
		}
	}
	
	 void initSaver () {
		expTable.clear(); 
		identTable.clear(); 
		tablewriter.init();
		TRefExp.init();
	}
	
	public void writeFileHeader() {
		streamwriter.writeFileHeader();
	}
   	
   	public void writeFileEnd() {
		streamwriter.writeFileEnd();
	}
	
	public void writePlayerApplet() {
		streamwriter.writePlayerApplet() ;
	}
	
	public void writeTitle(String str) {
		streamwriter.writeTitle(str) ;
	}
	
	public void writeAuthor(String str) {
		streamwriter.writeAuthor(str) ;
	}
	
	public void writeDescription(String str) {
		streamwriter.writeDescription(str) ;
	}
	
	void writeNullExp(TExp exp) {
		streamwriter.writeExpHeader();
		streamwriter.writeBodyHeader();
		exp.Accept(streamwriter, TWriteState.DEFAULT );  // 2¡ passe : ecriture du stream	
		streamwriter.writeBodyEnd();
		streamwriter.writeExpEnd();
	}
	
	public void writeExp(TExp exp) {
		initSaver ();
	
		if (exp instanceof TNullExp) {  // gestion de la null exp
			writeNullExp(exp);
		}else{
			TRefExp ref;
			TExp key;
				
			streamwriter.writeExpHeader();
			
			exp.Accept(tablewriter,TWriteState.DEFAULT); // construction de la hashtable d'expressions
						
			if (tablewriter.isLetClause()) {
				streamwriter.writeLetHeader();
				
			    clearTable (); // effacement de tous les éléments référencés une seule fois
			    
			   /* sauvegarde les clauses dans un ORDRE particulier :
			    une clause dépend seulement des clauses déja sauvées (pour le parser)
			   */
			    
				while ((key = getLetClause ()) != null) { 
					ref = (TRefExp)expTable.get(key);
					streamwriter.writeLetClause(ref, key);
					ref.saved = true;
				}
				streamwriter.writeLetEnd();
		   	}
	       	
	       	streamwriter.writeBodyHeader();
	       	exp.Accept(streamwriter, TWriteState.DEFAULT );  // 2¡ passe : ecriture du stream
	        streamwriter.writeBodyEnd();
	        streamwriter.writeExpEnd();
       }
    }
}

/*******************************************************************************************
Teste si un arbre peut etre sauvé: un arbre peut etre sauvé si tous ses sous-arbres
partagés sont deja sauvés.
*******************************************************************************************/

final class TExpChecker{
	
	Hashtable table;
	
	public TExpChecker (Hashtable table) { this.table = table; TNullExp.defaultRes = Boolean.TRUE; }
	
	boolean  checkNode (TExp exp) {
		TRefExp ref  = (TRefExp)table.get(exp);
		return ((ref == null) 
				|| (ref.count == 1)
				|| ((ref.count > 1) && ref.saved));
	}
	
	public boolean checkExp(TExp s) {
		TRefExp ref = (TRefExp)table.get(s);
		return (ref.count > 1) && (!ref.saved) && checkArg(s.getArg1()) && checkArg(s.getArg2());
	}
		
	public boolean checkArg( TExp s){
		return s.isLeaf () || (checkNode (s) && checkArg(s.getArg1()) && checkArg(s.getArg2()));
	}
}


/*******************************************************************************************
*
*	 TRefExp (classe) : compteur de référence
* 
*******************************************************************************************/

final  class TRefExp {
 	static int numcount= 0;
	int count = 1;
	int num;
	TExp id;
	boolean saved = false;
	
	TRefExp (TExp exp) { id =  exp; num = numcount; numcount++; } 
	void incRef () {count++;}
	
	static void init () {numcount = 0;}
}


/*******************************************************************************************
*
*	 THashtableWriter (classe) : visiteur d'expressions, détecte les sous arbres partagé et 
*                                rempli des hastables								
* 
*******************************************************************************************/


final class THashtableWriter extends TExpVisitor {
	Hashtable expTable;
	Hashtable identTable;
	boolean letClause = false;
	
	public THashtableWriter (Hashtable exptable,Hashtable identtable) { 
		this.expTable = exptable;
		this.identTable = identtable;
	}
	
	void init() {letClause = false;}
	
	// si un arbre est partagé, pas besoin de tester le partage des sous arbres

	boolean  firstRefExp (TExp  exp) {
		TRefExp ref = (TRefExp) expTable.get(exp);
		
		if (ref == null) {
			expTable.put(exp, new TRefExp(exp));
			return true;
		}else {
			ref.incRef();
			letClause = true; // une clause Let sera necessaire
			return false;
		}
	}
	
	boolean  firstRefIdent (TIdent  ident) {
		TRefExp ref = (TRefExp) identTable.get(ident);
		
		if (ref == null) {
			identTable.put(ident, new TRefExp(ident));
			return true;
		}else {
			ref.incRef();
			return false;
		}
	}
	
	boolean isLetClause() { return letClause;}
	
	
	public Object Visite( TApplExp s,Object arg){
		if (firstRefExp (s)) {
			s.getArg1().Accept(this,arg);
			s.getArg2().Accept(this,arg);
		}
		 return null;
	}
	
	
	public Object Visite( TAbstrExp s,Object arg){
		if (firstRefExp (s)) {
			s.getArg1().Accept(this,arg);
			s.getArg1().getArg1().Accept(this,arg); // expression associée à l'ident
			s.getArg2().Accept(this,arg);
		}
		 return null;
	}
	
	public Object Visite( TYAbstrExp s,Object arg){
		if (firstRefExp (s)) {
			s.getArg1().Accept(this,arg);
			s.getArg1().getArg1().Accept(this,arg); // expression associée à l'ident
			s.getArg2().Accept(this,arg);
		}
		 return null;
	}


	public Object Visite( TNamedExp s,Object arg){
		if (firstRefExp (s)) {	
			s.getArg1().Accept(this,arg);
		}
		 return null;
	}
	
	public Object Visite( TSequenceExp s,Object arg){
		TWriteState state = (TWriteState) arg;
		if (arg != TWriteState.SEQ) {
			if (firstRefExp (s)) {
				s.getArg1().Accept(this,TWriteState.DEFAULT);
				s.getArg2().Accept(this,TWriteState.SEQ);
			}
		}else {
			s.getArg1().Accept(this,TWriteState.DEFAULT);  // A REVOIR
			s.getArg2().Accept(this,TWriteState.SEQ);
		}
		 return null;
	}
	
	
	public Object Visite( TMixExp s,Object arg){
		TWriteState state = (TWriteState) arg;
		
		if (arg != TWriteState.MIX)  {
			if (firstRefExp (s)) {
				s.getArg1().Accept(this,TWriteState.DEFAULT);
				s.getArg2().Accept(this,TWriteState.MIX);
			}
		}else {
			s.getArg1().Accept(this,TWriteState.DEFAULT);
			s.getArg2().Accept(this,TWriteState.MIX);
		}
		 return null;
	}
	
	public Object Visite( TModifyExp s,Object arg){
		if (firstRefExp (s)) s.getArg1().Accept(this,arg);
		 return null;
	}
	
	public Object Visite( TExpandExp s,Object arg){
		if (firstRefExp (s)) {
			s.getArg1().Accept(this,arg);
			s.getArg2().Accept(this,arg);
		}
		 return null;
	}
	
	public Object Visite( TRestExp s,Object arg){
		if (firstRefExp (s)) {
			s.getArg1().Accept(this,arg);
			s.getArg2().Accept(this,arg);
		}
		return null;
	}
	
	public Object Visite( TBeginExp s,Object arg){
		if (firstRefExp (s)) {
			s.getArg1().Accept(this,arg);
			s.getArg2().Accept(this,arg);
		}
		return null;
	}
	
	public Object Visite( TIdent s,Object arg){
		firstRefIdent (s);
		return null;
	}

	public Object Visite( TUrlExp s ,Object arg){
		firstRefExp (s);
		return null;
	}
	
		
	public Object Visite( TMuteExp s,Object arg){
		if (firstRefExp (s)) s.getArg1().Accept(this,arg);
		return null;
	}
	
	public Object Visite( TArrayExp s,Object arg){
		if (firstRefExp (s)) {
			for (Enumeration e = s.arg1.elements(); e.hasMoreElements();) {
				TExp res = (TExp)e.nextElement();
				res.Accept(this,arg);
			}	
		}
		return null;
	}
}

/*************************/
// Classe TWriteState 
/*************************/


class TWriteState {
	static	public TWriteState SEQ = new TWriteState();
	static	public TWriteState MIX = new TWriteState();
	static	public TWriteState APPL = new TWriteState();
	static	public TWriteState EXPAND = new TWriteState();
	static	public TWriteState BEGIN = new TWriteState();
	static	public TWriteState REST = new TWriteState();
	static	public TWriteState DEFAULT = new TWriteState();
	static	public TWriteState LETCLAUSE = new TWriteState();
}

/*******************************************************************************************
*
*	 TExpWriter (classe) : visiteur d'expressions, ecrit l'expression dans un stream
* 
*******************************************************************************************/

final class TExpWriter extends TExpVisitor{
	PrintWriter out = null;
	Hashtable expTable = null;
	Hashtable identTable = null;
	String version = "100";
	
	public TExpWriter (OutputStream out) { this.out = new PrintWriter(new BufferedOutputStream(out));}
	
	public TExpWriter (OutputStream out, Hashtable exptable, Hashtable identtable) { 
		this.out = new PrintWriter(out);
		this.expTable = exptable;
		this.identTable = identtable;
	}
	
	boolean  checkNotSaved (TExp exp) {
		TRefExp ref = (TRefExp)expTable.get(exp);
		return (ref == null) || (!ref.saved);
	}
		
    void  writeFileHeader() {
		out.println ("<HTML> <BODY> <H2 ALIGN = CENTER> Elody File " + version + "</H2>");
	}

	
	void  writePlayerApplet() {
		out.println ( "<CENTER> <TABLE BORDER> <TR> <TD> <APPLET  archive= Player.zip");
		out.println	("code=grame.elody.editor.PlayerNetscape.class" );
		out.println	("width=290 height = 100");
		out.println	("<I>You must use a Java enabled browser to see the Applets!</I>");
		out.println	("</APPLET></CENTER></TD></TR></TABLE> </CENTER><BR><BR>");
	}
	
	void  writeTitle(String str) {
		out.println ( "<HR><P><B>Title:</B>" + str + "</P>");
	}
	
	void  writeAuthor(String str) {
		out.println ( "<P><B>Author:</B>" + str + "</P>");
	}
	
	void  writeDescription(String str) {
		out.println ( "<P><B>Description:</B>" + str + "</P>");
	}
	
	void  writeFileEnd() {
		// Pas re retour chariot pour le dernier??
		// out.println ("<HR></BODY></HTML>");
		out.print ("<HR></BODY></HTML>");
		out.flush();
	}
	
	
	void  writeExpHeader() {
		out.println ("<HR><UL>");
	}
	
	void  writeExpEnd() {
		out.println ("</UL>");
	}
	
	
	void  writeLetHeader() {
		out.println ("<LI><B>Let</B><UL><FONT size = 2>");
	}

	 void  writeLetEnd() {
		out.println ("</FONT></UL>");
	}
	 
	 
	void  writeBodyHeader() {
		//out.println ("<LI>");
	}

	void  writeBodyEnd() {
		//out.println ("");
	}

	 
	boolean checkNeedLine(Object arg) { return arg != TWriteState.LETCLAUSE;}
	 
	 
	void  writeId(TExp key,Object arg) {
		//System.out.println ("writeId " + key);
	
	
	 	TRefExp  ref = (TRefExp)expTable.get(key);
	 	
	 	//System.out.println ("ref " + ref);
	 	
	 	//out.println ("<LI> <B> Id" + ref.num + "</B> ");
	 	
	 	if (checkNeedLine(arg)) out.print ("<LI>");
	 	//out.println ("<LI> <B> LetId" + ref.num + "</B> ");
	 	out.println ("<B> LetId" + ref.num + "</B> ");
	 }
	 
	 

	 void  writeLetClause(TRefExp ref, TExp exp) {
	
		if (! (exp instanceof TIdent)) {
			out.println (" <LI> <B> LetId" + ref.num + "</B> ");
			exp.Accept(this,TWriteState.LETCLAUSE);
		}
	}
	
	
	public Object Visite (TEvent s,Object arg) {
	
		if (checkNotSaved(s)) {
			//TVector val = s.val;
			if (checkNeedLine(arg)) out.print ("<LI>");
			
			if (s.getType() == TExp.SOUND) {
				out.print ("<FONT color = #" + AwtUtils.toHexString(s.getColor().getRGB()) + ">"
					+ "<B> Event </B> <I>[");
			}else {
				out.print ("<FONT color = #" + AwtUtils.toHexString(s.getColor().getRGB()) + ">"
					+ "<B> Silence </B> <I>[");
			}
			out.print ((int)s.getColor().getRGB() + "," + (int)s.getPitch() + 
					"," + (int)s.getVel() + ","
					+ (int)s.getChan() + "," + (int)s.getDur());
			out.println ("]</I>  </FONT>");
		}else 
			writeId(s,arg);
			
		return null;
	}
	
	public  Object Visite (TIdent s,Object arg) {
		TRefExp  ref = (TRefExp)identTable.get(s);
	 	out.println ("<LI> <B> Id" + ref.num + "</B> ");
		return null;
	}
	
	public  Object Visite (TInput s,Object arg) {
	 	out.println ("<LI> <B> Input </B>");
		return null;
	}
	
	
	public  Object Visite (TNamedExp s,Object arg) {
		if (checkNotSaved(s)) {
			if (checkNeedLine(arg)) out.print ("<LI>");
			out.println ("<B> Name </B> <UL>");
			out.println ("<LI>[" + s.name + "]");  
			s.getArg1().Accept(this,TWriteState.DEFAULT);
			out.println ("</UL>");
		}else
			writeId(s,arg);
		return null;
	}
	
	public  Object Visite (TExpandExp s,Object arg) {
		if (checkNotSaved(s)) {
			if (checkNeedLine(arg)) out.print ("<LI>");
			out.println ("<B> Dilate </B> <UL>");
			s.getArg1().Accept(this,TWriteState.DEFAULT);
			s.getArg2().Accept(this,TWriteState.DEFAULT);
			out.println ("</UL>");
		}else
			writeId(s,arg);
		return null;
	}
	
	
	public Object Visite (TApplExp s,Object arg) {
		if (checkNotSaved(s)) {
			if (checkNeedLine(arg)) out.print ("<LI>");
			out.println ("<B> Appl </B> <UL>");
			s.getArg1().Accept(this,TWriteState.DEFAULT);
			s.getArg2().Accept(this,TWriteState.DEFAULT);
 			out.println ("</UL>");
 		}else 
			writeId(s,arg);
		return null;
	}
		
	public Object Visite (TAbstrExp s,Object arg) {
		if (checkNotSaved(s)) {
				
			if (checkNeedLine(arg)) out.print ("<LI>");
			
			/* Traitement particulier pour la valeur Float.POSITIVE_INFINITY
			 - Metrowerk code "Inf" mais n'arrive pas a décoder !!
			 - JDK code "1.#INF" et arrive a décoder
			 ==> gestion "à la main"
			*/
			
			if (Float.isInfinite(s.val)) {
				out.println ("<B> Lambda </B>[" + "Inf" + "]<UL>");
			}else{
				out.println ("<B> Lambda </B>[" + s.val + "]<UL>");
			}
			
			// cas particulier pour le premier
			s.getArg1().Accept(this,TWriteState.DEFAULT);
			s.getArg1().getArg1().Accept(this,TWriteState.LETCLAUSE);
			s.getArg2().Accept(this,TWriteState.DEFAULT);
 			out.println ("</UL>");
 		}else 
			writeId(s,arg);
		return null;
	}
		
	public Object Visite (TYAbstrExp s,Object arg) {
		if (checkNotSaved(s)) {
			
			if (checkNeedLine(arg)) out.print ("<LI>");
			
			/* Traitement particulier pour la valeur Float.POSITIVE_INFINITY
			 - Metrowerk code "Inf" mais n'arrive pas a décoder !!
			 - JDK code "1.#INF" et arriva a décoder
			 ==> gestion "à la main"
			*/
			
			if (Float.isInfinite(s.val)) {
				out.println ("<B> YLambda </B>[" + "Inf" + "]<UL>");
			}else{
				out.println ("<B> YLambda </B>[" + s.val + "]<UL>");
			}
			
			// cas particulier pour le premier
			s.getArg1().Accept(this,TWriteState.DEFAULT);
			s.getArg1().getArg1().Accept(this,TWriteState.LETCLAUSE);
			s.getArg2().Accept(this,TWriteState.DEFAULT);
 			out.println ("</UL>");
 		}else 
			writeId(s,arg);
		return null;
	}
 	
 	
	public Object Visite (TSequenceExp s,Object arg) {
		if (checkNotSaved(s)) {
			if (arg == TWriteState.SEQ){
				s.getArg1().Accept(this,TWriteState.DEFAULT);
				s.getArg2().Accept(this,TWriteState.SEQ);
			}else {
				if (checkNeedLine(arg)) out.print ("<LI>");
				out.println ("<B> Sequence </B> <UL>");
				s.getArg1().Accept(this,TWriteState.DEFAULT);
				s.getArg2().Accept(this,TWriteState.SEQ);
 				out.println ("</UL>");
 			}
 		}else
			writeId(s,arg);
		return null;
	}
 	
 	
 	public Object Visite (TMixExp s,Object arg) {
 		if (checkNotSaved(s)) {
 			if (arg == TWriteState.MIX){
				s.getArg1().Accept(this,TWriteState.DEFAULT);
 				s.getArg2().Accept(this,TWriteState.MIX);
 			}else {
 				if (checkNeedLine(arg)) out.print ("<LI>");
 				out.println ("<B> Mix </B> <UL>");
				s.getArg1().Accept(this,TWriteState.DEFAULT);
 				s.getArg2().Accept(this,TWriteState.MIX);
 				out.println ("</UL>");
 			}
 		}else
			writeId(s,arg);
		return null;
 	}
 	
 	
 	public Object Visite (TModifyExp s,Object arg) {
 		if (checkNotSaved(s)) {
 			if (checkNeedLine(arg)) out.print ("<LI>"); 
 			switch (s.index) {
					case TExp.PITCH:
						out.println ("<B> Transp </B>[" + s.value + "]<UL>");
						break;
					case TExp.VEL:
						out.println ("<B> Attn </B>[" + s.value + "]<UL>");
						break;
					case TExp.DURATION:
						out.println ("<B> Expand </B>[" + s.value + "]<UL>");	
						break;
					case TExp.CHAN:
						if (s.op == TAdd.op) 
							out.println ("<B> Trsch </B>[" + s.value + "]<UL>");	
						else
							out.println ("<B> Setch </B>[" + s.value + "]<UL>");
						break;
			}
					
			s.getArg1().Accept(this,TWriteState.DEFAULT);
			out.println ("</UL>");
		}else
			writeId(s,arg);
		return null;
	}
	
	 		
	public  Object Visite (TBeginExp s,Object arg) {
		if (checkNotSaved(s)) {
			if (checkNeedLine(arg)) out.print ("<LI>");
			out.println ("<B> Begin </B> <UL>");
			s.getArg1().Accept(this,TWriteState.DEFAULT);
			s.getArg2().Accept(this,TWriteState.DEFAULT);
			out.println ("</UL>");
		}else
			writeId(s,arg);
		return null;
	}
	
	public  Object Visite (TRestExp s,Object arg) {
		if (checkNotSaved(s)) {
			if (checkNeedLine(arg)) out.print ("<LI>");
			out.println ("<B> Rest </B> <UL>");
			s.getArg1().Accept(this,TWriteState.DEFAULT);
			s.getArg2().Accept(this,TWriteState.DEFAULT);
			out.println ("</UL>");
		}else
			writeId(s,arg);
		return null;
	}
	
 	public Object Visite( TUrlExp s ,Object arg){
 		if (checkNotSaved(s)) {
 			if (checkNeedLine(arg)) out.print ("<LI>");
 			// ATTENTION PAS D'ESPACE  entre HREF et "=" (pb du parser?)
 			out.println (" <A HREF=\"" + s.url + "\">Url</A>");
		}else
			writeId(s,arg);
		return null;
 	}
 	
 	
	public  Object Visite (TMuteExp s,Object arg) {
		if (checkNotSaved(s)) {
			if (checkNeedLine(arg)) out.print ("<LI>");
			out.println ("<B> Mute </B> <UL>");
			s.getArg1().Accept(this,TWriteState.DEFAULT);
			out.println ("</UL>");
		}else
			writeId(s,arg);
		return null;
	}
 	
 	public Object Visite( TArrayExp s,Object arg){
		if (checkNotSaved(s)) {
			if (checkNeedLine(arg)) out.print ("<LI>");
			out.println ("<B> Array </B>[" + s.arg1.size() + "]<UL>");	
			for (Enumeration e = s.arg1.elements(); e.hasMoreElements();) {
				TExp res = (TExp)e.nextElement();
				res.Accept(this,TWriteState.DEFAULT);
			}	
			out.println ("</UL>");
		}else
			writeId(s,arg);
		return null;
	}
 	
 	public Object Visite( TNullExp s ,Object arg){
 		out.println ("<LI><B> Null </B>");
 		return null;
 	}
}
