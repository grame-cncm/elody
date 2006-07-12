package grame.elody.editor.tleditor;

import grame.elody.editor.misc.TGlobals;
import grame.elody.editor.misc.draganddrop.TExpContent;
import grame.elody.editor.tleditor.tlevent.TLEvent;
import grame.elody.editor.tleditor.tlevent.TLRest;
import grame.elody.lang.TExpMaker;
import grame.elody.lang.texpression.expressions.TExp;

public class TLZone implements TExpContent {
	private static TLTrack			fScrap = null;	// buffer pour couper/copier/coller
	
	private final TLMultiTracks 	fMT;			// le multitrack concerné
	private int						fVoice;			// la ligne concernée
	private int						fStartTime;		// la date de début de la zone
	private int						fEndTime;		// la date de fin de la zone
	private boolean					fInGroup;		// vrai si cette zone est ds un groupe
	
	
	//======================================================================================
	//
	//		Les constructeurs
	//
	//======================================================================================


	public TLZone(TLMultiTracks mt)
	{
		fMT = mt;
		fVoice = 0;
		fStartTime = 0;
		fEndTime = 0;
		fInGroup = false;
	}
	
	public TLZone(TLMultiTracks mt, int time, int line)
	{
		this(mt);
		this.selectEvent(time, line);
	}
	
	public TLZone(TLZone z)
	{
		fMT 		= z.fMT;
		fVoice 		= z.fVoice;
		fStartTime 	= z.fStartTime;
		fEndTime 	= z.fEndTime;
		fInGroup 	= z.fInGroup;		
	}
	
	
	//======================================================================================
	//
	//		Pour debug
	//
	//======================================================================================


	public void print(String msg)
	{
		System.out.println( msg ); 
		System.out.println( " -> zone : " 
							+ ": " + fVoice
							+ ", " + fStartTime
							+ ", " + fEndTime
							+ ", " + fInGroup ); 
	}
	
	
	//======================================================================================
	//
	//		Accès aux champs
	//
	//======================================================================================


	public int start() 			{ return fStartTime; }
	public int end() 			{ return fEndTime; }
	public int duration() 		{ return fEndTime - fStartTime; }
	public boolean empty()		{ return fEndTime == fStartTime ; }
	public boolean inGroup()	{ return fInGroup; }
	
	public int voice() 			{ return fVoice; }
	public int topline() 		{ fMT.index(fVoice); return fMT.getPos(); }
	public int botline() 		{ fMT.index(fVoice+1); return fMT.getPos(); }
	
	
	//======================================================================================
	//
	//		Quelques prédicats
	//
	//======================================================================================


	public boolean contains(int time, int line)
	{
		return (line == fVoice) && (time < fEndTime) && (time >= fStartTime);
	}
	
	public boolean outside (TLZone z)
	{
		return (fVoice != z.fVoice) || (fEndTime <= z.fStartTime) || (fStartTime >= z.fEndTime);
	}
	
	public boolean after (TLZone z)
	{
		return (fVoice == z.fVoice) && (fStartTime > z.fStartTime);
	}
	
	
	//======================================================================================
	//
	//		Positionnement de la Zone
	//
	//======================================================================================


	public void set(TLZone z)
	{
		fVoice 		= z.fVoice;
		fStartTime 	= z.fStartTime;
		fEndTime 	= z.fEndTime;
		fInGroup 	= z.fInGroup;		
	}

	public void selectNothing()
	{
		fVoice = 0;
		fStartTime = 0;
		fEndTime = 0;
		fInGroup = false;
	}

	public void selectEvent(int time, int line)
	{
		if ( time >= 0 && line >= 0 && fMT.at(line) ) {
			fVoice = fMT.getIndex();
			TLTrack t = fMT.getTrack();
			if (t.at(time)) {
				TLEvent e = t.getEvent();
				fStartTime = t.getDate();
				fEndTime = fStartTime + e.fDur;
				fInGroup = t.isCurEvInGroup();
			} else {
				fStartTime = t.getDate();
				fEndTime = fStartTime;
				fInGroup = false;
			}
		}
	}

