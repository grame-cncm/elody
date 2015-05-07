/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.util;

import java.util.EmptyStackException;
import java.util.Vector;

/**
 * A Last-In-First-Out(LIFO) stack of objects.
 *
 * @version 	1.12, 11 Aug 1995
 * @author 	Jonathan Payne
 */

public class Fifo extends Vector<Object> {
    /**
     * Pushes an item onto the stack.
     * @param item the item to be pushed on.
     */
    public Object push(Object item) {
		addElement(item);
		return item;
    }

    /**
     * Pops an item off the stack.
     * @exception EmptyStackException If the stack is empty.
     */
    public Object pop() {
		Object	obj = peek();
		removeElementAt(0);
		return obj;
    }

    /**
     * Peeks at the top of the stack.
     * @exception EmptyStackException If the stack is empty.
     */
    public Object peek() {
		int	len = size();

		if (len == 0) throw new EmptyStackException();
		return elementAt(0);
    }

    /**
     * Returns true if the stack is empty.
     */
    public boolean empty() {
		return size() == 0;
    }

    /**
     * Sees if an object is on the stack.
     * @param o the desired object
     * @return the distance from the top, or -1 if it is not found.
     */
    public int search(Object o) {
		int i = lastIndexOf(o);

		if (i >= 0) {
	    	return size() - i;
		}
		return -1;
    }
}
