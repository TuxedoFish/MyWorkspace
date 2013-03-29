package gameelements;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import startup.Data;

public class HiringButtons {

	Data data;
	
	public HiringButtons(Data parent)
	{
		
		data = parent;
		
		try {
			data.Tick = ImageIO.read(new File("src\\resources\\tick.png"));
			data.clickbox = ImageIO.read(new File("src\\resources\\clickbox.png"));
			data.clickboxtouched = ImageIO.read(new File("src\\resources\\clickboxmouseover.png"));
			data.clickboxpressed = ImageIO.read(new File("src\\resources\\clickboxpressed.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public void update()
	{
		
		data.buffergraphics2D.drawImage(data.hiringscreen,0,0,null);
		data.string.ClearField();
		data.string.DrawString("intelligence", 70, 83,0);
		data.string.DrawString("by type", 231, 135,0);
		data.string.DrawString("production staff", 131, 263,0);
		data.string.DrawString("expeditioners",131, 323,0);
		data.string.DrawString("brilliant expeditioners",131, 383,0);
		//Close button
		if(data.buttons.testbutton(data.close,data.mousexpos,data.mouseypos))
		{
			
			if(data.released)
			{
				
				data.buffergraphics2D.drawImage(data.closetouchedbutton,500,90,null);
				
			}else{
				
				data.buffergraphics2D.drawImage(data.closepressedbutton,500,90,null);
				
			}
			
		}else{
			data.buffergraphics2D.drawImage(data.closebutton,500,90,null);
			
		}
		//Slider binary button
		if(data.buttons.testbutton(data.Slider,data.mousexpos,data.mouseypos))
		{
			
			if(data.released)
			{
				
				data.buffergraphics2D.drawImage(data.clickboxtouched,274,138,null);
				
			}else{
				
				data.buffergraphics2D.drawImage(data.clickboxpressed,274,138,null);
				
			}
			
		}else{

			data.buffergraphics2D.drawImage(data.clickbox,274,138,null);
			
		}
		//ProductionStaff Binary button
		if(data.buttons.testbutton(data.ProductionStaff,data.mousexpos,data.mouseypos))
		{
			
			if(data.released)
			{
				
				data.buffergraphics2D.drawImage(data.clickboxtouched,222,265,null);
				
			}else{
				
				data.buffergraphics2D.drawImage(data.clickboxpressed,222,265,null);
				
			}
			
		}else{

			data.buffergraphics2D.drawImage(data.clickbox,222,265,null);
			
		}
		//Expeditioners binary button
		if(data.buttons.testbutton(data.Expeditioners,data.mousexpos,data.mouseypos))
		{
			
			if(data.released)
			{
				
				data.buffergraphics2D.drawImage(data.clickboxtouched,206,328,null);
				
			}else{
				
				data.buffergraphics2D.drawImage(data.clickboxpressed,206,328,null);
				
			}
			
		}else{

			data.buffergraphics2D.drawImage(data.clickbox,206,328,null);
			
		}
		//Brilliant Expeditioners binary button
		if(data.buttons.testbutton(data.BrilliantExpeditioners,data.mousexpos,data.mouseypos))
		{
			
			if(data.released)
			{
				
				data.buffergraphics2D.drawImage(data.clickboxtouched,246,387,null);
				
			}else{
				
				data.buffergraphics2D.drawImage(data.clickboxpressed,246,387,null);
				
			}
			
		}else{

			data.buffergraphics2D.drawImage(data.clickbox,246,387,null);
			
		}
		
		switch(data.button)
		{
		
		case(1):
		data.buffergraphics2D.drawImage(data.Tick,274,138,null);
		break;
		
		case(2):
		data.buffergraphics2D.drawImage(data.Tick,222,265,null);
		break;
		
		case(3):
		data.buffergraphics2D.drawImage(data.Tick,206,328,null);
		break;
		
		case(4):
		data.buffergraphics2D.drawImage(data.Tick,246,387,null);
		break;
		
		}
		
		data.buffergraphics2D.fillOval(data.sliderxpos, 174, 10, 10);
		
		if(data.buttons.testbutton(data.Confirm,data.mousexpos,data.mouseypos))
		{
			
			if(data.released)
			{
				
				data.buffergraphics2D.drawImage(data.masterbuttonmouseover,450,380,null);
				data.string.DrawString("confirm",480,380,0);
				
			}else{
				
				data.buffergraphics2D.drawImage(data.masterbuttonpressed,450,380,null);
				data.string.DrawString("confirm",480,380,0);
				
			}
			
		}else{
			
			data.buffergraphics2D.drawImage(data.masterbutton,450,380,null);
			data.string.DrawStringWhite("confirm",480,380,0);
			
		}
		
		if(data.sliderdown && data.button == 1)
		{
			if(data.mousexpos + 5 < 391 && data.mousexpos - 5 > 212)
			{	
				data.sliderxpos = data.mousexpos - 5;
				
				if((Math.round(((data.sliderxpos - 212.0) / 169.0) * 10.0)) - 1 < 0.0)
				{
					data.MinToSearchFor = (int) (Math.round(((data.sliderxpos - 212.0) / 169.0) * 10.0));
				}
				else
				{
					data.MinToSearchFor = (int) (Math.round(((data.sliderxpos - 212.0) / 169.0) * 10.0)) - 1;
				}
				if((Math.round(((data.sliderxpos - 212.0) / 169.0) * 10.0)) + 1 > 10)
				{
					data.MaxToSearchFor = (int) (Math.round(((data.sliderxpos - 212.0) / 169.0) * 10.0));
				}
				else
				{
					data.MaxToSearchFor = (int) (Math.round(((data.sliderxpos - 212.0) / 169.0) * 10.0)) + 1;
				}
			}
		}
		
	}
	
}
