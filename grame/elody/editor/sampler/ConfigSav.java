package grame.elody.editor.sampler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class ConfigSav {
	
	private File configFile;
	
	private ConfigList list = new ConfigList();

	public ConfigSav(File f)
	{
		configFile = f;
		rebuildList();
	}
	
	public ConfigList getList() { return list; }
	
	public void writeAll()
	{
		BufferedWriter w=null;
		try {
			w = new BufferedWriter(new FileWriter(configFile));
			w.write("#----------------------------------------------------------");
			w.newLine();
			w.write("#\t\t"+configFile.getName());
			w.newLine();
			w.write("#----------------------------------------------------------");
			w.newLine();
			for (int i=0; i<=list.maxChannels(); i++)
			{
				Integer ch = Integer.valueOf(i);
				if (list.isSet(ch))
				{
					w.newLine();
					w.write("#ch\tkey\tfilename\tspee\toutput\tpan\tvol\tattack\tdecay\tsustain\trelease\tsensit");
					w.newLine();
					for (int j=0; j<=list.maxKeygroups(ch); j++)
					{
						if (list.getKeyg(ch, j)!=null)
						{
							int ref = list.getRef(ch, j);
							int minus = list.getMinus(ch, j);
							int plus = list.getPlus(ch, j);
							File f = list.getFile(ch, j);
							int output = list.getOutput(ch, j)-1;
							int pan = list.getPan(ch, j);
							int vol = list.getVol(ch, j);
							int attack = list.getAttack(ch, j);
							int decay = list.getDecay(ch, j);
							double sustain = list.getSustain(ch, j);
							int release = list.getRelease(ch, j);
							double sensit = list.getSensit(ch, j);
							for (int k=-minus; k<=plus; k++)
							{
								w.newLine();
								double speed = Math.pow(2, k/12.0);
								int key = ref+k;
								w.write((ch.intValue()-1)+"\t"+key+"\t"+f.getPath()+"\t"+speed+"\t"+output+"\t"+
										pan+"\t"+vol+"\t"+attack+"\t"+decay+"\t"+sustain+"\t"+release+"\t"+sensit);
							}
						}
					}
					w.newLine();
				}
			}
			w.flush();
			w.close();
		} catch (IOException e) {}
	}
	
	public void rebuildList()
	{
		/* Built the image of the config file by parsing it
		 * line after line.  The keygroups are reconstituted
		 * with the indication speed: a keygroup must 
		 * contain one note with speed==1, otherwise is the
		 * reverse processing not guaranteed. For this reason,
		 * if the config file is manually modified, it should be
		 * done carefully. */
		
		/* Construit l'image du fichier de configuration en
		 * le parcourant ligne après ligne.  Les keygroups sont
		 * reconstitués grâce à l'indication de vitesse :
		 * un keygroup doit obligatoirement contenir une note
		 * de vitesse 1, sinon le bon fonctionnement du reverse
		 * processing n'est pas garanti. Pour cette raison, 
		 * si le fichier de configuration est modifié manuellement,
		 * cela doit être fait avec prudence. */
		
		BufferedReader r=null;
		try {
			r = new BufferedReader(new FileReader(configFile));
		} catch (FileNotFoundException e1) {}
		String line;
		try {
			double prevSpeed=1;		int prevKey=-1;			int minPitch=-1; 	int refPitch=-1;		int maxPitch=-1;
			int index=0;			int output=1;			int pan=64;			int vol=100;			int attack=0;
			int decay=0;			double sustain=0.0;		int release=300;	double sensit=0.0;
			Integer channel = null;
			Integer prevChannel = null;
			File file = null;
			File prevFile = null;
			while ((line=r.readLine())!=null)
			{
				if ((line.length()==0)||(line.charAt(0)=='#'))
					continue;
				else
				{
					StringTokenizer st = new StringTokenizer(line," \t");
				    channel = Integer.valueOf(Integer.valueOf(st.nextToken()).intValue()+1);
				    int key = Integer.parseInt(st.nextToken());
				    file = new File(st.nextToken());
				    double speed = Double.parseDouble(st.nextToken());
				    output = Integer.parseInt(st.nextToken())+1;
				    pan = Integer.parseInt(st.nextToken());
				    vol = Integer.parseInt(st.nextToken());
				    attack = Integer.parseInt(st.nextToken());
				    decay = Integer.parseInt(st.nextToken());
				    sustain = Double.parseDouble(st.nextToken());
				    release = Integer.parseInt(st.nextToken());
				    sensit = Double.parseDouble(st.nextToken());
				    
				    if (speed<=prevSpeed)
				    {  
				    	if ((maxPitch=prevKey)!=-1)
				    	{
				    		list.addKeygroup(prevChannel, index, prevFile, refPitch, maxPitch-refPitch, refPitch-minPitch, output,
				    				pan, vol, attack, decay, sustain, release, sensit);
				    		index++;
				    	}
				       	minPitch=key;
				    }
				    if (speed==1) { refPitch=key; }
				    
				    if ((prevChannel!=null)&&(channel.intValue()>prevChannel.intValue())) { index=0; }
				    prevChannel=channel;
				    prevFile=file;
				    prevSpeed=speed;
				    prevKey=key;
				}
			}
			if ((maxPitch=prevKey)!=-1)
				list.addKeygroup(channel, index, file, refPitch, maxPitch-refPitch, refPitch-minPitch, output,
						pan, vol, attack, decay, sustain, release, sensit);
			r.close();
		} catch (IOException e) {}
		
	}

	public boolean isSet(int ch) { return list.isSet(Integer.valueOf(ch)); }
	public int maxKeygroups(int ch) {return list.maxKeygroups(Integer.valueOf(ch)); }
	public void addKeygroup(Integer channel, int index, File file, int ref, int plus, int minus, int output,
			int pan, int vol, int attack, int decay, double sustain, int release, double sensit)
	{
		list.addKeygroup(channel, index, file, ref, plus, minus, output, pan, vol, attack, decay, sustain, release, sensit);
	}
	public void delKeygroup(Integer channel, int index)
	{
		list.delKeygroup(channel, index);
	}
	public File getFile(int ch, int keygIndex) {return list.getFile(Integer.valueOf(ch), keygIndex); }
	public int getRef(int ch, int keygIndex) {return list.getRef(Integer.valueOf(ch), keygIndex); }
	public int getPlus(int ch, int keygIndex) {return list.getPlus(Integer.valueOf(ch), keygIndex); }
	public int getMinus(int ch, int keygIndex) {return list.getMinus(Integer.valueOf(ch), keygIndex); }
	public int getOutput(int ch, int keygIndex) {return list.getOutput(Integer.valueOf(ch), keygIndex); }
	public int getVol(int ch, int keygIndex) {return list.getVol(Integer.valueOf(ch), keygIndex); }
	public int getPan(int ch, int keygIndex) {return list.getPan(Integer.valueOf(ch), keygIndex); }
	public int getAttack(int ch, int keygIndex) {return list.getAttack(Integer.valueOf(ch), keygIndex); }
	public int getDecay(int ch, int keygIndex) {return list.getDecay(Integer.valueOf(ch), keygIndex); }
	public double getSustain(int ch, int keygIndex) {return list.getSustain(Integer.valueOf(ch), keygIndex); }
	public int getRelease(int ch, int keygIndex) {return list.getRelease(Integer.valueOf(ch), keygIndex); }
	public double getSensit(int ch, int keygIndex) {return list.getSensit(Integer.valueOf(ch), keygIndex); }
	public void setFile(File f, int ch, int keygIndex) {list.setFile(f, Integer.valueOf(ch), keygIndex); }
	public void setRef(int r, int ch, int keygIndex) {list.setRef(r, Integer.valueOf(ch), keygIndex); }
	public void setPlus(int p, int ch, int keygIndex) {list.setPlus(p, Integer.valueOf(ch), keygIndex); }
	public void setMinus(int m, int ch, int keygIndex) {list.setMinus(m, Integer.valueOf(ch), keygIndex); }
	public void setOutput(int o, int ch, int keygIndex) {list.setOutput(o, Integer.valueOf(ch), keygIndex); }
	public void setVol(int v, int ch, int keygIndex) {list.setVol(v, Integer.valueOf(ch), keygIndex); }
	public void setPan(int p, int ch, int keygIndex) {list.setPan(p, Integer.valueOf(ch), keygIndex); }
	public void setAttack(int a, int ch, int keygIndex) {list.setAttack(a, Integer.valueOf(ch), keygIndex); }
	public void setDecay(int d, int ch, int keygIndex) {list.setDecay(d, Integer.valueOf(ch), keygIndex); }
	public void setSustain(double s, int ch, int keygIndex) {list.setSustain(s, Integer.valueOf(ch), keygIndex); }
	public void setRelease(int r, int ch, int keygIndex) {list.setRelease(r, Integer.valueOf(ch), keygIndex); }
	public void setSensit(double s, int ch, int keygIndex) {list.setSensit(s, Integer.valueOf(ch), keygIndex); }
}

