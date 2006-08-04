package grame.elody.editor.constructors;

import grame.elody.editor.controlers.BarControler;
import grame.elody.editor.controlers.EditBarControler;
import grame.elody.editor.controlers.TextBarCtrl;
import grame.elody.editor.expressions.ExprHolder;
import grame.elody.editor.expressions.FunctionTag;
import grame.elody.editor.expressions.TNotesVisitor;
import grame.elody.editor.misc.Define;
import grame.elody.editor.misc.TGlobals;
import grame.elody.editor.misc.applets.BasicApplet;
import grame.elody.editor.misc.draganddrop.DragAble;
import grame.elody.editor.misc.draganddrop.DragAndDropTool;
import grame.elody.editor.misc.draganddrop.DropAble;
import grame.elody.editor.misc.draganddrop.TExpContent;
import grame.elody.lang.TEvaluator;
import grame.elody.lang.TExpMaker;
import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.lang.texpression.expressions.TNullExp;
import grame.elody.util.MsgNotifier;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

public class BegEndConstructor extends BasicApplet {
	static final String appletName = "BegEndConstructor";
	static final int begType = 1;
	static final int endType = 2;
	BegEndExprHolder eh;
	TimeControlerPanel beg, end;
	
	public BegEndConstructor() {
		super(TGlobals.getTranslation("Begin_End"));
		setLayout(new BorderLayout(4,4));
		setSize (410, 137);
	} 
    public void init() {
    	int defaultBeg=1000, defaultEnd=10000;
		eh = new BegEndExprHolder (defaultBeg, defaultEnd);
		add ("Center", eh);

		beg = new TimeControlerPanel (TGlobals.getTranslation("Begin"), BegEndExprHolder.beginMsg);
		beg.init (eh, defaultBeg);
		add ("West", beg);

		end = new TimeControlerPanel (TGlobals.getTranslation("End"), BegEndExprHolder.endMsg);
		end.init (eh, defaultEnd);
		add ("East", end);
		moveFrame (250, 350);
	}   
	public int getFType (TExp exp) {
		try {
			exp = exp.convertAbstrExp();
			exp = TExpMaker.gExpMaker.createBody(exp.getArg1(), exp.getArg2());
			exp = exp.convertAbstrExp();
			exp = TExpMaker.gExpMaker.createBody(exp.getArg1(), exp.getArg2());
			if (exp.convertBeginExp() != null) return begType;
			if (exp.convertRestExp() != null) return endType;
		}
		catch (Exception e) { }
		return 0;
	}
	public void decompose (TExp exp) {
   		TExpMaker maker = TExpMaker.gExpMaker;
		TExp begExp, endExp;
		TExp a = exp.convertApplExp();
		if (a == null) return;
		TExp body = a.getArg2();
		a = a.getArg1().convertApplExp();
		if (a == null) return;
		TExp arg1 = a.getArg2();
		TExp f = a.getArg1();
		if (f.convertApplExp() == null) {
			switch (getFType (f)) {
				case begType: 
					begExp = arg1;
					endExp = maker.createNote(Color.blue, 60, 100, 0, (int)TimeControlerPanel.timeLimit);
					break;
				case endType:
					begExp = maker.createNote(Color.blue, 60, 100, 0, 0);
					endExp = arg1;
					break;
				default: return;
			}
		}
		else {
			begExp = arg1;
			endExp = maker.createSeq(arg1, f.convertApplExp().getArg2());
		}
		eh.setExpression (body);
		beg.drop (begExp, null);
		end.drop (endExp, null);
	}
}

class EndDrag extends BegEndDrag
{
	public EndDrag (ExprHolder main, TExp expr, int val, Graphics g, Point p, Dimension d) {
		super (main, expr, val, g, p, d);
	}
	public TExp buildFunction () {
		TExpMaker maker = TExpMaker.gExpMaker;
		TExp exp = maker.createNote(Color.blue, 60, 100, 0, 1000);
		TExp note = maker.createNote( Color.black, 60, 100, 0, 10);
		TExp f = maker.createAbstr(exp, maker.createRest(exp, note));
		return maker.createAbstr(note, f);
	}
	public int getArgsCount () 		{ return 2; }
}

class BegDrag extends BegEndDrag
{
	public BegDrag (ExprHolder main, TExp expr, int val, Graphics g, Point p, Dimension d) {
		super (main, expr, val, g, p, d);		
	}
	public TExp buildFunction () {
		TExpMaker maker = TExpMaker.gExpMaker;
		TExp exp = maker.createNote(Color.blue, 60, 100, 0, 1000);
		TExp note = maker.createNote( Color.black, 60, 100, 0, 10);
		TExp f = maker.createAbstr(exp, maker.createBegin(exp, note));
		return maker.createAbstr(note, f);
	}
	public int getArgsCount () 		{ return 2; }
}

