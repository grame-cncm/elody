package grame.elody.util.fileselector;

import java.awt.Frame;

public class HTMLFileSelector extends FileSelector {
	public FileSelector createInstance(Frame frame, String info, String file, int mode) {
   		return  new HTMLFileSelector (frame, info, file, mode);
	}
	
	public HTMLFileSelector () {}
	 
	public HTMLFileSelector (Frame frame, String info, String file, int mode) {
    	super (frame, info, file, mode);
	} 
	public boolean select () {
		String exts[] = { ".html", ".htm", ".HTML", ".HTM" };
		return select (new FileFilter (exts));
	}
}
