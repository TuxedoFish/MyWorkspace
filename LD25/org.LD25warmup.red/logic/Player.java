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
	
	public Runnable animation;
	public Thread mytimerthread;
	
	private int health = 600;
	private int mode;
	
	private int texid;
	private int tochangetexid;
	
	private int stage;
	private int jumpstages = 10;
	private int falling;
	
	private boolean visible = true;
	
	private TextureHolder texture;
	
	private float vely;
	
	private Controller parent;
	
	public Player(Controller c) throws IOException {
		this.parent = c;
		GridParser gp = new GridParser();
		ImageReturn images = new ImageReturn();
		
		animation = new PlayerAnimation(this, c);
		mytimerthread = new Thread(animation);
		mytimerthread.start();
		
		texture = gp.parseGrid(images.getImage("Hero.png"), 50);
		texid = 0;
		me = new Sprite(images.getImage("Hero.png"), c, 50, 50, texture, 0, new Vector2f(0.0f, 0.5f));
	}
	public void changeHealth(int change) {
		health += change;
		if(health <= 0) {
			System.out.println("win");
			parent.win();
			visible = false;
		}
	}
	public void respawn(int level) {
		visible = true;
		ArrayList<Integer> texid = new ArrayList<Integer>();
		texid.add(this.texid);
		
		me.setPos(0.0f, 0.5f, texid, 0);
		this.health = 600 + (int)((Math.pow(level, 3))*100);
	}
	public void updateAnimation() {
		if(tochangetexid != -1) {
			texid = tochangetexid;
			tochangetexid = -1;
		}
		if(texid == -1) {
			texid = 0;
		}
	}
	public Vector2f getPos() {
		return me.getPos(0);
	}
	public void render(ArrayList<Block> b) {
		if(visible) {
			me.render();
			this.testCollisions(b);
		}
	}
	public Sprite getSprite() {
		return me;
	}
	public void jump() {
		if(!jump && falling != -1 && visible) {
			jump = true;
			stage = 0;
			vely = 0;
		}
	}
	public void changePos(float x, float y) {
		if(visible) {
			ArrayList<Integer> texid = new ArrayList<Integer>();
			texid.add(this.texid);
			if(right == -1 && x > 0.0f) {
				still = false;
				me.changePos(x, 0.0f, texid, 0);
				if(this.texid == 2) {
					mode = 2;
				}
				if(this.texid > 2 || this.texid == 0) {
					tochangetexid = 1;
					mode = 1;
				} else if(mode == 1) {
					tochangetexid = this.texid + 1;
				} else {
					tochangetexid = this.texid - 1;
					if(tochangetexid < 0) {
						tochangetexid = 0;
						mode = 1;
					}
				}
			} else {
				jump();
			}
			if(left == -1 && x < 0.0f) {
				still = false;
				me.changePos(x, 0.0f, texid, 0);
				if(this.texid == 5) {
					mode = 2;
				}
				if(this.texid > 5 || this.texid < 3 || this.texid == 3) {
					tochangetexid = 4;
					mode = 1;
				} else if(mode == 1) {
					tochangetexid = this.texid + 1;
				} else {
					tochangetexid = this.texid - 1;
				}
			}
			if(x == 0.0f) {
				tochangetexid = 0;
			}
		}
	}
	public void testCollisions(ArrayList<Block> b) {
		BufferedImage tex = me.getTexture();
		
		Vector2f posa = me.getPos(0);
		Vector2f pos = new Vector2f();
		pos.x = (posa.x + 1.0f) * (float)(Display.getWidth()/2);
		pos.y = (posa.y + 1.0f) * (float)(Display.getHeight()/2);
		
		if(!still) {
			falling = isColliding(new Point((int)pos.x, (int)pos.y - me.getHeight()/2 - 1), new Point((int)pos.x + me.getWidth()/2, 
					(int)pos.y - me.getHeight()/2 - 1), b, 1);
		}
		
		right = isColliding(new Point((int)pos.x  + me.getWidth()/2, (int)pos.y - me.getHeight()/2 + 5), 
				new Point((int)pos.x + me.getWidth()/2, (int)pos.y), b, 2);
		
		if(right != -1 && b.get(right).getTexid() > 1) {
			System.out.println("lose");
			parent.lose();
			visible = false;
		}
		
		if(jump) {
			if(stage < jumpstages) {
				vely += 0.0025f;
				ArrayList<Integer> texid = new ArrayList<Integer>();
				texid.add(this.texid);
				me.changePos(0.0f, vely, texid, 0);
				stage += 1;
			} else {
				still = false;
				jump = false;
			}
		} else {
			if(falling == -1) {
				vely -= 0.0025f;
				ArrayList<Integer> texid = new ArrayList<Integer>();
				texid.add(this.texid);
				me.changePos(0.0f, vely, texid, 0);
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
							ArrayList<Integer> texid = new ArrayList<Integer>();
							texid.add(this.texid);
							me.setPos(me.getPos(0).x, b.get(i).getY() + ((float)me.getHeight()/(Display.getHeight())), texid, 0);
							still = true;
						}
						return i;
					}
				}
			}
			return -1;
	}
}
