package grame.elody.editor.tleditor.tlaction;

import grame.elody.editor.tleditor.TLPane;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

//-------------------------------------------------
// action de resize quand on drag la fin d'un objet
//-------------------------------------------------

public class TLResizeAction extends TLDragAction {
	int fNewEndTime;	// nouvelle fin de la selection
	int	fLine;			// ligne (pour pouvoir annuler)
	
	public TLResizeAction(TLPane pane)
	{
		super(pane);
		fPane.getFSelection().normalizeZone();
		fNewEndTime = fPane.getFSelection().end();
		fLine 		= fPane.getFSelection().voice();
	}
	public void mouseDragged(MouseEvent m) 
	{ 
		int etime = fPane.x2AlignedTime(fPane.getFSelection().voice(), m.getX());
		fNewEndTime = (etime - fPane.getFSelection().start() > 0) ? etime : fPane.getFSelection().start() + 1;
		fLine = fPane.y2line(m.getY());
	}
	public void mouseReleased(MouseEvent m)
	{ 
		if ( (fNewEndTime != fPane.getFSelection().end()) && (fLine == fPane.getFSelection().voice()) ) {
			fPane.getFSelection().cmdResize(fNewEndTime);
			fPane.multiTracksChanged();
		}
	}
	public void drawVisualFeedback(Graphics g)
	{
		if ( (fNewEndTime != fPane.getFSelection().end()) && (fLine == fPane.getFSelection().voice()) ) {

			int rx = fPane.time2x(fPane.getFSelection().start());
			int tx = fPane.time2x(fNewEndTime);
			int ry = fPane.line2y(fLine), h = fPane.getSize().height;
			//Graphics g = getGraphics();
			g.setXORMode(fPane.getFArgColorBkg()); g.setColor(fPane.getFTraitColor()); 				
			g.drawRect(rx, ry+2, tx - rx -1, fPane.getFLineHeight()-5);

			g.drawLine(tx, 0, tx, h);
			int delta = fNewEndTime - fPane.getFSelection().start();
			g.drawString("<>"+ delta, tx+2, ry - 10);
			g.setPaintMode();
		}
	}
	public void mouseClicked(MouseEvent m) {}
}
