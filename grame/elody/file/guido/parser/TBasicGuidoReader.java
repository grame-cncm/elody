/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.file.guido.parser;

import grame.elody.lang.TExpMaker;
import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.util.TRational;

import java.awt.Color;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.StringTokenizer;


public class TBasicGuidoReader extends BasicGuidoReader {

	/* rtp auto increase on note append on/off */
	boolean rtp_auto_inc;


	/* --- state variables: --- */

	/* current segment properties: */
	int sg_num_voices = 0; 	/* voices counter */
	int sg_num_notes = 0; 	/* notes counter */
	int sg_num_tags = 0; 	/* tags counter */

	/* rel. dur. of current segment: */
	int sg_enum = 0;
	int sg_denom = 1;


	/* current sequence (voice) properties: */
	int sq_num_notes = 0;  /* note counter */
	int sq_num_tags = 0;   /* tag counter */
	int sq_max_voices = 0; /* max chord voices counter */

	/* rel. time pos within current voice: */
	int rtp_enum = 0;
	int rtp_denom = 1;
	
	/* rel. time pos within current voice before last note: */
	int last_rtp_enum = 0;
	int last_rtp_denom = 1;

	/* current note properties: */
	int nt_dotMode=0;	/* number of dots seen */
	int nt_enumSet=0; /* enum of rel.dur explicitely specified? */

	/* root pc, octave and accidentals of current / last note: */
	int nt_pc=0;
	int nt_oct=1;
	int nt_acc=0;

	/* rel. dur. of current / last note: */
	int nt_enum=0;
	int nt_denom=1;

	/* current chord properties: */
	int ch_num_notes=0;	/* number notes in chord */

	/* rel. dur. of current chord: */
	int ch_enum=0;
	int ch_denom=1;
	
		
	/* state variables and constants for tag processing: */

	
	int activeTag;
	int numTagArgs;

	static Hashtable<String, Integer> noteNames,tagNames;
	Stack<TExp>  tagStack,eventStack,seqStack,chordStack;
	
	int midiChannel = 0;
	int track = 0;
	double intensity = 0.8;
	double masterTempo = 60.0;
	double quartersPerMin = 60.0;
	
	static final int REST = -1;
	static final int T_UNKNOWN = 0;
	static final int T_INTENS = 1;
	static final int T_TEMPO = 3;
	
	public TExp result = TExpMaker.gExpMaker.createNull();
	
	public TBasicGuidoReader() {
	
		noteNames = new Hashtable<String, Integer>();
		tagNames = new Hashtable<String, Integer>();
		
		tagStack = new Stack<TExp>();
		eventStack = new Stack<TExp>();
		seqStack = new Stack<TExp>();
		chordStack = new Stack<TExp>();
		
		noteNames.put("c", new Integer(0));
		noteNames.put("do", new Integer(0));
		noteNames.put("cis", new Integer(1));
		
		noteNames.put("d", new Integer(2));
		noteNames.put("re", new Integer(2));
		noteNames.put("dis", new Integer(3));
		
		noteNames.put("e", new Integer(4));
		noteNames.put("mi", new Integer(4));
		
		noteNames.put("f", new Integer(5));
		noteNames.put("fa", new Integer(5));
		noteNames.put("fis", new Integer(6));
		
		noteNames.put("g", new Integer(7));
		noteNames.put("sol", new Integer(7));
		noteNames.put("gis", new Integer(8));
		
		noteNames.put("a", new Integer(9));
		noteNames.put("la", new Integer(9));
		noteNames.put("ais", new Integer(10));
		
		noteNames.put("b", new Integer(11));
		noteNames.put("si", new Integer(11));
		noteNames.put("h", new Integer(11));
		noteNames.put("ti", new Integer(11));
		
		noteNames.put("_", new Integer(REST));
		
		tagNames.put("\\i", new Integer(1));
		tagNames.put("\\intens", new Integer(1));
		tagNames.put("\\tempo", new Integer(3));
	}

	public void GD_INIT_SEGM() { 
		//System.out.println("GD_INIT_SEGM");
		
		sg_enum = 0;
		sg_denom = 1;

		sg_num_voices = 0;
		sg_num_notes = 0;
		sg_num_tags = 0;

		//na_segmInit();
	}
		
	public void GD_EXIT_SEGM(){ 
		//System.out.println("GD_EXIT_SEGM");
		
		while( !seqStack.empty()) {
			//System.out.println("POP seq");
			result = TExpMaker.gExpMaker.createMix(seqStack.pop(),result);
		}
	}
	
