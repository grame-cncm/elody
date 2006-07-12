package grame.elody.file.html.parser;

import grame.elody.file.parser.ElodyFileFormatException;
import grame.elody.file.parser.TFileContent;

import java.io.InputStream;
import java.net.URL;

/*******************************************************************************************
*
*	 TBasicHtmlParser (classe) : classe interne du parser HTML
* 
*******************************************************************************************/

public abstract class TBasicHtmlParser {
	protected THtmlStreamTokenizer tok;
	protected URL doc;
	protected InputStream input;
	
	public static TBasicHtmlParser readFileHeader(InputStream inputstream ) throws ElodyFileFormatException{
		
		THtmlStreamTokenizer tok = new THtmlStreamTokenizer(inputstream);
		TBasicHtmlParser res;
	
			
		String str1; do {str1 = tok.NextToken(); } while (!str1.startsWith("Elody"));
		String str2 =  tok.NextToken();  // "File"
		String str3 =  tok.NextToken();  // "100"  (numéro de version)
	
		
		if (str1.startsWith("Elody") && str2.startsWith("File")) { 
		
			try {
				res = (TBasicHtmlParser)Class.forName("grame.elody.file.html.parser.THtmlParser"+str3).newInstance();
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
	
	public abstract TFileContent readFileContent();
}
