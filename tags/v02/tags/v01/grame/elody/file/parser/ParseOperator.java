package grame.elody.file.parser;

import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.lang.texpression.expressions.TNullExp;

/*******************************************************************************************
*
*	 ParseOperator (classe) : opération de parsing HTML
* 
*******************************************************************************************/

public abstract class ParseOperator {
	 public abstract  TExp parseExp(THtmlParser1 parser); 
	 public final boolean  checkNull(TExp res) {return (res instanceof TNullExp);}
}
