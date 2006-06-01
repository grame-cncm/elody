package grame.elody.file.parser;

import grame.elody.file.text.parser.TTextParser;
import grame.elody.lang.texpression.expressions.TExp;

import java.io.InputStream;

/*******************************************************************************************
*
*	 TTEXTParser (classe) : le parser de TEXTE
* 
*******************************************************************************************/

public final class TTEXTEParser implements TImpFileParser {
	TTextParser parser;
	TExp res;
	
	public TFileContent readFile(InputStream input) throws Exception{ 
		parser = new TTextParser(input);
		res = parser.readTextFile();
		return new TFileContent ("","","",res);
	}
	/*
	public TFileContent readFile(InputStream input) throws Exception{ 
		System.out.println("Not yet available");
		return new TFileContent ("","","",TExpMaker.gExpMaker.createNull());
	}
	*/
}