	public void GD_APP_SEQ(){ 
		//System.out.println("GD_APP_SEQ");
		if (TRational.SupEq(new TRational(rtp_enum,rtp_denom),new TRational( sg_enum, sg_denom))) {
	 		sg_enum = rtp_enum;
	 		sg_denom = rtp_denom;
    	}
  
  		sg_num_voices++;
 		sg_num_notes += sq_num_notes;
  		sg_num_tags += sq_num_tags;
 	}

	public void GD_INIT_SEQ(){
		//System.out.println("GD_INIT_SEQ");
		rtp_enum=0;
	  	rtp_denom=1;

		sq_num_notes=0;
		sq_num_tags=0;
		sq_max_voices=1;

	  	nt_enum=1;
	  	nt_denom=4;
	  	nt_dotMode=0;
	  	nt_enumSet=0;
	  	nt_oct=1;

		rtp_auto_inc=true;
		//na_seqInit();
	}
	
	public void GD_EXIT_SEQ(){ 
		//System.out.println("GD_EXIT_SEQ");
		TExp res = TExpMaker.gExpMaker.createNull();
		
		while( !eventStack.empty()) {
			//System.out.println("POP event");
			res = TExpMaker.gExpMaker.createSeq(eventStack.pop(),res);
		}
		seqStack.push(res);
	}

	public void GD_NT(String s){ 
		nt_pc = convertNoteName(s);
		nt_acc=0;
	}
	
	public void GD_SH_NT(){ 
		//System.out.println("GD_SH_NT");
	 	nt_acc++;
	}
	
	public void GD_FL_NT(){  
		//System.out.println("GD_FL_NT"); 
		nt_acc--;
	}
	
	public void GD_OCT_NT(String s){ nt_oct = Integer.parseInt(s);}
	
	public void GD_ENUM_NT(String s){
	 	nt_enum = Integer.parseInt(s);
 	 	nt_denom = 1;
  		nt_enumSet = 1;
  		nt_dotMode = 0;
  	}
  	
	public void GD_DENOM_NT(String s){
		nt_denom = Integer.parseInt(s);
  		if(nt_enumSet == 0) nt_enum =1;
  		nt_dotMode = 0;
  	}
	
	public void GD_DOT_NT(){
		//System.out.println("GD_DOT_NT");
		 nt_dotMode++;
	}
	
	
	public void GD_DDOT_NT(){ 
		//System.out.println("GD_DDOT_NT");
	}

	public void GD_APP_NT(){ 
		int d,e;
	
		switch (nt_dotMode) {
			case 1: e = nt_enum *3; d = nt_denom *2; 	//System.out.println("GD_DOT_NT1");
	 	 break;
	 		case 2: e = nt_enum *7; d = nt_denom *4; 	//System.out.println("GD_DOT_NT2");
	 	break;
	 		default: e=nt_enum; d=nt_denom;
	 	}
		
		//TRational res = new TRational(e,d);

		eventStack.push(na_seqMakeNote(nt_pc,nt_oct,nt_acc,nt_enum,nt_denom,nt_dotMode, rtp_enum, rtp_denom));

		if(rtp_auto_inc) {
			last_rtp_enum = rtp_enum;
			last_rtp_denom = rtp_denom;
			TRational res =  TRational.Add(new TRational(rtp_enum, rtp_denom) , new TRational(e,d));
			rtp_enum = res.num();
			rtp_denom = res.denom();
		}

  		nt_enumSet=0;
		sq_num_notes++;
	}

	public void GD_INIT_CH(){
		ch_enum = 0;
		ch_denom = 1;
		ch_num_notes = 0;
	}
	
	
	public void GD_CH_APP_NT(){ 
	 	int d,e;
		 
		switch (nt_dotMode) {
			case 1: e = nt_enum *3; d = nt_denom *2; break;
			case 2: e = nt_enum *7; d = nt_denom *4; break;
		 	default: e=nt_enum; d=nt_denom;
		}

		chordStack.push(na_seqMakeNote(nt_pc,nt_oct,nt_acc, nt_enum,nt_denom,nt_dotMode,rtp_enum, rtp_denom));

		TRational res = new TRational(e,d);
		
		if (TRational.Sup(res, new TRational(ch_enum, ch_denom))){
		 	//ch_enum=e;
		 	//ch_denom=d;
		 	ch_enum = res.num();
		 	ch_denom = res.denom();
		}

	  	nt_enumSet=0;
	  	ch_num_notes++;
  	}
  	
	public void GD_APP_CH(){ 
		//gd_fracAdd(rtp_enum, rtp_denom, ch_enum, ch_denom);
		TRational res = TRational.Add(new TRational(rtp_enum,rtp_denom), new TRational(ch_enum, ch_denom));
		rtp_enum = res.num();
		rtp_denom = res.denom();

  		sq_num_notes += ch_num_notes;
  		if (ch_num_notes > sq_max_voices)	sq_max_voices = ch_num_notes;
  		
  		TExp res1 = TExpMaker.gExpMaker.createNull();
		
		while( !chordStack.empty()) {
			//System.out.println("POP event");
			res1 = TExpMaker.gExpMaker.createMix(chordStack.pop(),res1);
		}
		eventStack.push(res1);
  	}


