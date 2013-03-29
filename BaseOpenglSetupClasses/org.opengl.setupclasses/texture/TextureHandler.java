package texture;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
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
					foundsolution = true;
					pointc2D = new Vector2f(atoc.get(i).x, atoc.get(i).y);
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
		
		ArrayList<Vector2f> points = new ArrayList<Vector2f>();
		
		for(float y = lowesty.y; y <= highesty.y; y++) {
			float decimalheightup = (y/highesty.y);
			float xdif = Math.abs(highesty.x - lowesty.x);
			
			Vector2f otherpoint = new Vector2f();
			
			if(!(pointa2D == lowesty || pointa2D == highesty)) {
				otherpoint = pointa2D;
			}
			if(!(pointb2D == lowesty || pointb2D == highesty)) {
				otherpoint = pointb2D;
			}
			if(!(pointc2D == lowesty || pointc2D == highesty)) {
				otherpoint = pointc2D;
			}
			
			float xdif2 = Math.abs(otherpoint.x - highesty.x);
			
			if((xdif * decimalheightup) > otherpoint.x + (xdif2 * decimalheightup)) {
				for(float x = (xdif * decimalheightup); x >= otherpoint.x + (xdif2 * decimalheightup); x--) {
					points.add(new Vector2f(x, y));
				}
			}else {
				for(float x = (xdif * decimalheightup); x <= otherpoint.x - (xdif2 * decimalheightup); x++) {
					points.add(new Vector2f(x, y));
				}
			}
		}
		
		BufferedImage triangle2D = util.createimage(points, c);
		
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
			v.x = v.x/img.getWidth();
		}
		if(v.y != 0) {
			v.y = v.y/img.getHeight();
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
