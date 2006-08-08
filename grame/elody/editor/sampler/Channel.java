package grame.elody.editor.sampler;

import grame.elody.editor.constructors.Sampler;
import grame.elody.editor.misc.TGlobals;
import grame.elody.editor.misc.applets.BasicApplet;
import grame.elody.editor.misc.applets.Window;

import java.io.File;
import java.io.FileFilter;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;

import com.swtdesigner.SWTResourceManager;

public class Channel {
/***** DESCRIPTION ************************************
 * The MIDI channels are used to separate "voices" or "instruments".
 * In the sampler, the sounds associations (keygroups) are made in
 * each channel. Each "Channel" instance (identified by its MIDI number)
 * provides a channel window (shell) and a toggle button to open
 * or close it. It includes a keyboard viewer to represent the
 * keygroups locations, and an interface for creating, setting
 * and deleting keygroups. Some settings are global for every keygroups
 * in the channel, like volume, velocity sensitivity, ADSR envelope, and
 * output track or panoramic level (depending on the chosen device).
 ******************************************************/	
	final public String[] EXTENSIONS = {".wav",".aiff"};
	final public int MAX_TRACKS = 8;
	
	private short num;			// channel number
	private Button bt;			// open-close the shell
	private Shell sh = null;		// channel SWT window (shell)
	private Window w = null;	// generic window
	public Color bgColor;	
	private Vector keygroups;
	private boolean[] availKeyb;	// array indicating the keys (0..127) availability
				// to prevent from Keygroups collisions (true=available / false=busy)
	public Sampler sampler;
	private int output = 1;		// output track number (multitracks mode)
	private int pan = 64;		// panoramic (0=left / 64=center / 127=right)
	private int vol = 100;		// volume (0..127)		
	private Envelope envelope;		// ADSR envelope
	private double sensit = 0.0;	// velocity sensitivity (-1=soft / 0=normal / 1=hard)

	private boolean shellOpened = false;
	
	private Keyboard keyboard;	// keyboard viewer
	private ScrolledComposite scrolledComposite;
	private Composite keygComposite;
	private Spinner outputSpinner;	// output track number widget
	
	public Channel(Group parent, final short num, Color bgColor, Sampler samp) {
		sampler = samp;
		this.bgColor=bgColor;
		keygroups = new Vector();
		buttonCreate(parent, num);
		availKeyb = new boolean[128];
	}

