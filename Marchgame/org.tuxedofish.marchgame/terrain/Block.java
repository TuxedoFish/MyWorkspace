package terrain;

import java.awt.image.BufferedImage;

public class Block {
	private int width;
	private int height;
	
	int texid = 0;
	float x;
	float y; 
	
	public Block(float x, float y, int texid, int width, int height) {
		 this.width = width;
		 this.height = height;
		 this.x = x;
		 this.y = y;
	     this.texid = texid;
	}
	public void setTexID(int id) { 
		this.texid = id;
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
