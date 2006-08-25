package grame.elody.file.midifile;

import grame.elody.lang.TExpMaker;
import grame.elody.lang.texpression.expressions.TExp;
import grame.midishare.Midi;
import grame.midishare.MidiException;
import grame.midishare.tools.MidiSequence;

import java.awt.Color;
import java.util.Enumeration;
import java.util.Vector;


/*******************************************************************************************
*
*	 TMIDIFile (classe) : conversion d'expressions Elody en séquence MidiShare et vice versa
*                         methodes publiques : writeExp, readExp, readTrack
* 
*******************************************************************************************/

public final class TMIDIFile {
	static Color tableColor[] = { Color.orange, Color.red, Color.pink,
			Color.magenta, Color.green, Color.cyan, Color.blue, Color.gray,
			Color.black };

	// Methodes publiques

	public int writeExp(TExp exp) throws MidiException {

		TSeqVisitor writer = new TSeqVisitor();
		// TMultiSeqVisitor writer = new TMultiSeqVisitor();

		int seq = Midi.NewSeq();
		int ev = Midi.NewEv(Midi.typeEndTrack);

		if ((seq == 0) || (ev == 0))
			throw new MidiException("No more MidiShare events");

		writer.fillSeq(exp, seq);

		// très important pour la sauvegarde MidiFile
		if (Midi.GetLastEv(seq) != 0) {
			Midi.SetDate(ev, Midi.GetDate(Midi.GetLastEv(seq)));
		} else {
			Midi.SetDate(ev, 0);
		}
		Midi.AddSeq(seq, ev);
		return seq;
	}

	public TExp readExp(int seq) throws MidiException {

		MidiSequence.MatchKey(seq); // transformation des couples keyOn-keyOff
									// en Notes
		checkSeq(seq);

		int copy = copyNotes(seq); // copie des notes

		TExp res = parseTrack2(copy);
		Midi.FreeSeq(copy);
		return res;
	}

	public TExp readTrack(int seq, int tracknum) throws MidiException {

		MidiSequence.MatchKey(seq); // transformation des couples keyOn-keyOff
									// en Notes
		checkSeq(seq);
		int copy = copyNotes(seq); // copie des notes

		TExp res = parseTrack2(copy);
		Midi.FreeSeq(copy);
		return res;
	}

	// Methodes Internes

	// Demixe une sequence en plusieurs sous pistes

	void analyseSeq(int seq, TSeqTable table, int tracknum)
			throws MidiException {
		int next, cur = Midi.GetFirstEv(seq);

		while (cur != 0) {
			next = Midi.GetLink(cur); // car on insere l'ev et non une copie
			if (Midi.GetRefnum(cur) == tracknum)
				insertNote(cur, table);
			cur = next;
		}
	}

	void insertNote(int note, TSeqTable table) throws MidiException {
		int last, i, date = Midi.GetDate(note);

		for (i = 0; i < table.countseq; i++) {
			int seq = table.seqtable[i];
			last = Midi.GetLastEv(seq); // il y a toujours au moins un ev
			if (date >= Midi.GetDate(last) + Midi.GetField(last, 2)) {
				Midi.AddSeq(seq, Midi.CopyEv(note));
				return;
			}
		}

		int seq = Midi.NewSeq();
		if (seq == 0)
			throw new MidiException("No more MidiShare events");
		Midi.AddSeq(seq, Midi.CopyEv(note));
		table.seqtable[table.countseq] = seq;
		table.countseq++;
		if (table.countseq == 512)
			throw new MidiException("No more free line in seqTable");
	}

	void checkSeq(int seq) {
		int prev, cur;

		cur = Midi.GetFirstEv(seq);
		prev = Midi.GetFirstEv(seq);

		while (cur != 0) {
			prev = cur;
			cur = Midi.GetLink(cur);
		}
		if (prev != Midi.GetLastEv(seq))
			throw new StackOverflowError();
	}

	// rend une copie avec seulement les notes

	int copyNotes(int seq) throws MidiException {
		int cur = Midi.GetFirstEv(seq);
		int res = Midi.NewSeq();

		if (res == 0)
			throw new MidiException("No more MidiShare events");

		while (cur != 0) {
			if (Midi.GetType(cur) == Midi.typeNote)
				Midi.AddSeq(res, Midi.CopyEv(cur));
			cur = Midi.GetLink(cur);
		}
		return res;
	}

	TExp parseTrack2(int seq) throws MidiException {
		TExp track, res = TExpMaker.gExpMaker.createNull();
		TSeqTable table;

		for (int num = 0; num < 256; num++) {
			table = new TSeqTable();
			analyseSeq(seq, table, num);

			if (table.countseq > 0) {
				track = TExpMaker.gExpMaker.createNull();
				for (int i = 0; i < table.countseq; i++) {
					track = TExpMaker.gExpMaker.createMix(track, parseSeq(Midi
							.GetFirstEv(table.seqtable[i]), 0));
					Midi.FreeSeq(table.seqtable[i]);
				}
				res = TExpMaker.gExpMaker.createMix(track, res);
			}
		}
		return res;
	}

	/*
	 * // Recursif TExp parseSeq(int curNote, int date) { if (curNote == 0)
	 * return TExpMaker.gExpMaker.createNull();
	 * 
	 * if (Midi.GetDate(curNote) > date) { return TExpMaker.gExpMaker.createSeq(
	 * TExpMaker.gExpMaker.createSeq(parseSilence (Midi.GetDate(curNote) -
	 * date),parseNote(curNote)), parseSeq(Midi.GetLink(curNote),
	 * Midi.GetDate(curNote) + Midi.GetField(curNote,2))); }else { return
	 * TExpMaker.gExpMaker.createSeq(parseNote(curNote),
	 * parseSeq(Midi.GetLink(curNote), Midi.GetDate(curNote) +
	 * Midi.GetField(curNote,2))); } }
	 */

	TExp parseSeq(int curNote, int date) {
		Vector<TExp> table = new Vector<TExp>();
		Color c = tableColor[Midi.GetRefnum(curNote) % tableColor.length];

		while (curNote != 0) {

			if (Midi.GetDate(curNote) > date) {
				table.insertElementAt(parseSilence(
						Midi.GetDate(curNote) - date, c), 0);
				table.insertElementAt(parseNote(curNote, c), 0);
			} else {
				table.insertElementAt(parseNote(curNote, c), 0);
			}
			date = Midi.GetDate(curNote) + Midi.GetField(curNote, 2);
			curNote = Midi.GetLink(curNote);
		}

		TExp res = TExpMaker.gExpMaker.createNull();

		for (Enumeration<TExp> e = table.elements(); e.hasMoreElements();) {
			res = TExpMaker.gExpMaker.createSeq(e.nextElement(), res);
		}
		return res;
	}

	TExp parseNote(int ev, Color c) {
		return TExpMaker.gExpMaker.createNote(c, Midi.GetField(ev, 0), Midi
				.GetField(ev, 1), Midi.GetChan(ev) + Midi.GetPort(ev) * 16,
				Midi.GetField(ev, 2));
	}

	TExp parseSilence(int dur, Color c) {
		return TExpMaker.gExpMaker.createSilence(c, 0f, 0f, 0f, dur);
	}
}

final class TSeqTable {
	int  seqtable[] = new int[512];
	int  countseq = 0;
}
