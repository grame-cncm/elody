package grame.elody.editor.tleditor.tlaction;

import grame.elody.editor.tleditor.TLPane;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

//-------------------------------------------------
//action de zoom dans la regle du temps
//-------------------------------------------------
public class TLZoomAction extends TLDragAction
{
	int 	x0;
	int 	y0, yBase;
	double 	z0;
	boolean zmode;
	
	public TLZoomAction(TLPane pane, int x, int y)
	{
		super(pane);
		x0 = x; 
		y0 = y;
		yBase = y;
		z0 = fPane.getFZoom();
		zmode = false;
	}

	public void mouseDragged(MouseEvent m) 
	{
		int x1 = m.getX();
		int y1 = m.getY();
		int dx = Math.abs(x0-x1);
		int dy = Math.abs(y0-y1);
		
		if (dx >= dy) {
			// deplacement
			fPane.setScrollPos(fPane.xt2timePos(x1, fPane.x2time(x0)), fPane.getFLinePos());
		} else {
			// zoom
			int deltay = (yBase - y1) / 4;
			if (deltay > 0) {
				fPane.setZoomAt(z0/(1.0 + deltay*0.25), x1);
			} else if (deltay < 0){
				fPane.setZoomAt(z0*(1.0 + deltay*-0.25), x1);
			}
		}
		
		// update position courante
		x0 = x1;
		y0 = y1;
		
	}

	public void mouseReleased(MouseEvent m) { fPane.setFOldZoom(z0); }
	public void mouseClicked(MouseEvent m) {}
	public void clearVisualFeedback(Graphics g) {}
	public void drawVisualFeedback(Graphics g, boolean ctrlPressed) {}
}

