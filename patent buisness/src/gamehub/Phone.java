package gamehub;

import java.awt.Image;
import java.awt.Polygon;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import startup.Data;


public class Phone {

	public Image me;
	public Image metouched;
	public Image mepressed;
	public int xpos = 463;
	public int ypos = 362;
	public File mylocation = new File("src\\gamehubresources\\phone.png");
	public File mytouchedlocation = new File("src\\gamehubresources\\phonetouched.png");
	public File mypressedlocation = new File("src\\gamehubresources\\phonepressed.png");
	public int myxpos[] = {487,510,499,470,464,477};
	public int myypos[] = {362,370,400,391,376,379};
	public Polygon myshape = new Polygon(myxpos, myypos, 6);
	public Data data;
	
	public Phone(Data parent)
	{
		
		data = parent;
		try 
		{
			
			metouched = ImageIO.read(mytouchedlocation);
			mepressed = ImageIO.read(mypressedlocation);
			me = ImageIO.read(mylocation);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void update()
	{
		
		
		if(data.buttons.testbutton(myshape, data.mousexpos, data.mouseypos))
		{
			
			if(data.released)
			{
				
				data.buffergraphics.drawImage(metouched,xpos,ypos,null);
				
			}else
			{
				
				data.buffergraphics.drawImage(mepressed,xpos,ypos,null);
				
			}
			
		}else{
			
			data.buffergraphics.drawImage(me,xpos,ypos,null);
			
		}
		
	}
	
}