class CenterDrag extends BegEndDrag
{
	float end;
	
	public CenterDrag (ExprHolder main, TExp expr, int val, int end, Graphics g, Point p, Dimension d) {
		super (main, expr, val, g, p, d);
		this.end = (float)end;
	}
	public Object getObject() {
		TExpMaker maker = TExpMaker.gExpMaker; 
		TExp note1 = maker.createNote( Color.black, 60, 100, 0, (int)value);
		TExp note2 = maker.createNote( Color.black, 60, 100, 0, (int)(end - value));
		TExp e = maker.createAppl(function, note2); 
		e = maker.createAppl(e, note1); 
		e = maker.createAppl(e, expr); 
		maker.updateHistory(e);
		return e;
	}
	public TExp buildFunction () { 
		TExpMaker maker = TExpMaker.gExpMaker;
		TExp exp = maker.createNote( Color.blue, 60, 100, 0, 1000);
		TExp note1 = maker.createNote( Color.black, 60, 100, 0, 10);
		TExp note2 = maker.createNote( Color.red, 60, 100, 0, 100);
		TExp e = maker.createBegin (maker.createRest (exp, note1), note2);
		TExp f = maker.createAbstr(exp, e);
		return maker.createAbstr(note2, maker.createAbstr(note1, f));
	}
	public int getArgsCount () 		{ return 3; }
}

abstract class BegEndDrag implements DragAble
{
	TExp expr, function; Graphics graphics;
	Point location; float value;
	ExprHolder container;
	Dimension dim; boolean oldAccept;
	
	public BegEndDrag (ExprHolder main, TExp expr, int val, Graphics g, Point p, Dimension d) {
		this.graphics = g;
		this.expr = expr;
		location = p;
		dim = d; value = (float)val;
		container = main;
		FunctionTag tagger = new FunctionTag (BegEndConstructor.appletName, getArgsCount());
		function = tagger.tag (buildFunction ());
	}
	public void dragStart() {
		DragAndDropTool.border(graphics, location, dim);
		oldAccept = container.accept;
		container.accept = false;
	}
	public void dragStop() {
		DragAndDropTool.border(graphics, location, dim);
		container.accept = oldAccept;
	}
	public Object getObject() {
		TExpMaker maker = TExpMaker.gExpMaker;
		TExp note = maker.createNote( Color.black, 60, 100, 0, (int)value);
		TExp e = maker.createAppl(function, note); 
		e = maker.createAppl(e, expr); 
		maker.updateHistory(e);
		return e;
	}
	public TExp buildFunction () 	{ return null; }
	public int getArgsCount () 		{ return 0; }
}

class BegEndExprHolder extends ExprHolder 
{
	static final int beginMsg	= 5000;
	static final int endMsg		= 5001;
	int begin, end;
	
