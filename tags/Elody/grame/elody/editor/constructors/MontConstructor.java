package grame.elody.editor.constructors;

import grame.elody.editor.constructors.parametrer.ParamExprHolder;
import grame.elody.editor.constructors.parametrer.ParamFrame;
import grame.elody.editor.constructors.parametrer.ParamPanel;
import grame.elody.editor.controlers.EditControler;
import grame.elody.editor.controlers.JamButtonControler;
import grame.elody.editor.expressions.ExprHolder;
import grame.elody.editor.expressions.FunctionTag;
import grame.elody.editor.expressions.ParamDecomposer;
import grame.elody.editor.expressions.VarArgsFunctionDecomposer;
import grame.elody.editor.misc.Define;
import grame.elody.editor.misc.applets.BasicApplet;
import grame.elody.lang.TExpMaker;
import grame.elody.lang.texpression.expressions.TEvent;
import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.util.MsgNotifier;
import grame.elody.util.fileselector.ColoredLabel;

import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

public class MontConstructor extends BasicApplet implements Observer,
		ActionListener {
	static final String appletName = "MontConstructor";
	static final int stepsCount = 8;
	static public String clearCommand = "Clear";
	MainExprHolder meh;
	MontStepPanel ppanel[];

	public MontConstructor() {
		super("Montage constructor");
	}
    public void init() {
    	int exprWidth = 30;
    	int totalSteps = stepsCount + 2;
		Define.getButtons(this);

   		setSize(totalSteps * 74, 176);
		setLayout(new GridLayout(1, totalSteps, 3, 3));
		MontExprHolder exprHolders[] = new MontExprHolder[stepsCount];
		ppanel = new MontStepPanel[stepsCount];
    	MontMainPanel mainPane = new MontMainPanel ();
      	mainPane.init (meh = new MainExprHolder (null), exprWidth, exprWidth);
    	add (mainPane);
    	for (int i=0; i<stepsCount; i++) {
    		ppanel[i] = new MontStepPanel ();
     		ppanel[i].init (exprHolders[i] = new MontExprHolder(), exprWidth, exprWidth);
    		add (ppanel[i]);
   			mainPane.addStepObserver (ppanel[i]);
    	}
		meh.setList (exprHolders);
    	add (new MontCommandsPanel(exprHolders, exprWidth, this));
   		moveFrame (20, 310);
   }
    public void actionPerformed (ActionEvent e) {
    	String action = e.getActionCommand();
    	if (action.equals (clearCommand)) {
			meh.clear();
    	}
	}
	public void decompose (TExp exp) {
		VarArgsFunctionDecomposer md = new VarArgsFunctionDecomposer (exp, ppanel.length);
		int n = md.getArgsCount(), i=0;
		while (n > 0) {
			ppanel[i++].decompose (md.getArg(--n));
		}
	}
}

class DelayControler extends EditControler
{
	public DelayControler (Color inColor, Image img, int msg) {
		super (new JamButtonControler(0,9999,0,inColor,img), 4);
		setMessage (msg);
	}
  	public void update (Observable o, Object arg) {
  		MsgNotifier mn = (MsgNotifier)o;
  		if (mn.message() == Define.ResetMsg) 
  			setValue (0);
  		super.update (o, arg);
  	}
}

class MontMainPanel extends SeqMainPanel
{
	protected EditControler createDurControl () {
		EditControler e = new DelayControler (Define.durColor, Define.durButton, durMsg);
		e.setValue (0);
		return e;
	}
}

class MontStepPanel extends ParamPanel
{
	protected EditControler createDurControl () {
		EditControler e = new DelayControler (Define.durColor, Define.durButton, durMsg);
		e.setValue (0);
		return e;
	}
	public void decompose (TExp exp) {
		MontParamDecomposer pdec = new MontParamDecomposer();
		pdec.scan (exp);
		TExp body = pdec.getExpr();
		if (body != null) {
			holder.setExpression (body);
			pitchCtrl.setValue ((int)pdec.getPitch());
			velCtrl.setValue ((int)pdec.getVel());
			chanCtrl.setValue ((int)pdec.getChan());
			durCtrl.setValue ((int)pdec.getStrech());
		}
	}
}

