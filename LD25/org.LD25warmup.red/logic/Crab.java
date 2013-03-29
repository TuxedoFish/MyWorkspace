package logic;

import images.ImageReturn;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import level.Block;
import level.GridParser;
import level.TextureHolder;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import start.Controller;
import start.DisplaySetup;
import tdobject.Sprite;

public class Crab implements Enemy{
private Sprite me;
	
	private TextureHolder texture;
	
	private int width, height;
	
	private boolean real = true;
	
	private Controller parent;
	
	private int ticks = 0;
	
	private ArrayList<Vector2f> pos = new ArrayList<Vector2f>();
	private ArrayList<Block> blocks = new ArrayList<Block>();
	
	private ArrayList<Integer> left = new ArrayList<Integer>(), right = new ArrayList<Integer>();
	private ArrayList<Boolean> still = new ArrayList<Boolean>();
	
	private ArrayList<Integer> mode = new ArrayList<Integer>();
	
	private ArrayList<Integer> texid = new ArrayList<Integer>();
	
	private ArrayList<Integer> falling = new ArrayList<Integer>();
	private ArrayList<Float> vely = new ArrayList<Float>();
	
	private ArrayList<Integer> stage = new ArrayList<Integer>();
	
	private DisplaySetup d;
	
	public Crab(Controller c, Vector2f pos, int width, int height, ArrayList<Block> blocks, DisplaySetup d) throws IOException {
		this.d = d;
		this.parent = c;
		
		left.add(0); right.add(0); still.add(false); texid.add(0); falling.add(0); vely.add(0.0f); mode.add(1);
		stage.add(0);
		this.blocks = blocks;
		GridParser gp = new GridParser();
		ImageReturn images = new ImageReturn();
		
		texture = gp.parseGrid(images.getImage("Sprites.png"), 20);
		
		this.width = width;
		this.height = height;
		this.pos.add(pos);
		
		while(!Display.isActive()) {}
		start();
		me = new Sprite(images.getImage("Sprites.png"), c, width, height, texture, texid, this.pos);
	}
	@Override
	public void move(float x, float y) {
		me.changePos(x, y, texid, -1);
	}
	@Override
	public void render() {
		me.render();
	}
	public Sprite getSprite() {
		return me;
	}
	@Override
	public void changeTexture() {
		
	}
	public synchronized void start() {
	    new Thread(new Runnable() { // the wrapper thread is unnecessary, unless it blocks on the Clip finishing, see comments
	      public void run() {
	       while(!parent.getStopped()) {
	    	try {
	        	 Thread.sleep(10);
	        	 update();
	        	 ticks -= 1;
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
	       }
	      }
	    }).start();
	 }
	@Override
	public void addEnemy(float x, float y, ArrayList<Block> blocks) {
		if(ticks <= 0) {
			Block b = new Block();
			Vector2f pos = new Vector2f(Math.round(x/(b.getWidth()/2))*(b.getWidth()/2), 
					Math.round(y/(b.getHeight()/2))*(b.getHeight()/2));
			Vector2f eyepos = new Vector2f(pos.x/(Display.getWidth()/2) - 1.0f, 
					pos.y/(Display.getHeight()/2) - 1.0f);
			
			boolean collision = false;
			for(int i = 0; i < blocks.size(); i++) {
				if(nearEnough(blocks.get(i).getX(), eyepos.x) && nearEnough(blocks.get(i).getY(), eyepos.y)) {
					collision = true;
				}
			}
			if(!collision) {
				me.addObject(eyepos.x, eyepos.y, 0);
				left.add(0); right.add(0); still.add(false); texid.add(0); falling.add(0); vely.add(0.0f); mode.add(1);
				stage.add(0);
				ticks = 15;
				this.blocks = blocks;
			}
		}
	}
	public boolean nearEnough(float a, float b) {
		if(Math.abs(a - b) < 0.021) {
			return true;
		} else {
			return false;
		}
	}
	@Override
	public void move(float x, float y, int index, int texid) {
		this.texid.set(index, texid);
		me.changePos(x, y, this.texid, index);
	}
	@Override
	public void setPos(float x, float y, int index, int texid) {
		if(!me.empty()) {
			this.texid.set(index, texid);
			me.setPos(x, y, this.texid, index);
		}
	}
	@Override
	public void setPos(float x, float y) {
		
	}
	@Override
	public void update() {
		if(real && !(me.empty())) {
			testCollisions();
		}
	}
	public void setReal(Boolean b) {
		real = b;
	}
	private void testCollisions() {
		for(int i = 1; i < pos.size(); i++) {
			Player enemy = parent.getEnemy();
			Rectangle r1 = new Rectangle((int)((pos.get(i).x + 1.0f)*(Display.getWidth()/2)), 
					(int)((pos.get(i).y + 1.0f)*(Display.getHeight()/2)) - 10, 20, 20);
			Rectangle r2 = new Rectangle((int)((enemy.getPos().x + 1.0f)*(Display.getWidth()/2)), 
					(int)((enemy.getPos().y + 1.0f)*(Display.getHeight()/2)) - 40, 40, 40);
			
			if(r1.intersects(r2)) {
				enemy.jump();
				enemy.changeHealth(-2);
			}
			if(me.empty()) {
				return;
			}
			
			Vector2f posa = pos.get(i);
			Vector2f pos = new Vector2f();
			pos.x = (posa.x + 1.0f) * (Display.getWidth()/2);
			pos.y = (posa.y + 1.0f) * (Display.getHeight()/2);
			
			Vector4f eyecoords = new Vector4f();
			Matrix4f.transform(d.getModelViewMatrixAsMatrix(), new Vector4f(posa.x, posa.y, 0.0f, 1.0f), eyecoords);
			
			if(eyecoords.x > -1.1f && eyecoords.x < 1.1f) {
				if(i > still.size()) {
					System.err.println("err");
					return;
				}
				if(i > still.size()) {
					return;
				}
				if(!still.get(i)) {
					falling.set(i, isColliding(new Point((int)pos.x, (int)pos.y - me.getHeight()/2 - 1), new Point((int)pos.x + me.getWidth()/2, 
							(int)pos.y - me.getHeight()/2 - 1), blocks, 1, i));
				}
				if(falling.get(i) == -1) {
					vely.set(i, vely.get(i)-0.01f);
					move(0.0f, vely.get(i), i, 0);
				} else {
					if(mode.get(i) == 1) {
						right.set(i, isColliding(new Point((int)pos.x  + me.getWidth()/2, (int)pos.y - me.getHeight()/2 + 5), 
								new Point((int)pos.x + me.getWidth()/2, (int)pos.y), blocks, 2, i));
						if(right.get(i) == -1) {
							int tochange = 0;
							if(texid.get(i) > 3) {
								tochange = 1;
								stage.set(i, 1);
							} else {
								if(stage.get(i) == 1) {
									tochange = texid.get(i) + 1;
									if(tochange == 3) {
										stage.set(i, 2);
									}
								} else {
									tochange = texid.get(i) - 1;
									if(tochange <= 1) {
										tochange = 1;
										stage.set(i, 1);
									}
								}
							}
							move(0.01f, 0.0f, i, tochange);
							still.set(i, false);
						} else {
							mode.set(i, 2);
						}
					} else {
						left.set(i, isColliding(new Point((int)pos.x, (int)pos.y - me.getHeight()/2 + 5), 
								new Point((int)pos.x, (int)pos.y), blocks, 2, i));
						if(left.get(i) == -1) {
							int tochange = 0;
							if(texid.get(i) > 6 || texid.get(i) < 3) {
								tochange = 4;
								stage.set(i, 1);
							} else {
								if(stage.get(i) == 1) {
									tochange = texid.get(i) + 1;
									if(tochange == 6) {
										stage.set(i, 2);
									}
								} else {
									tochange = texid.get(i) - 1;
									if(tochange <= 3) {
										tochange = 3;
										stage.set(i, 1);
									}
								}
							}
							move(-0.01f, 0.0f, i, tochange);
							still.set(i, false);
						} else {
							mode.set(i, 1);
						}
					}
				}
			}
		}
	}
	public int isColliding(Point p1, Point p2, ArrayList<Block> b, int type, int index) {
		int temp = -1;
		
		for(int i = 0; i < b.size(); i++) {
			Vector2f bpos = new Vector2f((int)((b.get(i).getX() + 1.0f) * (Display.getWidth()/2)), 
					(int)((b.get(i).getY() + 1.0f) * (Display.getHeight()/2)));
		
			if(bpos.x - p1.x < 50) {
				Rectangle r1 = new Rectangle((int)((b.get(i).getX() + 1.0f) * (Display.getWidth()/2)), 
						(int)((b.get(i).getY() + 1.0f) * (Display.getHeight()/2)) - b.get(i).getHeight()/2, 
						b.get(i).getWidth()/2, b.get(i).getHeight()/2);
					if(r1.intersectsLine(new Line2D.Double(p1, p2))) {
						if(index > falling.size()) {
							return -1;
						}
						if(falling.get(index) == -1 && !still.get(index) && type == 1) {
							me.setPos(pos.get(index).x, b.get(i).getY() + ((float)me.getHeight()/(Display.getHeight())), texid, index);
							still.set(index, true);
							
							boolean done = false;
							while(!done) {
								boolean collision = false;
								for(int k = 0; k < blocks.size(); k++) {
									if(nearEnough(blocks.get(k).getX(), pos.get(index).x) && nearEnough(blocks.get(k).getY(), pos.get(index).y)) {
										collision = true;
									}
								}
								if(collision) {
									me.setPos(pos.get(index).x, pos.get(index).y + (float)2/48, texid, index);
								} else {
									done = true;
								}
							}
						}
						return i;
					}
				}
			}
			return -1;
	}
	@Override
	public void deleteAll() {
		me.deleteAll(); falling.clear(); left.clear();
		right.clear(); mode.clear(); pos.clear(); stage.clear(); still.clear(); texid.clear(); vely.clear();
	}
	@Override
	public boolean empty() {
		return me.empty();
	}
}
