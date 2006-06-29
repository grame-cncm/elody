package grame.elody.editor.sampler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

import com.swtdesigner.SWTResourceManager;

public class Keygroup {

	private int index, ref, plus, minus, output;
	private File file;
	private Group group;
	private Channel channel;
	
	private Text fileText;
	private Spinner refSpinner;
	private Label noteLabel;
	private Spinner plusSpinner;
	private Spinner minusSpinner;
	

	public int getIndex()		{ return index; }
	public int getRef()			{ return ref; }
	public int getPlus()		{ return plus; }
	public int getMinus()		{ return minus; }
	public int getOutput()		{ return output; }
	public Group getGroup()		{ return group; }

	
	public Keygroup(int index, Composite parent, Control relative, Color bgColor, Channel channel, boolean sav) {
		this.index = index;
		this.channel = channel;
		setDefaultValues();
		groupCreate(parent, relative, bgColor);
		if (sav) {channel.sampler.configSav.addKeygroup(Integer.valueOf(channel.getNum()), index, (File)  null, ref, plus, minus, channel.getOutput()); }
	}
	
	public void delKeygroup()
	{
		group.dispose();
		channel.delKeygroup(this);
		channel.keyboardRefresh();
	}
	
	private void setDefaultValues() {
		ref = getDefaultRef();
		channel.setAvailKeyb(ref,false);
		plus = 0;
		minus = 0;
	}
	
	public void setFile(File f)
	{
		List homelist = getPathList(new File("."));
		List filelist = getPathList(f);
		String relativePath = matchPathLists(homelist,filelist);
	
		file = new File(relativePath);
		fileText.setText(file.getName());
		channel.sampler.configSav.setFile(file, channel.getNum(), index);
		channel.sampler.needToReset=true;
	}
	
	public boolean setRef(int pitch)
	{
		if (((pitch+plus)>127)||((pitch-minus)<0))
			return false;
		for (int i=ref-minus; i<=ref+plus; i++)
		{
			channel.setAvailKeyb(i, true);
		}
		for (int i=pitch-minus; i<=pitch+plus; i++)
		{
			if (channel.getAvailKeyb(i)) { continue; }
			for (int j=ref-minus; j<=ref+plus; j++)
			{
				channel.setAvailKeyb(j, false);
			}
			return false;
		}
		ref = pitch;
		for (int i=ref-minus; i<=ref+plus; i++)
		{
			channel.setAvailKeyb(i, false);
		}
		refSpinner.setSelection(ref);
		noteLabel.setText(convertPitch(ref));
		channel.keyboardRefresh();
		channel.sampler.configSav.setRef(pitch, channel.getNum(), index);
		channel.sampler.needToReset=true;
		return true;
	}
	
	public boolean setPlus(int p)
	{
		if ((ref+p)>127)
			return false;
		if (p>plus)
		{
			for (int i=ref+plus+1; i<=ref+p; i++)
			{
				if (channel.getAvailKeyb(i)) { continue; }
				return false;
			}
			for (int i=ref+plus+1; i<=ref+p; i++)
			{
				channel.setAvailKeyb(i, false);
			}
			plus = p;
			plusSpinner.setSelection(plus);
			channel.keyboardRefresh();
			channel.sampler.configSav.setPlus(p, channel.getNum(), index);
			channel.sampler.needToReset=true;
			return true;
		}
		else if (p<plus)
		{
			for (int i=ref+plus; i>ref+p; i--)
			{
				channel.setAvailKeyb(i, true);
			}
			plus = p;
			plusSpinner.setSelection(plus);
			channel.keyboardRefresh();
			channel.sampler.configSav.setPlus(p, channel.getNum(), index);
			return true;			
		}
		else
		{
			channel.sampler.configSav.setPlus(p, channel.getNum(), index);
			return true;
		}
	}
	
	public boolean setMinus(int m)
	{
		if ((ref-m)<0)
			return false;
		if (m>minus)
		{
			for (int i=ref-minus-1; i>=ref-m; i--)
			{
				if (channel.getAvailKeyb(i)) { continue; }
				return false;
			}
			for (int i=ref-minus-1; i>=ref-m; i--)
			{
				channel.setAvailKeyb(i, false);
			}
			minus = m;
			minusSpinner.setSelection(minus);
			channel.keyboardRefresh();
			channel.sampler.configSav.setMinus(m, channel.getNum(), index);
			channel.sampler.needToReset=true;
			return true;
		}
		else if (m<minus)
		{
			for (int i=ref-minus; i<ref-m; i++)
			{
				channel.setAvailKeyb(i, true);
			}
			minus = m;
			minusSpinner.setSelection(minus);
			channel.keyboardRefresh();
			channel.sampler.configSav.setMinus(m, channel.getNum(), index);
			return true;			
		}
		else
		{
			channel.sampler.configSav.setMinus(m, channel.getNum(), index);
			return true;
		}
	}
	
