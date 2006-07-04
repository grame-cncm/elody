#include <ctype.h>
#include <string>
#include <string.h>
#include <math.h>
#include <stdlib.h>

#include <MidiShare.h>
#include "sampler.h"
#include "sndfile.h"
#include <direct.h>
//#include <libgen.h>

#if !defined(PI)
 #define PI (float)3.14159265359
#endif

#define TRACE(x) //printf x
//#define TRACE(x...)

using namespace std;

static int	iscomment (char* s)
{
	TRACE(("check for comments for line : %s\n", s));
	while ( (*s != 0) && isspace(*s) ) s++;
	return *s == 0 || *s =='#';
}

inline float square (float x) { return x*x; }

static void initVelScale(float tbl[])
{
	double p = 0.7;
	for (int v=0; v<128; v++)  tbl[v]= pow(v/127.0, p) ;
}

static void initChanVolPan(float vol[], float pan[])
{
	for (int c=0; c<CHANNELS_NUM; c++)
	{
		vol[c]= 100.0/127.0;
		pan[c]= 0.5;
	}
}

/********************************************************************************
							METHODES
							
	newSoundFile	: cree une nouvelle entree pour la liste des soundfiles
			
*********************************************************************************/

static void readSoundFile(TSound* snd)
{
	SNDFILE*		sf;
	SF_INFO			info;
	unsigned int 	q;
	short*			buffer;
	
	/*
		on ouvre le fichier et on fait quelques verifications
	*/
	sf = sf_open (snd->fSoundName,SFM_READ, &info);
	
	if (sf == NULL) { sf_perror(sf); exit(0); }
	
	if (info.channels != 1) { 
		printf("ERREUR : le fichier doit etre mono or %s comporte %d canaux\n", snd->fSoundName, (int)info.channels);
		exit(0);
	}
	
	if (info.samplerate != 44100) { 
		printf("WARNING : le fichier doit etre a 44100 Hz or %s est a %d Hz\n", snd->fSoundName, (int)info.samplerate);
	}
	
	if ( info.frames > (1<<23) ) { 
		printf("ERREUR : le fichier est trop long, %s comporte  %d samples\n", snd->fSoundName, (int)info.frames);
		exit(0);
	}
	/*
		on alloue la memoire necessaire aux echantillons
	*/
	snd->fSize = info.frames;
	snd->fSamples = (float*) malloc(info.frames * sizeof(float));
	
	//snd->fSamples = (float*)SIXTEENIZE(malloc(info.frames * sizeof(float) + 16));
	
	buffer = (short*) malloc(info.frames * sizeof(short));
	if ((snd->fSamples) == NULL || (buffer == NULL)) { 
		printf("ERREUR : impossible d'allouer la memoire pour les echantillons, %s comporte  %d samples\n", snd->fSoundName, (int)info.frames);
		exit(0);
	}
	/*
		et on lit les echantillons et on controle la taille lue
	*/
	
	q = sf_read_short(sf, buffer, snd->fSize);
	if (q < snd->fSize) {
		printf("ERREUR : de lire tous le fichier, %s comporte  %d samples, seuls %d ont put etre lus\n",  snd->fSoundName, (int)info.frames, q);
		exit(0);
	}
	for (q= 0; q <snd->fSize; q++) snd->fSamples[q] = (float) (buffer[q]*(1.0/(float)32767));
	free(buffer);
	
}  	
	
static TSound* newSoundFile(char* filename)
{
	TSound*	snd = (TSound*) malloc(sizeof(TSound));
	if (snd == NULL) {
		printf("ERREUR : allocation TSound impossible\n");
		exit(0);
	}
	snd->fSoundName[127] = 0;
	strncpy(snd->fSoundName, filename, 127);
	snd->fSamples = NULL;
	snd->fSize = 0;
	
	readSoundFile(snd);
	return snd;
}

static TSound* getSoundFile(TSampler* s, char* sndfilename)
{	
	long	n = listSize(&s->fSoundList);
	long	i;
	TSound*	snd;
	
	for (i=0; i < n; i++) {
		snd = (TSound*) getListElem(&s->fSoundList, i);
		if (strcmp(snd->fSoundName, sndfilename) == 0) return snd;
	}
	snd = newSoundFile(sndfilename);
	addListElem(&s->fSoundList, n, (Elem*)snd);
	return snd;
}

