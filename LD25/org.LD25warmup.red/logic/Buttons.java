package logic;

import images.ImageReturn;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.text.MaskFormatter;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import level.Block;
import level.GridParser;
import level.TextureHolder;

import sounds.SoundPlayer;
import start.Controller;
import start.DisplaySetup;
import tdobject.Sprite;

public class Buttons {
	private Sprite me;
	
	private TextureHolder texture;
	
	private ArrayList<Integer> texids = new ArrayList<Integer>();
	private ArrayList<Vector2f> pos = new ArrayList<Vector2f>();
	
	private ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	
	private int enemyselected = 0;
	
	private Controller parent;
	
	public Buttons(Controller c, ArrayList<Block> blocks, DisplaySetup d) throws IOException {
		GridParser gp = new GridParser();
		this.parent = c;
		ImageReturn images = new ImageReturn();
		
		texture = gp.parseGrid(images.getImage("Buttons.png"), 50);
		texids.add(0); texids.add(3); texids.add(3); texids.add(3); texids.add(3); texids.add(3);
		
		pos.add(new Vector2f(0.81f, 1.0f)); pos.add(new Vector2f(0.81f, 1.01f - (128.0f/Display.getHeight()))); 
		pos.add(new Vector2f(0.81f, 1.02f - ((128.0f/Display.getHeight())*2))); 
		pos.add(new Vector2f(0.81f, 1.03f - ((128.0f/Display.getHeight())*3)));
		pos.add(new Vector2f(0.81f, 1.04f - ((128.0f/Display.getHeight())*4)));
		pos.add(new Vector2f(0.81f, 1.05f - ((128.0f/Display.getHeight())*5)));
		
		Crab massivecrabb = new Crab(c, new Vector2f(0.83f, 1.0f), 100, 100, blocks, d);
		massivecrabb.setReal(false);
		Snake massiveundeadsnake = new Snake(c, new Vector2f(0.83f, 1.01f - (128.0f/Display.getHeight())), 100, 100, blocks, d);
		massiveundeadsnake.setReal(false);
		
		enemies.add(massivecrabb);
		enemies.add(massiveundeadsnake);
		
		me = new Sprite(images.getImage("Buttons.png"), c, 128, 128, texture, texids, pos);
	}
	public Vector2f getPos() {
		return me.getPos(0);
	}
	public void move(float x, float y) {
		me.changePos(x, y, texids, -1);
		enemies.get(0).move(x, y, 0, 0);
		enemies.get(1).move(x, y, 0, 8);
	}
	public void levelUpdate() {
		for(int i = 0; i < texids.size(); i++) {
			if(parent.getLevel()-1 < i) {
				texids.set(i, 3);
			} else {
				texids.set(i, 0);
			}
		}
	}
	public boolean mouseUpdate(String type, Vector2f pos, DisplaySetup d) {
		int mouseat = isTouching(pos, d);
		for(int i = 0; i < texids.size(); i++) {
			if(parent.getLevel()-1 < i) {
				texids.set(i, 3);
			} else {
				texids.set(i, 0);
			}
		}
		if(type.equals("Down")) {
			if(mouseat != -1 && !(parent.getLevel()-1 < mouseat)) {
				texids.set(mouseat, 1);
				me.changePos(0.0f, 0.0f, texids, mouseat);
			}
		}
		if(type.equals("Clicked")) {
			if(mouseat != -1  && !(parent.getLevel()-1 < mouseat)) {
				enemyselected = mouseat;
				texids.set(mouseat, 2);
				me.changePos(0.0f, 0.0f, texids, mouseat);
			}
		}
		if(mouseat == -1) {
			return false;
		} else {
			return true;
		}
	}
	public int getEnemySelected() {
		return enemyselected;
	}
	private int isTouching(Vector2f mousepos, DisplaySetup d) {
		Point mousepospoint = new Point((int)mousepos.x, (int)mousepos.y);
		Vector4f eyecoord = new Vector4f();
		
		for(int k = 0; k < this.pos.size(); k++) {
			Matrix4f.transform(d.getModelViewMatrixAsMatrix(), new Vector4f(pos.get(k).x, pos.get(k).y, 0.0f, 1.0f), eyecoord);
			Rectangle r = new Rectangle((int)((eyecoord.x + 1.0f) * (Display.getWidth()/2)), 
					(int)((eyecoord.y + 1.0f) * (Display.getHeight()/2)) - 64, 128, 128);
			if(r.contains(mousepospoint)) {
				return k;
			}
		}
		return -1;
	}
	public void render() {
		me.render();
		for(int i = 0; i < enemies.size(); i++) {
			enemies.get(i).render();
		}
	}
	public Sprite getSprite() {
		return me;
	}
}
