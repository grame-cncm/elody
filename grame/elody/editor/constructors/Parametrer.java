package grame.elody.editor.constructors;

import grame.elody.editor.constructors.parametrer.ParamExprHolder;
import grame.elody.editor.constructors.parametrer.ParamPanel;
import grame.elody.editor.misc.Define;
import grame.elody.editor.misc.TGlobals;
import grame.elody.editor.misc.applets.BasicApplet;
import grame.elody.lang.texpression.expressions.TEvent;
import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.util.MsgNotifier;

import java.awt.BorderLayout;
import java.util.Observable;
import java.util.Observer;

public class Parametrer extends BasicApplet implements Observer {
	static final String appletName = "Parametrer";
	ParamPanel param;
	ParamExprHolder exprHolder;
	
	public Parametrer() {
		super(TGlobals.getTranslation("Parametrer"));
		setLayout(new BorderLayout());
		setSize (180, 260);
	} 
    public void init() {
		Define.getButtons(this);
    	param = new ParamPanel(true);
    	exprHolder = new ParamExprHolder();
    	exprHolder.addObserver(this);
    	param.init (exprHolder, 120, 60);
		add("Center", param);
		moveFrame (200, 240);
	}   
	public void decompose (TExp exp) {
		param.decompose (exp);
	}
  	public void update (Observable o, Object arg) {
  		MsgNotifier msg = (MsgNotifier)o;
  		if (msg.message() == Define.ExprHolderMsg) {
  			if (exprHolder.isRecentlyDropped())
  			{
  				exprHolder.setRecentlyDropped(false);
  				if (exprHolder.getExpression() instanceof TEvent)
  				{
  					TEvent event = (TEvent) exprHolder.getExpression();
  					param.initControls((int)event.getPitch(), (int)event.getVel(),
  							event.getDur(), (int) event.getChan());
  				}
  				else
  				{
  					param.initControls(-1, -1, -1, -1);
  				}
  			}
  		}
  	}
}
