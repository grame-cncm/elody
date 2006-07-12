package grame.elody.file.html.parser;

import grame.elody.file.parser.ParseOperator;
import grame.elody.file.parser.TFileContent;
import grame.elody.file.parser.TFileParser;
import grame.elody.lang.TExpMaker;
import grame.elody.lang.texpression.expressions.TExp;

import java.util.Hashtable;

/*******************************************************************************************
*
*	 THtmlParser1 (classe) : classe interne du parser HTML
* 
*******************************************************************************************/

public class THtmlParser1 extends TBasicHtmlParser {

	protected static Hashtable parseTable = new Hashtable();
	protected Hashtable identTable;
		
	protected THtmlParser1 () {identTable = new Hashtable(); }// contient les expressions associées aux ident et aux letIdent

	// FILE VERSION 100
	
	public final TFileContent readFileContent() {
		identTable.clear();
		String str;
		
		//System.out.println(nextToken()); // premier "HTML"
		//System.out.println(nextToken()); // premier "UL"
		
		do {str = nextToken();} while (!str.startsWith("Title") && !str.startsWith("UL"));
		
		if (str.startsWith("UL")) {
		
			// Format 100 : pas d'info de titre, auteur et description
			// System.out.println("Format 100");
			
			TExp res =  parseExpression();  
			nextToken(); 	// dernier  "/UL"
			nextToken(); 	// dernier  "/HTML"
			return new TFileContent("","","",res);
			
		}else {
		
			// Format 101 : info de titre, auteur et description
			// System.out.println("Format 101");
			// System.out.println("ivi");
			
			String title = parseTextUntil ("Author");
			String author = parseTextUntil ("Description");
			String description = parseTextUntil ("UL");
			TExp res =  parseExpression();
			nextToken(); 	// dernier  "/UL"
			nextToken();	// dernier  "/HTML"
			return new TFileContent(title,author,description,res);
		}
	}
	
	
	final String parseTextUntil(String endtoken){
		StringBuffer buffer = new StringBuffer();
		String str;
		
		while (((str = nextToken()) != null) && !str.startsWith(endtoken)) {
			buffer.append(str);
			buffer.append(" "); // un espace entre chaque token
		}
		return new String(buffer);
	}
	
	
	public final String nextToken() {return tok.NextToken();}
	
	public final TExp parseExpression() {
		String str = nextToken();
		ParseOperator op;
		
		if (str.startsWith("/UL")) { 
			return TExpMaker.gExpMaker.createNull();
		}else if (str.startsWith("Id")) { 
			return parseIdent(str);
		}else if (str.startsWith("LetId")) { // LetId apparaissant dans le corps de l'expression
			return parseLetIdent1(str);
		}else if (str.startsWith("ELink")) { 
			return parseUrl(str.substring(5));
		}else if ((op = (ParseOperator)parseTable.get(str)) != null ) {
			return  op.parseExp(this);
		}else{
			return parseExpression();
		}
	}
	
	public final void parserLetIdent (String str, TExp exp) {identTable.put(str,exp);}
	
	// retourne l'expression associée à un LetIdent dans la table
	
	final TExp parseLetIdent1( String val) { return (TExp)identTable.get(val);}

	final TExp parseIdent( String val) {
		TExp res = (TExp)identTable.get(val);
		
		if (res == null) {
			res =  TExpMaker.gExpMaker.createIdent(null); // cree un Ident sans expression associée
			identTable.put(val,res);
		}
		return res;
	}
	
	final TExp parseUrl (String str) {
		try {
			TFileParser  parser = new TFileParser();
			TFileContent res = parser.readFile(TUrlResolver.makeUrl(doc,str));
			nextToken();  // "Url"
			return TExpMaker.gExpMaker.createUrl(res.exp,str);
		}catch (Exception e1) {
			System.out.println("Parse Url exception " + e1);
			return TExpMaker.gExpMaker.createNull();
		}
	}
		
	// valeur de la forme:   [125]
	
	public final float parseValue( String val) {
		String str = val.substring(1,val.length()-1);
		
		/* Traitement particulier pour la valeur Float.POSITIVE_INFINITY
		 - Metrowerk code "Inf" mais n'arrive pas a décoder !!
		 - JDK code "1.#INF" et arrive a décoder
		 ==> gestion "à la main"
		*/
		
		return (str.startsWith("Inf")) ?  Float.POSITIVE_INFINITY :  Float.valueOf(str).floatValue();	
	}
}
