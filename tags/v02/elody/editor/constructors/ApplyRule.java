package grame.elody.editor.constructors;

import grame.elody.editor.constructors.operateurs.ApplicationOp;

public class ApplyRule extends RuleApplet
{
    public ApplyRule () { super("Application"); }
    public void init() {
    	super.init();
 		add (new RulePanel( new ApplicationOp (getRuleImage("appl.jpg")),  
					result = new ApplResultHolder()));
		moveFrame (20, 20);
   }
}