	public void selectLine(int line)
	{
		if ( fMT.at(line) ) {
			fVoice = fMT.getIndex();
			TLTrack t = fMT.getTrack();
			fStartTime = 0;
			fEndTime = t.getFullDur();
			fInGroup = false;
		}
	}
	
	public void selectFreePoint(int time, int line)
	{
		if ( fMT.at(line) ) {
			fVoice = fMT.getIndex();
			fStartTime = time;
			fEndTime = time;
			fInGroup = false;
		}
	}
	
	public void selectDstPoint(int time, int line)
	{
		if ( time >= 0 && line >= 0 && fMT.at(line) ) {
		
			fVoice = fMT.getIndex();
			TLTrack t = fMT.getTrack();
			t.at(time);
			TLEvent e = t.getEvent();
			int st = t.getDate();
			
			if ( (e == null) || (e instanceof TLRest) ) {
				fStartTime = time;
				fEndTime = time;
				fInGroup = false;
				
			} else if (time <= (st + e.fDur/4)) {
				fStartTime = st;
				fEndTime = st;
				fInGroup = t.isCurDateInGroup();
				
			} else if (time >= (st + e.fDur*3/4)) {
				t.next();  
				fStartTime 	= t.getDate();
				fEndTime 	= fStartTime;
				fInGroup 	= t.isCurDateInGroup();

			} else {
				fStartTime = st;
				fEndTime = st + e.fDur;
				fInGroup = t.isCurEvInGroup();
			}
		}
	}
	
	
	//======================================================================================
	//
	//		Positionnement de la Zone (fonctions curseur)
	//
	//======================================================================================


	// position la zone à la frontière la plus proche
	public void moveTo (int time, int line)
	{
		if ( time < 0) time = 0;
		if (line < 0) line = 0;
		if (line > fMT.getCount()) line = fMT.getCount();
		
		fVoice = line;
		fMT.at(line);
		TLTrack t = fMT.getTrack();
		t.at(time);
		TLEvent e = t.getEvent();
		int st = t.getDate();
		
		if ( (e == null) || (e instanceof TLRest) ) {
			fStartTime = time;
			fEndTime = time;
			fInGroup = false;
			
		} else if (time <= (st + e.fDur/2)) {
			fStartTime = st;
			fEndTime = st;
			fInGroup = t.isCurDateInGroup();
			
		} else {
			t.next();  
			fStartTime 	= t.getDate();
			fEndTime 	= fStartTime;
			fInGroup 	= t.isCurDateInGroup();

		}
	}

	public void extendTo(TLZone z)
	{
		if (fVoice == z.fVoice && ! z.empty()) {
			if (z.fStartTime < fStartTime) fStartTime = z.fStartTime;
			if (z.fEndTime > fEndTime) fEndTime = z.fEndTime;
		}
	}
	
	public void moveRight(int unit)
	{
		TLTrack t;
		TLEvent e;
		int 	dc;
		
		if ( fMT.at(fVoice) ) {
			t = fMT.getTrack();
			if (fStartTime == fEndTime) {
				fStartTime = fEndTime = successor(t, fStartTime, unit);
			} else {
				fStartTime = fEndTime;
			}
		}
	}
	
	public void extendRight(int unit)
	{
		if ( fMT.at(fVoice) ) {
			TLTrack t = fMT.getTrack();
			fEndTime = successor(t, fEndTime, unit);
		}
	}
	
	public void moveLeft(int unit)
	{
		TLTrack t;
		TLEvent e;
		int 	dc;
		
		if ( fMT.at(fVoice) ) {
			t = fMT.getTrack();
			if (fStartTime == fEndTime) {
				fStartTime = fEndTime = predecessor(t, fStartTime, unit);
			} else {
				fEndTime = fStartTime;
			}
		}
	}
	
	public void extendLeft(int unit)
	{
		if ( fMT.at(fVoice) ) {
			TLTrack t = fMT.getTrack();
			fStartTime = predecessor(t, fStartTime, unit);
		}
	}
	
		
	public void moveUp()
	{
		moveTo((fStartTime+fEndTime)/2, fVoice-1);
	}
		
	public void moveDown()
	{
		moveTo((fStartTime+fEndTime)/2, fVoice+1);
	}
	
