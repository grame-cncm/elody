package grame.elody.editor.sampler;

import java.util.Vector;

public class PaJniConnect {
/***** DESCRIPTION ************************************
 * This class provides a bridge between the Elody Sampler
 * Editor and the MidiShare SamplerDriver, using Java Native
 * Interface (JNI). It loads statically the associated library.
 * Each instance of this class keeps a list with every
 * audio Host API, and then every audio output device.
 * It provides methods to question the MidiShare SamplerDriver concerning
 * its configuration and get informations like current sample rate,
 * frames per buffer. It also provides to reset and reload the driver.
 ******************************************************/	
	private native static int GetCurrentSampleRate();
	private native static int GetCurrentFramesPerBuffer();
	private native static int GetCurrentDeviceIndex();
	private native static String GetCurrentFileName();
	private native static int GetHostAPICount();
	private native static PaHostApiInfo[] GetHostAPIInfos(int apiNumber);
	private native static PaDeviceInfo[] GetDeviceInfos(int apiIndex, int devNumber);
	private native static int AudioReload(int sampleRate, int framesPerBuffer, String configFile, int devIndex);
	private native static int hostApiDeviceIndexToDeviceIndex(int hostApi, int hostApiDeviceIndex);
	public native static String GetErrorText(int err);
	private native static boolean testSampleRate(int sampleRate);
	
	private Vector apiList;
	
	public PaJniConnect()
	{
		int apiCount = GetHostAPICount();
		PaHostApiInfo apiArray[] = new PaHostApiInfo[apiCount];
		apiArray = GetHostAPIInfos(apiCount);
		apiList = new Vector();
		for (int i=0; i<apiCount; i++)
		{
			if (apiArray[i]!=null)
			{
				apiArray[i].setApiIndex(i);
				setDevList(apiArray[i]);
				apiList.add(apiArray[i]);
			}
		}
	}
	public Vector getApiList()		{ return apiList; }
	public String GetFileName()		{ return GetCurrentFileName(); }
	public int GetSampleRate()		{ return GetCurrentSampleRate(); }
	public int GetFramesPerBuffer()	{ return GetCurrentFramesPerBuffer(); }
	public PaDeviceInfo GetDevice()	{ return findByDevIndex(GetCurrentDeviceIndex()); }
	
	public int paDriverReload(int sampleRate, int framesPerBuffer, String configFile, int devIndex)
	// stop and restart the driver: causes a new reading of the config file 
	{
		return AudioReload(sampleRate, framesPerBuffer, configFile, devIndex);
	}
	
	public void setDevList(PaHostApiInfo api)
	// add a devices list to an audio Host API structure
	{
		Vector v = new Vector();
		PaDeviceInfo devArray[] = new PaDeviceInfo[api.getDeviceCount()];
		devArray = GetDeviceInfos(api.getApiIndex(), api.getDeviceCount());
		for (int i=0; i<api.getDeviceCount(); i++)
		{
			if (devArray[i]!=null)
			{
				devArray[i].setDevIndex(hostApiDeviceIndexToDeviceIndex(api.getApiIndex(),i));
				v.add(devArray[i]);
			}
		}
		api.setDevList(v);
	}
	
	public PaDeviceInfo findByDevIndex(int index)
	// find a device by searching through every audio Host API
	{
		for (int i=0; i<apiList.size(); i++)
		{
			PaHostApiInfo p = (PaHostApiInfo) apiList.get(i);
			for (int j=0; j<p.getDevList().size(); j++)
			{
				PaDeviceInfo d = (PaDeviceInfo) p.getDevList().get(j);
				if (d.getDevIndex()==index)
					return d;
			}
		}
		return null;
	}
	
	public Vector getAvailableSampleRates(int[] sampleArray)
	{
		Vector result = new Vector();
		for (int i=0; i<sampleArray.length; i++)
		{
			if (testSampleRate(sampleArray[i]))
				result.add(Integer.valueOf(sampleArray[i]));
				
		}
		return result;
	}
	
	static
	{
		System.loadLibrary("msSamplerDriver");
		System.loadLibrary("Player32");
		System.loadLibrary("JMidi");
		System.loadLibrary("JPlayer1");
	}
}
