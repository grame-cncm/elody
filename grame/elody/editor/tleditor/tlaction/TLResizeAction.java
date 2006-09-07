package grame.elody.editor.tleditor.tlaction;

import grame.elody.editor.tleditor.TLPane;
import grame.elody.editor.tleditor.TLActionItem.Action;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

//-------------------------------------------------
// action de resize quand on drag la fin d'un objet
//-------------------------------------------------

public class TLResizeAction extends TLDragAction {
	int fNewEndTime;	// nouvelle fin de la selection
	int	fLine;			// ligne (pour pouvoir annuler)
	boolean fPrevCtrlState;
	
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
			fPane.getFMultiTracks().at(fLine);
			fPane.toUndoStack(Action.TRACK);
			if (m.isControlDown()||m.isMetaDown())
				fPane.getFSelection().cmdQuickDuplicate(fNewEndTime);
			else
				fPane.getFSelection().cmdResize(fNewEndTime);
			fPane.multiTracksChanged();			
		}
	}
	
	public void clearVisualFeedback(Graphics g) { drawVisualFeedback(g, fPrevCtrlState); }
	
	public void drawVisualFeedback(Graphics g, boolean ctrlPressed)
	{
		if ( (fNewEndTime != fPane.getFSelection().end()) && (fLine == fPane.getFSelection().voice()) ) {

			fPrevCtrlState = ctrlPressed;
			fPane.fUpdater.scrollDrop(fNewEndTime, fLine, 0);
			
			int rx = fPane.time2x(fPane.getFSelection().start());
			int tx = fPane.time2x(fNewEndTime);
			int ry = fPane.line2y(fLine), h = fPane.getSize().height;
			//Graphics g = getGraphics();
			g.setXORMode(TLPane.getFArgColorBkg()); g.setColor(TLPane.getFTraitColor()); 				
			g.drawRect(rx, ry+2, tx - rx -1, TLPane.getFLineHeight()-5);

			g.drawLine(tx, 0, tx, h);
			int delta = fNewEndTime - fPane.getFSelection().start();
			if (ctrlPressed)
				g.setColor(Color.red);
			else
				g.setColor(Color.black);
			g.drawString("<>"+ delta, tx+2, ry - 10);
			g.setPaintMode();
		}
	}
	public void mouseClicked(MouseEvent m) {}
}
