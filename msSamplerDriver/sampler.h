/*
 *  ssp : soft sound player
 *  Yann Orlarey © 2001 Grame
 *	gcc -o yo -Wall -lsndfile main.c list.c
 *
 *  gcc -mcpu=i686 -O3 -ffast-math -fschedule-insns -fforce-mem -fforce-addr -fexpensive-optimizations 
 * -fschedule-insns2 -fssa -freduce-all-givs -o yo -Wall -lsndfile main.c list.c
 */

#if 0
#include <stdio.h>
#include <stdlib.h>
#include <time.h> 
#include <assert.h>
#include <sndfile.h>
#endif

#include "list.h"
#include "Envelope.h"

#define kMaxVoices				256
#define CHANNELS_NUM			32

/********************************************************************************
							STRUCTURES DE DONNEES
							
	TSampler: {sons en memoire, 16x128 regles de jeux, voix en cours de jeu}
	TSound	: un son en mémoire (element de liste)
	TAction	: action d'une regle (son + comment le jouer)
	TVoice	: une note en cours de jeu (element de liste)
			
*********************************************************************************/

struct TSound {
	Elem		fElement;				// element de la liste des sons
	char		fSoundName[128];		// nom du fichier son
	float*		fSamples;				// pointeur vers le premier echantillon
	unsigned long	fSize;				// nombre d'echantillons dans le son
};


struct TAction {
	TSound*		fSound;					// son a jouer
	float 		fSpeed;					// vitesse de lecture [0..1..]
	int			fTraj;					// numero de trajectoire
	int			fPhase;					// position (phase dans la trajectoire)
};


struct TVoice {				
	Elem		fElement;				// element de la liste des voix
	int			fChan;					// canal MIDI de la voix
	int			fKey;					// note MIDI de la voix
	int			fVel;					// velocite : 0..127
	float*		fSamples;				// premier echantillon
	unsigned long	fSize;				// nombre d'echantillons
	unsigned long	fCurPos;			// position courante en virgule fixe 8bits
	unsigned long 	fStep;				// pas de lecture en virgule fixe 8bits
	short		fDst;					// le numero du canal de sortie OU le panoramique en mode stereo
	char		fLoopMode;				// si !=0 indique lecture en boucle
	char		fStopRequest;			// si !=0 arret demande en fin de son
	Envelope	fEnvelope;				// enveloppe de cette voix
};
	

struct TSampler {
	float		fMaster;					// Master Output Volume
	float		fChanVol[CHANNELS_NUM];		// les volumes de chaque canal
	float		fChanPan[CHANNELS_NUM];		// les panoramiques de chaque canal (mode stereo seulement)
	Envelope	fChanEnvelope[CHANNELS_NUM];// les enveloppes pour chaque canal
	float		fChanSensit[CHANNELS_NUM];	// les sensibilités à la vélocité pour chaque canal
	float		fChanVel[CHANNELS_NUM][128]; // table de conversion des vélocités pour chaque canal
	TAction*	fRule[CHANNELS_NUM][128];	// regles de jeu
	TVoice		fVoiceTable[kMaxVoices];	// table des voix 
	List		fSoundList;					// liste des sons en memoire
	List		fFreeVoiceList;				// liste des voix disponibles
	List		fPlayVoiceList;				// liste des voix en cours de jeu
	int*		fTrajTbl[8];				// table des trajectoires
	float		fStereoMode;				// fStereoMode =! 0.0 n'utilise que les sorties 0 et 1
};


// Prototypes

bool initSampler(TSampler* s, char* fname);
void processMidiEvents(short ref);
//void processMidiEvents(TSampler* sampler,short ref);
void mixAllVoices (TSampler* s, unsigned long nbsamples, float* multi[]);

