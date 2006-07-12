package grame.elody.file.parser;

import grame.elody.file.guido.parser.BGuidoParser;
import grame.elody.file.guido.parser.BasicGuidoReader;

import java.io.InputStream;
import java.io.ObjectInputStream;

/*******************************************************************************************
*
*	 TOBJECTParser (classe) : le parser OBJECT (utilisation de la serialisation)
* 
*******************************************************************************************/

public final class TOBJECTParser implements TImpFileParser {
	BGuidoParser parser;
	BasicGuidoReader reader;
	
	public TFileContent readFile(InputStream input) throws Exception{ 
		ObjectInputStream s = new ObjectInputStream(input);
		return (TFileContent)s.readObject(); 
	}
}
