package grame.elody.editor.constructors;

import grame.elody.editor.constructors.operateurs.StretchOp;
import grame.elody.editor.misc.TGlobals;

public class StretchRule extends RuleApplet
{
    public StretchRule () { super(TGlobals.getTranslation("Stretch")); }
    public void init() {
    	super.init();
		add (new RulePanel( new StretchOp (getRuleImage("stretch.jpg")),  
					result = new StretchResultHolder()));
		moveFrame (70, 70);
   }
}
