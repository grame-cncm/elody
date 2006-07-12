package grame.elody.editor.constructors;

import grame.elody.editor.expressions.PlayExprHolder;
import grame.elody.editor.misc.TDialog;
import grame.elody.editor.misc.applets.BasicApplet;
import grame.elody.file.parser.TFileContent;
import grame.elody.lang.TExpMaker;
import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.net.Message;
import grame.elody.net.TConnection;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;

final public class TPlayerClient extends BasicApplet implements Observer,
		ActionListener {
	static final String startCommand = "Jouer";

	static final String stopCommand = "Arreter";

	static final String connectCommand = "Connecter";

	static final int port = 4001;

	Button start, stop, connect;

	TextField dateMS, serverName;

	PlayExprHolder sharedDisplayer, localDisplayer;

	TConnection connection = null;

	boolean connected;

	String localName = "";

	public TPlayerClient() {
		super("Player Client");
		setSize(320, 180);

		start = CreateCommand(startCommand);
		stop = CreateCommand(stopCommand);
		connect = CreateCommand(connectCommand);

		dateMS = new TextField("0 : 0 : 0", 10);
		sharedDisplayer = new PlayExprHolder(false);
		localDisplayer = new PlayExprHolder(true);
		localDisplayer.addObserver(this);

		start.setEnabled(false);
		stop.setEnabled(false);
	}

	private Button CreateCommand(String command) {
		Button button = new Button(command);
		button.setActionCommand(command);
		button.addActionListener(this);
		return button;
	}

	public void stop() {
		if (connected) {
			sendData(new Message("DECONNECTION"));
			clientDisconnect(); // déconnexion
		}
	}

	public void clientConnect() // connexion
	{
		try {
			Socket socket = new Socket(serverName.getText(), port);
			localName = socket.getInetAddress().toString();
			connection = new TConnection(socket, true);
			connection.addObserver(this);
			connect.setLabel("Déconnecter");
			connected = true;
			start.setEnabled(true);

		} catch (UnknownHostException e) { // Si le serveur n'a pas ete trouve
			TDialog Box = new TDialog(this.getFrame(), "Serveur Inconnu : "
					+ serverName.getText());
			Box.setVisible(true);
		} catch (Exception e) { // Si la connexion a echouee
			TDialog Box = new TDialog(this.getFrame(),
					"Impossible d'établir la connection");
			Box.setVisible(true);
		}
	}

	public void clientDisconnect() // déconnexion
	{
		sendData(new Message("DECONNECTION"));
		connection.Deconnection();
		connect.setLabel("Connecter");

		connection = null;
		connected = false;

		start.setEnabled(false);
		stop.setEnabled(false);
	}

	public void init() {
		setLayout(new BorderLayout(5, 5));

		Panel transportpanel = new Panel();
		Panel buttonpanel = new Panel();
		Panel bottompanel = new Panel();
		Panel textpanel = new Panel();
		Panel serverpanel = new Panel();
		serverpanel.setLayout(new BorderLayout(5, 5));

		bottompanel.setLayout(new BorderLayout());
		buttonpanel.setLayout(new GridLayout(1, 2, 2, 2));
		textpanel.setLayout(new GridLayout(1, 2, 2, 2));
		transportpanel.setLayout(new GridLayout(2, 1, 2, 2));

		buttonpanel.add(start);
		buttonpanel.add(stop);

		bottompanel.add("North", serverpanel);
		bottompanel.add("Center", localDisplayer);
		bottompanel.add("South", buttonpanel);

		TextField text = new TextField("Min  Sec  Ms");
		text.setEditable(false);
		textpanel.add(text);
		textpanel.add(dateMS);

		transportpanel.add(textpanel);
		transportpanel.add(bottompanel);

		serverpanel.add("West", new Label("Serveur"));
		serverName = new TextField();
		serverpanel.add("Center", serverName);
		serverpanel.add("East", connect);

		add("North", textpanel);
		add("Center", sharedDisplayer);
		add("South", bottompanel);

		moveFrame(100, 240);
	}

	void updateState(int date) {
		int min, sec, ms;

		min = date / 60000;
		date = date - min * 60000;
		sec = date / 1000;
		ms = date - sec * 1000;

		dateMS.setText(String.valueOf(min) + " : " + String.valueOf(sec)
				+ " : " + String.valueOf(ms));
		sharedDisplayer.showDate(date);
	}

	public void update(Observable o, Object arg) { //réception d'objets

		if (arg instanceof Message) { //s'il s'agit d'un message  

			String messageText = ((Message) arg).getValue();

			if (messageText.equals("DECONNECTION")) {
				clientDisconnect();
				TDialog Box = new TDialog(this.getFrame(),
						"Vous  n'etes plus connecté avec "
								+ serverName.getText());
				Box.setVisible(true);
			} else if (messageText.equals("RESET")) {
				sharedDisplayer.setExpression(TExpMaker.gExpMaker.createNull());
				localDisplayer.setExpression(TExpMaker.gExpMaker.createNull());
				start.setEnabled(true);
				stop.setEnabled(false);
			}

		} else if (arg instanceof TFileContent) { // expression 
			TFileContent file = (TFileContent) arg;
			TExp res = file.getExp();
			String author = file.getAuthor();
			sharedDisplayer.setExpression(res);
			if (author.equals(localName)) {
				stop.setEnabled(true);
				start.setEnabled(true);
			} else {
				stop.setEnabled(false);
				//start.setEnabled(false);
			}

		} else if (arg instanceof Integer) { // date
			updateState(((Integer) arg).intValue());
		}
	}

	void updateCommands(boolean state) {
		start.setEnabled(state);
		stop.setEnabled(state);
		connect.setEnabled(state);
	}

	void startPlayer() {
		sendData(new TFileContent("", localName, "", localDisplayer
				.getExpression()));
		start.setEnabled(false);
	}

	void stopPlayer() {
		sendData(new Message("STOP"));
	}

	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		if (action.equals(startCommand)) {
			startPlayer();
		} else if (action.equals(stopCommand)) {
			stopPlayer();
		} else if (action.equals(connectCommand)) {
			if (connected)
				clientDisconnect();
			else
				clientConnect();
		}
	}

	void sendData(Object Obj) {
		if (connection != null)
			connection.sendData(Obj);
	}
}
