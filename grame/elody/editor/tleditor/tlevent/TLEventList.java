/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.editor.tleditor.tlevent;

//===========================================================
//===========================================================
//
//					TLEventList
//
//Implemente une liste d'éléments de type <TLEvent> 
//ayant un lien de chainage <fLink>
//
//count() 	: nombre d'éléments dans la liste
//get(i) 	: ième élément de la liste
//ins(i,e) : insère un élément en ième position
//add(e) 	: ajoute un élément en fin de liste
//rem(i) 	: enlève l'élément en ième position
//
//===========================================================
//===========================================================

public class TLEventList {
	protected int 		fCount;			// nombre d'éléments
	protected int 		fIndex;			// indice élément fNext
	protected TLEvent 	fPrev;			// élément précèdent
	protected TLEvent 	fNext;			// élément suivant
	
	public TLEventList () {
		fCount=0; fIndex=0; fPrev=null; fNext=null;
	}
	
	public final TLEventList makeCopy() 
	{ 
		TLEventList l = new TLEventList();
		for (int i = 0; i < fCount; i++) l.add( this.get(i).makeCopy() );
		return l; 
	}
	

	//----------------------------------------------------
	// count() : nombre d'éléments dans la liste
	//----------------------------------------------------
	
	final public int count()	
	{ 
		return fCount; 
	}
	
	//----------------------------------------------------
	// get(i) : ième élément de la liste
	//----------------------------------------------------
	
	final public TLEvent get(int i) 
	{
		at(i);
		return fNext;
	}
	
	//----------------------------------------------------
	// ins(i,e) : insère un élément en ième position
	//----------------------------------------------------
	
	final public TLEventList ins(int i, TLEvent e) 
	{
		if (e != null) {
			at(i);
			e.fLink = fNext;
			fNext = e;
			fCount++;
		}
		return this;
	}
	
	//----------------------------------------------------
	// add(e) : ajoute un élément en fin de liste
	//----------------------------------------------------
	
	final public TLEventList add(TLEvent e) 
	{
		if (e != null) {
			at(fCount);
			e.fLink = fPrev;
			fPrev = e;
			fCount++;
			fIndex++;
		}
		return this;
	}
	
	//----------------------------------------------------
	// rem(i) : enlève l'élément en ième position
	//----------------------------------------------------
	
	final public TLEvent rem(int i) 
	{
		at(i);
		TLEvent e = fNext;
		if (e != null) {
			fNext = e.fLink;
			fCount--;
		}
		return e;
	}
	
	//----------------------------------------------------
	// fonctions privées at(i)
	//----------------------------------------------------
	
	final private void at(int i)
	{
		TLEvent temp;
		
		if (i != fIndex) {
		
			// limitation éventuelle de i
			if (i < 0) 				i = 0; 
			else if (i > fCount) 	i = fCount;
			
			// analyse des cas
			if ( i < fIndex ) {
				do {
					temp = fPrev.fLink;
					fPrev.fLink = fNext;
					fNext = fPrev;
					fPrev = temp;
					fIndex--;
				} while (i < fIndex);

			} else if (i > fIndex) {
				do {
					temp = fNext.fLink;
					fNext.fLink = fPrev;
					fPrev = fNext;
					fNext = temp;
					fIndex++;
				} while (i > fIndex);
			}
		}
	}
}