class ConfigList  {
	
	private TreeMap map;

	public ConfigList() { map = new TreeMap();	}
	
	public void addKeygroup(Integer channel, int index, File file, int ref, int plus, int minus, int output,
			int pan, int vol, int attack, int decay, double sustain, int release, double sensit)
	{
		TreeMap k = addChannel(channel);
		k.put(Integer.valueOf(index), new Keyg(file, ref, plus, minus,
				output, pan, vol, attack, decay, sustain, release, sensit));
	}
	public void delKeygroup(Integer channel, int index)
	{
		TreeMap k = (TreeMap) map.get(channel);
		k.remove(Integer.valueOf(index));
		if (k.isEmpty())
			map.remove(channel);
	}
	
	public TreeMap addChannel(Integer ch)
	{
		if (!isSet(ch))
			map.put(ch, new TreeMap());
		return (TreeMap) map.get(ch);
	}
	
	public TreeMap getMap() { return map; }
	
	public boolean isSet(Integer ch) {
		return (map.containsKey(ch));
		}
	
	public int maxChannels()
	{
		if (map.isEmpty())	return -1;
		Integer result = (Integer) map.lastKey();
		return result.intValue();
	}
	
	public int maxKeygroups(Integer ch)
	{
		TreeMap k = (TreeMap) map.get(ch);
		if (k.isEmpty())	return -1;
		Integer result = (Integer) k.lastKey();
		return result.intValue();
	}
	
