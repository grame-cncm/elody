package grame.elody.editor.constructors;

import grame.elody.editor.constructors.operateurs.MixageOp;

public class MixRule extends RuleApplet
{
    public MixRule () { super("Mix"); }
    public void init() {
    	super.init();
		add (new RulePanel( new MixageOp (getRuleImage("mix.jpg")),  
					result = new MixResultHolder()));
		moveFrame (40, 40);
   }
}
