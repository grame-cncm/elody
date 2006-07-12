package grame.elody.editor.constructors;

import grame.elody.editor.misc.applets.BasicApplet;

import java.awt.Canvas;
import java.awt.Panel;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;

import com.swtdesigner.SWTResourceManager;

public abstract class BasicShellSWT extends BasicApplet implements Runnable {
/***** DESCRIPTION ************************************
 * Instances of this class represent the "windows" applet-like components.
 * Each one provides a bridge between SWT and AWT, so that it is possible
 * to embedded SWT components in an AWT frame.
 * SWT needs a "Display" in order to manage the connection between SWT
 * and the underlying OS. This class provides the thread in which the
 * "Display" instance must be created, and distinguished as the
 * user-interface thread for that display (some SWT methods may only
 * be called from this thread). Note that this thread is not allowed to
 * construct other "Displays" until that main "Display" has been disposed.  
 ******************************************************/	
	protected Shell shell;
	protected Panel panel;
	protected Canvas canvas;
	protected Display display;
	protected Thread thread;
	
	//protected BasicShellSWT() { shell=null;	}
	
	public abstract void buildInterface();
	public abstract void setBgColor();
	
	public void updateCanvas()
	{
		canvas.setLocation(0, 0);
		canvas.setSize((int) frame.getSize().getWidth()
				- frame.getInsets().left - frame.getInsets().right, (int) frame
				.getSize().getHeight()
				- frame.getInsets().top - frame.getInsets().bottom);
	}
	
	public BasicShellSWT(String title)
	{
		super(title);
		thread = new Thread(this);
		panel = new Panel();
		canvas = new Canvas();
		frame.setVisible(true);
		panel.add(canvas);
		frame.add(panel);
			
		frame.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent arg0) {
					updateCanvas();
			}
		});
		thread.setName("SWT Thread");
		thread.start();
	
	}
	
	public void setSizeSWT(int w, int h)
	{
		setSize(0,0);
		shell.pack();
		int width = shell.getSize().x;
		int height = shell.getSize().y+40;
		if (w==-1)	w=width;
		if (h==-1)	h=height;
		this.getFrame().setSize(w, h);
	}
	
	public void setLayout(Layout layout)
	{
		shell.setLayout(layout);
	}

	public void run() {
		display = new Display();
		try {
			shell = SWT_AWT.new_Shell(display, canvas);
		} catch (IllegalArgumentException e) {
			System.out.println(e);
			e.printStackTrace();
		}
		setBgColor();
		buildInterface();
		frame.validate();
		updateCanvas();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		shell.dispose();
		SWTResourceManager.dispose();
	}

	/* METHODS TO OVERRIDE :
	 * 
	 * public AppletContext getAppletContext()
	 * public void setResizable (boolean resize)
	 * public void setTitle (String title)
	 * public void moveFrame (int x, int y)
	 * public void toFront ()
	 * 
	 */
}
