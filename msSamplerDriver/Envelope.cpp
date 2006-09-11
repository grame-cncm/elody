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

#include "Envelope.h"
#include <math.h>

#define OFF		0
#define ATTACK	1
#define DECAY	2
#define SUSTAIN	3
#define RELEASE	4
#define END		5

static int checkValue(int value, int min, int max)
{
	if (value<min)
		return min;
	if (value>max)
		return max;
	return value;
}

static double checkValue(double value, double min, double max)
{
	if (value<min)
		return min;
	if (value>max)
		return max;
	return value;
}

Envelope::Envelope() 
{}

Envelope::~Envelope()
{}

void Envelope::setEnvelope(const Envelope& e)
{
	setEnvelope(e.attack, e.decay, e.s_dB, e.release, e.sampleRate);
}

void Envelope::setEnvelope(int a, int d, double s, int r, int sR)
{
	value = 0.0;						
    state = OFF;
	sampleRate = sR;					// samples per seconds
	s_dB = checkValue(s,-30.0,0.0);

	attack = checkValue(a,0,15000);		// milliseconds
	decay = checkValue(d,0,25000);		// milliseconds
	sustain = exp( s_dB * log(10.0) / 10.0 );
	release = checkValue(r,0,25000);	// milliseconds

	target[OFF] = 0.0;
	rate[OFF] = 0.0;
	target[ATTACK] = 1.0;
	if ( attack != 0 )
		rate[ATTACK] = 1000.0 / (sampleRate * attack ) ;
	else
		rate[ATTACK] = 1.0;
	target[DECAY] = sustain;
	if ( decay != 0 )
		rate[DECAY] = - 1000.0 / (sampleRate * decay ) ;
	else
		rate[DECAY] = -1.0;
	target[SUSTAIN] = sustain;
	rate[SUSTAIN] = 0.0;
	target[RELEASE] = 0.0;
	if ( release != 0 )
	//	rate[RELEASE] = - 1000.0 / (sampleRate * release ) ;
		rate[RELEASE] = pow (10 , -2 * 1000.0 / (sampleRate * release ));
	else
		rate[RELEASE] = -1.0 ;
}


void Envelope::keyOn()
{
	state = ATTACK;
}

void Envelope::keyOff()
{
	state = RELEASE;
}

double Envelope::tick()
{
	if ((state!=OFF)&&(state!=SUSTAIN)) {
		if (state == RELEASE) {
			value *= rate[state];
			if (value <= target[state] + 0.01) {
				value = target[state];
				state++;
			}
		} else {
			if (target[state] > value) {
				value += rate[state];
				if (value >= target[state]) {
					value = target[state];
					state++;
				}
			} else {
				value += rate[state];
				if (value <= target[state]) {
					value = target[state];
					state++;
				}
			}
		}
    }
	if (state == END) state=OFF;
	return value;
}

double Envelope::lastOut()
{
    return value;
}

