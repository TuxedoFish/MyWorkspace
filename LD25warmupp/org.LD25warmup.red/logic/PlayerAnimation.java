package logic;

import org.lwjgl.opengl.Display;

public class PlayerAnimation extends Thread{
	Player parent;
	
	public PlayerAnimation(Player parent) {
		this.parent = parent;
		this.start();
	}
	@Override
	public void run() {
		while(!Display.isCloseRequested()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(1);
			}
			parent.updateAnimation();
		}
	}
}
