package logic.entities;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

public class PathPoint {
	private Vector2f pos;
	private int index;
	
	public Vector2f getPos() {
		return pos;
	}
	public int getIndex() {
		return index;
	}
	public PathPoint(Vector2f pos, int index) {
		this.pos = new Vector2f(((pos.x/Display.getWidth())*2.0f)-1.0f, 
				(((-pos.y)/Display.getHeight())*2.0f)-1.0f);
		this.index = index;
	}
	public void setPos(Vector2f pos) {
		this.pos = pos;
	}
}
