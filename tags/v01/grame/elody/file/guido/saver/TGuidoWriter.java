package grame.elody.file.guido.saver;

import grame.elody.file.TNoteConverter;
import grame.elody.file.midifile.TMultiSeqResult;
import grame.midishare.Midi;

import java.io.PrintWriter;
import java.io.Writer;

public final class TGuidoWriter {
	PrintWriter out;
	TNoteConverter noteConverter;
	int voice = 1;
	float currentVel = 100;
	int octaveOffset = 4; /* octave offset = 4, car la 440 = a1 dans GUIDO */
	static final int wholeDur = 4000;
	static final int defaultDur = wholeDur/4; /* la durée par défaut est celle d'une noire */
	
	static final String intensTable[] = {"pp","p","mf","f","ff"};

		
	public TGuidoWriter (Writer  out) {
		this.out = new PrintWriter(out);
		noteConverter = new TNoteConverter(octaveOffset, wholeDur, defaultDur, true); 
	}
	
	public void  writeFileHeader() {
		out.println ("% Generated from Elody");
		out.println ("% Web : http://www.grame.fr/Elody/Elody.html");
	}
	
	public void  writeTitle(String title) {
	 	out.println ( "% Title: " + title);
	}
	
	public void  writeAuthor(String author) {
	 	out.println ("% Author: " +author);
	}
	
	public void  writeDescription(String dec) {
	 	out.println ("% Description: " + dec);
	 	out.println("");
	}
	// VERIFIER LE PB DU DERNIER RETOUR CHARIOT
	public void  writeFileEnd() {
		out.flush();
	}
	
	String convertVel (float fvel) {
		if (!sameIntensity ((int)fvel,(int)currentVel)) {
			currentVel = fvel;
			return  "\\" + "intens<"+ "\"" + convertInt ((int)currentVel)+ "\"" + "," + (float)currentVel/127 + ">";
		}else 
			return "";
	}
	
	String convertInt(int vel) { return intensTable[(int)vel/26];}
	
	boolean sameIntensity(int vel , int curvel){ return ((vel/26) == (curvel/26));}
			
	// determine la key la mieux appropriée
	
	 String calcKey (int seq, int voice) {
	 	
		int cur = Midi.GetFirstEv(seq);
		int sup60 = 0;
		int inf60 = 0;
		
		
		while (cur != 0) {
			if (Midi.GetRefnum(cur) == voice) {
				if (Midi.GetField(cur,0) >= 60) sup60++; else inf60++;
			}
			cur = Midi.GetLink(cur);
		}
		return  (sup60 >= inf60) ? "g": "f";
	 }
		
	void writeVoice (int seq, int voice) {
	
		int cur = Midi.GetFirstEv(seq);
		int dateEv = 0;
		int curDate = 0;
		
		out.print ("[");
		noteConverter.setDefault(octaveOffset, defaultDur); // met les valeurs par défaut
		writeVoiceHeader("4/4", calcKey(seq,voice), "C", "1/4=60");
		
		while (cur != 0) {
			if (Midi.GetRefnum(cur) == voice) {
				dateEv = Midi.GetDate(cur);
				if (dateEv > curDate) {
					out.print(" ");
					out.print (noteConverter.convertSilence((float)(dateEv - curDate))); 			// ecrit un silence
				}
				out.print(convertVel((float)Midi.GetField(cur,1)));
				out.print(" ");
 				out.print (noteConverter.convertNote((float)Midi.GetField(cur,0)	
 										,(float)Midi.GetField(cur,2)));	// ecrit la note
 				curDate = dateEv + Midi.GetField(cur,2);
			}
			
			cur = Midi.GetLink(cur);
		}
		
		out.println ("]");
	}
	
	public void writeVoices (TMultiSeqResult res) {
		int i = 0;
		out.print ("{");   			// Debut de Mix
		
		while (res.isVoice(i) && (i < 255)) {
			if (i > 0) out.print (",");		// Séparateur de voies pour pistes > 0
			writeVoice(res.getSeq(),i);		
			i++;
		}
		
		out.println ("}");			// Fin de Mix
	}
		
	
	void  writeVoiceHeader(String meter, String clef,String key, String tempo) {
	 	out.println ( " " + "\\"  + "title<" + "\"" + "Voice" + voice + "\"" + "> " );
	 	out.print (" " + "\\" + "meter<" + "\"" + meter + "\"" + "> ");
	 	out.print (" " + "\\" + "clef<" + "\"" + clef + "\"" + "> ");
	 	out.println (" " + "\\" + "key<" + "\"" + key + "\"" + "> ");
	 	out.println (" " + "\\" + "tempo<"  + "\"" + "tempo" + "\"" +  "," + "\"" + tempo + "\"" + "> ");
	 	voice++;
	}
}
