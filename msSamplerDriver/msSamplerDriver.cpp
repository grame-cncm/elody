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


#include <direct.h>
#include <MidiShare.h>
#include <iostream>
#include <fstream>
#include <jni.h>

#include <time.h>

#include "portaudio.h"
#include "sampler.h"

#include "msSamplerDriver.h"
#include "msDriverState.h"


/* ----------------------------------*/
/* constants definitions             */
/* ----------------------------------*/

//#define TRACE(x) printf x
#define TRACE(x)

#define MidiShareDrvRef		127

#define SamplerSlotName1 "SoundPlayer Sampler Slot1"
#define SamplerSlotName2 "SoundPlayer Sampler Slot2"
#define kProfileName  "msSamplerDriver.ini"

#define DEFAULT_OUTPUT_DEVICE	Pa_GetDefaultOutputDevice()
#define DEFAULT_SAMPLE_RATE		44100
#define DEFAULT_BUFFER_SIZE		1024
#define DEFAULT_AUDIO_FILE		"soundplayer.conf"
#define MAX_CHANNELS			8

#define ERR_CONFIGFILE			-1000
#define ERR_JNICONVERT			-999

using namespace std;


/* ----------------------------------*/
/* driver data structure             */
/* ----------------------------------*/

typedef struct
{
	int				fRefNum;
    PaStream*		stream;
	float			*outCompute[8];
    int				numOutputs;
	int				framesPerBuffer;
	int				sampleRate;
	char			soundConfigFile[256];
	PaDeviceIndex	outputDevice;
	char			outputDeviceName[1024];
	bool			asleep;
} DriverData, * DriverDataPtr;


/* ----------------------------------*/
/* functions declarations            */
/* ----------------------------------*/

DriverData gData;
static DriverDataPtr GetData ()	{return &gData;}


/* ----------------------------------*/
/* FAUST generated code              */
/* ----------------------------------*/

class soundplayer
{
	int			fRefNum;
	int			fNumOutputs;
	TSampler 	fSampler;

	public:
	soundplayer() : fNumOutputs(8) {}
	virtual ~soundplayer() {}

	virtual int getNumInputs()
	{
		return 0;
	}
	virtual int getNumOutputs() 	
	{
	   	return fNumOutputs;
   	}
	virtual void setRefNum(int n)
	{
		fRefNum=n;
	}
	virtual void setNumOutputs(int n) 	
	{
	   fNumOutputs=n;
	   fSampler.fStereoMode = (fNumOutputs==2) ;
	}
	virtual bool setParam(int chan, int vol, int pan, double sens, int a, int d, double s, int r)
	{
		DriverDataPtr data = GetData();
		return setSamplerParam(&fSampler, data->sampleRate, chan, vol, pan, sens, a, d, s, r);
	}

	/* -------------------------------------------------------------*/
	/* Driver required callbacks                                    */
	/* -------------------------------------------------------------*/
	static void CALLBACK msWakeUp(short r)
	{
		TRACE(("msWakeUp\n"));
		
		PaError err = AudioWakeUp();
		
		//-- 1 - Errors
		if (err==ERR_CONFIGFILE)
		{
			Pa_Terminate();
			SaveConfig("Configuration", "Sound Samples INI file",
				"soundplayer.conf", GetProfileFullName(kProfileName));
			err = AudioWakeUp();
		}
		if ((err!=0)&&(err!=ERR_CONFIGFILE))
		{
			Pa_Terminate();
			fprintf( stderr, "An error occured while using the portaudio stream\n" );
			fprintf( stderr, "Error number: %d\n", err );
			fprintf( stderr, "Error message: %s\n", Pa_GetErrorText( err ) );
		}
	}
	static void CALLBACK msSleep(short r)
	{
		TRACE(("msSleep\n"));
		
		PaError err = AudioSleep();

		//-- 1 - Errors
		if ((err!=0)&&(err!=ERR_CONFIGFILE))
		{
			Pa_Terminate();
			fprintf( stderr, "An error occured while using the portaudio stream\n" );
			fprintf( stderr, "Error number: %d\n", err );
			fprintf( stderr, "Error message: %s\n", Pa_GetErrorText( err ) );
		}
	}
	virtual TDriverOperation getDriverOperation()
	{
		TDriverOperation op = {msWakeUp, msSleep, 0, 0, 0};
		return op;
	}
	/* -------------------------------------------------------------*/

