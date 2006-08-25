package grame.elody.editor.expressions;

import grame.elody.lang.TEvaluator;
import grame.elody.lang.texpression.TInput;
import grame.elody.lang.texpression.expressions.TEvent;
import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.lang.texpression.expressions.TNullExp;
import grame.elody.lang.texpression.valeurs.TApplVal;
import grame.elody.lang.texpression.valeurs.TClosure;
import grame.elody.lang.texpression.valeurs.TErrorVal;
import grame.elody.lang.texpression.valeurs.TMixVal;
import grame.elody.lang.texpression.valeurs.TNamedVal;
import grame.elody.lang.texpression.valeurs.TSequenceVal;
import grame.elody.lang.texpression.valeurs.TValueVisitor;

import java.awt.Dimension;
import java.awt.Graphics;


public class TGraphVisitor implements TValueVisitor, Runnable
{
	private int nYield;
	protected Thread	displayer; 
    protected TExp		expression;
    protected Graphics	graphic;
    protected Dimension	size;
	
	static int count = 0;

	public boolean stopVisite (int date, float offset) { return false; }
	public boolean initialize (TExp exp, Graphics g, Dimension d) {
		graphic = g;
		size = d;
		expression = exp;
		return ((graphic!=null) && (exp!=null));
	}
	public void yield () {
		if (nYield == 10) {
			try { Thread.sleep(count*100 + (int)Math.random()*100) ; }
			catch (Exception e) { System.err.println( "TGraphVisitor yield : " + e); }
			nYield = 0;
		}
		else nYield++;
	}
	public  void Visite (TMixVal val, int date, Object arg) {
		val.getValArg1().Accept(this,date,arg);
		val.getValArg2().Accept(this,date,arg);
		yield ();
	}
	public  void Visite (TSequenceVal val, int date, Object arg) {
		val.getValArg1().Accept(this, date, arg);
		float offset = val.getValArg1().Duration();
		yield ();
		if (!stopVisite (date, offset)) {
			val.getValArg2().Accept(this, (int)(date + offset), arg);
		}
	}
	public  void run() {
    	if (expression != null) {
    		// Render1 : methode qui ne catche pas les erreur
			TEvaluator.gEvaluator.Render1 (expression, this, 0, null);
			expression = null;
		}
		synchronized (this){
			displayer = null;
			count--;
		}
	}
  	  	
	public synchronized void renderExp (TExp exp, Graphics g, Dimension d) {
		if (displayer != null) {
			if (displayer.isAlive()) {
				displayer.suspend();
				displayer.stop();
				count--;
			}
			displayer = null;
		}
		if (initialize (exp, g, d)) {
			displayer = new Thread (this, "GraphVisitor");
			displayer.setPriority (Thread.MIN_PRIORITY);
			displayer.start();
			count++;
		}
	}
	public void Visite (TErrorVal val, int date, Object arg) { 
		TExp exp = val.getArg1();
		System.err.println( "TGraphVisitor Viste got TErrorVal : " + exp);
	}
	
	public void Visite(TInput ev,int date, Object arg) { }
	public void Visite (TClosure val, int date, Object arg) { }
	public void Visite (TApplVal val, int date, Object arg) { }
	public void Visite (TNullExp val, int date, Object arg) { }
	public void Visite(TNamedVal val,int date,Object arg){ val.Accept(this, date,arg);}
	public void renderExp(TExp exp) { }
    public String toString() 		{ return "TGraphVisitor"; }
	public void Visite(TEvent val, int date, Object arg) { }
}

