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

public class GuiBar implements GuiElement{
	private BufferedImage img;
	private Vector2f pos;
	private IntBuffer indicesfb;
	private FloatBuffer datafb;
	private Rectangle2D bounds;
	private BufferedImage emptybar;
	private BufferedImage bar;
	private int texid;
	
	public GuiBar(String barname, Vector2f pos, int percent) {
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
			bar = images.getImage("gui/" + barname + ".png");
			emptybar = images.getImage("gui/empty" + barname + ".png");
			img = new BufferedImage(emptybar.getWidth(), emptybar.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
			Graphics g = img.getGraphics();
			
			g.drawImage(emptybar, 0, 0, null);
			
			if(percent < 100) {
				g.drawImage(bar.getSubimage(0, 0, (int)(((float)bar.getWidth()/100.0f)*(float)percent), 
						bar.getHeight()), 1, 1, null);
			} else {
				g.drawImage(bar.getSubimage(0, 0, (int)(((float)bar.getWidth()/100.0f)*(float)100), 
						bar.getHeight()), 1, 1, null);
			}
			g.dispose();
		} catch (IOException e) {
			System.err.println("err finding button @ " + barname);
			e.printStackTrace();
		}
		
		TextureUtils util = new TextureUtils();
		texid = util.binddata(img);
		
		float[] data = {
				pos.x, pos.y, 0.0f, 1.0f, 	0.0f, 0.0f,		0.0f, 0.0f, 0.0f, 1.0f,
				
				pos.x + (float)(((float)emptybar.getWidth()/Display.getWidth())*2), pos.y, 0.0f, 1.0f, 	 1.0f, 0.0f, 
				0.0f, 0.0f, 0.0f, 1.0f,
				
				pos.x + (float)(((float)emptybar.getWidth()/Display.getWidth())*2), pos.y - (float)(((float)emptybar.getHeight()/Display.getHeight())*2), 0.0f, 1.0f, 
				1.0f, 1.0f, 
				0.0f, 0.0f, 0.0f, 1.0f,
				
				pos.x, pos.y - (float)(((float)emptybar.getHeight()/Display.getHeight())*2), 0.0f, 1.0f, 0.0f, 1.0f, 
				0.0f, 0.0f, 0.0f, 1.0f,
		};
		
		datafb = BufferUtils.createFloatBuffer(data.length);
		datafb.put(data);
		datafb.rewind();
		
		this.bounds = new Rectangle2D.Float((pos.x+1.0f)*(Display.getWidth()/2), (pos.y+1.0f)*(Display.getHeight()/2), emptybar.getWidth(), emptybar.getHeight());
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
	public void setImg(BufferedImage img) {
		this.img = img;
	}
	public void setPos(Vector2f pos) {
		this.pos = pos;
	}
	public int getTextureId() {
		return texid;
	}
	public String getType() {
		return "Bar";
	}
	public FloatBuffer getMatrix() {
		return null;
	}
}
