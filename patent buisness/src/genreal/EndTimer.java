package genreal;

import java.util.TimerTask;

import startup.Data;

public class EndTimer extends TimerTask{

	Data data;
	
    public EndTimer(Data parent)
    {
    	
    	data = parent;
    	
    }
	
	public void run() 
	{
		
		 data.transparencystage = 1;
		 System.out.println("finished");
		 data.stage = "cutscene(b)transition";
		 data.wait = false;
	
	}

}
