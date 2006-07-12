package grame.elody.editor.constructors;

import grame.elody.editor.constructors.operateurs.RestOp;

public class EndRule extends RuleApplet
{
    public EndRule () { super("End"); }
    public void init() {
    	super.init();
		add (new RulePanel( new RestOp (getRuleImage("rest.jpg")),  
					result = new RestResultHolder()));
		moveFrame (60, 60);
   }
}
