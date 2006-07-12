package grame.elody.file.guido.parser;

public interface BGuidoParserConstants {
	  int EOF = 0;
	  int SHARP = 14;
	  int FLAT = 15;
	  int STAR = 16;
	  int FRAC = 17;
	  int PLUS = 18;
	  int MINUS = 19;
	  int LACO = 20;
	  int RACO = 21;
	  int VIR = 22;
	  int DOT = 23;
	  int LTRI = 24;
	  int RTRI = 25;
	  int LPAR = 26;
	  int RPAR = 27;
	  int LBRA = 28;
	  int RBRA = 29;
	  int NUM = 30;
	  int DIGIT = 31;
	  int REAL = 32;
	  int DIGIT1 = 33;
	  int NOTE_ID = 34;
	  int TAG_ID = 35;
	  int STRING = 36;

	  int DEFAULT = 0;
	  int IN_LINE_COMMENT = 1;
	  int IN_COMMENT = 2;

	  String[] tokenImage = {
	    "<EOF>",
	    "\" \"",
	    "\"\\r\"",
	    "\"\\t\"",
	    "\"|\"",
	    "\"\\n\"",
	    "\"%\"",
	    "\"(*\"",
	    "\"\\n\"",
	    "\"\\r\"",
	    "<token of kind 10>",
	    "\"(*\"",
	    "\"*)\"",
	    "<token of kind 13>",
	    "\"#\"",
	    "\"&\"",
	    "\"*\"",
	    "\"/\"",
	    "\"+\"",
	    "\"-\"",
	    "\"{\"",
	    "\"}\"",
	    "\",\"",
	    "\".\"",
	    "\"<\"",
	    "\">\"",
	    "\"(\"",
	    "\")\"",
	    "\"[\"",
	    "\"]\"",
	    "<NUM>",
	    "<DIGIT>",
	    "<REAL>",
	    "<DIGIT1>",
	    "<NOTE_ID>",
	    "<TAG_ID>",
	    "<STRING>",
	  };

}