	public Keyg getKeyg(Integer ch, int keygIndex)
	{
		TreeMap k = null;
		if (isSet(ch))
		{
			k = (TreeMap) map.get(ch);
			if (k.containsKey(Integer.valueOf(keygIndex)))
				return (Keyg) k.get(Integer.valueOf(keygIndex));		
		}
		return (Keyg) null;
	}
	
	public File getFile(Integer ch, int keygIndex) { return getKeyg(ch,keygIndex).getFile(); }
	public int getRef(Integer ch, int keygIndex) { return getKeyg(ch,keygIndex).getRef(); }
	public int getPlus(Integer ch, int keygIndex) { return getKeyg(ch,keygIndex).getPlus(); }
	public int getMinus(Integer ch, int keygIndex) { return getKeyg(ch,keygIndex).getMinus(); }
	public int getOutput(Integer ch, int keygIndex) { return getKeyg(ch,keygIndex).getOutput(); }
	public int getPan(Integer ch, int keygIndex) { return getKeyg(ch,keygIndex).getPan(); }
	public int getVol(Integer ch, int keygIndex) { return getKeyg(ch,keygIndex).getVol(); }
	public int getAttack(Integer ch, int keygIndex) { return getKeyg(ch,keygIndex).getAttack(); }
	public int getDecay(Integer ch, int keygIndex) { return getKeyg(ch,keygIndex).getDecay(); }
	public double getSustain(Integer ch, int keygIndex) { return getKeyg(ch,keygIndex).getSustain(); }
	public int getRelease(Integer ch, int keygIndex) { return getKeyg(ch,keygIndex).getRelease(); }
	public double getSensit(Integer ch, int keygIndex) { return getKeyg(ch,keygIndex).getSensit(); }
	public void setFile(File f, Integer ch, int keygIndex) { getKeyg(ch,keygIndex).setFile(f); }
	public void setRef(int r, Integer ch, int keygIndex) { getKeyg(ch,keygIndex).setRef(r); }
	public void setPlus(int p, Integer ch, int keygIndex) { getKeyg(ch,keygIndex).setPlus(p); }
	public void setMinus(int m, Integer ch, int keygIndex) { getKeyg(ch,keygIndex).setMinus(m); }
	public void setOutput(int o, Integer ch, int keygIndex) { getKeyg(ch,keygIndex).setOutput(o); }
	public void setPan(int p, Integer ch, int keygIndex) { getKeyg(ch,keygIndex).setPan(p); }
	public void setVol(int v, Integer ch, int keygIndex) { getKeyg(ch,keygIndex).setVol(v); }
	public void setAttack(int a, Integer ch, int keygIndex) { getKeyg(ch,keygIndex).setAttack(a); }
	public void setDecay(int d, Integer ch, int keygIndex) { getKeyg(ch,keygIndex).setDecay(d); }
	public void setSustain(double s, Integer ch, int keygIndex) { getKeyg(ch,keygIndex).setSustain(s); }
	public void setRelease(int r, Integer ch, int keygIndex) { getKeyg(ch,keygIndex).setRelease(r); }
	public void setSensit(double s, Integer ch, int keygIndex) { getKeyg(ch,keygIndex).setSensit(s); }
}

