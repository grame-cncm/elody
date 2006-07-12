package grame.elody.editor.constructors;

import grame.elody.editor.constructors.colors.ColorHolder;
import grame.elody.editor.controlers.EditControler;
import grame.elody.editor.controlers.FloatEditCtrl;
import grame.elody.editor.controlers.JamButtonControler;
import grame.elody.editor.controlers.TextBarCtrl;
import grame.elody.editor.misc.Define;
import grame.elody.editor.misc.TGlobals;
import grame.elody.editor.misc.applets.BasicApplet;
import grame.elody.editor.misc.draganddrop.DropExtender;
import grame.elody.lang.TExpMaker;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;

public class ChordConstructor extends BasicApplet implements ActionListener {
	static final int pitchMsg 	= 5000;
	static final int velMsg		= 5001;
	static final int durMsg		= 5002;
	static final int chanMsg	= 5003;
	static final String clearCommand = "Clear";
	ChordPanel chordPanel; ChordExpressionHolder chordEh;

	public ChordConstructor() {
		super("Chord constructor");
		setLayout(new BorderLayout());
    	setFont (new Font("Times", Font.PLAIN, 12));
		setSize(390, 300);
	}
    public void add (Container c, Component p, int x, int y, int w, int h) {
		c.add(p);
		p.setSize(w, h);
		p.setLocation(x, y);
    }
    public Panel buildControlers (Observer obs) {
		Panel p = new Panel ();
		p.setLayout (new GridLayout(1, 4, 2,2));

		int col = Define.TextCtrlSize;
		EditControler e = new EditControler (new JamButtonControler(-64,+64,0,Define.pitchColor,Define.pitchButton), col);
		init (e, pitchMsg, obs, 0);
		p.add (new TextBarCtrl(e, "pitch", 12));

		e = new EditControler (new JamButtonControler(1,127,1,Define.velColor,Define.velButton), col);
		init (e, velMsg, obs, 100);
		p.add (new TextBarCtrl(e, "vel", 12));

		e = new FloatEditCtrl (new JamButtonControler(1,19,10,Define.durColor,Define.durButton), col);
		init (e, durMsg, obs, 1);
		p.add (new TextBarCtrl(e, "dur", 12));

		e = new EditControler (new JamButtonControler(1,32,1,Define.chanColor,Define.chanButton), col);
		init (e, chanMsg, obs, 1);
		p.add (new TextBarCtrl(e, "chan", 12));
		return p;
    }
    public Panel buildButtons (ColorHolder colorHolder, Checkbox checkbox) {
    	Panel p = new Panel ();
		p.setLayout (new FlowLayout(FlowLayout.LEFT));
		p.add (colorHolder);
		p.add (checkbox);
		Button clear = new Button (clearCommand);
     	clear.setActionCommand (clearCommand);
     	clear.addActionListener (this);
		p.add (clear);
		return p;
    }
    public void init (EditControler ebc, int msg, Observer obs, int defaultValue) {
		ebc.setMessage (msg);
		ebc.addObserver (obs);
		ebc.setValue (defaultValue);
    }
    public void init() {
    	Define.getButtons (this);

		ColorHolder colorHolder = new ChordColorHolder (Color.blue, true);
		DropExtender colorDrop = new DropExtender (colorHolder);
		colorDrop.setLayout (new BorderLayout(5,5));
		add ("Center", colorDrop);

		Checkbox checkbox = new Checkbox ("play");
		chordEh = new ChordExpressionHolder (checkbox);
		chordPanel = new ChordPanel(chordEh);
		colorHolder.addObserver(chordPanel);

		Panel east = new Panel();
		east.setLayout (new GridLayout(2,1,5,5));
		Panel south = new Panel();
		south.setLayout (new GridLayout(2,1,5,5));

		south.add (buildControlers(chordEh));
		south.add (buildButtons (colorHolder, checkbox));

		east.add (chordEh);
		east.add (south);

		colorDrop.add ("Center", chordPanel);		
		colorDrop.add ("East", east);	

		moveFrame (330, 10);
	}  

    public void actionPerformed (ActionEvent e) {
    	if (e.getActionCommand().equals(clearCommand)) {
			chordPanel.clear();
    	}
    }
}

