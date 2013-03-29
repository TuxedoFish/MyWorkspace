package genreal;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.color.ColorSpace;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageFilter;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorConvertOp;

import startup.Data;

public class Strings {

	Data data;
	public BufferedImage TextScreen = new BufferedImage(640,480,BufferedImage.TYPE_INT_ARGB);
	public Graphics2D TextGraphics = TextScreen.createGraphics();
	
	public Strings(Data parent)
	{
		
		data = parent;
		
	}
	
	public void ClearField()
	{
		
		TextGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f));
		Rectangle2D.Double rect = new Rectangle2D.Double(0,0,640,480); 
		TextGraphics.fill(rect);
		TextGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
	
	}
		
	public void DrawStringWhite(String WordToPrint,int StartX,int StartY,int MaxX)
	{
		
		String temp;
		int xpos = StartX;
		int ypos = StartY;
		int testx = 0;
		
		for(int print=0;print<WordToPrint.length();)
		{
			
			temp = WordToPrint.substring(print,print + 1);
			if(temp.contains(" "))
			{
				print += 1;
				xpos += 5;
				testx = xpos;
				for(int done3=print + 1;!WordToPrint.substring(done3,done3 + 1).contains(" ") && done3 + 1 < WordToPrint.length();done3++)
					{
						testx += data.KeyWidths.get(data.KeyLocations.indexOf(WordToPrint.substring(done3,done3+1)));
						
						
							if(testx >= MaxX && MaxX != 0)
							{
								
								ypos += 20;
								xpos = StartX;
								
							}
						
						
					}
				
			}
			else
			{
				
			if(xpos + data.KeyWidths.get(data.KeyLocations.indexOf(temp)) >= MaxX && MaxX != 0)
			{
				xpos = StartX;
				ypos += 20;
			}
			TextGraphics.drawImage(colorImage(data.Keys.get(data.KeyLocations.indexOf(temp)),255,255,255),xpos,ypos,null);
			
			xpos = StartX;
			ypos = StartY;
			print += 1;
			
			for(int done2=0;done2<print;done2++)
			{
				  if(!WordToPrint.substring(done2,done2+1).contains(" "))
				  {
					  xpos += data.KeyWidths.get(data.KeyLocations.indexOf(WordToPrint.substring(done2,done2+1)));
				  }
				  else
				  {
					  xpos += 5;
					  testx = xpos;
					  for(int done3=done2 + 1;!WordToPrint.substring(done3,done3 + 1).contains(" ") && done3 + 1 < WordToPrint.length();done3++)
						{
							testx += data.KeyWidths.get(data.KeyLocations.indexOf(WordToPrint.substring(done3,done3+1)));
							
							
								if(testx >= MaxX && MaxX != 0)
								{
									
									ypos += 20;
									xpos = StartX;
									
								}
							
							
						}
				  }
				  
				  if(xpos >= MaxX && MaxX != 0)
				  {
						xpos = StartX;
						ypos += 20;
				  }
				  
		    }
		  }
		}
	}
	
	public void DrawString(String WordToPrint,int StartX,int StartY,int MaxX)
	{
		
		String temp;
		int xpos = StartX;
		int ypos = StartY;
		int testx = StartX;
		boolean find = false;
		
		for(int print=0;print<WordToPrint.length();)
		{
			
			temp = WordToPrint.substring(print,print + 1);

			
			if(temp.contains(" "))
			{
				print += 1;
				xpos += 5;
				testx = xpos;
				for(int done3=print + 1;!WordToPrint.substring(done3,done3 + 1).contains(" ") && done3 + 1 < WordToPrint.length() && find;done3++)
					{
						testx += data.KeyWidths.get(data.KeyLocations.indexOf(WordToPrint.substring(done3,done3+1)));
						
						
							if(testx >= MaxX && MaxX != 0)
							{
								
								ypos += 20;
								xpos = StartX;
								find = false;
								
							}
						
						
					}
			}
			else
			{
			TextGraphics.drawImage(data.Keys.get(data.KeyLocations.indexOf(temp)),xpos,ypos,null);
			
			xpos = StartX;
			ypos = StartY;
			print += 1;
			
			for(int done2=0;done2<print;done2++)
			{
				  if(!WordToPrint.substring(done2,done2+1).contains(" "))
				  {
					  xpos += data.KeyWidths.get(data.KeyLocations.indexOf(WordToPrint.substring(done2,done2+1)));
				  }
				  else
				  {
					  find = true;
					  xpos += 5;
				      testx = xpos;
					  for(int done3=done2 + 1;!WordToPrint.substring(done3,done3 + 1).contains(" ") && done3 + 1 < WordToPrint.length() && find;done3++)
						{
							testx += data.KeyWidths.get(data.KeyLocations.indexOf(WordToPrint.substring(done3,done3+1)));
							
							
								if(testx >= MaxX && MaxX != 0)
								{
									
									ypos += 20;
									xpos = StartX;
									find = false;
									
								}
							
							
						}
				  }
				  /*if(xpos >= MaxX && MaxX != 0)
				  {
						ypos += 20;
				  }
				  */
				  if(find)
				  {
				
				  }
		    }
		}
	  }
	}	
	
	public BufferedImage colorImage(BufferedImage loadImg, int red, int green, int blue) {
		
	    BufferedImage img = new BufferedImage(loadImg.getWidth(), loadImg.getHeight(),BufferedImage.OPAQUE);
	    Graphics2D graphics = img.createGraphics(); 
	    Color newColor = new Color(red, green, blue, 0 /* alpha needs to be zero */);
	    graphics.setXORMode(newColor);
	    graphics.drawImage(loadImg, null, 0, 0);
	    graphics.dispose();
	    return img;
	}
	
}
