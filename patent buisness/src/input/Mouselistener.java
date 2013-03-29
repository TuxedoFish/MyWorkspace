package input;

import java.awt.Event;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.xml.bind.Marshaller.Listener;

import startup.*;


public class Mouselistener extends Thread{
    
	
	public Data data;
	public JFrame JFrame;
   
	
	public Mouselistener(JFrame frame, Data parent)
	{
		data = parent;
        JFrame = frame;
	    listen();
	}//end constructor
    
	public void listen()
	{
		
		JFrame.addMouseListener(new MouseListener() 
		{

			public void mouseClicked(MouseEvent e)
			{
				
				System.out.println(data.mousexpos + " and " + data.mouseypos);
				
			}
			public void mouseEntered(MouseEvent e) {
				
			}
			public void mouseExited(MouseEvent e) {
			
			}
			public void mousePressed(MouseEvent e)
			{
				
				data.down = true;
				data.released = false;
				if(SwingUtilities.isLeftMouseButton(e))	
				{
				if(data.buttons.testbutton(10,data.sliderxpos,174, data.mousexpos, data.mouseypos))
				{
					
					data.sliderdown = true;
					
				}
				}
				
			}
			public void mouseReleased(MouseEvent e) 
			{
				if(!data.released)
				{
				if(data.sliderdown)
				{
					data.sliderdown = false;
				}
				data.released = true;
				if(SwingUtilities.isLeftMouseButton(e))	
				{
					
				if(data.buttons.testbutton(data.Slider, data.mousexpos, data.mouseypos))
				{
			      if(data.button!=1)
				  {	
					 data.MinToSearchFor = 4;
					 data.MaxToSearchFor = 6;
					 data.button = 1;
				  }else{
					  data.button = 0;
				  }
						
				}	
				
				if(data.buttons.testbutton(data.ProductionStaff, data.mousexpos, data.mouseypos))
				{
				   if(data.button!=2)
				   {	
					 data.MinToSearchFor = 0;
					 data.MaxToSearchFor = 2;
					 data.button = 2;
				   }else{
					 data.button = 0;
				   }
				}	
				
				if(data.buttons.testbutton(data.Expeditioners, data.mousexpos, data.mouseypos))
				{
			      if(data.button!=3)
				  {
					 data.MinToSearchFor = 2;
					 data.MaxToSearchFor = 6;
					 data.button = 3;
				  }else{
					  data.button = 0;
				  }
				}	
				
				if(data.buttons.testbutton(data.BrilliantExpeditioners, data.mousexpos, data.mouseypos))
				{
				   if(data.button!=4)
				   {
					 data.MinToSearchFor = 6;
					 data.MaxToSearchFor = 10;
					 data.button = 4;
				   }else{
					   
					  data.button = 0;
					   
				   }
				}	
					
				if(data.buttons.testbutton(data.phone.myshape, data.mousexpos, data.mouseypos))
				{
					if(!data.hiringtocome)
					{
					    data.stage = "hiringscreen";
					    data.button = 0;
					}else{
						System.out.println("insert text telling user they have already put a hiring request");
					}
				}
				
				if(data.buttons.testbutton(data.close, data.mousexpos, data.mouseypos))
				{
					
					if(data.stage.equals("hiringscreen"))
					{
						
						data.string.ClearField();
						data.stage = "gamehub";
						
					}
					
				}
				
				if(data.buttons.testbutton(data.back,data.mousexpos,data.mouseypos))
				{
					
					if(data.stage.contains("help"))
					{
						System.out.println("back");
						data.stage = "title";
					}
					
				}
				
				if(data.buttons.testbutton(data.play,data.mousexpos,data.mouseypos))
				{
					
					if(data.stage.contains("title"))
					{
						System.out.println("play");
						data.stage = "cutscene";
						data.transparencystage = 0;
					}
					
				}
				
				if(data.buttons.testbutton(data.Confirm,data.mousexpos,data.mouseypos))
				{
					
					System.out.println("data.button");
					if(data.stage == "hiringscreen")
					{
						if(data.button != 0)
						{
							data.reader.pickemployees(data.MinToSearchFor,data.MaxToSearchFor);
							data.string.ClearField();
							data.stage = "gamehub";
						}
					}
				}
				
				if(data.buttons.testbutton(data.close2,data.mousexpos,data.mouseypos))
				{
				  if(data.stage.contains("employee"))
				  {
					data.stage = "gamehub";
				  }
				}
				if(data.buttons.testbutton(data.help,data.mousexpos,data.mouseypos))
				{
					
					if(data.stage.contains("title"))
					{
						System.out.println("help");
						data.stage = "help";
				    }
				}
			   }
			  }
	         }
		
		});
		
	}
	

	
	public void run() 
	{
		
	
	
	}
	
}
