package grame.elody.editor.tleditor;

import grame.elody.editor.tleditor.tlevent.TLEvent;
import grame.elody.editor.tleditor.tlevent.TLExpEv;
import grame.elody.editor.tleditor.tlevent.TLRest;
import grame.elody.lang.TEvaluator;
import grame.elody.lang.TExpMaker;
import grame.elody.lang.texpression.expressions.TAbstrExp;
import grame.elody.lang.texpression.expressions.TApplExp;
import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.lang.texpression.expressions.TMixExp;
import grame.elody.lang.texpression.expressions.TNamedExp;
import grame.elody.lang.texpression.expressions.TNullExp;
import grame.elody.lang.texpression.expressions.TSequenceExp;
import grame.elody.util.TExpRenderer;

import java.awt.Color;
import java.util.Hashtable;

public final class TLConverter {

	// outils pour gerer les fonctions identités et les silences à ignorer
	//--------------------------------------------------------------------
	static final TEvaluator eval = TEvaluator.gEvaluator;
	static final TExpMaker 	emk = TExpMaker.gExpMaker;
	static final Color 		ignoreColor = new Color(0,0,0);
	static final String 	ignoreName = "_IGNORE_";
	static final String 	mixFunTrack = "_MixFunTrack_";
	static final String 	seqFunTrack = "_SeqFunTrack_";
	static final TExp 		xn = emk.createNote(ignoreColor, 0, 0, 0, 1000);
	static final TExp 		id = emk.createIdent(xn);
	static final TExp       sil =  createIgnoreSil(1000);

	static final TExp 		evalLimit = emk.createNote(ignoreColor, 0, 0, 0, 200000);
	static final int 		reifyLimit = 30000;
	
	static final Hashtable mixExpTable =  new Hashtable();
			
	// outils pour gerer les fonctions identités et les silences à ignorer
	//--------------------------------------------------------------------
	private static TExp createIgnoreIdentity (float dur) 
	{
		return emk.createNamed( emk.createLambda(dur, id, xn, id), ignoreName ) ;
	}
	
	private static TExp createIgnoreSil (float dur) 
	{
		return emk.createNamed( emk.createLambda(dur, id, xn, emk.createSilence(ignoreColor, 0, 0, 0, dur)), ignoreName ) ;
	}
	
	private static TExp createIgnoreRest (float dur) 
	{
		return emk.createNamed( emk.createSilence(ignoreColor, 0, 0, 0, dur), ignoreName ) ;
	}
	
/*	private static TExp createIgnoreRest1 (float dur) 
	{
		return emk.createSilence(ignoreColor, 0, 0, 0, dur) ;
	} */
	
	private static boolean shouldIgnore (TExp e)
	{
		return (e instanceof TNamedExp) && ((TNamedExp)e).getName().equals(ignoreName);
	}

	
	
	// fonctions publiques de conversion : TExp -> TL
	//--------------------------------------------------
	
	// TLConverter.multi(TExp) 		-> TLMultiTracks
	// TLConverter.track(TExp) 		-> TLTrack
	// TLConverter.funtrack(TExp) 	-> TLTrack
	// TLConverter.event(TExp)		-> TLEvent
	
	public static TLMultiTracks multi(TExp e) 
	{ 
		return exp2Multi(e, new TLMultiTracks(), true); 
	}
		
	public static TLTrack track (TExp e)
	{
		return exp2Track(e, new TLTrack());
	}	
	
	public static TLTrack seqfuntrack (TExp e)
	{
		return exp2Track(e, (new TLTrack()).setTrackMode(TLTrack.SEQFUNCTION));
	}	
	
	public static TLTrack mixfuntrack (TExp e)
	{
		return exp2Track1(e, (new TLTrack()).setTrackMode(TLTrack.MIXFUNCTION));
		
	}	
	
	public static TLTrack abstrack (TExp e)
	{
		return exp2Track(e, (new TLTrack()).setTrackMode(TLTrack.ABSTRACTION));
	}	
	
	public static TLEvent event (TExp e)
	{
		if (shouldIgnore(e)) {
			return new TLRest(e);
		} else {
			return new TLExpEv(e);
		}
	}


	// fonctions publiques de conversion : TL -> TExp
	//--------------------------------------------------
	
	// TLConverter.exp(TLMultiTracks) 	-> TExp
	// TLConverter.exp(TLTrack)			-> TExp
	// TLConverter.exp(TLEvent) 		-> TExp
	// TLConverter.fun(TLEvent) 		-> TExp
	
