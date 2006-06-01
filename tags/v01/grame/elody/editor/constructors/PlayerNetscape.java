package grame.elody.editor.constructors;

import grame.elody.editor.expressions.PlayExprHolder;
import grame.elody.editor.misc.TGlobals;
import grame.elody.editor.misc.draganddrop.DragAble;
import grame.elody.editor.player.TRealTimePlayer;
import grame.elody.file.parser.TFileParser;
import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.util.MidiUtils;
import grame.midishare.player.PlayerPos;
import grame.midishare.player.PlayerState;

import java.applet.Applet;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Event;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.TextField;


public class PlayerNetscape extends Applet implements Runnable {  
	  Button start, stop ,cont ,backward ,forward;
	  Checkbox loopbox;
	  Thread selfThread = null;
	  
	  TextField dateMS, dateBBU;
	  PlayerPos pos;

	  TRealTimePlayer msplayer = null;
	  
	  NetscapeDisplayer  displayer;
	  TExp exp;
	  static String version = "1.05";

	  public PlayerNetscape ()
	  {
	   	pos = new PlayerPos();
		     
		start = new Button( "Start" ) ;
		stop = new Button( "Stop" ) ;
		cont = new Button( "Play" ) ;
		backward = new Button( "<<" ) ;
		forward = new Button( ">>" ) ;
		  
		dateMS = new TextField("0 : 0 : 0", 10);
		dateBBU = new TextField("1 : 1 : 1", 10);
		displayer = new NetscapeDisplayer();
		resize(300,200);
		setFont (new Font("Times", Font.PLAIN, 12));
	}

	 public void start(){
	 	
	 	//System.out.println("Start");
	 	
	 	TGlobals.init();
	 	
	 	try {
	 		if (msplayer == null) {
	     		msplayer = new TRealTimePlayer() ;
	     		msplayer.Open ("ElodyPlayer");
	     		msplayer.setLoopPlayer(true);
	     	}
	     	if (selfThread == null) {
		 		selfThread = new Thread(this);
		 		selfThread.start();
		 	}
		 	
		 	MidiUtils.connect(msplayer.getRefnum(),"MidiShare",1);
		 	MidiUtils.connect(msplayer.getRefnum(),"msExpander",1);
		  	
		  	//System.out.println("Open url");
			TFileParser reader = new TFileParser();
			exp = reader.readFile(getDocumentBase()).getExp();
			if (exp != null) {
				//System.out.println("Read exp OK");
				displayer.setExpression(exp);
				//System.out.println("Render exp OK");
			}else{
				System.out.println("Read exp ERROR");
			}
		 	
	   }catch (Exception e) {
		 	System.out.println(e);
	   }
	 }
	 
	 
	 public void stop(){
	 	if (msplayer != null) {
	 		selfThread.stop();
	 		selfThread = null;
	 		msplayer.Close();
	 		msplayer = null;
	 	}
	 	TGlobals.quit();
	 }
	 
	 
	  
	  public void init()
	  {
	  	System.out.println("ElodyPlayer Applet version: " + version);
	  	//System.out.println("Init");
	  	setLayout (new GridLayout(2,1));
	   	Panel transportpanel = new Panel() ;
		
		Panel buttonpanel = new Panel();
		Panel textpanel = new Panel();
		
		buttonpanel.setLayout( new GridLayout(1,6)) ;
		textpanel.setLayout(new GridLayout(1,4));  
		transportpanel.setLayout( new GridLayout(2,1)) ;
		
		loopbox = new  Checkbox("Loop",null, true);
		buttonpanel.add( loopbox );
		  	
	   	buttonpanel.add( backward) ;
	    buttonpanel.add( forward ) ;
	  
	    buttonpanel.add( start) ;
	    buttonpanel.add( stop ) ;
	    buttonpanel.add( cont ) ;
	    
	    
	  	TextField text = new TextField("Min  Sec  Ms");
	  	text.setEditable(false);
	     
		textpanel.add(text);
		textpanel.add(dateMS);
		
		text = new TextField("Bar Beat Unit");
		text.setEditable(false);
		textpanel.add(text);
		textpanel.add(dateBBU);

	    transportpanel.add( textpanel ) ;
	    transportpanel.add( buttonpanel ) ;
	    add(transportpanel);
	    add(displayer);
	    
	    resize(300,60);
	   }
	  
	  public void updateText(int date, int bar, int beat, int unit){
		int min,sec,ms;
		
		min = date/60000;
		date = date - min*60000;
		sec = date/1000;
		ms = date - sec*1000;
			
		dateMS.setText(String.valueOf (min) + " : " + String.valueOf(sec) + " : " + String.valueOf(ms));
		dateBBU.setText(String.valueOf (bar) + " : " + String.valueOf(beat) + " : " + String.valueOf(unit));
	  }
			
	 		
		void updateState (){
			PlayerState state = msplayer.getState(); 
			updateText(state.date, state.bar, state.beat, state.unit);
			displayer.showDate(state.date);
	 	}	
	  	
	  	public void run() {
			while (true) {
				updateState();
				try{
					Thread.currentThread().sleep(200);
				}catch (InterruptedException e) {}
			}
		}
		
		  
	   	public boolean action( Event e , Object o ) {
	  
		    if (e.target == start){
		       msplayer.startPlayer(exp) ;
		    }else  if (e.target == stop) {
		      	msplayer.stopPlayer() ;
		    }else if (e.target == cont){
		       	msplayer.contPlayer() ;
		    }else  if (e.target == backward) {
		    	System.out.println("Sorry: not yet implemented");
		    	/*
		    	PlayerState state = render.player.GetState();
		     	pos.bar = (short)Math.max(1,(state.bar-3));
		     	pos.beat = (short)Math.max(1,(state.beat-3));
				pos.unit = (short)Math.max(1,(state.unit-10));;
				render.player.SetPosBBU(pos);
				*/
					
		    }else if (e.target == forward) {
		    	System.out.println("Sorry: not yet implemented");
		   	 /*
		    	PlayerState state = render.player.GetState();
		     	pos.bar = (short)(state.bar+3);
		     	pos.beat = state.beat;
				pos.unit = state.unit;
				render.player.SetPosBBU(pos);
				*/ 
			}else  if (e.target == loopbox) {
		    	msplayer.setLoopPlayer(loopbox.getState());
		   }else return super.action (e, o);
		    
		   return true;
		}
}

class NetscapeDisplayer extends PlayExprHolder{

	public NetscapeDisplayer () {
		super();
	}
	public DragAble getDragObject (int x, int y) { return null; }
}