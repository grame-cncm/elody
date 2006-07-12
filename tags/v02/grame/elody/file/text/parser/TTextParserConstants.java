package grame.elody.file.text.parser;

public interface TTextParserConstants {
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
	  int ABSTR = 30;
	  int BEGIN = 31;
	  int REST = 32;
	  int EXPAND = 33;
	  int BMUTE = 34;
	  int EMUTE = 35;
	  int NUM = 36;
	  int INTEGER = 37;
	  int DIGIT1 = 38;
	  int NOTE_ID = 39;
	  int VAR_ID = 40;

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
	    "\"\\\\\"",
	    "\"<|\"",
	    "\"|>\"",
	    "\"<>\"",
	    "\"<<\"",
	    "\">>\"",
	    "<NUM>",
	    "<INTEGER>",
	    "<DIGIT1>",
	    "<NOTE_ID>",
	    "<VAR_ID>",
	    "\"=>\"",
	    "\":\"",
	    "\"@\"",
	  };
}
