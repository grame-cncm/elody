package grame.elody.editor.constructors;

import grame.elody.editor.constructors.colors.DraggedColor;
import grame.elody.editor.controlers.EditControler;
import grame.elody.editor.controlers.FloatEditCtrl;
import grame.elody.editor.controlers.JamButtonControler;
import grame.elody.editor.controlers.TextBarCtrl;
import grame.elody.editor.expressions.DelayedExprHolder;
import grame.elody.editor.expressions.ExprHolder;
import grame.elody.editor.expressions.TNotesVisitor;
import grame.elody.editor.misc.Define;
import grame.elody.editor.misc.TGlobals;
import grame.elody.editor.misc.applets.BasicApplet;
import grame.elody.editor.misc.draganddrop.TColorContent;
import grame.elody.editor.misc.draganddrop.TExpContent;
import grame.elody.lang.TExpMaker;
import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.lang.texpression.expressions.TNullExp;
import grame.elody.util.MsgNotifier;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Enumeration;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

public class Keyboard extends BasicApplet {
	KeyboardCtrlPanel kbCtrl;
	static final int octavesCount = 4;
	
	public Keyboard () {
		super(TGlobals.getTranslation("Keyboard"));
		setLayout(new BorderLayout(0, 4));
	}
	
	public void init () {
		Define.getButtons(this);
		KeyboardPanel kp = new KeyboardPanel(octavesCount);
		kbCtrl = new KeyboardCtrlPanel (kp, octavesCount);
		add ("North", kp);
		add ("Center", kbCtrl);
		setSize (380,210);
		moveFrame (100, 180);
	}
	public void decompose (TExp exp) {
	}
}

class KbdModeMenu extends Choice
{
	public static final String ChordMode 	= TGlobals.getTranslation("Chord_mode");
	public static final String SeqMode 		= TGlobals.getTranslation("Seq_mode");
	public KbdModeMenu (ItemListener listener) {
		addItemListener (listener);
		add (SeqMode);
		add (ChordMode);
		select (0);
	}
}

interface KeyboardTarget
{
	public void reset ();
	public void addNote (int note);
	public void delNote (int note);
	public void setMode (int mode);	
}

class KeyboardPanel extends Panel implements MouseListener, MouseMotionListener
{
	public static final int kbMsg 		= 5100;
	public static final int ChordMode 	= 0;
	public static final int SeqMode 	= 1;

	KeyboardTarget target;
	int octaves, mode;
	boolean[] pressed ;
	int curNote; int C60;

	public KeyboardPanel (int octaves) {
		super() ;
		this.octaves = octaves ;
		this.mode = SeqMode ;
		if (this.octaves < 1) this.octaves = 1 ;
		curNote = -1;
		C60 = (octaves/2) * 12;
		pressed = new boolean[ this.octaves * 12 +1] ;
		reset ();
		addMouseListener (this);
		addMouseMotionListener (this);
	}
	
	public Dimension getMinimumSize() 		{ return new Dimension( octaves * 42, 32 ); }
	public Dimension getPreferredSize()		{ return new Dimension( octaves * 84 , 64 ); }

	public void setTarget (KeyboardTarget res) { target = res; }
	public void setC60( int pitch ) { 
		if (C60 != pitch) {
			int old = C60;
			C60 = pitch;
			repaintnote (old);
			repaintnote (C60);
		}
	}
	public void setMode( int m ) { 
		if (mode != m) {
			reset ();
			if (mode == ChordMode) repaint();
			mode = m;
			target.setMode (mode);	
		}
	}

	public void reset () {
		for (int i = 0 ; i < this.octaves * 12 ; i++)
			pressed[ i ] = false;
	}
	public void clear () {
		reset();
		if (mode == ChordMode) repaint();
		target.reset();
	}
	
	public void setKeyboard(Enumeration<Integer> en) {
		reset ();
		while (en.hasMoreElements()) {
			Integer i = en.nextElement();
			int note = i.intValue();
			if ((note >= 0) && (note < octaves * 12))
				pressed[ note ] = true ;
		}
		repaint();
	}
	
	public void undo (Enumeration<Integer> en) {
		if (mode == ChordMode) {
			setKeyboard (en);
		}
	}
	
	public void setNote( int note, boolean state ) {
		if ((note >= 0) && (note < octaves * 12))
			pressed[ note ] = state ;
		repaintnote( note ) ;
	}
  