static void printSoundList(TSampler* s)
{
	long 	i;
	
	printf("sound list { \n");
	for (i=0; i < listSize(&s->fSoundList); i++) {
		printf("\t%2ld : %s\n", i, ((TSound*)getListElem(&s->fSoundList, i))->fSoundName);
	}
	printf("}\n");
}

static TAction* newAction(TSound* sound, float speed, int traj)
{
	TAction*	action = (TAction*) malloc(sizeof(TAction));
	if (action == NULL) {
		printf("ERREUR : allocation TAction impossible\n");
		exit(0);
	}
	action->fSound 		= sound;
	action->fSpeed 		= speed;
	action->fTraj		= traj;
	action->fPhase		= 0;
		
	return action;
}

static void addRule(TSampler* s, int midichan, int midikey, char* sndfilename, float speed, int traj)
{
	printf("rule : %2d %2d -> %s %f %d\n", midichan, midikey, sndfilename, speed, traj);
	if (s->fRule[midichan][midikey] != NULL) {
		printf("WARNING : ecrasement de la regle %d, %d\n", midichan, midikey);
		free (s->fRule[midichan][midikey]);
	}
		
	s->fRule[midichan][midikey] = newAction(getSoundFile(s, sndfilename), speed, traj);
}

// a la place de : char* dname = dirname(strdup(fname));
/*****************************************************/
static void get_dirname (char* result, char* fname)
{
	string name = fname;
	string::size_type l = name.length(); 
	string::size_type i = name.find_last_of ('/');
	if (i >= l) strcpy(result,".");
	string d = string(name, 0, i);
	strcpy(result,d.c_str());
}
/****************************************************/

static bool readConfigFile(TSampler* s, char* fname)
{
	FILE* 	file;
	char 	line[1024];
	int		midiChan, midiKey, traj, pan, vol;
	float	playspeed;
	char	sndfilename[1024];
	
	TRACE(("lecture du fichier de configuration \"%s\"...\n", fname));
	
	if ((file = fopen(fname,"r")) == NULL) {
		printf("ERREUR : impossible de lire le fichier de configuration \"%s\"\n", fname);
		return false;
	}
	
	// positionne le repertoire courant 
	// a la place de : char* dname = dirname(strdup(fname));
	char dname[255];
	get_dirname(dname,strdup(fname));
	_chdir(dname);
	
	while (fgets(line, 1024, file)) {
		TRACE(("point 1 (%s)\n", line));
		if (iscomment(line)) {
			TRACE(("it is a comment : %s\n", line));
			continue;
		} else if (sscanf(line, "%d %d %s %f %d %d %d", &midiChan, &midiKey, sndfilename, &playspeed, &traj, &pan, &vol) != 7) {
			printf ("ERREUR : ligne non reconnue %s\n", line);
			exit(0);
		}
		s->fChanVol[midiChan] = vol / 127.0;
		s->fChanPan[midiChan] = pan / 127.0;
		addRule(s, midiChan, midiKey, sndfilename, playspeed, traj);
	}
	printSoundList(s);
	return true;
}

/*	
int	traj0 [] = {0,1,2,3,4,5,6,7};
int	traj1 [] = {7,6,5,4,3,2,1,0};

int	traj2 [] = {1,3,5,7,6,4,2,0};
int	traj3 [] = {0,2,4,6,7,5,3,1};

int	traj4 [] = {0,1,5,4,2,3,7,6};
int	traj5 [] = {1,0,4,5,3,2,6,7};

int	traj6 [] = {7,5,3,1,6,4,2,0};
int	traj7 [] = {0,1,2,3,4,5,6,7};
*/

// le numéro de trajectoire représente la sortie :
int	traj0 [] = {0,0,0,0,0,0,0,0};
int	traj1 [] = {1,1,1,1,1,1,1,1};

int	traj2 [] = {2,2,2,2,2,2,2,2};
int	traj3 [] = {3,3,3,3,3,3,3,3};

int	traj4 [] = {4,4,4,4,4,4,4,4};
int	traj5 [] = {5,5,5,5,5,5,5,5};

int	traj6 [] = {6,6,6,6,6,6,6,6};
int	traj7 [] = {7,7,7,7,7,7,7,7};



