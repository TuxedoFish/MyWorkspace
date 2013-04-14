package logic.entities;

import images.ImageReturn;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.ArrayList;

import logic.GridParser;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import object.ObjectLoader;
import object.Shape;
import object.Sprite;
import physics.OctreeCollision;
import physics.Rectangle3D;
import shader.ShaderHandler;
import sounds.SoundHandler;
import start.Controller;
import start.DisplaySetup;
import texture.TextureHolder;
import utils.DataUtils;

public class Enemy extends Sprite{
	private Vector2f pos;
	private int texid;
	private PathPoint p;
	private PathPoint lastp;
	private EnemyPath ep;
	private boolean started = false;
	private int stage = 0;
	private int textstage = 0;
	private int dist;
	private int index;
	private int pattern = 2;
	private boolean stopped = false;
	private boolean scroll = true;
	private BulletHandler bullets;
	private Rectangle2D player;
	private Rectangle2D myrect;
	private Player playersprite;
	private Controller parent;
	
	private float direction = -1;
	private float togo = -1;
	private boolean onscreen = false;
	
	private int animationstyle = 1;
	
	private ArrayList<Bullet> playerbullets = new ArrayList<Bullet>();
	private ArrayList<Gun> guns = new ArrayList<Gun>();
	private int lti;
	private int hti;
	private boolean isup = true;
	private float width;
	private TextureHolder[] textures;
	private int texturestage;
	private int speed;
	private int hit;
	private int health;
	private int threadindex;
	private int shootspeed;
	private int shootthreadindex;
	
