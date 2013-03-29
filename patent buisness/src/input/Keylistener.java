package input;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import startup.*;


public class Keylistener extends JFrame implements KeyListener {

	public Data data;
	public JFrame JFrame;
	
	public Keylistener(JFrame frame, Data parent)
	{
		data = parent;
        JFrame = frame;
	    listen();
	    
	}//end constructor

	
	public void listen()
	{
		JFrame.addKeyListener(new KeyListener() {
			
		public void keyPressed(KeyEvent arg0) 
		{
		
		
		
		}
		public void keyReleased(KeyEvent evt) 
		{
			
			
			
		}


		
		public void keyTyped(KeyEvent e) {
		
			
	    
		}
		
	   });
	}

	public void keyPressed(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}    //Needed
	public void keyTyped(KeyEvent e) {}

}

	
	
	

