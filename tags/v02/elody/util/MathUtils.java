package grame.elody.util;

/*******************************************************************************************
*
*	 MathUtils (classe) : utilitaires 
* 
*******************************************************************************************/

public final class MathUtils {
	public final static	float setRange(float val,  float min , float max){
		return val < min ? min : val > max ? max : val;
	}
	
	public final static	int setRange(int val,  int min , int max){
		return val < min ? min :   val > max ? max : val;
	}
}