bool initSampler(TSampler* s, char* fname)
{
	// initialisation des trajectoires
	
	s->fTrajTbl[0] = traj0;
	s->fTrajTbl[1] = traj1;
	
	s->fTrajTbl[2] = traj2;
	s->fTrajTbl[3] = traj3;
	
	s->fTrajTbl[4] = traj4;
	s->fTrajTbl[5] = traj5;
	
	s->fTrajTbl[6] = traj6;
	s->fTrajTbl[7] = traj7;	
	
#if 0	
	
	printf("bidon...\n");
	for (int t=0; t<8; t++) {
		for (int p=0; p<8; p++) printf("%d ", s->fTrajTbl[t][p]);
		printf("\n");
	}
	{
		{0,1,2,3,4,5,6,7},
		{7,6,5,4,3,2,1,0},
		{1,3,5,7,6,4,2,0},
		{0,2,4,6,7,5,3,1},
		{0,1,2,3,4,5,6,7},
		{0,1,2,3,4,5,6,7},
		{0,1,2,3,4,5,6,7},
		{0,1,2,3,4,5,6,7}};
#endif
	
	/* Initialisation des tables	*/
	initVelScale(s->fVelScale);
	initChanVolPan(s->fChanVol, s->fChanPan);
	
	/* initialisation des listes a vide	*/
	
	initList(&s->fSoundList);
	initList(&s->fFreeVoiceList);
	initList(&s->fPlayVoiceList);
 	
	/* creation de la freelist des voix	*/
	
	for (int v=0; v<kMaxVoices; v++) addListElem(&s->fFreeVoiceList, v, (Elem*) &s->fVoiceTable[v]); 
	
	/* initialisation de la table des regles	*/
	
	for (int c=0;c<CHANNELS_NUM;c++) for (int k=0;k<128;k++) s->fRule[c][k] = NULL; 
	
	
	/* lecture du fichier de configuration 	*/
	return readConfigFile(s, fname);
}

static TVoice* allocFreeVoice(TSampler* s)
{
	if (listSize(&s->fFreeVoiceList) > 0) {
		
		/* retourne une voix disponible */
		return (TVoice *) remListElem(&s->fFreeVoiceList, 0);
		
	} else {
		
		/* si ca n'est pas possible, retourne la plus ancienne voix encore en activite */
		//return (TVoice *) remListElem(&s->fPlayVoiceList, 0);
		return 0;
	}
}

static void addPlayVoice(TSampler* s, TVoice* v)
{
	/* ajoute une voix en fin de liste pour preserver l'ordre */
	addListElem(&s->fPlayVoiceList, listSize(&s->fPlayVoiceList), (Elem*) v);
}

static void doKeyOn (TSampler* s, int chan, int key, int vel)
{
	TVoice*		v;
	TAction*	a;
	
	a = s->fRule[chan][key];
	
	if (a) {
		v = allocFreeVoice(s);
		if (v) {
			if (s->fStereoMode != 0.0)
			{
				v->fDst = (int) ( s->fChanPan[chan] * 127.0 );	// gestion du panoramique en mode stereo 
			}
			else
			{
				v->fDst	= s->fTrajTbl[a->fTraj][a->fPhase];
				a->fPhase = (a->fPhase+1)&7;
				if (v->fDst > 7)
				{
					printf ("error : dest = %d\n", v->fDst);
					v->fDst &= 7;
				}
			}
			v->fChan 	= chan;
			v->fKey 	= key;
			v->fSamples = a->fSound->fSamples;
			v->fSize 	= a->fSound->fSize;
			v->fVel 	= vel;
			v->fCurPos 	= 0;
			v->fStep 	= (unsigned long)(a->fSpeed * 256.0);
			v->fLoopMode = 0; // etait 1, voir autre strategie...
			v->fStopRequest = 0;
			addPlayVoice(s,v);
			//if (v->fStep==256) printf("OK 256\n"); else printf("ERREUR pas 256\n");
		}
	}
}

static void doKeyOff (TSampler* s, int chan, int key, int vel)
{
	/* 
		demande l'arret de la premiere (donc la plus ancienne) voix correspondant 
		a chan et pitch dans la liste des play voices 
	*/
	long 	i;
	
	for (i=0; i < listSize(&s->fPlayVoiceList); i++) {
		TVoice* v = (TVoice*)getListElem(&s->fPlayVoiceList, i);
		if (v->fChan == chan && v->fKey == key && v->fStopRequest == 0) {
			v->fStopRequest = 1;
			break;
		}
	}
}

//static long long TOTTIME = 0;
//static long TOTRUN = 0;

