/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.file.html.parser;

import grame.elody.file.parser.ParseOperator;
import grame.elody.lang.TExpMaker;
import grame.elody.lang.texpression.expressions.TExp;

import java.awt.Color;
import java.util.StringTokenizer;
import java.util.Vector;

/*******************************************************************************************
*
*	 THtmlParser100 (classe) : parser HTML (interne) format 100.
* 
*******************************************************************************************/

public final class THtmlParser100 extends THtmlParser1 {

	static {
		parseTable.put(new String("Event"), new ParseEvent100());
		parseTable.put(new String("Input"), new ParseInput100());
	
		parseTable.put(new String("Silence"), new ParseSilence100());
	 	
	 	parseTable.put(new String("Sequence"),  new ParseSeq100());
	 	parseTable.put(new String("Mix"),  new ParseMix100());
	 	
	 	parseTable.put(new String("Transp"),  new ParseTransp100());
	 	parseTable.put(new String("Attn"),  new ParseAttn100());
	 	parseTable.put(new String("Expand"),  new ParseExpv100());
	 	
	 	parseTable.put(new String("Trsch"),  new ParseTrschan100());
	 	parseTable.put(new String("Setch"),  new ParseSetchan100());
	 	
	 	parseTable.put(new String("Appl"),  new ParseAppl100());
	 	parseTable.put(new String("Lambda"),  new ParseAbtsr100());
	 	parseTable.put(new String("Rest"),  new ParseRest100());
	 	parseTable.put(new String("Begin"),  new ParseBegin100());
	 	
	 	parseTable.put(new String("Dilate"),  new ParseDilate100());
	 	parseTable.put(new String("Let"),  new ParseLet100());
	 	
	 	parseTable.put(new String("Name"),  new ParseNamed100());
	 	
	 	parseTable.put(new String("Null"),  new ParseNull100());
	 	parseTable.put(new String("YLambda"),   new ParseYAbtsr100());
	 	parseTable.put(new String("Mute"),   new ParseMute100());
	 	parseTable.put(new String("Array"),   new ParseArray100());
	 }
}

final class ParseLet100 extends ParseOperator {

	public TExp parseExp (THtmlParser1 parser) {
		parser.nextToken(); // UL
		parseLetClauses (parser); // effet de bord : remplit la hashtable (ident <==> exp)
		return parser.parseExpression();
	}
	
	public void parseLetClauses (THtmlParser1 parser) {
		String str = parser.nextToken();
		if (!str.startsWith("/UL")) { 
			parser.parserLetIdent(str,parser.parseExpression());
			parseLetClauses (parser);
		}
	}
}

final class ParseEvent100 extends  ParseOperator {
	// [color,pitch,vel,chan,dur]
	StringTokenizer stk;
	
	public TExp parseExp (THtmlParser1 parser) {
		String str= parser.nextToken();
		stk = new StringTokenizer(str.substring(1,str.length()-1), ",");
		String s1 = stk.nextToken();
		String s2 = stk.nextToken();
		String s3 = stk.nextToken();
		String s4 = stk.nextToken();
		String s5 = stk.nextToken();
		
		return TExpMaker.gExpMaker.createNote(new Color(Integer.parseInt(s1)),Integer.parseInt(s2),
			Integer.parseInt(s3),Integer.parseInt(s4),Integer.parseInt(s5) );
	}
}

final class ParseInput100 extends  ParseOperator {
	
	public TExp parseExp (THtmlParser1 parser) {
		return TExpMaker.gExpMaker.createInput();
	}
}

final class ParseSilence100 extends  ParseOperator {
	// [color,pitch,vel,chan,dur]
	StringTokenizer stk;
	
	public TExp parseExp (THtmlParser1 parser) {
		String str= parser.nextToken();
		stk = new StringTokenizer(str.substring(1,str.length()-1), ",");
		String s1 = stk.nextToken();
		String s2 = stk.nextToken();
		String s3 = stk.nextToken();
		String s4 = stk.nextToken();
		String s5 = stk.nextToken();
		
		return TExpMaker.gExpMaker.createSilence(new Color(Integer.parseInt(s1)),Integer.parseInt(s2),
			Integer.parseInt(s3),Integer.parseInt(s4),Integer.parseInt(s5) );
	}
}

final class ParseNamed100 extends ParseOperator {

	public TExp parseExp (THtmlParser1 parser) {
		parser.nextToken(); // "UL"
		String val = parser.nextToken(); // nom de la forme [toto]
		//System.out.println (val);
		
		String name = val.substring(1,val.length()-1);
		//System.out.println (name);
		
		TExp res = TExpMaker.gExpMaker.createNamed(parser.parseExpression(),name);
		parser.nextToken(); //  "/UL"
		return  res;
	}
}

final class ParseAppl100 extends  ParseOperator {

	public TExp parseExp (THtmlParser1 parser) {
		parser.nextToken(); // "UL"
		TExp res = TExpMaker.gExpMaker.createAppl (parser.parseExpression(),parser.parseExpression());
		parser.nextToken(); //  "/UL"
		return res;
	}
}

final class ParseAbtsr100 extends ParseOperator {

	public TExp parseExp (THtmlParser1 parser) {
		String str= parser.nextToken(); // valeur numerique
		parser.nextToken(); // "UL"
		TExp ident = parser.parseExpression();
		TExp arg = parser.parseExpression();
		TExp body = parser.parseExpression();
		TExp res =  TExpMaker.gExpMaker.createLambda(parser.parseValue(str),ident, arg, body);
		parser.nextToken(); //  "/UL"
		return res;
	}
}

