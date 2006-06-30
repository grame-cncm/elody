package grame.elody.editor.tleditor;

import grame.elody.editor.misc.TGlobals;
import grame.elody.editor.misc.applets.BasicApplet;
import grame.elody.editor.misc.draganddrop.ExtendedDropAble;
import grame.elody.editor.misc.draganddrop.TExpContent;
import grame.elody.editor.player.TRealTimePlayer;
import grame.elody.editor.tleditor.tlaction.TLChannelAction;
import grame.elody.editor.tleditor.tlaction.TLDragAction;
import grame.elody.editor.tleditor.tlaction.TLExtendSelectionAction;
import grame.elody.editor.tleditor.tlaction.TLMoveAction;
import grame.elody.editor.tleditor.tlaction.TLNullAction;
import grame.elody.editor.tleditor.tlaction.TLPitchAction;
import grame.elody.editor.tleditor.tlaction.TLResizeAction;
import grame.elody.editor.tleditor.tlaction.TLScoreMoveAction;
import grame.elody.editor.tleditor.tlaction.TLTrackMoveAction;
import grame.elody.editor.tleditor.tlaction.TLUnselectAction;
import grame.elody.editor.tleditor.tlaction.TLVelocityAction;
import grame.elody.editor.tleditor.tlaction.TLZoomAction;
import grame.elody.editor.tleditor.tlevent.TLEvent;
import grame.elody.editor.tleditor.tlevent.TLRest;
import grame.elody.lang.TEvaluator;
import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.lang.texpression.expressions.TNamedExp;
import grame.elody.lang.texpression.expressions.TNullExp;
import grame.elody.util.MsgNotifier;
import grame.midishare.Midi;
import grame.midishare.MidiAppl;
import grame.midishare.MidiTask;
import grame.midishare.player.MidiPlayer;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.PopupMenu;
import java.awt.Scrollbar;
import java.awt.Shape;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Observer;

