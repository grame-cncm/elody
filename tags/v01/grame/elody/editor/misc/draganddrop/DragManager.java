package grame.elody.editor.misc.draganddrop;

import java.awt.Component;
import java.awt.Point;


public class DragManager {
	static protected DragAndDrop drag = null;
    
    static final public void startDrag (DragAble o, Component c) {
   		if (drag != null)
	  		drag.stopDrag (0, 0);
    	if (o != null) {
    		drag = new DragAndDrop (c, o);
    	}
	}

    static final public void mouseDragged (Point globalPoint) {
    	if (drag != null) {
    		drag.doDrag (globalPoint);
    	}
	}

    static final public void stopDrag (Point globalPoint) {
   		if (drag != null) {
	  		drag.stopDrag (globalPoint);
    		drag = null;
    	}
	}	

    static final public void cancelDrag () 				{ drag = null; }
}
