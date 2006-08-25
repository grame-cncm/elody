package grame.elody.editor.sampler;

import java.util.Vector;

public class PaHostApiInfo 	{
/***** DESCRIPTION ************************************
 * Instances of this class represent the structure of
 * an audio API such as it is returned by PortAudio API.
 ******************************************************/		
	private int apiIndex;
	private int structVersion;
    private int type;
    private String name;
    private int deviceCount;
    private int defaultInputDevice;
    private int defaultOutputDevice;
    private Vector<PaDeviceInfo> devList;

	public PaHostApiInfo(int structVersion, int type, String name,
			int deviceCount, int defaultInputDevice, int defaultOutputDevice)
	{
		this.structVersion = structVersion;
		this.type = type;
		this.name = name;
		this.deviceCount = deviceCount;
		this.defaultInputDevice = defaultInputDevice;
		this.defaultOutputDevice = defaultOutputDevice;
	}

	public int getApiIndex() { return apiIndex; }
	public int getStructVersion() { return structVersion; }
	public int getType() { return type; }
	public String getName() { return name; }
	public int getDeviceCount() { return deviceCount; }
	public int getDefaultInputDevice() { return defaultInputDevice;	}
	public int getDefaultOutputDevice() { return defaultOutputDevice; }
	public Vector<PaDeviceInfo> getDevList() { return devList; }

	public void setDevList(Vector<PaDeviceInfo> v) { devList = v; }

	public void printAll(int n)
	{
		System.out.println();
		System.out.println("***** API no. "+n+" *****");
		System.out.println("structVersion="+structVersion);
		System.out.println("type="+type);
		System.out.println("name="+name);
		System.out.println("deviceCount="+deviceCount);
		System.out.println("defaultInputDevice="+defaultInputDevice);
		System.out.println("defaultOutputDevice="+defaultOutputDevice);
	}

	public void setApiIndex(int i) { apiIndex = i; }
}