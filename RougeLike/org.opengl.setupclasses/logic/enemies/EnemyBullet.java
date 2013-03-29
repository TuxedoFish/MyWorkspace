package logic.enemies;

import org.lwjgl.util.vector.Vector2f;

public class EnemyBullet {
	private Vector2f pos;
	private float rot;
	private int texid = 0;
	private int age = 0;
	private boolean destroyingself = false;
	private int lastage = 0;
	
	public EnemyBullet(Vector2f pos, float rot) {
		this.rot = rot;
		this.pos = pos;
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
}
