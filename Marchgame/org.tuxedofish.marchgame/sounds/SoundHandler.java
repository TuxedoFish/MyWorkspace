package sounds;

import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundHandler {
	public static synchronized void playSound(final String url) {
	    new Thread(new Runnable() { // the wrapper thread is unnecessary, unless it blocks on the Clip finishing, see comments
	      public void run() {
	        try {
	          InputStream myStream = new BufferedInputStream(getClass().getResourceAsStream(url)); 
	          AudioInputStream audio2 = AudioSystem.getAudioInputStream(myStream);
	          
	          Clip clip = AudioSystem.getClip();
	          clip.open(audio2);
	          clip.start(); 
	        } catch (Exception e) {
	          System.err.println(e.getMessage());
	        }
	      }
	    }).start();
	  }
}