	virtual bool init(char* soundConfigFile)
	{
		TRACE(("Init\n"));
		//-- 0 - Declarations
		DriverDataPtr data = GetData();
		char* gAudioConfigFile;				// config file path
		char soundConfName[256];			// buffer for config file path 
		char path[255];						// buffer for current path

		//-- 1 - Get config file path
		_getcwd(path, 255);
		_snprintf(soundConfName, 255, "%s\\%s", path, soundConfigFile);
		gAudioConfigFile = soundConfName;

		//-- 2 - Initialize Sampler
		if (!initSampler(&fSampler, gAudioConfigFile, data->sampleRate))
		{
			return false;
		}
		TRACE(("initSampler\n"));
		MidiSetInfo(fRefNum, &fSampler); //MidiGetInfo is called by processMidiEvents in sampler.cpp
		if (fSampler.fStereoMode)
			printf("msSamplerDriver loaded : stereo mode\n");
		else
			printf("msSamplerDriver loaded : %d tracks mode\n",fNumOutputs);
		return true;
	}

	virtual void compute(int len, float** inputs, float** outputs)
	{
		for (int c=0; c<fNumOutputs; c++)
	   	{
			for (int i=0; i<len; i++)
			   	outputs[c][i] = 0.0;
		}
		processMidiEvents(fRefNum);
		mixAllVoices (&fSampler, len, outputs);

		//TRACE(("MidiFreeSpace=%d / MidiCountEvs=%d / fRefNum=%d\n",MidiFreeSpace(), MidiCountEvs(fRefNum), fRefNum));

		// post processing for volume control
		const float v = fSampler.fMaster;
		for (int d=0; d<fNumOutputs; d++)
	   	{
			float* out = outputs[d];
			for (int i=0; i<len; i++)
			  	out[i] *= v;
		}
	}
};

soundplayer	DSP;

/* -----------------------------------------------------------------------------*/
/* INI Config 																	*/
/* -----------------------------------------------------------------------------*/

void LoadState()
{
	TRACE(("LoadState\n"));
	DriverDataPtr data = GetData();

	//-- 1 - Load default values
	data->asleep = false;
	if ( DEFAULT_OUTPUT_DEVICE != paNoDevice )
	{
		data->outputDevice = LoadConfigNum("Configuration",
			"Device Number", GetProfileFullName(kProfileName),DEFAULT_OUTPUT_DEVICE);
		const PaDeviceInfo* defOutputDevInfo = Pa_GetDeviceInfo( data->outputDevice );
		strcpy(data->outputDeviceName, LoadConfig("Configuration",
			"Device Name", GetProfileFullName(kProfileName), "") );
		// Reset default values if material configuration has changed
		if ( strcmp(data->outputDeviceName, Pa_GetDeviceInfo(data->outputDevice)->name )!=0)
		{
			data->outputDevice = DEFAULT_OUTPUT_DEVICE;
			defOutputDevInfo = Pa_GetDeviceInfo( data->outputDevice );
			strcpy(data->outputDeviceName, defOutputDevInfo->name);
		}
		
		data->sampleRate = LoadConfigNum("Configuration",
			"Sample rate", GetProfileFullName(kProfileName),defOutputDevInfo->defaultSampleRate);
		data->numOutputs = defOutputDevInfo->maxOutputChannels;
		if (data->numOutputs>MAX_CHANNELS)
			data->numOutputs = MAX_CHANNELS;
	}
	else
	{
		data->outputDevice = 0;
		data->sampleRate = DEFAULT_SAMPLE_RATE;
		data->numOutputs = 2;
		strcpy(data->outputDeviceName, "Unknown device");
	}

	data->framesPerBuffer = LoadConfigNum("Configuration",
			"Frames per buffer", GetProfileFullName(kProfileName),DEFAULT_BUFFER_SIZE);


	strcpy(data->soundConfigFile, LoadConfig("Configuration",
		"Sound Samples INI file", GetProfileFullName(kProfileName),DEFAULT_AUDIO_FILE) );
	
	DSP.setNumOutputs(data->numOutputs);

}

