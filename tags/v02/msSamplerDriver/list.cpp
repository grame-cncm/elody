#define NDEBUG 1

#include <assert.h>
#include "list.h"


/*
===========================================================
	DŽclaration des mŽthodes internes
===========================================================
*/

static void chooseCurrentElem (register List* l, register long i)
{
	register Elem* 	e;
	register long 	n;
	register long 	dt;
	register long	dr;
	register long	ga;
	
	assert (l != 0);
	assert (i >= 0);
	assert (i < l->size);
	
	e = l->elem;
	n = l->size;
	dt = i - l->index;
	if (dt != 0) {
		if (dt > 0) {
			dr = dt;
			ga = n - dt;
		} else {
			dr = n + dt;
			ga = - dt;
		}
		if (dr < ga) {
			do { e = e->nxt; } while (--dr);
		} else {
			do { e = e->prv; } while (--ga);
		}
		l->elem = e;
		l->index = i;
	}
}

static void	addCurrentElem (List* l, Elem* e)
{
	Elem* 	c;
	long 	n;

	assert (l != 0);
	assert (e != 0);

	c = l->elem;
	n = l->size;
	
	if (n > 0) {
		e->prv = c->prv;
		e->prv->nxt = e;

		e->nxt = c;
		c->prv = e;
	} else {
		e->nxt = e->prv = e;
	}

	l->elem = e;
	l->size = n+1;
}

static Elem* remCurrentElem (List* l)
{
	Elem* 	e;
	long	n;

	assert(l != 0);
	assert(l->size > 0);
	
	n = l->size;
	e = l->elem;
	if (n > 1) {
		l->elem = e->nxt;
		e->nxt->prv = e->prv;
		e->prv->nxt = e->nxt;
		l->size = n - 1;
		if (l->index == l->size) l->index = 0;
	} else {
		l->elem = 0;
		l->size = 0;
	}
	return e;
}

	

/*
===========================================================
	implementation des fonctions publiques 
===========================================================
*/

void initList(List* l)
{
	assert(l != 0);
	
	l->size = 0;
	l->index = 0;
	l->elem  = 0;
}

void addListElem (List* l, long i, Elem* e)
{
	assert(l != 0);
	assert(e != 0);
	assert(i >= 0);
	assert(i <= l->size);
	
	if (i == l->size) {
		if (l->index != 0) chooseCurrentElem(l, 0);
		l->index = i;
	} else {
		if (l->index != i) chooseCurrentElem(l, i);
	}
	addCurrentElem(l,e);
}

Elem* getListElem (List* l, long i)
{
	assert(l != 0);
	assert(i >= 0);
	assert(i < l->size);
	
	if (l->index != i) chooseCurrentElem(l, i);
	return l->elem;
}

Elem* remListElem (List* l, long i)
{
	assert(l != 0);
	assert(i >= 0);
	assert(i < l->size);
	
	if (l->index != i) chooseCurrentElem(l, i);
	return remCurrentElem(l);
}

