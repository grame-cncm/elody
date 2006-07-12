package grame.elody.editor.tleditor.tlaction;

import grame.elody.editor.tleditor.TLPane;
import grame.elody.editor.tleditor.TLZone;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

//-------------------------------------------------
// action d'extension de la selection
//-------------------------------------------------
public class TLExtendSelectionAction extends TLDragAction
{
	
	public TLExtendSelectionAction(TLPane pane){ super(pane); }
	public void mouseDragged(MouseEvent m) {}
	public void mouseReleased(MouseEvent m) {}
	public void drawVisualFeedback(Graphics g){}
	public void mouseClicked(MouseEvent m)
	{ 
		TLZone dest = new TLZone( fPane.getFMultiTracks(), fPane.x2time(m.getX()), fPane.y2line(m.getY()) );
		fPane.getFSelection().extendTo(dest);
		fPane.selectionChanged();
	}
} 
