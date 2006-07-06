package grame.elody.editor.sampler;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;

import com.swtdesigner.SWTResourceManager;

public class Envelope {
	
	private Channel ch;
	private Canvas canvas;
	
	private int attack = 0;
	private int decay = 0;
	private double sustain = 0.0;
	private int release = 300;
	
	private Shell s;
	private boolean shellOpened = false;
	
	public Envelope(int attack, int decay, double sustain, int release, Channel ch) {
		this.attack = attack;
		this.decay = decay;
		this.sustain = sustain;
		this.release = release;
		this.ch = ch;
	}

	public int getAttack()		{ return attack; }
	public int getDecay()		{ return decay; }
	public double getSustain()	{ return sustain; }
	public int getRelease()		{ return release; }
	
	public void shellOpen()
	{
		s = new Shell(SWT.DIALOG_TRIM);
		s.setRedraw(false);
		s.setSize(500, 450);
		s.setText("Sampler - Channel "+ch.getNum()+" - Envelope Editor");
		s.setBackground(ch.bgColor);
		s.setLayout(new FormLayout());
		s.setVisible(true);
		s.setActive();

		final Composite composite = new Composite(s, SWT.NONE);
		composite.setBackground(ch.bgColor);
		final FormData formData = new FormData();
		formData.bottom = new FormAttachment(0, 170);
		formData.right = new FormAttachment(100, -10);
		formData.top = new FormAttachment(0, 10);
		formData.left = new FormAttachment(0, 10);
		composite.setLayoutData(formData);
		composite.setLayout(new FormLayout());

		final Label attackLabel = new Label(s, SWT.NONE);
		attackLabel.setBackground(ch.bgColor);
		attackLabel.setFont(SWTResourceManager.getFont("", 10, SWT.BOLD));
		final FormData attackLabelFd = new FormData();
		attackLabelFd.right = new FormAttachment(100, -380);
		attackLabelFd.top = new FormAttachment(0, 200);
		attackLabel.setLayoutData(attackLabelFd);
		attackLabel.setText("ATTACK :");

		final Label decayLabel = new Label(s, SWT.NONE);
		decayLabel.setBackground(ch.bgColor);
		decayLabel.setFont(SWTResourceManager.getFont("", 10, SWT.BOLD));
		final FormData decayLabelFd = new FormData();
		decayLabelFd.right = new FormAttachment(100, -380);
		decayLabelFd.top = new FormAttachment(0, 250);
		decayLabel.setLayoutData(decayLabelFd);
		decayLabel.setText("DECAY :");

		final Label sustainLabel = new Label(s, SWT.NONE);
		sustainLabel.setBackground(ch.bgColor);
		sustainLabel.setFont(SWTResourceManager.getFont("", 10, SWT.BOLD));
		final FormData sustainLabelFd = new FormData();
		sustainLabelFd.right = new FormAttachment(100, -380);
		sustainLabelFd.top = new FormAttachment(0, 300);
		sustainLabel.setLayoutData(sustainLabelFd);
		sustainLabel.setText("SUSTAIN :");

		final Label releaseLabel = new Label(s, SWT.NONE);
		releaseLabel.setBackground(ch.bgColor);
		releaseLabel.setFont(SWTResourceManager.getFont("", 10, SWT.BOLD));
		final FormData releaseLabelFd = new FormData();
		releaseLabelFd.right = new FormAttachment(100, -380);
		releaseLabelFd.top = new FormAttachment(0, 350);
		releaseLabel.setLayoutData(releaseLabelFd);
		releaseLabel.setText("RELEASE :");

		final Spinner attackSpinner = new Spinner(s, SWT.BORDER);
		final Scale attackScale = new Scale(s, SWT.NONE);
		attackScale.setBackground(ch.bgColor);
		attackScale.setMinimum(0);
		attackScale.setMaximum(15000);
		attackScale.setIncrement(500);
		attackScale.setPageIncrement(1000);
		final FormData attackScaleFd = new FormData();
		attackScaleFd.left = new FormAttachment(0, 130);
		attackScaleFd.right = new FormAttachment(0, 350);
		attackScaleFd.top = new FormAttachment(0, 188);
		attackScale.setLayoutData(attackScaleFd);
		attackScale.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				attackSpinner.setSelection(attackScale.getSelection());
			}
		});

		final Spinner decaySpinner = new Spinner(s, SWT.BORDER);
		final Scale decayScale = new Scale(s, SWT.NONE);
		decayScale.setBackground(ch.bgColor);
		decayScale.setMinimum(0);
		decayScale.setMaximum(25000);
		decayScale.setIncrement(500);
		decayScale.setPageIncrement(1000);
		final FormData decayScaleFd = new FormData();
		decayScaleFd.left = new FormAttachment(0, 130);
		decayScaleFd.right = new FormAttachment(0, 350);
		decayScaleFd.top = new FormAttachment(0, 238);
		decayScale.setLayoutData(decayScaleFd);
		decayScale.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				decaySpinner.setSelection(decayScale.getSelection());
			}
		});

		final Spinner sustainSpinner = new Spinner(s, SWT.BORDER);
		final Scale sustainScale = new Scale(s, SWT.NONE);
		sustainScale.setBackground(ch.bgColor);
		sustainScale.setMinimum(0);
		sustainScale.setMaximum(300);
		sustainScale.setIncrement(10);
		sustainScale.setPageIncrement(50);
		final FormData sustainScaleFd = new FormData();
		sustainScaleFd.left = new FormAttachment(0, 130);
		sustainScaleFd.right = new FormAttachment(0, 350);
		sustainScaleFd.top = new FormAttachment(0, 288);
		sustainScale.setLayoutData(sustainScaleFd);
		sustainScale.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				sustainSpinner.setSelection(sustainScale.getSelection());
			}
		});

		final Spinner releaseSpinner = new Spinner(s, SWT.BORDER);
		final Scale releaseScale = new Scale(s, SWT.NONE);
		releaseScale.setBackground(ch.bgColor);
		releaseScale.setMinimum(0);
		releaseScale.setMaximum(25000);
		releaseScale.setIncrement(500);
		releaseScale.setPageIncrement(1000);
		final FormData releaseScaleFd = new FormData();
		releaseScaleFd.left = new FormAttachment(0, 130);
		releaseScaleFd.right = new FormAttachment(0, 350);
		releaseScaleFd.top = new FormAttachment(0, 338);
		releaseScale.setLayoutData(releaseScaleFd);
		releaseScale.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				releaseSpinner.setSelection(releaseScale.getSelection());
			}
		});

		attackSpinner.setMinimum(0);
		attackSpinner.setMaximum(15000);
		attackSpinner.setIncrement(10);
		attackSpinner.setPageIncrement(100);
		final FormData attackSpinnerFd = new FormData();
		attackSpinnerFd.left = new FormAttachment(0, 360);
		attackSpinnerFd.right = new FormAttachment(0, 440);
		attackSpinnerFd.top = new FormAttachment(0, 200);
		attackSpinner.setLayoutData(attackSpinnerFd);
		attackSpinner.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				attackScale.setSelection(attackSpinner.getSelection());
			}
		});

		decaySpinner.setMinimum(0);
		decaySpinner.setMaximum(25000);
		decaySpinner.setIncrement(10);
		decaySpinner.setPageIncrement(100);
		final FormData decaySpinnerFd = new FormData();
		decaySpinnerFd.left = new FormAttachment(0, 360);
		decaySpinnerFd.right = new FormAttachment(0, 440);
		decaySpinnerFd.top = new FormAttachment(0, 250);
		decaySpinner.setLayoutData(decaySpinnerFd);
		decaySpinner.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				decayScale.setSelection(decaySpinner.getSelection());
			}
		});

		sustainSpinner.setDigits(1);
		sustainSpinner.setMinimum(0);
		sustainSpinner.setMaximum(300);
		sustainSpinner.setIncrement(1);
		sustainSpinner.setPageIncrement(10);
		final FormData sustainSpinnerFd = new FormData();
		sustainSpinnerFd.left = new FormAttachment(0, 360);
		sustainSpinnerFd.right = new FormAttachment(0, 440);
		sustainSpinnerFd.top = new FormAttachment(0, 300);
		sustainSpinner.setLayoutData(sustainSpinnerFd);
		sustainSpinner.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				sustainScale.setSelection(sustainSpinner.getSelection());
			}
		});

		releaseSpinner.setMinimum(0);
		releaseSpinner.setMaximum(25000);
		releaseSpinner.setIncrement(10);
		releaseSpinner.setPageIncrement(100);
		final FormData releaseSpinnerFd = new FormData();
		releaseSpinnerFd.left = new FormAttachment(0, 360);
		releaseSpinnerFd.right = new FormAttachment(0, 440);
		releaseSpinnerFd.top = new FormAttachment(0, 350);
		releaseSpinner.setLayoutData(releaseSpinnerFd);
		releaseSpinner.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				releaseScale.setSelection(releaseSpinner.getSelection());
			}
		});

		final Label attackUnitLabel = new Label(s, SWT.NONE);
		attackUnitLabel.setBackground(ch.bgColor);
		final FormData attackUnitFd = new FormData();
		attackUnitFd.top = new FormAttachment(0, 200);
		attackUnitFd.left = new FormAttachment(0, 450);
		attackUnitLabel.setLayoutData(attackUnitFd);
		attackUnitLabel.setText("ms");

		final Label decayUnitLabel = new Label(s, SWT.NONE);
		decayUnitLabel.setBackground(ch.bgColor);
		final FormData decayUnitFd = new FormData();
		decayUnitFd.top = new FormAttachment(0, 250);
		decayUnitFd.left = new FormAttachment(0, 450);
		decayUnitLabel.setLayoutData(decayUnitFd);
		decayUnitLabel.setText("ms");

		final Label sustainUnitLabel = new Label(s, SWT.NONE);
		sustainUnitLabel.setBackground(ch.bgColor);
		final FormData sustainUnitFd = new FormData();
		sustainUnitFd.top = new FormAttachment(0, 300);
		sustainUnitFd.left = new FormAttachment(0, 450);
		sustainUnitLabel.setLayoutData(sustainUnitFd);
		sustainUnitLabel.setText("dB");

		final Label releaseUnitLabel = new Label(s, SWT.NONE);
		releaseUnitLabel.setBackground(ch.bgColor);
		final FormData releaseUnitFd = new FormData();
		releaseUnitFd.top = new FormAttachment(0, 350);
		releaseUnitFd.left = new FormAttachment(0, 450);
		releaseUnitLabel.setLayoutData(releaseUnitFd);
		releaseUnitLabel.setText("ms");
		
		final Button closeButton = new Button(s, SWT.NONE);
		final FormData closeFd = new FormData();
		closeFd.top = new FormAttachment(0, 385);
		closeFd.left = new FormAttachment(0, 220);
		closeButton.setLayoutData(closeFd);
		closeButton.setText("Close");
		closeButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				shellClose();
			}
		});
		
		//buildInterface();
		s.layout();
		s.setRedraw(true);
		shellOpened = true;
	}
	
	public void shellClose() {
		if (s!=null)
			s.close();
	}
	
}
