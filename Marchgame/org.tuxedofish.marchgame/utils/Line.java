package utils;

import java.nio.FloatBuffer;

import object.ColorHandler;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector4f;

public class Line {
	public FloatBuffer data;
	
	public Line(Vector4f pointa, Vector4f pointb) {
		data = BufferUtils.createFloatBuffer(20);
		
		ColorHandler util = new ColorHandler();
		
		float[] dataf = {
				pointa.x, pointa.y, pointa.z, pointa.w,
				util.getColCood().x, util.getColCood().y,
				pointa.x, pointa.y, pointa.z, pointa.w,
				
				pointb.x, pointb.y, pointb.z, pointb.w,
				util.getColCood().x, util.getColCood().y,
				pointb.x, pointb.y, pointb.z, pointb.w,
		};
		
		data.put(dataf);
		data.flip();
	}
	public FloatBuffer getData() {
		return this.data;
	}
}
