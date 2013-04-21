package object;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL21.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL41.*;
import static org.lwjgl.opengl.GL42.*;
import static org.lwjgl.opengl.GL31.*;
import static org.lwjgl.opengl.GL32.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.util.glu.GLU.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import javax.imageio.ImageIO;


import normal.NormalHandler;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import physics.OctreeCollision;

import shader.ShaderHandler;
import start.Controller;
import start.DisplaySetup;
import texture.TextureHandler;
import utils.DataUtils;
import utils.Line;
import utils.LineCollection;
import utils.MathUtils;

public class Shape {
	private VertexHandler vh = new VertexHandler();
	private TextureHandler th  = new TextureHandler(this);
	private NormalHandler nh  = new NormalHandler();
	private int vboid = glGenBuffers();
	private int vaoid = glGenVertexArrays();
	private boolean real = true;
	private boolean oc = false;
	private boolean finished = false;
	
	private int texid;
	
	private OctreeCollision octree;
	
	private String name;
	
	private int selected;
	
	public ArrayList<Integer> texids = new ArrayList<Integer>(); 
	private ArrayList<Color> colors = new ArrayList<Color>(); 
	private BufferedImage texture;
	private boolean loaded = false;
	
	private IntBuffer indicesb = BufferUtils.createIntBuffer(0);
	private ArrayList<int[]> indices = new ArrayList<int[]>();
	private int noindices;
	private int nopoints;
	private boolean made = false;
	
	private Quaternion permangle = new Quaternion(0.0f, 0.0f, 0.0f, 1.0f);
	
	private Vector3f pos = new Vector3f(0.0f, 0.0f, 0.0f);
	private Matrix4f modelmatrix = new Matrix4f();
	private Matrix4f rotationmatrix = new Matrix4f();
	private FloatBuffer modelmatrixfb = BufferUtils.createFloatBuffer(16);
	
	private FloatBuffer onetriangle;
	
	private ColorHandler ch = new ColorHandler();
	
