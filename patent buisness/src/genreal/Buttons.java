package genreal;

import java.awt.Polygon;
import java.awt.Rectangle;
import startup.*;

public class Buttons 
{
	
	public Rectangle mouse = new Rectangle(0,0,0,0);
	public Data data;
	
	public Buttons(Data parent)
	{
		
		data = parent;
		
	}
	
	public boolean testbutton(Rectangle button,int mousexpos,int mouseypos)
	{
		mouse.setBounds(mousexpos,mouseypos,2,2);
			
		    if(button.intersects(mouse))
			{
				
				return true;
				
			}else{
				
						return false;
		
				 }
	}
	
	public boolean testbutton(Polygon button,int mousexpos,int mouseypos)
	{
		mouse.setBounds(mousexpos,mouseypos,2,2);
			
		    if(button.contains(mouse))
			{
				
				return true;
				
			}else{
				
						return false;
		
				 }
	}
	
	public boolean testbutton(int radiusofbutton,int xposofcircle,int yposofcircle,int mousexpos,int mouseypos)
	{
		
		    if(Math.round(Math.hypot(mousexpos - xposofcircle, mouseypos - yposofcircle)) < radiusofbutton)
			{
				
				return true;
				
			}else{
				
						return false;
		
				 }
	}
	
	
}
