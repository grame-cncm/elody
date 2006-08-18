package grame.elody.editor.misc.winsolver;

import grame.elody.editor.misc.applets.Window;

import java.awt.Frame;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Vector;

import org.eclipse.swt.widgets.Shell;


public class Screen {
	
	protected Rectangle screen;
	protected Point screenCenter;
	protected Vector vectWindow;
	protected TreeSet vect1Window;
	protected TreeSet vect2Window;
	protected TreeSet vect2Rect;
	protected Vector resultRect;
	protected Vector anchPoints;
	
	/* indicateurs à TRUE si les fenêtres n'ont
	   pas bougé depuis la dernière réorganisation : */
	public boolean c1 = false;
	public boolean c2 = false;

	public Screen() {
		screen = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		screenCenter = new Point((int)screen.getCenterX(), (int)screen.getCenterY());
		vectWindow = new Vector();
		resultRect = new Vector();
	}
	
	public void addWindow(Frame frame)	{ addWindow(new Window(frame)); }
	public void delWindow (Frame frame) { delWindow(new Window(frame)); }
	public void addWindow(Shell shell)  { addWindow(new Window(shell));	}
	public void delWindow (Shell shell) { delWindow(new Window(shell)); }
	
	public void addWindow(Window window)
	{
		vectWindow.add(window);
	}
	public void delWindow (Window window)
	{
		vectWindow.remove(window);
	}
	
	public void printResult()
	{
		if (resultRect==null)
			System.out.println("pas de solution");
		else
			for (int j=0; j<resultRect.size(); j++)
			{
				Rect r = (Rect) resultRect.get(j);
				System.out.println("x="+r.x+" ; y="+r.y+" ; l="+r.width+" ; h="+r.height);
			}
	}
	
	public void showAll()
	/* affiche toutes les fenêtres */
	{
		for (int i=0; i<vectWindow.size(); i++)
		{
			Window w = (Window) vectWindow.get(i);
			w.show();
		}
	}
	
	public void closeAll()
	/* ferme toutes les fenêtres à l'exception du menu Elody */
	{
		for (int i=1; i<vectWindow.size(); i++)
		{
			Window w = (Window) vectWindow.get(i);
			w.close();
		}
	}
	
	public void compute1()
	/* réorganise les fenêtres en optimisant l'espace occupé par une attraction
	   vers le coin en haut à gauche de l'écran OU en cascade s'il n'y a pas
	   assez d'espace */
	{
		if (!c1)
		{
			vect1Window = new TreeSet(new WindowDiagComp());
			vect1Window.addAll(vectWindow);
			Iterator i = vect1Window.iterator();
			resultRect = new Vector();
			anchPoints = new Vector();
			anchPoints.add(new Point(0,0));
			while (i.hasNext())
			{
				Window w = (Window) i.next();
				Rect r = new Rect(w);
				Vector anchPointsEligible = new Vector();
				for (int j=0; j<anchPoints.size(); j++)
				{
					Point p = (Point) anchPoints.get(j);
					if (canAnchor(r,p))
					{
						PointEligible pe = new PointEligible(p);
						anchPointsEligible.add(pe);
					}
				}
				int minArea=-1;
				for (int j=0; j<anchPointsEligible.size(); j++)
				{
					TreeSet red = new TreeSet(new RedComp());
					TreeSet blue = new TreeSet(new BlueComp());
					PointEligible p0 = (PointEligible) anchPointsEligible.get(j);
					for (int k=0; k<anchPoints.size(); k++)
					{
						Point p1 = (Point) anchPoints.get(k);
						int crossProduct = p0.x*p1.y-p0.y*p1.x;
						if ((crossProduct<0)&&(p1.x<(p0.x+r.width))) red.add(p1);
						if ((crossProduct>0)&&(p1.y<(p0.y+r.height))) blue.add(p1);
					}
					int areaRedLost = 0;
					if (red.size()>0)
					{
						Point pr = (Point) red.first();
						Rect totalRedLost = new Rect(pr.x, pr.y, p0.x+r.width, p0.y);
						areaRedLost = totalRedLost.getArea();
					}
					int areaBlueLost = 0;
					if (blue.size()>0)
					{
						Point pb = (Point) blue.first();
						Rect totalBlueLost = new Rect(pb.x, pb.y, p0.x, p0.y+r.height);
						areaBlueLost = totalBlueLost.getArea();
					}
					p0.area = areaRedLost+areaBlueLost;
					if ((minArea==-1)||(p0.area<minArea))	minArea = p0.area;
				}
				Point bestPoint=null;
				for (int j=0; j<anchPointsEligible.size(); j++)
				{
					PointEligible p0 = (PointEligible) anchPointsEligible.get(j);
					if (p0.area==minArea)	bestPoint=new Point(p0);
				}
				if (bestPoint==null)
				{
					resultRect=null;
					break;
				}
				else
				{
					r.setLocation(bestPoint);
					anchPoints.remove(bestPoint);
					anchPoints.add(r.getP1());
					anchPoints.add(r.getP2());
					resultRect.add(r);
					w.setBounds(r);
				}
			}
			if (resultRect==null)
			{
				for (int j=0; j<vectWindow.size(); j++)
				{
					Window w = (Window) vectWindow.get(j);
					Rect r = new Rect(w);
					r.setLocation((15*j)%screen.width, (30*j)%screen.height);
					w.setBounds(r);
					w.requestFocus();
				}
				resultRect = new Vector(vectWindow);
			}
			//printResult();
			c1 = true;
		}
	}
	
