package grame.elody.editor.tleditor;

import grame.elody.editor.tleditor.tlevent.TLEvent;
import grame.elody.editor.tleditor.tlevent.TLRest;

import java.io.Serializable;

/***********************************************************************************************************
	La classe TLTrack permet de gérer une séquence d'événements (TLEvent) avec une notion de position courante
	(date et événement courant). On suppose que la durée d'un événement n'est jamais nulle et toujours > 0 !
	 
	Outre le constructeur TLTrack() qui construit une séquence vide d'événement, et le constructeur qui construit
	une track à partir d'une expression elody, il y a 8 fonctions :	

	--donnent des infos, mais sans effet de bord

	t.getDate() 	-> date		donne la date courante (date de l'événement courant), c'est à dire la somme 
								des durées des événements qui préc?dent. En fin de piste l'événement courant est nul et
								la date courante est la durée totale de la piste.
	t.getEvent() 	-> ev		donne l'événement courant (ou null si en fin de piste)
	t.getFullDur()	-> fulldur	donne la durée totale de la piste (somme des durées des événements) ou 0 
								s'il n'y a pas d'événement dans la piste

	--ajoute/supprime des événements à la position courante, sans changer la position courante, mais en 
	  ajustant la durée totale de la piste.

	t.insert(ev)				insére un événement à la date courante (protégée contre pointeur null)
	t.remove() 		-> ev		enleve et retourne l'événement courant (ou null si apr?s le dernier événement)

	--changent la position courante  (protégées contre les positions hors bornes)

	t.at(date) 		-> bool		change la date courante de sorte que la nouvelle date courante soit égale ou
								immédiatement inférieure à celle voulue. Renvoie Vrai s'il y a un événement à cette date.
	t.next() 		-> bool		passe à l'événement suivant. Renvoie true s'il y en a un.
	t.prev() 		-> bool		passe à l'événement précédent. Renvoie true s'il y en avait un.

	--opérations globales
	t.clear()					efface la totalité de la piste
	
***********************************************************************************************************/

public final class TLTrack implements Serializable {
	public TLTrack	fTrackLink;			// lien de chainage vers autre track		
	private boolean	fMuteFlag;			// pisted "mutée" (ignorée) si vrai
	private int		fTrackMode;			// mode de fonctionnement de la piste (normal, fonction, etc.)
	private int		fCurDate;			// date de l'événement courant		
	private TLEvent fCurEvent;			// événement courant (et suivants chainés)
	private TLEvent fPrevEvent;			// événement précédent (et précédents chainés)
	public int		fTrackDur;			// durée totale de la piste
	public int		fTrackHeight;		// nb de lignes totale de la piste
	
	// les valeurs possibles de fTrackMode
	public static final int	NORMAL 		= 0;
	public static final int	SEQFUNCTION 	= 1;
	public static final int	ABSTRACTION = 2;
	public static final int	PARAMETER 	= 3;
	public static final int	MIXFUNCTION = 4;
	
	
	
	//------------------------------------------------------------------------------
	// création d'une track vide
	//------------------------------------------------------------------------------
	public TLTrack()
	{
		fTrackLink		= null;				
		fTrackMode		= NORMAL;			
		fMuteFlag		= false;			
		fCurDate		= 0;				
		fCurEvent 		= null;				
		fPrevEvent 		= null;				
		fTrackDur		= 0;				
		fTrackHeight	= 1;				
	}
	
	//------------------------------------------------------------------------------
	// création d'une track contenant un événement
	//------------------------------------------------------------------------------
	public TLTrack(TLEvent e)
	{
		fTrackLink			= null;
		fTrackMode		= 0;
		fCurDate		= 0;
		fCurEvent 		= e;	e.fLink = null;
		fPrevEvent 		= null;
		fTrackDur		= e.fDur;
		fTrackHeight	= e.fHeight;
	}
	
