package startup;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;
import java.util.Timer;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import resources.*;
import gamehub.*;
import genreal.*;

public class Main extends JPanel{

	private static final long serialVersionUID = 1L;
	public Data data;
	public int TotalX;
	Timer timer = new Timer();
	
	public Main(Data parent)
    {
		
		data = parent;
		data.timecalculatorthread.start();
		repaint();
		
		try {
			
			data.startscreen = ImageIO.read(getClass().getResource("titlescreen.png"));
			data.cutscene = ImageIO.read(new File("src\\resources\\cutscene.png"));
			data.gamehub = ImageIO.read(new File("src\\resources\\gamehub.png"));
		    data.helpscreen = ImageIO.read(new File("src\\resources\\helpscreen.png"));
			data.hiringscreen = ImageIO.read(new File("src\\resources\\hiringscreen.png"));
			data.closebutton = ImageIO.read(new File("src\\resources\\close.png"));
			data.closetouchedbutton = ImageIO.read(new File("src\\resources\\closemouseover.png"));
			data.closepressedbutton = ImageIO.read(new File("src\\resources\\closepressed.png"));
			data.Tileset = ImageIO.read(new File("src\\resources\\letters.png"));
			data.masterbutton = ImageIO.read(new File("src\\resources\\confirmbutton.png"));
			data.masterbuttonmouseover = ImageIO.read(new File("src\\resources\\confirmbuttonmouseover.png"));
			data.masterbuttonpressed = ImageIO.read(new File("src\\resources\\confirmbuttonpressed.png"));
			data.employee = ImageIO.read(new File("src\\gamehubresources\\employee.png"));
			//Get Alphabet
			for(int gety=0;gety<(data.Tileset.getHeight()/20);gety ++)
			{
				for(int getx=0;getx<(data.Tileset.getWidth()/20);getx ++)
				{
					data.Keys.add(data.tile.gettile(data.Tileset, 20, getx, gety));
					data.KeyWidths.add(data.tile.gettileWidth(data.Tileset, 20, getx, gety,1));
				}
			}
			
		    } catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	public void paint(Graphics g)
	{
		
		//clear
		data.buffergraphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		data.buffergraphics2D.setColor(Color.WHITE);
		data.buffergraphics2D.fillRect(0, 0, 640, 480);

		
		if(data.stage.equals("title") || data.stage.equals("cutscene") || data.stage.equals("help"))
		{
			data.titleclass.update();
		}	
		if(data.stage.equals("cutscene(b)") || data.stage.equals("cutscene(b)transition"))
		{
			data.buffergraphics2D.drawImage(data.cutscene,0,0,this);
		}
		if(data.stage.equals("help"))
		{
		    data.helpclass.update();
		}
		if(data.stage.equals("gamehub") || data.stage.equals("employee"))
		{

		    data.string.ClearField();
			data.buffergraphics2D.drawImage(data.gamehub,0,0,this);
			data.phone.update();
			if(data.stage.equals("employee"))
			{
				data.employeeclass.Update();
			}
			
		}
		if(data.stage.equals("hiringscreen"))
		{
			data.hiringbuttons.update();
		}
		
		data.transition.update();
		
		if(data.stage.equals("cutscene(b)") && data.transparencystage >= data.fadespeed*2 - 1)
		{ 
			 data.stage = "cutscene(b)transition";
			 wait5secs(); 
		}
		 
		if(data.stage.equals("cutscene(b)transition") && data.transparencystage >= data.fadespeed  && !data.wait)
		{
			 data.stage = "gamehub";
		}
		   
		if(data.stage.equals("cutscene") && data.transparencystage >= data.fadespeed)
		{  
			 data.string.ClearField();
			 data.stage = "cutscene(b)";
		}
		//Draw buffered image
		g.drawImage(data.Offscreen,0,0,this);
		//Draw Text field
		g.drawImage(data.string.TextScreen,0,0,this);
        //Draw Transition Screen
		g.drawImage(data.Overlayscreen,0,0,this);
		repaint();
	}
	
	public synchronized void wait5secs()
	{
		
		 data.wait = true;
		 timer.schedule(new EndTimer(data), 5000);
		
	}
	
}