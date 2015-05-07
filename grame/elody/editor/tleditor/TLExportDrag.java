/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.editor.tleditor;

import grame.elody.editor.misc.draganddrop.DragAble;
import grame.elody.editor.misc.draganddrop.DragAndDrop;
import grame.elody.editor.misc.draganddrop.DragManager;
import grame.elody.editor.misc.draganddrop.TExpContent;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;

//-------------------------------------------------
//action de déplacement d'un objet à l'extérieur
//-------------------------------------------------
public class TLExportDrag implements DragAble {
	final TExpContent		fContent;
	final Component			fSource;
	//DropAble				fTarget;
 
	// interface d'un DragAble 
    public void 	dragStart() { }
    public void 	dragStop() { }
	public Object 	getObject () { return fContent; }
	
	public TLExportDrag(Component src, TExpContent content)
	{
		fSource = src;
		fContent = content;
		/*
		fTarget = null;
		*/
		DragManager.startDrag (this, src);
	} 
			
	public boolean mouseDragged(MouseEvent m) 
	{
		Point pt = m.getPoint();
		if (! fSource.contains (pt)) {
			/*
			DropAble oldZone = fTarget;
			Point gp = DragAndDrop.local2Global(fSource, pt);
			fTarget = DragAndDrop.global2DropAble(gp);
			if (fTarget == oldZone) {
				if ( (fTarget != null) && (fTarget instanceof ExtendedDropAble) ) {
					Point dropPt = DragAndDrop.global2Local((Component)fTarget, gp);
					((ExtendedDropAble)fTarget).feedback(dropPt.x, dropPt.y);
				}
			} else {
				if (oldZone != null) oldZone.dropLeave();
				if (fTarget != null) {
					if (fTarget.accept(fContent)) {
						fTarget.dropEnter();
						fTarget.accept(fContent);
					} else {
						fTarget = null;
					}
				}
			}
			*/
			DragManager.mouseDragged(DragAndDrop.local2Global(fSource, pt));
			return true;
			
		} else {
			/*
			if (fTarget != null) {
				fTarget.dropLeave();
				fTarget = null;
			}
			*/
			return false;
		}
	}
	
	public boolean mouseReleased(MouseEvent m)
	{ 
		Point pt = m.getPoint();
		if (! fSource.contains (pt)) {
			DragManager.stopDrag(DragAndDrop.local2Global(fSource, pt));
			/*
			if (fTarget != null) {
				// on était dans un DD externe
				Point dropPt = DragAndDrop.global2Local((Component)fTarget, DragAndDrop.local2Global(fSource, m.getPoint()));
				fTarget.dropLeave();
				fTarget.drop(fContent, dropPt);
				fTarget = null;						
			}
			*/
			return true;
		} else {
			/*
			if (fTarget != null) {
				fTarget.dropLeave();
				fTarget = null;
			}
			*/
			DragManager.cancelDrag();
			return false;
		}
	}
}
