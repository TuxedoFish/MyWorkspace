package main;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JFrame;



public class MouseMotionlistener extends JFrame{

	public Data data;
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
		JFrame.addMouseMotionListener(new MouseMotionListener() {

			
			public void mouseDragged(MouseEvent arg0) {

		     	
			
			}

			
			public void mouseMoved(MouseEvent arg0) {
							
				data.mousexpos = arg0.getX();
				data.mouseypos = arg0.getY() - 24;
	       
			}
			
		
	   });
	}
	
	public int findxandypos(int xpostofind,int ypostofind,ArrayList<Integer> xs, ArrayList<Integer> ys)
	{
		
		for(int done=0;done<xs.size();done++)
		{
			if(xs.get(done) == xpostofind && ys.get(done) == ypostofind)
			{
				System.out.println(done);
				return done;
			}
		}
		return 0;
	}

}

	
	
	

