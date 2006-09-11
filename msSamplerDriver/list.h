/*

  Copyright © Grame 2006

  This library is free software; you can redistribute it and modify it under 
  the terms of the GNU Library General Public License as published by the 
  Free Software Foundation version 2 of the License, or any later version.

  This library is distributed in the hope that it will be useful, but
  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
  or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Library General Public License 
  for more details.

  You should have received a copy of the GNU Library General Public License
  along with this library; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.

  Grame Research Laboratory, 9, rue du Garet 69001 Lyon - France
  grame@rd.grame.fr

*/


/*-------------------------------------------------------------
				Listes doublement chainees
			
Une liste comporte un nombre variable d'elements. Chaque element
associe une clef et un contenu. L'acces sequentiel aux elements
d'une liste et favorise par la gestion d'un cache vers le dernier
element accede et par des doubles liens de chainage entre les 
elements
---------------------------------------------------------------*/


/*-------------------------------------------------------------
			Definition des structures
---------------------------------------------------------------*/

typedef struct SList List;		
typedef struct SElem Elem;		

typedef struct SList {
	long	size;		/* nb d'elements 			*/
	long 	index;		/* numero element courant	*/
	Elem*	elem;		/* ptr element courant 		*/
} SList;

typedef struct SElem {
	Elem*	nxt;		/* chainage avant 			*/
	Elem* 	prv;		/* chainage arriere 		*/
} SElem;



/*-------------------------------------------------------------
			Fonctions publiques sur les listes
---------------------------------------------------------------*/

void 	initList(List* l);			/* initialise une liste ˆ vide	*/

#define listSize(l) ((l)->size)		/* nb d'elements dans la liste	*/

void	addListElem (List* l, long i, Elem* e);
Elem*	getListElem (List* l, long i);
Elem*	remListElem (List* l, long i);
