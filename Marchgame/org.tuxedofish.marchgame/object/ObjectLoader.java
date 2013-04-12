package object;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import utils.Face;
import utils.TextureUtils;

public class ObjectLoader {
	public Object loadShape(String dir) {
		try {
			Object obj = loadModel(getClass().getResourceAsStream(dir+"/data.obj"));
			BufferedReader reader = new BufferedReader(new FileReader(
					new File(getClass().getResource(dir).getFile()+"/matrix.txt")));
			float[] m = asFloatArray(reader.readLine().split(" "), 1); 
			float[] rm = asFloatArray(reader.readLine().split(" "), 1); 
			float[] q = asFloatArray(reader.readLine().split(" "), 1);
			float[] p = asFloatArray(reader.readLine().split(" "), 1);
			
			Matrix4f model = setMatrix(m[0], m[1], m[2], m[3], m[4], m[5], m[6], m[7], m[8], m[9], 
					m[10], m[11], m[12], m[13], m[14], m[15]);
			Matrix4f rot = setMatrix(rm[0], rm[1], rm[2], rm[3], rm[4], rm[5], rm[6], rm[7], rm[8], rm[9], 
					rm[10], rm[11], rm[12], rm[13], rm[14], rm[15]);
			Quaternion angle = new Quaternion(q[0], q[1], q[2], q[3]);
			Vector3f pos = new Vector3f(p[0], p[1], p[2]);
			obj.setLocRot(model, rot, angle, pos);
			return obj;
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("err loading shape");
			System.exit(1);
			return null;
		}
	}
	public Object loadShape(InputStream dir2, InputStream dir, InputStream matrix, int texid) {
		try {
			Object obj = loadModel(dir);
			BufferedReader reader = new BufferedReader(new InputStreamReader(matrix));
			float[] m = asFloatArray(reader.readLine().split(" "), 1); 
			float[] rm = asFloatArray(reader.readLine().split(" "), 1); 
			float[] q = asFloatArray(reader.readLine().split(" "), 1);
			float[] p = asFloatArray(reader.readLine().split(" "), 1);
			
			Matrix4f model = setMatrix(m[0], m[1], m[2], m[3], m[4], m[5], m[6], m[7], m[8], m[9], 
					m[10], m[11], m[12], m[13], m[14], m[15]);
			Matrix4f rot = setMatrix(rm[0], rm[1], rm[2], rm[3], rm[4], rm[5], rm[6], rm[7], rm[8], rm[9], 
					rm[10], rm[11], rm[12], rm[13], rm[14], rm[15]);
			Quaternion angle = new Quaternion(q[0], q[1], q[2], q[3]);
			Vector3f pos = new Vector3f(p[0], p[1], p[2]);
			obj.setLocRot(model, rot, angle, pos);
			if(texid == -1) {
				BufferedImage img = loadTexture(dir2);
				TextureUtils util = new TextureUtils();
				texid = util.binddata(img);
				obj.setTexid(texid, img);
			}
			return obj;
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("err loading shape");
			System.exit(1);
			return null;
		}
	}
	public float[] asFloatArray(String[] s, int offset) {
		float[] f = new float[s.length - offset];
		for(int i=offset; i<s.length; i++) {
			f[i-offset] = Float.parseFloat(s[i]);
		}
		return f;
	}
	public Matrix4f setMatrix(float m00, float m10, float m20, float m30, float m01, float m11, float m21, 
			float m31, float m02, float m12, float m22, float m32, float m03, float m13, float m23, float m33) {
		Matrix4f m = new Matrix4f();
		m.m00 = m00; m.m01 = m01; m.m02 = m02; m.m03 = m03;
		m.m10 = m10; m.m11 = m11; m.m12 = m12; m.m13 = m13;
		m.m20 = m20; m.m21 = m21; m.m22 = m22; m.m23 = m23;
		m.m30 = m30; m.m31 = m31; m.m32 = m32; m.m33 = m33;
		return m;
	}
	public BufferedImage loadTexture(InputStream f) {
		BufferedImage img;
		try {
			img = ImageIO.read(f);
			return img;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("err");
			return null;
		}
	}
	public Object loadModel(InputStream f) throws FileNotFoundException, IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(f));
		Object m = new Object();
		String line;
		
		while((line = reader.readLine()) != null) {
			if(line.startsWith("v ")) {
				float x = Float.valueOf(line.split(" ")[1]);
				float y = Float.valueOf(line.split(" ")[2]);
				float z = Float.valueOf(line.split(" ")[3]);
				
				m.vertices.add(new Vector3f(x, y, z));
			} else if (line.startsWith("vn ")) {
				float x = Float.valueOf(line.split(" ")[1]);
				float y = Float.valueOf(line.split(" ")[2]);
				float z = Float.valueOf(line.split(" ")[3]);
				
				m.normals.add(new Vector3f(x, y, z));
			} else if (line.startsWith("f ")) {
				Vector3f vectorIndices = new Vector3f(Float.valueOf(line.split(" ")[1].split("/") [0]),
						Float.valueOf(line.split(" ")[2].split("/")[0]),
						Float.valueOf(line.split(" ")[3].split("/")[0]));
				
				Vector3f normalIndices = new Vector3f(Float.valueOf(line.split(" ")[1].split("/") [2]),
						Float.valueOf(line.split(" ")[2].split("/")[2]),
						Float.valueOf(line.split(" ")[3].split("/")[2]));
				
				m.faces.add(new Face(vectorIndices, normalIndices));
			} else if (line.startsWith("vt ")) {
				float x = Float.valueOf(line.split(" ")[1]);
				float y = Float.valueOf(line.split(" ")[2]);
				
				m.tcoords.add(new Vector2f(x, y));
			}
		}
		return m;
	}
}
