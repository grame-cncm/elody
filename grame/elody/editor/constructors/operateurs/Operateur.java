package grame.elody.editor.constructors.operateurs;

import grame.elody.lang.TExpMaker;
import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.lang.texpression.expressions.TNullExp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;

public abstract class Operateur extends Panel {
	Image image; String name;
	
	public Operateur (Image img) {
		super ();
		image = img;
	}
    public abstract TExp compose(TExpMaker maker, TExp e1, TExp e2);

    public TExp compose(TExp e1, TExp e2) {
		if (isNullExp (e1, e2)) return getNull(e1, e2);
    	TExpMaker maker = TExpMaker.gExpMaker;
  		TExp e = compose (maker, e1, e2);
   		maker.updateHistory (e);
   		return e;
    }
    public TExp getNull (TExp e1, TExp e2) {
		if (e1 instanceof TNullExp) return e1;
		if (e2 instanceof TNullExp) return e2;
		return null;
    }
    public boolean isNullExp (TExp e1, TExp e2) {
    	return (e1 instanceof TNullExp) || (e2 instanceof TNullExp);
    }
    public void paint (Graphics g) {
    	Dimension d = getSize();
    	int offset = 6;
    	d.width-= offset*2; d.height-= offset*2;
    	if (image == null) {
     		FontMetrics fm = getFontMetrics (getFont());
     	//	int textH = fm.getHeight();
     		int textW = fm.stringWidth(name);
			g.setColor(Color.black);
			g.drawString (name, offset+(d.width-textW)/2, offset+(d.height/2));
		}
		else {
			int w; do { w = image.getWidth(null);  } while (w < 0);
			int h; do { h = image.getHeight(null); } while (h < 0);
		/*	if (w > d.width)  w = d.width;
			if (h > d.height) h = d.height; */
			g.drawImage (image, offset+(d.width-w)/2, offset+(d.height-h)/2, w, h, this);
		}
    }
    public String toString() 	{ return new String ("Operateur"); }
}
