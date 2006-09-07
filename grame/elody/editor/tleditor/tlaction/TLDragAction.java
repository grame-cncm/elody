package grame.elody.editor.tleditor.tlaction;

import grame.elody.editor.tleditor.TLPane;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

//-------------------------------------------------
//action de drag nulle
//-------------------------------------------------
public abstract class TLDragAction {
	TLPane fPane;
	
	public TLDragAction(TLPane pane) { fPane = pane; }
	
	// abstract methods to implement
	public abstract void mouseDragged(MouseEvent m);
	public abstract void mouseReleased(MouseEvent m);
	public abstract void mouseClicked(MouseEvent m);
	public abstract void clearVisualFeedback (Graphics g);
	public abstract void drawVisualFeedback (Graphics g, boolean ctrlPressed);
}