	//======================================================================================
	// 					LES COMMANDES
	//======================================================================================
	
	public void cmdDuplicateTo(TLZone dst)
	{
		TLTrack t = this.copyContentToTrack();
		int		d = t.getFullDur();
		dst.suppressRestTime(d);
		dst.insertTrackToContent(t);
		this.set(dst);
	}

	public void cmdCopyToScrap()
	{
		//System.out.println( "Copy");
		fScrap = this.transfertIntoTrack(true);
	}

	public void cmdCutToScrap()
	{
		//System.out.println( "Cut");
		fScrap = this.transfertIntoTrack(false);
		if (! onlyRest(fScrap)) {
			this.insertTrack(new TLTrack(new TLRest(fScrap.getFullDur())));
		}
		fEndTime = fStartTime;
	}

	public void cmdPasteFromScrap()
	{
		//System.out.println( "Paste");
		TLTrack t = this.transfertIntoTrack(false); 
		if (! onlyRest(t)) {
			this.insertTrack(new TLTrack(new TLRest(t.getFullDur())));
		}
		this.suppressRestTime(fScrap.getFullDur());
		this.insertTrack(fScrap.makeCopy());
		fStartTime += fScrap.getFullDur();
		fEndTime = fStartTime;
	}

	public void cmdMoveTo(TLZone dst)
	{
		TLTrack t = this.moveContentToTrack();
		int		d = t.getFullDur();
		dst.suppressRestTime(d);
		dst.insertTrackToContent(t);
		this.set(dst);
	}

	public void cmdApply (TExp fun)
	{
		TLTrack t; 
		TExp	appl;
		
		t = moveContentToTrack(); 
		//t.fFunMode = false;
		//appl = TExpMaker.gExpMaker.createAppl( fun, TLConverter.exp(t) );
		appl = TExpMaker.gExpMaker.createAppl( fun, TLConverter.exp(t) );
		t = evaluateAllEvents( TLConverter.track(appl) );

		suppressRestTime(t.getFullDur());
		insertTrackToContent(t);
	}

	public void cmdInsert (TExp exp)
	{
		TLTrack t = TLConverter.track(exp);
		suppressRestTime(t.getFullDur());
		insertTrackToContent(t);
	}

	public void cmdPitch (int tr)
	{
		TLTrack t; 
		TExp	exp;
		
		t = moveContentToTrack(); 
		t.setTrackMode(TLTrack.NORMAL);
		exp = TExpMaker.gExpMaker.createTrsp( TLConverter.exp(t), tr );
		t = evaluateAllEvents( TLConverter.track(exp) );

		suppressRestTime(t.getFullDur());
		insertTrackToContent(t);
	}

	public void cmdVelocity (int tr)
	{
		TLTrack t; 
		TExp	exp;
		
		t = moveContentToTrack(); 
		t.setTrackMode(TLTrack.NORMAL);
		exp = TExpMaker.gExpMaker.createAttn( TLConverter.exp(t), tr );
		t = evaluateAllEvents( TLConverter.track(exp) );

		suppressRestTime(t.getFullDur());
		insertTrackToContent(t);
	}

	public void cmdChannel (int tr)
	{
		TLTrack t; 
		TExp	exp;
		
		t = moveContentToTrack(); 
		t.setTrackMode(TLTrack.NORMAL);
		exp = TExpMaker.gExpMaker.createTrsch( TLConverter.exp(t), tr );
		t = evaluateAllEvents( TLConverter.track(exp) );

		suppressRestTime(t.getFullDur());
		insertTrackToContent(t);
	}
		
	public void cmdPlay()
	{
		TGlobals.player.startPlayer (this.getExpression());
	}
	
	public void cmdClear()
	{
		TLTrack t = this.transfertIntoTrack(false);
		if (! onlyRest(t)) {
			this.insertTrack(new TLTrack(new TLRest(t.getFullDur())));
		}
		fEndTime = fStartTime;
	}

		
	public void cmdGroup ()
	{
		TLTrack t; 
		TExp	appl;
		
		t = new TLTrack(TLConverter.event(TLConverter.exp(this.moveContentToTrack()))); 

		suppressRestTime(t.getFullDur());
		insertTrackToContent(t);
	}
		
