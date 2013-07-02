package utils;

import java.util.ArrayList;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class MathUtils {
	public Vector4f scaleVector(Vector4f vector, float scalar) {
		return new Vector4f(vector.x * scalar,
		vector.y * scalar,
		vector.z * scalar, 1.0f);
	}
	public Vector3f scaleVector(Vector3f vector, float scalar) {
		return new Vector3f(vector.x * scalar,
		vector.y * scalar,
		vector.z * scalar);
	}
	public Vector3f addVectors(Vector3f[] vs) {
		Vector3f dest = new Vector3f(vs[0].x, vs[0].y, vs[0].z);
		for(int i=1; i<vs.length;i++) {
			dest = new Vector3f(dest.x + vs[i].x, dest.y + vs[i].y, dest.y + vs[i].y);
		}
		return dest;
	}
	public Vector3f negateVectors(Vector3f[] vs) {
		Vector3f dest = new Vector3f(vs[0].x, vs[0].y, vs[0].z);
		for(int i=1; i<vs.length;i++) {
			dest = new Vector3f(dest.x - vs[i].x, dest.y - vs[i].y, dest.y - vs[i].y);
		}
		return dest;
	}
	public Vector4f negateVectors(Vector4f[] vs) {
		Vector4f dest = new Vector4f(vs[0].x, vs[0].y, vs[0].z, 1.0f);
		for(int i=1; i<vs.length;i++) {
			dest = new Vector4f(dest.x - vs[i].x, dest.y - vs[i].y, dest.z - vs[i].z, 1.0f);
		}
		return dest;
	}
	public Vector4f floatToInt(Vector4f pos) {
		pos.x = (int)((pos.x + 1.0) * (Display.getWidth()/2));
		pos.y = (int)((pos.y + 1.0) * (Display.getHeight()/2));
		
		return pos;
	}
	public float IntToFloat(float pos, int type) {
		if(type == 1) {
			pos = (pos/(Display.getHeight()/2)) - 1.0f;
		} else {
			pos = (pos/(Display.getWidth()/2)) - 1.0f;
		}
		
		return pos;
	}
	public float clamp(float number, float max) {
		if(number < 0.0f) {
			number = 0.0f;
		}
		if(number > max) {
			number = max;
		}
		return number;
	}
	public void rankony(ArrayList<Vector2f> allvecs,
			ArrayList<Vector2f> rankedy) {
		
		rankedy.add(allvecs.get(0));
		boolean done = false;
		
		for(int i = 1;i < allvecs.size();i++) {
			done = false;
			
			for(int j = 0; j < rankedy.size() && !done;j++) {
				
				if(allvecs.get(i).y > rankedy.get(j).y) {
					rankedy.add(j, allvecs.get(i));
					done = true;
				}
				if(!done) {
					rankedy.add(allvecs.get(i));
					done = true;
				}
			}
		}
	}
	public void calculatecirclepoints(Vector2f center, float diameter,
			ArrayList<Vector2f> points) {
		int y;
		int ydif;
		
		for(int x = (int) (center.x - (diameter/2)); x < center.x + diameter; ++x) {
			ydif = (int) Math.sqrt(Math.pow(diameter, 2) - Math.pow(Math.abs(center.x - x), 2));
			y = (int) Math.abs(center.y - ydif);
			
			if(!pointexist(new Vector2f(x, y), points)) {
				points.add(new Vector2f(x,y));
			}
			if(!pointexist(new Vector2f(x, -y), points)) {
				points.add(new Vector2f(x,y));
			}
		}
	}
	public boolean pointexist(Vector2f tofind, ArrayList<Vector2f> points) {
		for(int i = 0; i < points.size();i++) {
			if(points.get(i).x == tofind.x && points.get(i).y == tofind.y) {
				return true;
			}
		}
		return false;
	}
	public float distancebetween(Vector4f pointa, Vector4f pointb) {
		float distance = (float) Math.sqrt(Math.pow(Math.abs(pointa.x - pointb.x), 2) + Math.pow(Math.abs(pointa.y - pointb.y), 2) + Math.pow(Math.abs(pointa.z - pointb.z), 2)); 
		return distance;
	}
	public Vector3f multiplyVector(Vector3f a, Vector3f b) {
		return new Vector3f(a.x*b.x, a.y*b.y, a.z*b.z);
	}
	public boolean isBigger(Vector3f a, Vector3f b) {
		double alength = Math.cbrt(Math.pow(a.x, 2) + Math.pow(a.y, 2) + Math.pow(a.z, 2));
		double blength = Math.cbrt(Math.pow(b.x, 2) + Math.pow(b.y, 2) + Math.pow(b.z, 2));
		if(alength>blength) {
			return true;
		} else {
			return false;
		}
	}
	public Vector4f addVectors(Vector4f[] vs) {
		Vector4f dest = new Vector4f(vs[0].x, vs[0].y, vs[0].z, 1.0f);
		for(int i=1; i<vs.length;i++) {
			dest = new Vector4f(dest.x + vs[i].x, dest.y + vs[i].y, dest.y + vs[i].y, 1.0f);
		}
		return dest;
	}
	public boolean closeEnough(float a, float b, float giveortake) {
		if(a < b+giveortake && a > b-giveortake) {
			return true;
		} else {
			return false;
		}
	}
}
