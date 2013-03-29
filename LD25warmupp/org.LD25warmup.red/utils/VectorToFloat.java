package utils;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

public class VectorToFloat {
	private FloatBuffer floats = BufferUtils.createFloatBuffer(0);
	
	public void addvector(Vector4f v) {
		FloatBuffer f = floats;
		
		int pos = floats.position();
		
		floats = FloatBuffer.allocate(floats.capacity() + 4);
		
		for(int i = 0; i < f.capacity(); i ++) {
			floats.put(f.get(i));
		}
		
		floats.position(pos);
		
		floats.put(v.x);
		floats.put(v.y);
		floats.put(v.z);
		floats.put(v.w);
	}
	public void addvector(Vector2f v) {
		FloatBuffer f = floats;
		
		int pos = floats.position();
		
		floats = FloatBuffer.allocate(floats.capacity() + 2);
		
		for(int i = 0; i < f.capacity(); i ++) {
			floats.put(f.get(i));
		}
		
		floats.position(pos);
		
		floats.put(v.x);
		floats.put(v.y);
	}
	public FloatBuffer getfloats() {
		return floats;
	}
}
