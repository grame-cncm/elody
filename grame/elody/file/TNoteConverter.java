/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.file;

import grame.elody.util.TRational;

import java.util.Enumeration;
import java.util.Vector;


/*******************************************************************************************
*
*	 TNoteConverter (classe) : convertion de notes en texte.
* 
*	 - convertion de hauteur au format: notename dans une octave + numéro d'octave
*    - quantification des dates absolues et convertion en fraction de ronde.                   
*	 - Constructeur avec boolean : si true, maintien d'un contexte de durée et octave courant
*      (pour la génération Guido)
*
*******************************************************************************************/

public final class TNoteConverter {
	static String noteName[] = {"c","c#","d","d#","e","f","f#","g","g#","a","a#","b"};
	static int durTable[];
	
	static TRational durTable1[] = { 
		new TRational(3,2), new TRational(1,1), new TRational(3,4),
		new TRational(1,2), new TRational(3,8), new TRational(1,4),
		new TRational(3,16), new TRational(1,6), new TRational(1,8),
		new TRational(3,32), new TRational(1,12), new TRational(1,16),
		new TRational(1,24), new TRational(1,32), new TRational(1,64),
	};
	
	//static float wholeDur = 4000;
	
	// Etat courant
	float currentDur; 	// correspond à 1 ronde
	int currentOctave;
	int octaveOffset = 0;
	
	boolean state = false;
	
	
	public TNoteConverter (int octave_offset,  int whole_dur, int default_dur, boolean state) { 
		this.state = state; 
		durTable = calcDurTable(whole_dur);
		setDefault(octave_offset, default_dur);
	}

	// Fonctions internes 
	
    int[] calcDurTable(float w) {
		int durTable[] = new int[durTable1.length];
		for (int i = 0; i < durTable1.length; i++) {
			durTable[i] = (int) (durTable1[i].toFloat() * w);
		}
		return  durTable;
	}
	
	 int getInf (int val) {
		for (int i = 0; i < durTable.length-1; i++) {
			if (val >=  durTable[i+1]) return durTable[i+1];
		}
		return 0;
	}
	
	 int getSup (int val) {
		for (int i = 0; i < durTable.length-1; i++) {
			if (val >=  durTable[i+1]) return durTable[i];
		}
		return 0;
	}
	
	
	TRational getRational (int val) {
		for (int i = 0; i < durTable.length; i++) {
			if (val ==  durTable[i]) return durTable1[i];
		}
		return new TRational(1,1);
	}
		
	Vector<TRational> quantizeSymbAux(int v, Vector<TRational> res, int limit) {
		int d1 = getSup(v);
		int d2 = getInf(v);
		if ((v - d2) < limit) {
			if ((v - d2) <= (d1 - v)) {
				res.addElement(getRational(d2));
				return res;
			}else {
				res.addElement(getRational(d1));
				return res;
			}
		}else if ((d1 - v) < limit) {
				res.addElement(getRational(d1));
				return res;
		}else {
			res.addElement(getRational(d2));
			return quantizeSymbAux( v - d2, res, limit);
		}
	}
	
	Vector<TRational> quantizeSymb(int val , int limit) {
		Vector<TRational> res = new Vector<TRational>();
		
		if (val < limit) {
			if (val < (limit / 2)) {
				return res;
			}else {
				res.addElement(getRational(limit));
				return res;
			}
		}else {
			int quotient = val/durTable[0];
			int reste = val%durTable[0];
			for (int i = 0 ; i< quotient; i++) {
				res.addElement(durTable1[0]);
			}
			return quantizeSymbAux( reste, res, limit);
		}
	}
	
	
	public String convertPitch (float fpitch) {
		int pitch = (int) fpitch;
		//int octave = pitch/12 - 4;
		int octave = pitch/12 - octaveOffset;
		if (currentOctave != octave) {
			if (state) currentOctave = octave;
			return (noteName[pitch%12]+ Integer.toString(octave));
		}else{
			return noteName[pitch%12];
		}
	}
	
	
	String STEPHconvertDur (float fdur) {
		if (fdur != currentDur) {
			if (state) currentDur = fdur;
			Vector<TRational> vector =  quantizeSymb((int) fdur, durTable[durTable.length -1]);
			TRational res = new TRational(0,1);
			for (Enumeration<TRational> e = vector.elements() ; e.hasMoreElements() ;){ 
				res  = TRational.Add(res, e.nextElement());
			} 
			return  "*" + res.toString();
		}else {
			return "";
		}
	} 
	
	
	String convertDur (float fdur) {
		if (fdur != currentDur) {
			if (state) currentDur = fdur;
		/*	Vector vector =  quantizeSymb((int) fdur, durTable[durTable.length -1]);
			TRational res = new TRational(0,1);
			for (Enumeration<TRational> e = vector.elements() ; e.hasMoreElements() ;){ 
				res  = TRational.Add(res, e.nextElement());
			} 
			if (res.num() == 1) {
				return "/" + res.denom();
			} else if (res.denom() == 1) {
				return "*" + res.num();
			} else {
				return  "*" + res.toString();
			}
		*/
		// =====>>>> NO MUSICAL TIME, BUT ABSOLUTE TIME !
			return "  d="+(int)fdur;
		}else {
			return "";
		} 
	}
	
	// Fonctions externes 
	
	public String convertNote (float pitch, float dur) {
		return (convertPitch(pitch) + convertDur(dur));
	}
	
	public String convertSilence(float dur) {
		return ("_" + convertDur(dur));
	}
	
	// Met les valeurs par défaut
	public void setDefault(int octave_offset, int dur) {
		currentDur = dur; 
		currentOctave = 1; // valeur par defaut de l'octave
		octaveOffset = octave_offset;
		
	}
}
