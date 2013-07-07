package logic.entities.boss;

public class BossPart {
	private int pattern;
	private boolean hittable;
	private int[] shootthreadids;
	private int[] shootlengths;
	private int currentshootstage = 0;
	private int currentshootpattern = 0;
	
	public BossPart(int pattern, boolean hittable, int[] shootthreads, int[] shootlengths) {
		this.pattern = pattern;
		this.hittable = hittable;
		this.shootthreadids = shootthreads;
		this.shootlengths = shootlengths;
	}
	public int getPattern() {
		return pattern;
	}
	public void changeShootStage() {
		currentshootstage += 1;
	}
	public void resetShootStage() {
		currentshootstage = 0;
	}
	public void setPattern(int pattern) {
		this.pattern = pattern;
	}
	public int getShootStage() {
		return currentshootstage;
	}
	public void setShootPattern(int pattern) { 
		currentshootpattern = pattern;
	}
	public int getShootPattern() {
		return currentshootpattern;
	}
	public boolean isHittable() {
		return hittable;
	}
	public int[] getShootThreads() {
		return shootthreadids;
	}
	public int[] getShootLengths() {
		return shootlengths;
	}
	public void setHittable(boolean hittable) {
		this.hittable = hittable;
	}
}
