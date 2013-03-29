package startup;

public class TitleScreen {

	public Data data;
	
	public TitleScreen(Data parent)
	{
		
		data = parent;
		
	}
	
	public void update()
	{
		
		data.string.ClearField();
		data.buffergraphics2D.drawImage(data.startscreen,0,0,null);
		
		if(data.buttons.testbutton(data.play,data.mousexpos,data.mouseypos))
		{
			
			if(data.released)
			{
				
				data.buffergraphics2D.drawImage(data.masterbuttonmouseover,295,150,null);
				data.string.DrawString("play",325,150,0);
				
			}else{
				
				data.buffergraphics2D.drawImage(data.masterbuttonpressed,295,150,null);
				data.string.DrawString("play",325,150,0);
				
			}
			
		}else{
			
			data.buffergraphics2D.drawImage(data.masterbutton,295,150,null);
			data.string.DrawStringWhite("play",325,150,0);
			
		}
		
		if(data.buttons.testbutton(data.help,data.mousexpos,data.mouseypos))
		{
			
			if(data.released)
			{
				
				data.buffergraphics2D.drawImage(data.masterbuttonmouseover,295,250,null);
				data.string.DrawString("help",325,250,0);
				
			}else{
				
				data.buffergraphics2D.drawImage(data.masterbuttonpressed,295,250,null);
				data.string.DrawString("help",325,250,0);
				
			}
			
		}else{
			
			data.buffergraphics2D.drawImage(data.masterbutton,295,250,null);
			data.string.DrawStringWhite("help",325,250,0);
			
		}
		
	}
	
}
