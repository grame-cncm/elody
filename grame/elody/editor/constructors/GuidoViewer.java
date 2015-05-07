/*
  Elody
  Copyright (C) Grame

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at http://mozilla.org/MPL/2.0/.

  Grame Research Laboratory, 11, cours de Verdun Gensoul 69002 Lyon - France
  research@grame.fr

*/

package grame.elody.editor.constructors;

import grame.elody.editor.controlers.TextBarField;
import grame.elody.editor.expressions.ExprHolder;
import grame.elody.editor.expressions.TNotesVisitor;
import grame.elody.editor.misc.Define;
import grame.elody.editor.misc.TGlobals;
import grame.elody.editor.misc.applets.BasicApplet;
import grame.elody.file.guido.saver.TGuidoWriter;
import grame.elody.file.midifile.TMultiSeqVisitor;
import grame.elody.lang.TExpMaker;
import grame.elody.lang.texpression.expressions.TExp;
import grame.elody.lang.texpression.expressions.TNullExp;
import grame.elody.util.MsgNotifier;
import grame.midishare.Midi;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Label;
import java.awt.MediaTracker;
import java.awt.Panel;
import java.awt.ScrollPane;
import java.io.StringWriter;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;

public class GuidoViewer extends BasicApplet implements Observer, Runnable {
	TextBarField zoom;
	ExprHolder  displayer;
	GuidoPanel  guidoImage;
	ScrollPane  guidoView;
	TExp curExp = TExpMaker.gExpMaker.createNull();
	Thread loader = null;
	
	public GuidoViewer (){	
		super (TGlobals.getTranslation("Guido_Viewer"));
		guidoImage = new GuidoPanel();
		guidoView = new ScrollPane();
		setSize(500,300);
		
	}
	
	public void init(){
		Panel botPanel= new Panel();
		setLayout(new BorderLayout(2, 5));
		
		displayer = new ExprHolder(null, new TNotesVisitor(),true);
		displayer.addObserver (this);
		add("North",displayer);  
		
		guidoView.add(guidoImage);
		guidoView.setSize(200,200);
						
		add("Center",guidoView);
  		
  		zoom = new TextBarField("50",10);
  		zoom.addObserver(this);
		botPanel.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
		botPanel.add(new Label(TGlobals.getTranslation("Zoom_Factor")));
  		botPanel.add(zoom); 
  		    		
  		add("South",botPanel);
  		
  	}
    
	 public  void update  (Observable o, Object arg) { 
		MsgNotifier mn = (MsgNotifier)o;
		TExp exp = displayer.getExpression();
		
		if (exp instanceof TNullExp) { 
			stopLoading();
			guidoImage.setImage(null);
		}else {
	  		switch (mn.message()) {
	  			case Define.ExprHolderMsg:
	  				if (!exp.equals(curExp)) {
	  					updateView ();
	  					curExp = exp;
	  				}
	  				break;
	  				
	  			default:
	  				updateView();
	  				break; 
			}
		}
	  }
	  
	  
	  
	  public  void updateView () {
		stopLoading();
	 	loader = new Thread(this);
        loader.start();
	  }
	  
	  void stopLoading () { if (loader != null) loader.stop(); }
	
	  public void run() {
	   
	    try {
         
         	StringWriter out =  new StringWriter();
         	//ByteArrayOutputStream out = new ByteArrayOutputStream();
			TGuidoWriter writer = new TGuidoWriter(out);
			TMultiSeqVisitor visitor = new TMultiSeqVisitor();
			int seq = Midi.NewSeq();
			if (seq != 0) {
				writer.writeVoices(visitor.fillSeq(displayer.getExpression(),seq));
				Midi.FreeSeq(seq);
				
				/*
		     		String adresse = "http://hp3.iti.informatik.tu-darmstadt.de/"
		 			+ "scripts/salieri/gifserv.pl?id=noteserver&"
		 			+ "outputformat=gif87&"
		 			+ "pixelwidth=1024"
		 			+ "&pixelheight=600"
					// + "&zoomfactor=1.0"
					+ "&zoomfactor="+ (float)Integer.parseInt(zoom.getText())/100.0
		 			+ "&gmndata="  + java.net.URLEncoder.encode(out.toString())
		 			+ "&submit=+++Send++";
		 		*/
		 		
	 			String adresse = "http://www.noteserver.org/"
	 			+ "scripts/salieri/gifserv.pl?"
	 			+ "&defpw=16.0cm"
	 			+ "&defph=12.0cm"
				+ "&zoom="+ (float)Integer.parseInt(zoom.getText())/100.0
				+ "&mode=gif"
	 			+ "&gmndata="  + java.net.URLEncoder.encode(out.toString(), "UTF-8")
	 			
	 			+ "&gmnurl="
	 			+ "&midiurl="
	 			+ "&mdurl="
	 			+ "&submit="+ java.net.URLEncoder.encode("+++Send++", "UTF-8");
	 	
		 		System.out.println("adresse :" + adresse);

		   	 	URL myurl = new URL(adresse);
		    	
		    	Image img1 = getImage(myurl);
		  
		  		MediaTracker tracker = new MediaTracker(this);
		     	tracker.addImage(img1,0);
		    	
		    	tracker.waitForID(0);
		        
		   		guidoImage.setImage(img1);
		   	}
		  } catch (Exception e) {}
      }
}

class GuidoPanel extends Panel{

	 private Image image = null;
	 
	 public Dimension getPreferredSize() {
	 	return (image == null) ? new Dimension(1024,600)
	 		: new Dimension(image.getWidth(this),image.getHeight(this));
	 }
		 
	 public void paint(Graphics g) {
   	if (image != null) {
     	 	g.drawImage(image, 0, 0,  this);
   	}else{
   		g.drawString("GuidoView",0,0);
   	}
 	 }
 	
 	 public void setImage(Image image) { 
 	 	this.image = image; 
 	 	repaint();
 	 }
}