	public BegEndExprHolder (int beg, int end) {
		super(null, new TNotesVisitor(), true);
		this.begin = beg;
		this.end = end;
	}
  	public void update (Observable o, Object arg) {
  		MsgNotifier mn = (MsgNotifier)o;
  		switch (mn.message()) {
  			case beginMsg :
  				begin = ((Integer)arg).intValue();
 				break;
  			case endMsg :
  				end = ((Integer)arg).intValue();
  				break;
  		}
  		repaint();
  	}
   	public void paintMarker(Graphics g, Point p, int offset, Dimension d, boolean beg) {
   		int [] xp = new int[3];
   		int [] yp = new int[3];
   		if (offset > 0) {
   			int x = p.x + offset;
   			g.drawLine (x, p.y, x, p.y+d.height);
	   		xp[0] = xp[1] = x;
	   		xp[2] = beg ? Math.max (x-4, p.x) : Math.min (x+4, p.x + d.width);
   			yp[0] = yp[2] = p.y;
   			yp[1] = yp[0] + 4;
   			g.fillPolygon (xp, yp, 3);
   			yp[0] = yp[2] = p.y + d.height;
   			yp[1] = yp[0] - 4;
   			g.fillPolygon (xp, yp, 3);
   		}
   	}
   	public final int dateToPos (int date) {
   		TNotesVisitor nv = (TNotesVisitor)visitor;
   		return nv.dateToPos (date);
   	}
   	public void paint(Graphics g, Point p, Dimension d) {
   		super.paint (g, p, d);
   		if (!(getExpression() instanceof TNullExp)) {
	   		g.setColor (Color.black);
	   		paintMarker (g, p, dateToPos (begin), d, true);
	   		paintMarker (g, p, dateToPos (end), d, false);
   		}
   	}
    public void handleControlClick (MouseEvent e) {
		DragAble drag = getDragObject (e.getX(), e.getY());
		TExp exp = (drag != null) ? (TExp)drag.getObject() : getExpression();
		TGlobals.player.startPlayer (exp);
    }
   	public BegEndDrag buildDrag (TExp exp, int x, int y) {
		int xb = dateToPos (begin) + border();
		int xe = dateToPos (end) + border();
		Dimension size = getSize();
		if (x < xb) {
			return new BegDrag (this, exp, begin, getGraphics(), 
						new Point(0,0), new Dimension(xb, size.height + border()));
		}
		else if (x < xe) {
			return new CenterDrag (this, exp, begin, end, getGraphics(), 
						new Point(xb,0), new Dimension(xe-xb, size.height + border()));
		}
		return new EndDrag (this, exp, end, getGraphics(), new Point(xe,0),
						 new Dimension(size.width-xe + border(), size.height + border()));
	}
	public DragAble getDragObject (int x, int y) {
		DragAble dragged = null;
		TExp exp = getExpression();
		if (!(exp instanceof TNullExp) && contains(x, y)) {
			dragged = buildDrag (exp, x, y);
		}
		return dragged;
    }
}

class TimeControlerPanel extends Panel implements Observer, DropAble
{
	static public final long timeLimit = 3600000;
	TextBarCtrl mn, sec, mls; Color back;
	MsgNotifier notifier; Label title;
	
	public TimeControlerPanel(String text, int msg) {
		super();
		setLayout(new BorderLayout(2,2));
		this.title = new Label (text, Label.CENTER);
		add ("North", title);
		notifier = new MsgNotifier (msg);
	}
    public void add (Panel p, TextBarCtrl ctrl, int val) {
		ctrl.setValue (val);
		p.add (ctrl);
    	ctrl.addObserver (this);
    }
	public void init (Observer obs, int value) {
		Panel p = new Panel();
		p.setLayout(new GridLayout(1, 3, 1,1));
		notifier.addObserver (obs);
		BarControler bar = new BarControler (0, 59, BarControler.getKVertical(), new Color(120,120,255));
		mn = new TextBarCtrl (new EditBarControler (bar, 2), TGlobals.getTranslation("min"), 12);
		add (p, mn, value/60000);

		bar = new BarControler (0, 59, BarControler.getKVertical(), new Color(160,160,255));
		sec = new TextBarCtrl (new EditBarControler (bar, 2), TGlobals.getTranslation("sec"), 12);
		add (p, sec, (value%60000)/1000);

		bar = new BarControler (0, 999, BarControler.getKVertical(), new Color(200,200,255));
		mls = new TextBarCtrl (new EditBarControler (bar, 3), TGlobals.getTranslation("mls"), 12);
		add (p, mls, value%1000);
		add ("Center", p);
	}
  	public void update (Observable o, Object arg) {
  		MsgNotifier msg = (MsgNotifier)o;
  		if (msg.message() == Define.BarControlerMsg) {
  			int time = mn.getValue() * 60000;
  			time += sec.getValue() * 1000;
  			time += mls.getValue();
 			notifier.notifyObservers (new Integer(time));
 		}
  	}
	public boolean 	accept (Object o) { 
		return ((o instanceof TExpContent) || (o instanceof TExp)) && !(o instanceof TNullExp); 
	}
	public void flash() { 
		Graphics g = title.getGraphics();
		Dimension d = title.getSize ();
		g.setXORMode (Color.black);
		g.setColor (Color.black);
		g.fillRect (0, 0, d.width, d.height);
		g.setPaintMode ();
	}
	public void dropEnter() { flash(); }
	public void dropLeave() { flash(); }
	public void drop (Object o, Point where) { 
		if (accept(o)) {
			TExp exp = (o instanceof TExpContent) ? ((TExpContent)o).getExpression() : (TExp)o;
			int duration = (int)TEvaluator.gEvaluator.Duration(exp);
			if (duration > (timeLimit-1)) {
				mn.setValue(59);
				sec.setValue(59);
				mls.setValue(999);
			}
			else {
				mn.setValue(duration/60000);
				sec.setValue((duration%60000)/1000);
				mls.setValue(duration%1000);
			}
		}
	}
}