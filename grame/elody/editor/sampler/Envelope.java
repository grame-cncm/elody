package grame.elody.editor.sampler;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;

import com.swtdesigner.SWTResourceManager;

public class Envelope {
/***** DESCRIPTION ************************************
 * The ADSR envelope function is to modulate the volume of the sample sound.
 * The contour of the ADSR envelope is specified using four parameters:
 * -Attack (in milliseconds): how quickly the sound reaches full volume
 * 								after a MIDI KeyOn event.
 * -Decay (in milliseconds): how quickly the sound drops to the sustain
 * 								level after the initial peak.
 * -Sustain (in decibels): the "constant" volume that the sound takes after
 * 								decay until the note is released. Note that
 * 								this parameter specifies a volume level
 * 								rather than a time period.
 * -Release (in milliseconds): how quickly the sound fades when a note ends
 * 								(after a MIDI KeyOff event).
 * Each "Envelope" instance (identified by its parent channel)
 * provides an envelope editor window (shell) that includes a viewer to
 * represent the envelope shape, and an interface for getting and setting
 * these four parameters.
 ******************************************************/		
	private Channel ch;
	private Canvas canvas;
	
	private int attack;		//milliseconds (0..15000)
	private int decay;		//milliseconds (0..25000)
	private double sustain;	//negative decibels (-30..0)
	private int release;	//milliseconds (0..25000)
	
	private Shell s;
	private boolean shellOpened = false;
	
	final private Color RED = new Color(Display.getCurrent(),255,0,0);
	final private Color GREY = new Color(Display.getCurrent(),128,128,128);
	final private Color GREEN = new Color(Display.getCurrent(),0,128,0);
	final private Color YELLOW = new Color(Display.getCurrent(),255,255,200);
	final private Color BLACK = new Color(Display.getCurrent(),0,0,0);
	
	final private Color AXIS = BLACK;
	final private Color ENVELOPE_BORDER = GREEN;
	final private Color ENVELOPE_INSIDE = YELLOW;
	final private Color DIMENSIONS = GREY;
	final private Color PROJECTION_LINE = RED;
	
	public Envelope(int attack, int decay, double sustain, int release, Channel ch) {
		this.attack = attack;
		this.decay = decay;
		this.sustain = sustain;
		this.release = release;
		this.ch = ch;
	}

	public int getAttack()		{ return attack; }
	public int getDecay()		{ return decay; }
	public double getSustain()	{ return sustain; }
	public int getRelease()		{ return release; }
	
	public void shellOpen()
	{
		if (!shellOpened)
		{
			s = new Shell(SWT.DIALOG_TRIM);
			s.setRedraw(false);
			s.addShellListener(new ShellAdapter() {
				public void shellClosed(final ShellEvent e) {
					s.dispose();
					s=null;
					shellOpened = false;
				}
				public void shellDeactivated(final ShellEvent e) {
					ch.sampler.resetDriver();
				}
			});
			s.setSize(500, 450);
			s.setText("Sampler - Channel "+ch.getNum()+" - Envelope Editor");
			s.setBackground(ch.bgColor);
			s.setLayout(new FormLayout());
			s.setVisible(true);
			s.setActive();
			buildInterface();
			s.layout();
			s.setRedraw(true);
			shellOpened = true;
		}
		else
		{
			s.setActive();
		}
	}
	
	public void shellClose() {
		if (s!=null)
			s.close();
		// see ShellAdapter "shellClosed"
	}
	
