package utils;

public class Pixel {
	private int x; private int y;
	private boolean allocation = false;
	
	public Pixel(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public boolean getAllocation() {
		return allocation;
	}
	public void usedup() {
		allocation = true;
	}
}
