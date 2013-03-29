package texture;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import object.Polygon;
import object.Shape;

import org.lwjgl.util.vector.Vector2f;

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

public class TextureAtlas {
	private BufferedImage atlas = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
	private ArrayList<BufferedImage> subimages = new ArrayList<BufferedImage>();
	private ArrayList<Vector2f> coordinates = new ArrayList<Vector2f>();
	private ArrayList<Vector2f> drawingcoords = new ArrayList<Vector2f>();
	private ArrayList<Vector2f> realtexcoods = new ArrayList<Vector2f>();
	
	private TextureGrid tg;
	
	private Graphics atlasgraphics = atlas.getGraphics();
	
	private int texID = glGenTextures();
	
	public TextureAtlas() {
		atlas = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
		tg = new TextureGrid(32, 32);
	}
	public int getTextureID() {
		return texID;
	}
	public void addTexture(BufferedImage img, Vector2f[] v) {
		subimages.add(img);
		
		coordinates.add(v[0]);
		coordinates.add(v[1]);
		coordinates.add(v[2]);
		
		Vector2f drawloc = new Vector2f();
		
		for(boolean done = false; !done;) {
			try {
				drawloc = tg.nearestSpace(img);
				done = true;
			} catch (Exception e) {
				if(e.getLocalizedMessage() == "enlarge") {
					atlas = new BufferedImage(atlas.getWidth()*2, atlas.getHeight()*2, BufferedImage.TYPE_INT_ARGB);
					tg.enlarge(atlas);
					for(int i = 0; i < drawingcoords.size();i++) {
						tg.useup(drawingcoords.get(i), subimages.get(i));
					}
				}
			}
		}
		
		drawingcoords.add(drawloc);
		
		atlasgraphics = atlas.getGraphics();
		
		atlasgraphics.setColor(new Color(0.0f, 0.0f, 0.0f, 0.0f));
		atlasgraphics.fillRect(0, 0, atlas.getWidth(), atlas.getHeight());
		
		for(int i = 0; i < drawingcoords.size();i++) {
			atlasgraphics.drawImage((Image)subimages.get(i), (int)drawingcoords.get(i).x, (int)drawingcoords.get(i).y, null);
		}
		
		realtexcoods.clear();
		
		for(int i = 0;i < subimages.size(); i++) {
			Vector2f vec = new Vector2f();
			
			vec = converttoimagesizes(coordinates.get(i*3), drawingcoords.get(i), subimages.get(i), atlas); 
			realtexcoods.add(vec);
			
			vec = converttoimagesizes(coordinates.get((i*3) + 1), drawingcoords.get(i), subimages.get(i), atlas); 
			realtexcoods.add(vec);
			
			vec = converttoimagesizes(coordinates.get((i*3) + 2), drawingcoords.get(i), subimages.get(i), atlas); 
			realtexcoods.add(vec);
		}
		
		tg.useup(drawloc, img);
		
		TextureUtils util = new TextureUtils();
		util.binddata(atlas, texID);
		

//		File outputfile = new File("saved @ " + this.hashCode() + ".png");
//	    try {
//			ImageIO.write(atlas, "png", outputfile);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
	public void updatetexcoods(Shape s) {
		ArrayList<Polygon> polys = s.getPolygons();
		
		for(int i = 0; i < polys.size();i++) {
			polys.get(i).setTexCood(1, realtexcoods.get(i*3));
			polys.get(i).setTexCood(2, realtexcoods.get((i*3) + 1));
			polys.get(i).setTexCood(3, realtexcoods.get((i*3) + 2));
		}
	}
	private Vector2f converttoimagesizes(Vector2f oldvec, Vector2f offset, BufferedImage oldimage, BufferedImage newimage) {
		Vector2f vec = new Vector2f();
		
		vec.x = oldvec.x * oldimage.getWidth();
		vec.x += offset.x;
		vec.x /= newimage.getWidth();
		
		vec.y = oldvec.y * oldimage.getHeight();
		vec.y += offset.y;
		vec.y /= newimage.getHeight();
		
		return vec;
	}
}