	public void swapNote( int note ) {
		setNote (note, ! pressed[ note ]);
	}

	public void repaintnote( int note ) {
		int key = note % 12 ;
		Graphics g = getGraphics() ;

		if ((key == 0) || (key == 5)) {
			paintKey( g , note ) ;
			paintKey( g , note + 1 ) ;
		}
		else if ((key == 4) || (key == 11)) {
			paintKey( g , note ) ;
			paintKey( g , note - 1 ) ;
		}
		else if ((key == 2) || (key == 7) || (key == 9)) {
			paintKey( g , note ) ;
			paintKey( g , note + 1 ) ;
			paintKey( g , note - 1 ) ;
		}
		else paintKey( g , note ) ;
	}
    

	public void paintKey( Graphics g , int note ) {
		int w = getSize().width / (octaves*7) * (octaves*7) ;
		int h = getSize().height ;
		float o = (float) octaves ;
		boolean white[] = { true , false , true , false , true , true , false , true , false , true , false , true } ;
		int oct = note / 12 ;
		int key = note % 12 ;

		if ((note >= 0) && (note < octaves*12))
		if (white[ note % 12 ]) {
			if (key > 4) key +=1 ;
			key = key / 2 ;
			if ( pressed[ note ] )
				g.setColor( Color.red ) ;
			else
				g.setColor( Color.white ) ;
			int x = (int) (w/(7.0 * o) * (oct*7 + key));
			w = (int) (w/(7.0 * o));
			g.fillRect( x , 0 , w , h-1 ) ;
			g.setColor( Color.black ) ;
			g.drawRect( x , 0 , w , h-1 ) ;
			if ((note==C60) && !pressed[ note ] ) {
				g.setColor( Color.blue ) ;
				g.fillOval( x + w/2, h-6, 4, 4);
			}
		}
		else {
			if (key > 4) key +=1 ;
			key = (key - 1) / 2 ;
			if ( pressed[ note ] )
				g.setColor( Color.red ) ;
			else
				g.setColor( Color.black ) ;

			g.fillRect( (int) (w/(7.0 * o) * (oct*7 + key + 0.75)) , 0 , (int) (w/(7.0 * o)/2-1) , 3*h/5 ) ;
			g.setColor( Color.black ) ;
			g.drawRect( (int) (w/(7.0 * o) * (oct*7 + key + 0.75)) , 0 , (int) (w/(7.0 * o)/2) , 3*h/5 ) ;     
		}
	}

	public void paint( Graphics g ) {
		for ( int i = 0 ; i < octaves ; i++ ) {
			paintKey( g , i*12 ) ;
			paintKey( g , i*12 + 2 ) ;
			paintKey( g , i*12 + 4 ) ;
			paintKey( g , i*12 + 5 ) ;
			paintKey( g , i*12 + 7 ) ;
			paintKey( g , i*12 + 9 ) ;
			paintKey( g , i*12 + 11 ) ;
			paintKey( g , i*12 + 1 ) ;
			paintKey( g , i*12 + 3 ) ;
			paintKey( g , i*12 + 6 ) ;
			paintKey( g , i*12 + 8 ) ;
			paintKey( g , i*12 + 10 ) ;
		}
	}

	int findNote( int x , int y ) {
		int w = getSize().width / (octaves*7) * (octaves*7) ;
		int h = getSize().height ;
		int key, oct ;
		int note[] = { 0,0,0,1,1,2,2,3,3,4,4,4,5,5,5,6,6,7,7,8,8,9,9,10,10,11,11,11 } ;

		if (y > h * 3.0 / 5.0) {
			key = (int) ( ((float) x) / ((float) (((float) w) / (octaves * 7.0))) );
			oct = key / 7 ;
			key = key % 7 ;
			key = key * 2 ;
			if (key > 4) key = key - 1 ;
			return (oct * 12 + key) ;
		}
		else {
			key = (int) (x / (w / (octaves * 7.0) / 4.0)) ;
			oct = key / 28 ;
			key = key % 28 ;
			return (oct * 12 + note[ key ] ) ;
		}
	}

	public void doClick (Point p) {
		if (contains (p)) {
			curNote = findNote( p.x , p.y ) ;
			swapNote( curNote );
			if (pressed[curNote]) {
				target.addNote (curNote);
			}
			else {
				target.delNote (curNote);
			}
		}
	}
 
