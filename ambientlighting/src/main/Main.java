package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Main extends JPanel{

	public Data data;
	public int red;
	public int green;
	public int blue;
	public Color temp;
	public int awayfroms;	
	
	public Main(Data parent)
	{
		data = parent;
		repaint();
		for(int test=0;test<10;test++)
		{
			data.lightxpos.add(167 + test*35);
			data.lightypos.add(90);
			data.lightintensity.add(150);
			data.lightbrightness.add(170);
			data.lightred.add(100);
			data.lightgreen.add(40);
			data.lightblue.add(0);
		}
		// */
		lightsource(data.lightxpos,data.lightypos,data.lightintensity,data.lightbrightness,data.lightred,data.lightgreen,data.lightblue,data.testimage);
    	}
	

    public void paint(Graphics g)
    {
    	data.buffergraphics.setColor(Color.GRAY);
    	data.buffergraphics.fillRect(0, 0, 640, 480);
    	//Pretty pictures 
    	{data.buffergraphics.setColor(Color.ORANGE);
    	data.buffergraphics.fillRect(140,300,70,70);
    	data.buffergraphics.fillRect(300,140,200,200);
    	data.buffergraphics.setColor(Color.PINK);
    	data.buffergraphics.fillRect(100,100,140,140);}
	    //Set positions of player/object
    	data.playerxpos.set(0, data.mousexpos - 5);
    	data.playerypos.set(0, data.mouseypos + 10);
    	
    	data.playerxpos.set(1, data.mousexpos - 5);
    	data.playerypos.set(1, data.mouseypos);
    	
    	data.playerxpos.set(2, data.mousexpos + 3);
    	data.playerypos.set(2, data.mouseypos + 10);

    	data.playerxpos.set(3, data.mousexpos - 5);
    	data.playerypos.set(3, data.mouseypos + 10);
    	
    	data.graphics.shadows(data.playerxpos, data.playerypos, data.lightxpos, data.lightypos,data.mousexpos,data.mouseypos);
		data.buffergraphics.setColor(Color.BLUE);
	    data.buffergraphics.fillRect(data.mousexpos - 5, data.mouseypos, 10, 10);
	    data.buffergraphics.drawImage(data.testimage,0,0,this);
	    g.drawImage(data.Offscreen,0,0,this);
	    repaint();
	    
    }
  
    public void lightsource(ArrayList<Integer> lightxpos,ArrayList<Integer> lightypos,ArrayList<Integer> lightintensity,ArrayList<Integer> lightbrightness,ArrayList<Integer> lightred,ArrayList<Integer> lightgreen,ArrayList<Integer> lightblue,BufferedImage screen)
   	{
   
    	for(int x=0; x<screen.getWidth();x++)
       	{
       		for(int y=0; y<screen.getHeight();y++)
           	{
       		  awayfroms = 0;
       		  for(int done=0; done<lightxpos.size();done +=1)
       	      {
       			  if((Math.hypot(lightxpos.get(done) - x, lightypos.get(done) - y ) > lightintensity.get(done)));
       			  {
       				  
       				  awayfroms += 1;
       			   
       			  }
           	 }
       		if(awayfroms == lightxpos.size())
       		{
       			temp = new Color(0,0,0,230);
 				  screen.setRGB(x,y, temp.getRGB());
       		}
       	}	
     }	
    for(int item=0; item<lightxpos.size();item++)
    {
       	for(int done = 0; done<lightintensity.get(item);done++)
       	{
       		for(int doney = 0; doney<lightintensity.get(item);doney++)
           	{
       			if(Math.hypot(lightxpos.get(item) - (lightxpos.get(item) - lightintensity.get(item)/2 + done), lightypos.get(item) - (lightypos.get(item) - lightintensity.get(item)/2 + doney)) < lightintensity.get(item));
        		   {
        			   
       				temp = new Color(screen.getRGB(lightxpos.get(item) - lightintensity.get(item)/2 + done, lightypos.get(item) - lightintensity.get(item)/2 + doney));
       				
       				red = (int) (temp.getRed() + lightbrightness.get(item) - (Math.hypot(lightxpos.get(item) - (lightxpos.get(item) - lightintensity.get(item)/2 + done), lightypos.get(item) - (lightypos.get(item) - lightintensity.get(item)/2 + doney)) * 2 * (lightbrightness.get(item)/lightintensity.get(item))) + lightred.get(item));
       				green = (int) (temp.getGreen() + lightbrightness.get(item) - (Math.hypot(lightxpos.get(item) - (lightxpos.get(item) - lightintensity.get(item)/2 + done), lightypos.get(item) - (lightypos.get(item) - lightintensity.get(item)/2 + doney)) * 2 * (lightbrightness.get(item)/lightintensity.get(item))) + lightgreen.get(item));
       				blue = (int) (temp.getBlue() + lightbrightness.get(item) - (Math.hypot(lightxpos.get(item) - (lightxpos.get(item) - lightintensity.get(item)/2 + done), lightypos.get(item) - (lightypos.get(item) - lightintensity.get(item)/2 + doney)) * 2 * (lightbrightness.get(item)/lightintensity.get(item))) + lightblue.get(item));
       				
       				if(green>255)
       				{
       					green = 255;
       				}else{
       					if(green<0)
       					{
       						green = 0;
       					}
       				}
       				if(red>255)
       				{
       					red = 255;
       				}else{
       					if(red<0)
       					{
       						red = 0;
       					}
       				}
       				if(blue>255)
       				{
       					blue = 255;
       				}else{
       					if(blue<0)
       					{
       						blue = 0;
       					}
       				}
       				
       				       				
       				
       				temp = new Color(red,green,blue,200);
       				
       				if(Math.hypot(lightxpos.get(item) - (lightxpos.get(item) - lightintensity.get(item)/2 + done), lightypos.get(item) - (lightypos.get(item) - lightintensity.get(item)/2 + doney)) * 2 * (lightbrightness.get(item)/lightintensity.get(item)) > lightintensity.get(item))
       				{
       					
       				}else{
       				screen.setRGB(lightxpos.get(item) - lightintensity.get(item)/2 + done, lightypos.get(item) - lightintensity.get(item)/2 + doney, temp.getRGB());
       				     }
       				
       			} 
           }
       	}
    }  	
        WritableRaster raster = screen.getRaster();
      
       	repaint();
       	
   	}	
}
