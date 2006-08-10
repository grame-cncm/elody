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
