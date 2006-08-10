package grame.elody.editor.constructors;

import grame.elody.editor.constructors.operateurs.BeginOp;
import grame.elody.editor.misc.TGlobals;

public class BeginRule extends RuleApplet
{
    public BeginRule () { super(TGlobals.getTranslation("Begin")); }
    public void init() {
    	super.init();
		add (new RulePanel( new BeginOp (getRuleImage("begin.png")),  
					result = new BeginResultHolder()));
		moveFrame (50, 50);
   }
}
