package object;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class Polygon {
	public FloatBuffer data;
	
	public Polygon(FloatBuffer data) {
		
		this.data =  BufferUtils.createFloatBuffer(data.capacity());
		
		for(int i = 0; i < data.capacity(); i ++) {
			this.data.put(data.get(i));
		}
	}
	public void changePos(float xdif, float ydif, float zdif) {
		this.data.put(0, this.data.get(0) + xdif);
		this.data.put(10, this.data.get(10) + xdif);
		this.data.put(20, this.data.get(20) + xdif);
		
		this.data.put(1, this.data.get(1) + ydif);
		this.data.put(11, this.data.get(11) + ydif);
		this.data.put(21, this.data.get(21) + ydif);
		
		this.data.put(2, this.data.get(2) + zdif);
		this.data.put(12, this.data.get(12) + zdif);
		this.data.put(22, this.data.get(22) + zdif);
	}
	/**
	 * pos = 1 || 2 || 3
	 * @param index
	 * @param xdif
	 * @param ydif
	 * @param zdif
	 */
	public void changePos(int index, float xdif, float ydif, float zdif) {
		if(index < 1 || index > 3) {
			System.err.println("Error @ POLYGON.CHANGEPOS, INVALID PARAMETER INDEX : " + index);
			return;
		}
		this.data.put((index*10) - 10, this.data.get((index*10) - 10) + xdif);
		this.data.put((index*10) - 9, this.data.get((index*10) - 9) + ydif);
		this.data.put((index*10) - 8, this.data.get((index*10) - 8) + zdif);
	}
	public void changePos(int index, Vector4f loc) {
		if(index < 1 || index > 3) {
			System.err.println("Error @ POLYGON.CHANGEPOS, INVALID PARAMETER INDEX : " + index);
			return;
		}
		this.data.put((index*10) - 10, loc.x);
		this.data.put((index*10) - 9, loc.y);
		this.data.put((index*10) - 8, loc.z);
		this.data.put((index*10) - 7, loc.w);
	}
	public void setTexCood(int index, Vector2f vec) {
		if(index < 1 || index > 3) {
			System.err.println("Error @ POLYGON.CHANGEPOS, INVALID PARAMETER INDEX : " + index);
			return;
		}
		
		this.data.put((index*10) - 6, vec.x);
		this.data.put((index*10) - 5, vec.y);
	}
	public Vector4f getPos(int index) throws Exception {
		if(index < 1 || index > 3) {
			throw new Exception("Error @ POLYGON.GETPOS, INVALID PARAMETER INDEX : " + index);
		}
		
		return new Vector4f(data.get((index*10) - 10), data.get((index*10) - 9), data.get((index*10) - 8), 
				data.get((index*10) - 7));
	}
	public Vector2f getTexCood(int index) throws Exception {
		if(index < 1 || index > 3) {
			throw new Exception("Error @ POLYGON.GETPOS, INVALID PARAMETER INDEX : " + index);
		}
		
		return new Vector2f(data.get((index*10) - 6), data.get((index*10) - 5));
	}
	public Vector4f getNorm(int index) throws Exception {
		if(index < 1 || index > 3) {
			throw new Exception("Error @ POLYGON.GETPOS, INVALID PARAMETER INDEX : " + index);
		}
		
		return new Vector4f(data.get((index*10) - 4), data.get((index*10) - 3), data.get((index*10) - 2), 
				data.get((index*10) - 1));
	}
	public FloatBuffer getdata() {
		return data;
	}
}
