package utils;

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
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector2f;

public class TextureUtils {
	public int binddata(BufferedImage img) {
		int textureID = glGenTextures();
		binddata(img, textureID, loadtexture(img));
        return textureID;
	}
	public int binddata(BufferedImage img, ByteBuffer imgdata) {
		int textureID = glGenTextures();
		binddata(img, textureID, imgdata);
        return textureID;
	}
	public void binddata(BufferedImage img, int textureID) {
		binddata(img, textureID, loadtexture(img));
	}
	public void binddata(BufferedImage img, int textureID, ByteBuffer imgdata) {
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, textureID); //Bind texture ID
		
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        //Setup wrap mode
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        //Setup texture scaling filtering
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        
        //Send texel data to OpenGL
        glTexImage2D(GL_TEXTURE_2D, 0, 4, img.getWidth(), img.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, imgdata);
	}
	public ByteBuffer loadtexture(BufferedImage texture) {
		 int[] pixels = new int[texture.getWidth() * texture.getHeight()];
	     texture.getRGB(0, 0, texture.getWidth(), texture.getHeight(), pixels, 0, texture.getWidth());

	     ByteBuffer buffer = BufferUtils.createByteBuffer(texture.getWidth() * texture.getHeight() * 4); 
	        
	     for(int y = 0; y < texture.getHeight(); y++){
	         for(int x = 0; x < texture.getWidth(); x++){
	             int pixel = pixels[y * texture.getWidth() + x];
		         buffer.put((byte) ((pixel >> 16) & 0xFF));    
	             buffer.put((byte) ((pixel >> 8) & 0xFF));   
	             buffer.put((byte) (pixel & 0xFF));      
	             buffer.put((byte) ((pixel >> 24) & 0xFF));
	         }
	     }
	     buffer.flip(); 
	     return buffer;
	}
	public IntBuffer loadTextureInt(BufferedImage texture) {
		 int[] pixels = new int[texture.getWidth() * texture.getHeight()];
	     texture.getRGB(0, 0, texture.getWidth(), texture.getHeight(), pixels, 0, texture.getWidth());

	     IntBuffer buffer = BufferUtils.createIntBuffer(texture.getWidth() * texture.getHeight() * 4); 
	        
	     for(int y = 0; y < texture.getHeight(); y++){
	         for(int x = 0; x < texture.getWidth(); x++){
	             int pixel = pixels[y * texture.getWidth() + x];
		         buffer.put((int) ((pixel >> 16) & 0xFF));    
	             buffer.put((int) ((pixel >> 8) & 0xFF));   
	             buffer.put((int) (pixel & 0xFF));      
	             buffer.put((int) ((pixel >> 24) & 0xFF));
	         }
	     }
	     buffer.flip(); 
	     return buffer;
	}
	public BufferedImage createimage(ArrayList<Vector2f> points, Color c) {
		int lowestx = (int) points.get(0).x;
		int highestx = (int) points.get(0).x;
		int lowesty = (int) points.get(0).y;
		int highesty = (int) points.get(0).y;
		
		for(int i = 1; i < points.size(); i++) {
			if(points.get(i).x < lowestx) {
				lowestx = (int) points.get(i).x;
			}
			if(points.get(i).x > highestx) {
				highestx = (int) points.get(i).x;
			}
			if(points.get(i).y < lowesty) {
				lowesty = (int) points.get(i).y;
			}
			if(points.get(i).y > highesty) {
				highesty = (int) points.get(i).y;
			}
		}
		
		int w = Math.abs(lowestx - highestx);
		int h = Math.abs(lowesty - highesty);
		
		BufferedImage b = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics g = b.getGraphics();
		
		for(int i = 0; i < points.size(); i++) {
			g.setColor(c);
			g.drawRect((int) points.get(i).x,(int) points.get(i).y, 1, 1);
		}
		
		g.dispose();
		return b;
	}
}
