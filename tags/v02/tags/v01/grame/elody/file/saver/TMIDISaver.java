package grame.elody.file.saver;

import grame.elody.file.midifile.TMIDIFile;
import grame.elody.file.parser.TFileContent;
import grame.midishare.Midi;
import grame.midishare.midifile.MidiFileInfos;
import grame.midishare.midifile.MidiFileStream;

import java.io.OutputStream;

/*******************************************************************************************
*
*	 TMIDISaver (classe) : le saver MIDI
* 
*******************************************************************************************/

public final class TMIDISaver implements TImpFileSaver {
	TMIDIFile midiwriter;
	MidiFileInfos info;
	
	public TMIDISaver () {
		midiwriter = new TMIDIFile();
		info = new MidiFileInfos();
	}
	
	public void writeFile(TFileContent content,OutputStream outstream) throws Exception{
		// A FINIR (ajouter titre, author)
		
		int seq = midiwriter.writeExp(content.exp); 
		MidiFileStream mf = new MidiFileStream();		
		info.clicks = 500;
		info.format = MidiFileInfos.midifile1;
		info.timedef = MidiFileInfos.TicksPerQuarterNote;
		mf.Save (outstream,seq, info);
		Midi.FreeSeq(seq);
	}
}