	public boolean pointInCurrent (Point p) {
		if (contains (p) && (curNote > 0)) {
			return findNote( p.x , p.y ) == curNote;
		}
		return false;
	}
 
	public void track (Point p) {
		if (!pointInCurrent (p)) {
			if (mode == SeqMode)
				swapNote (curNote);
			doClick (p);
		}
	}
      
	public void release (Point p) {
		if (mode == SeqMode) {
			setNote( curNote, false ) ;
		}
		curNote = -1;
	}

    public void mouseEntered(MouseEvent e)	{}
    public void mouseExited(MouseEvent e) 	{}
    public void mouseMoved(MouseEvent e) 	{}
    public void mouseClicked(MouseEvent e)	{}
    public void mousePressed(MouseEvent e) {
    	doClick (e.getPoint());
    }
    public void mouseDragged(MouseEvent e) {
    	track (e.getPoint());
     }
    public void mouseReleased(MouseEvent e) { 
    	release (e.getPoint());
    }
}

class KbdResultHolder extends DelayedExprHolder implements KeyboardTarget
{
	Vector<Integer> notesList;
	int pitch, vel, chan; float dur;
	int mode; Object keyMap; 
	Component undoCtrl;

	public KbdResultHolder () {
		super (null, new TNotesVisitor(), false, 400);
		notesList = new Vector<Integer>(12,12);
		mode = KeyboardPanel.SeqMode;
		keyMap = Color.blue;
	}
  	public void update (Observable o, Object arg) {
  		MsgNotifier mn = (MsgNotifier)o;
  		switch (mn.message()) {
  			case Define.ExprHolderMsg:
				keyMap = arg;
				changed ();
  				break;
			case KeyboardParams.pitchMsg:
				int p = ((Integer)arg).intValue();
				if (p != pitch) {
					pitch = p;
					changed(true);
				}
				break;
			case KeyboardParams.velMsg:		
				int v = ((Integer)arg).intValue();
				if (v != vel) {
					vel = v;
					changed(false);
				}
				break;
			case KeyboardParams.chanMsg:
				int c = ((Integer)arg).intValue();
				if (c != chan) {
					chan = c;
					changed(false);
				}
				break;
			case KeyboardParams.durMsg:	
				float d = ((Float)arg).floatValue();
				if (d != dur) {
					dur = d;
					changed(true);
				}
				break;
  		}
  	}

	public void changed (boolean refresh) { 
		if (refresh) changed();
		else uptodate = false;
	}

	public TExp paramExp(TExp expr) {
		TExpMaker maker = TExpMaker.gExpMaker;
		if (vel != 0) expr = maker.createAttn (expr, vel);
		if (dur != 1) expr = maker.createExpv  (expr, dur);
		if (chan != 0) expr = maker.createTrsch  (expr, chan);
		return expr;
	}

	public TExp createStep(int note) {
		TExpMaker maker = TExpMaker.gExpMaker;
		if (keyMap instanceof Color) {
			return maker.createNote((Color)keyMap, note+(pitch*12), vel, chan-1, (int)(1000*dur));
		}
		else if (keyMap instanceof TExp) {
			note+= (pitch*12) - 24;
			TExp e = (TExp)keyMap;
			return (note != 0) ? maker.createTrsp  (e, note) : e;		
		}
		else System.out.println ("createStep : not a color nor a TExp");
		return null;
	}
	
	public TExp buildExpression() {
		Enumeration<Integer> en = notesList.elements();
		TExpMaker maker = TExpMaker.gExpMaker;
		TExp expr = null;
		while (en.hasMoreElements()) {
			Integer i = en.nextElement();
			TExp step = createStep(i.intValue());
			if (expr == null) expr = step;
			else if (mode == KeyboardPanel.SeqMode)
				expr = maker.createSeq (expr, step);
			else 
				expr = maker.createMix (expr, step);	
		}
		if (expr==null) return maker.createNull();
		if (keyMap instanceof TExp) expr = paramExp (expr);
		return expr; 
	}
	
