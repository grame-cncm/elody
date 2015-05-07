/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.editor.tleditor;

import java.io.Serializable;

/***********************************************************************************************************
La classe TLMultiTracks permet de gérer un ensemble de pistes (TLTracks) avec une notion de position et
de piste courante. 
Outre le constructeur TLMultiTracks() qui construit un ensemble vide de pistes, il y a 8 fonctions :	

--donnent des infos, mais sans effet de bord

mt.getCount() 	-> count	donne le nombre de pistes (avec getCount() ³ 0)
mt.getPos() 	-> pos		donne le numéro de la piste courante (forcement entre 0 et getCount())
mt.getTrack() 	-> trk		donne la piste courante (ou null si après la dernière piste)
mt.getMultiDur()	-> maxdur	donne la durée de la plus longue piste ou 0 s'il n'y a pas de pistes

--ajoute/supprime des pistes sans changer la position courante, mais en ajustant fTrackCount et fMultiDur.

mt.insert(trk)				insére une piste à la position courante (protégée contre pointeur null)
mt.remove() 	-> trk		enleve et retourne la piste courante (ou null si après la dernière piste)

--changent la position courante  (protégées contre les positions hors bornes)

mt.at(pos) 		-> bool		change la piste courante. Renvoie Vrai s'il y a une.
mt.next() 		-> bool		passe à la piste suivante. Renvoie Vraie s'il y en a une.

--opérations globales
mt.clear()					efface toutes les pistes

***********************************************************************************************************/

public final class TLMultiTracks implements Serializable {
	private int		fTrackCount;		// nombre total de pistes
	private int		fMultiHeight;		// somme des "epaisseurs" des pistes
	private int		fCurVPos;			// position verticale de la piste courante [0..fMultiHeight-1]		
	private int		fCurIndex;			// numero de la piste courante [0..trackCount-1]		
	private TLTrack fCurTrack;			// piste courante (et suivantes chainées)
	private TLTrack fPrevTrack;			// piste précédentes (et précédentes chainées)
	private int		fMultiDur;			// durée de la plus longue piste
	
	
	//------------------------------------------------------------------------------
	// création d'un multitracks vide
	//------------------------------------------------------------------------------
	public TLMultiTracks()
	{
		fMultiHeight= 0;
		fMultiDur	= 0;
		fTrackCount	= 0;
		fCurIndex	= 0;
		fCurVPos	= 0;
		fCurTrack 	= null;
		fPrevTrack 	= null;
	}
	
	//------------------------------------------------------------------------------
	// fonctions d'accès aux différents champs
	//------------------------------------------------------------------------------
	final public TLTrack 	getTrack()		{ return fCurTrack; }
	final public int 		getPos()		{ return fCurVPos; }
	final public int 		getIndex()		{ return fCurIndex; }
	final public int 		getCount()		{ return fTrackCount; }
	final public int 		getMultiDur()	{ return fMultiDur; }
	final public int 		getMultiHeight(){ return fMultiHeight; }
	
	

	//------------------------------------------------------------------------------
	// Fonctions de déplacement : begin(), end(), at(d), next() et prev(). Changent la position courante.
	// (retourne VRAI s'il y a une Track à la nouvelle position courante)
	//------------------------------------------------------------------------------
	
	final public boolean begin()
	{
		TLTrack 	p, t, c;
		
		if ((p = fPrevTrack) != null) {
			c = fCurTrack;
			do {
				t = p;
				p = t.fTrackLink;
				t.fTrackLink = c;
				c = t;
			} while (p != null);
			fCurTrack = c;
			fPrevTrack = null;
				
			fCurVPos = 0;
			fCurIndex = 0;
		}
		return (fCurTrack != null);
	}
	
	final public boolean end()
	{
		TLTrack 	p, t, c;
				
		if ((c = fCurTrack) != null) {
			p = fPrevTrack;
			do {
				t = c;
				c = t.fTrackLink;
				t.fTrackLink = p;
				p = t;
			} while (c != null);
			fCurTrack = null;
			fPrevTrack = p;
			fCurVPos = fMultiHeight;
			fCurIndex = fTrackCount;
		}
		return false;
	}

