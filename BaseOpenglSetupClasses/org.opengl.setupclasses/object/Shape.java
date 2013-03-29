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
import java.nio.FloatBuffer;
import java.util.ArrayList;


import normal.NormalHandler;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import shader.ShaderHandler;
import start.Controller;
import start.DisplaySetup;
import texture.TextureHandler;
import utils.DataUtils;
import utils.Line;
import utils.LineCollection;

public class Shape {
	private VertexHandler vh = new VertexHandler();
	private TextureHandler th  = new TextureHandler(this);
	private NormalHandler nh  = new NormalHandler();
	private int vboid = glGenBuffers();
	private int vaoid = glGenVertexArrays();
	private boolean real = true;
	private boolean oc = false;
	
	private int selected;
	
	public ArrayList<Integer> coltexids = new ArrayList<Integer>(); 
	private ArrayList<Color> colors = new ArrayList<Color>(); 
	

	private FloatBuffer onetriangle;
	
	private ColorHandler ch = new ColorHandler();
	
	public Shape() {
		coltexids.add(ch.newCol(Color.BLUE, colors));
	}
	public void setoc(boolean b) {
		oc = b;
	}
	public void draw(ShaderHandler sh, DisplaySetup d, float changex, float changey, float changez) {
		//Setup OPENGL stuff
		DataUtils util = new DataUtils();
		if(!oc) {
			util.setup(onetriangle, vh, sh, th.getTextureAtlas().getTextureID(), 1, null);
		} else {
			util.setup(onetriangle, vh, sh, coltexids.get(0), 1, null);
		}
		//Draw them as triangles
		glDrawArrays(GL_TRIANGLES, 0, onetriangle.capacity()/10);
				
		HighLevelObject objutil = new HighLevelObject();
		LineCollection lc = objutil.get3dRectWireframe(getPos(selected), sh, d);
			
		//VERRRYYYYYYYYYYY
		//SLOWWW
		
		/*for(int i = 0; i < vh.getpolygons().size(); i++) {
			Vector4f temp = new Vector4f(onetriangle.get(0), onetriangle.get(1), onetriangle.get(2), onetriangle.get(3));
			if(temp.x == getPos(selected).x && temp.y == getPos(selected).y && temp.z == getPos(selected).z) {
				vh.getPolygon(i).changePos(1, changex/(Display.getWidth()/2), changey/(Display.getHeight()/2), 
						changez/100);
			}
			temp = new Vector4f(onetriangle.get(10), onetriangle.get(11), onetriangle.get(12), onetriangle.get(13));
			if(temp.x == getPos(selected).x && temp.y == getPos(selected).y && temp.z == getPos(selected).z) {
				vh.getPolygon(i).changePos(2, changex/(Display.getWidth()/2), changey/(Display.getHeight()/2), 
					changez/100);
			}
			temp = new Vector4f(onetriangle.get(20), onetriangle.get(21), onetriangle.get(22), onetriangle.get(23));
			if(temp.x == getPos(selected).x && temp.y == getPos(selected).y && temp.z == getPos(selected).z) {
				vh.getPolygon(i).changePos(3, changex/(Display.getWidth()/2), changey/(Display.getHeight()/2), 
					changez/100);
			}
		}*/
				
		FloatBuffer Lines = BufferUtils.createFloatBuffer(lc.getData().capacity());
		Lines.position(0);
		Lines.put(lc.getData());
		Lines.rewind();
		
		if(real) {
				//Setup OPENGL stuff
				util.setup(Lines, vh, sh, coltexids.get(0), 1, null);
				//Draw them as triangles
			    glDrawArrays(GL_LINES, 0, Lines.capacity()/10);
		}
	}
	public void newTriangle(Vector4f[] points, Vector4f[] norms, Vector2f[] texcoords, int texqual, Color c, 
			boolean maketexture) {
		LowLevelObject objutil = new LowLevelObject();
		setoc(maketexture);
		
		try {
			if(maketexture) {
				objutil.newTriangle(points, norms, texcoords, 0, c, vh, th);
			} else {
				objutil.newTriangle(points, norms, null, texqual, c, vh, th);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public Vector2f[] getTexCoords() {
		return new Vector2f[]{
				new Vector2f(0.5f, 0.5f),
				new Vector2f(0.5f, 0.5f),	
				new Vector2f(0.5f, 0.5f),};
	}
	public void finish() {
		onetriangle = BufferUtils.createFloatBuffer(vh.getpolygons().size()*30);
		onetriangle.put(vh.getPolygonsAsFloatBuffer());
		onetriangle.rewind();
	}
	public void mouseUpdate(int x, int y, DisplaySetup d) {
		Vector4f pos;
		Vector4f cpos = new Vector4f(d.getPos().x, d.getPos().y, d.getPos().z, 1.0f);
		float xrot = d.getxrot();
		float yrot = d.getyrot();
		
		for(int i = 0; i < this.vh.getpolygons().size()*3; i++) {
			pos = getPos(i);
			
			Matrix4f eyecoords = new Matrix4f();
			
			eyecoords = Matrix4f.mul(d.getProjectionMatrixAsMatrix(), d.getModelViewMatrixAsMatrix(), eyecoords);
			
			pos = Matrix4f.transform(eyecoords, pos, pos);
			
				if(closeEnoughTo(0.1f, (float) ((x/((float)Display.getWidth()/2)) - 1.0), (float)pos.x)) {
					if(closeEnoughTo(0.1f, (float) ((y/((float)Display.getHeight()/2)) - 1.0), (float)pos.y)) {
							selected = i;
				}
			}
		}
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
}