	public void reset () { 
		notesList.removeAllElements();
		changed();
		undoCtrl.setEnabled (false);
	}
	public void playNote (int note) {
		TExp e = createStep (note);
		if (keyMap instanceof TExp) e = paramExp (e);
		TGlobals.player.startPlayer (e);
	}
	public void addNote (int note) {
		notesList.addElement (new Integer(note));
		changed();
		if (countNotes() == 1) undoCtrl.setEnabled (true);
		playNote(note);
	}
	public void delNote (int note) {
		for (int n=notesList.size()-1; n>=0; n--) {
			Integer i = (Integer)notesList.elementAt (n);
			if (i.intValue() == note)
				notesList.removeElementAt (n);
		}
		changed();
		if (countNotes() == 0) undoCtrl.setEnabled (false);
	}
	public void setMode (int m) {
		if (this.mode != m) {
			mode = m;
			changed();
		}
	}
	public void setUndoCtrl (Component undo) { 
		undoCtrl = undo;
		undoCtrl.setEnabled (countNotes() > 0);
	}
	public void undo () { 
		int n = countNotes();
		if (n > 0) {
			notesList.removeElementAt (--n);
			changed (true);
			if (n == 0) undoCtrl.setEnabled (false);
		}
	}
	public int countNotes () 				{ return notesList.size(); }
	public Enumeration<Integer> getList() 	{ return notesList.elements(); }
}

class KeyboardParams extends Panel implements Observer
{
	static final int pitchMsg 	= 5000;
	static final int velMsg		= 5001;
	static final int durMsg		= 5002;
	static final int chanMsg	= 5003;

	EditControler pitch, vel, dur, chan;
	int pitchRange; boolean relativeMode;
	KeyboardPanel keyPanel;
	
	public KeyboardParams(Observer o, KeyboardPanel kbd, int nOctaves) {
		super();
		setLayout (new GridLayout(1,4, 2, 2));
		keyPanel = kbd;
		pitchRange = 10 - nOctaves;
		relativeMode = false;
		buildControlers (o);
	}
    public void buildControlers (Observer obs) {
		int col = Define.TextCtrlSize;
		pitch = new EditControler (new JamButtonControler(0,pitchRange,0,Define.pitchColor,Define.pitchButton), col);
		pitch.addObserver (this);
		init (pitch, pitchMsg, obs, pitchRange/2 + (pitchRange%2));
		add (new TextBarCtrl(pitch, TGlobals.getTranslation("oct"), 12));

		vel = new EditControler (new JamButtonControler(1,127,1,Define.velColor,Define.velButton), col);
		init (vel, velMsg, obs, 100);
		add (new TextBarCtrl(vel, TGlobals.getTranslation("vel"), 12));

		dur = new FloatEditCtrl (new JamButtonControler(1,19,10,Define.durColor,Define.durButton), col);
		init (dur, durMsg, obs, 1);
		add (new TextBarCtrl(dur, TGlobals.getTranslation("dur"), 12));

		chan = new EditControler (new JamButtonControler(1,32,1,Define.chanColor,Define.chanButton), col);
		init (chan, chanMsg, obs, 0);
		add (new TextBarCtrl(chan, TGlobals.getTranslation("chan"), 12));
    }
    public void init (EditControler ebc, int msg, Observer obs, int defaultValue) {
		ebc.setMessage (msg);
		ebc.addObserver (obs);
		ebc.setValue (defaultValue);
    }

  	public void setRelativeMode (boolean relative) {
  		if (relativeMode != relative) {
  			relativeMode = relative;
	  		if (relative) {
	   			pitch.setRange (-pitchRange/2, (pitchRange/2)  + (pitchRange%2), 0, 0);
	 			vel.setRange (-64, 64, 0, 0);
	 			dur.setValue (1);
	  			chan.setRange (-32, 32, 0, 0);
	  			chan.setValue (0);
	  		}
	  		else {
	   			pitch.setRange (0, pitchRange, (pitchRange/2)  + (pitchRange%2), 0);
	 			vel.setRange (0, 127, 100, 0);
	 			dur.setValue (1);
	  			chan.setRange (1, 32, 1, 1);
	  			chan.setValue (1);
	  		}
  		}
  	}
  	public void update (Observable o, Object arg) {
  		MsgNotifier mn = (MsgNotifier)o;
  		if (mn.message() == Define.ExprHolderMsg) {
			if (arg instanceof Color) {
				setRelativeMode (false);
			}
			else if (arg instanceof TExp) {
				setRelativeMode (true);
			}
  		}
  		else if (mn.message() == pitchMsg) {
  			int midOctave = (10 - pitchRange) / 2;
  			int val = ((Integer)arg).intValue();
  			if (relativeMode) {
  				midOctave -= val;
  			}
  			else {
  				int home = pitchRange/2 + (pitchRange%2);
  				midOctave -= val - home;
  			}
  			keyPanel.setC60 (midOctave * 12);
  		}
  	}
}

