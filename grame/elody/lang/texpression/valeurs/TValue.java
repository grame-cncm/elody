package grame.elody.lang.texpression.valeurs;

import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.lang.texpression.operateurs.TOperator;

public interface TValue {
	public static   int MaxDur = 600000;
	public static   int MaxRecur = 25;
	
	public  TValue 	Begin(float n);
	public  TValue 	Rest(float n);
	public  TValue 	Apply(TValue susp);
	
	
	public  float 	Duration ();
	  		float 	DurationAux (float limit, int n);
	
	public 	void 	Accept(TValueVisitor v,int date, Object arg);
	
	public TValue 	Modify(int index,  float value, TOperator op);
	public TValue 	Mute();
	
	public TExp 	Reify(float dur);
	
	
	public  TValue getValArg1();
	public  TValue getValArg2();
}
