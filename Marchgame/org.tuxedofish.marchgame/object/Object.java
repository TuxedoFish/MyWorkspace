package object;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import utils.Face;

public class Object {
	public List<Vector3f> vertices = new ArrayList<Vector3f>();
	public List<Vector3f> normals = new ArrayList<Vector3f>();
	public List<Vector2f> tcoords = new ArrayList<Vector2f>();
	public List<Face> faces = new ArrayList<Face>();
	private Matrix4f model;
	private Matrix4f rot;
	private Quaternion angle;
	private boolean located = false;
	private Vector3f pos;
	private int texid = -1;
	private BufferedImage img;
	
	public void setTexid(int texid, BufferedImage img) {
		this.texid = texid;
		this.img = img;
	}
	public Shape createUsable() {
		Shape s = new Shape("");
		VertexHandler vh = new VertexHandler();
		Vector4f[] v = {
				new Vector4f(), new Vector4f(), new Vector4f()
		};
		Vector4f[] n = {
				new Vector4f(), new Vector4f(), new Vector4f()
		};
		Vector2f[] tc = {
				new Vector2f(), new Vector2f(), new Vector2f()
		};
		for(int i = 0; i < faces.size();i++) {
			v[0] = vh.vectorconvert(vertices.get((int)faces.get(i).vertex.x - 1));
			v[1] = vh.vectorconvert(vertices.get((int)faces.get(i).vertex.y - 1));
			v[2] = vh.vectorconvert(vertices.get((int)faces.get(i).vertex.z - 1));
			
			n[0] = vh.vectorconvert(normals.get((int)faces.get(i).normal.x - 1));
			n[1] = vh.vectorconvert(normals.get((int)faces.get(i).normal.y - 1));
			n[2] = vh.vectorconvert(normals.get((int)faces.get(i).normal.z - 1));
			
			if(tcoords.size() != 0) {
				tc[0] = tcoords.get((int)faces.get(i).normal.x - 1);
				tc[1] = tcoords.get((int)faces.get(i).normal.y - 1);
				tc[2] = tcoords.get((int)faces.get(i).normal.z - 1);
				
				s.newTriangle(v, n, tc, 0, Color.RED, true);
			} else {
				s.newTriangle(v, n, s.getTexCoords(), 0, Color.RED, true);
			}
		}
		if(this.located) {
			s.setLocRot(model, rot, angle, pos);
		} else {s.finish();}
		if(texid != -1) {
			s.setTexId(texid, img);
		}
		return s;
	}
	public void setLocRot(Matrix4f model, Matrix4f rot, Quaternion angle, Vector3f pos) {
		this.model = model;
		this.rot = rot;
		this.angle = angle;
		this.pos = pos;
		this.located = true;
	}
}
