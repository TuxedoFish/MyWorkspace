package genreal;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.geom.Rectangle2D;

import startup.Data;

public class Transition {

	public Data data;
	
	public Transition(Data parent)
	{
		
		data = parent;
		
	}
	
	public void update()
	{
		
		//Fade out
		 if(data.transparencystage < data.fadespeed)
		   {
			 
			 	//Clear it
			 	data.graphicsstuffgraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f));
			 	Rectangle2D.Double rect2 = new Rectangle2D.Double(0,0,640,480); 
			 	data.graphicsstuffgraphics.fill(rect2);
			 	data.graphicsstuffgraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
			 	//Work out colour
				data.transparency = ((255/data.fadespeed) * data.transparencystage);
				data.inttransparency = (int) Math.round(data.transparency);
				Color color = new Color(255,255,255,data.inttransparency);
				data.graphicsstuffgraphicsbuffered.setColor(color);
				data.graphicsstuffgraphicsbuffered.fillRect(0, 0, 640, 480);
				data.graphicsstuffgraphics.drawImage(data.BufferedOverlayscreen,0,0,null);
				data.transparencystage += 1;
				//clear buffered image (stops flashing)
				data.graphicsstuffgraphicsbuffered.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f));
				Rectangle2D.Double rect = new Rectangle2D.Double(0,0,640,480); 
				data.graphicsstuffgraphicsbuffered.fill(rect);
				data.graphicsstuffgraphicsbuffered.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
			
		   }
		   //Fade back in
		   if(data.transparencystage > data.fadespeed - 1 && data.transparencystage < data.fadespeed * 2)
		   {
			 
			 	//Clear it
			 	data.graphicsstuffgraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f));
			 	Rectangle2D.Double rect2 = new Rectangle2D.Double(0,0,640,480); 
			 	data.graphicsstuffgraphics.fill(rect2);
			 	data.graphicsstuffgraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
			 	//Work out colour
				data.transparency = (255 - (255/data.fadespeed) * (data.transparencystage - data.fadespeed));
				data.inttransparency = (int) Math.round(data.transparency);
				Color color = new Color(255,255,255,data.inttransparency);
				data.graphicsstuffgraphicsbuffered.setColor(color);
				data.graphicsstuffgraphicsbuffered.fillRect(0, 0, 640, 480);
				data.graphicsstuffgraphics.drawImage(data.BufferedOverlayscreen,0,0,null);
				data.transparencystage += 1;
				//clear buffered image (stops flashing)
				data.graphicsstuffgraphicsbuffered.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f));
				Rectangle2D.Double rect = new Rectangle2D.Double(0,0,640,480); 
				data.graphicsstuffgraphicsbuffered.fill(rect);
				data.graphicsstuffgraphicsbuffered.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
			
		   }
		
	}
	
}
