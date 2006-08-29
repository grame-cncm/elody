package grame.elody.editor.constructors;

import grame.elody.editor.misc.TGlobals;
import grame.elody.editor.sampler.Channel;
import grame.elody.editor.sampler.ConfigSav;
import grame.elody.editor.sampler.PaDeviceInfo;
import grame.elody.editor.sampler.PaHostApiInfo;
import grame.elody.editor.sampler.PaJniConnect;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;

import com.swtdesigner.SWTResourceManager;

public class Sampler extends BasicShellSWT {	
/***** DESCRIPTION ************************************
 * The sampler allows to associate sounds in WAV or AIFF audio format
 * with one or more MIDI notes, so that when the note is emitted, the
 * associated sound is played. The instance of this class displays
 * the main sampler window. 
 ******************************************************/	
	private PaJniConnect jni = null;
	
	private File file;
	public ConfigSav configSav;
	private int sampleRate;
	private int framesPerBuffer;
	private PaDeviceInfo device = null;
	private boolean stereo = true;
	private Color bgColor, wColor;
	
	private Text fileText = null;
	private Combo sampleRateCombo = null;
	private Combo bufferCombo = null;
	
	private Group fileGroup = null; 
	private Group sampleRateGroup = null;
	private Group bufferGroup = null;
	private Group outputDeviceGroup = null;
	private Group channelsToDisplayGroup = null;
	
	private Vector<Channel> channels = new Vector<Channel>();
	private Vector<Integer> sampleArray;
	
	public boolean needToReset = false;

	final int BG_COLOR = SWT.COLOR_WHITE;
	final short CH_NUM = 32; // number of channels to display
	final String[] EXTENSIONS = {"*.conf","*.*"};
    int[] STD_SAMPLERATES = {8000, 9600, 11025, 12000, 16000, 22050, 24000, 32000, 44100, 48000, 88200, 96000, 192000};
    final int[] STD_BUFFERSIZES = {256, 512, 1024, 2048, 4096}; 

	public Sampler() {
		super(TGlobals.getTranslation("Sampler"));
		file = new File("soundplayer.conf");
		try { file.createNewFile(); } catch (IOException e) {}
		configSav = new ConfigSav(file);
		jni = new PaJniConnect();
		setDevice(jni.GetDevice());
		sampleRate = jni.GetSampleRate();
		framesPerBuffer = jni.GetFramesPerBuffer();
		file = new File(jni.GetFileName());
		configSav = new ConfigSav(file);
	}

