/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

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
