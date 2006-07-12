package grame.elody.editor.misc.appletframe;

import java.applet.Applet;
import java.awt.event.WindowEvent;

public class PersistentFrame extends AppletFrame {
	public PersistentFrame (String name, Applet a) {
		super(name, a);
	}
    public void windowClosing(WindowEvent event) {
		setVisible(false);
    }
}
