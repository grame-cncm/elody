package grame.elody.util.fileselector;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.io.FilenameFilter;

public class FileSelector extends FileDialog {
	// methode generique d'instantiation
	public FileSelector createInstance(Frame frame, String info, String file, int mode) {
    	return  new FileSelector (frame, info, file, mode);
	}
	
	public FileSelector () { super ((Frame)null,"",FileDialog.LOAD);}
	
	public FileSelector (Frame frame, String info, String file, int mode) {
    	super (frame, info, mode);
    	if (mode == FileDialog.SAVE) setFile (file);
	} 
	public boolean select (FilenameFilter filter) {
    	if (filter != null) setFilenameFilter (filter);
    	setVisible (true);
    	return getFile() != null;
	}
	public boolean select () {
		 return select (null); 
	}
	public File file () {
		return new File (getDirectory(), getFile());
	}
	public String fullName () {
		return getDirectory() + getFile();
	}
}
