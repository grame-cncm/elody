package grame.elody.util.fileselector;

import java.awt.Frame;

public class GUIDOFileSelector extends FileSelector {
	public FileSelector createInstance(Frame frame, String info, String file, int mode) {
	 	return  new GUIDOFileSelector (frame, info, file, mode);
	}
	
	public GUIDOFileSelector () {}
	
	public GUIDOFileSelector (Frame frame, String info, String file, int mode) {
    	super (frame, info, file, mode);
	} 
	public boolean select () {
		String exts[] = { ".gmn", ".GMN" };
		return select (new FileFilter (exts));
	}
}
