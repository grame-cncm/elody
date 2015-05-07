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

import java.util.Stack;

/*******************************************************************************************
*
*	 TMemReuser (classe) : reutilisateur de mémoire
* 
*******************************************************************************************/

public final class TMemReuser {
	Stack<Object> stack = new Stack<Object>();
 	Class  object;
 
  	public TMemReuser (String name) { 
 		try {
 			object = Class.forName (name);
 			//CAUTION: take care of packages names!!
 		}catch (Exception e) {
 			System.out.println(e);
 		}
 	}

	public final Object allocate() {
		
		if (stack.empty()) {
			Object obj = null;
			try {
				obj =  object.newInstance();
			}catch (Exception e) {
				System.out.println(e);
			}
			return obj;
		}else {
			return stack.pop();
		}
	}
	
	public final void destroy (Object obj) {
		stack.push(obj);
	}
}
