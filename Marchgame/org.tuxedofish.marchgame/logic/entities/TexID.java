package logic.entities;

public class TexID {
	private int lowesttexid;
	private int highesttexid;
	private int currenttexid;
	private boolean up = false;
	
	public TexID(int lowesttexid, int highesttexid, int currenttexid) {
		this.lowesttexid = lowesttexid;
		this.highesttexid = highesttexid;
		this.currenttexid = currenttexid;
	}
	public int getCurrenttexid() {
		return currenttexid;
	}
	public void setCurrenttexid(int currenttexid) {
		this.currenttexid = currenttexid;
	}
	public int getLowesttexid() {
		return lowesttexid;
	}
	public void setLowesttexid(int lowesttexid) {
		this.lowesttexid = lowesttexid;
	}
	public int getHighesttexid() {
		return highesttexid;
	}
	public void setHighesttexid(int highesttexid) {
		this.highesttexid = highesttexid;
	}
	public boolean isUp() {
		return up;
	}
	public void setUp(boolean up) {
		this.up = up;
	}
}