class ChordPanel extends Panel implements Observer, MouseListener
{
    NotesManager nMgr;
    Image offscreen;
    Dimension offscreensize;
    Graphics offgraphics;
    int fontHeight, startRing;
    Color color;
    
    final int kBorder = 17;
    final int kNSteps = 8;
    final int kOctaveOffset = 1;


    public ChordPanel(ChordExpressionHolder exprHolder) {
    	setLayout(null);
    	setFont (new Font("Times", Font.PLAIN, 12));
    	FontMetrics fm = getFontMetrics (getFont());
    	fontHeight = fm.getHeight();
    	color = Color.blue;
    	nMgr = new NotesManager(exprHolder, color);
    	exprHolder.nMgr = nMgr;
    	addMouseListener (this);
    }

	public void update (Observable o, Object arg) {
		if (arg instanceof Color) {
			nMgr.color = color = (Color)arg;
			nMgr.eh.changed();
		}
		else if (arg instanceof Point) {
			Point p = (Point)arg;
			Note n = nMgr.getNote (p.x, p.y);
			if (n!=null) {
				nMgr.changeColor (n, color);
				paint (n);
			}
			else mouseClicked (p.x, p.y);
		}
	}

    private int Diametre (Dimension size) { return Math.min(size.height, size.width) - kBorder*2; }
    private int Rayon 	 (Dimension size) { return Diametre(size)/2; }
    private int SliceSize(Dimension size) { return Rayon(size) / (kNSteps+1); }
    private int NoteSize (Dimension size) { return Math.max(SliceSize(size) - 2, 2); }
    private double Arc 	 (int pitch) 	  { return (-(pitch % 12) + 6) * Math.PI / 6; }

  	private Point Center  (Dimension size) {
     	int x = size.width/2;
    	int y = size.height/2;
    	return new Point (x, y); 
    }
    private Point PitchPos (int pitch, Dimension size) {
    	int slice = SliceSize (size);
    	int r = Rayon (size) - (slice * (kNSteps - (pitch / 12)));
     	Point c = Center (size);
     	double arc = Arc (pitch);
     	int x = (int)(c.x + r * Math.sin(arc));
    	int y = (int)(c.y + r * Math.cos(arc));
    	return new Point (x, y); 
    }

    public void clear() {
    	nMgr.clear();
    	paint (getGraphics());
    }
    public void paint (Note n) {
		if (n!=null) {
			Dimension d = getSize();
			n.paintOval (getGraphics(), PitchPos (n.pitch(), d), NoteSize(d));
		}
    }
   	public void paint(Graphics g) {
		Dimension d = getSize();
		if ((offscreen == null) || (d.width != offscreensize.width) || (d.height != offscreensize.height)) {
		    offscreen = createImage(d.width, d.height);
		    offscreensize = d;
		    offgraphics = offscreen.getGraphics();
		    offgraphics.setFont(getFont());
		}

		offgraphics.setColor(getBackground());
		offgraphics.fillRect(0, 0, d.width, d.height);		
		int diam= Diametre (d);
		int step= SliceSize(d);
		startRing = diam/2 - step * kNSteps + step/2;
		offgraphics.setColor(Color.white);
		offgraphics.fillOval ((d.width-diam)/2, (d.height-diam)/2, diam, diam);
		for (int i=0, x=(d.width-diam)/2, y=(d.height-diam)/2, rc=diam; i<kNSteps; i++) {
			offgraphics.setColor((i==3) ? Color.blue : Color.black);
			offgraphics.drawOval (x, y, rc, rc);
			x+= step; y+= step;
			rc-= step * 2;
		}
		
		offgraphics.setColor(Color.red);
		double arc=Math.PI/6;
		Point c = Center (d);
		for (int i=0, r=diam/2; i<12; i++) {
			int x1 = (int)(c.x + r * Math.sin(i*arc));
			int y1 = (int)(c.y + r * Math.cos(i*arc));
			offgraphics.drawLine (c.x, c.y, x1, y1);
		}
				
		offgraphics.setColor(Color.blue);
		char name[]={'A'};
		c.x -= fontHeight/3;
		c.y += fontHeight/3;
		for (int i=0, r=diam/2+fontHeight-2, n=9; i<7; i++, name[0]++) {
			int x = (int)(c.x + r * Math.sin(n*arc));
			int y = (int)(c.y + r * Math.cos(n*arc));
			offgraphics.drawChars (name,0,1,x,y);
			n -= ((i==1) || (i==4)) ? 1 : 2;
		}
		
		Note n = nMgr.getFirst();
		int noteSize = NoteSize(d);
		while (n != null) {
			n.paintOval (offgraphics, PitchPos(n.pitch, d), noteSize);
			n = n.next;
		}
		g.drawImage(offscreen, 0, 0, null);
    }

