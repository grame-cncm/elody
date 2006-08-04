package grame.elody.editor.constructors;

import grame.elody.editor.constructors.operateurs.YAbstractionOp;
import grame.elody.editor.misc.TGlobals;

public class YAbstrRule extends RuleApplet
{
    public YAbstrRule () { super(TGlobals.getTranslation("Y-Abstraction")); }
    public void init() {
    	super.init();
		add (new RulePanel( new YAbstractionOp (getRuleImage("yabstr.jpg")), 
					result = new YAbstrResultHolder()));
		moveFrame (80, 80);
   }
}
