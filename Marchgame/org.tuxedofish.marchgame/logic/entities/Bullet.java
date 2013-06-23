package logic.entities;

import java.awt.Polygon;
import java.awt.geom.Rectangle2D;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import start.DisplaySetup;

public class Bullet {
	private Vector2f pos;
	private float rot;
	private int texid = 0;
	private int age = 0;
	private boolean destroyingself = false;
	private int lastage = 0;
	private float radius;
	private String type;
	private Rectangle2D bounds;
	
	public Bullet(Vector2f pos, float rot, float radius, String type) {
		this.rot = rot;
		this.pos = pos;
		this.radius = radius/Display.getWidth();
		this.type = type;
		this.bounds = new Rectangle2D.Float();
		this.bounds.setRect((pos.x+1.0f)*(Display.getWidth()/2.0f), (pos.y+1.0f)*(Display.getHeight()/2.0f), radius*2.0f, radius*2.0f);
	}
	public String getType() {
		return type;
	}
	public void age() {
		age += 1;
	}
	public boolean getDestroying() {
		return destroyingself;
	}
	public void setDestroyingSelf(boolean destroyingself) {
		this.destroyingself = destroyingself;
		this.lastage = age;
	}
	public boolean contains(Vector2f pos, float width, float height, DisplaySetup d) {
		float yconv = Display.getWidth()/Display.getHeight();
		
		if(Math.sqrt(Math.pow(pos.x+(width/2) - (this.pos.x+(radius/2)), 2) + Math.pow(pos.y-(height/2) - (this.pos.y-(radius*yconv/2)), 2))<radius) {
			Vector2f p2 = new Vector2f(pos.x + width, pos.y);
			Vector2f p3 = new Vector2f(pos.x + width, pos.y - height);
			Vector2f p4 = new Vector2f(pos.x, pos.y - height);
			
			float d1 = (float)Math.sqrt(Math.pow(pos.x - (this.pos.x+(radius/2)), 2) + Math.pow(pos.y - (this.pos.y-(radius*yconv/2)), 2));
			float d2 = (float)Math.sqrt(Math.pow(p2.x - (this.pos.x+(radius/2)), 2) + Math.pow(p2.y - (this.pos.y-(radius*yconv/2)), 2));
			float d3 = (float)Math.sqrt(Math.pow(p3.x - (this.pos.x+(radius/2)), 2) + Math.pow(p3.y - (this.pos.y-(radius*yconv/2)), 2));
			float d4 = (float)Math.sqrt(Math.pow(p4.x - (this.pos.x+(radius/2)), 2) + Math.pow(p4.y - (this.pos.y-(radius*yconv/2)), 2));
			
			if(d1<radius || d2<radius || d3<radius || d4<radius) {
				return true;
			}
		}
		if(this.pos.x >= pos.x && this.pos.x <= pos.x + width 
				&& this.pos.y <= pos.y && this.pos.y >= pos.y - height) {
			return true;
		}
		return false;
	}
	public int getAge() {
		return age;
	}
	public int getLastAge() {
		return lastage;
	}
	public void setTexid(int texid) {
		this.texid = texid;
	}
	public void setPos(Vector2f pos) {
		this.pos = pos;
	}
	public int getTexid() {
		return texid;
	}
	public float getRot() {
		return rot;
	}
	public Vector2f getPos() {
		return pos;
	}
	public boolean contains(Vector2f pos, Polygon polygon, DisplaySetup d) {
		Polygon polygon2 = new Polygon();
		for(int i=0; i<polygon.npoints; i++) {
			polygon2.addPoint((int)((pos.x+1.0f)*(Display.getWidth()/2.0f)+polygon.xpoints[i]), 
					(int)((pos.y+1.0f)*(Display.getHeight()/2.0f)+polygon.ypoints[i]));
		}
		System.out.println(pos + " : " + this.bounds + " : " + polygon2.ypoints[0] + " : " + polygon.ypoints[0]);
		return polygon2.contains(this.bounds);
	}
}
