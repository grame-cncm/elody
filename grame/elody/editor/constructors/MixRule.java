package grame.elody.editor.constructors;

import grame.elody.editor.constructors.operateurs.MixageOp;
import grame.elody.editor.misc.TGlobals;

public class MixRule extends RuleApplet
{
    public MixRule () { super(TGlobals.getTranslation("Mix")); }
    public void init() {
    	super.init();
		add (new RulePanel( new MixageOp (getRuleImage("mix.png")),  
					result = new MixResultHolder()));
		moveFrame (40, 40);
   }
}
