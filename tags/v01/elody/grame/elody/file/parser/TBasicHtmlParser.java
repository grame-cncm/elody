package grame.elody.file.parser;

import grame.elody.file.html.parser.THtmlStreamTokenizer;

import java.io.InputStream;
import java.net.URL;

/*******************************************************************************************
*
*	 TBasicHtmlParser (classe) : classe interne du parser HTML
* 
*******************************************************************************************/

public abstract class TBasicHtmlParser {
	THtmlStreamTokenizer tok;
	URL doc;
	InputStream input;
	
	static  TBasicHtmlParser readFileHeader(InputStream inputstream ) throws ElodyFileFormatException{
		
		THtmlStreamTokenizer tok = new THtmlStreamTokenizer(inputstream);
		TBasicHtmlParser res;
	
			
		String str1; do {str1 = tok.NextToken(); } while (!str1.startsWith("Elody"));
		String str2 =  tok.NextToken();  // "File"
		String str3 =  tok.NextToken();  // "100"  (numéro de version)
	
		
		if (str1.startsWith("Elody") && str2.startsWith("File")) { 
		
			try {
				res = (TBasicHtmlParser)Class.forName("grame.elody.file.THtmlParser"+str3).newInstance();
				res.tok = tok;
				return res;
			}catch (Exception e) {
				System.out.println ("This file use a newer format");	
				throw new ElodyFileFormatException();
			}
		}else {
			throw new ElodyFileFormatException(); 
		}
	}
	
	abstract TFileContent readFileContent();
}
