package grame.elody.editor.constructors;

import grame.elody.editor.constructors.operateurs.StretchOp;

public class StretchRule extends RuleApplet
{
    public StretchRule () { super("Stretch"); }
    public void init() {
    	super.init();
		add (new RulePanel( new StretchOp (getRuleImage("stretch.jpg")),  
					result = new StretchResultHolder()));
		moveFrame (70, 70);
   }
}
