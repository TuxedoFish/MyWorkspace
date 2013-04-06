package start.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

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

import shader.ShaderHandler;
import utils.DataUtils;
import utils.MathUtils;
import utils.TextureUtils;

public class Window {
	private float width, height;
	private int texID;
	private FloatBuffer windowdata;
	private ShaderHandler sh;
	private int vboID = glGenBuffers(), vaoID = glGenVertexArrays();
	private int[] indices = {};
	private Rectangle leftbox, rightbox, topbox, bottombox, movebox, wholebox;
	private Boolean left, right, top, bottom, move;
	private boolean resizable = true;
	private GuiElementHandler gui = new GuiElementHandler();
	
	public Window(float x, float y, float width, float height, ShaderHandler sh) {
		this.width = width; this.height = height;
		this.sh = sh;
		texID = glGenTextures();
		
		MathUtils util = new MathUtils();
		x = util.clamp(x, Display.getWidth());
		y = util.clamp(y, Display.getHeight());
		
		float[] window = makeWindow(texID, new Vector4f(x/(Display.getWidth()/2) - 1.0f, y/(Display.getHeight()/2) - 1.0f, -1.0f, 1.0f), 
				width/(Display.getWidth()/2), height/(Display.getHeight()/2), indices);
		
		this.windowdata = BufferUtils.createFloatBuffer(window.length);
		this.windowdata.put(window);
		this.windowdata.rewind();
		
		indices = getIndices();
		
		setupCollisionBoxs();
	}
	private void setupCollisionBoxs() {
		MathUtils util = new MathUtils();
		Vector4f p1 = util.floatToInt(getPos(1));
		Vector4f p2 = util.floatToInt(getPos(2));
		Vector4f p3 = util.floatToInt(getPos(3));
		Vector4f p4 = util.floatToInt(getPos(4));
		
		topbox = new Rectangle((int)p1.x, (int)p1.y - 10, (int)Math.abs(p1.x - p2.x), 10);
		bottombox = new Rectangle((int)p4.x, (int)p3.y, (int)Math.abs(p4.x - p3.x), 10);
		leftbox = new Rectangle((int)p1.x, (int)p4.y, 10, (int)(Math.abs(p1.y - p4.y)));
		rightbox = new Rectangle((int)p2.x - 10, (int)p3.y, 10, (int)(Math.abs(p2.y - p3.y)));
		movebox = new Rectangle((int)p1.x, (int)(p1.y - 30), (int)Math.abs(p1.x - p2.x), (int)30);
		
		wholebox = new Rectangle((int)p1.x, (int)p3.y, (int)Math.abs(p4.x - p3.x), (int)(Math.abs(p2.y - p3.y)));
	}
	private int[] getIndices() {
		int[] indices = {
				0, 1, 2,
				2, 3, 0,
		};
		
		return indices;
	}
	private float[] makeWindow(int textureID, Vector4f topleft, float width, float height, int[] indices) {
		float[] rectdata = {
				//MAIN
				topleft.x, topleft.y, topleft.z, topleft.w, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f,	
				topleft.x + width, topleft.y, topleft.z, topleft.w, 0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f,	
				topleft.x + width, topleft.y - height, topleft.z, topleft.w, 0.5f, 0.5f, 0.0f, 0.0f, 0.0f, 1.0f,	
				topleft.x, topleft.y - height, topleft.z, topleft.w, 0.0f, 0.5f, 0.0f, 0.0f, 0.0f, 1.0f,	
				};
		
		BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();
		
		g.setColor(Color.black);
		g.fillRect(0, 0, 50, 50);
		
		g.setColor(new Color(0.0f, 0.0f, 1.0f, 1.0f));
		g.fillRect(50, 50, 50, 50);
		
		TextureUtils util = new TextureUtils();
		
		util.binddata(img, textureID);
		
		gui.clearElements();
		gui.newString("gotta love testing ", Color.blue, width, height, new Vector2f(topleft.x, topleft.y));
		gui.newString("wooo writing", Color.blue, width, height, new Vector2f(topleft.x, topleft.y));
		gui.newLine();
		gui.newString("new line", Color.blue, width, height, new Vector2f(topleft.x, topleft.y));
		return rectdata;
	}
	public void draw() {
		//Bind to a Vertex Array Object
		int indicesCount = indices.length;
		IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indicesCount);
		indicesBuffer.put(indices);
		indicesBuffer.flip();
		
