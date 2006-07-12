package grame.elody.file.parser;

import grame.elody.file.midifile.TMIDIFile;
import grame.elody.lang.texpression.expressions.TExp;
import grame.midishare.Midi;
import grame.midishare.MidiException;
import grame.midishare.midifile.MidiFileInfos;
import grame.midishare.midifile.MidiFileStream;
import grame.midishare.tools.MidiSequence;

import java.io.InputStream;

/*******************************************************************************************
*
*	 TMIDIFileParser (classe) : le parser MIDIFile
* 
*******************************************************************************************/

public final class TMIDIFileParser implements TImpFileParser {
	TMIDIFile midireader;
	MidiFileInfos info;

	public TMIDIFileParser () {
		midireader = new TMIDIFile();
		info = new MidiFileInfos();
	}
		
	public TFileContent readFile(InputStream input) throws Exception{ 
		TExp  exp;
		int seq;
		
		seq = Load(input);
		exp = midireader.readExp(seq); 
		Midi.FreeSeq(seq);
		return new TFileContent ("","","",exp);
	}

  	
	public int Load (InputStream input) throws Exception{
		int seq = Midi.NewSeq();
			
		if (seq != 0) {	
			MidiFileStream mf = new MidiFileStream();	
			mf.Load(input,seq,info);
			MidiSequence.TrsfTempo(seq, (double)info.clicks);
			return seq;
		}else{
			throw new MidiException ("No more MidiShare event");
		}
	}
}
