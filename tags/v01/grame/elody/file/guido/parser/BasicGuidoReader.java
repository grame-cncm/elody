package grame.elody.file.guido.parser;

public abstract class BasicGuidoReader {
	public void GD_INIT_SEGM() {}
	public void GD_EXIT_SEGM(){}
	public void GD_APP_SEQ(){}

	public void GD_INIT_SEQ(){}
	public void GD_EXIT_SEQ(){}

	public void GD_NT(String s){}
	public void GD_SH_NT(){}
	public void GD_FL_NT(){}
	public void GD_OCT_NT(String s){}
	public void GD_ENUM_NT(String s){}
	public void GD_DENOM_NT(String s){}
	public void GD_DOT_NT(){}
	public void GD_DDOT_NT(){}

	public void GD_APP_NT(){}

	public void GD_INIT_CH(){}
	public void GD_CH_APP_NT(){}
	public void GD_APP_CH(){}

	public void GD_TAG_START(String s){}
	public void GD_TAG_END(){}
	public void GD_TAG_NARG(String s){}
	public void GD_TAG_RARG(String s){}
	public void GD_TAG_SARG(String s){}
	public void GD_TAG_TARG(String s){}  /* don't use this! */
	public void GD_TAG_ADD_ARG(){}       /* don't use this! */
	public void GD_TAG_ADD(){}
	
	
	public Object getExp() {return null;}
}
