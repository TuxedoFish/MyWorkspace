package genreal;

import startup.Data;

public class TimeCalculator implements Runnable
{
	
	public Data data;
	
	public TimeCalculator(Data parent)
	{
		
		data = parent;
		
	}

	public void run() 
	{
		while(true)
		{
		    data.time += 1;
		    if(data.hiringtocome)
		    {
		    
		    	data.waituntilshow -= 1;
		    	if(data.waituntilshow == 0)
		    	{
		    		
		    		data.hiringtocome = false;
		    		data.stage = "employee";
		    		
		    	}
		    	
		    }
		    
		    try
		    {
				Thread.sleep(100);
			} catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		    
		}
	}

}
