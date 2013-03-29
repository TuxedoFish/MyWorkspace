package logic.enemies;

import images.ImageReturn;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
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
import start.Controller;
import start.DisplaySetup;
import texture.TextureHolder;

public class Enemy {
	private Vector2f pos;
	private int texid;
	private Sprite me;
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
	private ArrayList<EnemyBullet> bullets = new ArrayList<EnemyBullet>();
	private ArrayList<EnemyBullet> explosions = new ArrayList<EnemyBullet>();
	private Sprite bullet;
	private Sprite explosion;
	private Vector2f bpos = new Vector2f(0.0f, 0.0f);
	private Vector2f epos = new Vector2f(0.0f, 0.0f);
	private Rectangle2D player;
	private Rectangle2D myrect;
	private Sprite playersprite;
	private Controller parent;
	private ArrayList<EnemyBullet> playerbullets = new ArrayList<EnemyBullet>();
	private int lti;
	private int hti;
	private boolean isup = true;
	private int width;
	
	public Enemy(Vector2f pos, int texid, Controller parent, EnemyPath ep, Sprite player, 
			ArrayList<EnemyBullet> playerbullets, String texloc, int lowesttexid, int highesttexid
			, int width, int pattern) {
		this.width = width/Display.getWidth();
		this.playerbullets = playerbullets;
		this.parent = parent;
		this.pattern = pattern;
		this.lti = lowesttexid;
		this.hti = highesttexid;
		this.pos = new Vector2f(((pos.x/Display.getWidth())), 
				((pos.y)/(Display.getHeight()/2.0f)));
		this.playersprite = player;
		this.ep = ep;
		this.texid = texid;
		this.player = new Rectangle2D.Float();
		this.myrect = new Rectangle2D.Float();
		
		ImageReturn images = new ImageReturn();
		GridParser gp = new GridParser();
		TextureHolder texture;
		
		try {
			texture = gp.parseGrid(images.getImage("explosion.png"), 30);
			this.explosion = new Sprite(images.getImage("explosion.png"), parent, 70, 70, texture, 
					0, new Vector2f(0.0f, 0.0f));
			texture = gp.parseGrid(images.getImage("bullets2.png"), 20);
			this.bullet = new Sprite(images.getImage("bullets2.png"), parent, 40, 40, texture, 
					0, new Vector2f(0.0f, 0.0f));
			texture = gp.parseGrid(images.getImage(texloc), 50);
			me = new Sprite(images.getImage(texloc), parent, 100, 100, texture, 0, this.pos);
		} catch (IOException e) {
			System.err.println("err at enemy");
			System.exit(1);
			e.printStackTrace();
		}
	}
	public void animate() {
		if(isup) {
			if(hti>texid+1) {
				me.changeTexture(texid+1);
				texid += 1;
			} else {
				isup = false;
			}
		} else {
			if(lti<texid-1) {
				me.changeTexture(texid-1);
				texid -= 1;
			} else {
				isup = true;
			}
		}
	}
	public void update(DisplaySetup d) {
		Vector4f p = new Vector4f((float)playersprite.getPos().x, (float)playersprite.getPos().y, 0.0f, 1.0f);
		p = Matrix4f.transform(d.getModelViewMatrixAsMatrix(), p, p);
		Vector4f p2 = new Vector4f((float)me.getPos().x, (float)me.getPos().y, 0.0f, 1.0f);
		p2 = Matrix4f.transform(d.getModelViewMatrixAsMatrix(), p2, p2);
		
		this.player.setRect(p.x, p.y, 
				(float)playersprite.getWidth()/Display.getWidth(), 
				(float)playersprite.getHeight()/Display.getHeight());
		this.myrect.setRect(p2.x - width/2, p2.y, 
				(float)width, 
				(float)me.getHeight()/Display.getHeight());
	}
	private void shoot(float rot) {
		bullets.add(new EnemyBullet(new Vector2f(me.getPos().x+(me.getWidth()/(Display.getWidth()*2.0f)), me.getPos().y), rot));
	}
	public void scroll() {
		me.changePos(0.0f, 0.005f);
	}
	public void render(ShaderHandler sh, DisplaySetup d) {
		update(d);
		animate();
		if(Math.abs(d.getPos().y) + 1.0f > pos.y && !stopped) {
			for(int i=0; i<playerbullets.size(); i++) {
				Vector4f tempos = new Vector4f(playerbullets.get(i).getPos().x + ((25/Display.getWidth())/2.0f), 
						playerbullets.get(i).getPos().y - ((25/Display.getHeight())/2.0f), 0.0f, 1.0f);
				tempos = Matrix4f.transform(d.getModelViewMatrixAsMatrix(), tempos, tempos);
				Vector2f p1 = new Vector2f((float)myrect.getX(), (float)myrect.getY());
				Vector2f p2 = new Vector2f((float)(myrect.getX() + myrect.getWidth()), (float)myrect.getY());
				Vector2f p3 = new Vector2f((float)(myrect.getX() + myrect.getWidth()), (float)(myrect.getY() - myrect.getHeight()));
				Vector2f p4 = new Vector2f((float)myrect.getX(), (float)(myrect.getY() - myrect.getHeight()));
				if(Math.abs(Math.sqrt(Math.pow(p1.x - tempos.x, 2) + 
						Math.pow(p1.y - tempos.y, 2))) < (float)40.0f/Display.getWidth() ||
				   Math.abs(Math.sqrt(Math.pow(p2.x - tempos.x, 2) + 
						Math.pow(p2.y - tempos.y, 2))) < (float)40.0f/Display.getWidth() ||
				   Math.abs(Math.sqrt(Math.pow(p3.x - tempos.x, 2) + 
						Math.pow(p3.y - tempos.y, 2))) < (float)40.0f/Display.getWidth() ||
				   Math.abs(Math.sqrt(Math.pow(p4.x - tempos.x, 2) + 
						Math.pow(p4.y - tempos.y, 2))) < (float)40.0f/Display.getWidth()) {
					//parent.bulletexplode(i);
					stopped = true;
				}
			}
			if(started) {
				if(stage >= dist) {
					index += 1;
					stage = 0;
					if(index >= ep.getSize()) {
						if(index==ep.getSize()) {
							this.lastp = ep.getPoint(index-1);
							this.p = new PathPoint(new Vector2f(0.0f, 0.0f), ep.getPoint(index-1).getIndex());
							this.p.setPos(new Vector2f(lastp.getPos().x, lastp.getPos().y - 0.6f));
							dist = 30;
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
					me.changePos((p.getPos().x - lastp.getPos().x)/dist, (p.getPos().y - lastp.getPos().y)/dist);
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
				me.setPos(startpos.x, startpos.y + pos.y + 1.0f);
			}
			me.render(sh);
		}
		for(int i=0; i<explosions.size(); i++) {
			explosion.changeTexture((explosions.get(i).getAge()/5));
			explosions.get(i).age();
			explosion.changePos(explosions.get(i).getPos().x-epos.x, explosions.get(i).getPos().y-epos.y);
			epos = new Vector2f(explosions.get(i).getPos().x, explosions.get(i).getPos().y);
			explosion.render(sh);
			if(explosions.get(i).getAge()>24) {
				explosions.remove(i);
				i-=1;
			}
		}
		if(textstage >= 35) {
			textstage = 0;
		}
		textstage += 1;
		boolean changed = true;
		for(int i=0; i<bullets.size(); i++) {
			if(changed) {
				bullet.changeTexture((textstage/5));
				changed = false;
			}
			boolean stopped = false;
			if(!bullets.get(i).getDestroying()) {
				bullets.get(i).setPos(new Vector2f((float)(bullets.get(i).getPos().x - (Math.sin(bullets.get(i).getRot())/200.0f)), 
					(float)(bullets.get(i).getPos().y - (Math.cos(bullets.get(i).getRot())/200.0f))));
				if(bullets.get(i).getAge() > 100) {
					bullets.get(i).setDestroyingSelf(true);
					explosions.add(new EnemyBullet(bullets.get(i).getPos(), 0.0f));
					changed = true;
					bullet.changeTexture(8);
				}
			} else {
				if(bullets.get(i).getAge() < bullets.get(i).getLastAge()+20) {
					bullet.changeTexture(8 + ((bullets.get(i).getAge()-bullets.get(i).getLastAge())/5));
					changed = true;
				} else {
					stopped = true;
					bullets.remove(i);
					i-=1;
				}
			}
			if(!stopped) {
				bullets.get(i).age();
				bullet.changePos(bullets.get(i).getPos().x-bpos.x, bullets.get(i).getPos().y-bpos.y);
				bpos = new Vector2f(bullets.get(i).getPos().x, bullets.get(i).getPos().y);
				bullet.render(sh);
				if(!bullets.get(i).getDestroying()) {
					Vector4f tempos = new Vector4f(bullets.get(i).getPos().x + ((bullet.getWidth()/Display.getWidth())/2.0f), 
							bullets.get(i).getPos().y - ((bullet.getHeight()/Display.getHeight())/2.0f), 0.0f, 1.0f);
					tempos = Matrix4f.transform(d.getModelViewMatrixAsMatrix(), tempos, tempos);
					Vector2f p1 = new Vector2f((float)player.getX(), (float)player.getY());
					Vector2f p2 = new Vector2f((float)(player.getX() + player.getWidth()), (float)player.getY());
					Vector2f p3 = new Vector2f((float)(player.getX() + player.getWidth()), (float)(player.getY() - player.getHeight()));
					Vector2f p4 = new Vector2f((float)player.getX(), (float)(player.getY() - player.getHeight()));
					if(Math.abs(Math.sqrt(Math.pow(p1.x - tempos.x, 2) + 
							Math.pow(p1.y - tempos.y, 2))) < (float)40.0f/Display.getWidth() ||
					   Math.abs(Math.sqrt(Math.pow(p2.x - tempos.x, 2) + 
							Math.pow(p2.y - tempos.y, 2))) < (float)40.0f/Display.getWidth() ||
					   Math.abs(Math.sqrt(Math.pow(p3.x - tempos.x, 2) + 
							Math.pow(p3.y - tempos.y, 2))) < (float)40.0f/Display.getWidth() ||
					   Math.abs(Math.sqrt(Math.pow(p4.x - tempos.x, 2) + 
							Math.pow(p4.y - tempos.y, 2))) < (float)40.0f/Display.getWidth()) {
						bullets.get(i).setDestroyingSelf(true);
						explosions.add(new EnemyBullet(bullets.get(i).getPos(), 0.0f));
						explosions.add(new EnemyBullet(new Vector2f((float)(bullets.get(i).getPos().x + (Math.random()*0.2f)-0.1f), (float)(bullets.get(i).getPos().y + (Math.random()*0.08f)-0.04f)), 0.0f));
						explosions.add(new EnemyBullet(new Vector2f((float)(bullets.get(i).getPos().x + (Math.random()*0.2f)-0.1f), (float)(bullets.get(i).getPos().y + (Math.random()*0.08f)-0.04f)), 0.0f));
						parent.damage(5);
					}
				}
			}
		}
	}
	public void fire(DisplaySetup d) {
		if(Math.abs(d.getPos().y) + 1.0f > pos.y && !stopped) {
			if(pattern == 1) {
				for(float i=0; i<2*Math.PI; i+=1.5f) {
					shoot(i);
				}
			}
			if(pattern == 2) {
				Vector4f p = new Vector4f(playersprite.getPos().x, -playersprite.getPos().y, 0.0f, 1.0f);
				p = Matrix4f.transform(d.getModelViewMatrixAsMatrix(), p, p);
				Vector4f p2 = new Vector4f(me.getPos().x, -me.getPos().y, 0.0f, 1.0f);
				p2 = Matrix4f.transform(d.getModelViewMatrixAsMatrix(), p2, p2);
				//System.out.println(p + " : " + p2);
				
				shoot((float)(Math.atan2((p.y - p2.y), (p.x - p2.x)) * (180.0f/Math.PI)));
			}
		}
	}
	public int getTexid() {
		return texid;
	}
	public Vector2f getPos() {
		return pos;
	}
}