	public void cmdResize(int newEndTime)
	{
		TLTrack t = moveContentToTrack();	
		resizeTrack(t, newEndTime - fStartTime);
		suppressRestTime(t.getFullDur());
		insertTrackToContent(t);
	}
	
	public void cmdEvaluate()
	{
		if (fMT.at(fVoice)) {
			TLTrack t = fMT.remove();
			for (t.at(fEndTime); t.prev() && t.getDate() >= fStartTime;) { t.evaluate(); }
			fStartTime = t.getDate();
			fEndTime = fStartTime;
			fInGroup = t.isCurDateInGroup();
			fMT.insert(t);
		}
	}
	
	public void cmdReify()
	{
		if (fMT.at(fVoice)) {
			TLTrack t = fMT.remove();
			for (t.at(fEndTime); t.prev() && t.getDate() >= fStartTime;) { t.reify(); }
			fStartTime = t.getDate();
			fEndTime = fStartTime;
			fInGroup = t.isCurDateInGroup();
			fMT.insert(t);
		}
	}
	
	public void cmdUnevaluate()
	{
		if (fMT.at(fVoice)) {
			TLTrack t = fMT.remove();
			for (t.at(fEndTime); t.prev() && t.getDate() >= fStartTime;) { t.unevaluate(); }
			fStartTime = t.getDate();
			fEndTime = fStartTime;
			fInGroup = t.isCurDateInGroup();
			fMT.insert(t);
		}
	}

	

	//======================================================================================
	//
	//		Opérations de transfert
	//
	//======================================================================================


	public TLTrack transfertIntoTrack(boolean copymode)
	{		
		TLTrack r = new TLTrack();
		
		if ( fMT.at(fVoice) ) {
		
			TLTrack t = fMT.getTrack();
			r.setTrackMode(t.getTrackMode());
			
			// on verifie que la selection ne commence pas après la fin de la piste
			if (! t.at(fStartTime)) {
				//fStartTime = t.getDate();
				//fEndTime = fStartTime;
				return r;
			} 
			
			// on traite le cas d'une selection qui commence au milieu d'un événement
			if (t.getDate() < fStartTime) {
				TLEvent e = t.getEvent();
				if (e instanceof TLRest) {
					t.remove();
					t.insert(new TLRest(t.getDate() + e.fDur - fStartTime));
					t.insert(new TLRest(fStartTime - t.getDate()));
				} else {
					t.next();
					fStartTime = t.getDate();
					if (fStartTime >= fEndTime) {
						fEndTime = fStartTime;
					}
				}
			}
			
			// on vérifie les questions de groupage à fStartTime
			if (t.isCurDateInGroup() && !copymode) t.forgetGroup();
			
			// on ajuste la fin de sélection
			if (fEndTime >= t.getFullDur()) {
				fEndTime = t.getFullDur();
			} else {
				t.at(fEndTime);
				if (t.getDate() < fEndTime) {
					// la fin est au milieu d'un événement
					TLEvent e = t.getEvent();
					if (e instanceof TLRest) {
						t.remove();
						t.insert(new TLRest(t.getDate() + e.fDur - fEndTime));
						t.insert(new TLRest(fEndTime - t.getDate()));
					} else {
						t.prev();
						fEndTime = t.getDate();
					}
				}
				// on vérifie les questions de groupage à fEndTime
				if (t.isCurDateInGroup() && !copymode) t.forgetGroup();
			}
			
			
			// on transfert les événements
			t.at(fEndTime);
			if (copymode) {
				while (t.prev() && t.getDate() >= fStartTime) {
					r.insert(t.getEvent().makeCopy());
				}
				t.at(fStartTime); t.mergeRests();
				t.at(fEndTime); t.mergeRests();
				
			} else {
				fMT.remove();
				while (t.prev() && t.getDate() >= fStartTime) {
					r.insert(t.remove());
				}
				t.mergeRests();
				fMT.insert(t);
			}
		}
		
		return r;
	}
	
