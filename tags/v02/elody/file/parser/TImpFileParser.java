package grame.elody.file.parser;

import java.io.InputStream;

/*******************************************************************************************
*
*	 TImpFileParser (interface) : l'interface que les parser (internes) doivent implémenter
* 
*******************************************************************************************/

public interface TImpFileParser {
	 public TFileContent readFile(InputStream stream) throws Exception;
}
