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

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

import start.Controller;
import utils.TextureUtils;

public class GuiButton implements GuiElement{
	private BufferedImage[] imgs;
	private Vector2f pos;
	private IntBuffer indicesfb;
	private FloatBuffer datafb;
	private Rectangle2D bounds;
	private int current = 0;
	private Controller parent;
	private String message;
	private int[] texids;
	
	public GuiButton(String buttonname, Vector2f pos, float width, float height, Controller parent, 
			String eventmessage) {
		this.parent = parent;
		this.message = eventmessage;
		this.pos = pos;
		int[] indices = {
				0, 1, 2,
				2, 3, 0
		};
		indicesfb = BufferUtils.createIntBuffer(indices.length);
		indicesfb.put(indices);
		indicesfb.flip();
		
		ImageReturn images = new ImageReturn();
		
		try {
			imgs = new BufferedImage[] {
					images.getImage("gui/" + buttonname+".png"), 
					images.getImage("gui/" + buttonname+"hover.png"), 
					images.getImage("gui/" + buttonname+"pressed.png"), 
			};
			TextureUtils util = new TextureUtils();
			texids = new int[] {
					util.binddata(imgs[0]),
					util.binddata(imgs[1]),
					util.binddata(imgs[2])
			};
		} catch (IOException e) {
			System.err.println("err finding button @ " + buttonname);
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
		
		this.bounds = new Rectangle2D.Float((pos.x+1.0f)*(Display.getWidth()/2), (pos.y+1.0f)*(Display.getHeight()/2), width, height);
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
		return imgs[current];
	}
	public void setState(int state) {
		if(state == 2) {
			parent.action(message);
		}
		this.current = state;
	}
	public void setImg(BufferedImage img) {
		this.imgs = imgs;
	}
	public void setPos(Vector2f pos) {
		this.pos = pos;
	}
	@Override
	public int getTextureId() {
		return texids[current];
	}
}
