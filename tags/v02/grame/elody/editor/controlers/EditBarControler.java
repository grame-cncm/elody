package grame.elody.editor.controlers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

public class EditBarControler extends EditControler
{
	boolean showMiddle; Color middleColor;
	
	public EditBarControler (Controler ctrl, int cols) {
		super (ctrl, cols);
		showMiddle = true;
		middleColor = Color.red;
	}
    public void paint (Graphics g) {
    	if (showMiddle) {
    		g.setColor (middleColor);
	 		Dimension d = ctrl.getSize();
	 		Point p = ctrl.getLocation();
			if (ctrl.getDirection() == BarControler.kHorizontal) {
				int x = p.x + d.width/2 - 1;
				g.drawLine (x, p.y-2, x, p.y + d.height + 2);
			}
			else {
				int y = p.y + d.height/2;
				g.drawLine (p.x-2, y, p.x + d.width + 2, y);
			}
	   	}
    }
}