	public static TExp exp (TLMultiTracks mt)
	{
		TExp 			rslt 	= emk.createNull();		// résultat général
		TExp 			appl 	= emk.createNull();		// groupe applicatif intermédiare
		
		mt.at(mt.getCount());
		
		while (mt.prev()) {
		
			TLTrack tr = mt.getTrack();
			if (! tr.getMuteFlag()) {
				if (tr.getFullDur() == 0) {
					rslt = emk.createMix(appl, rslt);
					appl = emk.createNull();
					
				} else if (tr.isSeqFunTrack()) {
					appl = emk.createAppl(expSeqWithIdentity(tr), appl);
				
				} else if (tr.isMixFunTrack()) {
					appl = emk.createAppl(expMixWithIdentity(tr), appl);
				
				} else if (tr.isParamTrack()) {
					TExp foo = collectParamTracks(mt, exp(tr));
					appl = emk.createAppl(foo, appl);
					
				} else if (tr.isAbstrTrack()) {
					rslt = emk.createAbstr(exp(tr), emk.createMix(appl, rslt));
					appl = emk.createNull();
					
				} else {
					appl = emk.createMix(exp(tr), appl);
				}
			}
		}
		return emk.createMix(appl, rslt);
	}
	
	private static TExp collectParamTracks (TLMultiTracks mt, TExp p)
	{
		if (mt.prev()) {
			TLTrack tr = mt.getTrack();
			if (tr.isParamTrack()) {
				return emk.createAppl(collectParamTracks(mt, exp(tr)),p);
			} else if (tr.isSeqFunTrack()) {
				return emk.createAppl(exp(tr),p);
			} else if (tr.isMixFunTrack()) {
					return emk.createAppl(exp(tr),p);
			} else {
				return emk.createAppl(exp(tr),p);
			}
		} else {
			return p;
		}
	}
	
	
	public static TExp expSeqWithIdentity (TLTrack t)
	{
		final TExpMaker mk = TExpMaker.gExpMaker;
		TExp 			result;
		
		t.at(t.getFullDur());
		result = mk.createNull();
		
		if (t.isSeqFunTrack()) {
			// conversion d'une séquence de fonctions
			while (t.prev()) { result = mk.createSeq(funSeq(t.getEvent()), result); }
			
			// 2/10/2000 : Correction bug abstraction 
			// La fonction identité de droite est ajoutée à la fin 
			// (de mani?re à ce que f1,f2,f3,sil soit transformé en [f1 : [f2 : f3]]:Id
			// ainsi l'objet [f1 : [f2 : f3]] peut être abstrait)

			result =  mk.createSeq(result, createIgnoreIdentity(1000));
			
		} else {
			// conversion d'une séquence normale
			while (t.prev()) { result = mk.createSeq(exp(t.getEvent()), result); }
		}
			
		return emk.createNamed(result, seqFunTrack); // Nomme la piste (pour permettre la déconstruction)
	}
	
	
	public static TExp expMixWithIdentity (TLTrack t)
	{
		final TExpMaker mk = TExpMaker.gExpMaker;
		TExp 			result;
		int 			curDate = 0;
		
		if (t.isMixFunTrack()) {
			// conversion d'une séquence de fonctions en un Mix
			t.at(0); // au début
			result = mk.createNull();
			do  {
				// Construit [ABS : FUN : ABS] ou ABS est une fonction à un argument qui rend une silence de la durée de son argument
				result = mk.createMix(funMix(t.getEvent(), curDate), result);
				curDate += t.getEvent().fDur;
			}while (t.next());
		
			// Ajoute une piste qui contient une fonction identité pour toute la durée qui reste
			result = mk.createMix(funMix(new TLRest(600000), curDate), result);
			
		} else {
			// conversion d'une séquence normale
			t.at(t.getFullDur()); // à la fin
			
			result = mk.createNull();
			while (t.prev()) {
				result = mk.createSeq(exp(t.getEvent()), result);
			}
		}
		
		return emk.createNamed(reverseMix(result, emk.createNull()), mixFunTrack); // Nomme la piste (pour permettre la déconstruction)
		
	}
		
	public static TExp exp (TLTrack t)
	{
		final TExpMaker mk = TExpMaker.gExpMaker;
		TExp 			result;
		
		t.at(t.getFullDur());
		
		if (t.isSeqFunTrack() || t.isMixFunTrack()) {
			// conversion d'une séquence de fonctions (sans IDENTITY à la fin)
			result = mk.createNull(); //createIgnoreIdentity(1000);
			while (t.prev()) {
				result = mk.createSeq(funSeq(t.getEvent()), result);
			}
		} else {
			// conversion d'une séquence normale
			result = mk.createNull();
			while (t.prev()) {
				result = mk.createSeq(exp(t.getEvent()), result);
			}
		}
		return result;
	}
	
	public static TExp exp (TLEvent e)
	{
		if (e instanceof TLRest) {
			return createIgnoreRest(e.fDur);
		} else {
			return e.getExp();
		}
	}
	
