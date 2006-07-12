package grame.elody.lang.texpression.operateurs;


public final class TAdd implements TOperator {
	public static  TAdd op = new TAdd();
	public float Execute (float a, float b) {return a + b;}
}
