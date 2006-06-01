package grame.elody.editor.constructors;

import grame.elody.editor.constructors.operateurs.SequenceOp;

public class SeqRule extends RuleApplet {
    public SeqRule () { super("Sequence"); }
    public void init() {
    	super.init();
		add (new RulePanel( new SequenceOp (getRuleImage("seq.jpg")),  
					result = new SeqResultHolder()));
		moveFrame (30, 30);
   }
}