static int mixOneVoiceFixedSpeed (TSampler* sss, unsigned long const n, float* multi[], TVoice* v )
{
	float * const		src 	= v->fSamples;
	unsigned long 		pos 	= v->fCurPos;
	unsigned long const maxpos 	= v->fSize;
	
	/* Calcul des niveaux de sortie */
	
   	float 	level   = sss->fVelScale[v->fVel] * sss->fVelScale[int(127.0*sss->fChanVol[v->fChan])];
	float	cosValue = cos ( v->fDst / 127.0 * PI / 2.0 );
	float	sinValue = sin ( v->fDst / 127.0 * PI / 2.0 );	

	unsigned long		i = 0;		/* i indique le nombre d'echantillons mixes */
	
	/* 
		tant qu'on n'a pas mixe le nb voulu d'echantillons
	*/
	while (i < n) {
		
		if ((n-i) + pos < maxpos) {
			
			/*	
				si le mixage peut se faire sans depasser la fin du son 
				on mixe tous les echantillons demandes d'un coup.
			*/

			for (;i<n; i++) {
				float x = src[pos++];
				if (sss->fStereoMode == 0.0)
					multi[v->fDst][i] += x * level;
				else
				{
					multi[0][i] += x * level * cosValue ;
					multi[1][i] += x * level * sinValue ;
				}
			} 
				
			/*	on sort en indiquant qu'il faudra continuer a jouer cette voix 
				puisqu'on n'est pas arrive au bout du son
			*/
			v->fCurPos = pos;
			//printf ("bench 1 : %Ld cycles\n", t2-t1);
			return 0; 

		} else {
			
			/* 
				on mixe jusqu'a la fin du son et on avise
			*/
			do {
				float x = src[pos];
				if (sss->fStereoMode == 0.0)
					multi[v->fDst][i] += x * level;
				else
				{
					multi[0][i] += x * level * cosValue ;
					multi[1][i] += x * level * sinValue ;
				}
				pos++;
				i++;
			} while (pos < maxpos);
			
			/* 
				on sort en indiquant que la voix a termine si elle n'est pas en mode
				boucle, ou si un arret a ete demande.
			*/
			if (v->fLoopMode == 0 || v->fStopRequest) { 
				v->fCurPos = pos;
				//printf ("bench 2 : %Ld cycles\n", t2-t1);
				//printf ("ARRET\n"); 
				return 1;
			}

			/* 
				sinon on wrappe pos et on recommence
			*/
			pos -= maxpos;
		}
	} 
	/* cas rare ? */
	v->fCurPos = pos;
	//printf ("bench 3 : %Ld cycles\n", t2-t1);
	return 0;
}
					
static int mixOneVoiceVariSpeed (TSampler* sss, unsigned long const n, float* multi[], TVoice* v )
{
	unsigned long const step 	= v->fStep;
	float * const		src 	= v->fSamples;
	unsigned long 		pos 	= v->fCurPos;
	unsigned long const maxpos 	= v->fSize * 256;
	
	/* Calcul des niveaux de sortie */
	
   	float 	level   = sss->fVelScale[v->fVel] * sss->fVelScale[int(127.0*sss->fChanVol[v->fChan])];
	float	cosValue = cos ( v->fDst / 127.0 * PI / 2.0 );
	float	sinValue = sin ( v->fDst / 127.0 * PI / 2.0 );	
	
	unsigned long		i = 0;		/* i indique le nombre d'echantillons mixes */
	
	/* 
		tant qu'on n'a pas mixe le nb voulu d'ech. 
	*/
	while (i < n) {
		
		if ((n-i)*step + pos < maxpos) {
			
			/*	
				si le mix peut se faire sans depasser la fin du son 
				on mixe tous les echantillons demandes d'un coup.
			*/
			for (;i<n; i++) {
				float x = src[pos >> 8];
				pos += step;
				if (sss->fStereoMode == 0.0)
					multi[v->fDst][i] += x * level;
				else
				{
					multi[0][i] += x * level * cosValue;
					multi[1][i] += x * level * sinValue;
				}

				
			} 
			
			/*	on sort en indiquant qu'il faudra continuer a jouer cette voix 
				puisqu'on n'est pas arrive au bout du son
			*/
			v->fCurPos = pos;
			//printf ("bench 1 : %Ld cycles\n", t2-t1);
			return 0; 

		} else {
			
			/* 
				on mixe jusqu'a la fin du son et on avise
			*/
			do {
				float x = src[pos >> 8];
				pos += step;
				if (sss->fStereoMode == 0.0)
					multi[v->fDst][i] += x * level;
				else
				{
					multi[0][i] += x * level * cosValue;
					multi[1][i] += x * level * sinValue;
				}

				i++;
			} while (pos < maxpos);
			
			/* 
				on sort en indiquant que la voix a termine si elle n'est pas en mode
				boucle, ou si un arret a ete demande.
			*/
			if (v->fLoopMode == 0 || v->fStopRequest) { 
				v->fCurPos = pos;
				//printf ("bench 2 : %Ld cycles\n", t2-t1);
				//printf ("ARRET\n"); 
				return 1;
			}

			/* 
				sinon on wrappe pos et on recommence
			*/
			pos -= maxpos;
		}
	} 
	/* cas rare ? */
	v->fCurPos = pos;
	//printf ("bench 3 : %Ld cycles\n", t2-t1);
	return 0;
}
		
