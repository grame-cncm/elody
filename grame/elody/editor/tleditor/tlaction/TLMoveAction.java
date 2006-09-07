package grame.elody.editor.tleditor.tlaction;

import grame.elody.editor.tleditor.TLActionItem;
import grame.elody.editor.tleditor.TLExportDrag;
import grame.elody.editor.tleditor.TLPane;
import grame.elody.editor.tleditor.TLTrack;
import grame.elody.editor.tleditor.TLZone;
import grame.elody.editor.tleditor.TLActionItem.Action;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.MouseEvent;

//-------------------------------------------------
// action de déplacement d'un objet
//-------------------------------------------------
public class TLMoveAction extends TLDragAction {
	final int vXOffset;

	TLZone fDestZone;

	int fLine, fTime;

	TLExportDrag fExport;

	boolean fExternal;

	public TLMoveAction(TLPane pane, int xOffset) {
		super(pane);
		vXOffset = xOffset;
		fDestZone = new TLZone(fPane.getFSelection());
		fTime = fDestZone.start();
		fLine = fDestZone.voice();
		fExport = new TLExportDrag(fPane, fPane.getFSelection());
		fExternal = false;
		fPane.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}

	public void mouseDragged(MouseEvent m) {
		fExternal = fExport.mouseDragged(m);
		
		if (!fExternal) {
			fLine = fPane.y2line(m.getY());
			if (fLine < 0)
				fLine = 0;
			else if (fLine > 127)
				fLine = 127;
			fTime = fPane.x2AlignedTime(fLine, m.getX() + vXOffset);
			
			fPane.fUpdater.scrollDrop(fTime, fLine, fPane.getFSelection().duration());

			if (!m.isAltDown() && fPane.getFSelection().contains(fTime, fLine)) {
				fDestZone.selectFreePoint(fTime, fLine);
			} else {
				fDestZone.selectDstPoint(fTime, fLine);
			}
		}
	}

	public void clearVisualFeedback(Graphics g) { drawVisualFeedback(g, false); }
	public void drawVisualFeedback(Graphics g, boolean ctrlPressed) {
		// verifie que l'on n'est pas dans un drag externe
		if (!fExternal) {
			// Graphics g = getGraphics();
			g.setXORMode(TLPane.getFArgColorBkg());
			g.setColor(TLPane.getFTraitColor());
			if ((fDestZone.voice() != fPane.getFSelection().voice())
					|| (fDestZone.start() != fPane.getFSelection().start())) {

				// dessin de l'ombre de l'objet draggé
				int rx = fPane.time2x(fTime);
				int rx2 = fPane.time2x(fTime + fPane.getFSelection().duration());
				int ry = fPane.line2y(fLine);
				g.drawRect(rx, ry + 2, rx2 - rx - 1, TLPane.getFLineHeight() - 5);

				if (fDestZone.empty()) {
					// c'est une insertion
					int tx = fPane.time2x(fDestZone.start());
					if (rx == tx) {
						g.drawLine(rx, 0, rx, ry);
						g.drawLine(rx, ry + TLPane.getFLineHeight(), rx, fPane
								.getSize().height);
						g.drawLine(rx2, 0, rx2, fPane.getSize().height);

					} else {
						g.drawLine(tx, 0, tx, fPane.getSize().height);
					}
					g.drawString("t=" + fDestZone.start(), tx + 2, ry - 10);

				} else {
					// c'est une application
					final int k = 10;
					int tx = fPane
							.time2x((fDestZone.start() + fDestZone.end()) / 2);
					g.drawLine(tx, 0, tx, ry - k);
					g.drawString("APPLY", tx + 2, ry - k - 1);
					if (fDestZone.inGroup()) {
						g.drawString("WARNING", tx + 2, ry - 20 - 1);
					}

					Polygon plg = new Polygon();
					plg.addPoint(tx - k, ry - k + 1);
					plg.addPoint(tx + k, ry - k + 1);
					plg.addPoint(tx, ry + 1);
					g.drawPolygon(plg);

					g.drawLine(tx, ry + TLPane.getFLineHeight(), tx,
							fPane.getSize().height);
				}
			}
		}
	}

	public void mouseReleased(MouseEvent m) {
		fExternal = fExport.mouseReleased(m);
		if (!fExternal) {
			if ((fDestZone.voice() != fPane.getFSelection().voice())
					|| (fDestZone.start() != fPane.getFSelection().start())) { // si
				// mouvement
				
				if (!fDestZone.empty()) { // application
					fPane.toUndoStack(Action.TRACK);
					fDestZone.cmdApply(fPane.getFSelection().getFunction());
					fPane.getFSelection().set(fDestZone);

				} else if (m.isAltDown()) { // duplication
					/* fDestZone.cmdDuplicate(fPane.fSelection); */
					fPane.toUndoStack(Action.TRACK);
					fPane.getFSelection().cmdDuplicateTo(fDestZone);

				} else { // déplacement
					/* fDestZone.cmdMove(fPane.fSelection); */
					fPane.toUndoStack(Action.MOVE, fPane.getFSelection().topline(), fDestZone.topline());				
					fPane.getFSelection().cmdMoveTo(fDestZone);
				}

				fPane.multiTracksChanged();
			}
		}
	}

	public void mouseClicked(MouseEvent m) {
		// System.out.println( "TLMoveAction mouseClicked " + m );
		if (m.getClickCount() == 2) {
			if (m.isAltDown()) {
				fPane.getFSelection().cmdUnevaluate();
			} else {
				fPane.getFSelection().cmdEvaluate();
			}
			fPane.multiTracksChanged();
		}
	}
}