	public void insertTrack(TLTrack r)
	{
		if ( fMT.at(fVoice) ) {
		
			TLTrack t = fMT.remove();	
					
			// on prépare le point d'insertion
			
			if (! t.at(fStartTime) && (t.getDate() < fStartTime) ) {
				t.insert(new TLRest(fStartTime - t.getDate()));
				t.next();
				
			} else if (t.getDate() < fStartTime) {
				TLEvent e = t.getEvent();
				if (e instanceof TLRest) {
					t.remove();
					t.insert(new TLRest(t.getDate() + e.fDur - fStartTime));
					t.insert(new TLRest(fStartTime - t.getDate()));
					t.next();
				} else {
					t.next();
					fStartTime = t.getDate();
				}
			}
			
			// on détruit les info de groupe au point d'insertion
			t.forgetGroup();
			
			// on insere les événements
			int du = r.getFullDur();
			for (r.at(du); r.prev(); ) t.insert(r.remove());
			fEndTime = fStartTime + du;
			
			// on merge les rests
			t.at(fStartTime); t.mergeRests();
			t.at(fEndTime); t.mergeRests();
			
			// on remet la piste
			fMT.insert(t);
		}
	}
	
	public void replace(TLTrack t)
	{		
		this.transfertIntoTrack(false);
		this.insertTrack(t);
	}
	
	public TExp getFunction()
	{		
		TLTrack r;
		
		r = this.transfertIntoTrack(true);
		r.setTrackMode(TLTrack.SEQFUNCTION);
		
		if (r.at(0) && ! r.next()) {
			// il n'y a qu'1 evenement
			r.prev();
			return TLConverter.funSeq(r.getEvent());
		} else {
			return TLConverter.exp(r);
		}
	}
	
	public TExp getExpression()
	{
		return TLConverter.exp(this.transfertIntoTrack(true));
	}
	
	static boolean onlyRest(TLTrack t)
	{
		for (t.end(); t.prev();) if (! (t.getEvent() instanceof TLRest) ) return false;
		return true;
	}


	
	//-------------------------------------------------------------
	// 					outils pour les commandes
	//-------------------------------------------------------------
	
	
	public void normalizeZone()
	{
		// normalisation de numéro de piste
		if (fVoice < 0) fVoice = 0; else if (fVoice >= fMT.getCount()) fVoice = fMT.getCount() - 1;
		
		fMT.at(fVoice);
		TLTrack t 	= fMT.getTrack();
		int 	td 	= t.getFullDur();
		int		dc;
		TLEvent	e;
		
		// cas d'une zone à la fin
		if (fStartTime >= td) {
			fStartTime = fEndTime = td;
			return;
		}
		
		// normalisation de fStartTime
		t.at(fStartTime);
		dc = t.getDate();
		
		if ( (dc < fStartTime) && !(t.getEvent() instanceof TLRest) ) {
			fStartTime = dc;
		}
		
		// normalisation de fEndTime
		if (fEndTime >= td) {
			fEndTime = td;
		} else {
			t.at(fEndTime);
			dc = t.getDate();
			e = t.getEvent();
			if ( (dc < fEndTime) && !(e instanceof TLRest) ) {
				fEndTime = dc + e.fDur;
			}
		}
	}
	
		
	//--------------------------------------------------------------------------
	// moveContentToTrack() : 
	// copie le contenu de la zone dans une track et remplace ce contenu
	// dans la source par des silences (sauf si la zone de départ ne contenait 
	// que des silences)
	//--------------------------------------------------------------------------