	private void buttonCreate(Group parent, final short num) {
		bt=new Button(parent, SWT.TOGGLE);
		this.num=num;
		bt.setCursor(SWTResourceManager.getCursor(SWT.CURSOR_HAND));
		StringBuffer v = new StringBuffer(String.valueOf(num));
		if (num<10) { v.insert(0,"0"); }
		bt.setText(v.toString());
		bt.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if (bt.getSelection())
				{
					shellOpen();
				}
				else
				{
					shellClose();
				}
			}
		});
	}

	public void shellOpen() {
		for (int i=0; i<availKeyb.length; i++)
			availKeyb[i] = true;
		sampler.setOutputDeviceGroupEnable(false);
		sh = new Shell();
		w = new Window(sh);
		BasicApplet.screen.addWindow(w);
		sh.setRedraw(false);
		sh.addShellListener(new ShellAdapter() {
			public void shellClosed(final ShellEvent e) {
				envelope.shellClose();
				bt.setSelection(false);
				keygroups.clear();
				sampler.configSav.writeAll();
				BasicApplet.screen.delWindow(w);
				sh.dispose();
				sh=null;
				shellOpened = false;
				if (sampler.getChannelsOpenedCount()==0)
					sampler.setOutputDeviceGroupEnable(true);
			}
			public void shellDeactivated(final ShellEvent e) {
				sampler.resetDriver();
			}
		});

		sh.setSize(500, 450);
		sh.setLocation(sh.getLocation().x, sh.getLocation().y+340);
		sh.setText(TGlobals.getTranslation("Sampler")+ " - "+ TGlobals.getTranslation("Channel")+" "+num);
		sh.setBackground(bgColor);
		sh.setLayout(new FormLayout());
		// Keygroups can be added with drag-and-drop operations on audio files
		final DropTarget target = new DropTarget(sh, DND.DROP_NONE);
		target.setTransfer(new Transfer[] {FileTransfer.getInstance()});
		target.addDropListener(new DropTargetAdapter() {
			public void drop(final DropTargetEvent e) {
		        String[] filesPaths = (String[])e.data;
		        Vector files = new Vector(filesPaths.length);
		        AudioExtensionFilter filter = new AudioExtensionFilter();
		 		for (int i=0; i<filesPaths.length; i++)
				{
		 			File f = new File(filesPaths[i]);
		 			if (filter.accept(f))
		 				files.add(f);
				}
		 		File[] result = new File[files.size()];
		 		files.toArray(result);
		 		addKeygroup(result,filter);
			}
		});
		sh.setVisible(true);
		sh.setActive();
		int a = 0; 		int d = 0;		double s = 0.0;	int r = 300;
		if (sampler.configSav.isSet(num))
		{
			// getting the settings from config file
			setOutput(sampler.configSav.getOutput(num, 0));
			setVol(sampler.configSav.getVol(num, 0));
			setPan(sampler.configSav.getPan(num, 0));
			a = sampler.configSav.getAttack(num, 0);
			d = sampler.configSav.getDecay(num, 0);
			s = sampler.configSav.getSustain(num, 0);
			r = sampler.configSav.getRelease(num, 0);
		}
		envelope = new Envelope(a,d,s,r, this);
		/* each keygroup has an ouput, volume and panoramic,
		 * and envelope informations, but in interface, only the
		 * first keygroup in a channel is considered as the main
		 * information of this channel (others are ignored)
		 */
		buildInterface();
		sh.addControlListener(new ControlAdapter() {
			public void controlResized(final ControlEvent e) 
			{
				keygCompositeRefresh(false);
			}
		});
		
		if (sampler.configSav.isSet(num))
		{
			// getting the keygroups from config file
			// (adding Keygroups ONLY AFTER building interface)
			for (int i=0; i<=sampler.configSav.maxKeygroups(num); i++)
			{
				File file = sampler.configSav.getFile(num,i);
				int ref = sampler.configSav.getRef(num,i);
				int plus = sampler.configSav.getPlus(num,i);
				int minus = sampler.configSav.getMinus(num,i);
				addKeygroup(false, i, file, ref, plus, minus);
			}
		}
		sh.layout();
		sh.setRedraw(true);
		keygCompositeRefresh(false);
		shellOpened = true;
	}
	
	public void shellClose() {
		if (sh!=null)
			sh.close();
		// see ShellAdapter "shellClosed"
	}
	
	public short getNum()				{return num;}
	public Shell getShell()				{ return sh;	}
	public boolean getAvailKeyb(int i)	{ return availKeyb[i]; }
	public Vector getKeygroups() 		{ return keygroups; }
	public int getOutput()				{ return output; }
	public int getPan()					{ return pan; }
	public int getVol()					{ return vol; }
	public double getSensit()			{ return sensit; }
	// 4 envelope parameters:
	public int getAttack()		{ return envelope.getAttack(); }
	public int getDecay()		{ return envelope.getDecay(); }
	public double getSustain()	{ return envelope.getSustain(); }
	public int getRelease()		{ return envelope.getRelease(); }
	
	public void setOutput(int o)
	{
		output=o;
		for (int i=0; i<keygroups.size(); i++)
		{
			Keygroup k = (Keygroup) keygroups.get(i);
			k.setOutput(o);
		}
	}
	public void resetOutput()
	{
		int numOutputs = sampler.getNumOutputs();
		if (numOutputs>MAX_TRACKS) numOutputs = MAX_TRACKS;
		outputSpinner.setMinimum(1);
		outputSpinner.setMaximum(numOutputs);
		if (output<=numOutputs)
			outputSpinner.setSelection(output);
		else
		{
			final MessageBox mb = new MessageBox(sh, SWT.OK);
			mb.setMessage(TGlobals.getTranslation("not_enough_outputs")+num);
			mb.open();
		}
	}
	public void setVol(int v)
	{
		vol=v;
		for (int i=0; i<keygroups.size(); i++)
		{
			Keygroup k = (Keygroup) keygroups.get(i);
			k.setVol(v);
		}
	}
	public void setPan(int p)
	{
		pan=p;
		for (int i=0; i<keygroups.size(); i++)
		{
			Keygroup k = (Keygroup) keygroups.get(i);
			k.setPan(p);
		}
	}
	public void setSensit(int s)
	{
		sensit=(s/100.0)-1; // sensit value is a 0..200 integer in interface
		for (int i=0; i<keygroups.size(); i++)
		{
			Keygroup k = (Keygroup) keygroups.get(i);
			k.setSensit(sensit);
		}
	}
	public boolean isShellOpened() {return shellOpened; }
	public void setAvailKeyb(int i, boolean value) { availKeyb[i] = value; }
	public void addKeygroup(File[] files, AudioExtensionFilter filter)
	{
		Cursor cursor = new Cursor(sh.getDisplay(), SWT.CURSOR_WAIT);
		sh.setCursor(cursor);
		for (int i=0; i<files.length; i++)
		{
			if (files[i].isDirectory())
			{
				addKeygroup(files[i].listFiles(filter), filter);
			}
			else
			{
				addKeygroup(false, -1);
				Keygroup k = (Keygroup) keygroups.lastElement();
				k.setFile(files[i]);
				sampler.configSav.writeAll();
			}

		}
		keygCompositeRefresh(true);
		sh.setCursor(null);
		cursor.dispose();
	}
	
	public void addKeygroup(boolean refresh, int index, File file, int ref, int plus, int minus)
	{
		addKeygroup(refresh, index);
		Keygroup k = (Keygroup) keygroups.lastElement();
		k.setFile(file);
		k.setRef(ref);
		k.setPlus(plus);
		k.setMinus(minus);
		sampler.configSav.writeAll();
	}
	
	public void addKeygroup(boolean refresh, int index)
	{	
		boolean sav=true; // need to be added in config file
		int keygIndex = 1;
		Composite relative = null;
		if (!keygroups.isEmpty())
		{
			Keygroup last = (Keygroup) keygroups.lastElement();
			keygIndex = last.getIndex()+1;
			relative = last.getGroup();
		}
		if (index!=-1) // keygroup already indexed
		{
			keygIndex=index;
			sav=false; // no need to be added in config file
		} 
		keygroups.add(new Keygroup(keygIndex, keygComposite, relative, bgColor, this, sav));
		if (refresh) { keygCompositeRefresh(true); }
		sampler.needToReset=true;
	}

	public void delKeygroup(Keygroup k)
	{
		for (int i=k.getRef()-k.getMinus(); i<=k.getRef()+k.getPlus(); i++)
		{
			setAvailKeyb(i,true);
		}
		int currentIndex = keygroups.indexOf(k);
		if (currentIndex<keygroups.size()-1) 
		{
			/* this keygroup is not the last added, we need to reconnect
			   the linked list before deleting */
			Keygroup next = (Keygroup) keygroups.get(currentIndex+1);
			if (currentIndex>0)
			{
				Keygroup prev = (Keygroup) keygroups.get(currentIndex-1);
				next.relocate(prev.getGroup());
			}
			else
			{
				// this keygroup is the first added
				next.relocate(null);
			}
		}
		keygroups.remove(k);
		sampler.configSav.delKeygroup(Integer.valueOf(num), k.getIndex());
		sampler.configSav.writeAll();
		keygCompositeRefresh(false);
		sampler.needToReset=true;
	}
	
	private void buildInterface() {
		Composite keybComposite = keybCompositeCreate(sh);
		Composite menuComposite1 = menuComposite1Create(sh, keybComposite);
		Composite menuComposite2 = menuComposite2Create(sh, menuComposite1);
		scrolledCompositeCreate(sh, menuComposite2);	
	}

	private Composite keybCompositeCreate(Composite parent) {
		final ScrolledComposite keybComposite = new ScrolledComposite(parent, SWT.H_SCROLL);
		final FormData formData = new FormData();
		formData.bottom = new FormAttachment(0, 110);
		formData.right = new FormAttachment(100, -10);
		formData.top = new FormAttachment(0, 10);
		formData.left = new FormAttachment(0, 10);
		keybComposite.setLayoutData(formData);
		keybComposite.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		keybComposite.setLayout(new FormLayout());
		keybComposite.getHorizontalBar().setIncrement(5);
		keybComposite.getHorizontalBar().setPageIncrement(91);

		keyboard = new Keyboard(keybComposite, SWT.NONE, this);
		keybComposite.setContent(keyboard);
		keybComposite.setOrigin(274,0);
		
		return keybComposite;
	}

	private Composite menuComposite1Create(Composite parent, Control relative) {
		
		boolean stereo = sampler.isStereo();
		
		final Composite menuComposite1 = new Composite(parent, SWT.NONE);
		menuComposite1.setBackground(bgColor);
		final FormData fd = new FormData();
		fd.bottom = new FormAttachment(relative, 50, SWT.BOTTOM);
		fd.right = new FormAttachment(100, 0);
		fd.top = new FormAttachment(relative, 0, SWT.BOTTOM);
		fd.left = new FormAttachment(0, 0);
		menuComposite1.setLayoutData(fd);
		menuComposite1.setLayout(new FormLayout());

		final Label volLabel = new Label(menuComposite1, SWT.NONE);
		volLabel.setFont(SWTResourceManager.getFont("", 9, SWT.BOLD));
		volLabel.setBackground(bgColor);
		final FormData volFd = new FormData();
		volFd.top = new FormAttachment(0, 15);
		volFd.left = new FormAttachment(0, 10);
		volLabel.setLayoutData(volFd);
		volLabel.setText(TGlobals.getTranslation("VOL"));
		
		final Scale volScale = new Scale(menuComposite1, SWT.HORIZONTAL);
		volScale.setBackground(bgColor);
		volScale.setMinimum( 0 );
		volScale.setMaximum( 127 );
		volScale.setSelection( vol );
		volScale.setIncrement( 1 );
		volScale.setPageIncrement( 10 );
		volScale.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(final SelectionEvent e) {
			setVol(volScale.getSelection());
			sampler.configSav.writeAll();
			sampler.needToReset=true;
		}
	});
		final FormData volScFd = new FormData();
		volScFd.top = new FormAttachment(0, 5);
		volScFd.left = new FormAttachment(volLabel, 5, SWT.RIGHT);
		volScFd.right = new FormAttachment( (stereo ? 33 : 35) , 0);
		volScale.setLayoutData(volScFd);
		
		final Label sensitLabel = new Label(menuComposite1, SWT.NONE);
		sensitLabel.setFont(SWTResourceManager.getFont("", 9, SWT.BOLD));
		sensitLabel.setBackground(bgColor);
		final FormData sensitFd = new FormData();
		sensitFd.top = new FormAttachment(0, 15);
		sensitFd.left = new FormAttachment(volScale, 10, SWT.RIGHT);
		sensitLabel.setLayoutData(sensitFd);
		sensitLabel.setText(TGlobals.getTranslation("SEN"));
		
		final Scale sensitScale = new Scale(menuComposite1, SWT.HORIZONTAL);
		sensitScale.setBackground(bgColor);
		// sensit value is a 0..200 integer in interface
		sensitScale.setMinimum( 0 );
		sensitScale.setMaximum( 200 );
		sensitScale.setSelection( (int) ( (sensit+1)*100 ) );
		sensitScale.setIncrement( 5 );
		sensitScale.setPageIncrement( 50 );
		sensitScale.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(final SelectionEvent e) {
			setSensit(sensitScale.getSelection());
			sampler.configSav.writeAll();
			sampler.needToReset=true;
		}
	});
		sensitScale.addMouseListener(new MouseAdapter() {

			public void mouseDoubleClick(MouseEvent e) {
				setSensit(100);
				sensitScale.setSelection(100);
				sampler.configSav.writeAll();
				sampler.needToReset=true;
			}		
		});
		final FormData sensitScFd = new FormData();
		sensitScFd.top = new FormAttachment(0, 5);
		sensitScFd.left = new FormAttachment(sensitLabel, 5, SWT.RIGHT);
		sensitScFd.right = new FormAttachment( (stereo ? 66 : 70) , 0);
		sensitScale.setLayoutData(sensitScFd);
		
		if (stereo)
		{
			final Label panLabel = new Label(menuComposite1, SWT.NONE);
			panLabel.setFont(SWTResourceManager.getFont("", 9, SWT.BOLD));
			panLabel.setBackground(bgColor);
			final FormData panFd = new FormData();
			panFd.top = new FormAttachment(0, 15);
			panFd.left = new FormAttachment(sensitScale, 10, SWT.RIGHT);
			panLabel.setLayoutData(panFd);
			panLabel.setText(TGlobals.getTranslation("PAN"));
			
			final Scale panScale = new Scale(menuComposite1, SWT.HORIZONTAL);
			panScale.setBackground(bgColor);
			panScale.setMinimum( 0 );
			panScale.setMaximum( 127 );
			panScale.setSelection( pan );
			panScale.setIncrement( 1 );
			panScale.setPageIncrement( 10 );
			panScale.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				setPan(panScale.getSelection());
				sampler.configSav.writeAll();
				sampler.needToReset=true;
			}
		});
			panScale.addMouseListener(new MouseAdapter() {

				public void mouseDoubleClick(MouseEvent e) {
					setPan(64);
					panScale.setSelection(64);
					sampler.configSav.writeAll();
					sampler.needToReset=true;
				}		
			});
			final FormData panScFd = new FormData();
			panScFd.top = new FormAttachment(0, 5);
			panScFd.left = new FormAttachment(panLabel, 5, SWT.RIGHT);
			panScFd.right = new FormAttachment(100, -10);
			panScale.setLayoutData(panScFd);
		}
		else	// multitracks mode
		{
			outputSpinner = new Spinner(menuComposite1, SWT.BORDER);
			resetOutput();
			final FormData spinnerFd = new FormData();
			spinnerFd.top = new FormAttachment(0, 15);
			spinnerFd.right = new FormAttachment(100, -20);
			spinnerFd.left = new FormAttachment(100, -80);
			outputSpinner.setLayoutData(spinnerFd);
			outputSpinner.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					setOutput(outputSpinner.getSelection());
					sampler.configSav.writeAll();
					sampler.needToReset=true;
				}
			});
			
			final Label outputLabel = new Label(menuComposite1, SWT.NONE);
			outputLabel.setFont(SWTResourceManager.getFont("", 9, SWT.BOLD));
			outputLabel.setBackground(bgColor);
			final FormData outputFd = new FormData();
			outputFd.top = new FormAttachment(0, 15);
			outputFd.right = new FormAttachment(outputSpinner, -15, SWT.LEFT);
			outputLabel.setLayoutData(outputFd);
			outputLabel.setText(TGlobals.getTranslation("OUT"));
		}
		
		
		return menuComposite1;
	}
	
	private Composite menuComposite2Create(Composite parent, Control relative) {
		final Composite menuComposite2 = new Composite(parent, SWT.NONE);
		menuComposite2.setBackground(bgColor);
		final FormData fd = new FormData();
		fd.bottom = new FormAttachment(relative, 30, SWT.BOTTOM);
		fd.right = new FormAttachment(100, 0);
		fd.top = new FormAttachment(relative, 0, SWT.BOTTOM);
		fd.left = new FormAttachment(0, 0);
		menuComposite2.setLayoutData(fd);
		menuComposite2.setLayout(new FormLayout());

		final Label keygroupsLabel = new Label(menuComposite2, SWT.NONE);
		keygroupsLabel.setFont(SWTResourceManager.getFont("", 9, SWT.BOLD));
		keygroupsLabel.setBackground(bgColor);
		final FormData keygroupsFd = new FormData();
		keygroupsFd.top = new FormAttachment(0, 8);
		keygroupsFd.left = new FormAttachment(0, 30);
		keygroupsLabel.setLayoutData(keygroupsFd);
		keygroupsLabel.setText(TGlobals.getTranslation("KEYGROUPS"));

		final Button newButton = new Button(menuComposite2, SWT.NONE);
		final FormData buttonFd = new FormData();
		buttonFd.right = new FormAttachment(0, 165);
		buttonFd.top = new FormAttachment(0, 5);
		buttonFd.left = new FormAttachment(keygroupsLabel, 10, SWT.RIGHT);
		newButton.setLayoutData(buttonFd);
		newButton.setText(TGlobals.getTranslation("New"));
		newButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				addKeygroup(true, -1);
			}
		});
		
		final Label label = new Label(menuComposite2, SWT.NONE);
		label.setFont(SWTResourceManager.getFont("", 9, SWT.BOLD));
		label.setBackground(bgColor);
		final FormData labelFd = new FormData();
		labelFd.top = new FormAttachment(0, 15);
		labelFd.right = new FormAttachment(100, -120);
		label.setLayoutData(labelFd);
		label.setText(TGlobals.getTranslation("ref"));
		
		final Button envButton = new Button(menuComposite2, SWT.NONE);
		final FormData envFd = new FormData();
		envFd.top = new FormAttachment(0, 5);
		envFd.left = new FormAttachment(100, -90);
		envFd.right = new FormAttachment(100, -20);
		envButton.setLayoutData(envFd);
		envButton.setText(TGlobals.getTranslation("Envelope"));
		envButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				envelope.shellOpen();
			}
		});

		return menuComposite2;
	}

	private ScrolledComposite scrolledCompositeCreate(Composite parent, Control relative) {
		scrolledComposite = new ScrolledComposite(parent, SWT.V_SCROLL | SWT.BORDER );
		scrolledComposite.setBackground(bgColor);
		final FormData fd = new FormData();
		fd.bottom = new FormAttachment(100, -10);
		fd.right = new FormAttachment(100, -10);
		fd.top = new FormAttachment(relative, 10, SWT.BOTTOM);
		fd.left = new FormAttachment(0, 10);
		scrolledComposite.setLayoutData(fd);
		scrolledComposite.getVerticalBar().setIncrement(40);
		scrolledComposite.getVerticalBar().setPageIncrement(160);
		keygComposite = new Composite(scrolledComposite, SWT.NONE);
		keygComposite.setLocation(0, 0);
		keygComposite.setBackground(bgColor);
		keygComposite.setLayout(new FormLayout());
		scrolledComposite.setContent(keygComposite);
		keygCompositeRefresh(false);
		return scrolledComposite;
	}
	
	private void keygCompositeRefresh(boolean scrollToEnd) {
		scrolledComposite.setRedraw(false);
		keygComposite.layout();
		keygComposite.pack();
		keygComposite.setSize(scrolledComposite.getSize().x-22,keygComposite.getSize().y);
		if (scrollToEnd)
			scrolledComposite.setOrigin(keygComposite.getSize());
		scrolledComposite.setRedraw(true);
	}
	
	public void keyboardRefresh()
	{
		keyboard.redraw();
	}
	
	class AudioExtensionFilter implements FileFilter {
		/* Instances of this class provide a method "accept" that indicates
		 * if the File parameter is a correct audio file by inspecting
		 * its extension. These instances are used by File.listFiles method. */ 
	    public boolean accept(File file) {
	    	String name = file.getName();
	    	if (file.isDirectory())
	    		return true;
	    	for (int i=0; i<EXTENSIONS.length; i++)
	    		if (name.substring(name.length()-EXTENSIONS[i].length()).equalsIgnoreCase(EXTENSIONS[i]))
	    			return true;
	    	return false;
	    }
	}
}
