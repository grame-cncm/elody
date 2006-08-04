package grame.elody.editor.tleditor.tlevent;

import grame.elody.editor.misc.TGlobals;

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
	
	private Color negative(Color bgColor)
	// returns a text color so that it is readable
	// on the background color in parameter
	{
		int r = bgColor.getRed();
		int g = bgColor.getGreen();
		int b = bgColor.getBlue();
		int bright = (r+g+b)/3;
		if ((bright>70)&&(bright<170)) // contrast lack 
		{
			int sat = (int) Math.sqrt(((Math.pow(r-bright,2)+Math.pow(g-bright,2)+Math.pow(b-bright,2))/3));
			if (sat<100) // saturation lack
				return Color.white;
		}
		return new Color(255-r,255-g,255-b);
	}

	final public void draw(Graphics g, FontMetrics fm, Color dark, Color light,
			int x, int y, int w, int h) {
		// calcul du début du nom
		String name = fName.contains("Input") ? fName.replaceAll("Input", TGlobals.getTranslation("Input")) : fName;
		int sw = fm.stringWidth(name);
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
		Color main = (ec == null) ? dark : ec;
		g.setColor(main);
		g.fillRect(x, y, w, h);
		g.setColor(light);
		g.drawLine(x + 1, y + 1, x + w - 1, y + 1);
		g.drawLine(x + 1, y + 1, x + 1, y + h - 1);
		g.setColor(Color.black);
		g.drawRect(x, y, w - 1, h);

		// dessin centré du nom
		Color textColor = negative(main);
		g.setColor(textColor);
		g.drawString(name, sx, sy);
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
