package grame.elody.lang.texpression.operateurs;


public final class TSub implements TOperator {
	public static  TSub op = new TSub();
	public float Execute (float a, float b) {return a - b;}
}
