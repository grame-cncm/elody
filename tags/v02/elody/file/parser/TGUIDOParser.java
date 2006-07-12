package grame.elody.file.parser;

import grame.elody.file.guido.parser.BGuidoParser;
import grame.elody.file.guido.parser.BasicGuidoReader;
import grame.elody.file.guido.parser.TBasicGuidoReader;
import grame.elody.lang.texpression.expressions.TExp;

import java.io.InputStream;

/*******************************************************************************************
*
*	 TGUIDOParser (classe) : le parser GUIDO
* 
*******************************************************************************************/

public final class TGUIDOParser implements TImpFileParser {
	BGuidoParser parser;
	BasicGuidoReader reader;
	
	public TFileContent readFile(InputStream input) throws Exception{ 
		parser = new BGuidoParser(input);
		reader = new TBasicGuidoReader();
		parser.readGuidoFile(reader);
		return new TFileContent ("","","",(TExp)reader.getExp());
	}
}
