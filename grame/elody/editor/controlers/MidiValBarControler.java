/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.editor.controlers;

import grame.elody.util.MathUtils;

import java.awt.Color;


public class MidiValBarControler extends BarControler {
	double coef, mult;
	
	public MidiValBarControler (int slope, int dir, Color inColor) {
		super(-127,127, dir, inColor);
		double c[] = { 1.2667, 1.194, 1.153, 1.1269, 1.1087, 1.0953 };
		slope = MathUtils.setRange (slope, 1, 6) - 1;
		coef = (127 * 2 * c[slope]) / Math.PI;
		mult = (double)(slope + 2) / 127;
	}
    public int PointToVal(int x, int y) {
    	if (getDirection()==kVertical) {
    		if (y <= 1) return max;
    		if (y >= getSize().height) return min;
    	}
    	else if (getDirection()==kHorizontal) {
    		if (x <= 1) return min;
    		if (x >= getSize().width) return max;
    	}
		int v = super.PointToVal (x, y);
		return  (int)(Math.tan (v / coef) / mult);
    }
    protected int ValToOffset(int val) {
    	if ((val < max) && (val > min)) 
    		val = (int)(Math.atan (val * mult) * coef);
		return super.ValToOffset (val);
    }
}
