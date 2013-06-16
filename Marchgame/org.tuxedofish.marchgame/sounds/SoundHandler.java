package sounds;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundHandler {
	public static synchronized void playSound(final Clip clip) {
		new Thread(new Runnable() {
	      public void run() {
	        try {
	          clip.stop();
	          clip.setFramePosition(0);
	          FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
	          volume.setValue(-15);
	          clip.start(); 
	        } catch (Exception e) {
	          System.err.println(e.getMessage());
	        }
	      }
		}).start();
	}
	public Clip loadClip(String url) {
        try {
	        return AudioSystem.getClip();
		} catch (LineUnavailableException e) {
			System.err.println("couldnt load sound");
			e.printStackTrace();
			System.exit(1);
			return null;
		}
	}
	public AudioInputStream getAudioStream(String url) {
		try {
			InputStream myStream = new BufferedInputStream(getClass().getResourceAsStream(url)); 
			return AudioSystem.getAudioInputStream(myStream);
		} catch (UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
			System.err.println("err loading sound");
			System.exit(1);
			return null;
		}
	}
}
