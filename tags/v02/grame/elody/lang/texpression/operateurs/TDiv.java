package grame.elody.lang.texpression.operateurs;


public final class TDiv implements TOperator {
	public static  TDiv op = new TDiv();
	public float Execute (float a, float b) {return a / b;}
}
