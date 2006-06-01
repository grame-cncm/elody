package grame.elody.file.text.saver;

import grame.elody.lang.texpression.expressions.TExp;

import java.io.BufferedWriter;
import java.io.Writer;


/*******************************************************************************************
*
*	 TTextExpSaver (classe) : le saver TEXT (interne)
* 
*******************************************************************************************/

public final class TTextExpSaver {
	TTextExpWriter writer;
	
	public TTextExpSaver (Writer out) { writer = new TTextExpWriter(new BufferedWriter(out));}
	public void writeFileHeader() {writer.writeFileHeader();}
	public void writeTitle(String title) {writer.writeTitle(title);}
	public void writeAuthor(String author) { writer.writeAuthor(author);}
	public void writeDescription(String dec) {writer.writeDescription(dec);}
	public void writeFileEnd() {writer.writeFileEnd();}
	public void writeExp(TExp exp) {exp.Accept(writer,null);}
}
