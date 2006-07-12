package grame.elody.editor.controlers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

public class ButtonControler extends Controler
{
	private double homePos;
	Image image; int imageWidth;
	
	public ButtonControler (int min, int max, int home, Color inColor, Image img) {
		super (min, max, inColor);
		image = img;
    	if (image != null) do { 
    		imageWidth = image.getWidth(null);
    	} while (imageWidth < 0);
		init (min, max, home);
	}
    public void setRange (int min, int max, int newValue, int home) { 
		homePos = Math.PI - ((max - home) * 2 * Math.PI) / (max - min + 1);
		super.setRange (min, max, newValue, home);
    }
    protected void init (int min, int max, int home) {
		direction = kVertical;
		homePos = Math.PI - ((max - home) * 2 * Math.PI) / (max - min + 1);
		setValue (home);
	}
    public int PointToVal(int x, int y) {
    	Dimension d = getSize();
  		return ArcToVal (Math.atan2 (x - d.width/2, y - d.height/2));
    }
    public int getWidth () {
    	Dimension d = getSize();
   		return Math.min (d.width, d.height);
    }
    protected double ValToArc (int val) {
    	return homePos + ((max - val) * 2 * Math.PI) / (max - min + 1);
    }
    protected int ArcToVal (double arc) {
    	arc -= homePos;
		if (arc < 0) arc = (2 * Math.PI) + arc;
    	return max - (int)(arc * (max - min + 1) / (2 * Math.PI));
    }
    protected int ValToX (int val, int r) {
		return (int)(r + r * Math.sin( ValToArc(val)));
    }
    protected int ValToY (int val, int r) {
		return (int)(r + r * Math.cos( ValToArc(val)));
    }
    public Dimension getMinimumSize () {
    	int w = (image != null) ? getImageWidth() : 15;
		return new Dimension (w, w);
    }
    public Dimension getPreferredSize () {
		return getMinimumSize ();
    }
    protected int getImageWidth () {
     	Dimension d = getSize();
  		int w = Math.min(d.width, d.height);
		if ((image != null) && (imageWidth < w)) w = imageWidth;
		return w;
    }
    public void paint(Graphics g) {
    	Dimension d = getSize();
   		int w = getImageWidth();
   		int x = (d.width  - w) / 2;
   		int y = (d.height - w) / 2;
    	if (image == null) {
    		g.clearRect (0,0,d.width,d.height);
			g.setColor (inColor);
			g.fillOval (x, y, w, w);
			g.setColor (Color.black);
			g.drawOval (x, y, w, w);
		}
		else {
			g.drawImage (image, (d.width-w)/2, (d.height-w)/2, w, w, this);
		}
		int v = getValue();
		int r = w / 2;
		int bx = x +3 + ValToX (v, r-3);
		int by = y +3 + ValToY (v, r-3);
		g.setColor (Color.white);
		g.drawLine (x + r, y + r, bx, by);
    }
   	public void update(int oldValue, int newValue) {
    	Graphics g = getGraphics ();
    	if ((g!=null) && (oldValue!=newValue)) {
    		paint(g);
		}
 	}
    public synchronized boolean contains (int x, int y) {
		int w = getImageWidth();
 		Dimension d = getSize();
		Rectangle r = new Rectangle((d.width-w)/2, (d.height-w)/2, w, w);
		return r.contains (x, y);
    }
}