	//------------------------------------------------------------------------------
	// fonctions d'acc?s aux différents champs
	//------------------------------------------------------------------------------
	final public TLEvent 	getEvent()				{ return fCurEvent; }
	final public int 		getDate()				{ return fCurDate; }
	final public int 		getFullDur()			{ return fTrackDur; }
	final public int 		getMaxHeight()			{ return fTrackHeight; }
	
	final public int		getTrackMode()			{ return fTrackMode; }
	final public boolean	getMuteFlag()			{ return fMuteFlag; }
	
	final public TLTrack	setTrackMode(int m)		{ fTrackMode = m; return this; }
	final public TLTrack	setMuteFlag(boolean b)	{ fMuteFlag = b;; return this; }
	
	final public boolean	isSeqFunTrack()			{ return fTrackMode == SEQFUNCTION; }
	final public boolean	isMixFunTrack()			{ return fTrackMode == MIXFUNCTION; }
	final public boolean	isParamTrack()			{ return fTrackMode == PARAMETER; }
	final public boolean	isAbstrTrack()			{ return fTrackMode == ABSTRACTION; }
	final public boolean	isNormalTrack()			{ return fTrackMode == NORMAL; }
	final public boolean	isMutedTrack()			{ return fMuteFlag; }
	
	//------------------------------------------------------------------------------
	// Fonctions de déplacement : begin(), end(), at(d), next() et prev(). Changent la position courante.
	// (retourne VRAI s'il y a un événement à la nouvelle position courante)
	//------------------------------------------------------------------------------
	
	final public boolean begin()
	{
		TLEvent 	p, t, c;
		
		if ((p = fPrevEvent) != null) {
			c = fCurEvent;
			do {
				t = p;
				p = t.fLink;
				t.fLink = c;
				c = t;
			} while (p != null);
				
			fCurDate = 0;
			fCurEvent = c;
			fPrevEvent = null;
		}
		return (fCurEvent != null);
	}
	
	final public boolean end()
	{
		TLEvent 	p, t, c;
		int			d;
		
		if ((c = fCurEvent) != null) {
			p = fPrevEvent;
			d = fCurDate;
			do {
				t = c;
				c = t.fLink;
				d += t.fDur;
				t.fLink = p;
				p = t;
			} while (c != null);
				
			fCurDate = d;
			fCurEvent = null;
			fPrevEvent = p;
		}
		return false;
	}

	final public boolean at(int dv)
	{
		TLEvent 	e 	= fCurEvent;
		int 		dc 	= fCurDate;
		int 		dt;
			
		dv = (dv < 0) ? 0 : dv;
		
		if (dv != dc) {
			TLEvent		prv = fPrevEvent;
			TLEvent 	tmp;
			
			if ( dv > dc ) {
				while ( (e != null) && (dv >= (dt = dc + e.fDur)) ) {
					tmp = e.fLink;
					e.fLink = prv;
					prv = e;
					e = tmp;
					dc = dt;
				}
			} else /* 0 ² dv < dc */ {
				do {
					tmp = prv.fLink;
					prv.fLink = e;
					e = prv;
					prv = tmp;
				} while ((dc -= e.fDur) > dv);
			}
			fCurEvent = e;
			fPrevEvent = prv;
			fCurDate = dc;
		}

		return (e != null);
	}
	
	final public boolean next()
	{
		TLEvent 	e;
		
		if ((e = fCurEvent) != null) {
			fCurDate += e.fDur;
			fCurEvent = e.fLink;
			e.fLink = fPrevEvent;
			fPrevEvent = e;
		}
		return (fCurEvent != null);
	}
	
	final public boolean prev()
	{
		TLEvent 	e;
		
		if ((e = fPrevEvent) != null) {
			fCurDate -= e.fDur;
			fPrevEvent = e.fLink;
			e.fLink = fCurEvent;
			fCurEvent = e;
		}
		return (e != null);
	}
			
	
	
	//------------------------------------------------------------------------------
	// fonctions d'insertion/suppression, qui ne changent pas la date courante
	//------------------------------------------------------------------------------
	