	public Shape(String name) {
		texids.add(ch.newCol(Color.BLUE, colors));
		updateModelMatrix();
		this.name = this.toString();
		if(!(name.equals(""))) {
			this.name = name;
		}
	}
	public Shape(Shape shape) {
		this.colors = new ArrayList<Color>(shape.colors);
		this.texids = new ArrayList<Integer>(shape.texids);
		this.texid = new Integer(shape.texid);
		this.th = shape.th;
		this.finished = new Boolean(shape.finished);
		this.indices = new ArrayList<int[]>(shape.indices);
		this.made = new Boolean(shape.made);
		this.modelmatrix = new Matrix4f(shape.modelmatrix);
		this.rotationmatrix = new Matrix4f(shape.rotationmatrix);
		this.name = new String(shape.name);
		this.noindices = new Integer(shape.noindices);
		this.nopoints = new Integer(shape.nopoints);
		this.oc = new Boolean(shape.oc);
		this.permangle = new Quaternion(shape.permangle);
		this.pos = new Vector3f(shape.pos);
		this.real = new Boolean(shape.real);
		this.selected = new Integer(shape.selected);
		this.vh = shape.vh;
		
		makeIndices();
		updateModelMatrix();
		
		onetriangle = BufferUtils.createFloatBuffer(vh.getpolygons().size()*30);
		onetriangle.put(vh.getPolygonsAsFloatBuffer());
		onetriangle.rewind();
		this.octree = new OctreeCollision(this);
	}
	public Matrix4f updateModelMatrix() {
		Matrix4f m4f = new Matrix4f();
		
		Matrix4f.mul(modelmatrix, rotationmatrix, m4f);
		
		modelmatrixfb.clear();
		m4f.store(modelmatrixfb);
		modelmatrixfb.flip();
		return m4f;
	}
	public int getPoints() {
		return nopoints;
	}
	public void setoc(boolean b) {
		oc = b;
	}
	public void move(float x, float y, float z) {
		modelmatrix.translate(new Vector3f(x, y, z));
		updateModelMatrix();
	}
	public void name(String str) {
		name = str;
	}
	public Quaternion getAxis() {
		return permangle;
	}
	public void rotate(float x, float y, float z) {
		double PI = Math.PI/180;
		float p = (float) (x*PI/2.0);
		float yaw = (float) (z*PI/2.0);
		float r = (float) (y*PI/2.0);
		
		double sinp = Math.sin(p);
		double siny = Math.sin(yaw);
		double sinr = Math.sin(r);
		double cosp = Math.cos(p);
		double cosy = Math.cos(yaw);
		double cosr = Math.cos(r);
		
		Quaternion local_rotation = new Quaternion();
		local_rotation.w  = (float) (cosr * cosp * cosy + sinr * sinp * siny);
		local_rotation.x = (float) (sinr * cosp * cosy - cosr * sinp * siny);
		local_rotation.y = (float) (cosr * sinp * cosy + sinr * cosp * siny);
		local_rotation.z = (float) (cosr * cosp * siny - sinr * sinp * cosy);
		local_rotation.normalise();
		
		Quaternion.mul(permangle, local_rotation, permangle);
		permangle.normalise();
		
		float x2 = permangle.x * permangle.x;
		float y2 = permangle.y * permangle.y;
		float z2 = permangle.z * permangle.z;
		float xy = permangle.x * permangle.y;
		float xz = permangle.x * permangle.z;
		float yz = permangle.y * permangle.z;
		float wx = permangle.w * permangle.x;
		float wy = permangle.w * permangle.y;
		float wz = permangle.w * permangle.z;
		
		rotationmatrix.m00 = 1.0f - 2.0f * (y2 + z2);
		rotationmatrix.m01 = 2.0f * (xy - wz);
		rotationmatrix.m02 = 2.0f * (xz + wy);
		rotationmatrix.m03 = 0.0f;
		
		rotationmatrix.m10 = 2.0f * (xy + wz);
		rotationmatrix.m11 = 1.0f - 2.0f * (x2 + z2);
		rotationmatrix.m12 = 2.0f * (yz - wx);
		rotationmatrix.m13 = 0.0f;
		
		rotationmatrix.m20 = 2.0f * (xz - wy);
		rotationmatrix.m21 = 2.0f * (yz + wx);
		rotationmatrix.m22 = 1.0f - 2.0f * (x2 + y2);
		rotationmatrix.m23 = 0.0f;
		
		rotationmatrix.m30 = pos.x;
		rotationmatrix.m31 = pos.y;
		rotationmatrix.m32 = pos.z;
		rotationmatrix.m33 = 1.0f;
		
		rotationmatrix.translate(new Vector3f(-pos.x, -pos.y, -pos.z));
		octree.update(this);
		updateModelMatrix();
	}
	public Vector3f getPos() {
		return pos;
	}
	public String getName() {
		return name;
	}
	public void setLocRot(Matrix4f model, Matrix4f rot, Quaternion angle, Vector3f pos) {
		this.pos = pos;
		modelmatrix = model;
		rotationmatrix = rot;
		permangle = angle;
		
		onetriangle = BufferUtils.createFloatBuffer(vh.getpolygons().size()*30);
		onetriangle.put(vh.getPolygonsAsFloatBuffer());
		onetriangle.rewind();
		
		updateModelMatrix();
		finished = true;
		octree = new OctreeCollision(this);
	}
	public Matrix4f getMatrix() {
		Matrix4f m4f = new Matrix4f();
		Matrix4f.mul(modelmatrix, rotationmatrix, m4f);
		return m4f;
	}
	public void resetPosition() {
		float lowestx = onetriangle.get(0), lowesty = onetriangle.get(1), lowestz = onetriangle.get(2);
		float highestx = onetriangle.get(0), highesty = onetriangle.get(1), highestz = onetriangle.get(2);
		
		for(int i=1; i<nopoints;i++) {
			if(onetriangle.get(i*10)>highestx) highestx=onetriangle.get(i*10);
			if(onetriangle.get(i*10)<lowestx) lowestx=onetriangle.get(i*10);
			
			if(onetriangle.get((i*10)+1)>highesty) highesty=onetriangle.get((i*10) + 1);
			if(onetriangle.get((i*10)+1)<lowesty) lowesty=onetriangle.get((i*10) + 1);
			
			if(onetriangle.get((i*10)+2)>highestz) highestz=onetriangle.get((i*10) + 2);
			if(onetriangle.get((i*10)+2)<lowestz) lowestz=onetriangle.get((i*10) + 2);
		}
		pos = new Vector3f(lowestx + Math.abs((highestx-lowestx)/2), lowesty + Math.abs((highesty-lowesty)/2), 
				lowestz + Math.abs((highestz-lowestz)/2));
		modelmatrix.translate(pos);
		updateModelMatrix();
	}
	public void draw(ShaderHandler sh, DisplaySetup d, float changex, float changey, float changez) {
		//Setup OPENGL stuff
		if(!made) {
			makeIndices();
		}
		DataUtils util = new DataUtils();
		if(oc) {
			texid = 0;
		}
		util.setup(onetriangle, vh, sh, texids.get(texid), 1, indicesb);
		//set matrix
		int modelmatrixloc = glGetUniformLocation(sh.getPrograms().get(0).getId(), "ModelMatrix");
		glUniformMatrix4(modelmatrixloc, false, modelmatrixfb);
		//Draw them as triangles
		glDrawArrays(GL_TRIANGLES, 0, noindices);
	}
	public void newCube(Vector4f pos, float width, float height, float depth, Color c, int texqual) {
		HighLevelObject objutil = new HighLevelObject();
		made = false; 
		
		int[] indicesa = {
				nopoints, nopoints+1, nopoints+2,
				nopoints+3, nopoints+4, nopoints+5,
				
				nopoints+6, nopoints+7, nopoints+8,
				nopoints+9, nopoints+10, nopoints+11,
				
				nopoints+12, nopoints+13, nopoints+14,
				nopoints+15, nopoints+16, nopoints+17,
				
				nopoints+18, nopoints+19, nopoints+20,
				nopoints+21, nopoints+22, nopoints+23,
				
				nopoints+24, nopoints+25, nopoints+26,
				nopoints+27, nopoints+28, nopoints+29,
				
				nopoints+30, nopoints+31, nopoints+32,
				nopoints+33, nopoints+34, nopoints+35,
		};
		noindices += 36;
		nopoints += 36;
		
		indices.add(indicesa);
		
		Integer.valueOf("3");
		try {
			objutil.newCube(pos, width, height, depth, c, texqual, vh, th);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void newTriangle(Vector4f[] points, Vector4f[] norms, Vector2f[] texcoords, int texqual, Color c, 
			boolean maketexture) {
		LowLevelObject objutil = new LowLevelObject();
		setoc(maketexture);
		made = false;
		
		int[] indicesa = {
				nopoints, nopoints+1, nopoints+2,  
		};
		noindices += 3;
		nopoints += 3;
		
		indices.add(indicesa);
		
		try {
			if(maketexture) {
				objutil.newTriangle(points, norms, null, 0, c, vh, th);
			} else {
				objutil.newTriangle(points, norms, texcoords, texqual, c, vh, th);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void makeIndices() {
		indicesb = BufferUtils.createIntBuffer(noindices);
		for(int i=0; i<indices.size();i++) {
			indicesb.put(indices.get(i));
		}
		indicesb.flip();
		made = true;
	}
	public Vector2f[] getTexCoords() {
		return new Vector2f[]{
				new Vector2f(0.5f, 0.5f),
				new Vector2f(0.5f, 0.5f),	
				new Vector2f(0.5f, 0.5f),};
	}
	public void setTexId(int texid, BufferedImage img) {
		this.texture = img;
		this.loaded = true;
		texids.add(texid);
		this.texid = texids.size()-1;
		this.oc = false;
	}
	public void finish() {
		finished = true;
		onetriangle = BufferUtils.createFloatBuffer(vh.getpolygons().size()*30);
		onetriangle.put(vh.getPolygonsAsFloatBuffer());
		onetriangle.rewind();
		octree = new OctreeCollision(this);
		if(oc) {
			texids.add(th.getTextureAtlas().getTextureID());
			texid = texids.size()-1;
		}
		resetPosition();
	}
	public FloatBuffer getdata() {
		return onetriangle;
	}
	public OctreeCollision getCollision() {
		return octree;
	}
	public float opp(float f) {
		return f * -1;
	}
	public boolean closeEnoughTo(float bound, float number, float number2) {
		if(number > number2 - bound && number < number2 + bound) {
			return true;
		} else {
			return false;
		}
	}
	public Vector4f getPos(int index) {
		FloatBuffer p;
		int stage = getStageOfPolygon(index);
		p = getPolygon(index);
		
		return new Vector4f(p.get((stage * 10) + 0), p.get((stage * 10) + 1), p.get((stage * 10) + 2), 
				p.get((stage * 10) + 3));
	}
	public Vector2f getTexCood(int index) {
		FloatBuffer p;
		int stage = getStageOfPolygon(index);
		p = getPolygon(index);
		
		return new Vector2f(p.get((stage * 10) + 4), p.get((stage * 10) + 5));
	}
	public Vector4f getNorm(int index) {
		FloatBuffer p;
		int stage = getStageOfPolygon(index);
		p = getPolygon(index);
		
		return new Vector4f(p.get((stage * 10) + 6), p.get((stage * 10) + 7), p.get((stage * 10) + 8), 
				p.get((stage * 10) + 9));
	}
	public FloatBuffer getPolygon(int index) {
		int ploc = 0;
		int stage = 0;
		
		for(int i = 0; i < index;i++) {
			if(stage == 2) {
				stage = 0;
				ploc += 1;
			} else {
				stage += 1;
			}
		}
		
		return this.vh.getPolygonAsFloatBuffer(ploc);
	}
	public int getStageOfPolygon(int index) {
		int ploc = 0;
		int stage = 0;
		
		for(int i = 0; i < index;i++) {
			if(stage == 2) {
				stage = 0;
				ploc += 1;
			} else {
				stage += 1;
			}
		}
		
		return stage;
	}
	public VertexHandler getVh() {
		return vh;
	}
	public TextureHandler getTh() {
		return th;
	}
	public NormalHandler getNh() {
		return nh;
	}
	public void setReal(boolean torf) {
		real = torf;
	}
	public ArrayList<Polygon> getPolygons() {
		return this.vh.getpolygons();
	}
	public Matrix4f getRotMatrix() {
		return rotationmatrix;
	}
	public Matrix4f getModelMatrix() {
		return modelmatrix;
	}
}
