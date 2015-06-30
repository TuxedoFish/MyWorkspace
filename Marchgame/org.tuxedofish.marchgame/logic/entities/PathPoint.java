package logic.entities;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

public class PathPoint {
	private Vector2f pos;
	private int index;
	private float speed;
	
	public Vector2f getPos() {
		return pos;
	}
	public int getIndex() {
		return index;
	}
	public float getSpeed() {
		return speed;
	}
	public PathPoint(Vector2f pos, Float speed, int index) {
		this.pos = new Vector2f(((pos.x/Display.getWidth())*2.0f)-1.0f, 
				(((-pos.y)/Display.getHeight())*2.0f)-1.0f);
		this.index = index;
		this.speed = speed;
	}
	public void setPos(Vector2f pos) {
		this.pos = pos;
	}
}
