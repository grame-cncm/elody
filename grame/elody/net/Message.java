package grame.elody.net;

import java.io.Serializable;

public class Message implements Serializable {
	String Value;
	public Message(String text)	{ Value = text; }
	public Message() { Value = ""; }
	public String getValue() {return Value;}
}
