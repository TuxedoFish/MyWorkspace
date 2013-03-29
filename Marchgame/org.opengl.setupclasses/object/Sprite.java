package object;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glDrawElements;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import texture.TextureHolder;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import shader.ShaderHandler;
import start.Controller;
import utils.DataUtils;
import utils.TextureUtils;

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

public class Sprite{
	private BufferedImage texture;
	
	private float[] data;
	
	private FloatBuffer datafb;
	private IntBuffer indices;
	
	private int textureid;
	private int vboID = glGenBuffers();
	private int vaoID = glGenVertexArrays();
	
	private int width = 0, height = 0;
	private TextureHolder th;
	
	private Matrix4f modelmatrix = new Matrix4f();
	private Matrix4f rotmatrix = new Matrix4f();
	
	private Vector2f pos = new Vector2f();
	private int textureids;
	
	private boolean any = false;
	
	private Controller parent;
	
	public Sprite(BufferedImage img, Controller c, int width, int height, TextureHolder th, int texid, Vector2f pos) {
		parent = c;
		any = true;
		this.th = th;
		texture = img;
		TextureUtils util = new TextureUtils();
		textureid = util.binddata(texture);
		
		modelmatrix.translate(pos);
		
		this.width = width;
		this.height = height;
		this.pos = pos;
		datafb = getData(this.pos, width, height, texid);
		indices = getIndices(0);
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public boolean contains(Sprite s) {
		return false;
	}
	public Matrix4f getModelMatrix() {
		return modelmatrix;
	}
	public FloatBuffer getData(Vector2f pos, int width, int height, int texid) {
		FloatBuffer datafb;
		
		float z = 0.0f;
		float realwidth = (float)width/Display.getWidth();
		float realheight = (float)height/Display.getHeight();
		Vector2f[] tc = th.getTextureCoords(texid);
		
		data = new float[]{
				//0
				0.0f, 0.0f, z, 1.0f, 
				tc[0].x, tc[0].y, 
				0.0f, 0.0f, .0f, 0.0f,
				//1
				realwidth, 0.0f, z, 1.0f,
				tc[1].x, tc[1].y, 
				0.0f, 0.0f, 0.0f, 0.0f,
				//2
				realwidth, -realheight, z, 1.0f,
				tc[2].x, tc[2].y, 
				0.0f, 0.0f, 0.0f, 0.0f,
				//3
				0.0f, 0.0f, z, 1.0f, 
				tc[0].x, tc[0].y, 
				0.0f, 0.0f, 0.0f, 0.0f,
				//4
				realwidth, -realheight, z, 1.0f,
				tc[2].x, tc[2].y, 
				0.0f, 0.0f, 0.0f, 0.0f,
				//5
				0.0f, -realheight, z, 1.0f, 
				tc[3].x, tc[3].y, 
				0.0f, 0.0f, 0.0f, 0.0f,
		};
		
		datafb = BufferUtils.createFloatBuffer(60);
		datafb.put(data);
		datafb.flip();
		
		return datafb;
	}
	public IntBuffer getIndices(int index) {
		IntBuffer indicesloc;
		
		int[] i = {
				(index*6), (index*6) + 1, (index*6) + 2, 
				(index*6) + 3, (index*6) + 4, (index*6) + 5
		};
		indicesloc = BufferUtils.createIntBuffer(6);
		indicesloc.put(i);
		indicesloc.flip();
		
		return indicesloc;
	}
	public boolean empty() {
		Boolean any = this.any;
		if(any != null) {
			return !any;
		} else {
			return false;
		}
	}
	public void changePos(float x, float y) {
		if(any) {
				pos = new Vector2f(pos.x + x, pos.y + y);
				modelmatrix.translate(new Vector2f(x, y));
		}
	}
	public void changeTexture(int texid) {
		datafb = getData(pos, width, height, texid);
	}
	public void render(ShaderHandler sh) {
		if(any) {
			DataUtils util = new DataUtils();
			
			util.setup(datafb, vaoID, vboID, parent.getSh(), textureid, 2, indices);
			
			FloatBuffer modelmatrixfb = BufferUtils.createFloatBuffer(16);
			Matrix4f.mul(rotmatrix, modelmatrix, new Matrix4f()).store(modelmatrixfb);
			modelmatrixfb.flip();
			
			int modelmatrixloc = glGetUniformLocation(sh.getPrograms().get(0).getId(), "ModelMatrix");
			glUniformMatrix4(modelmatrixloc, false, modelmatrixfb);
			
			glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
		}
	}
	public void rotate(float angle) {
		modelmatrix.translate(new Vector2f(pos.x, pos.y));
		modelmatrix.rotate(angle, new Vector3f(0.0f, 0.0f, 1.0f));
		modelmatrix.translate(new Vector2f(-pos.x, -pos.y));
	}
	public Vector2f getPos() {
		return pos;
	}
	public void setPos(float x, float y) {
		if(any) {
			modelmatrix.translate(new Vector2f(x-pos.x, y-pos.y));
			pos = new Vector2f(x, y);
		}
	}
	public BufferedImage getTexture() {
		return texture;
	}
	public int gettexid() {
		return textureid;
	}
}
