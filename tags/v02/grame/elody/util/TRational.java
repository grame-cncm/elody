package grame.elody.util;

/*******************************************************************************************
*
*	 TRational (classe) :manipulation de rationel
* 
*******************************************************************************************/

public final class TRational {
	private int num = 1;
	private int denom = 1;
	
	static int calcPGCD (int a, int b) { return (b == 0) ? a : calcPGCD(b, a%b);}

	public TRational(int num, int denom) {
		int g = calcPGCD( num, denom);
		this.num = num/g;
		this.denom = denom/g;
	}
	
	public int num () {return num;}
	public int denom () {return denom;}
	
	public int toInteger () {return num/denom;}
	public float toFloat () {return ((float)num)/ ((float)denom);}
	public String toString() { return (Integer.toString(num) + "/" + Integer.toString(denom));}
	

	static public TRational Add (TRational a, TRational b) {
		return new TRational( a.num() * b.denom() + a.denom() * b.num(), a.denom() * b.denom());
	}

	static public TRational Sub (TRational a, TRational b) {
		return new TRational( a.num() * b.denom() - a.denom() * b.num(), a.denom() * b.denom());
	}

	static public TRational Mult (TRational a, TRational b) {
		return new TRational( a.num() * b.num() , a.denom() * b.denom());
	}

	static public TRational Div (TRational a, TRational b) {
		return new TRational( a.num() * b.denom() , a.denom() * b.num());
	}
	
	public boolean equals(Object obj) {
		return (this == obj) || ((obj instanceof TRational) 
			&& num == ((TRational)obj).num()
			&& denom == ((TRational)obj).denom());
	}
	
	static public boolean Sup (TRational a, TRational b) {
	 	return (a.num() * b.denom() - a.denom() * b.num() > 0) ;
	}
	
	static public boolean SupEq (TRational a, TRational b) {
	 	return (a.num() * b.denom() - a.denom() * b.num() >= 0) ;
	}
	
	static public boolean Inf (TRational a, TRational b) {
	 	return (a.num() * b.denom() - a.denom() * b.num() < 0) ;
	}
	
	static public boolean InfEq (TRational a, TRational b) {
	 	return (a.num() * b.denom() - a.denom() * b.num() <= 0) ;
	}
}
