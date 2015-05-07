/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

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
	
	public Point getP0() { return new Point(x, y); } //top-left corner
	public Point getP1() { return new Point(x+width, y); } //top-right corner
	public Point getP2() { return new Point(x, y+height); } //bottom-left corner
	public Point getP3() { return new Point(x+width, y+height); } //bottom-right corner
	
	public int getArea() { return height*width; }
	public Point getCenter() { return new Point(x+width/2,y+height/2); }
	public void setCenter(Point p) { setCenter(p.x, p.y); }
	public void setCenter(int x, int y)
	{
		this.x = x - width/2;
		this.y = y - height/2;
	}
	
	public void goAttract(Point p)
	/* moves of 1px this Rect in a direction so that
	 * it is attracted by the p point */
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
	/* moves of 1px this Rect in a direction so that
	 * it is repulsed by the p point */
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
	/* checks all the specified rectangles and returns those
	 * with which it intersects */
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
