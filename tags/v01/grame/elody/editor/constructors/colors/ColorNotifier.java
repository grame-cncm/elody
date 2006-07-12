package grame.elody.editor.constructors.colors;

import java.util.Observable;

public class ColorNotifier extends Observable {
	public void notifyObservers (Object o) {
		setChanged();
		super.notifyObservers (o);
	}
}
