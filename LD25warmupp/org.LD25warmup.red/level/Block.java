package level;

import java.awt.image.BufferedImage;

public class Block {
	private int width = 20;
	private int height = 20;
	
	int texid = 0;
	float x;
	float y; 
	
	public Block() {}
	public Block(float x, float y, int texid) {
		 this.x = x;
		 this.y = y;
	     this.texid = texid;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public float getX() {
		return x;
	}
	public float getY() {
		return y;
	}
	public int getTexid() {
		return texid;
	}
}