class ExprAndColorHolder extends ExprHolder
{
	Color color;
	public ExprAndColorHolder (Observer obs) {
		super(null, new TNotesVisitor(), true);
		color = Color.blue;
		addObserver (obs);
		notifyObservers ();
	}
	public Object 	getObject () { 
		if (color!=null)
			return new DraggedColor(color);
		return super.getObject(); 
	}
	public boolean accept (Object o) {
		if (accept) {
			if (o instanceof TColorContent) return true;
			if (o instanceof TExpContent) {
				TExp e = ((TExpContent)o).getExpression();
				return !(e instanceof TNullExp);
			}
		}
		return false;
	}
	public void drop (Object o, Point where) {
		if (o instanceof TColorContent)  {
			color = ((TColorContent)o).getColor();
			expr = null;
			repaint();
			notifyObservers();
		}
		else {
			color = null;
			super.drop(o, where);
		}
	}
    public void paint(Graphics g, Point p, Dimension d) {
    	if (color != null) {
			g.setColor	(color);
			g.fillRect (p.x, p.y, d.width, d.height);
		}
		else super.paint(g, p, d);
   	}
    public void notifyObservers () {
   		Object o;
   		if (color==null) 
   			o = getExpression();
   		else o = color;
    	notifier.notifyObservers (o);
    }
}

class KeyboardCtrlPanel extends Panel implements ItemListener, ActionListener
{
	static final String ClearCmd = TGlobals.getTranslation("Clear");
	static final String UndoCmd  = TGlobals.getTranslation("Undo");
	KeyboardPanel kbPanel;
	KbdResultHolder result;
	Dimension prefSize;
	
	public KeyboardCtrlPanel (KeyboardPanel kp, int octaves) {
		super();
		kbPanel = kp;
		result = new KbdResultHolder();
		kbPanel.setTarget (result);
		Panel p = init (result, kbPanel, octaves);
		setLayout(new BorderLayout(4, 0));
		add ("Center", result);
		add ("East", p);
		Dimension d = p.getPreferredSize();
		prefSize = new Dimension (d.width+120, Math.max(d.height,100));
	}
    public Panel init (Observer obs, KeyboardPanel kbd, int octaves) {
    	Panel p = new Panel();
		p.setLayout(new BorderLayout(0,4));
		KeyboardParams params = new KeyboardParams(obs,kbd, octaves);
		p.add ("Center", params);
		p.add ("South", buildCommandsPanel(obs, params));
		return p;
    }
    public Panel buildButtons (ActionListener listener) {
    	Panel p = new Panel();
		p.setLayout (new BorderLayout(2, 2));
		Button b = new Button (ClearCmd);
		b.addActionListener (listener);
		p.add ("North", b);
		Button undo = new Button (UndoCmd);
		undo.addActionListener (listener);
		p.add ("South", undo);
		result.setUndoCtrl (undo);
		return p;
    }
    public Panel buildCommandsPanel (Observer obs, Observer params) {
    	Panel p = new Panel();
		p.setLayout (new FlowLayout(FlowLayout.CENTER, 4, 2));
		ExprAndColorHolder c = new ExprAndColorHolder (obs);
		c.addObserver (params);
		p.add (c);
		p.add (buildButtons (this));
		p.add (new KbdModeMenu(this));
		return p;
    }

	public Dimension getMinimumSize() 		{ return getPreferredSize(); }
	public Dimension getPreferredSize()		{ return prefSize; }
	
	public void itemStateChanged(ItemEvent e) {
		Object o = e.getItem();
		if (o instanceof String) {
			String s = (String)o;
			if (s.equals(KbdModeMenu.SeqMode)) {
				kbPanel.setMode (KeyboardPanel.SeqMode);
			}
			else if (s.equals(KbdModeMenu.ChordMode)) {
				kbPanel.setMode (KeyboardPanel.ChordMode);
				kbPanel.setKeyboard (result.getList());
			}
		}
	}
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals(ClearCmd))
			kbPanel.clear();
		else if (cmd.equals(UndoCmd)) {
			result.undo();
			kbPanel.undo (result.getList());
		}
	}
}