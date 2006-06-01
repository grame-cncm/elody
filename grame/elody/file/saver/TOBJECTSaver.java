package grame.elody.file.saver;

import grame.elody.file.parser.TFileContent;

import java.io.ObjectOutputStream;
import java.io.OutputStream;

/*******************************************************************************************
*
*	 TOBJECTSaver (classe) : le saver OBJECT (utilisation de la serialisation)
* 
*******************************************************************************************/

public final class TOBJECTSaver implements TImpFileSaver {
	public TOBJECTSaver () {}
	
	public void writeFile(TFileContent content,OutputStream outstream) throws Exception{
		ObjectOutputStream s = new ObjectOutputStream(outstream);
		s.writeObject(content);
		s.flush();
	}
}
