package logic;

import org.lwjgl.opengl.Display;

import start.Controller;

public class PlayerAnimation extends Thread{
	Player parent;
	Controller parent2;
	
	public PlayerAnimation(Player parent, Controller parent2) {
		this.parent = parent;
		this.parent2 = parent2;
		this.start();
	}
	@Override
	public void run() {
		while(!parent2.getStopped()) {
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
