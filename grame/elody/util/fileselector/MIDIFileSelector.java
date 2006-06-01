package grame.elody.util.fileselector;

import java.awt.Frame;

public class MIDIFileSelector extends FileSelector {
	public FileSelector createInstance(Frame frame, String info, String file, int mode) {
		return  new MIDIFileSelector (frame, info, file, mode);
	}
	public MIDIFileSelector () {}
	
	public MIDIFileSelector (Frame frame, String info, String file, int mode) {
    	super (frame, info, file, mode);
	} 
	public boolean select () {
		String exts[] = { ".midi", ".mid", ".MIDI", ".MID" };
		return select (new FileFilter (exts));
	}
}
