package grame.elody.editor.expressions;

import grame.elody.lang.texpression.expressions.TExp;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;


public class VarGraphExprHolder extends ExprHolder {
	boolean cursorOn; int oldx, oldy, startPos, curStep;
	
	public VarGraphExprHolder (TExp e, boolean accept) {
		super (e, new TAdjustableVisitor(), accept);
	}
	public int posToDate (int x) {
		return ((TAdjustableVisitor)visitor).posToDate (x);
	}
	public int dateToPos (int date) {
		return ((TAdjustableVisitor)visitor).dateToPos (date) + pos.x;
	}
	public int getPointedStep (int x, int y, int step) {
		if (contains (x, y)) {
			int date = posToDate (x - pos.x) / 1000;
			return ((date % step) == 0) ? (date / step) * step : 0;
		}
		return 0;
	}
	//--------------------------------------------------------------
	public void clearCursor (boolean line) {
		if (cursorOn) {
			Graphics g = getGraphics();
    		g.setXORMode (Color.white);
			drawCursor (g, oldx, oldy, line);
			cursorOn = false;
    		g.setPaintMode ();
    	}
	}
	//--------------------------------------------------------------
	public void drawCursor (Graphics g, int x, int y, boolean line) {
		int h = 6;
		g.fillRect (x-3, y-h, 2, 2*h);
		g.fillRect (x+2, y-h, 2, 2*h);
		if (line) {
			y = pos.y;
			g.drawLine (x, y, x, y + getSize().height);
		}
	}
	//--------------------------------------------------------------
	public void drawCursor (int x, int y, boolean line) {
		Graphics g = getGraphics();
    	g.setXORMode (Color.white);
		if (cursorOn) drawCursor (g, oldx, oldy, line);
		drawCursor (g, x, y, line);
		oldx = x; oldy = y;
		cursorOn = true;
    	g.setPaintMode ();
	}
	//--------------------------------------------------------------
    public void mouseMoved(MouseEvent e) {
    	Point p = e.getPoint();
		int step = getPointedStep (p.x, p.y, 30);
		if (step > 0) {
			drawCursor(dateToPos (curStep = step*1000), p.y, false);
		}
		else {
			clearCursor(false);
			super.mouseMoved (e);
		}
	}
    public void mousePressed (MouseEvent e) {
		if (cursorOn) {
			startPos = e.getX();
		}
		else super.mousePressed (e);
    }
    public void mouseDragged(MouseEvent e) {
    	Point p = e.getPoint();
		if (cursorOn) {
			if (startPos != p.x) {
				drawCursor (p.x, p.y, true);
			}
		}
		else super.mouseDragged (e);
    }
    public void mouseReleased(MouseEvent e) {
		if (cursorOn) {
			clearCursor (true);
			((TAdjustableVisitor)visitor).setFactor (curStep, e.getX() - pos.x);
			refresh = true;
			repaint();
			startPos = 0;
		}
		else super.mouseReleased (e);
    }
}

class TAdjustableVisitor extends TNotesVisitor
{
	public void setFactor (int date, int newPos) {
		nb.factor = (int)(date / Math.tan((newPos*nb.coef) / nb.usedWidth()));
	}
}
