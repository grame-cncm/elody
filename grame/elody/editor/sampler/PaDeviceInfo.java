package grame.elody.editor.sampler;


public class PaDeviceInfo	{
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

	public void printAll(int n)
	{
		System.out.println();
		System.out.println("***** DEVICE no. "+n+" *****");
		System.out.println("structVersion="+structVersion);
		System.out.println("name="+name);
		System.out.println("hostApi="+hostApi);
		System.out.println("maxInputChannels="+maxInputChannels);
		System.out.println("maxOutputChannels="+maxOutputChannels);
		System.out.println("defaultLowInputLatency="+defaultLowInputLatency);
		System.out.println("defaultLowOutputLatency="+defaultLowOutputLatency);
		System.out.println("defaultHighInputLatency="+defaultHighInputLatency);
		System.out.println("defaultHighOutputLatency="+defaultHighOutputLatency);
		System.out.println("defaultSampleRate="+defaultSampleRate);
	}

	public void setDevIndex(int i) { devIndex = i;}
}