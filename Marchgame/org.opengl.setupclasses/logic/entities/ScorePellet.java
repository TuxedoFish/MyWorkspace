package logic.entities;

import org.lwjgl.util.vector.Vector2f;

public class ScorePellet {
	private int score;
	private Vector2f pos;
	
	public ScorePellet(int score, Vector2f pos) {
		this.score = score;
		this.pos = pos;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public Vector2f getPos() {
		return pos;
	}
	public void setPos(Vector2f pos) {
		this.pos = pos;
	}
}
