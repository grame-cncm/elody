package grame.elody.file.parser;

import grame.elody.lang.texpression.expressions.TExp;

import java.io.Serializable;


/*******************************************************************************************
*
*	 TFileContent (classe) : contenu d'un fichier Elody
* 
*	 - un titre
*    - un auteur              
*	 - une description
*    - une expression 
*
* 	 Ces informations sont sauvées dans le format HTML et Texte
*
*******************************************************************************************/

public final class TFileContent implements Serializable {
	public String title = "title";
	public String author  = "author";
	public String description  = "description";
	public TExp exp = null;
	
	public TFileContent (TExp exp) {this.exp = exp;}
	
	public TFileContent (String title, String author, String description, TExp exp) {
		this.title = title;
		this.author = author;
		this.description = description;
		this.exp = exp;
	}
	
	public String getTitle() {return title;}
	public String getAuthor() {return author;}
	public String getDescription() {return description;}
	public TExp getExp() {return exp;}
}
