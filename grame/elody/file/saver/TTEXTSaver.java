package grame.elody.file.saver;

import grame.elody.file.parser.TFileContent;
import grame.elody.file.text.saver.TTextExpSaver;

import java.io.OutputStream;
import java.io.OutputStreamWriter;

/*******************************************************************************************
*
*	 TTEXTSaver (classe) : le saver TEXT
* 
*******************************************************************************************/

public final class TTEXTSaver implements TImpFileSaver {
	public TTEXTSaver () {}
	
	public void writeFile(TFileContent content,OutputStream outstream) throws Exception{
	
		TTextExpSaver saver = new TTextExpSaver (new OutputStreamWriter(outstream));
		saver.writeFileHeader();
		saver.writeTitle (content.title);
		saver.writeAuthor (content.author);
		saver.writeDescription (content.description);
		saver.writeExp(content.exp);
		saver.writeFileEnd();
	}
}
