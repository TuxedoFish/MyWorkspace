package startup;

public class HelpScreen {

	public Data data;
	
	public HelpScreen(Data parent)
	{
		
		data = parent;
		
	}
	
	public void update()
	{
		
		data.string.ClearField();
		data.buffergraphics2D.drawImage(data.helpscreen,0,0,null);
		
		if(data.buttons.testbutton(data.back,data.mousexpos,data.mouseypos))
		{
			
			if(data.released)
			{
				
				data.buffergraphics2D.drawImage(data.masterbuttonmouseover,500,350,null);
				data.string.DrawString("back",530,350,0);
				
			}else{
				
				data.buffergraphics2D.drawImage(data.masterbuttonpressed,500,350,null);
				data.string.DrawString("back",530,350,0);
				
			}
			
		}else{
			
			data.buffergraphics2D.drawImage(data.masterbutton,500,350,null);
			data.string.DrawStringWhite("back",530,350,0);
			
		}
		
	}
	
}
