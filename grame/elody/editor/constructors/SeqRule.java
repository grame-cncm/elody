/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.editor.constructors;

import grame.elody.editor.constructors.operateurs.SequenceOp;
import grame.elody.editor.misc.TGlobals;

public class SeqRule extends RuleApplet {
    public SeqRule () { super(TGlobals.getTranslation("Sequence")); }
    public void init() {
    	super.init();
		add (new RulePanel( new SequenceOp (getRuleImage("seq.png")),  
					result = new SeqResultHolder()));
		moveFrame (30, 30);
   }
}
