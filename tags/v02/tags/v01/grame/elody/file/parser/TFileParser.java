package grame.elody.file.parser;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Vector;

/*******************************************************************************************
*
*	 TFileParser (classe) : classe générique utilisée par les clients
*
*    TFileParser parser = new TFileParser(); 
*    TFileContext content  = parser.readFile(URL...ou File... ou InsputStream)
* 
*******************************************************************************************/

public final class TFileParser {
	protected static Vector formatTable = new Vector();
	public static void registerParser(Class name) { formatTable.addElement(name);}
		
	public TFileParser () {}
	
	public TFileContent readFile(File file) throws Exception {
		InputStream input = null;
		TImpFileParser parser;
		
		for (Enumeration e = formatTable.elements(); e.hasMoreElements();) {
			try {
				Class classname = (Class)e.nextElement();
				parser = (TImpFileParser)classname.newInstance();
				input = new  BufferedInputStream (new FileInputStream(file.getAbsolutePath()));
				return parser.readFile(input);
			}catch (Exception ex) {}
			
			finally {if (input != null) input.close();}
		}
		throw new ElodyFileFormatException();
	}
	
	public TFileContent readFile(URL url) throws Exception {
		
		InputStream input = null;
		TImpFileParser parser;
		
		for (Enumeration e = formatTable.elements(); e.hasMoreElements();) {
			try {
				Class classname = (Class)e.nextElement();
				parser = (TImpFileParser)classname.newInstance();
				input =  new  BufferedInputStream(url.openStream());
				return parser.readFile(input);
			}catch (Exception ex) {
				//System.out.println(ex);
			}
			
			finally {if (input != null) input.close();}
		}
		throw new ElodyFileFormatException();
	}
	
	// ATTENTION ne marche seulement 1 fois
	
	public TFileContent readFile(InputStream input) throws Exception{ 
		TImpFileParser parser;
	
		for (Enumeration e = formatTable.elements(); e.hasMoreElements();) {
			try {
				Class classname = (Class)e.nextElement();
				parser = (TImpFileParser)classname.newInstance();
				return parser.readFile(input);
			}catch (Exception ex) {}
		}
		throw new ElodyFileFormatException();
	}
}
