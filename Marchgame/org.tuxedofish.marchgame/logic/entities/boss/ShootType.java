package logic.entities.boss;

public class ShootType {
	private int[] shootthreads; 
	private int[] shootlengths;
	private int[] pattern;
	
	public ShootType(int[] shootthreads, int[] shootlengths, int[] pattern) {
		this.shootlengths = shootlengths;
		this.shootthreads = shootthreads;
		this.pattern = pattern;
	}
	public int[] getShootthreads() {
		return shootthreads;
	}
	public void setShootthreads(int[] shootthreads) {
		this.shootthreads = shootthreads;
	}
	public int[] getShootlengths() {
		return shootlengths;
	}
	public void setShootlengths(int[] shootlengths) {
		this.shootlengths = shootlengths;
	}
	public int[] getPatterns() {
		return pattern;
	}
	public void setPattern(int[] pattern) {
		this.pattern = pattern;
	}
}
