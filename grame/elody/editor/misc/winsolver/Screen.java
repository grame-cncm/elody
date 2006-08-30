package grame.elody.editor.misc.winsolver;

import grame.elody.editor.constructors.Stock;
import grame.elody.editor.main.Elody;
import grame.elody.editor.misc.TGlobals;
import grame.elody.editor.misc.appletframe.AppletFrame;
import grame.elody.editor.misc.applets.Window;

import java.awt.Frame;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Vector;

import org.eclipse.swt.widgets.Shell;


public class Screen {
	
	protected Rectangle screen;
	protected Point screenCenter;
	protected Vector<Window> vectWindow;
	protected TreeSet<Window> vect1Window;
	protected TreeSet<Window> vect2Window;
	protected TreeSet<Rect> vect2Rect;
	protected Vector<Rect> resultRect;
	protected Vector<Point> anchPoints;
	
	/* indicateurs à TRUE si les fenêtres n'ont
	   pas bougé depuis la dernière réorganisation : */
	public boolean c1 = false;
	public boolean c2 = false;

	public Screen() {
		screen = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		screenCenter = new Point((int)screen.getCenterX(), (int)screen.getCenterY());
		vectWindow = new Vector<Window>();
		resultRect = new Vector<Rect>();
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
				Rect r = resultRect.get(j);
				System.out.println("x="+r.x+" ; y="+r.y+" ; l="+r.width+" ; h="+r.height);
			}
	}
	
	public void showAll()
	/* affiche toutes les fenêtres */
	{
		for (int i=0; i<vectWindow.size(); i++)
		{
			Window w = vectWindow.get(i);
			w.show();
		}
	}
	
	public void closeAll()
	/* ferme toutes les fenêtres à l'exception du menu Elody */
	{
		boolean stopClosing = false;
   		while(!(Stock.stocks.isEmpty()||stopClosing))
   		{
   			stopClosing = Stock.stocks.firstElement().showConfirmDialog();
   		}
   		if (!stopClosing)
   	   	// si l'utilisateur choisit ANNULER dans le ConfirmDialog
   	   	// d'un Stock, le processus de fermeture est stoppé
   		{
   			for (int i=1; i<vectWindow.size(); i++)
   			{
   				Window w = vectWindow.get(i);
   				w.close();
   			}
   		}
	}
	
	public void compute1()
	/* réorganise les fenêtres en optimisant l'espace occupé par une attraction
	   vers le coin en haut à gauche de l'écran OU en cascade s'il n'y a pas
	   assez d'espace */
	{
		if (!c1)
		{
			vect1Window = new TreeSet<Window>(new WindowDiagComp());
			vect1Window.addAll(vectWindow);
			Iterator<Window> i = vect1Window.iterator();
			resultRect = new Vector<Rect>();
			anchPoints = new Vector<Point>();
			anchPoints.add(new Point(0,0));
			while (i.hasNext())
			{
				Window w = i.next();
				Rect r = new Rect(w);
				Vector<PointEligible> anchPointsEligible = new Vector<PointEligible>();
				for (int j=0; j<anchPoints.size(); j++)
				{
					Point p = anchPoints.get(j);
					if (canAnchor(r,p))
					{
						PointEligible pe = new PointEligible(p);
						anchPointsEligible.add(pe);
					}
				}
				int minArea=-1;
				for (int j=0; j<anchPointsEligible.size(); j++)
				{
					TreeSet<Point> red = new TreeSet<Point>(new RedComp());
					TreeSet<Point> blue = new TreeSet<Point>(new BlueComp());
					PointEligible p0 = anchPointsEligible.get(j);
					for (int k=0; k<anchPoints.size(); k++)
					{
						Point p1 = anchPoints.get(k);
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
					PointEligible p0 = anchPointsEligible.get(j);
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
				resultRect = new Vector<Rect>();
				for (int j=0; j<vectWindow.size(); j++)
				{
					Window w = vectWindow.get(j);
					Rect r = new Rect(w);
					r.setLocation((15*j)%screen.width, (30*j)%screen.height);
					w.setBounds(r);
					w.requestFocus();
					resultRect.add(r);
				}
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
			vect1Window = new TreeSet<Window>(new WindowAreaComp());
			vect1Window.addAll(vectWindow);
			Rect bigger = new Rect((Window) vect1Window.first());
			Point bary = bigger.getCenter();
			vect2Window = new TreeSet<Window>(new WindowMagnPtComp(bary));
			vect2Rect = new TreeSet<Rect>(new RectMagnPtComp(bary));
			vect2Window.addAll(vectWindow);
			for (int i=0; i<vectWindow.size(); i++)
				vect2Rect.add(new Rect(vectWindow.get(i)));
			for (int z=0; z<1000; z++)
			{
				Iterator<Rect> i = vect2Rect.iterator();
				while (i.hasNext())
				{
					Rect r = i.next();
					if (!r.equals(bigger))
					{
						r.goAttract(bary);
						Vector<Rect> everyRect = new Vector<Rect>(vect2Rect);
						everyRect.remove(r);
						Vector<Rect> collisionRect = r.checkCollision(everyRect);
						for (int j=0; j<collisionRect.size(); j++)
						{
							Rect cr = collisionRect.get(j);
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
			Iterator<Rect> iR = vect2Rect.iterator();
			Iterator<Window> iF = vect2Window.iterator();
			while (iR.hasNext())
			{
				iF.next().setBounds(iR.next());
			}
			resultRect = new Vector<Rect>();
			Vector<Window> v = new Vector<Window>(vect2Window); 
			for (int j=0; j<v.size(); j++)
			{
				Window w = v.get(j);
				Rect r = new Rect(w);
				resultRect.add(r);
			}
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
			Rect rVisit = resultRect.get(i);
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

class WindowDiagComp implements Comparator<Window> {

	public int compare(Window w0, Window w1) {
		int d0 = (int) (Math.pow(w0.getHeight(),2)+Math.pow(w0.getWidth(), 2));
		int d1 = (int) (Math.pow(w1.getHeight(),2)+Math.pow(w1.getWidth(), 2));
		return (d1-d0)==0 ? 1 : (d1-d0);
	}	
}

class WindowAreaComp implements Comparator<Window> {

	public int compare(Window w0, Window w1) {
		Rect r0 = new Rect(w0);
		Rect r1 = new Rect(w1);
		return (r1.getArea()-r0.getArea())==0 ? 1 : r1.getArea()-r0.getArea();
	}	
}

class WindowMagnPtComp implements Comparator<Window> {

	protected Point magnPt;
	public WindowMagnPtComp(Point p) { super(); magnPt = p; }

	public int compare(Window w0, Window w1) {
		Rect r0 = new Rect(w0);
		Rect r1 = new Rect(w1);
		Point p0 = r0.getCenter();
		Point p1 = r1.getCenter();
		int d0 = (int) p0.distance(magnPt);
		int d1 = (int) p1.distance(magnPt);		
		return (d0-d1)==0 ? 1 : d0-d1;
	}	
}

class RectMagnPtComp implements Comparator<Rect> {

	protected Point magnPt;
	public RectMagnPtComp(Point p) { super(); magnPt = p; }

	public int compare(Rect r0, Rect r1) {
		Point p0 = r0.getCenter();
		Point p1 = r1.getCenter();
		int d0 = (int) p0.distance(magnPt);
		int d1 = (int) p1.distance(magnPt);		
		return (d0-d1)==0 ? 1 : d0-d1;
	}	
}

class RedComp implements Comparator<Point> {

	public int compare(Point p0, Point p1) {
		return p0.x-p1.x;
	}	
}

class BlueComp implements Comparator<Point> {

	public int compare(Point p0, Point p1) {
		return p0.y-p1.y;
	}	
}