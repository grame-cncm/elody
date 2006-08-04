package grame.elody.editor.misc.applets;

import java.awt.Frame;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.widgets.Shell;

public class Window {

	protected boolean isFrame;
	protected Frame frame;
	protected Shell shell;
	
	public Window(Frame f)
	{
		frame = f;
		frame.addComponentListener(new ComponentAdapter() {
			public void componentMoved(ComponentEvent e) { BasicApplet.updateScreen(); }
			public void componentResized(ComponentEvent e) { BasicApplet.updateScreen(); }
		});
		isFrame = true;
	}
	public Window(Shell s)
	{
		shell = s;
		shell.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
			public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
				System.out.println("keypressed "+e.getSource().getClass().getName());
				if (e.keyCode == SWT.F11)
				{
					System.out.println("SWT F11");
					BasicApplet.screen.compute1();
				}
				else if (e.keyCode == SWT.F12)
				{
					System.out.println("SWT F12");
					BasicApplet.screen.compute2();
				}
			}
		});
		s.addControlListener(new ControlListener() {

			public void controlMoved(ControlEvent e) {
				BasicApplet.updateScreen();				
			}

			public void controlResized(ControlEvent e) {
				BasicApplet.updateScreen();
			}

			
		});
		isFrame = false;
	}

	public boolean isFrame() { return isFrame ; }
	public boolean isShell() { return !isFrame ; }
	
	public void setBounds(java.awt.Rectangle rect)
	{
		setBounds(rect.x, rect.y, rect.width, rect.height);			
	}
	
	public void setBounds(org.eclipse.swt.graphics.Rectangle rect)
	{
		setBounds(rect.x, rect.y, rect.width, rect.height);		
	}
	
	public void setBounds(int x, int y, int width, int height)
	{
		if (isFrame)
			frame.setBounds(x, y, width, height);
		else
			shell.setBounds(x, y, width, height);
	}
	
	public java.awt.Rectangle getBounds()
	{
		if (isFrame)
			return frame.getBounds();
		else
		{
			org.eclipse.swt.graphics.Rectangle rect = shell.getBounds();
			return new java.awt.Rectangle(rect.x, rect.y, rect.width, rect.height);
		}
	}
	
	public void requestFocus()
	{
		if (isFrame)
			frame.requestFocus();
		else
			shell.forceFocus();
	}
	
	public int getWidth()
	{
		if (isFrame)
			return frame.getWidth();
		else
			return shell.getSize().x;
	}
	
	public int getHeight()
	{
		if (isFrame)
			return frame.getHeight();
		else
			return shell.getSize().y;
	}
	
}
