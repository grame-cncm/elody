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


#if !defined(__Envelope_h)
#define __Envelope_h

class Envelope
{
    protected:
        double value;
        double target[5];
        double rate[5];
        int state;
		int attack;
		int decay;
		double s_dB;
		double sustain;
		int release;
		int sampleRate;
		
    public:
        Envelope();
        virtual ~Envelope();
		void setEnvelope(const Envelope&);
		void setEnvelope(int, int, double, int, int);
        void keyOn();
        void keyOff();
        double tick();
        double lastOut();
};

#endif


