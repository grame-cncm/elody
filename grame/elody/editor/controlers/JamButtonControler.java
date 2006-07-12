package grame.elody.editor.controlers;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;

public class JamButtonControler extends ButtonControler
{
	public JamButtonControler (int min, int max, int home, Color inColor, Image img) {
		super (min, max, home, inColor, img);
	}
    public void mouseDragged(MouseEvent e) {
    	if (dragStarted) { 
    		Point p = e.getPoint();
	    	int val = getValue();
	    	int n 	= PointToVal (p.x, p.y);
	    	double arc = ((val - n) * 2 * Math.PI) / (max - min + 1);
	    	if (Math.abs(arc) >= Math.PI/2)
	    		n = arc > 0 ? val +1 : val -1;
			setValue (n);
    	}
    }
    public void mouseReleased(MouseEvent e) {
    	if (dragStarted) {
    		dragStarted = false;
    	}
    }
}