	TLTrack moveContentToTrack()
	{
		// on suppose la zone correctement normalisée
		fMT.at(fVoice);
		
		TLTrack 	t	= fMT.getTrack();
		TLTrack 	r	= new TLTrack(); 
					r.setTrackMode(t.getTrackMode());		// coorection bug groupage pour les fonctions
		TLEvent 	e;
		int			dc;
		boolean 	allRest = true;
		
		t.at(fEndTime);
		dc = t.getDate();
		
		// si la fin est a cheval sur un silence
		if (dc < fEndTime) {
			// on est dans un silence que l'on va couper en deux parties
			e = t.remove();
			t.insert( new TLRest(dc + e.fDur - fEndTime) );
			t.insert( new TLRest(fEndTime - dc) );
			// on reviens se placer entre les deux silences
			t.next(); 
		}
		
		// ici on est après le dernier événement de la zone
		
		// on transfert le contenu
		while (t.prev() && t.getDate() > fStartTime) {
			e = t.remove();
			r.insert(e); 
			allRest &= (e instanceof TLRest);
		}
		
		// on ajuste pour le début
		dc = t.getDate();
		if (dc < fStartTime) {
			// on est sur un silence que l'on va couper en deux parties
			e = t.remove();
			r.insert( new TLRest(dc + e.fDur - fStartTime) );
			t.insert( new TLRest(fStartTime - dc) );
			t.next();
		} else {
			e = t.remove();
			r.insert(e); 
			allRest &= (e instanceof TLRest);
		}
		
		// on compense si souhaité
		int dr = r.getFullDur();
		if ((! allRest)  && dr > 0) {
			t.insert( new TLRest(dr) );
		} 
		
		t.mergeRests();
		
		return r;
	}
	
		
	//--------------------------------------------------------------------------
	// copyContentToTrack() : 
	// copie le contenu de la zone dans une track (sans le supprimer de l'original)
	//--------------------------------------------------------------------------

	public TLTrack copyContentToTrack()
	{
		// on suppose la zone correctement normalisée :
		// 0 <= startTime <= endTime <= fulldur, 
		// startTime et endTime ne sont a cheval que sur des silences
		
		fMT.at(fVoice);
		
		TLTrack 	t	= fMT.getTrack();
		TLTrack 	r	= new TLTrack();
		TLEvent 	e;
		int			dc;
		
		if (fStartTime < fEndTime) {
			t.at(fEndTime);
			dc = t.getDate();
			
			// si la fin est a cheval sur un silence
			if (dc < fEndTime) {
				// on est dans un silence dont on ne va prendre qu'une partie
				e = t.getEvent();
				
				if ( (e == null) || ! (e instanceof TLRest) ) {
					System.out.println ("erreur sur fEndtime dans copyContentToTrack()");
					return r;
				}
				if (fStartTime >= dc) {
					r.insert( new TLRest(fEndTime - fStartTime) );
					return r; // ???? on a copié qu'une partie d'un silence ????
				} else {
					r.insert( new TLRest(fEndTime - dc) );
				}
			}
			
			// ici on est après le dernier événement de la zone
			
			// on transfert le contenu
			while (t.prev() && t.getDate() > fStartTime) {
				e = t.getEvent();
				r.insert(e.makeCopy()); 
			}
			
			// on ajuste pour le début
			dc = t.getDate();
			if (dc < fStartTime) {
				// on est dans un silence dont on ne va prendre qu'une partie
				e = t.getEvent();
				r.insert( new TLRest(dc + e.fDur - fStartTime) );
			} else {
				e = t.getEvent();
				r.insert(e.makeCopy()); 
			}
		}
		return r;
	}

	
	void resizeTrack(TLTrack t, long nd)
	{
		TLEvent e;
		long 	od = t.getFullDur();
		long	d2;
		
		if (od > 0 && nd > 0) {
			for (t.end(); t.prev();) {
				e = t.remove();
				d2 = ( e.fDur * nd ) / od ;
				t.insert( e.makeResizedCopy( (int) d2 ) );
			}
		}
	}
	
	void printTrack(TLTrack t, String msg)
	{
		System.out.println (msg);
		System.out.println (" printTrack(" + t + ")" );
		for (t.begin(); t.getEvent() != null; t.next()) {
			TLEvent e = t.getEvent();
			System.out.println(e + " - " + t.getDate() + ", " + e.fDur);
		}
		System.out.println ("total : " + t.getFullDur());
		System.out.println ("");
	}
	
	//--------------------------------------------------------------------------
	// insertTrackToContent(TLTrack r)
	// insere le contenu d'une track
	//--------------------------------------------------------------------------
	
