package grame.elody.editor.misc;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;

public class BiGraph extends Graphics {
	Graphics origin, dup;
	
	public BiGraph (Graphics g1, Graphics g2, Point dupOffset, Dimension d) {
		origin = g1;
		dup = g2.create(dupOffset.x, dupOffset.y, d.width, d.height);
	}
    public Graphics create() 				{ return origin.create(); }
    public Graphics create (int x, int y, int width, int height) {
    	return origin.create (x, y, width, height);
    }
    public void translate (int x, int y) 	{ origin.translate(x, y); }
    public Color getColor()					{ return origin.getColor(); }
    public void setColor(Color c)			{ origin.setColor(c); dup.setColor(c);}
    public void setPaintMode()				{ origin.setPaintMode(); dup.setPaintMode(); }
    public void setXORMode(Color c1)		{ origin.setXORMode(c1); dup.setXORMode(c1); }
    public Font getFont()					{ return origin.getFont(); }
    public void setFont(Font font)			{ origin.setFont(font); dup.setFont(font); }
// méthode qui n'est plus abstraite depuis JDK1.1
//    public FontMetrics getFontMetrics() 		{ return origin.getFontMetrics(); }
    public FontMetrics getFontMetrics(Font f)	{ return origin.getFontMetrics(f); }

    public Rectangle getClipBounds()			{ return origin.getClipBounds(); }
    public void clipRect(int x, int y, int w, int h)	
    				{ 	origin.clipRect(x, y, w, h); 
    					dup.clipRect(x, y, w, h);}
    public void setClip(int x, int y, int w, int h)
    				{ 	origin.setClip(x, y, w, h); 
    					dup.setClip(x, y, w, h);}
    public void setClip(Shape clip)
    				{ 	origin.setClip (clip); 
    					dup.setClip (clip);}
    public Shape getClip() { return origin.getClip (); }

    public void copyArea(int x, int y, int w, int h, int dx, int dy)
    				{ 	origin.copyArea(x, y, w, h, dx, dy); 
    					dup.copyArea(x, y, w, h, dx, dy);}
    public void drawLine(int x1, int y1, int x2, int y2)
    				{ 	origin.drawLine(x1, y1, x2, y2); 
    					dup.drawLine(x1, y1, x2, y2);}
    public void drawPolyline(int xPoints[], int yPoints[], int nPoints)
    				{ 	origin.drawPolyline(xPoints, yPoints, nPoints); 
    					dup.drawPolyline(xPoints, yPoints, nPoints); }
    public void fillRect(int x, int y, int w, int h)
    				{ origin.fillRect(x, y, w, h); dup.fillRect(x, y, w, h);}
    public void drawRect(int x, int y, int w, int h)
    				{ origin.drawRect(x, y, w, h); dup.drawRect(x, y, w, h);}
    public void clearRect(int x, int y, int w, int h)
    				{ origin.clearRect(x, y, w, h); dup.clearRect(x, y, w, h);}

    public void drawRoundRect(int x, int y, int w, int h, int arcWidth, int arcHeight) { 
    	origin.drawRoundRect(x, y, w, h, arcWidth, arcHeight); 
    	dup.drawRoundRect(x, y, w, h, arcWidth, arcHeight);
    }
    public void fillRoundRect(int x, int y, int w, int h, int arcWidth, int arcHeight){ 
    	origin.fillRoundRect(x, y, w, h, arcWidth, arcHeight); 
    	dup.fillRoundRect(x, y, w, h, arcWidth, arcHeight);
    }
    public void draw3DRect(int x, int y, int w, int h, boolean raised) {
    	origin.draw3DRect(x, y, w, h, raised); 
    	dup.draw3DRect(x, y, w, h, raised);
    }
    public void fill3DRect(int x, int y, int w, int h, boolean raised) {
    	origin.fill3DRect(x, y, w, h, raised); 
    	dup.fill3DRect(x, y, w, h, raised);
    }
    public void drawOval(int x, int y, int w, int h) 
    					{ origin.drawOval(x, y, w, h); dup.drawOval(x, y, w, h); }
    public void fillOval(int x, int y, int w, int h) 
    					{ origin.fillOval(x, y, w, h); dup.fillOval(x, y, w, h); }
    public void drawArc(int x, int y, int w, int h, int startAngle, int arcAngle) {
    	origin.drawArc(x, y, w, h, startAngle, arcAngle); 
    	dup.drawArc(x, y, w, h, startAngle, arcAngle);
    }
    public void fillArc(int x, int y, int w, int h, int startAngle, int arcAngle) {
    	origin.fillArc(x, y, w, h, startAngle, arcAngle); 
    	dup.fillArc(x, y, w, h, startAngle, arcAngle);
    }
    public void drawPolygon(int xPoints[], int yPoints[], int nPoints) {
    	origin.drawPolygon(xPoints, yPoints, nPoints);
    	dup.drawPolygon(xPoints, yPoints, nPoints);
    }
    public void drawPolygon(Polygon p) 
    			{ origin.drawPolygon(p); dup.drawPolygon(p); }

    public void fillPolygon(int xPoints[], int yPoints[], int nPoints) { 
    	origin.drawPolygon(xPoints, yPoints, nPoints); 
    	dup.drawPolygon(xPoints, yPoints, nPoints);
    }
    public void fillPolygon(Polygon p) 
    			{ origin.fillPolygon(p); dup.fillPolygon(p); }
    public void drawString(String str, int x, int y) 
    			{ origin.drawString(str, x, y); dup.drawString(str, x, y); }

    public void drawChars(char data[], int charOffset, int length, int x, int y) {
    	origin.drawChars(data, charOffset, length, x, y); 
    	dup.drawChars(data, charOffset, length, x, y);
    }
    public void drawBytes(byte data[], int charOffset, int length, int x, int y) {
    	origin.drawBytes(data, charOffset, length, x, y); 
    	dup.drawBytes(data, charOffset, length, x, y);
    }
 
    public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
    	return origin.drawImage(img, x, y, observer) && dup.drawImage(img, x, y, observer);
    }
    public boolean drawImage(Image img, int x, int y, int w, int h, ImageObserver observer) {
    	return origin.drawImage(img, x, y, w, h, observer) && dup.drawImage(img, x, y, w, h, observer);
    }
    public boolean drawImage(Image img, int x, int y, Color bgcolor, ImageObserver observer) {
    	return origin.drawImage(img, x, y, bgcolor, observer) && dup.drawImage(img, x, y, bgcolor, observer);
    }
    public boolean drawImage(Image img, int x, int y, int w, int h, Color bgcolor, ImageObserver obs) {
    	return origin.drawImage(img, x, y, w, h, bgcolor, obs) &&  dup.drawImage(img, x, y, w, h, bgcolor, obs);
    }
    public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2,
				      int sx1, int sy1, int sx2, int sy2, ImageObserver obs) {
    	return origin.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, obs) &&  
    			dup.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, obs);
    }
    public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2,
				      		int sx1, int sy1, int sx2, int sy2, Color bgcolor, ImageObserver obs) {
    	return origin.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, bgcolor, obs) &&  
    			dup.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, bgcolor, obs);
    }

    public void dispose() 		{ }
    public void finalize() 		{ }
    public String toString() 	{ return getClass().getName();}
	public void drawString(AttributedCharacterIterator arg0, int arg1, int arg2) { }
}
