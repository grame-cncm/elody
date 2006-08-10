package grame.elody.editor.constructors;

import grame.elody.editor.constructors.operateurs.ApplicationOp;
import grame.elody.editor.misc.TGlobals;

public class ApplyRule extends RuleApplet
{
    public ApplyRule () { super(TGlobals.getTranslation("Application")); }
    public void init() {
    	super.init();
 		add (new RulePanel( new ApplicationOp (getRuleImage("appl.png")),  
					result = new ApplResultHolder()));
		moveFrame (20, 20);
   }
}