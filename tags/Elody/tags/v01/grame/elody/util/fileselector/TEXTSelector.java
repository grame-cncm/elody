package grame.elody.util.fileselector;

import java.awt.Frame;

public class TEXTSelector extends FileSelector {
	public FileSelector createInstance(Frame frame, String info, String file, int mode) {
   		return  new TEXTSelector (frame, info, file, mode);
	}
	
	public TEXTSelector () {}
	 
	public TEXTSelector (Frame frame, String info, String file, int mode) {
    	super (frame, info, file, mode);
	} 
	public boolean select () {
		String exts[] = { ".txt", ".TXT" };
		return select (new FileFilter (exts));
	}
}
