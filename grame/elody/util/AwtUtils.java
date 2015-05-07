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

import java.awt.TextField;

/*******************************************************************************************
*
*	 AwtUtils (classe) : utilitaires
* 
*******************************************************************************************/

public final class AwtUtils {
	 public static String toHexString(int i) {
			return toUnsignedString(i, 4);
			//return toUnsignedString(i, 3);
	     }
	    
	     private static String toUnsignedString(int i, int shift) {
			StringBuffer buf = new StringBuffer(shift >= 3 ? 11 : 32);
			int radix = 1 << shift;
			int mask = radix - 1;
			do {
		    	buf.append(Character.forDigit(i & mask, radix));
		    	i >>>= shift;
			} while (i != 0);
			return buf.reverse().toString();
	    }


	 	public final static	float setRange (TextField field, float min , float max) {
	 			float val = MathUtils.setRange(Float.valueOf(field.getText()).floatValue(), min,max); 
				field.setText(new Float(val).toString());
				return val;
	 		}
	 		
	 	public final static int setRange (TextField field, int min , int max) {
	 		int val =  MathUtils.setRange(Integer.parseInt(field.getText()),min,max);
			field.setText(new Integer(val).toString());
			return val;
	 	}
}