	public void setOutput(int o)
	{
		output=o;
		channel.sampler.configSav.setOutput(o, channel.getNum(), index);
	}
	
	private int getDefaultRef()
	/* Returns a default ref value which is not already occupied by another keygroup,
	   starting at the left of the keyboard.
	   User must ensure that there is at least one available note
	   before calling this function, otherwise it will return an out of range value. 
	*/ 
	{
		int ref = 0;
		while (!channel.getAvailKeyb(ref)) { ref++; }
		return ref;
	}
	
	private int getDefaultRef1() {
		/* Returns a default ref value which is not already occupied by another keygroup,
		   following a specific discrete distribution on the keyboard.
		   User must ensure that there is at least one available note
		   before calling this function, otherwise it will return an out of range value. 
		*/ 
		int ref = 60;
		int result=ref;
		int a=1;
		int n=-1;
		boolean force=false;
		while (force||(!channel.getAvailKeyb(result)))
		{
			force=false;
			if (n<11) n++;
			else
			{
				n=0;
				a=1-a;
				if (a==0) ref+=5;
				else ref-=7;
			}
			int r = (int) Math.pow(-1,n);
			result = ref+12*(r*(1+n/2));
			if ((result>127)||(result<0))
				force=true;
		}
		return result;
	}
	
	private void groupCreate(Composite parent, Control relative, Color bgColor)
	{
		group = new Group(parent, SWT.NONE);
		relocate(relative);
		group.setLayout(new FormLayout());
		group.setText(String.valueOf(index));
		group.setBackground(bgColor);
		
		fileText = new Text(group, SWT.BORDER);
		final Button loadButton = new Button(group, SWT.NONE);
		refSpinner = new Spinner(group, SWT.BORDER);
		plusSpinner = new Spinner(group, SWT.BORDER);
		minusSpinner = new Spinner(group, SWT.BORDER);
		final Button delButton = new Button(group, SWT.NONE);

		
		delButton.setImage(SWTResourceManager.getImage("Images/ico_x.gif"));
		final FormData delFd = new FormData();
		delFd.bottom = new FormAttachment(100, -2);
		delFd.top = new FormAttachment(0, 0);
		delFd.right = new FormAttachment(100, -3);
		delFd.left = new FormAttachment(100, -40);
		delButton.setLayoutData(delFd);
		delButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				delKeygroup();
			}
		});
		
		minusSpinner.setMaximum(127);
		minusSpinner.setPageIncrement(12);
		final FormData minusFd = new FormData();
		minusFd.bottom = new FormAttachment(100, -2);
		minusFd.top = new FormAttachment(0, 0);
		minusFd.right = new FormAttachment(delButton, -10, SWT.LEFT);
		minusFd.left = new FormAttachment(delButton, -60, SWT.LEFT);
		minusSpinner.setLayoutData(minusFd);
		minusSpinner.setSelection(minus);
		minusSpinner.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if (!setMinus(minusSpinner.getSelection()))
				{
					minusSpinner.setSelection(minus);
				}
				channel.sampler.configSav.writeAll();
			}
		});
		
		final FormData plusFd = new FormData();
		plusFd.bottom = new FormAttachment(100, -2);
		plusFd.top = new FormAttachment(0, 0);
		plusFd.right = new FormAttachment(minusSpinner, -5, SWT.LEFT);
		plusFd.left = new FormAttachment(minusSpinner, -55, SWT.LEFT);
		plusSpinner.setLayoutData(plusFd);
		plusSpinner.setMaximum(127);
		plusSpinner.setPageIncrement(12);
		plusSpinner.setSelection(plus);
		plusSpinner.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if (!setPlus(plusSpinner.getSelection()))
				{
					plusSpinner.setSelection(plus);
				}
				channel.sampler.configSav.writeAll();
			}
		});
		
		noteLabel = new Label(group, SWT.NONE);
		
		final FormData refFd = new FormData();
		refFd.bottom = new FormAttachment(100, -2);
		refFd.top = new FormAttachment(0, 0);
		refFd.right = new FormAttachment(plusSpinner, -10, SWT.LEFT);
		refFd.left = new FormAttachment(plusSpinner, -65, SWT.LEFT);
		refSpinner.setLayoutData(refFd);
		refSpinner.setMaximum(127);
		refSpinner.setPageIncrement(12);
		refSpinner.setSelection(ref);
		refSpinner.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if (!setRef(refSpinner.getSelection()))
				{
					refSpinner.setSelection(ref);
				}
				channel.sampler.configSav.writeAll();
			}
		});
		
		noteLabel.setBackground(bgColor);
		final FormData labelFd = new FormData();
		labelFd.bottom = new FormAttachment(100, -2);
		labelFd.top = new FormAttachment(11, 0);
		labelFd.right = new FormAttachment(refSpinner, -5, SWT.LEFT);
		labelFd.left = new FormAttachment(refSpinner, -35, SWT.LEFT);
		noteLabel.setLayoutData(labelFd);
		noteLabel.setAlignment(SWT.RIGHT);
		noteLabel.setText(convertPitch(ref));
		
		final FileDialog fDial = new FileDialog(channel.getShell(), SWT.APPLICATION_MODAL);
		String generalFilter = "*"+channel.EXTENSIONS[0];
		for (int i=1; i<channel.EXTENSIONS.length; i++)
		{
			generalFilter = generalFilter.concat(";*"+channel.EXTENSIONS[i]);
		}
		String[] filter = new String[channel.EXTENSIONS.length+1];
		filter[0] = generalFilter;
		for (int i=0; i<channel.EXTENSIONS.length; i++)
		{
			filter[i+1]="*"+channel.EXTENSIONS[i];
		}
		fDial.setFilterExtensions(filter);
		
		final FormData loadFd = new FormData();
		loadFd.bottom = new FormAttachment(100, -2);
		loadFd.top = new FormAttachment(0, 0);
		loadFd.right = new FormAttachment(noteLabel, -10, SWT.LEFT);
		loadFd.left = new FormAttachment(noteLabel, -50, SWT.LEFT);
		loadButton.setLayoutData(loadFd);
		loadButton.setText("Load");
		loadButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
					fileLoad(fileText, fDial);			
			}
		});
		
		final FormData textFd = new FormData();
		textFd.bottom = new FormAttachment(100, -2);
		textFd.top = new FormAttachment(0, 0);
		textFd.right = new FormAttachment(loadButton, -5, SWT.LEFT);
		textFd.left = new FormAttachment(0, 30);
		fileText.setLayoutData(textFd);
		fileText.setEditable(false);
		fileText.addMouseListener(new MouseAdapter() {
			public void mouseDoubleClick(final MouseEvent e) {
					fileLoad(fileText, fDial);
			}
		});
	}
	
	private void fileLoad(Text fileText, FileDialog fd)
	{
		System.out.println();
		if (file!=null) { fd.setFileName(file.getName()); }
		String path = fd.open();
		
		if (!(path==null))
		{
			setFile(new File(path));
			channel.sampler.configSav.writeAll();
			channel.sampler.needToReset = true;
		}
	}
	
	public void relocate(Control relative)
	{
		final FormData fd = new FormData();
		if (relative==null)
		{
			fd.top = new FormAttachment(0, 5);
			fd.bottom = new FormAttachment(0, 45);
		}
		else
		{
			fd.top = new FormAttachment(relative, 0, SWT.BOTTOM);
			fd.bottom = new FormAttachment(relative, 40, SWT.BOTTOM);
		}
		fd.right = new FormAttachment(100, -5);
		fd.left = new FormAttachment(0, 5);
		group.setLayoutData(fd);
	}

	public String convertPitch(int pitch) {
		String noteName[] = { "c", "c#", "d", "d#", "e", "f", "f#", "g", "g#",
				"a", "a#", "b" };
		int octave = pitch / 12;
		// int octave = pitch/12 - 4;
		return (noteName[pitch % 12] + Integer.toString(octave));
	}
	
	private static List getPathList(File f) {
		List l = new ArrayList();
		File r;
		try {
			r = f.getCanonicalFile();
			while(r != null) {
				l.add(r.getName());
				r = r.getParentFile();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
			l = null;
		}
		return l;
	}

	private static String matchPathLists(List r,List f) {
		int i;
		int j;
		String s;
		// start at the beginning of the lists
		// iterate while both lists are equal
		s = "";
		i = r.size()-1;
		j = f.size()-1;

		// first eliminate common root
		while((i >= 0)&&(j >= 0)&&(r.get(i).equals(f.get(j)))) {
			i--;
			j--;
		}

		// for each remaining level in the home path, add a ..
		for(;i>=0;i--) {
			s += ".." + File.separator;
		}

		// for each level in the file path, add the path
		for(;j>=1;j--) {
			s += f.get(j) + File.separator;
		}

		// file name
		s += f.get(j);
		return s;
	}
}