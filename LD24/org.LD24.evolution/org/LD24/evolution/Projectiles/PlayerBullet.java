package org.LD24.evolution.Projectiles;

public class PlayerBullet {
	private int xpos;
	private int ypos;
	
	public PlayerBullet(int xpos, int ypos) {
		this.xpos = xpos;
		this.ypos = ypos;
	}
	public void update() {
		this.ypos -= 3;
	}
	public int getXpos() {
		return xpos;
	}
	public void setXpos(int xpos) {
		this.xpos = xpos;
	}
	public int getYpos() {
		return ypos;
	}
	public void setYpos(int ypos) {
		this.ypos = ypos;
	}
}
