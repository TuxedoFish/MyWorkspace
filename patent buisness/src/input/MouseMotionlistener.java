package input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import startup.*;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;



public class MouseMotionlistener extends JFrame
{

	public startup.Data data;
	public JFrame JFrame;
	public Boolean mode;
    public int temp;
	
	public MouseMotionlistener(JFrame frame, Data parent)
	{
		data = parent;
        JFrame = frame;
	    listen();
	   
	}//end constructor

	
	public void listen()
	{
		JFrame.addMouseMotionListener(new MouseMotionListener() 
		{

			
			public void mouseDragged(MouseEvent arg0) 
			{
                if(data.released)
                {
                	if(data.down)
                	{
                		data.down = false;
                	}else{
                		data.down = true;
                	     }
                	data.released = false;
                }
                
				data.mousexpos = arg0.getX() - 3;
				data.mouseypos = arg0.getY() - 25;
				
				if(SwingUtilities.isLeftMouseButton(arg0))	
				{
				if(data.buttons.testbutton(10,data.sliderxpos,174, data.mousexpos, data.mouseypos))
				{
					data.sliderdown = true;
				}
				}
				
			}

			
			public void mouseMoved(MouseEvent arg0) 
			{
				
				data.mousexpos = arg0.getX() - 3;
				data.mouseypos = arg0.getY() - 25;
			
			}
		
	   });
	}

}

	
	
	

