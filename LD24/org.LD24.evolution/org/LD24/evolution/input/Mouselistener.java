package org.LD24.evolution.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.LD24.evolution.Projectiles.PlayerBullet;
import org.LD24.evolution.graphics.Data;

/**
 * Gets mouse input
 * @author harry
 *
 */
public class Mouselistener {
	private Data data;
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

			public void mouseClicked(MouseEvent e) {
			}
			public void mouseEntered(MouseEvent e) {
			}
			public void mouseExited(MouseEvent e) {
			}
			public void mousePressed(MouseEvent e) {
				data.playerimage = 1;
			}
			public void mouseReleased(MouseEvent e) {
				if(data.playerimage == 1 && data.started && data.timerup) {
					data.playerimage = 2;
					data.mousex = e.getX();
					data.mousey = e.getY();
					data.playerbullets.add(new PlayerBullet(data.playerx + 35,430));
					fired("shoot.wav");
				}
			}
		
		});
		
	}
	public static synchronized void playSound(final String url) {
	    new Thread(new Runnable() { // the wrapper thread is unnecessary, unless it blocks on the Clip finishing, see comments
	      public void run() {
	        try {
	          
	        } catch (Exception e) {
	          System.err.println(e.getMessage());
	        }
	      }
	    }).start();
	  }
	public synchronized void fired(final String url) {
		 new Thread(new Runnable() { // the wrapper thread is unnecessary, unless it blocks on the Clip finishing, see comments
		      public void run() {
				try {
					InputStream myStream = new BufferedInputStream(getClass().getResourceAsStream(url)); 
			        AudioInputStream audio2;
					audio2 = AudioSystem.getAudioInputStream(myStream);
		            Clip clip = AudioSystem.getClip();
		            clip.open(audio2);
		            clip.start(); 
				} catch (UnsupportedAudioFileException e) {
					System.err.println("error : " + e.getLocalizedMessage());
					System.exit(1);
				} catch (LineUnavailableException e) {
					System.err.println("error : " + e.getLocalizedMessage());
					System.exit(1);
				} catch (IOException e) {
					System.err.println("error : " + e.getLocalizedMessage());
					System.exit(1);
				}
		      try {
			     Thread.sleep(300);
			  } catch (InterruptedException e) {
				 e.printStackTrace();
			  }
		      data.playerimage = 0;
		      }
		    }).start();
	}
}