	void insertTrackToContent(TLTrack r)
	{
		// on suppose la zone correctement normalisée
		fMT.at(fVoice);
		TLTrack t	= fMT.getTrack();
		TLEvent e;
		int		dc;

		//printTrack(t, "destination insertion : ( à la date " + fStartTime + ")");
		//printTrack(r, "piste à insérer : " + fStartTime);

		t.at(fStartTime);
		dc = t.getDate();
		
		if (dc < fStartTime) {								// on n'est pas à une frontière d'événement !!!

			e = t.getEvent();
			
			if (e == null) { 								// on est au dela de la fin de piste
				t.insert(new TLRest(fStartTime - dc)); 		// on a joute un silence
				t.next();									// et on se met après
				
			} else if (e instanceof TLRest) {				// on est au milieu d'un silence
				t.remove();									// on le remplace
				t.insert(new TLRest(dc+e.fDur-fStartTime));	// par deux parties
				t.insert(new TLRest(fStartTime-dc));
				t.next();									// et on se place entre les deux
				
			} else {
				// erreur, fStartTime n'est doit pas etre a cheval sur un événement non-silence
			 	System.out.println("erreur insertTrackToContent"); 
			 	return; 
			}
		}
		
		int du = r.getFullDur();
		for (r.end(); r.prev();) { t.insert(r.remove()); }
		
		fEndTime = fStartTime + du;
		
		t.at(fEndTime); t.mergeRests();
		t.at(fStartTime); t.mergeRests();
		
		//printTrack(t, "résultat après insertion : ");
	}
	
	//--------------------------------------------------------------------------
	// insertRestTime(int dr) :
	// insere du silence à partir de fStartTime sur la durée dr
	//--------------------------------------------------------------------------
	void insertRestTime (int dr)
	{
		this.insertTrack(new TLTrack(new TLRest(dr)));
	}
	
	//--------------------------------------------------------------------------
	// suppressRestTime(int dr) :
	// supprime du silence à partir de fStartTime si possible jusqu'a 
	// concurrence de la durée dr. S'arrete au 1er evenement non-silence
	//--------------------------------------------------------------------------
	void suppressRestTime(int dr)
	{
		// on suppose la zone correctement normalisée

		fMT.at(fVoice);
		
		TLTrack t	= fMT.getTrack();
		TLEvent e;
		int		dc;
		
		if (fStartTime < t.getFullDur()) {
			t.at(fStartTime);
			dc = t.getDate();
			
			if (dc < fStartTime) {
				e = t.remove();
				if (! (e instanceof TLRest)) { System.out.println("ERROR (not TLRest) in suppressRestTime"); return; }
				t.insert(new TLRest(dc+e.fDur - fStartTime));
				t.insert(new TLRest(fStartTime - dc));
				// et on se place entre les deux
				t.next();
			}
			e = t.getEvent();
			while ( (e != null) && (e instanceof TLRest) && (e.fDur < dr) ) {
				t.remove();
				dr -= e.fDur;
				e = t.getEvent();
			}
			if ((e != null) && (e instanceof TLRest)) {
				if (e.fDur > dr) {
					t.remove();
					t.insert(new TLRest(e.fDur - dr));
				} else { // (e.fDur == dr)
					t.remove();
				}
			} else {
				// on n'a pas pu tout compenser !
			}
		}
	}
	
	void shift(int deltaTime)
	{
		fStartTime += deltaTime;
		fEndTime += deltaTime;
	}
	
	
	
	//-------------------------------------------------
	// fonctions statiques de transformation de pistes
	//-------------------------------------------------
	
	static TLTrack evaluateAllEvents(TLTrack t)
	{ 
		for (t.end(); t.prev(); ) { t.evaluate(); }
		return t;
	}
	
	static TLTrack unevaluateAllEvents(TLTrack t)
	{ 
		for (t.end(); t.prev(); ) { t.unevaluate(); }
		return t;
	}
		
	//-------------------------------------------------
	// autres fonctions statiques de pistes
	//-------------------------------------------------
	
	static int successor(TLTrack t, int d, int u)
	{
		t.at(d);
		if (t.next()) {
			return t.getDate();
		} else if (d < t.getDate()){
			return t.getDate();
		} else {
			return (d/u + 1) * u;
		}
	}
	
	static int predecessor(TLTrack t, int d, int u)
	{
		t.at(d);
		int dc = t.getDate();
		if (dc < d) {
			d = (d/u - 1) * u;
			if (t.getFullDur() < d) {
				return d;
			} else {
				return dc;
			}
		} else {
			t.prev();
			return t.getDate();
		}
	}
}
