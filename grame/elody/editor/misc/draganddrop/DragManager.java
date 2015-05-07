/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

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
