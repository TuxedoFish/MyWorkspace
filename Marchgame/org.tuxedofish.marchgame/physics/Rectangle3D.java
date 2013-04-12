package physics;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import object.Shape;
import utils.MathUtils;

public class Rectangle3D {
	private Vector3f topleftfrontpoint = new Vector3f();
	private float depth, width, height;
	private Matrix4f model;
	
	public Rectangle3D(Shape s) {
		float lowestx = s.getdata().get(0), lowesty = s.getdata().get(1), lowestz = s.getdata().get(2);
		float highestx = s.getdata().get(0), highesty = s.getdata().get(1), highestz = s.getdata().get(2);
		
		for(int i=1; i<s.getPoints();i++) {
			if(s.getdata().get(i*10)>highestx) highestx=s.getdata().get(i*10);
			if(s.getdata().get(i*10)<lowestx) lowestx=s.getdata().get(i*10);
			
			if(s.getdata().get((i*10)+1)>highesty) highesty=s.getdata().get((i*10) + 1);
			if(s.getdata().get((i*10)+1)<lowesty) lowesty=s.getdata().get((i*10) + 1);
			
			if(s.getdata().get((i*10)+2)>highestz) highestz=s.getdata().get((i*10) + 2);
			if(s.getdata().get((i*10)+2)<lowestz) lowestz=s.getdata().get((i*10) + 2);
		}
		width = Math.abs(highestx-lowestx); height = Math.abs(highesty-lowesty); 
		depth = Math.abs(highestz-lowestz);
		topleftfrontpoint = new Vector3f(lowestx, highesty, lowestz);
	}
	public Rectangle3D(Vector3f topleftfrontpoint, Vector3f dimensions) {
		this.topleftfrontpoint = topleftfrontpoint;
		this.width = dimensions.x; this.height = dimensions.y; this.depth = dimensions.z; 
	}
	public Vector3f getPos() {
		return topleftfrontpoint;
	}
	public Vector3f getDimensions() {
		return new Vector3f(width, height, depth);
	}
	public Vector4f transform(Vector4f v, Matrix4f m) {
		return Matrix4f.transform(m, new Vector4f(v.x, v.y, v.z, 1.0f), v);
	}
	public boolean contains(Rectangle3D r) {
		Vector3f d1 = r.getDimensions();
		Vector3f d2 = this.getDimensions();
		Vector4f v1 = new Vector4f(r.getPos().x, r.getPos().y, r.getPos().z, 1.0f);
		Vector4f v2 = new Vector4f(this.getPos().x, this.getPos().y, this.getPos().z, 1.0f);
		
		if((v1.x >= v2.x && v1.x <= v2.x + d2.x) || (v1.x + d1.x >= v2.x && v1.x + d1.x <= v2.x + d2.x)) {
			if((v1.y >= v2.y && v1.y <= v2.y + d2.y) || (v1.y + d1.y >= v2.y && v1.y + d1.y <= v2.y + d2.y)) {
				if((v1.z >= v2.z && v1.z <= v2.z + d2.z) || (v1.z + d1.z >= v2.z && v1.z + d1.z <= v2.z + d2.z)) {
					return true;
				}
			}
		}
		return false;
	}
	public boolean contains(Vector3f point) {
		Vector3f d2 = this.getDimensions();
		Vector4f v2 = new Vector4f(this.getPos().x, this.getPos().y, this.getPos().z, 1.0f);
		
		if(point.x >= v2.x && point.x <= v2.x + d2.x) {
			if(point.y <= v2.y && point.y >= v2.y - d2.y) {
				if(point.z >= v2.z && point.z <= v2.z + d2.z) {
					return true;
				}
			}
		}
		return false;
	}
	public boolean contains(Shape s) {
		Rectangle3D r = new Rectangle3D(s);
		return contains(r);
	}
}
