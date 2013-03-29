package terrain;

import org.lwjgl.util.vector.Vector2f;

public class CaveLine {
	private Vector2f p1;
	private Vector2f p2;
	private Vector2f s;
	private int direction;
	private int intersect1=0;
	private int intersect2=0;
	private float offset;
	
	public CaveLine(Vector2f p1, Vector2f p2, int direction, float offset, Vector2f s) {
		this.offset = offset;
		this.direction = direction;
		this.p1 = p1;
		this.p2 = p2;
		this.s = s;
	}
	public Vector2f getStart() {
		return s;
	}
	public float getOffset() {
		return offset;
	}
	public Vector2f getP1() {
		return p1;
	}
	public void setP1(Vector2f p1) {
		this.p1 = p1;
	}
	public Vector2f getP2() {
		return p2;
	}
	public void setP2(Vector2f p2) {
		this.p2 = p2;
	}
	public int getDirection() {
		return direction;
	}
	public boolean collide(CaveLine cl2) {
		float dist = (float) Math.sqrt(Math.pow(this.getP1().x - this.getP2().x, 2) +
				Math.pow(this.getP1().y - this.getP2().y, 2));
		float dist2 = (float) Math.sqrt(Math.pow(cl2.getP1().x - cl2.getP2().x, 2) +
				Math.pow(cl2.getP1().y - cl2.getP2().y, 2));
		for(float i=0; i<dist; i+=0.05f) {
			for(float j=0; j<dist2; j+=0.05f) {
				Vector2f thisp = new Vector2f(this.getP1().x + ((this.getP1().x - this.getP2().x)*dist)
						, this.getP1().y + ((this.getP1().y - this.getP2().y)*dist));
				Vector2f cl2p = new Vector2f(cl2.getP1().x + ((cl2.getP1().x - cl2.getP2().x)*dist2)
						, cl2.getP1().y + ((cl2.getP1().y - cl2.getP2().y)*dist2));
				float got = 0.01f;
				if(thisp.x < cl2p.x + got && thisp.x > cl2p.x - got) {
					if(thisp.y < cl2p.y + got && thisp.y > cl2p.y - got) {
						return true;
					}
				}
			}
		}
		return false;
	}
	public void setDirection(int direction) {
		this.direction = direction;
	}
	public int getIntersect1() {
		return intersect1;
	}
	public void setIntersect1(int intersect1) {
		this.intersect1 = intersect1;
	}
	public int getIntersect2() {
		return intersect2;
	}
	public void setIntersect2(int intersect2) {
		this.intersect2 = intersect2;
	}
}
