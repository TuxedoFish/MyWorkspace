package main;

import java.awt.Graphics;
import javax.swing.JFrame;



public class Frame {

	
	public static void main(String args[])
	{
		JFrame frame = new JFrame();
		Graphics frameContext = frame.getGraphics();
		
		Data data = new Data();
        Main main = new Main(data);
        MouseMotionlistener mousemotion = new MouseMotionlistener(frame,data);
        
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
        frame.add(main);
		frame.setSize(640,480);
		frame.setVisible(true);
		frame.setResizable(false);
	}
	
	
	public void setup()
	{
		
	}
	
}