	public void setBgColor() {
		bgColor = Display.getCurrent().getSystemColor(BG_COLOR);
		wColor = Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW);
	}
	
	public boolean setFile(String path) {
		File f = new File(path);
		if (!f.exists())
			try { f.createNewFile(); } catch (IOException e) { return false; }
		file = f;
		fileText.setText(file.getName());
		closeAllChannels();
		configSav = new ConfigSav(file);
		return true;
	}
	
	public void setDevice(PaDeviceInfo dev)
	{
		device = dev;
		stereo = ( device.getMaxOutputChannels() == 2 );
	}

	public void buildInterface() {
		shell.setRedraw(false);
		setLayout(new FormLayout());
		shell.setBackground(bgColor);
		Composite compositeTop = compositeTopCreate(shell);
		outputDeviceGroupCreate(shell, compositeTop);
		moveFrame(170, 20);
		setSizeSWT(540, -1);
		shell.setRedraw(true);
	}

	private Composite compositeTopCreate(Composite parent) {
		final Composite compositeTop = new Composite(parent, SWT.NONE);
		compositeTop.setBackground(bgColor);
		final FillLayout fillLayout = new FillLayout();
		fillLayout.spacing = 10;
		compositeTop.setLayout(fillLayout);
		final FormData fd = new FormData();
		fd.bottom = new FormAttachment(0, 200);
		fd.top = new FormAttachment(0, 10);
		fd.right = new FormAttachment(100, -10);
		fd.left = new FormAttachment(0, 10);
		compositeTop.setLayoutData(fd);
		compositeTopLeftCreate(compositeTop);
		channelsToDisplayGroupCreate(compositeTop);
		return compositeTop;
	}
	private Group outputDeviceGroupCreate(Composite parent, Control relative) {
		outputDeviceGroup = new Group(parent, SWT.NONE);
		outputDeviceGroup.setLayout(new FormLayout());
		outputDeviceGroup.setBackground(bgColor);
		outputDeviceGroup.setText(TGlobals.getTranslation("output_devices"));
		final FormData fd = new FormData();
		fd.top = new FormAttachment(relative, 10, SWT.BOTTOM);
		fd.right = new FormAttachment(100, -10);
		fd.left = new FormAttachment(0, 10);
		fd.bottom = new FormAttachment(100, -10);
		outputDeviceGroup.setLayoutData(fd);
		
		OutputDevicesTreeCreate(jni.getApiList(), outputDeviceGroup);
	
		return outputDeviceGroup;
	}
	private Composite compositeTopLeftCreate(Composite parent) {
		final Composite compositeTopLeft = new Composite(parent, SWT.NONE);
		compositeTopLeft.setBackground(bgColor);
		compositeTopLeft.setLayout(new FormLayout());

		Group fileGroup = fileGroupCreate(compositeTopLeft);
		Group sampleRateGroup = sampleRateGroupCreate(compositeTopLeft,
				fileGroup);
		bufferGroupCreate(compositeTopLeft, sampleRateGroup);

		return compositeTopLeft;
	}

	private Group channelsToDisplayGroupCreate(Composite parent) {
		channelsToDisplayGroup = new Group(parent, SWT.NONE);
		final RowLayout rowLayout = new RowLayout();
		rowLayout.spacing = 4;
		rowLayout.marginRight = 10;
		rowLayout.marginTop = 15;
		rowLayout.marginLeft = 10;
		rowLayout.marginBottom = 10;
		channelsToDisplayGroup.setLayout(rowLayout);
		channelsToDisplayGroup.setBackground(bgColor);
		channelsToDisplayGroup.setText(TGlobals.getTranslation("channels_to_display"));

		for (short i=1; i<=CH_NUM; i++)
		{
			Channel c = new Channel(channelsToDisplayGroup, i, bgColor, this);
			channels.add(c);
		}
		
		return channelsToDisplayGroup;
	}
	
	private Group fileGroupCreate(Composite parent) {
		fileGroup = new Group(parent, SWT.NONE);
		fileGroup.setBackground(bgColor);
		fileGroup.setText(TGlobals.getTranslation("file"));

		final FormData fd = new FormData();
		fd.bottom = new FormAttachment(0, 50);
		fd.left = new FormAttachment(0, 0);
		fd.right = new FormAttachment(100, 0);
		fd.top = new FormAttachment(0, 0);
		fileGroup.setLayoutData(fd);
		fileGroup.setLayout(new FormLayout());

		final Button fileNewButton = new Button(fileGroup, SWT.NONE);
		final Button fileLoadButton = new Button(fileGroup, SWT.NONE);
		final FileDialog fDial = new FileDialog(shell);
		
		fDial.setFilterExtensions(EXTENSIONS);

		fileLoadButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if (!fileText.getEditable()) {
					fileLoad(fileText, fDial);
				} else {
					fileCancel(fileText, fileNewButton, fileLoadButton);
				}
			}
		});
		final FormData loadButtonFd = new FormData();
		loadButtonFd.right = new FormAttachment(100, -10);
		loadButtonFd.bottom = new FormAttachment(100, -5);
		loadButtonFd.top = new FormAttachment(0, 5);
		fileLoadButton.setLayoutData(loadButtonFd);
		fileLoadButton.setText(TGlobals.getTranslation("Load"));
		
		fileNewButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if (!fileText.getEditable()) {
					fileNew(fileText, fileNewButton, fileLoadButton);
				} else {
					fileOk(fileText, fileNewButton, fileLoadButton);
				}

			}
		});
		final FormData newButtonFd = new FormData();
		newButtonFd.right = new FormAttachment(fileLoadButton, -5, SWT.LEFT);
		newButtonFd.bottom = new FormAttachment(100, -5);
		newButtonFd.top = new FormAttachment(0, 5);
		fileNewButton.setLayoutData(newButtonFd);
		fileNewButton.setText(TGlobals.getTranslation("New"));
		
		fileText = new Text(fileGroup, SWT.BORDER);
		fileText.addKeyListener(new KeyAdapter() {
			public void keyReleased(final KeyEvent e) {
					if (e.character == SWT.CR) {
						fileOk(fileText, fileNewButton, fileLoadButton);
					} else if (e.character == SWT.ESC) {
						fileCancel(fileText, fileNewButton, fileLoadButton);
					}
			}
		});

		fileText.addMouseListener(new MouseAdapter() {
			public void mouseDoubleClick(final MouseEvent e) {
				if (!fileText.getEditable()) {
					fileLoad(fileText, fDial);
				}
			}
		});
		fileText.setEditable(false);
		fileText.setText(file.getName());
		final FormData textFd = new FormData();
		textFd.right = new FormAttachment(fileNewButton, -10, SWT.LEFT);
		textFd.bottom = new FormAttachment(100, -5);
		textFd.top = new FormAttachment(0, 5);
		textFd.left = new FormAttachment(0, 10);
		fileText.setLayoutData(textFd);

		return fileGroup;
	}
	private Group sampleRateGroupCreate(Composite parent, Control relative) {
		sampleRateGroup = new Group(parent, SWT.NONE);
		sampleRateGroup.setBackground(bgColor);
		sampleRateGroup.setText(TGlobals.getTranslation("sample_rate"));
		final FormData fd = new FormData();
		fd.top = new FormAttachment(relative, 10, SWT.BOTTOM);
		fd.bottom = new FormAttachment(relative, 60, SWT.BOTTOM);
		fd.right = new FormAttachment(100, 0);
		fd.left = new FormAttachment(0, 0);
		sampleRateGroup.setLayoutData(fd);
		sampleRateGroup.setLayout(new FormLayout());

		sampleRateCombo = new Combo(sampleRateGroup, SWT.READ_ONLY);
		
		refreshSampleRates();
		sampleRateCombo.setBackground(wColor);
		sampleRateCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				needToReset=true;
				resetDriver();
			}
		});
		
		
		final FormData comboFd = new FormData();
		comboFd.bottom = new FormAttachment(100, -5);
		comboFd.right = new FormAttachment(100, -10);
		comboFd.top = new FormAttachment(0, 5);
		comboFd.left = new FormAttachment(0, 10);
		sampleRateCombo.setLayoutData(comboFd);
		
		return sampleRateGroup;
	}
	private Group bufferGroupCreate(Composite parent, Control relative) {
		bufferGroup = new Group(parent, SWT.NONE);
		bufferGroup.setBackground(bgColor);
		bufferGroup.setText(TGlobals.getTranslation("frames_per_buffer"));
		final FormData fd = new FormData();
		fd.bottom = new FormAttachment(relative, 60, SWT.BOTTOM);
		fd.top = new FormAttachment(relative, 10, SWT.BOTTOM);
		fd.right = new FormAttachment(100, 0);
		fd.left = new FormAttachment(0, 0);
		bufferGroup.setLayoutData(fd);
		bufferGroup.setLayout(new FormLayout());

		bufferCombo = new Combo(bufferGroup, SWT.READ_ONLY);
		String[] items = new String[STD_BUFFERSIZES.length];
		int defaultSelect = 0;
		for (int i=0; i< STD_BUFFERSIZES.length; i++)
		{
			items[i]=intToStr(STD_BUFFERSIZES[i]);
			if (STD_BUFFERSIZES[i]==framesPerBuffer) { defaultSelect=i; }
		}
		bufferCombo.setItems(items);
		bufferCombo.select(defaultSelect);
		bufferCombo.setBackground(wColor);
		bufferCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				needToReset=true;
				resetDriver();
			}
		});
		
		final FormData comboFd = new FormData();
		comboFd.bottom = new FormAttachment(100, -5);
		comboFd.right = new FormAttachment(100, -10);
		comboFd.top = new FormAttachment(0, 5);
		comboFd.left = new FormAttachment(0, 10);
		bufferCombo.setLayoutData(comboFd);
		
		return bufferGroup;
	}

	private void fileNew(Text fileText, Button fileNewButton,
			Button fileLoadButton) {
		sampleRateGroup.setEnabled(false);
		bufferGroup.setEnabled(false);
		outputDeviceGroup.setEnabled(false);
		fileText.forceFocus();
		fileText.setText("");
		fileText.setEditable(true);
		fileNewButton.setText(TGlobals.getTranslation("OK"));
		fileLoadButton.setText(TGlobals.getTranslation("Cancel"));
		fileGroup.layout();
	}
	private void fileLoad(Text fileText, FileDialog fd) {
		fd.setFileName(file.getName());
		String path = fd.open();
		String asw = fd.getFileName();
		if (!(asw.equals("")||asw.equals(file.getName())))
		{
			setFile(path);
			needToReset=true;
			resetDriver();
		}
	}
	private void fileOk(Text fileText, Button fileNewButton,
			Button fileLoadButton) {
		try {
			final MessageBox mb = new MessageBox(shell, SWT.OK);
			String path = fileText.getText();
			if ((path.length()<6)||(!path.substring(path.length()-5).equalsIgnoreCase(".conf")))
			{
				path+=".conf";
				fileText.setText(path);
			}
			File f = new File(path);
			if (f.createNewFile()) {
				file = f;
				closeAllChannels();
				configSav = new ConfigSav(file);
				fileText.setEditable(false);
				fileNewButton.setText(TGlobals.getTranslation("New"));
				fileLoadButton.setText(TGlobals.getTranslation("Load"));
				fileGroup.layout();
				fileNewButton.forceFocus();
				needToReset=true;
				resetDriver();
				sampleRateGroup.setEnabled(true);
				bufferGroup.setEnabled(true);
				outputDeviceGroup.setEnabled(true);
			} else {

				mb.setMessage(TGlobals.getTranslation("file_exists1") + fileText.getText()
						+ TGlobals.getTranslation("file_exists2"));
				mb.open();
			}
		} catch (IOException e1) {
		}
	}
	private void fileCancel(Text fileText, Button fileNewButton, Button fileLoadButton) {
		fileText.setText(file.getName());
		fileText.setEditable(false);
		fileNewButton.setText(TGlobals.getTranslation("New"));
		fileLoadButton.setText(TGlobals.getTranslation("Load"));
		fileGroup.layout();
		fileLoadButton.forceFocus();
		sampleRateGroup.setEnabled(true);
		bufferGroup.setEnabled(true);
		outputDeviceGroup.setEnabled(true);
	}

	private String intToStr(int a)
	// convert an integer to a string adding separating spaces for thousands 
	{
		StringBuffer v = new StringBuffer(String.valueOf(a));
		int n = v.length() / 3;
		for (int i = n; i > 0; i--) {
			v.insert(v.length() - 3 * i, " ");
		}
		if (v.charAt(0) == ' ') {
			v.deleteCharAt(0);
		}
		return v.toString();
	}

	private void OutputDevicesTreeCreate(Vector<PaHostApiInfo> apiList, Composite parent) {

		Control relative = null;
		for (int i=0; i<apiList.size(); i++)
		{
			relative = ApiTreeCreate(apiList.get(i), parent, relative);
		}
	}

	private Control ApiTreeCreate(PaHostApiInfo api, Composite parent, Control relative)
	{
		final Label apiIcon = new Label(parent, SWT.NONE);
		apiIcon.setImage(SWTResourceManager.getImage("Images/ico_d.bmp"));
		final FormData iconFd = new FormData();
		if (relative==null)	{ iconFd.top = new FormAttachment(0, 10); }
		else				{ iconFd.top = new FormAttachment(relative, 10, SWT.DEFAULT); }
		iconFd.left = new FormAttachment(0, 10);
		apiIcon.setLayoutData(iconFd);
		apiIcon.setBackground(bgColor);
		
		final Label apiLabel = new Label(parent, SWT.NONE);
		final FormData labelFd = new FormData();
		if (relative==null)	{ labelFd.top = new FormAttachment(0, 10); }
		else				{ labelFd.top = new FormAttachment(relative, 10, SWT.DEFAULT); }
		labelFd.left = new FormAttachment(apiIcon, 10, SWT.RIGHT);
		apiLabel.setLayoutData(labelFd);
		apiLabel.setBackground(bgColor);
		apiLabel.setText(api.getName());
		Control rel = apiIcon;
		for (int i=0; i<api.getDevList().size(); i++)
		{
			rel = DeviceCreate(i, parent, rel, api.getDevList().get(i));
		}
		return rel;
	}

	private Control DeviceCreate(final int devIndex, Composite parent, Control relative, final PaDeviceInfo dev)
	{
		final Button devButton = new Button(parent, SWT.RADIO);
		devButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if (devButton.getSelection())
				{
					setDevice(dev);
					sampleRate=device.getDefaultSampleRate();
					refreshSampleRates();
					refresh();
					needToReset=true;
					resetDriver();
				}
			}
		});
		if (device.getDevIndex()==dev.getDevIndex()) { devButton.setSelection(true); }
		devButton.setImage(SWTResourceManager.getImage("Images/ico_s.bmp"));
		devButton.setBackground(bgColor);
		final FormData buttonFd = new FormData();
		buttonFd.top = new FormAttachment(relative, 5, SWT.BOTTOM);
		buttonFd.left = new FormAttachment(0, 15);
		devButton.setLayoutData(buttonFd);

		final Label devLabel = new Label(parent, SWT.NONE);
		devLabel.setBackground(bgColor);
		final FormData labelFd = new FormData();
		labelFd.top = new FormAttachment(relative, 5, SWT.BOTTOM);
		labelFd.left = new FormAttachment(devButton, 5, SWT.RIGHT);
		devLabel.setLayoutData(labelFd);
		devLabel.setText(dev.getName());
		
		return devButton;
	}
	
	private void refresh()
	{
		fileText.setText(file.getName());
		for (int i=0; i< sampleArray.size(); i++)
		{
			if (sampleArray.get(i).intValue()==sampleRate) {sampleRateCombo.select(i); }
		}
		for (int i=0; i< STD_BUFFERSIZES.length; i++)
		{
			if (STD_BUFFERSIZES[i]==framesPerBuffer) {bufferCombo.select(i); }
		}
	}
	
	public void resetDriver()
	{
		if (needToReset)
		{
			int err = jni.paDriverReload(sampleArray.get(sampleRateCombo.getSelectionIndex()).intValue(),	
					STD_BUFFERSIZES[bufferCombo.getSelectionIndex()],
					fileText.getText(),
					device.getDevIndex());
			if (err==0)
			{
				sampleRate=sampleArray.get(sampleRateCombo.getSelectionIndex()).intValue();
				framesPerBuffer=STD_BUFFERSIZES[bufferCombo.getSelectionIndex()];
			}
			else
			{
				refresh();
				final MessageBox mb = new MessageBox(shell, SWT.OK);
				String errorMsg = PaJniConnect.GetErrorText(err);
				mb.setMessage(errorMsg);
				mb.open();
			}
			needToReset = false;
		}
	}
	private void closeAllChannels()
	{
		for (int i=0; i<channels.size(); i++)
		{
			Channel c = channels.get(i);
			c.shellClose();
		}
	}
	private void refreshSampleRates()
	{
		sampleArray = jni.getAvailableSampleRates(STD_SAMPLERATES);
		String[] items = new String[sampleArray.size()];
		int defaultSelect = 0;
		for (int i=0; i< sampleArray.size(); i++)
		{
			Integer n = sampleArray.get(i);
			items[i]=n.toString()+" Hz";
			if (n.intValue()==sampleRate) { defaultSelect=i; }
		}
		sampleRateCombo.setItems(items);
		sampleRateCombo.select(defaultSelect);
	}
	
	public void setOutputDeviceGroupEnable(boolean b)	{ outputDeviceGroup.setEnabled(b); }
	public int getNumOutputs()	{ return device.getMaxOutputChannels(); }
	public boolean isStereo()	{ return stereo; }
	public int getChannelsOpenedCount()
	{
		int n=0;
		for (int i=0; i<channels.size(); i++)
		{
			Channel c = channels.get(i);
			if (c.isShellOpened())	n++;
		}
		return n;
		
	}
}