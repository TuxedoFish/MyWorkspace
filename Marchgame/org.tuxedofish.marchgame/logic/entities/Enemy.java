package logic.entities;

import images.ImageReturn;

import java.awt.Polygon;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;

import logic.GridParser;

import org.lwjgl.*;
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
	private String movementtype;
	
	private float direction = -1;
	private float togo = -1;
	private boolean onscreen = false;
	
	private ArrayList<Integer> gunspeeds = new ArrayList<Integer>();
	
	private int animationstyle = 1;
	
	private ArrayList<Bullet> playerbullets = new ArrayList<Bullet>();
	private ArrayList<Gun> guns = new ArrayList<Gun>();
	private int lti;
	private int hti;
	private boolean isup = true;
	private float width;
	private float height;
	private TextureHolder[] textures;
	private int texturestage;
	private int speed;
	private int hit;
	private int health;
	private int threadindex;
	private int shootspeed;
	private int[] shootthreadindexs;
	private String name;
	
	private boolean firing = false;
	
	private SoundHandler sounds = new SoundHandler();
	private Clip explosionsound;
	
	private int bullettexid;
	private int explosiontexid;
	
	private String[] sizes;
	
	private ArrayList<Polygon> collisions = new ArrayList<Polygon>();
	
	public ArrayList<Polygon> getCollisions() {
		return collisions;
	}
	public int getHti() {
		return hti;
	}
	public int getLti() {
		return lti;
	}
	public int getHealth() {
		return health;
	}
	public int getPattern() {
		return pattern;
	}
	public int getAnimationType() {
		return animationstyle;
	}
	public String getMovementStyle() {
		return movementtype;
	}
	public EnemyPath getEnemyPath() {
		return ep;
	}
	public Enemy(Vector2f pos, int texid, Controller parent, EnemyPath ep, Player player, 
			ArrayList<Bullet> playerbullets, int lowesttexid, int highesttexid
			, String[] sizes, int pattern, TextureHolder[] ts, int health, int shootspeed, int bullettexid, int explosiontexid,
			String movementtype, int animationtype, ArrayList<Polygon> collisions) {
		super(parent, Integer.valueOf(sizes[0]), Integer.valueOf(sizes[1]), ts[2], 0, new Vector2f(((pos.x/Display.getWidth())), 
				((pos.y)/(Display.getHeight()/2.0f))), texid);
		this.collisions = collisions;
		this.explosiontexid = explosiontexid;
		this.bullettexid = bullettexid;
		this.movementtype = movementtype;
		this.animationstyle = animationtype;
		this.sizes = sizes;
		if(movementtype.contains("swirl") || movementtype.contains("toplayer")) {
			setScroll(false);
			setPos((float)(-0.6f + (Math.random()*1.2f)), pos.y/(Display.getHeight()/2)-1.0f);
		}
		setup(pos, texid, parent, ep, player, playerbullets, lowesttexid, highesttexid, sizes, pattern, ts, health, shootspeed);
	}
	public String getName() {
		return name;
	}
	public void setScroll(boolean b) {
		scroll = b;
	}
	public void setup(Vector2f pos, int texid, Controller parent, EnemyPath ep, Player player, 
			ArrayList<Bullet> playerbullets, int lowesttexid, int highesttexid
			, String[] sizes, int pattern, TextureHolder[] ts, int health, int shootspeed) {
		try {
			explosionsound = sounds.loadClip("explosion.wav");
			explosionsound.open(sounds.getAudioStream("explosion.wav"));
		} catch (LineUnavailableException | IOException e) {
			e.printStackTrace();
		}
		this.width = Float.valueOf(sizes[0])/Display.getWidth();
		this.height = Float.valueOf(sizes[1])/Display.getHeight();
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
		this.texid = 0;
		this.player = new Rectangle2D.Float();
		this.myrect = new Rectangle2D.Float();
		this.hit = 0;
		
		textures = ts;
		bullets = new BulletHandler(ts[1], ts[0], parent, player, bullettexid, explosiontexid);
	}
	public void setAnimationSpeed(int speed) {
		this.speed = speed;
	}
	public BulletHandler getBulletHandler() {
		return bullets;
	}
	public void addGun(Gun g, boolean visible, int shootspeed) {
		guns.add(g);
		if(shootspeed==-1) {
			shootspeed = this.shootspeed;
		}
		gunspeeds.add(shootspeed);
		guns.get(guns.size()-1).setVisible(visible);
		guns.get(guns.size()-1).finish(getBulletHandler());
	}
	public int getAmountOfGuns() {
		return guns.size();
	}
	public int[] getShootThreadID() {
		return shootthreadindexs;
	}
	public ArrayList<Integer> getShootSpeeds() {
		return gunspeeds;
	}
	public Integer getShootSpeed() {
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
						if(pattern == 3) {
							firing = true;
							speed = 10;
						}
						isup = false;
					}
				} else {
					if(texid>lti) {
						if(animationstyle == 1) {
							if(pattern == 3) {
								firing = false;
								speed = 10;
								for(int i=0; i<guns.size(); i++) {guns.get(i).clear();}
							}
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
		if(animationstyle == 4) {
			float direction = (float) Math.atan2(playersprite.getPos().y-getPos().y, playersprite.getPos().x-getPos().x);
			int texture = 6-(int)(direction/(Math.PI/4));
			if(texture < 0) {
				texture += 8;
			}
			if(texture > 7) {
				texture -= 8;
			}
			this.changeTexture(texture + (texid*10));
			if(texturestage>speed) {
				if(isup) {
					if(texid<hti) {
						texid += 1;
					} else {
						isup = false;
					}
				} else {
					if(texid>lti) {
						texid=0;
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
				(float)height);
	}
	public String[] getSizes() {
		return this.sizes;
	}
	public void scroll() {
		if(scroll) {
			this.changePos(0.0f, 0.0025f);
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
				if(stage >= dist/p.getSpeed()) {
					index += 1;
					stage = 0;
					if(index >= ep.getSize()) {
						if(index==ep.getSize()) {
							this.stopped = true;
						}
					} else {
						this.p = ep.getPoint(index);
						this.lastp = ep.getPoint(index-1);
						dist = (int)(Math.sqrt(Math.pow(p.getPos().x - lastp.getPos().x, 2) + 
								Math.pow(p.getPos().y - lastp.getPos().y, 2))*300.0f);
					}
				} else {
					this.changePos(((p.getPos().x - lastp.getPos().x)/dist)*p.getSpeed(), 
							((p.getPos().y - lastp.getPos().y)/dist)*p.getSpeed());
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
	public boolean isDead() {
		return stopped;
	}
	public void update(ShaderHandler sh, DisplaySetup d, DataUtils util) {
		updateColl(d);
		animate();
		if(firing && !stopped) {
			for(int i=0; i<guns.size(); i++) {guns.get(i).fire();}
		}
		if(movementtype.contains("path")) {
			followPath(d);
		}
		if(movementtype.contains("swirl")) {
			swirlAround();
		}
		if(movementtype.contains("toplayer")) {
			if(getPos().y > playersprite.getPos().y) {
				gotoplayer();
			} else {
				changePos(0.0f, -0.01f);
			}
		}
		
		if(Math.abs(d.getPos().y) + 1.0f > getPos().y) {
			onscreen = true;
		}
		if(!stopped) {
			for(int i=0; i<playerbullets.size(); i++) {
				if(playerbullets.get(i).contains(this.getPos(), collisions.get(texid), d)
						&& !playerbullets.get(i).getDestroying()) {
					parent.bulletexplode(i);
					health -= 2;
					hit = 1;
					if(health <= 0 && !stopped) {
						SoundHandler.playSound(explosionsound);
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
	private void gotoplayer() {
		direction=(float) Math.atan2(playersprite.getPos().y - this.getPos().y, 
				playersprite.getPos().x - this.getPos().x);
		changePos((float)Math.cos(direction)/200.0f, (float)Math.sin(direction)/200.0f);
	}
	public void finish(IntBuffer vboids, IntBuffer vaoids, int[] index) {
		this.threadindex = index[0];
		this.shootthreadindexs = new int[index.length-1];
		for(int i=1; i<index.length; i++) {
			shootthreadindexs[i-1] = index[i];
		}
		bullets.finishwithouttex(vboids, vaoids);
		for(int i=0; i<guns.size(); i++) {
			guns.get(i).finish(vboids.get(1+i), vaoids.get(1+i));
		}
		this.finishwithouttex(vboids.get(0), vaoids.get(0));
	}

	public void finish(int[] vbo, int[] vao, int[] index) {
		this.threadindex = index[0];
		this.shootthreadindexs = new int[index.length-1];
		for(int i=1; i<index.length; i++) {
			shootthreadindexs[i-1] = index[i];
		}
		bullets.finishwithouttex(vbo, vao);
		for(int i=0; i<guns.size(); i++) {
			guns.get(i).finish(vbo[1+i], vao[1+i]);
		}
		this.finishwithouttex(vbo[0], vao[0]);
	}
	public void fire(DisplaySetup d, int threadindex) {
		if(!stopped && pattern != 3) {
				int index=-1;
				for(int i=0; i<shootthreadindexs.length; i++) {
					if(shootthreadindexs[i]==threadindex) {
						index=i;
					}
				}
			if(index<guns.size()) {
				guns.get(index).update(getPos(), (float)myrect.getWidth(), (float)myrect.getHeight());
				guns.get(index).fire();
			}
		}
	}
	public int getTexid() {
		return texid;
	}
}
