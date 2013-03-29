package utils;

import java.util.ArrayList;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

public class MathUtils {
	public Vector4f scaleVector(Vector4f vector, int scalar) {
		return new Vector4f(vector.x * scalar,
		vector.y * scalar,
		vector.z * scalar, 1.0f);
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
}
