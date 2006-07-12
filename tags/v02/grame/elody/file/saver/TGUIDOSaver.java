package grame.elody.file.saver;

import grame.elody.file.guido.saver.TGuidoExpSaver;
import grame.elody.file.parser.TFileContent;

import java.io.OutputStream;
import java.io.OutputStreamWriter;

/*******************************************************************************************
*
*	 TGUIDOSaver (classe) : le saver GUIDO
* 
*******************************************************************************************/

public final class TGUIDOSaver implements TImpFileSaver {
	public TGUIDOSaver () {}
	
	public void writeFile(TFileContent content,OutputStream out) throws Exception{
	
		TGuidoExpSaver saver = new TGuidoExpSaver (new OutputStreamWriter(out));
		saver.writeFileHeader();
		saver.writeTitle (content.title);
		saver.writeAuthor (content.author);
		saver.writeDescription (content.description);
		saver.writeExp(content.exp);
		saver.writeFileEnd();
	}
}