/* -----------------------------------------------------------------------------*/
void SaveState()
{
	TRACE(("SaveState\n"));
	DriverDataPtr data = GetData();
	
	//-- 1 - Save current values as default values
	SaveConfigNum("Configuration", "Frames per buffer",
			data->framesPerBuffer , GetProfileFullName(kProfileName));
	SaveConfigNum("Configuration", "Sample rate",
			data->sampleRate , GetProfileFullName(kProfileName));
	SaveConfig("Configuration", "Sound Samples INI file",
			data->soundConfigFile , GetProfileFullName(kProfileName));
	SaveConfigNum("Configuration", "Device Number",
			data->outputDevice , GetProfileFullName(kProfileName));
	SaveConfig("Configuration", "Device Name",
			data->outputDeviceName , GetProfileFullName(kProfileName));


}


/* -----------------------------------------------------------------------------*/
/* PortAudio Callback															*/
/* -----------------------------------------------------------------------------*/

int process (const void *inputBuffer, void *outputBuffer,
		unsigned long framesPerBuffer, const PaStreamCallbackTimeInfo* timeInfo,
		PaStreamCallbackFlags statusFlags, void *userData)
{
	int i, j;
	(void) inputBuffer;	/* Prevent "unused variable" warnings. */ 
	(void) timeInfo; 
	(void) statusFlags;
	
	DriverData *data = (DriverData*)userData;

	DSP.compute(framesPerBuffer, NULL, data->outCompute);

	// uninterlacing loop
	float *out = (float*)outputBuffer;
	for (i=0; i<framesPerBuffer; i++)
	{
		for (j=0; j<data->numOutputs; j++)
		{
			*out++ = data->outCompute[j][i];
		}
	}
	return 0;
}


/* -----------------------------------------------------------------------------*/
/* Audio functions definitions													*/
/* -----------------------------------------------------------------------------*/
int AudioWakeUp() 
{
	TRACE(("AudioWakeUp\n"));

	//-- 0 - All declarations must be before GOTO 
	DriverDataPtr data = GetData ();	// driver data structure
	PaError err;						// port audio error code
	PaStreamParameters outputParameters;
	int i;

	//-- 1 - Initialize PortAudio
	err = Pa_Initialize();
	if( err != paNoError )
		return err;
	TRACE(("Pa_Initialize\n"));

	//-- 2 - Load state
	LoadState();

	//-- 3 - Initialize sampler
	if (!DSP.init(data->soundConfigFile))
		return ERR_CONFIGFILE;

	//-- 4 - Allocate outCompute array
	for (i=0; i<data->numOutputs; i++)
	{
 		data->outCompute[i] = (float*) calloc(data->framesPerBuffer,sizeof(float));
	}
	TRACE(("Allocate outCompute\n"));

	//-- 5 - Open audio stream

	outputParameters.device = data->outputDevice;
	outputParameters.channelCount = data->numOutputs;
	outputParameters.sampleFormat = paFloat32;
	outputParameters.suggestedLatency =
		Pa_GetDeviceInfo( data->outputDevice )->defaultLowOutputLatency;
	outputParameters.hostApiSpecificStreamInfo = NULL;
	TRACE(("numOutputs=%d\n",data->numOutputs));
	TRACE(("channelCount=%d\n",Pa_GetDeviceInfo( data->outputDevice )->maxOutputChannels));
	err = Pa_OpenStream(
			&data->stream,				/* newly opened stream */
			NULL,						/* input parameters */
			&outputParameters,			/* output parameters */
			data->sampleRate,			/* sample rate */
			data->framesPerBuffer,		/* frames per buffer */
			paNoFlag,					/* flags */
			process,					/* callback function for processing and
							  	 			filling input and output buffers */
			data );						/* client pointer which is passed to the
							   				callback function */
	if ( err != paNoError )
		return err;
	TRACE(("Pa_OpenStream\n"));

	//-- 6 - Start audio stream
	err = Pa_StartStream(data->stream);
	if ( err != paNoError )
		return err;
	TRACE(("Pa_StartStream\n"));

	//-- 7 - Midi connect

	MidiConnect(MidiShareDrvRef, data->fRefNum, true);
	MidiConnect(data->fRefNum, MidiShareDrvRef, true);
	TRACE(("MidiConnect\n"));
	TRACE(("----------------\n"));

	return 0;
}


