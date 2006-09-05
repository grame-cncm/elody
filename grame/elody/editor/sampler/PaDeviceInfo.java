package grame.elody.editor.sampler;

import java.io.FileWriter;
import java.io.IOException;


public class PaDeviceInfo	{
/***** DESCRIPTION ************************************
 * Instances of this class represent the structure of a
 * device such as it is returned by PortAudio API.
 ******************************************************/		
	private int devIndex;
	private int structVersion;
	private String name;
	private int hostApi;
	private int maxInputChannels;
	private int maxOutputChannels;
	private double defaultLowInputLatency;
	private double defaultLowOutputLatency;
	private double defaultHighInputLatency;
	private double defaultHighOutputLatency;
	private int defaultSampleRate;

	public PaDeviceInfo(int structVersion, String name, int hostApi,
			int maxInputChannels, int maxOutputChannels,
			double defaultLowInputLatency, double defaultLowOutputLatency,
			double defaultHighInputLatency, double defaultHighOutputLatency,
			double defaultSampleRate)
	{
		this.structVersion = structVersion;
		this.name = name;
		this.hostApi = hostApi;
		this.maxInputChannels = maxInputChannels;
		this.maxOutputChannels = maxOutputChannels;
		this.defaultLowInputLatency = defaultLowInputLatency;
		this.defaultLowOutputLatency = defaultLowOutputLatency;
		this.defaultHighInputLatency = defaultHighInputLatency;
		this.defaultHighOutputLatency = defaultHighOutputLatency;
		this.defaultSampleRate = (int) defaultSampleRate;
	}

	public int getDevIndex() { return devIndex; }
	public int getStructVersion() { return structVersion; }
	public String getName() { return name; }
	public int getHostApi() { return hostApi; }
	public int getMaxInputChannels() { return maxInputChannels; }
	public int getMaxOutputChannels() { return maxOutputChannels; }
	public double getDefaultLowInputLatency() { return defaultLowInputLatency; }
	public double getDefaultLowOutputLatency() { return defaultLowOutputLatency; }
	public double getDefaultHighInputLatency() { return defaultHighInputLatency; }
	public double getDefaultHighOutputLatency() { return defaultHighOutputLatency; }
	public int getDefaultSampleRate() { return defaultSampleRate; }

	public void printAll(int n, FileWriter fw)
	{
		try {
			fw.write("\r\n***** DEVICE no. "+n+" *****");
			fw.write("\r\nstructVersion="+structVersion);
			fw.write("\r\nname="+name);
			fw.write("\r\nhostApi="+hostApi);
			fw.write("\r\nmaxInputChannels="+maxInputChannels);
			fw.write("\r\nmaxOutputChannels="+maxOutputChannels);
			fw.write("\r\ndefaultLowInputLatency="+defaultLowInputLatency);
			fw.write("\r\ndefaultLowOutputLatency="+defaultLowOutputLatency);
			fw.write("\r\ndefaultHighInputLatency="+defaultHighInputLatency);
			fw.write("\r\ndefaultHighOutputLatency="+defaultHighOutputLatency);
			fw.write("\r\ndefaultSampleRate="+defaultSampleRate);
			fw.write("\r\n");
		} catch (IOException e) {}
	}

	public void setDevIndex(int i) { devIndex = i;}
}