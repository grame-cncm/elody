package grame.elody.util.fileselector;

import java.awt.Frame;

public class OBJECTSelector extends FileSelector {
	public FileSelector createInstance(Frame frame, String info, String file, int mode) {
   		return  new OBJECTSelector (frame, info, file, mode);
	}
	
	public OBJECTSelector () {}
	 
	public OBJECTSelector (Frame frame, String info, String file, int mode) {
    	super (frame, info, file, mode);
	} 
	public boolean select () {
		String exts[] = { ".obj", ".OBJ" };
		return select (new FileFilter (exts));
	}
}
