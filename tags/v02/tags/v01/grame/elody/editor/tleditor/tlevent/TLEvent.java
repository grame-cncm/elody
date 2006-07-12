package grame.elody.editor.tleditor.tlevent;

import grame.elody.lang.texpression.expressions.TExp;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.io.Serializable;


//ATTENTION : la durée d'un événement doit tjrs être > 0 !!!!!

/*******************************************************************************************
*
*	 TLEvent (CLASSE ABSTRAITE)
*
*******************************************************************************************/

public abstract class TLEvent implements Serializable {
	public 			TLEvent		fLink;		// lien vers l'événement suivant (librement modifiable) 
	public 			TLEvent		fFather;	// lien vers le "père" (librement modifiable)
	final public 	int			fDur;		// durée de l'événement (> O) (librement consultable)
	final public 	int			fHeight;	// nombre de lignes occupée par l'evnmt (librement consultable)
	final public 	String 		fName;		// description textuelle (librement consultable)
	
	public TLEvent(int d, String str, int h)
	{ 
		fLink = null;
		fFather = null;
		fDur = (d <= 0) ? 1 : d;
		fHeight = h;
		fName = str;
	}
	// gestion des ancêtres
	
	TLEvent getAncestor() 	
	{ 
		TLEvent f;
		if ( (f = fFather) != null) {
			TLEvent g;
			while ( (g = f.fFather) != null ) f = g;
			return f;
		} else {
			return null;
		}
	}
	
	public boolean hasAncestor(TLEvent g) 	
	{ 
		TLEvent f = this.fFather;
		while (f != null && f != g) f = f.fFather;
		return f == g;
	}
	
	public boolean isRelatedTo(TLEvent e) 	
	{ 
		TLEvent an = this.getAncestor();
		return (e != null) && (an != null) && (e.getAncestor() == an);
	}
	

	
	// les méthodes abstraites
	abstract public boolean	isRest();				// indique si l'événement est un silence ou pas
	abstract public TLEvent	makeCopy();				// crée une copie
	abstract public TLEvent	makeResizedCopy(int d);	// crée un nouvel événement avec cette durée
	abstract public TExp	getExp();				// crée un nouvel événement Elody

	abstract public void draw(Graphics g, FontMetrics fm, Color dark, Color light, int x, int y, int w, int h );
}