	final public boolean at(int dv)
	{
		TLTrack 	e 	= fCurTrack;
		int 		dc 	= fCurVPos;
		int 		idx = fCurIndex;
		int 		dt;
			
		dv = (dv < 0) ? 0 : dv;
		
		if (dv != dc) {
			TLTrack		prv = fPrevTrack;
			TLTrack 	tmp;
			
			if ( dv > dc ) {
				while ( (e != null) && (dv >= (dt = dc + e.fTrackHeight)) ) {
					tmp = e.fTrackLink;
					e.fTrackLink = prv;
					prv = e;
					e = tmp;
					dc = dt;
					idx++;
				}
			} else /* 0 ² dv < dc */ {
				do {
					tmp = prv.fTrackLink;
					prv.fTrackLink = e;
					e = prv;
					prv = tmp;
					idx--;
				} while ((dc -= e.fTrackHeight) > dv);
			}
			fCurTrack = e;
			fPrevTrack = prv;
			fCurVPos = dc;
			fCurIndex = idx;
		}

		return (e != null);
	}

	// se positionne sur la ieme piste
	final public boolean index(int iv)
	{
		TLTrack 	e 	= fCurTrack;
		int 		idx = fCurIndex;
			
		iv = (iv < 0) ? 0 : iv;
		
		if (iv != idx) {
			int 		cvp = fCurVPos;
			TLTrack		prv = fPrevTrack;
			TLTrack 	tmp;
			
			if ( iv > idx ) {
				while ( (e != null) && (iv > idx) ) {
					cvp += e.fTrackHeight;
					tmp = e.fTrackLink;
					e.fTrackLink = prv;
					prv = e;
					e = tmp;
					idx++;
				}
			} else /* 0 ² iv < idx */ {
				do {
					tmp = prv.fTrackLink;
					prv.fTrackLink = e;
					e = prv;
					prv = tmp;
					idx--;
					cvp -= e.fTrackHeight;
				} while (iv < idx);
			}
			fCurTrack = e;
			fPrevTrack = prv;
			fCurVPos = cvp;
			fCurIndex = idx;
		}

		return (e != null);
	}
	
	final public boolean next()
	{
		TLTrack 	e;
		
		if ((e = fCurTrack) != null) {
			fCurVPos += e.fTrackHeight;
			fCurTrack = e.fTrackLink;
			e.fTrackLink = fPrevTrack;
			fPrevTrack = e;
			fCurIndex++;
		}
		return (fCurTrack != null);
	}
	
	final public boolean prev()
	{
		TLTrack 	e;
		
		if ((e = fPrevTrack) != null) {
			fCurVPos -= e.fTrackHeight;
			fPrevTrack = e.fTrackLink;
			e.fTrackLink = fCurTrack;
			fCurTrack = e;
			fCurIndex--;
		}
		return (e != null);
	}
			
	
	
	//------------------------------------------------------------------------------
	// fonctions d'insertion/suppression, qui ne changent pas la position courante
	//------------------------------------------------------------------------------
	
	// insert(TLTrack t) : insére une piste à la position courante
	final public void insert(TLTrack t)
	{
		if (t != null) {
			t.fTrackLink = fCurTrack;
			fCurTrack = t;
			fTrackCount++;
			if (t.getFullDur() > fMultiDur) fMultiDur = t.getFullDur();
		}
	}
	
	// remove() -> trk : supprime la piste courante
	
	final public TLTrack remove()
	{
		TLTrack t;
		if ((t = fCurTrack) != null) {
			fTrackCount--;
			fCurTrack = t.fTrackLink;
			if (t.getFullDur() == fMultiDur) fMultiDur = calcMultiDur();
			
		}
		return t;
	}
	
	
	// calcfMultiDur() : calcule le max des durées des pistes (sans modifier la position courante)
	final private int calcMultiDur()
	{
		TLTrack t;
		int		d;
		int		dmax = 0;
		for (t = fCurTrack; t != null; t = t.fTrackLink) {
			if ((d=t.getFullDur()) > dmax) dmax = d;
		}
		for (t = fPrevTrack; t != null; t = t.fTrackLink) {
			if ((d=t.getFullDur()) > dmax) dmax = d;
		}
		return dmax;
	}
	
	//------------------------------------------------------------------------------
	// autres fonctions 
	//------------------------------------------------------------------------------
	
	
	// clear : efface toutes les pistes
	final public void clear()
	{
		TLTrack t;
		for (t = fCurTrack; t != null; t = t.fTrackLink) {
			t.clear();
		}
		for (t = fPrevTrack; t != null; t = t.fTrackLink) {
			t.clear();
		}
		fMultiDur = 0;
	}
}
