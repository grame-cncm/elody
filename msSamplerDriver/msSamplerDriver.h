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

#include <jni.h>

#ifndef __msSamplerDriver__
#define __msSamplerDriver__

#define kSamplerDriverVersion      100
#define SamplerDriverName	"Sampler Driver"

Boolean SetUpMidi();
int AudioWakeUp();
int AudioSleep();

void CloseMidi();

#endif

#ifndef _Included_grame_elody_editor_sampler_PaJniConnect
#define _Included_grame_elody_editor_sampler_PaJniConnect
#ifdef __cplusplus
extern "C" {
#endif

/*
 * Class:     grame_elody_editor_sampler_PaJniConnect
 * Method:    GetCurrentFileName
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_grame_elody_editor_sampler_PaJniConnect_GetCurrentFileName
  (JNIEnv *, jclass);

/*
 * Class:     grame_elody_editor_sampler_PaJniConnect
 * Method:    GetErrorText
 * Signature: (I)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_grame_elody_editor_sampler_PaJniConnect_GetErrorText
  (JNIEnv *, jclass, jint);

/*
 * Class:     grame_elody_editor_sampler_PaJniConnect
 * Method:    GetCurrentSampleRate
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_grame_elody_editor_sampler_PaJniConnect_GetCurrentSampleRate
  (JNIEnv *, jclass);

/*
 * Class:     grame_elody_editor_sampler_PaJniConnect
 * Method:    GetCurrentFramesPerBuffer
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_grame_elody_editor_sampler_PaJniConnect_GetCurrentFramesPerBuffer
  (JNIEnv *, jclass);

/*
 * Class:     grame_elody_editor_sampler_PaJniConnect
 * Method:    GetCurrentDeviceIndex
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_grame_elody_editor_sampler_PaJniConnect_GetCurrentDeviceIndex
  (JNIEnv *, jclass);

/*
 * Class:     grame_elody_editor_sampler_PaJniConnect
 * Method:    GetHostAPICount
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_grame_elody_editor_sampler_PaJniConnect_GetHostAPICount
  (JNIEnv *, jclass);

/*
 * Class:     grame_elody_editor_sampler_PaJniConnect
 * Method:    SetParam
 * Signature: (IIIDIIDI)Z
 */
JNIEXPORT jboolean JNICALL Java_grame_elody_editor_sampler_PaJniConnect_SetParam
  (JNIEnv *, jclass, jint, jint, jint, jdouble, jint, jint, jdouble, jint);

/*
 * Class:     grame_elody_editor_sampler_PaJniConnect
 * Method:    GetHostAPIInfos
 * Signature: (I)[Lgrame/elody/editor/sampler/PaHostApiInfo;
 */
JNIEXPORT jobjectArray JNICALL Java_grame_elody_editor_sampler_PaJniConnect_GetHostAPIInfos
  (JNIEnv *, jclass, jint);

/*
 * Class:     grame_elody_editor_sampler_PaJniConnect
 * Method:    GetDeviceInfos
 * Signature: (II)[Lgrame/elody/editor/sampler/PaDeviceInfo;
 */
JNIEXPORT jobjectArray JNICALL Java_grame_elody_editor_sampler_PaJniConnect_GetDeviceInfos
  (JNIEnv *, jclass, jint, jint);

/*
 * Class:     grame_elody_editor_sampler_PaJniConnect
 * Method:    AudioReload
 * Signature: (ILjava/lang/String;II)I
 */
JNIEXPORT jint JNICALL Java_grame_elody_editor_sampler_PaJniConnect_AudioReload
  (JNIEnv *, jclass, jint, jint, jstring, jint);

/*
 * Class:     grame_elody_editor_sampler_PaJniConnect
 * Method:    hostApiDeviceIndexToDeviceIndex
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_grame_elody_editor_sampler_PaJniConnect_hostApiDeviceIndexToDeviceIndex
  (JNIEnv *, jclass, jint, jint);

/*
 * Class:     grame_elody_editor_sampler_PaJniConnect
 * Method:    testSampleRate
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_grame_elody_editor_sampler_PaJniConnect_testSampleRate
  (JNIEnv *env, jclass cls, jint sampleRate);

#ifdef __cplusplus
}
#endif
#endif
