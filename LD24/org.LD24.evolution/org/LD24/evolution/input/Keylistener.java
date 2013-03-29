package org.LD24.evolution.input;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;

import org.LD24.evolution.graphics.Data;
/**
 * Gets key input
 * @author harry
 *
 */
public class Keylistener extends JFrame {

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
			
		public void keyPressed(KeyEvent evt) {
			if(evt.getKeyCode() == data.optright) {
				data.rightdown = true;
				data.playerdir = 3;
			}
			if(evt.getKeyCode() == data.startbutton) {
				data.started = true;
				data.mytimerthread.start();
			}
			if(evt.getKeyCode() == data.optleft) {
				data.leftdown = true;
				data.playerdir = 0;
			}
		}
		public void keyReleased(KeyEvent evt) {
			if(evt.getKeyCode() == data.optright) {
				data.rightdown = false;
			}
			if(evt.getKeyCode() == data.optleft) {
				data.leftdown = false;
			}
		}
		public void keyTyped(KeyEvent evt) {}
		
	   });
	}

}

	
	
	

