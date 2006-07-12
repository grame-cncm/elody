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