/* -----------------------------------------------------------------------------*/
int AudioSleep() 
{
	TRACE(("Audiosleep\n"));

	//-- 0 - All declarations must be before GOTO 
	int i;
	DriverDataPtr data = GetData ();
	PaError err;

	if (!data->asleep)
	{
		//-- 1 - Midi deconnect
		MidiConnect(MidiShareDrvRef, data->fRefNum, false);
		MidiConnect(data->fRefNum, MidiShareDrvRef, false);
		MidiFlushEvs (data->fRefNum);
		TRACE(("MidiDeconnect / MidiFlushEvs\n"));

		//-- 2 - Stop audio stream
		err = Pa_StopStream( data->stream );
		if( err != paNoError )
			return err;
		TRACE(("Pa_StopStream\n"));

		//-- 3 - Close audio stream
		err = Pa_CloseStream( data->stream );
		if( err != paNoError )
			return err;
		TRACE(("Pa_CloseStream\n"));

		//-- 4 - Terminate PortAudio
		Pa_Terminate();
		TRACE(("Pa_Terminate\n"));

		//-- 5 - Desallocate outCompute array
		for (i=0; i<data->numOutputs; i++)
		{
 			free(data->outCompute[i]);
		}
		TRACE(("Desallocate outCompute\n"));

		//-- 6 - Save state
		SaveState();
		data->asleep = true;
	}
	return 0;
}  

/* -----------------------------------------------------------------------------*/
/* JNI functions definitions													*/
/* -----------------------------------------------------------------------------*/

JNIEXPORT jint JNICALL Java_grame_elody_editor_sampler_PaJniConnect_AudioReload
	(JNIEnv *env, jclass cls, jint sampleRate, jint framesPerBuffer, 
	 jstring configFile, jint dev)
{
	TRACE(("JNI_AudioReload\n"));

	//-- 0 - All declarations must be before GOTO 
	DriverDataPtr data = GetData ();	// driver data structure
	const char *fileName;				// sound samples config file name
	PaError err;

	//-- 1 - Convert and get JNI parameters
	fileName = env->GetStringUTFChars(configFile, NULL);
	if (fileName == NULL) 
	{
		TRACE(("ERROR: GetStringUTFChars returned NULL\n"));
		return ERR_JNICONVERT; /* OutOfMemoryError already thrown */
	}

	strcpy(data->soundConfigFile, fileName);
	env->ReleaseStringUTFChars(configFile, fileName);

	data->sampleRate = (int) sampleRate;
	data->framesPerBuffer = (int) framesPerBuffer;
	data->outputDevice = (int) dev;
	strcpy(data->outputDeviceName, (Pa_GetDeviceInfo(data->outputDevice))->name);
	TRACE(("JNI parameters conversion\n"));

	//-- 2 - Audio sleep
	err = AudioSleep();
	if (err!=0)		return err;

	//-- 3 - Audio wake up
	err = AudioWakeUp();

	return err;
}

/* -----------------------------------------------------------------------------*/
JNIEXPORT jstring JNICALL Java_grame_elody_editor_sampler_PaJniConnect_GetCurrentFileName
  (JNIEnv *env, jclass cls)
{
	DriverDataPtr data = GetData ();
    return env->NewStringUTF(data->soundConfigFile);
}

/* -----------------------------------------------------------------------------*/
JNIEXPORT jstring JNICALL Java_grame_elody_editor_sampler_PaJniConnect_GetErrorText
  (JNIEnv *env, jclass cls, jint error)
{
	PaError err = (PaError) error;
	if (err==ERR_CONFIGFILE)
		return env->NewStringUTF("ERROR: Incorrect sound configuration file");
	else if (err==ERR_JNICONVERT)
		return env->NewStringUTF("ERROR: JNI types conversion failure");
	else if (err==0)
		return env->NewStringUTF("");
    else
	{
		char msg[256];
		strcpy (msg,"ERROR using PortAudio: ");
		strcat(msg, Pa_GetErrorText(err));
		return env->NewStringUTF(msg);
	}
}

/* -----------------------------------------------------------------------------*/
JNIEXPORT jint JNICALL Java_grame_elody_editor_sampler_PaJniConnect_GetCurrentSampleRate
  (JNIEnv *env, jclass cls)
{
	DriverDataPtr data = GetData ();
	return (jint) data->sampleRate;
}

/* -----------------------------------------------------------------------------*/
JNIEXPORT jint JNICALL Java_grame_elody_editor_sampler_PaJniConnect_GetCurrentFramesPerBuffer
  (JNIEnv *env, jclass cls)
{
	DriverDataPtr data = GetData ();
	return (jint) data->framesPerBuffer;
}

