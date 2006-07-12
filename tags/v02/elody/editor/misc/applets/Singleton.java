package grame.elody.editor.misc.applets;

public class Singleton extends BasicApplet {
	protected Singleton () { }
	public Singleton (String title) 		{ super(title);}

	public void toFront () {
		if (!frame.isShowing()) frame.setVisible(true);
		getFrame().toFront(); 
	}

	public static boolean isSingle (String className) throws ClassNotFoundException {
		Class c = Class.forName(className);
		while (c!=null) {
			if (c.getName().equals("grame.elody.misc.applets.Singleton")) {
				return true;
			}
			c = c.getSuperclass();
		}
		return false;
	}
}