class MontHolder extends ResultHolder
{
	public MontHolder (ExprHolder exprHolders[]) {
		super(exprHolders);
	}
	public TExp buildFunction() {
		TExpMaker maker = TExpMaker.gExpMaker;
		int n = countExpr();
		TExp note[] = new TExp[n];
		TExp f = maker.createNull();
		for (int i=0; i< n; i++) {
			note[i] = maker.createNote( Define.expColors[i], 60, 100, 0, 1000);
			f = maker.createMix (note[i], f);
		}
		for (int i=0; i< n; i++) {
			f = maker.createAbstr (note[i], f);
		}
		FunctionTag tagger = new FunctionTag (MontConstructor.appletName, n);
		return tagger.tag (f);
	}
}

class MontCommandsPanel extends ParamFrame
{
	public MontCommandsPanel(ExprHolder exprHolders[], int width, ActionListener listener) {
		super(Color.red);
		MontHolder mh = new MontHolder (exprHolders);
		super.init (mh, width, width);
   		GridBagLayout gbl = (GridBagLayout)getLayout();
		GridBagConstraints c = new GridBagConstraints();
		int eol = GridBagConstraints.REMAINDER;
		int align = GridBagConstraints.CENTER;

		setConstraints (c, eol, GridBagConstraints.NONE, align, 0,0, 0,0, 0,2,2,1);
		add (new ColoredLabel("Parameters", Label.CENTER, Color.darkGray), gbl, c);
		setConstraints (c, eol, GridBagConstraints.BOTH, align, 30,30, 1,1, 0,4,4,4);
		add (new SeqHelpPanel (), gbl, c);
		setConstraints (c, eol, GridBagConstraints.NONE, align, 20,0, 1,0, 0,5,5,2);
		setConstraints (c, eol, GridBagConstraints.NONE, align, 20,0, 1,0, 0,5,5,4);

		Button clear = new Button (MontConstructor.clearCommand);
     	clear.setActionCommand (MontConstructor.clearCommand);
     	clear.addActionListener (listener);
		add (clear, gbl, c);

		for (int i=0; i < exprHolders.length; i++) {
			exprHolders[i].addObserver (mh);
		}
	}
	public void add (Component p, GridBagLayout gbl, GridBagConstraints c) {
		gbl.setConstraints (p, c);
		add (p);
	}
}

class MontParamDecomposer extends ParamDecomposer
{
	public MontParamDecomposer () {
		super ();
		stretch = 0;
	}
	/*
	public TExp scanStrech (TExp exp) {
		TExp converted = exp.convertSeqExp();
		if (converted != null) {
			if (stretch != 0) return null;
			TVector val = ((TEvent)converted.getArg1()).val;
			stretch = val.getVal(TEvent.DURATION);
			exp = converted.getArg2();
		}
		return exp;
	}
	*/
	
	// modif steph le 21/07/98
	
	public TExp scanStrech (TExp exp) {
		TExp converted = exp.convertSeqExp();
		if (converted != null) {
			if (stretch != 0) return null;
			TEvent ev = (TEvent)converted.getArg1();
			stretch = ev.getDur();
			exp = converted.getArg2();
		}
		return exp;
	}	
}

class MontExprHolder extends ParamExprHolder
{
	public MontExprHolder () {
		super();
		dur = 0;
	}
  	public boolean catchDur (Object arg) {
		int p = ((Integer)arg).intValue();
		if (p != dur) {
			dur = p;
			refresh = true;
			return true;
		}
		return false;
  	}
	public TExp createDuration (TExp e, float dur) {
		if (dur > 0) {
			TExp silence = TExpMaker.gExpMaker.createSilence(Color.black, 0, 0, 0, dur);
			e = TExpMaker.gExpMaker.createSeq  (silence,  e);
		}
		return e;
	}
}
