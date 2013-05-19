package start.gui;

import images.ImageReturn;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.text.AttributedString;

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

import javax.imageio.ImageIO;

import logic.GridParser;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;

import start.Controller;
import texture.TextureHolder;
import utils.TextureUtils;

public class GuiMarker implements GuiElement{
	private BufferedImage img;
	private Vector2f pos;
	private IntBuffer indicesfb;
	private FloatBuffer datafb;
	private Rectangle2D bounds;
	private int current = 0;
	private Controller parent;
	private String message;
	private int texid;
	private boolean animation;
	private TextureHolder texture;
	private int textures;
	private int animationid;
	private int currenttexid = 0;
	private FloatBuffer matrixfb;
	private Matrix4f matrix;
	
	public GuiMarker(String markername, Vector2f pos, float width, float height, Controller parent, 
			String eventmessage, int animationid, int textures) {
		this.parent = parent;
		this.message = eventmessage;
		this.pos = pos;
		this.animationid = animationid;
		int[] indices = {
				0, 1, 2,
				2, 3, 0
		};
		indicesfb = BufferUtils.createIntBuffer(indices.length);
		indicesfb.put(indices);
		indicesfb.flip();
		
		matrix = new Matrix4f();
		matrixfb = BufferUtils.createFloatBuffer(16);
		matrix.store(matrixfb);
		matrixfb.flip();
		
		this.textures = textures;
		
		ImageReturn images = new ImageReturn();
		GridParser gp = new GridParser();
		
		try {
			img = images.getImage("gui/" + markername + ".png");
			texture = gp.parseGrid(img, 20);
			
			TextureUtils util = new TextureUtils();
			texid = util.binddata(img);
		} catch (IOException e) {
			System.err.println("err finding button @ " + markername);
			e.printStackTrace();
		}
		
		float[] data = {
				pos.x, pos.y, 0.0f, 1.0f, 	0.0f, 0.0f,		0.0f, 0.0f, 0.0f, 1.0f,
				
				pos.x + (float)((width/Display.getWidth())*2), pos.y, 0.0f, 1.0f, 	 1.0f, 0.0f, 
				0.0f, 0.0f, 0.0f, 1.0f,
				
				pos.x + (float)((width/Display.getWidth())*2), pos.y - (float)((height/Display.getHeight())*2), 0.0f, 1.0f, 
				1.0f, 1.0f, 
				0.0f, 0.0f, 0.0f, 1.0f,
				
				pos.x, pos.y - (float)((height/Display.getHeight())*2), 0.0f, 1.0f, 0.0f, 1.0f, 
				0.0f, 0.0f, 0.0f, 1.0f,
		};
		
		datafb = BufferUtils.createFloatBuffer(data.length);
		datafb.put(data);
		datafb.rewind();
		
		Vector2f[] texids = texture.getTextureCoords(0);
		
		datafb.put(4, texids[0].x); datafb.put(5, texids[0].y);
		datafb.put(14, texids[1].x); datafb.put(15, texids[1].y);
		datafb.put(24, texids[2].x); datafb.put(25, texids[2].y);
		datafb.put(34, texids[3].x); datafb.put(35, texids[3].y);
		
		this.bounds = new Rectangle2D.Float((pos.x+1.0f)*(Display.getWidth()/2), (pos.y+1.0f)*(Display.getHeight()/2), width, height);
	}
	public void animate() {
		if(animation) {
			Vector2f[] texids;
			if(currenttexid < textures-1) {
				texids = texture.getTextureCoords(currenttexid + 1);
				currenttexid += 1;
			} else {
				texids = texture.getTextureCoords(0);
				currenttexid = 0;
			}
			datafb.put(4, texids[0].x); datafb.put(5, texids[0].y);
			datafb.put(14, texids[1].x); datafb.put(15, texids[1].y);
			datafb.put(24, texids[2].x); datafb.put(25, texids[2].y);
			datafb.put(34, texids[3].x); datafb.put(35, texids[3].y);
		}
	}
	public void pressed() {
		parent.action(message);
	}
	public Rectangle2D getBounds() { 
		return bounds;
	}
	public Vector2f getPos() {
		return pos;
	}
	public FloatBuffer getData() {
		return datafb;
	}
	public IntBuffer getIndices() {
		return indicesfb;
	}
	public BufferedImage getImg() {
		return img;
	}
	public void mouseupdate(boolean mouseover) {
		animation = mouseover;
	}
	public void setImg(BufferedImage img) {
		this.img = img;
	}
	public void setPos(Vector2f pos) {
		this.pos = pos;
	}
	public int getAnimationThreadID() {
		return animationid;
	}
	public int getTextureId() {
		return texid;
	}
	public String getType() {
		return "Marker";
	}
	public FloatBuffer getMatrix() {
		return matrixfb;
	}
}
