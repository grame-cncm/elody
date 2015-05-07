/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.editor.sampler;

import java.io.FileWriter;
import java.io.IOException;
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

	public void printAll(int n, FileWriter fw)
	{
		try {
			fw.write("\r\n***** API no. "+n+" *****");
			fw.write("\r\nstructVersion="+structVersion);
			fw.write("\r\ntype="+type);
			fw.write("\r\nname="+name);
			fw.write("\r\ndeviceCount="+deviceCount);
			fw.write("\r\ndefaultInputDevice="+defaultInputDevice);
			fw.write("\r\ndefaultOutputDevice="+defaultOutputDevice);
			fw.write("\r\n");
		} catch (IOException e) {}

	}

	public void setApiIndex(int i) { apiIndex = i; }
}