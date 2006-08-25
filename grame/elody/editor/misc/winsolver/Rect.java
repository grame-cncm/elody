package grame.elody.editor.misc.winsolver;

import grame.elody.editor.misc.applets.Window;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Vector;

public class Rect extends Rectangle {

	public Rect() { super(); }
	public Rect(Dimension d) { super(d); }
	public Rect(int x, int y, int width, int height) { super(x, y, width, height); }
	public Rect(int width, int height) { super(width, height); }
	public Rect(Point p, Dimension d) { super(p, d); }
	public Rect(Point p) { super(p); }
	public Rect(Rectangle r) { super(r); }
	public Rect(Window w) { super(w.getBounds()); }
	
	public Point getP0() { return new Point(x, y); }
	public Point getP1() { return new Point(x+width, y); }
	public Point getP2() { return new Point(x, y+height); }
	public Point getP3() { return new Point(x+width, y+height); }
	
	public int getArea() { return height*width; }
	public Point getCenter() { return new Point(x+width/2,y+height/2); }
	public void setCenter(Point p) { setCenter(p.x, p.y); }
	public void setCenter(int x, int y)
	{
		this.x = x - width/2;
		this.y = y - height/2;
	}
	
	public void goAttract(Point p)
	{
		if ( p.x > getCenter().x )
			setLocation(x+1, y);
		else if ( p.x < getCenter().x )
			setLocation(x-1, y);
		if ( p.y > getCenter().y )
			setLocation(x, y+1);
		else if ( p.y < getCenter().y )
			setLocation(x, y-1);
	}
			
	public void goRepuls(Point p)
	{
		if ( p.x > getCenter().x )
			setLocation(x-2, y);
		else if ( p.x < getCenter().x )
				setLocation(x+2, y);
		if ( p.y > getCenter().y )
			setLocation(x, y-2);
		else if ( p.y < getCenter().y )
			setLocation(x, y+2);	
	}
	public Vector<Rect> checkCollision(Vector<Rect> rectangles)
	{
		Vector<Rect> result = new Vector<Rect>();
		for (int j=0; j<rectangles.size(); j++)
		{
			Rect r = rectangles.get(j);
			if (this.intersects(r))
				result.add(r);
		}
		return result;
	}

}
