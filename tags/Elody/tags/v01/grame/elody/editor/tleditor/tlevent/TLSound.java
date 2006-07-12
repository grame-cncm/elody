package grame.elody.editor.tleditor.tlevent;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

/*******************************************************************************
 * 
 * TLSound (non-silence, class abstraite)
 * 
 ******************************************************************************/
public abstract class TLSound extends TLEvent {
	public TLSound(int d, String str, int h) {
		super(d, str, h);
	}

	// implémentation des méthodes abstraites

	public final boolean isRest() {
		return false;
	}

	final public void draw(Graphics g, FontMetrics fm, Color dark, Color light,
			int x, int y, int w, int h) {
		// calcul du début du nom
		int sw = fm.stringWidth(fName);
		int sx = x + Math.max(1, (w - sw) / 2);
		int sh = fm.getHeight();
		int sy = y + (h + sh) / 2;

		// limitation de la taille des rectangles (bug de l'awt)
		if (x < 0) {
			w += x;
			x = 0;
		}
		;
		if (w > 2000)
			w = 2000;

		// dessin du rectangle
		Color ec = getColor();
		g.setColor((ec == null) ? dark : ec);
		g.fillRect(x, y, w, h);
		g.setColor(light);
		g.drawLine(x + 1, y + 1, x + w - 1, y + 1);
		g.drawLine(x + 1, y + 1, x + 1, y + h - 1);
		g.setColor(Color.black);
		g.drawRect(x, y, w - 1, h);

		// dessin centré du nom
		g.drawString(fName, sx, sy);
	}

	// méthodes abstraites à implémenter
	/*
	 * abstract public TLEvent makeCopy(); // crée une copie abstract public
	 * TLEvent makeResizedCopy(int d); // crée un nouvel événement avec cette
	 * durée
	 * 
	 */
	abstract public Color getColor(); //donne la couleur
}
