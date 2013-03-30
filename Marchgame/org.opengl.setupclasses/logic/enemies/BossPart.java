package logic.enemies;

public class BossPart {
	private int pattern;
	private boolean hittable;

	public BossPart(int pattern, boolean hittable) {
		this.pattern = pattern;
		this.hittable = hittable;
	}
	public int getPattern() {
		return pattern;
	}
	public void setPattern(int pattern) {
		this.pattern = pattern;
	}
	public boolean isHittable() {
		return hittable;
	}
	public void setHittable(boolean hittable) {
		this.hittable = hittable;
	}
}