final class ParseYAbtsr100 extends ParseOperator {

	public TExp parseExp (THtmlParser1 parser) {
		String value = parser.nextToken(); // valeur numerique
		parser.nextToken(); // "UL"
		TExp ident = parser.parseExpression();
		TExp arg = parser.parseExpression();
		TExp body = parser.parseExpression();
		TExp res =  TExpMaker.gExpMaker.createYLambda(parser.parseValue(value),ident, arg, body);
		parser.nextToken(); //  "/UL"
		return res;
	}
}

final class ParseSeq100 extends ParseOperator {

	public TExp parseExp (THtmlParser1 parser) {
		parser.nextToken(); // "UL"
		return parseArgs(parser);
	}
	
	 TExp parseArgs (THtmlParser1 parser) {
		TExp res = parser.parseExpression();
		return checkNull(res) ? res : TExpMaker.gExpMaker.createSeq (res,parseArgs(parser));
	}
}

final class ParseMix100 extends ParseOperator {

	public TExp parseExp (THtmlParser1 parser) {
		parser.nextToken(); // UL
		return parseArgs(parser);
	}
	
	TExp parseArgs (THtmlParser1 parser) {
		TExp res = parser.parseExpression();
		return checkNull(res) ? res : TExpMaker.gExpMaker.createMix (res,parseArgs(parser));
	}
}

final class ParseTransp100 extends ParseOperator {

	public TExp parseExp (THtmlParser1 parser) {
		String str= parser.nextToken(); // valeur numerique
		parser.nextToken(); // "UL"
		TExp res = TExpMaker.gExpMaker.createTrsp (parser.parseExpression(),parser.parseValue(str));
		parser.nextToken();  //  "/UL"
		return res;
	}
}

final  class ParseAttn100 extends ParseOperator {

	public TExp parseExp (THtmlParser1 parser) {
		String str= parser.nextToken(); // valeur numerique
		parser.nextToken(); // "UL"
		TExp res = TExpMaker.gExpMaker.createAttn (parser.parseExpression(),parser.parseValue(str));
		parser.nextToken();  //  "/UL"
		return res;
	}
}

final  class ParseExpv100 extends ParseOperator {

	public TExp parseExp (THtmlParser1 parser) {
		String str= parser.nextToken(); // valeur numerique
		parser.nextToken(); // "UL"
		TExp res = TExpMaker.gExpMaker.createExpv (parser.parseExpression(),parser.parseValue(str));
		parser.nextToken();  //  "/UL"
		return res;
	}
}

final class ParseTrschan100 extends ParseOperator {

	public TExp parseExp (THtmlParser1 parser) {
		String str= parser.nextToken(); // valeur numerique
		parser.nextToken(); // "UL"
		TExp res = TExpMaker.gExpMaker.createTrsch (parser.parseExpression(),parser.parseValue(str));
		parser.nextToken();  //  "/UL"
		return res;
	}
}

final class ParseSetchan100 extends ParseOperator {

	public TExp parseExp (THtmlParser1 parser) {
		String str= parser.nextToken(); // valeur numerique
		parser.nextToken(); // "UL"
		TExp res = TExpMaker.gExpMaker.createSetch (parser.parseExpression(),parser.parseValue(str));
		parser.nextToken();  //  "/UL"
		return res;
	}
}

final  class ParseBegin100 extends ParseOperator {

	public TExp parseExp (THtmlParser1 parser) {
		parser.nextToken();  //  "UL"
		TExp res =  TExpMaker.gExpMaker.createBegin (parser.parseExpression(),parser.parseExpression());
		parser.nextToken();  //  "/UL"
		return res;
	}
}

final class ParseRest100 extends ParseOperator {

	public TExp parseExp (THtmlParser1 parser) {
		parser.nextToken();  //  "UL"
		TExp res =  TExpMaker.gExpMaker.createRest (parser.parseExpression(),parser.parseExpression());
		parser.nextToken();  //  "/UL"
		return res;
	}
}

final class ParseMute100 extends ParseOperator {

	public TExp parseExp (THtmlParser1 parser) {
		parser.nextToken();  //  "UL"
		TExp res =  TExpMaker.gExpMaker.createMute (parser.parseExpression());
		parser.nextToken();  //  "/UL"
		return res;
	}
}

final class ParseArray100 extends ParseOperator {

	public TExp parseExp (THtmlParser1 parser) {
	 	Vector<TExp> vector = new Vector<TExp>();
	 	String str= parser.nextToken(); // valeur numerique
		parser.nextToken();  //  "UL"
		
		for (int i = 0; i < (int)parser.parseValue(str); i++) {
			vector.addElement(parser.parseExpression());
		}
		TExp res =  TExpMaker.gExpMaker.createArray (vector);
		parser.nextToken();  //  "/UL"
		return res;
	}
}

final class ParseDilate100 extends ParseOperator {

	public TExp parseExp (THtmlParser1 parser) {
		parser.nextToken();  //  "UL"
		TExp res =  TExpMaker.gExpMaker.createDilate (parser.parseExpression(),parser.parseExpression());
		parser.nextToken();  //  "/UL"
		return res;
	}
}

final class ParseNull100 extends ParseOperator {

	public TExp parseExp (THtmlParser1 parser) {
		return TExpMaker.gExpMaker.createNull();
	}
}