		DataUtils util = new DataUtils();
		
        util.setup(windowdata, vboID, vaoID, sh, texID, 2, indicesBuffer, null, 0);
        
		glDrawElements(GL_TRIANGLES, indicesCount, GL_UNSIGNED_INT, 0);
		
		gui.drawElements(sh);
	}
	public void mouseTests(int x, int y, float chgx, float chgy) {
		right = rightbox.contains(new Point(x, y));
		left = leftbox.contains(new Point(x, y));
		top = topbox.contains(new Point(x, y));
		bottom = bottombox.contains(new Point(x, y));
		
		if(top == false && right == false && left == false) {
			move = movebox.contains(new Point(x, y));
		} else {
			move = false;
		}
	}
	public boolean isTouching(int x, int y) {
		return wholebox.contains(new Point(x, y));
	}
	public void moveit(float chgx, float chgy) {
		Vector4f p1 = getPos(1);
		Vector4f p2 = getPos(2);
		Vector4f p3 = getPos(3);
		Vector4f p4 = getPos(4);
		
		if(top != null) {
			if(move) { Vector4f[] v = {p1, p2, p3, p4}; movePoints(v, chgx, chgy);}
			if(top) { Vector4f[] v = {p1, p2}; movePoints(v, 0.0f, chgy);}
			if(bottom) { Vector4f[] v = {p3, p4}; movePoints(v, 0.0f, chgy); }
			if(right) { Vector4f[] v = {p2, p3}; movePoints(v, chgx, 0.0f); }
			if(left) { Vector4f[] v = {p1, p4}; movePoints(v, chgx, 0.0f); }
		}
	}
	public void movePoints(Vector4f[] v,float x, float y) {
		for(int i = 0;i < windowdata.capacity()/10; i++) {
			for(int j = 0; j < v.length; j++) {
				if(windowdata.get(i*10) == v[j].x && windowdata.get((i*10) + 1) == v[j].y) {
					changePos(i + 1, x, y);
				}
			}
		}
		setupCollisionBoxs();
		
		float[] window = makeWindow(texID, (getPos(1)), Math.abs((getPos(1).x) - (getPos(2).x)), 
				Math.abs((getPos(1).y) - (getPos(4).y)), indices);
		
		this.windowdata = BufferUtils.createFloatBuffer(window.length);
		this.windowdata.put(window);
		this.windowdata.rewind();
	}
	public float convertx(float f) {
		f = (f + 1.0f) * (Display.getWidth()/2);
		return f;
	}
	public float converty(float f) {
		f = (f + 1.0f) * (Display.getHeight()/2);
		return f;
	}
	public Vector4f convert(Vector4f v) {
		v.x = (v.x + 1.0f) * (Display.getWidth()/2);
		v.y = (v.y + 1.0f) * (Display.getHeight()/2);
		return v;
	}
	public void changePos(int index, float x, float y) {
		windowdata.put((index - 1)*10, windowdata.get((index - 1)*10) + (x/(Display.getWidth()/2)));
		windowdata.put((((index - 1)*10) + 1), windowdata.get(((index - 1)*10) + 1) + (y/(Display.getHeight()/2)));
	}
	public Vector4f getPos(int index) {
		return new Vector4f(windowdata.get((index - 1)*10), windowdata.get(((index - 1)*10) + 1), 
				windowdata.get(((index - 1)*10) + 2), windowdata.get(((index - 1)*10) + 3));
	}
	public void setResizable(boolean state) {
		this.resizable = state;
	}
}
