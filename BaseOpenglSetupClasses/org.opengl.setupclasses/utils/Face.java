package utils;

import org.lwjgl.util.vector.Vector3f;

public class Face {
	public Vector3f vertex;// three indices to connect triangles
	public Vector3f normal;
	public Vector3f texcoord;
	
	public Face(Vector3f vertex, Vector3f normal, Vector3f texcoord) {
		this.vertex = vertex;
		this.normal = normal;
		this.texcoord = texcoord;
	}
}