/* -----------------------------------------------------------------------------*/
JNIEXPORT jint JNICALL Java_grame_elody_editor_sampler_PaJniConnect_GetCurrentDeviceIndex
  (JNIEnv *env, jclass cls)
{
	DriverDataPtr data = GetData ();
	return (jint) data->outputDevice;
}

/* -----------------------------------------------------------------------------*/
JNIEXPORT jint JNICALL Java_grame_elody_editor_sampler_PaJniConnect_GetHostAPICount
  (JNIEnv *env, jclass cls)
{
	return (jint) Pa_GetHostApiCount();
}

/* -----------------------------------------------------------------------------*/
JNIEXPORT jboolean JNICALL Java_grame_elody_editor_sampler_PaJniConnect_SetParam
  (JNIEnv *env, jclass cls, jint chan, jint vol, jint pan, jdouble sens, jint a, jint d, jdouble s, jint r)
{
	if (DSP.setParam(chan, vol, pan, sens, a, d, s, r))
		return JNI_TRUE;
	else
		return JNI_FALSE;
}

/* -----------------------------------------------------------------------------*/
JNIEXPORT jint JNICALL Java_grame_elody_editor_sampler_PaJniConnect_hostApiDeviceIndexToDeviceIndex
  (JNIEnv *env, jclass cls, jint hostApi, jint hostApiDeviceIndex)
{
	return (jint) Pa_HostApiDeviceIndexToDeviceIndex((int) hostApi, (int) hostApiDeviceIndex);

}

/* -----------------------------------------------------------------------------*/
JNIEXPORT jboolean JNICALL Java_grame_elody_editor_sampler_PaJniConnect_testSampleRate
  (JNIEnv *env, jclass cls, jint sampleRate)
{
	DriverDataPtr data = GetData ();
	PaStreamParameters outputParameters;
	outputParameters.device = data->outputDevice;
	outputParameters.channelCount = data->numOutputs;
	outputParameters.sampleFormat = paFloat32;
	outputParameters.suggestedLatency =
		Pa_GetDeviceInfo( data->outputDevice )->defaultLowOutputLatency;
	outputParameters.hostApiSpecificStreamInfo = NULL;
	int err = Pa_IsFormatSupported( NULL, &outputParameters, (double) sampleRate);
	if (err==0)
		return JNI_TRUE;
	else
		return JNI_FALSE;
}

/* -----------------------------------------------------------------------------*/
JNIEXPORT jobjectArray JNICALL Java_grame_elody_editor_sampler_PaJniConnect_GetHostAPIInfos
	(JNIEnv *env, jclass cls, jint apiNumber)
{
	jobjectArray result;
	PaHostApiIndex i;
	const PaHostApiInfo * info;
	jclass PaHostApiInfoCls = env->FindClass("grame/elody/editor/sampler/PaHostApiInfo");
	if (PaHostApiInfoCls == NULL) return NULL; /* exception thrown */
	result = env->NewObjectArray((jsize) apiNumber, PaHostApiInfoCls, NULL);
	if (result == NULL) return NULL; /* out of memory error thrown */
	jmethodID constructorID = env->GetMethodID(PaHostApiInfoCls, "<init>", "(IILjava/lang/String;III)V");
	for (i=0; i<apiNumber; i++)
	{
		info = Pa_GetHostApiInfo(i);
		if ((info->defaultOutputDevice)>=0)
		{
			jobject jInfo = env->NewObject(PaHostApiInfoCls, constructorID, (jint) info->structVersion,
					(jint) info->type, env->NewStringUTF(info->name), (jint) info->deviceCount,
					(jint) info->defaultInputDevice, (jint) info->defaultOutputDevice);
			if (jInfo == NULL)
			{
				printf("jInfo == NULL at i=%d\n",i);
				return NULL; /* exception thrown */
			}
			env->SetObjectArrayElement(result, i, jInfo);
			env->DeleteLocalRef(jInfo);
		}
	}
	return result;
}

