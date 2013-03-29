package tdobject;

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

public class Sprite{
	private BufferedImage texture;
	
	private float[] data;
	
	private FloatBuffer datafb;
	private IntBuffer indices;
	
	private int textureid;
	private int vboID = glGenBuffers();
	private int vaoID = glGenVertexArrays();
	
	private int width = 0, height = 0, mode;
	private TextureHolder th;
	
	private ArrayList<Vector2f> pos = new ArrayList<Vector2f>();
	private ArrayList<Integer> textureids = new ArrayList<Integer>();
	
	private boolean any = false;
	
	private Controller parent;
	
	public int getMode() {
		return mode;
	}
	public Sprite(BufferedImage img, Controller c, int width, int height, TextureHolder th,
			 ArrayList<Integer> textureids, ArrayList<Vector2f> pos) {
		any = true;
		parent = c;
		this.th = th;
		texture = img;
		
		TextureUtils util = new TextureUtils();
		textureid = util.binddata(texture);
		
		this.width = width;
		this.height = height;
		this.mode = 2;
		
		indices = BufferUtils.createIntBuffer(pos.size()*6);
		datafb = BufferUtils.createFloatBuffer(pos.size()*60);
		this.pos = pos;
		this.textureids = textureids;
		
		for(int i = 0; i < pos.size(); i++) {
			FloatBuffer toadd = getData(pos.get(i), width, height, textureids.get(i));
			IntBuffer toaddi = getIndices(i);
			
			for(int k = 0; k < toaddi.capacity(); k++) {
				indices.put((i*6) + k, toaddi.get(k));
			}
			for(int j = 0; j < toadd.capacity(); j++) {
				datafb.put((i*60) + j, toadd.get(j));
			}
		}
	}
	public Sprite(File f, Controller c, int width, int height, TextureHolder th, int texid) {
		parent = c;
		this.th = th;
		this.mode = 1;
		any = true;
		
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
		pos.add(new Vector2f(0.5f, 0.0f));
		getData(pos.get(0), width, height, texid);
		indices = getIndices(0);
		this.mode = 1;
	}
	public Sprite(BufferedImage img, Controller c, int width, int height, TextureHolder th, int texid, Vector2f pos) {
		parent = c;
		any = true;
		this.th = th;
		texture = img;
		this.mode = 1;
		TextureUtils util = new TextureUtils();
		textureid = util.binddata(texture);
		
		this.width = width;
		this.height = height;
		this.pos.add(pos);
		datafb = getData(this.pos.get(0), width, height, texid);
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
	public FloatBuffer getData(Vector2f pos, int width, int height, int texid) {
		FloatBuffer datafb;
		
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
	public void deleteAll() {
		any = false;
		pos.clear();
		textureids.clear();
	}
	public boolean empty() {
		Boolean any = this.any;
		if(any != null) {
			return !any;
		} else {
			return false;
		}
	}
	public void changePos(float x, float y, ArrayList<Integer> texid, int index) {
		if(any) {
			if(mode == 1) {
				pos.set(0, new Vector2f(pos.get(index).x + x, pos.get(index).y + y));
			
				datafb = getData(pos.get(index), width, height, texid.get(0));
				indices = getIndices(index);
			} else {
				if(index == -1) {
				indices = BufferUtils.createIntBuffer(pos.size()*6);
				datafb = BufferUtils.createFloatBuffer(pos.size()*60);
				
				for(int i = 0; i < pos.size(); i++) {
					pos.set(i, new Vector2f(pos.get(i).x + x, pos.get(i).y + y));
					
					FloatBuffer toadd = getData(pos.get(i), width, height, texid.get(i));
					IntBuffer toaddi = getIndices(i);
					
					for(int k = 0; k < toaddi.capacity(); k++) {
						if(this.empty()) {
							indices = null;
							datafb = null;
							return;
						}
						indices.put((i*6) + k, toaddi.get(k));
					}
					for(int j = 0; j < toadd.capacity(); j++) {
						if(this.empty()) {
							indices = null;
							datafb = null;
							return;
						}
						datafb.put((i*60) + j, toadd.get(j));
					}
				}
				} else {
					indices = BufferUtils.createIntBuffer(pos.size()*6);
					datafb = BufferUtils.createFloatBuffer(pos.size()*60);
					
					pos.set(index, new Vector2f(pos.get(index).x + x, pos.get(index).y + y));
					
					for(int i = 0; i < pos.size(); i++) {
						FloatBuffer toadd = getData(pos.get(i), width, height, texid.get(i));
						IntBuffer toaddi = getIndices(i);
						for(int k = 0; k < toaddi.capacity(); k++) {
							if(this.empty()) {
								indices = null;
								datafb = null;
								return;
							}
							indices.put((i*6) + k, toaddi.get(k));
						}
						for(int j = 0; j < toadd.capacity(); j++) {
							if(this.empty()) {
								indices = null;
								datafb = null;
								return;
							}
							datafb.put((i*60) + j, toadd.get(j));
						}
					}
				}
			}
		}
	}
	public void render() {
		if(any) {
			DataUtils util = new DataUtils();
			
			util.setup(datafb, vaoID, vboID, parent.getSh(), textureid, 2, indices);
	
			glDrawElements(GL_TRIANGLES, pos.size()*6, GL_UNSIGNED_INT, 0);
		}
	}
	public Vector2f getPos(int index) {
		if(any) {
			return pos.get(index);
		} else {
			System.err.println("no obj");
			System.exit(1);
			return null;
		}
	}
	public void setPos(float x, float y, ArrayList<Integer> texid, int index) {
		if(any) {
			if(mode == 1) {
				pos.set(0, new Vector2f(x, y));
			
				datafb = getData(pos.get(index), width, height, texid.get(0));
				indices = getIndices(index);
			} else {
				if(index == -1) {
				indices = BufferUtils.createIntBuffer(pos.size()*6);
				datafb = BufferUtils.createFloatBuffer(pos.size()*60);
				
				for(int i = 0; i < pos.size(); i++) {
					pos.set(i, new Vector2f(x, y));
					
					FloatBuffer toadd = getData(pos.get(i), width, height, texid.get(i));
					IntBuffer toaddi = getIndices(i);
					
					for(int k = 0; k < toaddi.capacity(); k++) {
						indices.put((i*6) + k, toaddi.get(k));
					}
					for(int j = 0; j < toadd.capacity(); j++) {
						datafb.put((i*60) + j, toadd.get(j));
					}
				}
				} else {
					indices = BufferUtils.createIntBuffer(pos.size()*6);
					datafb = BufferUtils.createFloatBuffer(pos.size()*60);
					
					pos.set(index, new Vector2f(x, y));
					
					for(int i = 0; i < pos.size(); i++) {
						FloatBuffer toadd = getData(pos.get(i), width, height, texid.get(i));
						IntBuffer toaddi = getIndices(i);
						
						for(int k = 0; k < toaddi.capacity(); k++) {
							indices.put((i*6) + k, toaddi.get(k));
						}
						for(int j = 0; j < toadd.capacity(); j++) {
							datafb.put((i*60) + j, toadd.get(j));
						}
					}
				}
			}
		}
	}
	public BufferedImage getTexture() {
		return texture;
	}
	public void addObject(float x, float y, int texid) {
		this.pos.add(new Vector2f(x, y));
		this.textureids.add(texid);
		
		indices = BufferUtils.createIntBuffer(pos.size()*6);
		datafb = BufferUtils.createFloatBuffer(pos.size()*60);
		
		for(int i = 0; i < pos.size(); i++) {
			FloatBuffer toadd = getData(pos.get(i), width, height, textureids.get(i));
			IntBuffer toaddi = getIndices(i);
			
			for(int k = 0; k < toaddi.capacity(); k++) {
				indices.put((i*6) + k, toaddi.get(k));
			}
			for(int j = 0; j < toadd.capacity(); j++) {
				datafb.put((i*60) + j, toadd.get(j));
			}
		}
		any = true;
	}
}
