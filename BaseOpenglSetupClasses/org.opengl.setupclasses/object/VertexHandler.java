package object;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import utils.VectorToFloat;

public class VertexHandler {
	private FloatBuffer vertexs;
	private ArrayList<Polygon> polygons = new ArrayList<>();
	private int vaoId;
	private float autoZ = -1;
    //Generate
    private int VboId = glGenBuffers();
	
	public VertexHandler() {
		vaoId = glGenVertexArrays();
	}
	/**
	 * @param pos
	 * @param col
	 * @param norm
	 */
	public Vertex newvertex(Vector4f pos, Vector2f texcoods, Vector4f norm) {
		float[] tempdata = {
				pos.x, pos.y, pos.z, pos.w,
				texcoods.x, texcoods.y,
				norm.x, norm.y, norm.z, norm.w
		};
		
		return new Vertex(tempdata);
	}
	public void addpolygon(Vertex v1, Vertex v2, Vertex v3) {
		VectorToFloat vtf = new VectorToFloat();
		
		vtf.addvector(v1.getPos());
		vtf.addvector(v1.getCol());
		vtf.addvector(v1.getNorm());
		
		vtf.addvector(v2.getPos());
		vtf.addvector(v2.getCol());
		vtf.addvector(v2.getNorm());
		
		vtf.addvector(v3.getPos());
		vtf.addvector(v3.getCol());
		vtf.addvector(v3.getNorm());
		
		Polygon p = new Polygon(vtf.getfloats());
		polygons.add(p);
	}
	public FloatBuffer getPolygonAsFloatBuffer(int index) {
		FloatBuffer f = BufferUtils.createFloatBuffer(30);
		f = polygons.get(index).getdata();
		f.position(0);
		f.rewind();
		
		return f;
	}
	public FloatBuffer binddata() {
		FloatBuffer f = BufferUtils.createFloatBuffer(0);
		FloatBuffer c = BufferUtils.createFloatBuffer(0);
		
		for(int i = 0;i < polygons.size();i++) {
			
			c = BufferUtils.createFloatBuffer(f.capacity());
			c.position(0);
			int pos = f.position();
			f.position(0);
			c.put(f);
			if(c.hasRemaining()) {
				System.out.println(c.hasRemaining());
				System.out.println(c.get(0));
			}
			
			f = BufferUtils.createFloatBuffer(f.capacity() + 30);
			f.position(0);
			f.put(c);
			
			f.position(pos);
			
			for(int j = 0;j < polygons.get(i).getdata().capacity();j++) {
				f.put(polygons.get(i).getdata().get(j));
			}
		}
		f.rewind();
		return f;
	}
	public int getcount() {
		int val = 0;
		
		for(int i = 0;i < polygons.size();i++) {
			for(int j = 0;j < polygons.get(i).getdata().capacity();j++) {
				val += 1;
			}
		}
		
		return val;
	}
	public Vector4f vectorconvert(Vector2f v) {
		return new Vector4f(v.x, v.y, autoZ, 1.0f);
	}
	public Vector4f vectorconvert(Vector3f v) {
		return new Vector4f(v.x, v.y, v.z, 1.0f);
	}
	public void addpolygon(FloatBuffer data) {
		Polygon p = new Polygon(data);
		polygons.add(p);
	}
	public FloatBuffer getvertexs() {
		return vertexs;
	}
	public Polygon getPolygon(int index) {
		return polygons.get(index);
	}
	public ArrayList<Polygon> getpolygons() {
		return polygons;
	}
	public int getVao() {
		return vaoId;
	}
	public int getVboId() {
		return VboId;
	}
	public void setAutoZ(float autoZ) {
		this.autoZ = autoZ;
	}
	public FloatBuffer getPolygonsAsFloatBuffer() {
		FloatBuffer f = BufferUtils.createFloatBuffer(polygons.size() * 30);
		
		for(int i = 0; i < polygons.size();i++) {
			FloatBuffer fb = polygons.get(i).getdata();
			
			for(int j = 0; j < fb.capacity(); j++) {
				f.put((i*30) + j, fb.get(j));
			}
		}
		f.position(0);
		f.rewind();
		
		return f;
	}
}