/* -----------------------------------------------------------------------------*/
JNIEXPORT jobjectArray JNICALL Java_grame_elody_editor_sampler_PaJniConnect_GetDeviceInfos
	(JNIEnv *env, jclass cls, jint apiIndex, jint drvNumber)
{
	jobjectArray result;
	const PaDeviceInfo * info;
	jclass PaDeviceInfoCls = env->FindClass("grame/elody/editor/sampler/PaDeviceInfo");
	if (PaDeviceInfoCls == NULL) return NULL; /* exception thrown */
	result = env->NewObjectArray((jsize) drvNumber, PaDeviceInfoCls, NULL);
	if (result == NULL) return NULL; /* out of memory error thrown */
	jmethodID constructorID = env->GetMethodID(PaDeviceInfoCls, "<init>", "(ILjava/lang/String;IIIDDDDD)V");
	for (int i=0; i<drvNumber; i++)
	{
		info = Pa_GetDeviceInfo( Pa_HostApiDeviceIndexToDeviceIndex( (PaHostApiIndex) apiIndex, i ) );
		if (((info->maxOutputChannels)>0)&&((info->defaultSampleRate)>0))
		{
			jobject jInfo = env->NewObject( PaDeviceInfoCls, constructorID,
					(jint) info->structVersion,	env->NewStringUTF(info->name), (jint) info->hostApi,
					(jint) info->maxInputChannels, (jint) info->maxOutputChannels,
					(jdouble) info->defaultLowInputLatency,	(jdouble) info->defaultLowOutputLatency,
					(jdouble) info->defaultHighInputLatency, (jdouble) info->defaultHighOutputLatency,
					(jdouble) info->defaultSampleRate );
			if (jInfo == NULL)
			{
				printf("jInfo == NULL at i=%d\n",i);
				return NULL; /* exception thrown */
			}
			env->SetObjectArrayElement(result, i, jInfo);
			env->DeleteLocalRef(jInfo);
		}
	}
	return result;
}

/* -----------------------------------------------------------------------------*/
Boolean Start() 
{
	TRACE(("Start\n"));

	//-- 0 - All declarations must be before GOTO 
	DriverDataPtr data = GetData ();
	TDriverInfos infos = {SamplerDriverName, kSamplerDriverVersion, 0};
	TDriverOperation op = DSP.getDriverOperation();

	//-- 1 - Register Driver
	data->fRefNum = MidiRegisterDriver(&infos, &op);
	DSP.setRefNum(data->fRefNum);
	if (data->fRefNum < 1)
	{
		printf("ERROR: Midishare is unable to register Sampler Driver\n");
		return false;
	}
	TRACE(("MidiRegisterDriver\n"));

	//-- 1 - Add and load slot
	MidiAddSlot(data->fRefNum, SamplerSlotName1, MidiOutputSlot);
	MidiAddSlot(data->fRefNum, SamplerSlotName2, MidiOutputSlot);
	TRACE(("MidiAddSlot\n"));
	LoadSlot("Output Slots", GetProfileFullName(kProfileName),SamplerDriverName,1);
	LoadSlot("Output Slots", GetProfileFullName(kProfileName),SamplerDriverName,2);
	TRACE(("LoadSlot\n"));

	return true;
}

/* -----------------------------------------------------------------------------*/
void Stop()
{
	TRACE(("Stop\n"));
	DriverDataPtr data = GetData ();

	//-- 1 - Audio sleep
	//AudioSleep(); <- called by msSleep on MidiUnregisterDriver !!!

	//-- 2 - Save slot
	SaveSlot("Output Slots", GetProfileFullName(kProfileName),SamplerDriverName, 1);
	SaveSlot("Output Slots", GetProfileFullName(kProfileName),SamplerDriverName, 2);
	TRACE(("SaveSlot\n"));
	//-- 2 - Unregister Driver
	MidiUnregisterDriver (data->fRefNum);
	TRACE(("MidiUnregisterDriver\n"));
}

/* -----------------------------------------------------------------------------*/
BOOL WINAPI DllEntryPoint(HINSTANCE  hinstDLL, DWORD fdwReason, LPVOID lpvReserved )
{
	if ( MidiGetVersion() < 184) return FALSE;
	switch (fdwReason) {
		case DLL_PROCESS_ATTACH:
			TRACE(("Dll Attach\n"));
			Start();
			break;
		case DLL_PROCESS_DETACH:
			TRACE(("Dll Detach\n"));
			Stop();
			break;
	 }
    return TRUE;
}

// intended only to avoid link error
main() {return 0;}