void mixAllVoices (TSampler* s, unsigned long nbsamples, float* multi[])
{
	long i = listSize(&s->fPlayVoiceList); 
	
	/* 
		Mixe toutes les voix dans les 2 buffers de sortie.
		Attention, on parcourt les voix en commençant par la fin de la liste
		car on va supprimer au fur et a mesure les voix qui ont termine. 
	*/
	
	while (i--) {
		int stopped;
		
		TVoice* v = (TVoice*)getListElem(&s->fPlayVoiceList, i);
		
		if (v->fStep == 256) {
			stopped	= mixOneVoiceFixedSpeed(s, nbsamples, multi, v);
		} else {
			stopped	= mixOneVoiceVariSpeed(s, nbsamples, multi, v);
		}
		
		if (stopped) {
			/*
				Si la voix a termine, on la supprime de la liste des play voices
				et on l'ajoute a la free list.
			*/
			addListElem(&s->fFreeVoiceList, 0, remListElem(&s->fPlayVoiceList, i));
		}	
	}
}

void processMidiEvents(short ref)
{
	TSampler* s = (TSampler*)MidiGetInfo(ref);
	/*
		Traite tous les evenements midi recus et adapte la liste des voix actives du sampler
		en consequence.
	*/
	MidiEvPtr	e;
	Boolean trace = false;

	while ((e = MidiGetEv(ref))) {
		switch (EvType(e)) {
		
			case typeNote:
				if (trace)
					printf("Note\t(chan=%d,\tpitch=%d,\tvel=%d,\tdur=%d)\n",Chan(e)+Port(e)*16,Pitch(e),Vel(e), Dur(e));
				doKeyOn(s, Chan(e)+Port(e)*16, Pitch(e), Vel(e));
				/* auto envoie le keyoff */
				EvType(e)=typeKeyOff;
				MidiSendAt(ref|128, e, MidiGetTime()+Dur(e));
				break;
				
			case typeKeyOff:
				if (trace)
					printf("KeyOff\t(chan=%d,\tpitch=%d,\tvel=%d)\n",Chan(e)+Port(e)*16,Pitch(e),Vel(e));
				doKeyOff(s, Chan(e)+Port(e)*16, Pitch(e), Vel(e));
				MidiFreeEv(e);
				break;

			 case typeKeyOn:
				if (trace)
					printf("KeyOn\t(port=%d,\tchan=%d,\tpitch=%d,\tvel=%d)\n",Port(e),Chan(e)+Port(e)*16,Pitch(e),Vel(e));
				if (Vel(e) > 0) {
					doKeyOn(s, Chan(e)+Port(e)*16, Pitch(e), Vel(e));
				} else {
					doKeyOff(s, Chan(e)+Port(e)*16, Pitch(e), 64);
				}
				MidiFreeEv(e);
				break;

			case typeCtrlChange:
				if (trace)
					printf("CtrlChange\t(chan=%d,\tpitch=%d,\tvel=%d)\n",Chan(e)+Port(e)*16,Pitch(e),Vel(e));
				if (Pitch(e) == 7) {
					s->fChanVol[Chan(e)+Port(e)*16] = float(Vel(e))/127.0;					
				}
				if ((Pitch(e) == 10)&&(s->fStereoMode != 0.0)) {
					s->fChanPan[Chan(e)+Port(e)*16] = float(Vel(e))/127.0;					
				}
				MidiFreeEv(e);
				break;
			
			default:
				//if (trace)
				//	printf("Unknow\t(chan=%d,\tpitch=%d,\tvel=%d)\n",Chan(e)+Port(e)*16,Pitch(e),Vel(e));
				MidiFreeEv(e);
				break;

		}
	}
}	

