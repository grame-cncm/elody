package grame.elody.file.parser;

import java.io.InputStream;

/*******************************************************************************************
*
*	 THTMLParser (classe) : le parser HTML
* 
*******************************************************************************************/

public final class THTMLParser implements TImpFileParser{
	TBasicHtmlParser parser ;
	
	public TFileContent readFile(InputStream input) throws Exception{ 
		parser = TBasicHtmlParser.readFileHeader(input); // alloue le parser correspondant à la version du fichier
		return parser.readFileContent();
	}
}

