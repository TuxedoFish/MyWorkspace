package object;

import java.awt.Color;
import java.io.File;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import utils.Face;

public class Object {
	public List<Vector3f> vertices = new ArrayList<Vector3f>();
	public List<Vector3f> normals = new ArrayList<Vector3f>();
	public List<Face> faces = new ArrayList<Face>();
	
	public Shape createUsable() {
		Shape s = new Shape();
		VertexHandler vh = new VertexHandler();
		Vector4f[] v = {
				new Vector4f(), new Vector4f(), new Vector4f()
		};
		Vector4f[] n = {
				new Vector4f(), new Vector4f(), new Vector4f()
		};
		
		for(int i = 0; i < faces.size();i++) {
			v[0] = vh.vectorconvert(vertices.get((int) faces.get(i).vertex.x - 1));
			v[1] = vh.vectorconvert(vertices.get((int) faces.get(i).vertex.y - 1));
			v[2] = vh.vectorconvert(vertices.get((int) faces.get(i).vertex.z - 1));
			
			n[0] = vh.vectorconvert(normals.get((int) faces.get(i).normal.x - 1));
			n[1] = vh.vectorconvert(normals.get((int) faces.get(i).normal.y - 1));
			n[2] = vh.vectorconvert(normals.get((int) faces.get(i).normal.z - 1));
			
			s.newTriangle(v, n, s.getTexCoords(), 0, Color.RED, true);
		}
		
		s.finish();
		return s;
	}
}
