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
/* DESCRIPTION OF WINDOWS REORGANIZATION STRATEGIES:
 * 1) The first strategy is to reorganize the screen's windows in order to optimize
 * the space occupation with an attraction to the top-left screen's corner OR in cascade
 * if there is not enough space. We will use the concept of "anchor points": each window
 * might cling an anchor point, i.e. this window might be moved until its top-left corner
 * point corresponds to the specified anchor point. The first anchor point is the
 * top-left corner of the screen, so the first window will cling this point and be located at
 * the top-left corner of the screen. When a window clings to an anchor point, this point
 * loses its anchor statute because it is not available anymore: each anchor point can hold
 * only one window. But when a window clings, it creates two new anchor points: its top-right
 * corner point, and its bottom-left corner point. An anchor point is eligible for a window
 * if once clung, this window is not going to get out of the screen, or to intersect with
 * another already clung window. When there is more than one available eligible anchor point,
 * the next window has to choose which one is the best, i.e. which one will cause an optimal
 * space distribution, i.e. which one will generate a minimal loss of area.
 * To compute this loss of area for each anchor point, we will split all available anchor points
 * in two groups, that we will call red and blue for more facilities.
 * You have to imagine a virtual line splitting the screen in two parts by joining the top-left
 * corner of the screen to the candidate anchor point. Every other anchor point that is in upper
 * part is a red one, every anchor point that is in lower part is a blue one.
 * Every red point that is before the top-right corner of the candidate window (once clung) on
 * the horizontal axis will cause a loss of area. Similarly, every blue point that is before
 * the bottom-left corner of the candidate window (once clung) on the vertical axis will also
 * cause a loss of area.
 * ************************************
 * 2) The second strategy is to reorganize the screen's windows by using a heuristic method
 * in order to optimize the space occupation with an attraction to the biggest window, and
 * preventing from exceeding the screen's limits. This strategy will preserve as much as possible
 * the initial location of each window, and give as illusion that the windows have been magnetized.
 * All the windows will one after the others undergo an infinitesimal movement (i.e. 1 pixel) so that
 * it will at the same time be attracted by the biggest window (that do not move), i.e. its barycenter
 * point, and be repulsed by other windows with which it intersects, i.e. their barycenter points, and
 * also be repulsed by the screen borders.
 * All the windows will move step by step, one after each others, until an equilibrium will be reached.
 * As computing an equilibrium is rather difficult, we will consider that there is a great probability
 * that this equilibrium may be reached after 1000 iterations. 
 */

	
	protected Rectangle screen; // virtual representation of hardware screen
	//protected Point screenCenter; // center point of the hardware screen
	protected Vector<Window> vectWindow; // virtual representation of all the screen's Elody's windows
	protected TreeSet<Window> vect1Window; // virtual representation of all the screen's Elody's windows, 
										  // sorted by diagonal length OR area value (used by computing methods)
	protected TreeSet<Window> vect2Window; // virtual representation of all the screen's Elody's windows, 
										  // sorted by proximity to the biggest of these windows
	protected TreeSet<Rect> vect2Rect; // same as vect2Window, but for manipulating Rect instead of Window
	protected Vector<Rect> resultRect; // contains all the Rect that have already been computed
	protected Vector<Point> anchPoints; // contains all the points to which a Rect can be anchored in the first computing strategy
	// these points are available top-right and bottom-left corners of already-computed rectangles
	
	/* flags are TRUE if windows did not move since last reorganization computing,
	 *  i.e. if there is no need to recompute: */
	public boolean c1 = false; // for first computing strategy
	public boolean c2 = false; // for second computing strategy

	public Screen() {
		screen = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		//screenCenter = new Point((int)screen.getCenterX(), (int)screen.getCenterY());
		vectWindow = new Vector<Window>();
		resultRect = new Vector<Rect>();
	}
	
	// AWT methods:
	public void addWindow(Frame frame)	{ addWindow(new Window(frame)); }
	public void delWindow (Frame frame) { delWindow(new Window(frame)); }
	// SWT methods:
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
	
	public String toString()
	{
		String s="";
		if (resultRect==null)
			s="no solution";
		else
			for (int j=0; j<resultRect.size(); j++)
			{
				Rect r = resultRect.get(j);
				s+="[x="+r.x+"; y="+r.y+"; l="+r.width+"; h="+r.height+"]; ";
			}
		return s;
	}
	
	public void showAll()
	/* shows all windows */
	{
		for (int i=0; i<vectWindow.size(); i++)
		{
			Window w = vectWindow.get(i);
			w.show();
		}
	}
	
	public void closeAll()
	/* close all windows except the Elody menu */
	{
		boolean stopClosing = false;
   		while(!(Stock.stocks.isEmpty()||stopClosing))
   		{
   			stopClosing = Stock.stocks.firstElement().showConfirmDialog();
   		}
   		if (!stopClosing)
   	   	// if the user chooses CANCEL in the ConfirmDialog
   	   	// of a Stock, the closing process is stopped
   		{
   			for (int i=1; i<vectWindow.size(); i++)
   			{
   				Window w = vectWindow.get(i);
   				w.close();
   			}
   		}
	}
	
	public void compute1()
	/* first strategy to reorganize the screen's windows: to optimize the space occupation with an attraction
	   to the top-left screen's corner OR in cascade if there is not enough space */
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
			//System.out.println(toString());
			c1 = true;
		}
	}
	
	public void compute2()
	/* second strategy to reorganize the screen's windows: heuristic method
	 * to optimize the space occupation with an attraction to the biggest window,
	 * and preventing from exceeding the screen's limits */
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
			// System.out.println(toString());
			c2 = true;
		}
	}
	
	protected boolean canAnchor(Rect r, Point p)
	/* Determine if the r rectangle can cling to the p anchor point
	 * without getting out of the screen and intersecting with 
	 * other rectangles */
	{
		r.setLocation(p);
		Point p1 = r.getP1(); //top-right corner
		Point p2 = r.getP2(); //bottom-left corner
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
	/* Returns a Rect that represents the window's bar of the specified Rect
	 * It can be used to prevent this bar from getting out of the screen during
	 * a computing process. */
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
/* sorting windows on their diagonal length */
	public int compare(Window w0, Window w1) {
		int d0 = (int) (Math.pow(w0.getHeight(),2)+Math.pow(w0.getWidth(), 2));
		int d1 = (int) (Math.pow(w1.getHeight(),2)+Math.pow(w1.getWidth(), 2));
		return (d1-d0)==0 ? 1 : (d1-d0);
	}	
}

class WindowAreaComp implements Comparator<Window> {
/* sorting windows on their area value */
	public int compare(Window w0, Window w1) {
		Rect r0 = new Rect(w0);
		Rect r1 = new Rect(w1);
		return (r1.getArea()-r0.getArea())==0 ? 1 : r1.getArea()-r0.getArea();
	}	
}

class WindowMagnPtComp implements Comparator<Window> {
/* sorting windows on their proximity to a specified point */
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
/* sorting rectangles on their proximity to a specified point */
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