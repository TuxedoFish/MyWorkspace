package texture;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.print.attribute.standard.MediaSize.Other;

import object.Shape;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import utils.MathUtils;
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

public class TextureHandler {

	private TextureAtlas ta = new TextureAtlas();
	
	private Shape parent;
	
	public TextureHandler (Shape parent) {
		this.parent = parent;
	}
	public Vector2f[] addTriangleTexture(Vector4f pointa, Vector4f pointb, Vector4f pointc, int res, Color c) {
		MathUtils maths = new MathUtils();
		TextureUtils util = new TextureUtils();
		
		Vector4f pointaclo = maths.scaleVector(pointa, res);
		Vector4f pointbclo = maths.scaleVector(pointb, res);
		Vector4f pointcclo = maths.scaleVector(pointc, res);
		
		Vector2f pointa2D = new Vector2f(0,0);
		Vector2f pointb2D = new Vector2f(pointa2D.x + maths.distancebetween(pointaclo, pointbclo), pointa2D.y);
		
		float atocdist = maths.distancebetween(pointaclo, pointcclo);
		float btocdist = maths.distancebetween(pointbclo, pointcclo);
		
		ArrayList<Vector2f> atoc = new ArrayList<Vector2f>();
		ArrayList<Vector2f> btoc = new ArrayList<Vector2f>();
		
		maths.calculatecirclepoints(pointa2D, atocdist, atoc);
		maths.calculatecirclepoints(pointb2D, btocdist, btoc);
		
		boolean foundsolution = false;
		Vector2f pointc2D = new Vector2f();
		
		for(int i = 0; i < atoc.size(); i++) {
			for(int j = 0; j < btoc.size(); j++) {
				if(equal(atoc.get(i), btoc.get(j))) {
					if(atoc.get(i).x > pointc2D.x || atoc.get(i).y > pointc2D.y) {
						foundsolution = true;
						pointc2D = new Vector2f(atoc.get(i).x, btoc.get(j).y);
					}
				}
			}
		}
		if(!foundsolution) {
			System.err.println("error creating triangle texture when making first points");
			System.out.println(pointa + " : " + pointb + " : " + pointc);
			System.exit(1);
			return null;
		}
		Vector2f lowesty = pointa2D;
		Vector2f highesty = pointa2D;
		
		ArrayList<Vector2f> rankedy = new ArrayList<Vector2f>();
		ArrayList<Vector2f> allvecs = new ArrayList<Vector2f>();
		
		allvecs.add(pointa2D);
		allvecs.add(pointb2D);
		allvecs.add(pointc2D);
		
		maths.rankony(allvecs, rankedy);
		
		highesty = rankedy.get(0);
		
		if(rankedy.get(2) == pointb2D) {
			lowesty = rankedy.get(1);
		} else {
			lowesty = rankedy.get(2);
		}
		
		BufferedImage triangle2D = createImage(pointa2D, pointb2D, pointc2D, (int)(highesty.x-lowesty.y), 
				(int)(highesty.y-lowesty.y), c);
		
		correct(pointa2D, triangle2D);
		correct(pointb2D, triangle2D);
		correct(pointc2D, triangle2D);
		
		Vector2f[] vectors = {
				pointa2D,
				pointb2D,
				pointc2D
		};
		
		ta.addTexture(triangle2D, vectors);
		return vectors;
	}
	private BufferedImage createImage(Vector2f p1, Vector2f p2, Vector2f p3, int width, int height, Color c) {
		BufferedImage img = new BufferedImage((int)(width), (int)(height), BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();
		
		g.setColor(c);
		g.fillPolygon(new Polygon(new int[]{(int)(p1.x), (int)(p2.x), (int)(p3.x)}, 
				new int[]{(int)(p1.y), (int)(p2.y), (int)(p3.y)}, 3));
		g.drawLine(Math.round(p1.x), Math.round(p1.y), Math.round(p2.x), Math.round(p2.y));
		g.drawLine(Math.round(p3.x), Math.round(p3.y), Math.round(p2.x), Math.round(p2.y));
		g.drawLine(Math.round(p1.x), Math.round(p1.y), Math.round(p3.x), Math.round(p3.y));
		
		return img;
	}
	private boolean equal(Vector2f a, Vector2f b) {
		if((a.x < b.x + 0.1 || a.x > b.x - 0.1)
				&& (a.y < b.y + 0.1 || a.y > b.y - 0.1)) {
			return true;
		} else {
			return false;
		}
	}
	private void correct(Vector2f v, BufferedImage img) {
		if(v.x != 0) {
			v.x = (v.x/(img.getWidth()));
		}
		if(v.y != 0) {
			v.y = (v.y/(img.getHeight()));
		}
		if(v.x > 1) {
			v.x = 1;
		}
		if(v.y > 1) {
			v.y = 1;
		}
	}
	public Shape getShape() {
		return parent;
	}
	public TextureAtlas getTextureAtlas() {
		return ta;
	}
}
