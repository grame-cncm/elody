package grame.elody.editor.constructors;

import grame.elody.editor.misc.TGlobals;
import grame.elody.editor.misc.applets.BasicApplet;

import java.awt.Canvas;
import java.awt.Panel;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;

public class BasicShellSWT extends BasicApplet {

	protected Shell shell;
	protected Panel panel;
	protected Canvas canvas;
	
	protected BasicShellSWT() {	}
	
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
		frame.setVisible(true);
		panel = new Panel();
		frame.add(panel);
		canvas = new Canvas();
		panel.add(canvas);
		
		frame.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent arg0) {
					updateCanvas();
			}
		});
		shell = SWT_AWT.new_Shell(TGlobals.display, canvas);
		updateCanvas();
	}
	
	public void setLayout(Layout layout)
	{
		shell.setLayout(layout);
	}

	/*
	 * public AppletContext getAppletContext()
	 * public void setResizable (boolean resize)
	 * public void setTitle (String title)
	 * public void moveFrame (int x, int y)
	 * public void toFront ()
	 * 
	 */
}
