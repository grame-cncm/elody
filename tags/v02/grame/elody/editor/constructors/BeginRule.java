package grame.elody.editor.constructors;

import grame.elody.editor.constructors.operateurs.BeginOp;

public class BeginRule extends RuleApplet
{
    public BeginRule () { super("Begin"); }
    public void init() {
    	super.init();
		add (new RulePanel( new BeginOp (getRuleImage("begin.jpg")),  
					result = new BeginResultHolder()));
		moveFrame (50, 50);
   }
}