	public void GD_TAG_START(String s){ 
		//System.out.println("GD_TAG_START");
		
		Integer res = tagNames.get(s);
		if (res !=  null) {
			activeTag = res.intValue();
		}else {
			activeTag = T_UNKNOWN;
		}
		numTagArgs = 0;
	}
	
	
	public void GD_TAG_END(){
		 //System.out.println("GD_TAG_END");
		 
		 activeTag = T_UNKNOWN;
 		 numTagArgs = 0;
	}
	
	public void GD_TAG_NARG(String s){
		//System.out.println("GD_TAG_NARG");
		 
		numTagArgs++;
  		numTagArgs++;
	}
	
	public void GD_TAG_RARG(String s){ 
		float r =  Float.valueOf(s).floatValue();
		numTagArgs++;
	  	
 		if(activeTag == T_INTENS && numTagArgs == 2) {
 		  	if (r < 0.0 || r > 1.0) {
	      		System.out.println("Warning: GUIDO intensity value out of range, ");
	      		System.out.println("ignored.\n");
	      	}else{
	     		intensity = r;
	     	}
		}
	}
	public void GD_TAG_SARG(String s){ 
		//System.out.println("GD_TAG_SARG");
		numTagArgs++;
		
	  	if(activeTag == T_TEMPO && numTagArgs==2) {
	  		float tempo = scanTempoParam2(s);
	  		if (tempo <= 0) {
      			System.out.println("Warning: unknown or out-of-range tempo parameter, ");
        		System.out.println("ignored.\n");
        	}else {
       	 		
	   			if (masterTempo == 0.0)   masterTempo = tempo;  else  quartersPerMin = tempo;
	    	}
		}
	}
	
	public void GD_TAG_TARG(String s){ 
		//System.out.println("GD_TAG_TARG");
	}  /* don't use this! */
	public void GD_TAG_ADD_ARG(){ 
		//System.out.println("GD_TAG_ADD_ARG");
	}     /* don't use this! */
	public void GD_TAG_ADD(){ 
		//System.out.println("GD_TAG_ADD");
	}
	
	
	int convertNoteName(String s) {
		Integer res = noteNames.get(s);
		if (res == null) {
			System.out.println("Unknown notename, replaced by rest.");
			res = noteNames.get("_");
		}
		return res.intValue();
	}
	
	
	TExp na_seqMakeNote(int pc, int oct, int acc, int durN, int durD, int dots,int durPosN, int durPosD) {
	  int pitch, chan, vel;
	  double duration,dotDuration,dotStart,start;

	  chan = midiChannel+track-1;
	  vel = (int)(127.0*intensity);
	  
	  /* get duration in milli seconds: */
	  duration = (double) (masterTempo/quartersPerMin)*4*durN/durD*1000;
	  
	  /* new by jk 98/06/02 */
 	  /* calculate dotted length */
  		start = 1.0*durPosN/durPosD;
  		dotDuration = duration * 0.5;
  		dotStart = start * 0.5;
  		for( int i = 0; i < dots; i++ ){
        	 duration += dotDuration;
        	 start += dotStart;
         	dotDuration *= 0.5;
         	dotStart *= 0.5;
  		}
	  /* end of changes */


	  /* get midi key number: */
	  if (pc != REST){
		  pitch = 48 + (oct * 12 + pc) + acc;
		  return TExpMaker.gExpMaker.createNote(Color.blue, pitch,vel,chan,(int)duration);
	  }else {  /* rests are mapped onto c1, vel=0 */
		  return TExpMaker.gExpMaker.createSilence( Color.blue, 0,0,0,(float)duration);
	  }
	}
	
	float scanTempoParam2(String s) {
		  /* returns 4*x/y*n, if s (2nd param of \tempo tag) == "x/y=n"
		     -1.0 otherwise */
		 s = s.substring(1,s.length()-1); // String de la forme ""1/4=60"" , supprime les ""
		 //System.out.println(s);
         StringTokenizer st = new StringTokenizer(s, "/=");
		   try {
		   		float x = Integer.parseInt(st.nextToken());
             	float y = Integer.parseInt(st.nextToken());
             	float n = Integer.parseInt(st.nextToken());
             	//System.out.println( " x y n " + x + y + n);
             	return(4*x/y*n);
		   }catch ( NoSuchElementException e) {
		   		System.out.println( "Tempo syntax error" + s);
		   		return -1;
		  }
	 }

	
	public Object getExp() {return result;}
}
