package object;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

public class Vertex {
	private Vector4f pos;
	private Vector2f texcoods;
	private Vector4f norm;
	
	public Vertex(float x, float y, float z, float w,
			      float tx, float ty,
			      float nx, float ny, float nz, float nw) {
		
		pos = new Vector4f(x,y,z,w);
		texcoods = new Vector2f(tx, ty);
		norm = new Vector4f(nx,ny,nz,nw);
	}
	public Vertex(Vector4f pos, Vector2f texcoods, Vector4f norm) {
		this.pos = pos;
		this.texcoods = texcoods;
		this.norm = norm;
	}
	public Vertex(float[] f) {
		this.pos = new Vector4f(f[0], f[1], f[2], f[3]);
		this.texcoods = new Vector2f(f[4], f[5]);
		this.norm = new Vector4f(f[6], f[7], f[8], f[9]);
	}
	public Vector4f getPos() {
		return pos;
	}
	public Vector2f getCol() {
		return texcoods;
	}
	public Vector4f getNorm() {
		return norm;
	}
	public void setPos(int x, int y, int z, int w) {
		pos = new Vector4f(x,y,z,w);
	}
	public void setCol(int tx, int ty) {
		texcoods = new Vector2f(tx, ty);
	}
	public void setNorm(int nx, int ny, int nz, int nw) {
		norm = new Vector4f(nx,ny,nz,nw);
	}
	public void setPos(Vector4f pos) {
		this.pos = pos;
	}
	public void setCol(Vector2f texcoods) {
		this.texcoods = texcoods;
	}
	public void setNorm(Vector4f norm) {
		this.norm = norm;
	}
}
