package grame.elody.editor.tleditor.tlaction;

import grame.elody.editor.tleditor.TLExportDrag;
import grame.elody.editor.tleditor.TLPane;
import grame.elody.editor.tleditor.TLTrack;
import grame.elody.editor.tleditor.TLZone;
import grame.elody.editor.tleditor.TLActionItem.Action;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

//-------------------------------------------------
// action de déplacement d'une piste
//-------------------------------------------------

public class TLTrackMoveAction extends TLDragAction {
	final int srcLine, srcY;		// info statiques
	
	int dstLine, dstY;				// ligne et y courants
	boolean copymode;
	TLZone 	selection;
	
	TLExportDrag	fExport;
	boolean			fExternal;
	
	public TLTrackMoveAction(TLPane pane, TLZone sel, int y)
	{
		super(pane);
		srcY = y;
		srcLine = fPane.y2line(y);
		fPane.getFSelection().selectLine(srcLine);
		fPane.selectionChanged();
		
		dstY = y;
		dstLine = srcLine;
		copymode = false;
		selection = sel;
		
		fExport 	= new TLExportDrag(fPane, fPane.getFSelection());
		fExternal 	= false;
		fPane.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}
	public void mouseDragged(MouseEvent m) 
	{
		fExternal = fExport.mouseDragged(m);
		if (! fExternal ) {
			copymode = m.isAltDown();
			dstY = m.getY();
			dstLine = fPane.y2line(dstY + fPane.getFLineHeight()/2);
			if (dstLine < 0) dstLine = 0; else if (dstLine > 127) dstLine = 127;
		}
	}
	public void mouseReleased(MouseEvent m) 
	{
		fExternal = fExport.mouseReleased(m);
		if (! fExternal) {
			if ( (dstY != srcY) && (copymode || (dstLine != srcLine) && (dstLine != srcLine+1)) ) {
				if (fPane.getFMultiTracks().at(srcLine)) {
					fPane.toUndoStack(Action.MULTITRACKS);
					if (copymode) {
						TLTrack src = fPane.getFMultiTracks().getTrack();
						TLTrack cpy = src.makeCopy();
						cpy.setTrackMode(src.getTrackMode());		// à cause bug dans makeCopy
						fPane.getFMultiTracks().at(dstLine);
						fPane.getFMultiTracks().insert(cpy);
						fPane.getFSelection().selectLine(dstLine);
						fPane.selectionChanged();
					} else {
						TLTrack trk = fPane.getFMultiTracks().getTrack();
						fPane.getFMultiTracks().remove();
						fPane.getFMultiTracks().at((dstLine < srcLine) ? dstLine : dstLine-1);
						fPane.getFMultiTracks().insert(trk);
						fPane.getFSelection().selectLine(fPane.getFMultiTracks().getPos());
						fPane.selectionChanged();
					}
					fPane.multiTracksChanged();
				}
			}
		}
	}
	public void drawVisualFeedback(Graphics g) 
	{
		if (! fExternal ) {
			Dimension dim = fPane.getSize();
			//Graphics g = getGraphics();
			g.setXORMode(fPane.getFArgColorBkg()); g.setColor(fPane.getFTraitColor()); 				
			// ombre de la piste source
			int x0 = 0, y0 = fPane.line2y(srcLine);
			g.drawRect(x0, y0+1, dim.width, fPane.getFLineHeight()-2);
			
			if ( (dstY != srcY) && (copymode || (dstLine != srcLine) && (dstLine != srcLine+1)) ) {
				// hilite de la destination
				int y1 = fPane.line2y(dstLine);
				y0 = y1 - fPane.getFLineHeight()/2;
				g.drawRect(x0, y0+1, dim.width, fPane.getFLineHeight()-2);
				g.drawRect(x0+1, y0+2, dim.width-2, fPane.getFLineHeight()-4);
				g.drawLine(0, y1-1, dim.width, y1-1);
				g.drawLine(0, y1, dim.width, y1);
			}
			
			g.setPaintMode();
		}
		
	}
	public void mouseClicked(MouseEvent m) 
	{
	}
}