	public Enemy(Vector2f pos, int texid, Controller parent, EnemyPath ep, Player player, 
			ArrayList<Bullet> playerbullets, BufferedImage tex, int lowesttexid, int highesttexid
			, int width, int pattern, TextureHolder[] ts, int health, int shootspeed) {
		super(tex, parent, 100, 100, ts[2], 0, new Vector2f(((pos.x/Display.getWidth())), 
				((pos.y)/(Display.getHeight()/2.0f))));
		setup(pos, texid, parent, ep, player, playerbullets, tex, lowesttexid, highesttexid, width, pattern, ts, health, shootspeed);
	}
	public Enemy(Vector2f pos, int texid, Controller parent, EnemyPath ep, Player player, 
			ArrayList<Bullet> playerbullets, BufferedImage tex, int lowesttexid, int highesttexid
			, int width, int pattern, GridParser gp, int health, int shootspeed) {
		super(tex, parent, 100, 100, gp.parseGrid(tex, 49.0f), 0, new Vector2f(((pos.x/Display.getWidth())), 
				((pos.y)/(Display.getHeight()/2.0f))));
		setup(pos, texid, parent, ep, player, playerbullets, tex, lowesttexid, highesttexid, width, pattern, null, health, shootspeed);
	}
	public void setScroll(boolean b) {
		scroll = b;
	}
	public void setup(Vector2f pos, int texid, Controller parent, EnemyPath ep, Player player, 
			ArrayList<Bullet> playerbullets, BufferedImage tex, int lowesttexid, int highesttexid
			, int width, int pattern, TextureHolder[] ts, int health, int shootspeed) {
		this.width = (float)width/Display.getWidth();
		this.shootspeed = shootspeed;
		this.playerbullets = playerbullets;
		this.health = health;
		this.parent = parent;
		this.pattern = pattern;
		this.lti = lowesttexid;
		this.hti = highesttexid;
		this.speed = 3;
		this.pos = new Vector2f(((pos.x/Display.getWidth())), 
				((pos.y)/(Display.getHeight()/2.0f)));
		this.playersprite = player;
		this.ep = ep;
		this.texid = texid;
		this.player = new Rectangle2D.Float();
		this.myrect = new Rectangle2D.Float();
		this.hit = 0;
		
		ImageReturn images = new ImageReturn();
		GridParser gp = new GridParser();
		TextureHolder texture; TextureHolder texture2; TextureHolder texture3;
		
		try {
			if(ts != null) {
				textures = ts;
				bullets = new BulletHandler(ts[1], ts[0], "bullets2", parent, player);
			} else {
				texture = gp.parseGrid(images.getImage("explosion.png"), 29.0f);
				texture2 = gp.parseGrid(images.getImage("bullets2.png"), 19.0f);
				texture3 = gp.parseGrid(tex, 49.0f);
				
				bullets = new BulletHandler(texture2, texture, "bullets2", parent, player);
				textures = new TextureHolder[]{
						texture, texture2, texture3
				};
			}
		} catch (IOException e) {
			System.err.println("err at enemy");
			System.exit(1);
			e.printStackTrace();
		}
	}
	public void setAnimationSpeed(int speed) {
		this.speed = speed;
	}
	public void finish(IntBuffer vboids, IntBuffer vaoids, int[] index) {
		bullets.finish(vboids, vaoids);
		this.finish(vboids.get(2), vaoids.get(2));
		this.threadindex = index[0];
		this.shootthreadindex = index[1];
		for(int i=0; i<guns.size(); i++) {
			guns.get(i).finish(vboids.get(2+i), vaoids.get(2+i));
		}
	}
	public BulletHandler getBulletHandler() {
		return bullets;
	}
	public void addGun(Gun g, boolean visible) {
		guns.add(g);
		guns.get(guns.size()-1).setVisible(visible);
	}
	public int getAmountOfGuns() {
		return guns.size();
	}
	public int getShootThreadID() {
			return shootthreadindex;
	}
	public int getShootSpeed() {
		return shootspeed;
	}
	public TextureHolder[] getTextures() {
		return textures;
	}
	public void animate() {
		if(animationstyle < 3) {
			if(texturestage>speed) {
				if(isup) {
					if(texid<hti) {
						this.changeTexture(texid+1);
						texid += 1;
					} else {
						isup = false;
					}
				} else {
					if(texid>lti) {
						if(animationstyle == 1) {
							this.changeTexture(texid-1);
							texid -= 1;
						}
						if(animationstyle == 2) {
							this.changeTexture(0);
							texid = 0;
						}
					} else {
						isup = true;
					}
				}
				texturestage = 0;
			} else {
				texturestage += 1;
			}
		}
	}
	public void setAnimationStyle(int style) {
		this.animationstyle = style;
	}
	public void updateColl(DisplaySetup d) {
		Vector4f p = new Vector4f((float)playersprite.getPos().x, (float)playersprite.getPos().y, 0.0f, 1.0f);
		p = Matrix4f.transform(d.getModelViewMatrixAsMatrix(), p, p);
		Vector4f p2 = new Vector4f((float)this.getPos().x, (float)this.getPos().y, 0.0f, 1.0f);
		p2 = Matrix4f.transform(d.getModelViewMatrixAsMatrix(), p2, p2);
		
		this.player.setRect(p.x, p.y, 
				(float)playersprite.getWidth()/Display.getWidth(), 
				(float)playersprite.getHeight()/Display.getHeight());
		this.myrect.setRect(p2.x - width/2, p2.y, 
				(float)width, 
				(float)this.getHeight()/Display.getHeight());
	}
	public void scroll() {
		if(scroll) {
			this.changePos(0.0f, 0.005f);
		}
	}
	public int getThreadID() {
		return threadindex;
	}
	public void resetBlinking() {
		hit = 0;
	}
	public void followPath(DisplaySetup d) {
		if(Math.abs(d.getPos().y) + 1.0f > pos.y && !stopped) {
			if(started) {
				if(stage >= dist) {
					index += 1;
					stage = 0;
					if(index >= ep.getSize()) {
						if(index==ep.getSize()) {
							this.lastp = ep.getPoint(index-1);
							this.p = new PathPoint(new Vector2f(0.0f, 0.0f), ep.getPoint(index-1).getIndex());
							this.p.setPos(new Vector2f(lastp.getPos().x, lastp.getPos().y - 0.6f));
							dist = 50;
						} else {
							this.stopped = true;
						}
					} else {
						this.p = ep.getPoint(index);
						this.lastp = ep.getPoint(index-1);
						dist = (int)(Math.sqrt(Math.pow(p.getPos().x - lastp.getPos().x, 2) + 
								Math.pow(p.getPos().y - lastp.getPos().y, 2))*100.0f);
					}
				} else {
					this.changePos((p.getPos().x - lastp.getPos().x)/dist, (p.getPos().y - lastp.getPos().y)/dist);
					if(animationstyle == 3) {
						float direction = (float) Math.atan2(p.getPos().y - lastp.getPos().y, p.getPos().x - lastp.getPos().x);
						int texture = 6-(int)(direction/(Math.PI/4));
						if(texture < 0) {
							texture += 8;
						}
						if(texture > 7) {
							texture -= 8;
						}
						this.changeTexture(texture);
					}
					stage += 1;
				}
			} else {
				this.index = 1;
				this.p = ep.getPoint(1);
				this.lastp = ep.getPoint(0);
				dist = (int) Math.sqrt(Math.pow(p.getPos().x - lastp.getPos().x, 2) + 
						Math.pow(p.getPos().y - lastp.getPos().y, 2))*10;
				started = true;
				Vector2f startpos = ep.getPoint(0).getPos();
				this.setPos(startpos.x, startpos.y + pos.y + 1.0f);
			}
		}
	}
	public void swirlAround() {
		  if(togo == -1) {
			  direction = (float)(Math.random()*2.0f*Math.PI);
			  togo = 10.0f;
		  }
		  if(togo <= 0 || getPos().x >= 1.0f || getPos().x <= -1.0f) {
			  direction -= Math.PI/4.0f;
			  togo = 10.0f;
		  } else {
			  changePos((float)Math.cos(direction)/200.0f, (float)Math.sin(direction)/200.0f);
			  togo -= 1.0f; 
		  }
	}
	public void update(ShaderHandler sh, DisplaySetup d, DataUtils util) {
		updateColl(d);
		animate();
		if(Math.abs(d.getPos().y) + 1.0f > getPos().y) {
			onscreen = true;
		}
		if(!stopped) {
			for(int i=0; i<playerbullets.size(); i++) {
				if(playerbullets.get(i).contains(this.getPos(), (float)myrect.getWidth(), (float)myrect.getHeight(), d)
						&& !playerbullets.get(i).getDestroying()) {
					parent.bulletexplode(i);
					health -= 5;
					parent.resetThread(threadindex);
					hit = 1;
					if(health <= 0 && !stopped) {
						SoundHandler.playSound("explosion.wav");
						parent.addScorePellet(getPos(), 20);
						stopped = true;
					}
				}
			}
			this.render(sh, util, hit);
			for(int i=0; i<guns.size(); i++) {
				guns.get(i).update(getPos(), (float)myrect.getWidth(), (float)myrect.getHeight());
				if(guns.get(i).isVisible()) {
					guns.get(i).render(sh, util, 0);
					guns.get(i).animate();
				}
			}
		}
		bullets.render(sh, d, util);
	}
	public void fire(DisplaySetup d) {
		for(int i=0; i<guns.size(); i++) {
			if(!stopped) {
				guns.get(i).update(getPos(), (float)myrect.getWidth(), (float)myrect.getHeight());
				guns.get(i).fire();
			}
		}
	}
	public int getTexid() {
		return texid;
	}
}
