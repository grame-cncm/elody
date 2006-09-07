package grame.elody.editor.tleditor.tlaction;

import grame.elody.editor.tleditor.TLPane;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

//-------------------------------------------------
// action de deselection
//-------------------------------------------------

public class TLUnselectAction extends TLDragAction {
	public TLUnselectAction(TLPane pane){ super(pane);}
	public void mouseDragged(MouseEvent m) {}
	public void mouseReleased(MouseEvent m)
	{ 
		fPane.getFSelection().moveTo( fPane.x2time(m.getX()), fPane.y2line(m.getY()) );
		fPane.selectionChanged();
	}
	public void clearVisualFeedback(Graphics g) {}
	public void drawVisualFeedback(Graphics g, boolean ctrlPressed){}
	public void mouseClicked(MouseEvent m)
	{ 
		fPane.getFSelection().moveTo( fPane.x2time(m.getX()), fPane.y2line(m.getY()) );
		fPane.selectionChanged();
	}
}
