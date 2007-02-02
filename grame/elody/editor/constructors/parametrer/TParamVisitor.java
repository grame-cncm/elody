package grame.elody.editor.constructors.parametrer;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

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

public class TParamVisitor implements TValueVisitor {
	
	protected boolean containsClosure = false;
	protected boolean containsInput = false;
	
	protected ParamMap absPitch = new ParamMap();
	protected ParamMap absVel = new ParamMap();
	protected ParamMap absChan = new ParamMap();
	
	public TParamVisitor() {}
	
	public void Visite(TEvent val,int date, Object arg)
	{
		if (val.getType() == TExp.SOUND)
		{
			absPitch.push((int) val.getPitch());
			absVel.push((int) val.getVel());
			absChan.push((int) val.getChan());
		}
	}

	public void Visite(TSequenceVal val,int date,Object arg) {
		val.getValArg1().Accept(this,date,arg);
 		val.getValArg2().Accept(this,date,arg);
	}
	
	public void Visite(TMixVal val,int date,Object arg) {
		val.getValArg1().Accept(this,date,arg);
 		val.getValArg2().Accept(this,date,arg);
	}
	
	public void Visite(TClosure val,int date,Object arg)
	{
		containsClosure = true;
	}
	public void Visite(TApplVal val,int date,Object arg) {}
	public void Visite(TErrorVal val,int date,Object arg){}
	public void Visite(TNullExp val,int date,Object arg){}
	public void Visite(TNamedVal val,int date,Object arg){ val.Accept(this, date,arg);}
	
	public void Visite(TInput val,int date,Object arg)
	{
		containsInput = true;
	}

	public int getAbsPitch()	{return getAbsVal(absPitch);}
	public int getAbsVel() 		{return getAbsVal(absVel);}
	//public int getAbsChan()		{return getAbsVal(absChan);}
	public int getAbsChan()		
	{	
		int val = getAbsVal(absChan);
		return (val < 0) ? val : val + 1;
	}
	
	public int getAbsVal(ParamMap map)
	{
		if ((map.size()==1)&&(!containsClosure)&&(!containsInput))
			return map.firstKey().intValue();
		else
			return -1;
	}
	
	public void renderExp(TExp exp) {}
}


class ParamMap extends TreeMap<Integer,Integer>
{
	public ParamMap() { super(); }
	public ParamMap(Comparator<Integer> c) { super(c); }
	public ParamMap(Map<Integer, Integer> m) { super(m); }
	public ParamMap(SortedMap<Integer, Integer> m) { super(m); }

	public void push (int key)
	{
		Integer k = Integer.valueOf(key);
		if (!containsKey(k))
			put(key, Integer.valueOf(1));
		else
		{
			int value = get(k).intValue()+1;
			put(key, Integer.valueOf(value));
		}					
	}
}