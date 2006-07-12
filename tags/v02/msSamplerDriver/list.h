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