	private void buildInterface()
	{
		final Composite composite = new Composite(s, SWT.NONE);
		composite.setBackground(ch.bgColor);
		final FormData formData = new FormData();
		formData.bottom = new FormAttachment(0, 175);
		formData.right = new FormAttachment(100, -10);
		formData.top = new FormAttachment(0, 10);
		formData.left = new FormAttachment(0, 10);
		composite.setLayoutData(formData);
		composite.setLayout(new FillLayout());
		
		canvas = new Canvas(composite, SWT.BORDER);
		canvas.setBackground(ch.bgColor);
		canvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				redraw(e.gc);
			}
		});

		final Label attackLabel = new Label(s, SWT.NONE);
		attackLabel.setBackground(ch.bgColor);
		attackLabel.setFont(SWTResourceManager.getFont("", 10, SWT.BOLD));
		final FormData attackLabelFd = new FormData();
		attackLabelFd.right = new FormAttachment(100, -380);
		attackLabelFd.top = new FormAttachment(0, 200);
		attackLabel.setLayoutData(attackLabelFd);
		attackLabel.setText("ATTACK:");

		final Label decayLabel = new Label(s, SWT.NONE);
		decayLabel.setBackground(ch.bgColor);
		decayLabel.setFont(SWTResourceManager.getFont("", 10, SWT.BOLD));
		final FormData decayLabelFd = new FormData();
		decayLabelFd.right = new FormAttachment(100, -380);
		decayLabelFd.top = new FormAttachment(0, 250);
		decayLabel.setLayoutData(decayLabelFd);
		decayLabel.setText("DECAY:");

		final Label sustainLabel = new Label(s, SWT.NONE);
		sustainLabel.setBackground(ch.bgColor);
		sustainLabel.setFont(SWTResourceManager.getFont("", 10, SWT.BOLD));
		final FormData sustainLabelFd = new FormData();
		sustainLabelFd.right = new FormAttachment(100, -380);
		sustainLabelFd.top = new FormAttachment(0, 300);
		sustainLabel.setLayoutData(sustainLabelFd);
		sustainLabel.setText("SUSTAIN:");

		final Label releaseLabel = new Label(s, SWT.NONE);
		releaseLabel.setBackground(ch.bgColor);
		releaseLabel.setFont(SWTResourceManager.getFont("", 10, SWT.BOLD));
		final FormData releaseLabelFd = new FormData();
		releaseLabelFd.right = new FormAttachment(100, -380);
		releaseLabelFd.top = new FormAttachment(0, 350);
		releaseLabel.setLayoutData(releaseLabelFd);
		releaseLabel.setText("RELEASE:");

		final Spinner attackSpinner = new Spinner(s, SWT.BORDER);
		final Scale attackScale = new Scale(s, SWT.NONE);
		attackScale.setBackground(ch.bgColor);
		attackScale.setMinimum(0);
		attackScale.setMaximum(15000);
		attackScale.setIncrement(500);
		attackScale.setPageIncrement(1000);
		attackScale.setSelection(attack);
		final FormData attackScaleFd = new FormData();
		attackScaleFd.left = new FormAttachment(0, 130);
		attackScaleFd.right = new FormAttachment(0, 350);
		attackScaleFd.top = new FormAttachment(0, 188);
		attackScale.setLayoutData(attackScaleFd);
		attackScale.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				attackSpinner.setSelection(attackScale.getSelection());
				attack = attackSpinner.getSelection();
				canvas.redraw();
				for (int i=0; i<ch.getKeygroups().size(); i++)
				{
					Keygroup k = (Keygroup) ch.getKeygroups().get(i);
					k.setAttack(attack);
				}
				ch.sampler.configSav.writeAll();
				ch.sampler.needToReset=true;
			}
		});

		final Spinner decaySpinner = new Spinner(s, SWT.BORDER);
		final Scale decayScale = new Scale(s, SWT.NONE);
		decayScale.setBackground(ch.bgColor);
		decayScale.setMinimum(0);
		decayScale.setSelection(decay);
		decayScale.setMaximum(25000);
		decayScale.setIncrement(500);
		decayScale.setPageIncrement(1000);
		final FormData decayScaleFd = new FormData();
		decayScaleFd.left = new FormAttachment(0, 130);
		decayScaleFd.right = new FormAttachment(0, 350);
		decayScaleFd.top = new FormAttachment(0, 238);
		decayScale.setLayoutData(decayScaleFd);
		decayScale.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				decaySpinner.setSelection(decayScale.getSelection());
				decay = decaySpinner.getSelection();
				canvas.redraw();
				for (int i=0; i<ch.getKeygroups().size(); i++)
				{
					Keygroup k = (Keygroup) ch.getKeygroups().get(i);
					k.setDecay(decay);
				}
				ch.sampler.configSav.writeAll();
				ch.sampler.needToReset=true;
			}
		});

		final Spinner sustainSpinner = new Spinner(s, SWT.BORDER);
		final Scale sustainScale = new Scale(s, SWT.NONE);
		sustainScale.setBackground(ch.bgColor);
		sustainScale.setMinimum(0);
		sustainScale.setMaximum(300);
		sustainScale.setIncrement(10);
		sustainScale.setPageIncrement(50);
		// sustain value is a 0..300 integer in interface
		sustainScale.setSelection((int)(-sustain*10));
		final FormData sustainScaleFd = new FormData();
		sustainScaleFd.left = new FormAttachment(0, 130);
		sustainScaleFd.right = new FormAttachment(0, 350);
		sustainScaleFd.top = new FormAttachment(0, 288);
		sustainScale.setLayoutData(sustainScaleFd);
		sustainScale.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				sustainSpinner.setSelection(sustainScale.getSelection());
				sustain = (double) (-sustainSpinner.getSelection()/10.0);
				canvas.redraw();
				for (int i=0; i<ch.getKeygroups().size(); i++)
				{
					Keygroup k = (Keygroup) ch.getKeygroups().get(i);
					k.setSustain(sustain);
				}
				ch.sampler.configSav.writeAll();
				ch.sampler.needToReset=true;
			}
		});

		final Spinner releaseSpinner = new Spinner(s, SWT.BORDER);
		final Scale releaseScale = new Scale(s, SWT.NONE);
		releaseScale.setBackground(ch.bgColor);
		releaseScale.setMinimum(0);
		releaseScale.setMaximum(25000);
		releaseScale.setIncrement(500);
		releaseScale.setPageIncrement(1000);
		releaseScale.setSelection(release);
		final FormData releaseScaleFd = new FormData();
		releaseScaleFd.left = new FormAttachment(0, 130);
		releaseScaleFd.right = new FormAttachment(0, 350);
		releaseScaleFd.top = new FormAttachment(0, 338);
		releaseScale.setLayoutData(releaseScaleFd);
		releaseScale.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				releaseSpinner.setSelection(releaseScale.getSelection());
				release = releaseSpinner.getSelection();
				canvas.redraw();
				for (int i=0; i<ch.getKeygroups().size(); i++)
				{
					Keygroup k = (Keygroup) ch.getKeygroups().get(i);
					k.setRelease(release);
				}
				ch.sampler.configSav.writeAll();
				ch.sampler.needToReset=true;
			}
		});

		attackSpinner.setMinimum(0);
		attackSpinner.setMaximum(15000);
		attackSpinner.setIncrement(10);
		attackSpinner.setPageIncrement(100);
		attackSpinner.setSelection(attack);
		final FormData attackSpinnerFd = new FormData();
		attackSpinnerFd.left = new FormAttachment(0, 360);
		attackSpinnerFd.right = new FormAttachment(0, 440);
		attackSpinnerFd.top = new FormAttachment(0, 200);
		attackSpinner.setLayoutData(attackSpinnerFd);
		attackSpinner.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				attackScale.setSelection(attackSpinner.getSelection());
				attack = attackSpinner.getSelection();
				canvas.redraw();
				for (int i=0; i<ch.getKeygroups().size(); i++)
				{
					Keygroup k = (Keygroup) ch.getKeygroups().get(i);
					k.setAttack(attack);
				}
				ch.sampler.configSav.writeAll();
				ch.sampler.needToReset=true;
			}
		});

		decaySpinner.setMinimum(0);
		decaySpinner.setMaximum(25000);
		decaySpinner.setIncrement(10);
		decaySpinner.setPageIncrement(100);
		decaySpinner.setSelection(decay);
		final FormData decaySpinnerFd = new FormData();
		decaySpinnerFd.left = new FormAttachment(0, 360);
		decaySpinnerFd.right = new FormAttachment(0, 440);
		decaySpinnerFd.top = new FormAttachment(0, 250);
		decaySpinner.setLayoutData(decaySpinnerFd);
		decaySpinner.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				decayScale.setSelection(decaySpinner.getSelection());
				decay = decaySpinner.getSelection();
				canvas.redraw();
				for (int i=0; i<ch.getKeygroups().size(); i++)
				{
					Keygroup k = (Keygroup) ch.getKeygroups().get(i);
					k.setDecay(decay);
				}
				ch.sampler.configSav.writeAll();
				ch.sampler.needToReset=true;
			}
		});

		sustainSpinner.setDigits(1); // gives a 0.0 to 30.0 floating representation
				// but the stored value is still a 0..300 integer
		sustainSpinner.setMinimum(0);
		sustainSpinner.setMaximum(300);
		sustainSpinner.setIncrement(1);
		sustainSpinner.setPageIncrement(10);
		// sustain value is a 0..300 integer in interface
		sustainSpinner.setSelection((int)(-sustain*10));
		final FormData sustainSpinnerFd = new FormData();
		sustainSpinnerFd.left = new FormAttachment(0, 360);
		sustainSpinnerFd.right = new FormAttachment(0, 440);
		sustainSpinnerFd.top = new FormAttachment(0, 300);
		sustainSpinner.setLayoutData(sustainSpinnerFd);
		sustainSpinner.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				sustainScale.setSelection(sustainSpinner.getSelection());
				// sustain real value is a -30..0 double
				sustain = (double) (-sustainSpinner.getSelection()/10.0);
				canvas.redraw();
				for (int i=0; i<ch.getKeygroups().size(); i++)
				{
					Keygroup k = (Keygroup) ch.getKeygroups().get(i);
					k.setSustain(sustain);
				}
				ch.sampler.configSav.writeAll();
				ch.sampler.needToReset=true;
			}
		});

		releaseSpinner.setMinimum(0);
		releaseSpinner.setMaximum(25000);
		releaseSpinner.setIncrement(10);
		releaseSpinner.setPageIncrement(100);
		releaseSpinner.setSelection(release);
		final FormData releaseSpinnerFd = new FormData();
		releaseSpinnerFd.left = new FormAttachment(0, 360);
		releaseSpinnerFd.right = new FormAttachment(0, 440);
		releaseSpinnerFd.top = new FormAttachment(0, 350);
		releaseSpinner.setLayoutData(releaseSpinnerFd);
		releaseSpinner.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				releaseScale.setSelection(releaseSpinner.getSelection());
				release = releaseSpinner.getSelection();
				canvas.redraw();
				for (int i=0; i<ch.getKeygroups().size(); i++)
				{
					Keygroup k = (Keygroup) ch.getKeygroups().get(i);
					k.setRelease(release);
				}
				ch.sampler.configSav.writeAll();
				ch.sampler.needToReset=true;
			}
		});

		final Label attackUnitLabel = new Label(s, SWT.NONE);
		attackUnitLabel.setBackground(ch.bgColor);
		final FormData attackUnitFd = new FormData();
		attackUnitFd.top = new FormAttachment(0, 200);
		attackUnitFd.left = new FormAttachment(0, 450);
		attackUnitLabel.setLayoutData(attackUnitFd);
		attackUnitLabel.setText("ms");

		final Label decayUnitLabel = new Label(s, SWT.NONE);
		decayUnitLabel.setBackground(ch.bgColor);
		final FormData decayUnitFd = new FormData();
		decayUnitFd.top = new FormAttachment(0, 250);
		decayUnitFd.left = new FormAttachment(0, 450);
		decayUnitLabel.setLayoutData(decayUnitFd);
		decayUnitLabel.setText("ms");

		final Label sustainUnitLabel = new Label(s, SWT.NONE);
		sustainUnitLabel.setBackground(ch.bgColor);
		final FormData sustainUnitFd = new FormData();
		sustainUnitFd.top = new FormAttachment(0, 300);
		sustainUnitFd.left = new FormAttachment(0, 450);
		sustainUnitLabel.setLayoutData(sustainUnitFd);
		sustainUnitLabel.setText("dB");

		final Label releaseUnitLabel = new Label(s, SWT.NONE);
		releaseUnitLabel.setBackground(ch.bgColor);
		final FormData releaseUnitFd = new FormData();
		releaseUnitFd.top = new FormAttachment(0, 350);
		releaseUnitFd.left = new FormAttachment(0, 450);
		releaseUnitLabel.setLayoutData(releaseUnitFd);
		releaseUnitLabel.setText("ms");
		
		final Button closeButton = new Button(s, SWT.NONE);
		final FormData closeFd = new FormData();
		closeFd.top = new FormAttachment(0, 385);
		closeFd.left = new FormAttachment(0, 220);
		closeButton.setLayoutData(closeFd);
		closeButton.setText("Close");
		closeButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				shellClose();
			}
		});
	}

	public void redraw(GC gc)
	{
		Point origin = new Point(15,140);
		int absLength = 420;
		int ordLength = 130;
		int sustainConstant = 150;
		
		int A = time2pixel(attack, absLength, sustainConstant);
		int D = time2pixel(decay, absLength, sustainConstant);
		int S = db2pixel(sustain, ordLength);
		int R = time2pixel(release, absLength, sustainConstant);
		if ((A+D+R)==0)	sustainConstant=absLength;
		Point p1 = origin;
		Point p2 = new Point(p1.x+A,origin.y-ordLength);
		Point p3 = new Point(p2.x+D,origin.y-S);
		Point p4 = new Point(p3.x+sustainConstant,origin.y-S);
		Point p5 = new Point(origin.x+absLength,origin.y);
		
		gc.setBackground(ENVELOPE_INSIDE);
		gc.fillPolygon(new int[] {p1.x,p1.y, p2.x, p2.y, p3.x, p3.y, p4.x, p4.y, p5.x, p5.y});

		drawVerticalDottedLine(p2, origin.y, gc);
		drawVerticalDottedLine(p3, origin.y, gc);
		drawVerticalDottedLine(p4, origin.y, gc);
		
		gc.setForeground(ENVELOPE_BORDER);
		gc.drawPolyline(new int[] {p1.x,p1.y, p2.x, p2.y, p3.x, p3.y, p4.x, p4.y, p5.x, p5.y});
		
		drawDimension(p1,p2, "A", origin, gc);
		drawDimension(p2,p3, "D", origin, gc);
		drawDimension(p3,p4, "S", origin, gc);
		drawDimension(p4,p5, "R", origin, gc);
		
		drawAxis(origin.y, absLength+20, origin.x, ordLength,gc);

	}
	
	private void drawAxis(int abs, int absLength, int ord, int ordLength, GC gc)
	{
		gc.setForeground(AXIS);
		//horizontal axis
		gc.drawLine(ord-6,abs,ord+absLength,abs);
		//horizontal arrow
		gc.drawLine(ord+absLength-11,abs-1,ord+absLength-3,abs-1);
		gc.drawLine(ord+absLength-11,abs+1,ord+absLength-3,abs+1);
		gc.drawLine(ord+absLength-11,abs-2,ord+absLength-6,abs-2);
		gc.drawLine(ord+absLength-11,abs+2,ord+absLength-6,abs+2);
		gc.drawLine(ord+absLength-12,abs-3,ord+absLength-9,abs-3);
		gc.drawLine(ord+absLength-12,abs+3,ord+absLength-9,abs+3);
		gc.drawLine(ord+absLength-13,abs-4,ord+absLength-11,abs-4);
		gc.drawLine(ord+absLength-13,abs+4,ord+absLength-11,abs+4);
		//vertical axis 
		gc.drawLine(ord,abs-ordLength,ord,abs+16);
		gc.drawLine(ord+absLength-20,abs,ord+absLength-20,abs+16);
		//vertical arrow
		gc.drawLine(ord-1,abs-ordLength+3,ord-1,abs-ordLength+11);
		gc.drawLine(ord+1,abs-ordLength+3,ord+1,abs-ordLength+11);
		gc.drawLine(ord-2,abs-ordLength+6,ord-2,abs-ordLength+11);
		gc.drawLine(ord+2,abs-ordLength+6,ord+2,abs-ordLength+11);
		gc.drawLine(ord-3,abs-ordLength+9,ord-3,abs-ordLength+12);
		gc.drawLine(ord+3,abs-ordLength+9,ord+3,abs-ordLength+12);
		gc.drawLine(ord-4,abs-ordLength+11,ord-4,abs-ordLength+13);
		gc.drawLine(ord+4,abs-ordLength+11,ord+4,abs-ordLength+13);
	}
	
	private void drawVerticalDottedLine(Point p, int abs, GC gc)
	{
		gc.setForeground(PROJECTION_LINE);
		drawBasicVerticalDottedLine(p.x, p.y, abs, gc);
		gc.drawLine(p.x, abs, p.x, abs+16);
	}
	
	private void drawBasicVerticalDottedLine(int x, int y1, int y2, GC gc)
	{
		short count = 0;
		for (int y=y2; y>=y1; y--)
		{
			if (count==3)
			{
				count=0;
				continue;
			}
			gc.drawPoint(x,y);
			count++;
		}
	}
	
	private int time2pixel (int time, int axisLength, int sustainConstant)
	{
		if (time==0)
			return 0;
		return (int) (time * (axisLength-sustainConstant) / (attack+decay+release));		
	}
	private int db2pixel (double db, int axisLength)
	{
		double k = Math.exp(db*Math.log(10.0)/10.0);
		return (int) (axisLength * k);
	}
	
	private void drawDimension(Point p1, Point p2, String label, Point origin, GC gc)
	{
		gc.setForeground(DIMENSIONS);
		int var;
		if (label.equals("S"))
		{
			var = origin.y-p1.y;
			if (var>1)
			{
				int x = (p2.x+2*p1.x)/3;
				// vertical axis
				gc.drawLine(x,p1.y+1,x,origin.y-1);
				// vertical arrows
				if (var>3)
				{
					gc.drawLine(x-1,p1.y+2,x+1,p1.y+2);
					gc.drawLine(x-1,origin.y-2,x+1,origin.y-2);
					if (var>5)
					{
						gc.drawLine(x-2,p1.y+3,x+2,p1.y+3);
						gc.drawLine(x-2,origin.y-3,x+2,origin.y-3);
						if (var>16)
						{
							gc.setFont(SWTResourceManager.getFont("", 9, SWT.BOLD));
							gc.drawString(label,x+6,((p1.y+origin.y)/2)-7,true);
						}
					}
				}
			}
			
		}
		else
		{
			var = p2.x-p1.x;
			if (var>1)
			{
				// horizontal axis
				gc.drawLine(p1.x+1,origin.y+4,p2.x-1,origin.y+4);
				// horizontal arrows
				if (var>3)
				{
					gc.drawLine(p1.x+2,origin.y+3,p1.x+2,origin.y+5);
					gc.drawLine(p2.x-2,origin.y+3,p2.x-2,origin.y+5);
					if (var>5)
					{
						gc.drawLine(p1.x+3,origin.y+2,p1.x+3,origin.y+6);
						gc.drawLine(p2.x-3,origin.y+2,p2.x-3,origin.y+6);
						if (var>16)
						{
							gc.setFont(SWTResourceManager.getFont("", 9, SWT.BOLD));
							gc.drawString(label,((p1.x+p2.x)/2)-3,origin.y+4,true);
						}
					}
				}	
			}
		}
	}
}