	public static TExp funSeq (TLEvent e)
	{
		if (e instanceof TLRest) {
			return createIgnoreIdentity(e.fDur);
		} else {
			return e.getExp();
		}
	}
	/*
	public static TExp funMix (TLEvent e, int date)
	{
		if (e instanceof TLRest) {
			return emk.createSeq(createIgnoreSil(date), emk.createSeq(createIgnoreIdentity(e.fDur), createIgnoreSil(1000)));
		} else {
			return emk.createSeq(createIgnoreSil(date), emk.createSeq(e.getExp(), createIgnoreSil(1000)));
		}
	}
	*/
	
	
	public static TExp funMix (TLEvent e, int date)
	{
		TExp e1,e2;
		if (e instanceof TLRest) {
			e1 = emk.createSeq(createIgnoreIdentity(e.fDur), sil);
		} else {
			e1 = emk.createSeq(e.getExp(),sil);
		}
		
		if ((e2 = (TExp)mixExpTable.get(e1)) == null) {
			mixExpTable.put(e1,e1);
			//System.out.println("Put in table");
			return emk.createSeq(createIgnoreSil(date), e1);
		}else{
			//System.out.println("Get from table");
			return emk.createSeq(createIgnoreSil(date), e2);
		}
	}

	// évaluation symbolique
	//---------------------------------
	
	public static TExp symbolicEval (TExp exp)
	{
		//return eval.Reify(eval.Eval(exp), reifyLimit);
		return TExpRenderer.gExpRenderer.oneStepEval(exp);
	}
	
	public static TExp reify (TExp exp)
	{
		return eval.Reify(eval.Eval(exp), reifyLimit);
	}

	// application symbolique
	//---------------------------------
	
/*	public static TExp symbolicApply (TExp fun, TExp arg)
	{
		return symbolicEval(emk.createAppl(fun,arg));
	}
*/
	// fonctions internes de conversion
	//---------------------------------
	
	private static TLMultiTracks exp2Multi(TExp e, TLMultiTracks mt, boolean blankflag)
	{
		if (e instanceof TMixExp) {
		
			exp2Multi(e.getArg2(), mt, blankflag);
			exp2Multi(e.getArg1(), mt, blankflag);
			
		} else if (e instanceof TApplExp) {
			
			if (blankflag) mt.insert(new TLTrack());
			exp2Multi(e.getArg2(), mt, false);
		
			// Decompose la fonction en SeqFunctionTrack ou MixFunctionTrack en fonction du nom
			if (e.getArg1() instanceof TNamedExp){
				if (((TNamedExp)e.getArg1()).getName().equals(seqFunTrack))
					mt.insert(seqfuntrack(e.getArg1().getArg1()));
				else if (((TNamedExp)e.getArg1()).getName().equals(mixFunTrack))
					mt.insert(mixfuntrack(e.getArg1().getArg1()));
				else 
					mt.insert(seqfuntrack(e.getArg1()));
			}else {
				mt.insert(seqfuntrack(e.getArg1()));
			}
			
		} else if (e instanceof TAbstrExp) {
		
			if (blankflag) mt.insert(new TLTrack());
			
			TExp ident = e.getArg1();
			TExp body = e.getArg2();
				
			exp2Multi( emk.createBody(ident, body), mt, true );
			mt.insert(abstrack(ident.getArg1()));
			
		} else {
		
			mt.insert(track(e));
		}
		return mt;
	}
	
	// Convertit une piste SeqFunctionTrack 
	
	private static TLTrack exp2Track(TExp e, TLTrack t)
	{
		if (e instanceof TSequenceExp) {
		
			exp2Track(e.getArg2(), t);
			exp2Track(e.getArg1(), t);
			
		} else if (e instanceof TNullExp) {
		
		} else {
			t.insert(event(e));
		}
		return t;
	}
	
	
	// Convertit l'expression en SeqFunctionTrack
	// La fonction convertMFTrack2SFT convertit l'exp MixFunctionTrack en une piste simple (SeqFunctionTrack)
	
	private static TLTrack exp2Track1(TExp e, TLTrack t){ 
		return exp2Track (convertMFTrack2SFT(e),t); 
	}
	
	private static TExp getEventFromSeq(TExp e) { return  e.getArg2().getArg1();}
	
	private static TExp convertMFTrack2SFT(TExp e)
	{
		if (e instanceof TMixExp) {
			return emk.createSeq(getEventFromSeq(e.getArg1()), convertMFTrack2SFT(e.getArg2()));
		}else if (e instanceof TSequenceExp) {
			// Attention : dernier élement à supprimer (contenait la fonction identité pour toute la durée qui reste)
			return emk.createNull();
		}else {
			System.out.println("Error : convertMFTrack2SFT");
			return emk.createNull();
		}
	}
	
/*	private static TExp reverseSeq(TExp e, TExp res)
	{
		if (e instanceof TSequenceExp) {
			return reverseSeq (e.getArg2(), emk.createSeq(e.getArg1(), res));
		}else{
			return emk.createSeq(e, res);
		}
	} */
	
	private static TExp reverseMix(TExp e, TExp res)
	{
		if (e instanceof TMixExp) {
			return reverseMix (e.getArg2(), emk.createMix(e.getArg1(), res));
		}else{
			return emk.createMix(e, res);
		}
	}
}
