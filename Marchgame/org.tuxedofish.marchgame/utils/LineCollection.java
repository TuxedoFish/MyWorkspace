package utils;

import java.nio.FloatBuffer;


public class LineCollection {
	FloatBuffer data = FloatBuffer.allocate(0);
	
	public LineCollection() {
		
	}
	public void addLine(Line l) {
		FloatBuffer f = data;
		
		int pos = data.position();
			
		data = FloatBuffer.allocate(data.capacity() + 20);
		
		for(int i = 0; i < f.capacity(); i ++) {
			data.put(f.get(i));
		}
		
		data.position(pos);
			
		data.put(l.getData());
	}
	public FloatBuffer getData() {
		FloatBuffer f = data;
		f.position(0);
		return f;
	}
}
