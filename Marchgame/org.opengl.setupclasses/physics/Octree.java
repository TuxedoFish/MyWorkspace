package physics;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class Octree {
	private Octree[] subtrees;
	private Vector3f dimensions;
	private Vector4f pos;
	private boolean filled = false;
	
	public Octree(Vector4f center, Vector3f dimensions) {
		this.dimensions = dimensions;
		this.pos = center;
	}
	public boolean getfilled() {
		return filled;
	}
	public void createSubTrees() {
		Vector3f d = dimensions;
		subtrees = new Octree[]{
				new Octree(new Vector4f(pos.x + (d.x/4.0f), pos.y  + (d.y/4.0f), pos.z  + (d.z/4.0f), 1.0f), 
						new Vector3f(d.x/2.0f, d.y/2.0f, d.z/2.0f)),
				new Octree(new Vector4f(pos.x - (d.x/4.0f), pos.y  + (d.y/4.0f), pos.z  + (d.z/4.0f), 1.0f), 
						new Vector3f(d.x/2.0f, d.y/2.0f, d.z/2.0f)), 
				new Octree(new Vector4f(pos.x - (d.x/4.0f), pos.y  - (d.y/4.0f), pos.z  + (d.z/4.0f), 1.0f), 
						new Vector3f(d.x/2.0f, d.y/2.0f, d.z/2.0f)), 
				new Octree(new Vector4f(pos.x + (d.x/4.0f), pos.y  - (d.y/4.0f), pos.z  + (d.z/4.0f), 1.0f), 
						new Vector3f(d.x/2.0f, d.y/2.0f, d.z/2.0f)), 
				new Octree(new Vector4f(pos.x + (d.x/4.0f), pos.y  + (d.y/4.0f), pos.z  - (d.z/4.0f), 1.0f), 
						new Vector3f(d.x/2.0f, d.y/2.0f, d.z/2.0f)), 
				new Octree(new Vector4f(pos.x - (d.x/4.0f), pos.y  + (d.y/4.0f), pos.z  - (d.z/4.0f), 1.0f), 
						new Vector3f(d.x/2.0f, d.y/2.0f, d.z/2.0f)),
				new Octree(new Vector4f(pos.x - (d.x/4.0f), pos.y  - (d.y/4.0f), pos.z  - (d.z/4.0f), 1.0f), 
						new Vector3f(d.x/2.0f, d.y/2.0f, d.z/2.0f)), 
				new Octree(new Vector4f(pos.x + (d.x/4.0f), pos.y  - (d.y/4.0f), pos.z  - (d.z/4.0f), 1.0f), 
						new Vector3f(d.x/2.0f, d.y/2.0f, d.z/2.0f))
		};
	}
	public Octree[] getSubTrees() {
		return subtrees;
	}
	public Vector3f getDimensions() {
		return dimensions;
	}
	public Vector4f getPos() {
		return pos;
	}
	public void setFilled(boolean b) {
		filled = b;
	}
}
