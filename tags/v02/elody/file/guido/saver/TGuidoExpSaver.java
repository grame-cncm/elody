package grame.elody.file.guido.saver;

import grame.elody.file.midifile.TMultiSeqVisitor;
import grame.elody.lang.texpression.expressions.TExp;
import grame.midishare.Midi;

import java.io.BufferedWriter;
import java.io.Writer;

/*******************************************************************************************
*
*	 TGuidoExpSaver (classe) :sauvegarde des expressions au format GUIDO (interne)
* 
*******************************************************************************************/

public final class TGuidoExpSaver {
	TGuidoWriter writer;
	
	public TGuidoExpSaver (Writer out) { 
		writer = new TGuidoWriter(new BufferedWriter(out));
	}
	
	public void  writeFileHeader() {
		writer.writeFileHeader();
	}
	
	public void  writeTitle(String title) {
	 	writer.writeTitle(title);
	}
	
	public void  writeAuthor(String author) {
		 writer.writeAuthor(author);
	}
	
	public void  writeDescription(String dec) {
	 	writer.writeDescription(dec);
	}
	
	public void  writeFileEnd() {
		writer.writeFileEnd();
	}
	
	
	public void writeExp(TExp exp) {
		TMultiSeqVisitor visitor = new TMultiSeqVisitor();
		int seq = Midi.NewSeq();
		if (seq != 0) writer.writeVoices(visitor.fillSeq(exp,seq));
		Midi.FreeSeq(seq);
	}
}
