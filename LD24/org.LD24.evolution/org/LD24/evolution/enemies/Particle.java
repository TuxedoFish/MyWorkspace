package org.LD24.evolution.enemies;

public class Particle{
	private int xpos;
	private int ypos;
	private int direction;
	private int velocity;
	
	public Particle(int xpos, int ypos) {
		this.xpos = xpos;
		this.ypos = ypos;
		this.direction = (int)(Math.random()*360);
		this.velocity = (int)(Math.random()*20);
	}
	public void update() {
		this.xpos -= Math.sin(Math.toRadians(this.direction)) * this.velocity;
		this.ypos -= Math.cos(Math.toRadians(this.direction)) * this.velocity;
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
