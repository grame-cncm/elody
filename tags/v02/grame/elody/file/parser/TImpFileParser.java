package grame.elody.file.parser;

import java.io.InputStream;

/*******************************************************************************************
*
*	 TImpFileParser (interface) : l'interface que les parser (internes) doivent impl�menter
* 
*******************************************************************************************/

public interface TImpFileParser {
	 public TFileContent readFile(InputStream stream) throws Exception;
}
