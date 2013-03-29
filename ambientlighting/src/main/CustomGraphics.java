package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

public class CustomGraphics extends JPanel{

	public Data data; 
	public ArrayList<Integer> polygonsx = new ArrayList<Integer>();
	public ArrayList<Integer> polygonsy = new ArrayList<Integer>();
	public int finalpolygonsx[] = {0,0,0,0};
	public int finalpolygonsy[] = {0,0,0,0};
	
    public CustomGraphics (Data parent)
	{
		
	  data = parent;

	}
    
   public void shadows(ArrayList<Integer> pointxpos,ArrayList<Integer> pointypos,ArrayList<Integer> lightxpos,ArrayList<Integer> lightypos,int objectposx,int objectposy)
   {
	   
	    polygonsx.clear();
   	    polygonsy.clear();
   	    int[] endpolygonx = new int[pointxpos.size()];
    	int[] endpolygony = new int[pointxpos.size()];
    	ArrayList<Integer> refrancex = new ArrayList<Integer>();
    	ArrayList<Integer> refrancey = new ArrayList<Integer>();
    	//Getting all of the relevant points
   	    for(int item=0;item<lightxpos.size();item++)
   	    {		
   	    	    for(int done=0;done<pointxpos.size();done++)
   	    		{
	    	
   	    				polygonsx.add(pointxpos.get(done));
   	    				polygonsy.add(pointypos.get(done));
   	    				polygonsx.add((pointxpos.get(done) - (lightxpos.get(item) - objectposx)) * data.shadownlength);
   	    				polygonsy.add((pointypos.get(done) - (lightypos.get(item) - objectposy)));
   	    				//Used for making the ends 
   	    				refrancex.add((pointxpos.get(done) - (lightxpos.get(item) - objectposx)) * data.shadownlength);
   	    				refrancey.add((pointypos.get(done) - (lightypos.get(item) - objectposy)));
   	    				
   	    				//When it is drawing it has to do polygons as Quadrilateral.
   	    				//Therefore it goes top to bottom and then bottom to top.
   	    				//This means there needs alternating system for adding the points 
   	    				//As these will later be used to draw the shadow
   	    				done += 1;
   	    				if(done<pointxpos.size())
   	    				{
   	    				polygonsx.add((pointxpos.get(done) - (lightxpos.get(item) - objectposx)) * data.shadownlength);
   	    				polygonsy.add((pointypos.get(done) - (lightypos.get(item) - objectposy)));
   	    				polygonsx.add(pointxpos.get(done));
   	    				polygonsy.add(pointypos.get(done));
   	    				//Used for making the ends
   	    				refrancex.add((pointxpos.get(done) - (lightxpos.get(item) - objectposx)) * data.shadownlength);
   	    				refrancey.add((pointypos.get(done) - (lightypos.get(item) - objectposy)));
   	    				}
   	    		}
        }
		//Drawing that
   	    for(int done2=0;done2<(polygonsx.size()/4);done2 += 1)
   	    {
   	    	
   	    	finalpolygonsx[0] = polygonsx.get((done2*4));
   	    	finalpolygonsx[1] = polygonsx.get((done2*4) + 1);
   	    	finalpolygonsx[2] = polygonsx.get((done2*4) + 2);
   	    	finalpolygonsx[3] = polygonsx.get((done2*4) + 3);
   	    	finalpolygonsy[0] = polygonsy.get((done2*4));
   	    	finalpolygonsy[1] = polygonsy.get((done2*4) + 1);
   	    	finalpolygonsy[2] = polygonsy.get((done2*4) + 2);
   	    	finalpolygonsy[3] = polygonsy.get((done2*4) + 3);
   	    	
   	    	data.buffergraphics.setColor(Color.BLACK);
   	    	data.buffergraphics.fillPolygon(finalpolygonsx,finalpolygonsy,4);
              	    	
   	    }
   	    
   	    //Making the ends of a shadow look right
   	    int done3 = 0;
   	    for(int done5=0;done5<lightxpos.size();done5 ++)
    	{	
   	        for(int done2=0;done2<pointxpos.size();done2++)
   	        {
   	        	endpolygonx[done2] = refrancex.get(done3 +done2);
   	        	endpolygony[done2] = refrancey.get(done3 + done2);
   	        }
   	        done3 += pointxpos.size();
   	    	data.buffergraphics.fillPolygon(endpolygonx,endpolygony,endpolygonx.length);
   	    	
    	}
   }

}
