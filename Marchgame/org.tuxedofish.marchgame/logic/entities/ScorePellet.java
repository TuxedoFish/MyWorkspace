package logic.entities;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import start.DisplaySetup;

public class ScorePellet {
	private int score;
	private Vector2f pos;
	private int texid;
	private int animationwait = 0;
	
	public ScorePellet(int score, Vector2f pos, int texid) {
		this.score = score;
		this.pos = pos;
		this.texid = texid;
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
	public int getTexid() {
		return texid;
	}
	public void changeTexid(int change) {
		texid += change;
	}
	public void animationWait() {
		animationwait +=1;
	}
	public void resetWait() {
		animationwait = 0;
	}
	public int getAnimationWait() {
		return animationwait;
	}
	public void setTexid(int texid) {
		this.texid = texid;
	}
	public boolean contains(Vector2f pos, float width, float height, DisplaySetup d) {
		float radius = 50/Display.getWidth();
		Vector2f p2 = new Vector2f(pos.x + width, pos.y);
		Vector2f p3 = new Vector2f(pos.x + width, pos.y - height);
		Vector2f p4 = new Vector2f(pos.x, pos.y - height);
		float d1 = (float)Math.sqrt(Math.pow(pos.x - this.pos.x, 2) + Math.pow(pos.y - this.pos.y, 2));
		float d2 = (float)Math.sqrt(Math.pow(p2.x - this.pos.x, 2) + Math.pow(p2.y - this.pos.y, 2));
		float d3 = (float)Math.sqrt(Math.pow(p3.x - this.pos.x, 2) + Math.pow(p3.y - this.pos.y, 2));
		float d4 = (float)Math.sqrt(Math.pow(p4.x - this.pos.x, 2) + Math.pow(p4.y - this.pos.y, 2));
		
		Vector4f tempos = new Vector4f(this.pos.x + (radius/Display.getWidth()), 
				this.pos.y - (radius/Display.getHeight()), 0.0f, 1.0f);
		tempos = Matrix4f.transform(d.getModelViewMatrixAsMatrix(), tempos, tempos);
		
		if(d1<radius || d2<radius || d3<radius || d4<radius) {
			return true;
		}
		if(this.pos.x >= pos.x && this.pos.x <= pos.x + width 
				&& this.pos.y <= pos.y && this.pos.y >= pos.y - height) {
			return true;
		}
		return false;
	}
}
