package startup;

import java.awt.Graphics;
import javax.swing.JFrame;
import input.*;


public class Frame {

	
	public static void main(String args[])
	{
		JFrame frame = new JFrame();
		
		Data data = new Data();
        Main main = new Main(data);
		Keylistener keylistener = new Keylistener(frame,data);
		MouseMotionlistener mousemotionlistener= new MouseMotionlistener(frame,data);
		Mouselistener mouselistener= new Mouselistener(frame,data);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(main);
		frame.setSize(640,480);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
	}
	
	
	public void setup()
	{
		
	}
	
}
