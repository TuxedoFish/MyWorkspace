package object;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import texture.Material;
import utils.Face;

public class ObjectLoader {
	public Object loadModel(String f) throws FileNotFoundException, IOException {
		BufferedReader reader = new BufferedReader(new FileReader(
				new File(getClass().getResource(f).getFile())));
		Object m = new Object();
		String line;
		
		while((line = reader.readLine()) != null) {
			if(line.startsWith("v ")) {
				float x = Float.valueOf(line.split(" ")[1]);
				float y = Float.valueOf(line.split(" ")[2])*((float)Display.getWidth()/(float)Display.getHeight());
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
				
				Vector3f texcoordIndices = new Vector3f(Float.valueOf(line.split(" ")[1].split("/") [1]),
						Float.valueOf(line.split(" ")[2].split("/")[1]),
						Float.valueOf(line.split(" ")[3].split("/")[1]));
				
				Vector3f normalIndices = new Vector3f(Float.valueOf(line.split(" ")[1].split("/") [2]),
						Float.valueOf(line.split(" ")[2].split("/")[2]),
						Float.valueOf(line.split(" ")[3].split("/")[2]));
				
				m.faces.add(new Face(vectorIndices, normalIndices, texcoordIndices));
			} else if (line.startsWith("mtllib ")) {
				m.mtl = loadTexture(line.split(" ")[1]).get(0);
			} else if (line.startsWith("vt ")) {
				float x = Float.valueOf(line.split(" ")[1]);
				float y = Float.valueOf(line.split(" ")[2]);
				
				m.tcoords.add(new Vector2f(x, 1.0f-y));
			}
		}
		return m;
	}
	private ArrayList<Material> loadTexture(String texloc) throws IOException {
		String line = null;
		int currenttexture = -1;
		ArrayList<Material> mtls = new ArrayList<Material>();
		BufferedReader reader = new BufferedReader(new FileReader(
				new File(getClass().getResource(texloc).getFile())));
		
		String mtlname = null;
		float ns = 0.0f, ni = 0.0f, d = 0.0f;
		int illum = 0;
		Vector3f ka = null, kd = null, ks = null;
		String map_kd = null, map_ka = null, map_ks = null, map_ns = null, map_d = null, map_bump = null;
		BufferedImage texture = null;
		
		while((line = reader.readLine()) != null) {
			if(line.startsWith("newmtl ")) {
				if(currenttexture != -1) {
					mtls.add(new Material(mtlname, ns, ni, d, illum, ka, kd, ks, map_kd, map_ka, map_ks, map_ns, map_d, map_bump, texture));
				}
				currenttexture += 1;
			}
			if(line.startsWith("Ns ")) ns = Float.parseFloat(line.split(" ")[1]);
			if(line.startsWith("Ni ")) ns = Float.parseFloat(line.split(" ")[1]);
			if(line.startsWith("d ")) ns = Float.parseFloat(line.split(" ")[1]);
			if(line.startsWith("illum ")) illum = Integer.parseInt(line.split(" ")[1]);
			if(line.startsWith("map_d ")) map_d = line.split(" ")[1];
			if(line.startsWith("map_Ka ")) map_ka = line.split(" ")[1];
			if(line.startsWith("map_Ks ")) map_ks = line.split(" ")[1];
			if(line.startsWith("map_Kd ")) texture = ImageIO.read(getClass().getResourceAsStream(line.split(" ")[1]));
			if(line.startsWith("Ka ")) ka = new Vector3f(Float.parseFloat(line.split(" ")[1]), Float.parseFloat(line.split(" ")[2]),
					Float.parseFloat(line.split(" ")[3]));
			if(line.startsWith("Ks ")) ks = new Vector3f(Float.parseFloat(line.split(" ")[1]), Float.parseFloat(line.split(" ")[2]),
					Float.parseFloat(line.split(" ")[3]));
			if(line.startsWith("Kd ")) kd = new Vector3f(Float.parseFloat(line.split(" ")[1]), Float.parseFloat(line.split(" ")[2]),
					Float.parseFloat(line.split(" ")[3]));
		}
		if(currenttexture != -1) {
			mtls.add(new Material(mtlname, ns, ni, d, illum, ka, kd, ks, map_kd, map_ka, map_ks, map_ns, map_d, map_bump, texture));
		}
		return mtls;
	}
}
