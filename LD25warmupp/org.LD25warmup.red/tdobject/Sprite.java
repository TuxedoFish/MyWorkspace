package tdobject;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glDrawElements;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import level.TextureHolder;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

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

public class Sprite {
	BufferedImage texture;
	
	float[] data;
	
	FloatBuffer datafb;
	IntBuffer indices;
	
	int textureid;
	int vboID = glGenBuffers();
	int vaoID = glGenVertexArrays();
	
	int width, height;
	TextureHolder th;
	
	Vector2f pos = new Vector2f(0.0f, 0.5f);
	
	Controller parent;
	
	public Sprite(File f, Controller c, int width, int height, TextureHolder th, int texid) {
		parent = c;
		this.th = th;
		try {
			texture = ImageIO.read(f);
		} catch (IOException e) {
			System.err.println("failed to load image @ " + f);
			e.printStackTrace();
			System.exit(1);
		}
		TextureUtils util = new TextureUtils();
		textureid = util.binddata(texture);
		
		this.width = width;
		this.height = height;
		setData(pos, width, height, texid);
	}
	public Sprite(BufferedImage img, Controller c, int width, int height, TextureHolder th, int texid) {
		parent = c;
		this.th = th;
		texture = img;
		TextureUtils util = new TextureUtils();
		textureid = util.binddata(texture);
		
		this.width = width;
		this.height = height;
		setData(pos, width, height, texid);
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
	public void setData(Vector2f pos, int width, int height, int texid) {
		
		float z = 0.0f;
		float realwidth = (float)width/Display.getWidth();
		float realheight = (float)height/Display.getHeight();
		Vector2f[] tc = th.getTextureCoords(texid);
		
		data = new float[]{
				//0
				pos.x, pos.y, z, 1.0f, 
				tc[0].x, tc[0].y, 
				0.0f, 0.0f, .0f, 0.0f,
				//1
				pos.x + realwidth, pos.y, z, 1.0f,
				tc[1].x, tc[1].y, 
				0.0f, 0.0f, 0.0f, 0.0f,
				//2
				pos.x + realwidth, pos.y - realheight, z, 1.0f,
				tc[2].x, tc[2].y, 
				0.0f, 0.0f, 0.0f, 0.0f,
				//3
				pos.x, pos.y, z, 1.0f, 
				tc[0].x, tc[0].y, 
				0.0f, 0.0f, 0.0f, 0.0f,
				//4
				pos.x + realwidth, pos.y - realheight, z, 1.0f,
				tc[2].x, tc[2].y, 
				0.0f, 0.0f, 0.0f, 0.0f,
				//5
				pos.x, pos.y - realheight, z, 1.0f, 
				tc[3].x, tc[3].y, 
				0.0f, 0.0f, 0.0f, 0.0f,
		};
		int[] i = {
				0, 1, 2, 
				3, 4, 5
		};
		indices = BufferUtils.createIntBuffer(data.length);
		indices.put(i);
		indices.flip();
		
		datafb = BufferUtils.createFloatBuffer(data.length);
		datafb.put(data);
		datafb.flip();
	}
	public void changePos(float x, float y, int texid) {
		pos.x += x;
		pos.y += y;
		
		setData(pos, width, height, texid);
	}
	public void render() {
		DataUtils util = new DataUtils();
		
		util.setup(datafb, vaoID, vboID, parent.getSh(), textureid, 2, indices);
		
		glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
	}
	public Vector2f getPos() {
		return pos;
	}
	public void setPos(float x, float y) {
		pos = new Vector2f(x, y);
	}
	public BufferedImage getTexture() {
		return texture;
	}
}
