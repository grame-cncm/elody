package grame.elody.file.saver;

import grame.elody.file.parser.TFileContent;

import java.io.OutputStream;

/*******************************************************************************************
*
*	 TImpFileSaver (interface) : l'interface que les saver (internes) doivent implémenter
* 
*******************************************************************************************/

public interface TImpFileSaver {
	public void writeFile(TFileContent content,OutputStream out) throws Exception;
}
