package grame.elody.editor.tleditor.tlaction;

import grame.elody.editor.tleditor.TLPane;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

//-------------------------------------------------
//action de drag nulle
//-------------------------------------------------
public class TLNullAction extends TLDragAction {
	public TLNullAction(TLPane pane) { super(pane); }
	
	public void mouseDragged(MouseEvent m) {}
	public void mouseReleased(MouseEvent m) {}
	public void mouseClicked(MouseEvent m) {}
	public void clearVisualFeedback(Graphics g) {}
	public void drawVisualFeedback(Graphics g, boolean ctrlPressed) {}
}