	// insert(TLEvent e) : insére un événement à la date courante
	final public void insert(TLEvent e)
	{
		if (e != null) {
			e.fLink = fCurEvent;
			fCurEvent = e;
			fTrackDur += e.fDur;
			if (e.fHeight > fTrackHeight) fTrackHeight = e.fHeight;
		}
	}
	
	// remove() -> e : supprime l'événement courant
	final public TLEvent remove()
	{
		TLEvent e;
		if ((e = fCurEvent) != null) {
			fCurEvent = e.fLink;
			fTrackDur -= e.fDur;
			if (fTrackHeight == e.fHeight) fTrackHeight = computeMaxHeight();
		}
		return e;
	}
			
	
	
	//------------------------------------------------------------------------------
	// Calcul de l'epaisseur d'une piste
	//------------------------------------------------------------------------------
	
	// computeMaxHeight() : epaisseur d'une track
	final int computeMaxHeight()
	{
		int h = 1;
		TLEvent e;
		for (e = fCurEvent; e != null; e = e.fLink) {
			if (e.fHeight > h) h = e.fHeight;
		}
		for (e = fPrevEvent; e != null; e = e.fLink) {
			if (e.fHeight > h) h = e.fHeight;
		}
		return h;
	}
			
	
	
	//------------------------------------------------------------------------------
	// fonctions de copie
	//------------------------------------------------------------------------------
	
	// makeCopy() : duplique une track
	final public TLTrack makeCopy()
	{
		TLTrack t = new TLTrack();
		if (this.at(0)) do {
			t.insert(this.getEvent().makeCopy());
			t.next();
		} while (this.next());
		return t;
	}
	
		
	//==========================================================================================
	// fonctions de modification spéciales
	// 		mergeRests() 	  : concatene tous les silences immediatement autour de la 
	//							date courante
	//==========================================================================================
	
	final public TLTrack mergeRests()
	{

		// comptabilise les silences vers le future
		TLEvent cur = fCurEvent;
		int		dsf = 0;
		while (cur != null && cur.isRest()) {
			dsf += cur.fDur;
			cur  = cur.fLink;
		}
		
		// comptabilise les silences vers le passé
		TLEvent prv = fPrevEvent;
		int		dsp = 0;
		while (prv != null && prv.isRest()) {
			dsp += prv.fDur;
			prv  = prv.fLink;
		}
		
		// ajustement de la piste
		int	dst = dsp + dsf;		// durée totale des silences
		if (dst > 0) {
			// il y a eu des silences
			if (cur == null) {
				// c'était des silences en fin de piste
				fCurEvent = cur;
				fPrevEvent = prv;
				fCurDate -= dsp;
				fTrackDur -= dst;
			} else {
				// c'était des silences en cours de piste
				// on les remplace par un seul silence et
				// on se place apr?s
				fCurEvent = cur;
				fPrevEvent = new TLRest(dst);
				fPrevEvent.fLink = prv;
				fCurDate += dsf;
			}
		}
				
		return this;
	}
	
		
	//==========================================================================================
	// fonctions de recherche de la plus proche fronti?re
	// 		frontier(dv) -> df 	  : recherche la frontiere la plus proche
	//==========================================================================================
	
	final public int frontier(int dv)
	{
		dv = (dv < 0) ? 0 : dv;
			
		int 		dc 	= fCurDate;
		
		if (dv < dc) {
			TLEvent 	prv = fPrevEvent;
			int			dp = dc - prv.fDur;
			while (dp > dv) {
				dc = dp;
				prv = prv.fLink;
				dp = dc - prv.fDur;
			}
			return  (dv > (dp + dc)/2) ? dc : dp;
		} else if (dv > dc) {
			TLEvent	cur = fCurEvent;
			int		dn = dc;
			while (cur != null && (dn += cur.fDur) < dv) {
				cur = cur.fLink;
				dc = dn;
			}
			if (cur != null) {
				return (dv > (dc + dn)/2) ? dn : dc;
			} else {
				return dc;
			}
		} else {
			return dc;
		}
	}
	