	public void compute2()
	/* réorganise les fenêtres de manière heuristique en optimisant l'espace
	   occupé par une attraction vers la fenêtre la plus grande
	   en empêchant toute sortie de l'écran */
	{
		if (!c2)
		{
			vect1Window = new TreeSet(new WindowAreaComp());
			vect1Window.addAll(vectWindow);
			Rect bigger = new Rect((Window) vect1Window.first());
			Point bary = bigger.getCenter();
			vect2Window = new TreeSet(new WindowMagnPtComp(bary));
			vect2Rect = new TreeSet(new RectMagnPtComp(bary));
			vect2Window.addAll(vectWindow);
			for (int i=0; i<vectWindow.size(); i++)
				vect2Rect.add(new Rect((Window)vectWindow.get(i)));
			for (int z=0; z<1000; z++)
			{
				Iterator i = vect2Rect.iterator();
				while (i.hasNext())
				{
					Rect r = (Rect) i.next();
					if (!r.equals(bigger))
					{
						r.goAttract(bary);
						Vector everyRect = new Vector(vect2Rect);
						everyRect.remove(r);
						Vector collisionRect = r.checkCollision(everyRect);
						for (int j=0; j<collisionRect.size(); j++)
						{
							Rect cr = (Rect) collisionRect.get(j);
							r.goRepuls(cr.getCenter());
						}
					}
					if (!screen.contains(getBar(r)))
					{
						int nx = (int) r.getX();
						int ny = (int) r.getY();
						if (nx < screen.x)	nx = screen.x;
						if (ny < screen.y)	ny = screen.y;
						if ( (nx+r.getWidth()) > (screen.getX()+screen.getWidth()) )
							nx=(int)(screen.getX()+screen.getWidth()-r.getWidth());
						if ( (ny+r.getHeight()) > (screen.getY()+screen.getHeight()) )
							ny=(int)(screen.getY()+screen.getHeight()-r.getHeight());
						r.setLocation(nx, ny);
					}
				}
			}
			Iterator iR = vect2Rect.iterator();
			Iterator iF = vect2Window.iterator();
			while (iR.hasNext())
			{
				Window f = (Window) iF.next();
				f.setBounds((Rect) iR.next());
			}
			resultRect = new Vector(vect2Window);
			//printResult();
			c2 = true;
		}
	}
	
	protected boolean canAnchor(Rect r, Point p)
	{
		r.setLocation(p);
		Point p1 = r.getP1();
		Point p2 = r.getP2();
		if (!(screen.contains(p1)&&screen.contains(p2)))
			return false;
		for (int i=0; i<resultRect.size(); i++)
		{
			Rect rVisit = (Rect) resultRect.get(i);
			//if (rVisit.contains(p1)||rVisit.contains(p2))
			if (r.intersects(rVisit))
				return false;
		}
		return true;
	}
	
	static public Rect getBar(Rect r)
	{
		return new Rect(r.x, r.y, (int) r.getWidth(), 30);
	}
	
}

class PointEligible extends Point {

	public int area;
	
	public PointEligible() { super(); }
	public PointEligible(int x, int y) { super(x, y); }
	public PointEligible(Point p) { super(p); }
	
}

class WindowDiagComp implements Comparator {

	public int compare(Object o0, Object o1) {
		Window w0 = (Window) o0;
		Window w1 = (Window) o1;
		int d0 = (int) (Math.pow(w0.getHeight(),2)+Math.pow(w0.getWidth(), 2));
		int d1 = (int) (Math.pow(w1.getHeight(),2)+Math.pow(w1.getWidth(), 2));
		return (d1-d0)==0 ? 1 : (d1-d0);
	}	
}

class WindowAreaComp implements Comparator {

	public int compare(Object o0, Object o1) {
		Rect r0 = new Rect ((Window) o0);
		Rect r1 = new Rect ((Window) o1);
		return (r1.getArea()-r0.getArea())==0 ? 1 : r1.getArea()-r0.getArea();
	}	
}

class WindowMagnPtComp implements Comparator {

	protected Point magnPt;
	public WindowMagnPtComp(Point p) { super(); magnPt = p; }

	public int compare(Object o0, Object o1) {
		Rect r0 = new Rect ((Window) o0);
		Rect r1 = new Rect ((Window) o1);
		Point p0 = r0.getCenter();
		Point p1 = r1.getCenter();
		int d0 = (int) p0.distance(magnPt);
		int d1 = (int) p1.distance(magnPt);		
		return (d0-d1)==0 ? 1 : d0-d1;
	}	
}

class RectMagnPtComp implements Comparator {

	protected Point magnPt;
	public RectMagnPtComp(Point p) { super(); magnPt = p; }

	public int compare(Object o0, Object o1) {
		Rect r0 = (Rect) o0;
		Rect r1 = (Rect) o1;
		Point p0 = r0.getCenter();
		Point p1 = r1.getCenter();
		int d0 = (int) p0.distance(magnPt);
		int d1 = (int) p1.distance(magnPt);		
		return (d0-d1)==0 ? 1 : d0-d1;
	}	
}

class RedComp implements Comparator {

	public int compare(Object o0, Object o1) {
		Point p0 = (Point) o0;
		Point p1 = (Point) o1;
		return p0.x-p1.x;
	}	
}

class BlueComp implements Comparator {

	public int compare(Object o0, Object o1) {
		Point p0 = (Point) o0;
		Point p1 = (Point) o1;
		return p0.y-p1.y;
	}	
}