package grame.elody.editor.constructors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;

public class Sampler extends BasicShellSWT {

	public Sampler() {
		super("Sampler");
		setLayout(/*new FormLayout()*/new FillLayout());
	}

	public void init() {
		setSize(540, 400);
		moveFrame(170, 20);
		final Button swtButton = new Button(shell, SWT.PUSH);
		swtButton.setText("SWT Button");
	}
}