	public void clear()
	{
		fTrackMode		= NORMAL;			
		fMuteFlag		= false;			
		fCurDate		= 0;				
		fCurEvent 		= null;				
		fPrevEvent 		= null;				
		fTrackDur		= 0;				
		fTrackHeight	= 1;				
	
	}
	
	//==========================================================================================
	// Evaluation de l'événement courant
	//==========================================================================================
	
	public void	evaluate() 
	{ 
		//System.out.println( "evaluate " + fCurEvent );
		TLEvent ce;
		
		if ( (ce = fCurEvent) != null && ! ce.isRest()) {
			this.remove();
			TLTrack trk = TLConverter.track(TLConverter.symbolicEval(ce.getExp()));
			trk.at(trk.getFullDur());
			while(trk.prev()) {
				TLEvent x = trk.remove();
				x.fFather = ce;
				this.insert(x);
			}
		}
	}
	
	public void	reify() 
	{ 
		//System.out.println( "evaluate " + fCurEvent );
		TLEvent ce;
		
		if ( (ce = fCurEvent) != null && ! ce.isRest()) {
			this.remove();
			TLTrack trk = TLConverter.track(TLConverter.reify(ce.getExp()));
			trk.at(trk.getFullDur());
			while(trk.prev()) {
				TLEvent x = trk.remove();
				x.fFather = ce;
				this.insert(x);
			}
		}
	}
	
	//==========================================================================================
	// Desévaluation de l'événement courant
	//==========================================================================================
	
	public void unevaluate()
	{
		//System.out.println( "unevaluate " + fCurEvent );
		TLEvent ce, fe;
		
		if ( (ce = fCurEvent) != null ) {
			if ( (fe = ce.fFather) != null ) {
				
				// elimination vers le future
				int fdur = 0;
				while (ce != null && ce.hasAncestor(fe)) { fdur += ce.fDur; ce = ce.fLink; }
				
				// insertion du p?re
				fCurEvent = fe; fe.fLink = ce;
				
				// elimination vers le passé
				int pdur = 0;
				ce = fPrevEvent;
				while (ce != null && ce.hasAncestor(fe)) { pdur += ce.fDur; ce = ce.fLink; }
				fPrevEvent = ce;
				
				// rectification du temps
				fTrackDur = fTrackDur + fe.fDur - fdur - pdur;
				fCurDate -= pdur; 
			}
		}
	}
	
	//==========================================================================================
	// forgetGroup : détruit les info de groupage à la date courante
	//==========================================================================================
	
	public void forgetGroup()
	{
		//System.out.println( "forget group : " + this);
		TLEvent ce, x;
		if ( (ce = fCurEvent) != null ) {
			boolean alone = true;
			x = ce.fLink;
			while (ce.isRelatedTo(x)) { alone = false; x.fFather = null; x = x.fLink; }
			x = fPrevEvent;;
			while (ce.isRelatedTo(x)) { alone = false; x.fFather = null; x = x.fLink; }
			if (! alone) { ce.fFather = null; }
		}
	}
	
	//==========================================================================================
	// isCurEvInGroup() : indique si l'événement courant est groupé avec ces voisins
	//==========================================================================================
	
	public boolean isCurEvInGroup()
	{
		TLEvent ce;
		if ( (ce = fCurEvent) != null ) {
			return ce.isRelatedTo(ce.fLink) || ce.isRelatedTo(fPrevEvent);
		} else {
			return false;
		}
	}
	
	//==========================================================================================
	// isCurDateInGroup() : indique si la date est entre deux événements d'un m?me groupe
	//==========================================================================================
	
		
	public boolean isCurDateInGroup()
	{
		return (fCurEvent != null) && fCurEvent.isRelatedTo(fPrevEvent);
	}
}
