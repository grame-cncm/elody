package grame.elody.file.text.parser;

public class ASTchannel extends SimpleNode {

	float value;

	public void setVal(float val) {
		value = val;
	}

	public ASTchannel(int id) {
		super(id);
	}

	public ASTchannel(TTextParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	public Object jjtAccept(TTextParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}
}
