package grame.elody.lang.texpression.operateurs;


public final class TMult implements TOperator {
	public static  TMult op = new TMult();
	public float Execute (float a, float b) {return a * b;}
}
