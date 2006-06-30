package grame.elody.file.saver;

import grame.elody.file.parser.TFileContent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;

/*******************************************************************************************
*
*	 TFileSaver (classe) : classe générique utilisée par les clients
*
*    TFileSaver saver = new TFileSaver(); 
*    saver.writeFile(new TFileContent ("steph", "test", "description ", exp));
* 
*******************************************************************************************/

public final class TFileSaver {
	public TFileSaver () {}
	
	public void writeFile(TFileContent content,URL url, String format) throws Exception {
		System.out.println ("Not yet implemented");
	}
	
	public void writeFile(TFileContent content,File file, String format) throws Exception {
		TImpFileSaver saver =  (TImpFileSaver)Class.forName("grame.elody.file.saver."+ "T" + format + "Saver").newInstance();
		OutputStream outstream = new FileOutputStream(file.getAbsolutePath());
		saver.writeFile(content,outstream);
		outstream.close();
	}
	
	public void writeFile(TFileContent content,OutputStream outstream, String format) throws Exception {
		TImpFileSaver saver =  (TImpFileSaver)Class.forName("grame.elody.file.saver."+ "T" + format + "Saver").newInstance();
		saver.writeFile(content,outstream);
		outstream.flush();
	}
}