    private int getPitch (int x, int y) {
    	Dimension d = getSize();
    	Point c = Center (d);
    	int pitch = 0;
    	double xo = x - c.x; double yo = y - c.y;
    	int dist = (int)Math.sqrt(xo*xo + yo*yo);
	   	if (dist > startRing) {
    		int octave = (dist - startRing) / SliceSize(d);
    		if (octave < kNSteps) {
    			octave += kOctaveOffset;
				double arc=Math.PI/6;
				double ret = Math.atan2 (xo, yo);
				int note = (int)((ret +arc/2) / arc);
				if (note < 0) note = 11 + note;
				if ((note == 0) && (ret < -arc/2)) note = 11;
				note -= 6;
				if (note < 0) note += 12;
				if (note != 0) note = 12 - note;
				pitch = note + 12 * octave;
			}
    	}
    	return pitch;
	}
    public void mouseClicked (int x, int y)	{
		Note n = nMgr.getNote (x, y);
		if (n!=null) {
			Graphics g = getGraphics();
			Rectangle r = n.area();
			nMgr.removeNote (n);
			g.clipRect (r.x, r.y, r.width, r.height);
			paint (g);
		}
		else {
	    	int pitch = getPitch (x, y);
			if ((pitch > 0) && (nMgr.getNote(pitch)==null)){
				paint (nMgr.addNote (pitch, color));
			}
		}
    }

    public void mouseEntered(MouseEvent e)	{}
    public void mouseExited(MouseEvent e) 	{}
    public void mousePressed(MouseEvent e)	{}
    public void mouseReleased(MouseEvent e)	{}
    public void mouseClicked(MouseEvent e) {
    	Point p = e.getPoint();
    	mouseClicked (p.x, p.y);
    }
}

class ChordColorHolder extends ColorHolder
{
    public ChordColorHolder (Color c, boolean acceptColor) {
    	super (c, acceptColor);
    }
	public void drop (Object o, Point where) {
		super.drop (o, where);
		notify (where);
	}
}

class NotesManager extends Note
{
	ChordExpressionHolder eh;
	
    public NotesManager(ChordExpressionHolder exprHolder, Color color) {
    	super (0, color);
    	eh = exprHolder;
    }
    
     public Note addNote (int pitch, Color color) {
    	Note n = new Note (pitch, color);
    	Note last = getLast ();
    	if (last != null) last.next = n;
    	else next = n;
		play (n);
    	eh.changed();
    	return n;
    }
     
    public Note getFirst () { return next; }
    
   
    public Note getLast () { 
		Note n = next;
		if (n == null) return null;
		while (n.next != null) { n = n.next; }
		return n;
	}
	
		
    public void clear () { 
    	next = null;
    	eh.changed();
    }

    public void play (Note n) {
  		if (eh.playIt()) TGlobals.player.startPlayer(TExpMaker.gExpMaker.createNote(Color.black,n.pitch(), eh.vel,eh.chan, eh.dur));
    }
    public void changeColor (Note n, Color c) {
		n.color = c;
		play (n);
		eh.changed();
    }
    public Note getNote (int x, int y) {
		Note n = next;
		while (n != null) {
			if (n.inside (x, y)) return n;
			n = n.next;
		}
		return null;
    }

    public Note getNote (int pitch) {
		Note n = next;
		while (n != null) {
			if (n.pitch () == pitch) return n;
			n = n.next;
		}
		return null;
    }

    public void removeNote (Note toRemove) {
		Note n = next;
		if (toRemove.equals(n)) {
			next = toRemove.next;
		}
		else while (n != null) {
			if (toRemove.equals(n.next)) {
				n.next = toRemove.next;
				break;
			}
			n = n.next;
		}
    	eh.changed();
    }
}