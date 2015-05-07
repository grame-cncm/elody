/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

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
