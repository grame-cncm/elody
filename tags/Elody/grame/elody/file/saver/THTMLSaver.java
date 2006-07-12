package grame.elody.file.saver;

import grame.elody.file.html.saver.TExpSaver;
import grame.elody.file.parser.TFileContent;

import java.io.OutputStream;

/*******************************************************************************************
*
*	 THTMLSaver (classe) : le saver HTML
* 
*******************************************************************************************/

public final class THTMLSaver implements TImpFileSaver {
	public THTMLSaver () {}
	
	public void writeFile(TFileContent content, OutputStream out) throws Exception {
		TExpSaver saver = new TExpSaver (out);
		saver.writeFileHeader();
		saver.writePlayerApplet();
		saver.writeTitle (content.title);
		saver.writeAuthor (content.author);
		saver.writeDescription (content.description);
		saver.writeExp(content.exp);
		saver.writeFileEnd();
		
	}
}