class Keyg {
	private File file;
	private int ref, plus, minus, output, pan, vol, attack, decay, release;
	private double sustain, sensit;
	public Keyg(File file, int ref, int plus, int minus, int output,
			int pan, int vol, int attack, int decay, double sustain, int release, double sensit) {
		this.file = file;
		this.ref = ref;
		this.plus = plus;
		this.minus = minus;
		this.output = output;
		this.pan = pan;
		this.vol = vol;
		this.attack = attack;
		this.decay = decay;
		this.sustain = sustain;
		this.release = release;
		this.sensit = sensit;
	}
	public File getFile() { return file; }
	public int getMinus() { return minus; }
	public int getPlus() { return plus; }
	public int getRef() { return ref; }
	public int getOutput() { return output; }
	public int getPan() { return pan; }
	public int getVol() { return vol; }
	public int getAttack() { return attack; }
	public int getDecay() { return decay; }
	public double getSustain() { return sustain; }
	public int getRelease() { return release; }
	public double getSensit() { return sensit; }
	public void setFile(File f) { file=f; }
	public void setMinus(int m) { minus=m; }
	public void setPlus(int p) { plus=p; }
	public void setRef(int r) { ref=r; }
	public void setOutput(int o) { output=o; }
	public void setPan(int p) { pan=p; }
	public void setVol(int v) { vol=v; }
	public void setAttack(int a) { attack=a; }
	public void setDecay(int d) { decay=d; }
	public void setSustain(double s) { sustain=s; }
	public void setRelease(int r) { release=r; }
	public void setSensit(double s) { sensit=s; }
}

class Comparateur implements Comparator {
	  public int compare(Object obj1, Object obj2){
	    return ((Comparable)obj2).compareTo(obj1);
	  }
	  public boolean equals(Object obj){
	    return this.equals(obj);
	  }
	}