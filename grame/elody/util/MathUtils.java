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

/*******************************************************************************************
*
*	 MathUtils (classe) : utilitaires 
* 
*******************************************************************************************/

public final class MathUtils {
	public final static	float setRange(float val,  float min , float max){
		return val < min ? min : val > max ? max : val;
	}
	
	public final static	int setRange(int val,  int min , int max){
		return val < min ? min :   val > max ? max : val;
	}
}