public class TLPane extends Canvas implements AdjustmentListener, MouseListener,
	MouseMotionListener, KeyListener, ActionListener, FocusListener, 
	/*ComponentListener,*/ ExtendedDropAble
{
	// les couleurs utilisées
	final static Color	fGrisClair 		= new Color(238, 238, 238);
	final static Color	fGris 			= new Color(221, 221, 221);
	final static Color	fBlueArdoise 	= new Color(51, 153, 204);
	final static Color	fBlueGris 		= new Color(143,146,192);
	final static Color	fPainBrule 		= new Color(237, 143, 78);
	final static Color	fRouge		 	= new Color(237, 0, 78);	
	final static Color	fRougeClair	 	= new Color(237, 180, 180);	
	
	final static Color	fTraitColor 	= Color.black; //new Color(243, 231, 216);
	final static Color	fRuleColor 		= fGris;
	
	final static Color	fSelColorDark 	= fRouge;	
	final static Color	fSelColorLight 	= fRougeClair;
	
	final static Color	fSeqFunColorDark 	= new Color(237, 143, 78);		// pain brulé
	final static Color	fSeqFunColorLight	= new Color(237, 178, 138);
	final static Color	fSeqFunColorBkg	= fBlueArdoise;
	
	final static Color	fMixFunColorBkg = Color.green;		// vert
	
	
	final static Color 	fArgColorDark 	= new Color(143,146,192);		// bleu ardoise
	final static Color 	fArgColorLight 	= new Color(142,205,240);
	final static Color 	fArgColorBkg 	= fGrisClair;
	
	final static Color 	fAbsColorBkg 	= fPainBrule;
	final static Color 	fParColorBkg	= new Color(130,200,247);		// bleu clair
	
	// les tailles
	final static Font		fFont = new Font("SansSerif", Font.PLAIN, 9);
	final static int		fRuleWidth	= 20;			// largeur de la règle verticale des pistes
	final static int		fZeroWidth	= 40;			// largeur du temps zero des pistes
	final static int		fRuleHeight	= 20;			// hauteur de la règle horizontale du temps
	final static int		fLineHeight	= 27; 			// hauteur d'une piste
	
	// zoom sur echelle du temps
	final static int	kExtraDur = 60000;		// @@@@@@@@@
	final static double	fZoomMax = 8.0;
	final static double	fZoomMin = 0.001;
	final static int	fUnitMin = 5;			// les petits traits ne sont dessinés que s'il y a cet espace minimum
	double				fOldZoom = 1.0;			// fZoom et fUnit sont modifiables par setZoom(double zoom)
	double				fZoom = 1.0;			// fZoom et fUnit sont modifiables par setZoom(double zoom)
	int					fUnit = 10;				// fUnit indique la plus petite division de la règle de temps
	transient int		fZoomX;					// position en x d'un click dans la barre du temps
	boolean				fDecMode;
	
	// contenu de l'éditeur
	TLMultiTracks		fMultiTracks;
	double				fScoreDur;				// durée de la partition en seconde
	TextField			fName;					// nom éventuel de la partition
	
	// les scrollbar externes
	Scrollbar			fVSB;					// références aux scrollbars pour pouvoir les
	Scrollbar			fHSB;					// ajuster quand la taille de la partition change
	final static int	kSBMax = 1000;			// valeur max du sb du temps (fixe, mis a l'echelle)
	
	// position du scroll : coord de ce qui est affiché en haut a gauche
	// pour scroller il suffit de modifier ces valeurs et de redessiner
	int			fTimePos = 0;						// date du pixel à gauche juste apres la règle verticale
	int			fLinePos = 0;						// numéro de piste en haut juste en dessous de la règle horizontale
	boolean 	fAutoSroll = true;                  // autorise l'autoscroll
	
	
	
	// action de drag courante (drag&drop interne)
	final  TLDragAction	sTLNullAction = new TLNullAction(this);	// une action nulle pour ne pas avoir à la créer a chaque fois
	TLDragAction	fDragAction;							// l'action courante ( choisie par mousePressed(MouseEvent m) ) 

	// contenu courant de la selection
	TLZone		fSelection;
	int			fTrackNum;
	// informations auxilliaires volatiles maj a chaque appel de getEventAt
	TLEvent		sAuxEvent 	= null;		// l'ev trouvé par getEventAt
	int			sStartTime 	= 0;			// sa date de début
	int			sEndTime 	= 0;			// sa date de fin
	boolean		sEvInGroup 	= false;		// l'événement fait partie d'un groupe
	boolean		sDtInGroup 	= false;		// la date courante est dans un groupe
	
	// informations pour le drag and drop (de Dom) en cours
	Object		fCurDDObj = null;		// objet qui va être eventuellement droppée (non convertit en exp)
	TExp		fCurDDExp = null;		// cet objet convertit en TExp elody
	int			fCurDDExpDur;			// durée de cette expression
	boolean		fCurDDFlag;				// vrai si feedback visuel à déjà été dessiné une première fois
	int			fCurDDx;				// position x 
	int			fCurDDy;				// position y
	int			fCurDDTime;				// position x en temps
	int			fCurDDITime;			// position x en temps contraint à 1 pt d'insertion possible
	int			fCurDDLine;				// position y en ligne
	boolean		fCurDDApplFlag;			// mode "application" si vrai
	
	// STEPH
	// Notification 
	MsgNotifier  notifier;
	  
	// pour avoir le clavier
	boolean 	fNeedFocusRequest;
	boolean 	fHasFocus = false;
	  
	  
	// menus contextuels
	PopupMenu scoreMenu;		// pour la partition totale
	PopupMenu trackMenu;		// pour une track particulière
	PopupMenu eventMenu;		// pour l'événement selectionné
	PopupMenu timeMenu;			// pour la regle du temps
	
	// gestion du doubleBuffering
	Dimension 	offDimension;
    Image 		offImage;
    Graphics 	offGraphics;
    
    TLUpdater	fUpdater;
    
    
    // gestion du curseur de jeu
	TRealTimePlayer fPlayer;    // Player associé au Time Line editeur
	TimeLineTask 	fTask;      // Tache d'affichage pendant le jeu
	//int          	fOldDateX;  // Date de la dernière tache
	int          	fOldDate;  // Date de la dernière tache
	int 		 	fDuration;  // Durée de l'expression en cours de jeu
	int				fPosMs;		// date courante dans le règle de temps
	TExp 		 	fCurExp;    // Expression en cours de jeu
	
	
	//==================================================================================
	//
	//						TLPane : le constructeur
	//
	//==================================================================================

	public TLPane(Scrollbar	hsb, Scrollbar	vsb, MsgNotifier  not, TextField name, TRealTimePlayer player)
	{
		fUpdater = new TLUpdater(this);
		
		fVSB = vsb;
		fHSB = hsb;
		fName = name;
		
		InternalSetMultiTracks(new TLMultiTracks());
		setZoom(0.1);
		initScrollbars();
		
		fNeedFocusRequest = true;
		setFont(fFont);
		setBackground(fArgColorBkg);
		
		
		/*addComponentListener(this);*/ 			// pas moyen de le faire marcher !!!!
		addFocusListener(this);
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		
		fVSB.addAdjustmentListener(this);
		fHSB.addAdjustmentListener(this);
		
		notifier = not; 
		
		// YO : création des divers popup menus
		makePopupMenus();
		
		fPlayer = player;
		fTask = new TimeLineTask (this);
		fCurExp = null;
		
	}
	
	public void notifyContentChanged()
	{
		notifier.notifyObservers ();
	}
	
	public void setMultiTracks(TLMultiTracks mt)
	{
		InternalSetMultiTracks(mt);
		fUpdater.doUpdates();
	}
	public void InternalSetMultiTracks(TLMultiTracks mt)
	{
		// complete le nouveau MT à 128 pistes
		int nbt = mt.getCount();
		mt.at(nbt);
		for (int i = nbt; i < 128; i++) mt.insert(new TLTrack());
		
		// remplace l'ancien
		fMultiTracks = mt;
		setScoreSize(fMultiTracks.getMultiDur());
		if (fTimePos > mt.getMultiDur()) setScrollPos(mt.getMultiDur(),fLinePos);
		fSelection = new TLZone(fMultiTracks);
		multiTracksChanged();
	}

	public void setTrack(int line, TLTrack trk)
	{
		if (fMultiTracks.at(line)) {
			fMultiTracks.remove();
			fMultiTracks.insert(trk);
			fSelection.selectLine(line);
		} else {
			fMultiTracks.insert(trk);
			fSelection.selectLine(fMultiTracks.getPos());
		}
		
		multiTracksChanged();
	}

/*
	public void componentShown(ComponentEvent e)
	{
		System.out.println( "componentShown : ");
		requestFocus();			// pour avoir les touches du clavier
	}

	public void componentHidden(ComponentEvent e) {}
	public void componentMoved(ComponentEvent e) {}
	public void componentResized(ComponentEvent e) {}
	
*/
	
	public Dimension getPreferredSize()
	{
		return new Dimension(fRuleWidth+300, fRuleHeight+5*fLineHeight);
	}
	
	
	//STEPH notification
	
	public void addObserver	  	(Observer o) { notifier.addObserver(o); }
    public void deleteObserver	(Observer o) { notifier.deleteObserver(o); }
	
	/*
	public void processEvent(AWTEvent m)
	{
		System.out.print( "<< " + m );
		super.processEvent(m);
		System.out.println( " >>" );
	}
	*/

	public boolean isFocusTraversable()
	{
		return true;
	}
	
	//==================================================================================
	//
	//			 Conversion des coord (x,y) <-> (time, line)
	//
	//==================================================================================
	// (ce qui est appelé line correspond à un numéro de piste)
	final public int time2x(int time)	{ return fZeroWidth + (int)((time - fTimePos) * fZoom); }
	final public int time2x(int time, double zoom)	{ return fZeroWidth + (int)((time - fTimePos) * zoom); }
	final public int x2time(int x)		{ return fTimePos + (int)((x - fZeroWidth) / fZoom); }

	final public int line2y(int line)	{ return fRuleHeight + (line - fLinePos) * fLineHeight; }
	final public int y2line(int y)		{ return fLinePos + (y - fRuleHeight) / fLineHeight; }
	
	
	// calcul le temps du bord gauche pour que x et t coincides
	// en fonction du zoom courant
	final public int xt2timePos(int x, int t)		
	{ 
		int ftp = t - (int)((x - fZeroWidth)/fZoom); 
		return (ftp < 0) ? 0 : ftp;
	}
	
	// !!! toto !!!
	
	// ajuste le zoom à des limites raisonnables et 
	// calcule l'unité pour l'affichage de la règle de temps
	void setZoom(double zoom)
	{
		fOldZoom = fZoom;
		fZoom = (zoom < fZoomMin) ? fZoomMin : (zoom > fZoomMax) ? fZoomMax : zoom;
		fUnit = 1; while (fUnit*zoom < 4.0) fUnit *= 10;
		if (fZoom != fOldZoom) fUpdater.viewChanged();
	}
	
		
	void setScoreSize(int contentDur )
	{
		double oldScoreDur = fScoreDur;
		fScoreDur = contentDur / 1000.0 + 60.0;
		if (oldScoreDur != fScoreDur) 	fUpdater.durationChanged();
		//if (contentDur < fTimePos) 		{ fTimePos = contentDur; fUpdater.viewChanged(); }
	}
		
		
	public void selectionChanged()
	{
		fUpdater.selectionChanged();
	}
	
	public void multiTracksChanged()
	{
		setScoreSize(fMultiTracks.getMultiDur());
		fUpdater.contentChanged();
	}

	public void setScrollPos(int time, int line)
	{
		int oldtime = fTimePos;
		int oldline = fLinePos;
		fTimePos = (time < 0) ? 0 : time;
		fLinePos = (line < 0) ? 0 : line;
		if ((oldtime != fTimePos) | (oldline != fLinePos)) fUpdater.viewChanged();
	}
	
	public void setZoomAt(double zoom, int xpos)
	{
		int time = x2time(xpos);
		setZoom(zoom);
		setScrollPos(xt2timePos(xpos,time), fLinePos);
	}
	

	//==================================================================================
	//
	//			 Ajustement des scrollbars et scroll
	//
	//==================================================================================
		
	
	public void initScrollbars()
	{
		fHSB.setValues(0,1,0,1000);
		fVSB.setValues(0,1,0,127);
	}
	
	public void adjustScrollbars()
	{
		fHSB.setValue( (int)(fTimePos / fScoreDur));
		fVSB.setValue(fLinePos);
	}
	
	public void adjustmentValueChanged(AdjustmentEvent e) 
	{
		setScrollPos((int)(fHSB.getValue() * fScoreDur), fVSB.getValue());
		fUpdater.doUpdates(false);
	}
	
	
	//==================================================================================
	//
	//			 Affichage de la partition
	//
	//==================================================================================

	public void prepareOffscreen()
	{
		Dimension dim = getSize();
		
        // Erase the previous image.
        offGraphics.setColor(getBackground());
        offGraphics.fillRect(0, 0, dim.width, dim.height);
        offGraphics.setColor(Color.black);
		
		// Draw the tracks into the offscreen
		//drawTracks(offGraphics, fLinePos, fTimePos, y2line(dim.height)+1, x2time(dim.width)+1);
		drawMultiTracks(offGraphics, dim, fLinePos, /*fTimePos*/ x2time(fRuleWidth), y2line(dim.height)+1, x2time(dim.width)+1);
		drawHRule(offGraphics, 0, 0, dim.width, fRuleHeight);
		//drawVRule(offGraphics, 0, fRuleHeight, fRuleWidth-1, dim.height-fRuleHeight);
		drawSQRule(offGraphics, -1, -1, fRuleWidth-1, fRuleHeight);
	}
	
	// affichage du curseur de jeu
	
	public void drawTimeLine(Graphics g) 
	{
		g.setXORMode (fArgColorBkg);
		drawDate (g, time2x(fOldDate),  getSize().height);
  		g.setPaintMode ();
	}
	
	public boolean updateTimeLine() 
    {
    	try {
    		int time = fPlayer.getState().date;
    		Graphics g = getGraphics();
    		int h = getSize().height;
    		g.setXORMode (fArgColorBkg);
    		drawDate (g, time2x(fOldDate),  getSize().height);
			drawDate (g, time2x(time), h);
			fOldDate = time;
			g.setPaintMode ();
			return (time < fDuration);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return true;
   }

	//------------------------------------------------
	private void drawDate (Graphics g, int x, int h) {g.drawLine (x, fRuleWidth, x, h);}
	
	
	// dessine les règles et le contenu des pistes
    public void paint(Graphics g) {update(g);}
  
    public void update(Graphics g) 
	{
		// pb : pour avoir le clavier il faut faire un focus request quand le composant est visible
		// je n'ai trouvé que cette solution, c'est pas idéal !!!!
		
		if (fNeedFocusRequest) { requestFocus(); fNeedFocusRequest = false; }

        //Create the offscreen graphics context, if no good one exists.
		Dimension dim = getSize();
        if ( (offGraphics == null)
          || (dim.width != offDimension.width)
          || (dim.height != offDimension.height) ) {
            offDimension = dim;
            offImage = createImage(dim.width, dim.height);
            if (offGraphics != null) offGraphics.dispose();
            offGraphics = offImage.getGraphics();
            prepareOffscreen();
        }
        g.drawImage(offImage, 0, 0, this);
		g.clipRect(fRuleWidth, fRuleHeight, dim.width, dim.height);
		drawSelection(g, fSelection);
		drawTimeLine(g);
	}

	// dessine le contour de la sélection 
	public void drawSelection(Graphics g, TLZone sel) 
	{
		Dimension d = this.getSize();
		int x0 = time2x(sel.start());
		int x1 = time2x(sel.end());
		int y0 = line2y(sel.topline());
		int y1 = line2y(sel.botline());
		
		if ( (y1 < 0) || (y0 > d.height) || (x1 < 0) || (x0 > d.width) ) {
			// selection hors écran
		} else {
			// correction des coord pour ne pas dépasser les limites
			if (x0 < 0) 	x0 = 0;
			if (x1 > 2000) 	x1 = 2000;
			g.setColor((fHasFocus) ? fSelColorDark : fSelColorLight);
			g.drawRect(x0-1, y0 + 2, x1-x0+1, y1-y0-5);
			g.drawRect(x0-2, y0 + 1, x1-x0+3, y1-y0-3);
		}
	}
	
	// dessine le contenu des pistes entre sline et eline, et dans la portion de temps
	// entre stime et etime. 
	public void drawMultiTracks(Graphics g, Dimension dim, int sline, int stime, int eline, int etime) 
	{
		FontMetrics fm	= g.getFontMetrics();
		
		// dessine les pistes concernées
		if (fMultiTracks.at(sline)) {
			do {
				drawTrack(g, dim, fm, fMultiTracks.getTrack(), fMultiTracks.getIndex(), fMultiTracks.getPos(), stime, eline, etime);
			} while (fMultiTracks.next() && fMultiTracks.getPos() < eline);
		}
		// efface le reste de la page
		if (fMultiTracks.getPos() < eline) {
			g.setColor(fArgColorBkg);
			int y0 = line2y(fMultiTracks.getPos());
			int y1 = line2y(eline);
			g.fillRect(0, y0, dim.width, y1-y0);
			g.setColor(Color.black);
			g.drawLine(0,y0,dim.width,y0);
		}
	}
	
	void drawTrack(Graphics g, Dimension dim, FontMetrics fm, TLTrack t, int index, int sline, int stime, int eline, int etime)
	{
		final int ln0 = sline;
		final int ln1 = sline + t.fTrackHeight;
		final int y0 = line2y(ln0);
		final int y1 = line2y(ln1);
		

		// choisie la couleur du fond
		Color bkg 	= (t.isSeqFunTrack()) ? fSeqFunColorBkg 
					: (t.isMixFunTrack()) ? fMixFunColorBkg 
					: (t.isAbstrTrack()) ? fAbsColorBkg 
					: (t.isParamTrack()) ? fParColorBkg 
					: fArgColorBkg;
		
		Color bkg2 	= (t.isSeqFunTrack()) ? fSeqFunColorBkg.darker() 
					: (t.isMixFunTrack()) ? fMixFunColorBkg.darker()  
					: (t.isAbstrTrack()) ? fAbsColorBkg.darker() 
					: (t.isParamTrack()) ? fParColorBkg.darker() 
					: fRuleColor;
		
		// dessine le fond de la piste
		g.setColor(bkg);
		g.fillRect(0, y0, dim.width, y1-y0);

		// dessin le trait supérieur de piste
		g.setColor(Color.black);
		g.drawLine(0, y0, dim.width, y0); 
		
		// dessin du numéro de piste
		g.setColor(bkg2);
		g.fillRect(0, y0, fRuleWidth-2, y1-y0);
		g.setColor(Color.black);
		g.drawRect(0, y0, fRuleWidth-2, y1-y0);
		int sy = (y0 + y1 + fm.getHeight())/2;
		g.drawString(String.valueOf(index), 3, sy);
		
		// dessine le trait date 0
		if (stime <= 0) {
			g.setColor(fRuleColor);
			int x = time2x(0);
			g.drawLine(x, y0, x, y1);
			g.drawLine(x-2, y0, x-2, y1);
		}
		
		// prepare le clipping
		Shape clipzone1 = g.getClip();
		g.clipRect(fRuleWidth, y0, dim.width-fRuleWidth, y1- y0);
		Shape clipzone2 = g.getClip();
		
		// dessine les evenements
		if (t.at(stime)) {
			do {
				TLEvent e = t.getEvent();
				if (! (e instanceof TLRest) ) {
					int t0 = t.getDate();
					int t1 = t0 + e.fDur;
					int x0 = time2x(t0);
					int x1 = time2x(t1);
					drawEvent(g, fm, bkg, e, ln0, t0, Math.min(ln1, eline), etime);
					g.setClip(clipzone2);
				}
			} while (t.next() && t.getDate() < etime);
		}

		// hachure les pistes mutées
		if (t.getMuteFlag()) {
			g.setColor(Color.black);
			for (int hx = fLineHeight - (y1-y0); hx < dim.width; hx += fLineHeight) {
				g.drawLine(hx, y1, hx+(y1-y0), y0);
			}
		}
		
		// restore le clipping
		g.setClip(clipzone1);
	
	}
	
	void drawEvent (Graphics g, FontMetrics fm, Color bkg, TLEvent e, int sline, int stime, int eline, int etime)
	{
		int d1 = stime + e.fDur;
		int x0 = time2x(stime);
		int x1 = time2x(d1);
		int y0 = line2y(sline);
		int y1 = line2y(sline+e.fHeight);
			
		g.clipRect(x0, y0+3, x1-x0, y1-y0-6);
		e.draw(g, fm, fArgColorDark, fArgColorLight, x0, y0+3, x1-x0, y1-y0-7);
	}	
	
	public void drawTracks(Graphics g, int sline, int stime, int eline, int etime) 
	{
		Color 		dark, light, bkg;
		FontMetrics fm	= g.getFontMetrics();	

		int	xorg = time2x(stime);
		int	xend = time2x(etime);
		int yorg = line2y(sline);
		int yend = line2y(eline+1);
		
		for (int l = sline; l <= eline; l++) {
		
			int y = line2y(l) + 3;
			int h = line2y(l+1) - y - 4;
			
			int d0 = stime;
			int x0 = xorg;
			
			bkg = Color.white;
			dark = fArgColorDark;
			light = fArgColorLight;
			
			if (fMultiTracks.at(l)) {

				TLTrack t = fMultiTracks.getTrack();

				// choisie la couleur du fond
				bkg = (t.isSeqFunTrack()) ? fSeqFunColorBkg 
					: (t.isMixFunTrack()) ? fMixFunColorBkg 
					: (t.isAbstrTrack()) ? fAbsColorBkg 
					: fArgColorBkg;
				
				// dessine le fond de la piste					
				g.setColor(bkg);
				g.fillRect(x0, y-3, xend-x0, h+7);
				g.setColor(Color.black);
				g.drawLine(x0, y-3, xend, y-3); 
				
				// dessine les événements
				if (t.at(stime))  {

					d0 = t.getDate();
					x0 = time2x(d0);
					do {
						TLEvent e = t.getEvent();
						int d1 = d0 + e.fDur;
						int x1 = time2x(d1);
						if (e instanceof TLRest) {
							g.setColor(bkg);
							g.fillRect(x0, y, x1-x0, h);
						} else {
							e.draw(g, fm, dark, light, x0, y, x1-x0, h);
						}
						d0 = d1;
						x0 = x1;
					} while (t.next() && d0 < etime);
				}
				
				// complete éventuellement la piste
				if (d0 < etime) { 
					g.setColor(bkg);
					g.fillRect(x0, y, xend-x0, h);
				}
				
				// hachure les pistes mutées
				if (t.getMuteFlag()) {
					g.setColor(Color.black);
					for (int hx = xorg; hx < xend; hx += fLineHeight) {
						g.drawLine(hx, y-3+fLineHeight, hx+fLineHeight, y-3);
					}
				}
				
			} else {
			
				// si piste inexistante
				g.setColor(Color.white);
				g.fillRect(xorg, y, xend-xorg, h);
			
			}
		}
	}	
	
	// masque le coin en haut a gauche
	public void drawSQRule(Graphics g, int x, int y, int w, int h)
	{
		g.setColor(fRuleColor);
		g.fillRect(x, y, w, h);
		g.setColor(Color.black);
		g.drawRect(x, y, w, h);
	}
	
	
	final String convertDate (int date) {
		int min = date/60000;
		int reste = date%60000;
		int sec = reste/1000;
		int milli = reste%1000;
		String s1 = "";
		String s2 = "";
		String s3 = "";
		if (min > 0) s1 = String.valueOf(min) + "m"; 
		if (sec > 0) s2 = String.valueOf(sec) + "s"; 
		if (milli > 0) s3 = String.valueOf(milli); 
		return s1+s2+s3;
	}
	
	
	// dessine la règle horizontale du temps
	public void drawHRule(Graphics g, int x, int y, int w, int h)
	{
		FontMetrics fm = g.getFontMetrics();
		int sy = y + (h + fm.getHeight())/2 - 2;
		int t0 = x2time(0); if (t0 < 0) t0=0;
		
		g.setColor(fRuleColor);
		g.fillRect(x, y, w, h);
		
		g.setColor(Color.black);
		int xm = x+w;

		int y1 = y;
		int bigUnit = fUnit*10 ;
		int medUnit = fUnit*5;
		
		// dessine les plus grosses barres avec numéros
		int y2 = y+h-1;
		int tr = (t0/bigUnit)*bigUnit;
		int xr = time2x(tr);
		while (xr < xm) {
			// converion de tr en min,sec,milli
			g.drawLine(xr,y1,xr,y2);
			g.drawString(convertDate(tr), xr+3, sy);
			tr+=bigUnit;
			xr = time2x(tr);
		}
		
		// dessine les barres intermédiares
		int pxSize = (int)(medUnit*fZoom);
		boolean txt = false;
		if (pxSize > 75) {
			txt = true;
		} else if (pxSize > 50) {
			y1 = y + 2;
		} else {
			y1 = y2 - 4;
		}
		tr = (t0/medUnit)*medUnit;
		xr = time2x(tr);
		while (xr < xm) {
			g.drawLine(xr,y1,xr,y2);
			if (txt) g.drawString(convertDate(tr), xr+3, sy);
			tr+=medUnit;
			xr = time2x(tr);
		}
		
		// dessine éventuellement les barres unités
		pxSize = (int)(fUnit*fZoom);
		if (pxSize > 40) {
			y1 = y;
		} else if (pxSize > 30) {
			y1 = y2 - 8;
		} else if (pxSize > 20) {
			y1 = y2 - 6;
		} else if (pxSize > 15) {
			y1 = y2 - 5;
		} else if (pxSize > 10) {
			y1 = y2 - 3;
		} else {
			y1 = y2 - 2;
		}
		if (pxSize >= fUnitMin) {
			tr = (t0/fUnit)*fUnit;
			xr = time2x(tr);
			while (xr < xm) {
				g.drawLine(xr,y1,xr,y2);
				tr+=fUnit;
				xr = time2x(tr);
			}
		}
		if (pxSize > 15) {
			int su = fUnit/2;
			y1 = y2 - 2;
			tr = (t0/su)*su;
			xr = time2x(tr);
			while (xr < xm) {
				g.drawLine(xr,y1,xr,y2);
				tr+=su;
				xr = time2x(tr);
			}
		}
		
		g.drawLine(x,y2,x+w,y2);
	}
	
	// dessine la règle verticale des numéros de pistes
	public void drawVRule(Graphics g, int x, int y, int w, int h)
	{
		FontMetrics fm = g.getFontMetrics();
		int sy = (fLineHeight + fm.getHeight())/2;
		
		g.setColor(fRuleColor);
		g.fillRect(x,y,w,h);
		g.setColor(Color.black);
		
		int ym = y+h;
		int lr = fLinePos;
		int	yr = y;
		int	x2 = x + w;
		while (yr < ym) {
			g.drawLine(x,yr,x2,yr);
			if (fMultiTracks.at(lr)) {
				if (fMultiTracks.getTrack().isSeqFunTrack()||fMultiTracks.getTrack().isMixFunTrack()) {
					g.setColor(Color.blue);
				} else {
					g.setColor(Color.darkGray);
				}
			} else {
				g.setColor(Color.lightGray);
			}
			g.drawString(String.valueOf(lr), x+1, yr+sy);
			lr++;
			yr+=fLineHeight;
			g.setColor(Color.black);
		}
		g.drawLine(x2,y,x2,y+h-1);
	}
		
	
	
	//==================================================================================
	//
	//			  gestion du drag&drop Elody (celui de Dom)
	//
	//==================================================================================

	public void 	dropEnter ()				
	{ 
		fCurDDObj 	= null;
		fCurDDExp 	= null;
		fCurDDFlag 	= false;
	}
	
	public void 	dropLeave ()				
	{ 
		if (fCurDDFlag) { 
			drawDDVisualFeedback(fCurDDLine, fCurDDTime, fCurDDITime, fCurDDExpDur); 
			fCurDDFlag = false;
		}
	}
	
	public boolean accept (Object o) 
	{
		//System.out.println( "accept : " + o );
		if ( (o != null) && (o instanceof TExpContent) ) {
			if (o != fCurDDObj) { // obligé de faire ca a cause du DD de dom !
				fCurDDObj = o;
				prepareDrop((TExpContent)o);
			}
			return true;
		} else {
			return false;
		}
	}
	
	void prepareDrop(TExpContent ec)
	{
		fCurDDExp 		= ec.getExpression();
		fCurDDExpDur 	= (fCurDDExp instanceof TNullExp) ? 0 : (int) TEvaluator.gEvaluator.Duration(fCurDDExp);
		fCurDDFlag 		= false;
		fCurDDApplFlag 	= false;
	}
	
	public void drop (Object o, Point where) 
	{
		//System.out.println( "drop : " + where + " (" + o + ")" );
		if (fCurDDObj != o) {
			System.out.println ("pb !!!, l'objet dragged a change en cours de route "
								+ ": ancien = " + fCurDDObj
								+ ", nouveau = " + o);
		} else if (fCurDDExp != null) {
			if (where.x < fRuleWidth) {
			
				if (where.y < fRuleHeight) {						// drop d'une expression complète
				
					TExp exp; String name;
					if (fCurDDExp instanceof TNamedExp) {
						exp = fCurDDExp.getArg1();
						name =((TNamedExp)fCurDDExp).getName();
					} else {
						exp = fCurDDExp;
						name = "";
					}
					InternalSetMultiTracks(TLConverter.multi(exp));
					fName.setText(name);
					
				} else {											// drop d'une piste complète
					setTrack(y2line(where.y), TLConverter.track(fCurDDExp));
				}
				
			} else if (fCurDDApplFlag) {
				if (fSelection.contains(x2time(where.x), y2line(where.y))) {
					fSelection.cmdApply(fCurDDExp);
				} else {
					TLZone dest = new TLZone (fMultiTracks, x2time(where.x), y2line(where.y));
					dest.cmdApply(fCurDDExp);
					fSelection.set(dest);
				}
				
			} else {
				fSelection.selectDstPoint(fCurDDITime, fCurDDLine);
				fSelection.cmdInsert(fCurDDExp);
			}
			fCurDDExp = null;
			fCurDDFlag = false;
			fCurDDApplFlag = false;
			multiTracksChanged();
			fUpdater.doUpdates();
		}
	}

	public void feedback (int x, int y)		
	{ 
		//System.out.println( "feedback : " + x + ", " + y ); 
		
		if (fCurDDExp != null) {
			if (fCurDDFlag) { 
				// eteint le feedback précédent
				drawDDVisualFeedback(fCurDDLine, fCurDDTime, fCurDDITime, fCurDDExpDur); 
			}
			fCurDDx = x;
			fCurDDy = y;
			// calcul de la nouvelle position (line,time)
			int line = y2line(y); if (line >127) line = 127;
			int time = x2AlignedTime(line,x); 
			int itime = time;
			
			if (time >= fTimePos && line >= fLinePos) {
				// il s'agit d'une insertion dans une piste
		
				// calcul de la date d'insertion "itime" qui peut être ­ de "time" 
				// en fonction de ce qu'il y a dessous le curseur
			
				getEventAt(time,line);	// resultat dans : sAuxEvent, sStartTime, sEndTime
		
				fCurDDApplFlag = false;
				
				if (sAuxEvent == null) 	
					 { itime = time;}
				else if (sAuxEvent.isRest()) 	
					{ itime = time;}
				else if (time < (sStartTime + sAuxEvent.fDur/4)) 	
					{ itime = sStartTime;}
				else if (time > (sEndTime - sAuxEvent.fDur/4)) 		
					{ itime = sEndTime;}
				else 								
					{ itime = (sStartTime + sAuxEvent.fDur/2); fCurDDApplFlag = true;}
				
			}
			
			// met à jour l'état
			fCurDDTime 	= time;
			fCurDDITime = itime;
			fCurDDLine 	= line;
			fCurDDFlag = true;

			drawDDVisualFeedback(line, time, itime, fCurDDExpDur);
			
		}
	}
	
	public int x2AlignedTime (int line, int x)
	{
		int t0 = x2time(x), 
			t1 = x2time(x+1),
			time = (t0+t1)/2;
		
		if (t0 < 0) {
			return 0;
		} else if (t0 == t1) {
			// pas d'alignement necessaire
			return t0;
			
		} else {
		
			// essai d'aligner sur la frontière d'un objet
			int		ft;	
			boolean searchprev, searchnext;
			int		pline = line, nline = line+1;
			do {
				if (searchprev = (pline >= 0) ) {
					fMultiTracks.at(pline);
					ft = fMultiTracks.getTrack().frontier(time);
					if (ft >= t0 && ft < t1) return ft;
					pline--;
				}
				if (searchnext = (nline < 127)) {
					fMultiTracks.at(nline);
					ft = fMultiTracks.getTrack().frontier(time);
					if (ft >= t0 && ft < t1) return ft;
					nline++;
				}
			} while ( searchprev || searchnext );
			
			// sinon essai d'aligner sur la grille de temps
			time = t1;
			// essai les unités moyennes
			int medUnit = fUnit*5;
			int mt = (time/medUnit)*medUnit;
			int	mx = time2x(mt);
			if (mx == x)  return mt;
			
			// sinon les unités
			int ut = (time/fUnit)*fUnit;
			int	ux = time2x(ut);
			if (ux == x) return ut;
			
			// sinon pas d'alignement !!!
			/*
			System.out.println("mt=" + mt + ", mx=" + mx);
			System.out.println("ut=" + ut + ", ux=" + ux);
			System.out.println(" t=" + time + ",  x=" + x);
			System.out.println("");
			*/
			return time;
		}	
			
	}
		
	void drawDDVisualFeedback(int line, int time, int itime, int dur)
	{
		Graphics g = getGraphics();
		g.setXORMode(fArgColorBkg); g.setColor(fTraitColor); 		
		if (time >= fTimePos && line >= fLinePos) {
			// il s'agit d'un drag sur le contenu d'une piste
			
			// calcule les positions graphiques du rectangle et du point d'insertion
			int rx = time2x(time);		// position rectangle
			int rx2 = time2x(time+dur);	
			int ry = line2y(line);
			int tx = time2x(itime);		// point d'insertion
			
			// dessin du rectangle
			g.drawRect(rx, ry+2, rx2-rx-1, fLineHeight-5);
			
			if (fCurDDApplFlag) {
				// feedback d'une application
				final int k = 10;
				
				g.drawLine(tx, 0, tx, ry - k);
				g.drawString("APPLY", tx+2, ry - k - 1);
				
				/*
				g.drawLine(tx-k, ry-k+1, tx+k, ry-k+1);
				g.drawLine(tx-k, ry-k+1, tx, ry+2);
				g.drawLine(tx+1, ry+1, tx+k, ry-k+1);
				*/
				Polygon plg = new Polygon();
				plg.addPoint(tx-k, ry-k+1);
				plg.addPoint(tx+k, ry-k+1);
				plg.addPoint(tx, ry+1);
				g.drawPolygon(plg);
								
				g.drawLine(tx, ry+fLineHeight, tx, getSize().height);
				
			} else {
				// feedback d'une insertion
				if (rx == tx) {
					// ligne indiquant le début de l'objet (en deux parties, a cause de l'xor)
					g.drawLine(rx, 0, rx, ry);
					g.drawLine(rx, ry+fLineHeight, rx, getSize().height);

					// ligne indiquant la fin de l'objet
					g.drawLine(rx2, 0, rx2, getSize().height);
				} else {
					// ligne indiquant seulement le point d'insertion (entre deux objets)
					g.drawLine(tx, 0, tx, getSize().height);
				}
				g.drawString("t="+ itime, tx+2, ry - 10);
			}
		} else if (fCurDDy > fRuleHeight) {
			//System.out.println("Dans piste : line = " + line + ", fLinePos = " + fLinePos);
			// il s'agit d'un drag sur la règle verticale des pistes (remplacement de piste)
			Dimension dim = getSize();
			// ombre de la piste source
			int x0 = 0, y0 = line2y(line) /*+ dstY - srcY*/;
			g.drawRect(x0, y0+1, dim.width, fLineHeight-2);
		} else {
			//System.out.println("Dans coin");
			// il s'agit d'un drag dans le coin
			Dimension dim = getSize();
			// ombre du coin & du reste
			g.drawRect(1, 1, fRuleWidth-2, fRuleHeight-2);
			g.drawRect(fRuleWidth, fRuleHeight+1, dim.width - fRuleWidth - 1, dim.height - fRuleHeight - 2);
		}
		g.setPaintMode();
		g.dispose();
	}

		
	
	
	//==================================================================================
	//
	//			  gestion des popup menus
	//
	//==================================================================================

	boolean isPopupEvent(MouseEvent m)
	{
		return m.isMetaDown();
	}
	
	void makePopupMenus ()
	{
		scoreMenu 	= makeOneMenu(new String[]{"StartAll","StopAll", "PlayAll" ,"ClearAll"});
		trackMenu 	= makeOneMenu(new String[]{"NormalTrack", "SeqFunctionTrack","MixFunctionTrack", "ParamTrack", "AbstractTrack", "MuteTrack", "UnmuteTrack", "PlayTrack", "ClearTrack"});
		eventMenu 	= makeOneMenu(new String[]{"PlayEvent", "Evaluate", "Simplify", "Unevaluate", "Group", "ClearEvent"});
		timeMenu 	= makeOneMenu(new String[]{"SetPos", "MaxZoom", "PreviousZoom", "ZoomIn", "ZoomOut"});
	}
	
	PopupMenu makeOneMenu(String[] labels)
	{
		PopupMenu ppm = new PopupMenu();
		for (int i = 0; i < labels.length; i++) {
			MenuItem mi = new MenuItem(labels[i]);
			mi.setActionCommand(labels[i]);
			mi.addActionListener(this);
			ppm.add(mi);
		}
		this.add(ppm);
		return ppm;
	}	
	
		
	public void actionPerformed(ActionEvent e)
	{
		//System.out.println( e.getActionCommand());
		String cmd = e.getActionCommand();
		
		// analyse des commandes
				if (cmd.equals("PlayAll")) 			PlayAllTracks();
		else 	if (cmd.equals("ClearAll")) 		ClearAllTracks();
		else 	if (cmd.equals("StopAll")) 			StopAllTracks();
		else 	if (cmd.equals("StartAll")) 		StartAllTracks();
		
		else 	if (cmd.equals("NormalTrack")) 		NormalModeSelTrack();
		else 	if (cmd.equals("SeqFunctionTrack")) SeqFunModeSelTrack();
		else 	if (cmd.equals("MixFunctionTrack")) MixFunModeSelTrack();
		else 	if (cmd.equals("ParamTrack")) 		ParModeSelTrack();
		else 	if (cmd.equals("AbstractTrack")) 	AbsModeSelTrack();
		else 	if (cmd.equals("MuteTrack")) 		muteSelTrack();
		else 	if (cmd.equals("UnmuteTrack")) 		unmuteSelTrack();
		else 	if (cmd.equals("PlayTrack")) 		PlaySelTrack();
		else 	if (cmd.equals("ClearTrack")) 		ClearSelTrack();
		
		else 	if (cmd.equals("PlayEvent"))		PlaySelEvent();
		else 	if (cmd.equals("Evaluate")) 		EvaluateSelEvent();
		else 	if (cmd.equals("Simplify")) 		ReifySelEvent();
		else 	if (cmd.equals("Unevaluate")) 		UnevaluateSelEvent();
		else 	if (cmd.equals("ClearEvent")) 		ClearSelEvent();
		else 	if (cmd.equals("Group"))	 		GroupSelEvent();
		
		else 	if (cmd.equals("SetPos")) 			SetPos(fPosMs);
		else 	if (cmd.equals("MaxZoom")) 			MaxZoom();
		else 	if (cmd.equals("PreviousZoom")) 	PreviousZoom();
		else 	if (cmd.equals("ZoomIn")) 			ZoomIn();
		else 	if (cmd.equals("ZoomOut")) 			ZoomOut();
			
		fUpdater.doUpdates();
	}
	
	void SetPos(int date_ms)
	{
		if (fCurExp != null) {  // Si pas d'expression en cours
			fPlayer.setPosMsPlayer(date_ms);
			updateTimeLine();
		}
	}
	
	void PlayAllTracks()
	{
		StopAllTracks();
	
		if (fCurExp == null) {  // Si pas d'expression en cours
			StartAllTracks();
		}else if (fPlayer.getState().date < fDuration) {  // Si le rendu n'est pas terminé
			fTask.Forget();
			fPlayer.contPlayer();
			TGlobals.midiappl.ScheduleTask(fTask, Midi.GetTime());
		}
	}
	
	void StartAllTracks()
	{
		StopAllTracks();
		
		fCurExp = TLConverter.exp(fMultiTracks);
		
		if (!(fCurExp instanceof TNullExp)) {
			fDuration = (int)TEvaluator.gEvaluator.Duration(fCurExp); 
			fTask.Forget();
			fPlayer.startPlayer (fCurExp);
			TGlobals.midiappl.ScheduleTask(fTask, Midi.GetTime());
			notifier.notifyObservers (fCurExp);
		}
	}
	
	void StopAllTracks()
	{
		if (fPlayer.getState().state == MidiPlayer.kPlaying) {
			fTask.Forget();
			fPlayer.stopPlayer();
		}
	}
	
	void ClearAllTracks()
	{
		fMultiTracks.clear();
		fName.setText("");
		multiTracksChanged();
		fCurExp = TLConverter.exp(fMultiTracks);
		notifier.notifyObservers (fCurExp);
	}

	void NormalModeSelTrack()
	{
		if (fMultiTracks.at(fTrackNum)) {
			fMultiTracks.getTrack().setTrackMode(TLTrack.NORMAL);
			multiTracksChanged();
		}
	}

	void SeqFunModeSelTrack()
	{
		if (fMultiTracks.at(fTrackNum)) {
			fMultiTracks.getTrack().setTrackMode(TLTrack.SEQFUNCTION);
			multiTracksChanged();
		}
	}
	
	void MixFunModeSelTrack()
	{
		if (fMultiTracks.at(fTrackNum)) {
			fMultiTracks.getTrack().setTrackMode(TLTrack.MIXFUNCTION);
			multiTracksChanged();
		}
	}

	
	void ParModeSelTrack()
	{
		if (fMultiTracks.at(fTrackNum)) {
			fMultiTracks.getTrack().setTrackMode(TLTrack.PARAMETER);
			multiTracksChanged();
		}
	}
	
	void AbsModeSelTrack()
	{
		if (fMultiTracks.at(fTrackNum)) {
			fMultiTracks.getTrack().setTrackMode(TLTrack.ABSTRACTION);
			multiTracksChanged();
		}
	}

	void muteSelTrack()
	{
		if (fMultiTracks.at(fTrackNum)) {
			fMultiTracks.getTrack().setMuteFlag(true);
			multiTracksChanged();
		}
	}

	void unmuteSelTrack()
	{
		if (fMultiTracks.at(fTrackNum)) {
			fMultiTracks.getTrack().setMuteFlag(false);
			multiTracksChanged();
		}
	}

	void PlaySelTrack()
	{
		if (fMultiTracks.at(fTrackNum)) {
			TLTrack tr =fMultiTracks.getTrack();
			int savedTrackMode = tr.getTrackMode();
			tr.setTrackMode(TLTrack.NORMAL);
			TExp  res = TLConverter.exp(tr);
			tr.setTrackMode(savedTrackMode);
			notifier.notifyObservers (res);
			TGlobals.player.startPlayer (res);
		}
	}

	void ClearSelTrack() 
	{
		if (fMultiTracks.at(fTrackNum)) {
			TLTrack t = fMultiTracks.remove();
			t.clear();
			fMultiTracks.insert(t);
			if (fSelection.voice() == fTrackNum) fSelection.normalizeZone();
				
			multiTracksChanged();
		}
	}

	void PlaySelEvent()
	{
		fSelection.cmdPlay();
	}

	void ClearSelEvent()
	{
		fSelection.cmdClear();
		multiTracksChanged();
	}

	void GroupSelEvent()
	{
		fSelection.cmdGroup();
		multiTracksChanged(); 
	}

	void EvaluateSelEvent()
	{
		fSelection.cmdEvaluate();
		multiTracksChanged();
	}

	void UnevaluateSelEvent()
	{
		fSelection.cmdUnevaluate();
		multiTracksChanged();
	}

	void ReifySelEvent()
	{
		fSelection.cmdReify();
		multiTracksChanged();
	}

	void MaxZoom()
	{
		setZoomAt(fZoomMax, fZoomX);
	}

	void PreviousZoom()
	{
		setZoomAt(fOldZoom, fZoomX);
	}

	void ZoomIn()
	{
		setZoomAt(fZoom*2, fZoomX);
	}

	void ZoomOut()
	{
		setZoomAt(fZoom/2, fZoomX);
	}
	
	
	//==================================================================================
	//
	//			  gestion du clavier
	//
	//==================================================================================
	
	public void keyReleased (KeyEvent e) 	{  }
	public void keyTyped(KeyEvent e) 	{  }
	public void keyPressed(KeyEvent e) 
	{
		//System.out.println( "keyPressed " + e + ", " + e.getKeyCode());
		int kc = e.getKeyCode();

		if (kc == KeyEvent.VK_BACK_SPACE) {
			if (fSelection.empty()) {
				fSelection.extendLeft(fUnit);
			}
			if (!fSelection.empty()) {
				fSelection.cmdClear();
				multiTracksChanged();
			}
			fUpdater.selectionChanged();
			fUpdater.doUpdates();
		
		} else if (kc == KeyEvent.VK_LEFT /*37*/) {
			if (e.isShiftDown()) {
				fSelection.extendLeft(fUnit);
			}else{
				fSelection.moveLeft(fUnit);
			}
			fUpdater.selectionChanged();
			fUpdater.doUpdates();
		
		} else if (kc == KeyEvent.VK_RIGHT /*39*/) {
			if (e.isShiftDown()) { 
				fSelection.extendRight(fUnit);
			}else{
				fSelection.moveRight(fUnit);
			}
			fUpdater.selectionChanged();
			fUpdater.doUpdates();
		
		} else if (kc == KeyEvent.VK_UP /*38*/) {
			fSelection.moveUp();
			fUpdater.selectionChanged();
			fUpdater.doUpdates();
		
		} else if (kc == KeyEvent.VK_DOWN /*40*/) {
			fSelection.moveDown();
			fUpdater.selectionChanged();
			fUpdater.doUpdates();
		
		} else if (e.isControlDown() || e.isMetaDown()) {

			if (kc == KeyEvent.VK_X /*88*/) {				// Ctrl+X
				fSelection.cmdCutToScrap();
				multiTracksChanged();
				fUpdater.selectionChanged();
				fUpdater.doUpdates();
			
			} else if (kc == KeyEvent.VK_C /*67*/) {		// Ctrl+C
				fSelection.cmdCopyToScrap();
				multiTracksChanged();
				fUpdater.selectionChanged();
				fUpdater.doUpdates();
			
			} else if (kc == KeyEvent.VK_V /*86*/) {		// Ctrl+V
				fSelection.cmdPasteFromScrap();
				multiTracksChanged();
				fUpdater.selectionChanged();
				fUpdater.doUpdates();
			
			}
		}
	}
	
	private BasicApplet getApplet () {
    	Component c=this;
    	while (c!=null) {
    		if (c instanceof BasicApplet){
    			return (BasicApplet)c;
    		}
    		c = c.getParent();
    	}
    	return null;
   	}

	
	
	//==================================================================================
	//
	//			  gestion du focus
	//
	//==================================================================================

	public void focusGained(FocusEvent e) 
	{ 
		setFocus(true); 
	}
	
	public void focusLost(FocusEvent e)
	{ 
		setFocus(false); 
	}

	void setFocus(boolean f)
	{
		fHasFocus = f;
		Dimension dim = getSize();
   		Graphics g = getGraphics(); 
		g.clipRect(fRuleWidth, fRuleHeight, dim.width, dim.height);
		drawSelection(g, fSelection);
		g.dispose();
	}
	
	//==================================================================================
	//
	//			  gestion de la souris
	//
	//==================================================================================

	final static int EVNMENT 		= 0;
	final static int BORD_GAUCHE 	= 1;
	final static int BORD_HAUT 		= 2;
	final static int BORD_BAS 		= 3;
	final static int BORD_DROIT 	= 4;
	final static int COIN 			= 5;
	final static int PISTE 			= 6;
	final static int TEMPS 			= 7;
	final static int RIEN	 		= 8;

	int	analyzeMousePos(MouseEvent m) 
	{ 
		return analyzeMousePos(m.getX(), m.getY()); 
	}
	
	int analyzeMousePos(int mx, int my)
	{
		if (mx < fRuleWidth && my < fRuleHeight) {			
		
			return COIN;
			
		} else if (mx >= fRuleWidth && my >= fRuleHeight) { 
			int ctime = x2time(mx);
			int cline = y2line(my);
			
			TLZone z = new TLZone(fMultiTracks, ctime, cline);
			if (z.empty()) {
				return RIEN;
			} else if (time2x(z.end()) - 5 < mx) {
				return BORD_GAUCHE;
			} else if (time2x(z.start()) + 4 > mx) {
				return BORD_DROIT;
			} else if (line2y(cline) + 4 > my) {
				return BORD_HAUT;
			} else if (line2y(cline+1) - 5 < my) {
				return BORD_BAS;
			} else {
				return EVNMENT;
			}
		} else if (my >= fRuleHeight) {	
		
			return PISTE;

		} else {
		
			return TEMPS;
		}
	}

	public void mouseEntered(MouseEvent e) { setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)); }
	public void mouseExited(MouseEvent e) { setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)); }
	public void mouseMoved(MouseEvent e) 
	{ 
		switch (analyzeMousePos(e)) {
			case EVNMENT 		:  setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)); return;
			
			case BORD_GAUCHE 	:  setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR)); return;
			case BORD_HAUT 		:  setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR)); return;
			case BORD_BAS 		:  setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR)); return;
			case BORD_DROIT 	:  setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR)); return;
			
			case COIN 			:  setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)); return;
			case PISTE 			:  setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)); return;
			case TEMPS 			:  setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)); return;

			case RIEN 			:  setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)); return;
		} 
	
	}
	
	public void adjustSelection(int ctime, int cline)
	{
		if (!fSelection.contains(ctime, cline)) {
			fSelection.selectEvent(ctime, cline);
			fUpdater.selectionChanged();
		}
	}
	
	public void mousePressed(MouseEvent m) 
	{
		int mx 		= m.getX();
		int my 		= m.getY();
		int ctime 	= x2time(mx);
		int cline 	= y2line(my);
		
		fDragAction = sTLNullAction;
		int z = analyzeMousePos(mx,my);
		
		if (isPopupEvent(m)) {
			switch (z) {
				case EVNMENT : 	adjustSelection(ctime, cline); 
								selectionChanged(); 
								eventMenu.show(this, mx, my); 
								break;
				case COIN	 : scoreMenu.show(this, mx, my); break;
				case PISTE 	 : fTrackNum = y2line(my); trackMenu.show(this, mx, my); break;
				case TEMPS	 : fZoomX = mx; fPosMs = ctime; timeMenu.show(this, mx, my); break;
			}
			
		} else if (m.isShiftDown()) {
			switch (z) {
				case EVNMENT : 	fDragAction = new TLExtendSelectionAction(this); 
								//selectionChanged(); 
								break;
			}
			
		} else {
			switch (z) {
				case EVNMENT 	:  
					adjustSelection(ctime, cline);
					fDragAction = new TLMoveAction (this, time2x(fSelection.start()) - mx); 
					break;
				
				case BORD_GAUCHE 	:  
					adjustSelection(ctime, cline);
					fDragAction = new TLResizeAction (this); 
					break;
					
				case BORD_HAUT 		: 
					adjustSelection(ctime, cline);
					fDragAction = new TLPitchAction (this, mx,my);; 
					break;
					
				case BORD_BAS 		: 
					adjustSelection(ctime, cline);
					fDragAction = new TLVelocityAction (this, mx, my); 
					break;
					
				case BORD_DROIT 	:  
					adjustSelection(ctime, cline);
					fDragAction = new TLChannelAction (this, mx, my); 
					break;
				
				case COIN 			:  fDragAction = new TLScoreMoveAction(this); break;
				case PISTE 			:  fDragAction = new TLTrackMoveAction(this, fSelection, my); break;
				case TEMPS 			:  fDragAction = new TLZoomAction(this, mx, my); break;

				case RIEN 			:  fDragAction = new TLUnselectAction(this); break;
			}
		}
		fUpdater.doUpdates();
		Graphics g = getGraphics();
		fDragAction.drawVisualFeedback(g);
		g.dispose();
	}
	
	
	public void mouseDragged(MouseEvent m) 
	{ 
	
		Graphics g = getGraphics();
	
		// redessine le feedback visuel pour l'effacer (mode XOR)
		fDragAction.drawVisualFeedback(g);

		// appel l'action de drag et redessine le nouveau feedback visuel
		fDragAction.mouseDragged(m); 
		fUpdater.doUpdates(false);
		fDragAction.drawVisualFeedback(g);

		g.dispose();
	}
	
	public void mouseReleased(MouseEvent m)
	{ 
		Graphics g = getGraphics();
		fDragAction.drawVisualFeedback(g);
		g.dispose();
		fDragAction.mouseReleased(m);
		fUpdater.doUpdates();
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
	
	
	public void mouseClicked(MouseEvent m) 
	{ 
		fDragAction.mouseClicked(m);
		fUpdater.doUpdates();
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		requestFocus(); // essai
	}
	
	
	
	//==================================================================================
	//
	//			  Les fonctions d'édition du multitrack
	//
	//==================================================================================


	//-------------------------------------------------
	// getEventAt(time, line) 
	//-------------------------------------------------
	// récupére l'événement sous les coords (time, line)
	// modifie les variables sAuxEvent, sStartTime, sEndTime
	
	public TLEvent getEventAt(int time, int line) 
	{
		sAuxEvent = null;
		if ( time >= 0 && line >= 0 && fMultiTracks.at(line) ) {
			TLTrack t = fMultiTracks.getTrack();
			if (t.at(time)) {
				sAuxEvent = t.getEvent();
				sStartTime = t.getDate();
				sEndTime = sStartTime + sAuxEvent.fDur;
			}
			sEvInGroup = t.isCurEvInGroup();
			sDtInGroup = t.isCurDateInGroup();
		}
		return sAuxEvent;
	}

	public static Color getFArgColorBkg() { return fArgColorBkg; }
	public static int getFLineHeight() { return fLineHeight; }
	public int getFLinePos() { return fLinePos; }
	public TLMultiTracks getFMultiTracks() { return fMultiTracks; }
	public TextField getFName() { return fName; }
	public TLZone getFSelection() { return fSelection; }
	public static Color getFTraitColor() { return fTraitColor; }
	public double getFZoom() { return fZoom; }

	public void setFOldZoom(double oldZoom) { fOldZoom = oldZoom; }	
	public void setFAutoSroll(boolean autoSroll) { fAutoSroll = autoSroll; }
}

/*******************************************************************************************
*
*	TimeLineTask (classe) : tache d'affichage du curseur de jeu
* 
*******************************************************************************************/

final class TimeLineTask extends MidiTask {

	TLPane pane;
	
	TimeLineTask (TLPane pane) { this.pane = pane;}
	
	public void Execute (MidiAppl appl, int date) { 
		if(pane.updateTimeLine()) {  // player toujours en marche
			TGlobals.midiappl.ScheduleTask(this, Math.max (Midi.GetTime(), date + 250));
		}
	}
}