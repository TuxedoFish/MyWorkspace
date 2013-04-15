package start.gui;

import images.ImageReturn;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
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

import utils.TextureUtils;

public class GuiString implements GuiElement{
	private BufferedImage img;
	private Vector2f pos;
	private IntBuffer indicesfb;
	private FloatBuffer datafb;
	private Rectangle2D bounds;
	private int texid;
	
	public GuiString(BufferedImage img, Rectangle2D r, Color c, Vector2f pos, float width, float height, Vector2f topleft) {
		this.pos = pos;
		this.img = img;
		TextureUtils util = new TextureUtils();
		texid = util.binddata(img);
		
		int[] indices = {
				0, 1, 2,
				2, 3, 0
		};
		indicesfb = BufferUtils.createIntBuffer(indices.length);
		indicesfb.put(indices);
		indicesfb.flip();
		
		float[] data = {
				pos.x, pos.y, -1.0f, 1.0f, 	0.0f, 0.0f,		0.0f, 0.0f, 0.0f, 1.0f,
				
				pos.x + (float)((r.getWidth()/Display.getWidth())*3), pos.y, -1.0f, 1.0f, 	 1.0f, 0.0f, 
				0.0f, 0.0f, 0.0f, 1.0f,
				
				pos.x + (float)((r.getWidth()/Display.getWidth())*3), pos.y - (float)((r.getHeight()/Display.getHeight())*3), -1.0f, 1.0f, 
				1.0f, 1.0f, 
				0.0f, 0.0f, 0.0f, 1.0f,
				
				pos.x, pos.y - (float)((r.getHeight()/Display.getHeight())*3), -1.0f, 1.0f, 	0.0f, 1.0f, 
				0.0f, 0.0f, 0.0f, 1.0f,
		};
		
		datafb = BufferUtils.createFloatBuffer(data.length);
		datafb.put(data);
		datafb.rewind();
		
		this.bounds = r;
	}
	public GuiString(String str, Color c, Vector2f pos, float width, float height, Vector2f topleft) {
		this.pos = pos;
		int[] indices = {
				0, 1, 2,
				2, 3, 0
		};
		indicesfb = BufferUtils.createIntBuffer(indices.length);
		indicesfb.put(indices);
		indicesfb.flip();
		
		img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();
		
		ImageReturn images = new ImageReturn();
		try {
			g.setFont(Font.createFont(Font.TRUETYPE_FONT, images.getFont("HARLOWSI.TTF")).deriveFont(10.0f));
		} catch (FontFormatException | IOException e) {
			System.err.println("err loading font");
			e.printStackTrace();
		}
		
		Rectangle2D r = g.getFontMetrics().getStringBounds(str, 0, str.length(), g);
		
		for(int i=0; i < str.length();i++) {
			if(!((Math.abs(pos.x-topleft.x)) + (g.getFontMetrics().getStringBounds(str, 0, i, g).getWidth()
					/(Display.getWidth()/2)) < width)) {
				if(i < 3) {
					if(i < 2) {
						str = "";
					} else {
						str = "...";
					}
				} else {
					str = str.substring(0, i-2) + "...";
				}
				i = str.length();
			}
		}
		
		img = new BufferedImage((int)r.getWidth(), (int)r.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		g = img.getGraphics();
		
		try {
			g.setFont(Font.createFont(Font.TRUETYPE_FONT, images.getFont("HARLOWSI.TTF")).deriveFont(10.0f));
		} catch (FontFormatException | IOException e) {
			System.err.println("err loading font");
			e.printStackTrace();
		}
		g.setColor(c);
		g.drawString(str, 0, (int)r.getHeight()/4*3);
		g.dispose();

		TextureUtils util = new TextureUtils();
		texid = util.binddata(img);
		
		float[] data = {
				pos.x, pos.y, -1.0f, 1.0f, 	0.0f, 0.0f,		0.0f, 0.0f, 0.0f, 1.0f,
				
				pos.x + (float)((r.getWidth()/Display.getWidth())*3), pos.y, -1.0f, 1.0f, 	 1.0f, 0.0f, 
				0.0f, 0.0f, 0.0f, 1.0f,
				
				pos.x + (float)((r.getWidth()/Display.getWidth())*3), pos.y - (float)((r.getHeight()/Display.getHeight())*3), -1.0f, 1.0f, 
				1.0f, 1.0f, 
				0.0f, 0.0f, 0.0f, 1.0f,
				
				pos.x, pos.y - (float)((r.getHeight()/Display.getHeight())*3), -1.0f, 1.0f, 	0.0f, 1.0f, 
				0.0f, 0.0f, 0.0f, 1.0f,
		};
		
		datafb = BufferUtils.createFloatBuffer(data.length);
		datafb.put(data);
		datafb.rewind();
		
		this.bounds = r;
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
	@Override
	public int getTextureId() {
		return texid;
	}
}
