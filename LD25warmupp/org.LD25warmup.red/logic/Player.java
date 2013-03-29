package logic;

import images.ImageReturn;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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

public class Player {
	private Sprite me;
	private int left, right;
	private boolean jump = false, still = false;
	
	public Runnable animation = new PlayerAnimation(this);
	public Thread mytimerthread = new Thread(animation);
	
	private int mode;
	
	private int texid;
	private int tochangetexid;
	
	private SoundPlayer sp = new SoundPlayer();
	
	private int stage;
	private int jumpstages = 10;
	private int falling;
	
	private TextureHolder texture;
	
	private float vely;
	
	public Player(Controller c) throws IOException {
		GridParser gp = new GridParser();
		ImageReturn images = new ImageReturn();
		
		texture = gp.parseGrid(images.getImage("CHARACTER.png"), 20, 40);
		texid = 0;
		me = new Sprite(images.getImage("CHARACTER.png"), c, 20, 40, texture, 0);
	}
	public void updateAnimation() {
		if(tochangetexid != -1) {
			texid = tochangetexid;
			tochangetexid = -1;
		}
	}
	public void render(ArrayList<Block> b) {
		me.render();
		this.testCollisions(b);
	}
	public Sprite getSprite() {
		return me;
	}
	public void jump() {
		if(!jump && falling != -1) {
			jump = true;
			stage = 0;
			vely = 0;
			sp.playSound("jump.wav");
		}
	}
	public void testCollisions(ArrayList<Block> b) {
		BufferedImage tex = me.getTexture();
		
		Vector2f posa = me.getPos();
		Vector2f pos = new Vector2f();
		pos.x = (posa.x + 1.0f) * (float)(Display.getWidth()/2);
		pos.y = (posa.y + 1.0f) * (float)(Display.getHeight()/2);
		
		if(!still) {
			falling = isColliding(new Point((int)pos.x, (int)pos.y - me.getHeight()/2 - 1), new Point((int)pos.x + me.getWidth()/2, 
					(int)pos.y - me.getHeight()/2 - 1), b, 1);
		}
		
		right = isColliding(new Point((int)pos.x  + me.getWidth()/2, (int)pos.y - me.getHeight()/2 + 5), 
				new Point((int)pos.x + me.getWidth()/2, (int)pos.y), b, 2);
				
		left = isColliding(new Point((int)pos.x, (int)pos.y - me.getHeight()/2 + 5), 
				new Point((int)pos.x, (int)pos.y), b, 2);
		
		if(jump) {
			if(stage < jumpstages) {
				vely += 0.0025f;
				me.changePos(0.0f, vely, texid);
				stage += 1;
			} else {
				still = false;
				jump = false;
			}
		} else {
			if(falling == -1) {
				vely -= 0.0025f;
				me.changePos(0.0f, vely, texid);
				still = false;
			} else {
				vely = 0.0f;
			}
		}
	}
	public int isColliding(Point p1, Point p2, ArrayList<Block> b, int type) {
		int temp = -1;
		
		for(int i = 0; i < b.size(); i++) {
			Vector2f bpos = new Vector2f((int)((b.get(i).getX() + 1.0f) * (Display.getWidth()/2)), 
					(int)((b.get(i).getY() + 1.0f) * (Display.getHeight()/2)));
		
			if(Math.sqrt(Math.pow(bpos.x - p1.x, 2) + Math.pow(bpos.y - p1.y, 2)) <= 50 ||
					Math.sqrt(Math.pow(bpos.x - p2.x, 2) + Math.pow(bpos.y - p2.y, 2)) <= 50) {
				
				Rectangle r1 = new Rectangle((int)((b.get(i).getX() + 1.0f) * (Display.getWidth()/2)), 
						(int)((b.get(i).getY() + 1.0f) * (Display.getHeight()/2)) - b.get(i).getHeight()/2, 
						b.get(i).getWidth()/2, b.get(i).getHeight()/2);
					if(r1.intersectsLine(new Line2D.Double(p1, p2))) {
						if(falling == -1 && !still && type == 1) {
							me.setPos(me.getPos().x, b.get(i).getY() + ((float)me.getHeight()/(Display.getHeight())));
							still = true;
						}
						return i;
					}
				}
			}
			return -1;
	}
	public void changePos(float x, float y, DisplaySetup d) {
		if(right == -1 && x > 0.0f) {
			Vector4f s = new Vector4f();
			Matrix4f.transform(d.getModelViewMatrixAsMatrix(), new Vector4f(me.getPos().x, me.getPos().y, 0.0f, 1.0f), s);
			
			still = false;
			me.changePos(x, 0.0f, texid);
			if(s.x >= 0.0f) {
				d.changepos(x*-1, 0.0f, 0.0f);
			}
			if(texid == 2) {
				mode = 2;
			}
			if(texid > 2 || texid == 0) {
				tochangetexid = 1;
				mode = 1;
			} else if(mode == 1) {
				tochangetexid = texid + 1;
			} else {
				tochangetexid = texid - 1;
			}
		}
		if(left == -1 && x < 0.0f) {
			if(d.getPos().x >= 0.0f) {
				d.setPos(0.0f, d.getPos().y, d.getPos().z);
			} else {
				d.changepos(x*-1, 0.0f, 0.0f);
			}
			still = false;
			me.changePos(x, 0.0f, texid);
			if(texid == 5) {
				mode = 2;
			}
			if(texid > 5 || texid < 3 || texid == 3) {
				tochangetexid = 4;
				mode = 1;
			} else if(mode == 1) {
				tochangetexid = texid + 1;
			} else {
				tochangetexid = texid - 1;
			}
		}
		if(x == 0.0f) {
			tochangetexid = 0;
		}
		
	}
}
