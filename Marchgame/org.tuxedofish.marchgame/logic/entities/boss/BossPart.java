package logic.entities.boss;

import java.util.ArrayList;

public class BossPart {
	private boolean hittable;
	private int currentshootstage = 0;
	private int currentshootpattern = 0;
	private ArrayList<ShootType> shoottypes;
	private int currenttype = 0;
	
	public BossPart(boolean hittable, ArrayList<ShootType> shoottypes) {
		this.shoottypes = shoottypes;
		this.hittable = hittable;
	}
	public void changeShootStage() {
		currentshootstage += 1;
	}
	public void resetShootStage() {
		currentshootstage = 0;
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
	public ArrayList<ShootType> getShootTypes() { 
		return shoottypes;
	}
	public void setHittable(boolean hittable) {
		this.hittable = hittable;
	}
	public int[] getShootThreads() {
		return shoottypes.get(currenttype).getShootthreads();
	}
	public int[] getShootLengths() {
		return shoottypes.get(currenttype).getShootlengths();
	}
	public int[] getPatterns() {
		return shoottypes.get(currenttype).getPatterns();
	}
	public void setShootType(int type) {
		currenttype = type;
	}
}
