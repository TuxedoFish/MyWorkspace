package object;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import texture.Material;
import texture.TextureHandler;
import utils.Face;
import utils.TextureUtils;

public class Object {
	public List<Vector3f> vertices = new ArrayList<Vector3f>();
	public List<Vector3f> normals = new ArrayList<Vector3f>();
	public List<Vector2f> tcoords = new ArrayList<Vector2f>();
	public List<Face> faces = new ArrayList<Face>();
	public Material mtl;
	
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
				tc[0] = tcoords.get((int)faces.get(i).texcoord.x - 1);
				tc[1] = tcoords.get((int)faces.get(i).texcoord.y - 1);
				tc[2] = tcoords.get((int)faces.get(i).texcoord.z - 1);
				
				s.newTriangle(v, n, tc, 0, Color.RED, false);
			} else {
				s.newTriangle(v, n, s.getTexCoords(), 0, Color.RED, true);
			}
		}
		if(mtl != null) {
			TextureUtils util = new TextureUtils();
			s.setTexId(util.binddata(mtl.getTexture()), mtl.getTexture());
		}
		s.finish();
		return s;
	}
}
