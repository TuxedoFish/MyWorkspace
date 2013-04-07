package logic.enemies;

import images.ImageReturn;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.ArrayList;

import logic.GridParser;
import object.Sprite;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import shader.ShaderHandler;
import start.Controller;
import start.DisplaySetup;
import texture.TextureHolder;
import utils.DataUtils;

public class Boss {
	private Vector2f pos;
	private int texid;
	private ArrayList<Sprite> me = new ArrayList<Sprite>();
	private PathPoint p;
	private PathPoint lastp;
	private EnemyPath ep;
	private int stage = 0;
	private boolean started = false;
	private int anstage = 0;
	private int textstage = 0;
	private int dist;
	private int index;
	private int pattern = 1;
	private boolean stopped = false;
	private ArrayList<EnemyBullet> bullets = new ArrayList<EnemyBullet>();
	private ArrayList<EnemyBullet> explosions = new ArrayList<EnemyBullet>();
	private ArrayList<TexID> texids = new ArrayList<TexID>();
	private ArrayList<BossPart> bps = new ArrayList<BossPart>();
	private Sprite bullet;
	private Sprite explosion;
	private Vector2f bpos = new Vector2f(0.0f, 0.0f);
	private Vector2f epos = new Vector2f(0.0f, 0.0f);
	private Rectangle2D player;
	private ArrayList<Rectangle2D> myrect = new ArrayList<Rectangle2D>();
	private Sprite playersprite;
	private Controller parent;
	private ArrayList<Integer> healths = new ArrayList<Integer>();
	private ArrayList<EnemyBullet> playerbullets = new ArrayList<EnemyBullet>();
	private ArrayList<Integer> hit = new ArrayList<Integer>();
	private ArrayList<Integer> threadids = new ArrayList<Integer>();
	
	public Boss(Vector2f pos, int texid, Controller parent, EnemyPath ep, Sprite player, 
			ArrayList<EnemyBullet> playerbullets) {
		this.playerbullets = playerbullets;
		this.parent = parent;
		this.pos = new Vector2f(((pos.x/Display.getWidth())*2.0f)-1.0f, 
				((pos.y)/Display.getHeight()));
		this.playersprite = player;
		this.ep = ep;
		this.texid = texid;
		this.player = new Rectangle2D.Float();
		
		ImageReturn images = new ImageReturn();
		GridParser gp = new GridParser();
		TextureHolder texture;
		try {
			texture = gp.parseGrid(images.getImage("explosion.png"), 29);
			this.explosion = new Sprite(images.getImage("explosion.png"), parent, 70, 70, texture, 
					0, new Vector2f(0.0f, 0.0f));
			texture = gp.parseGrid(images.getImage("bullets2.png"), 19);
			this.bullet = new Sprite(images.getImage("bullets2.png"), parent, 40, 40, texture, 
					0, new Vector2f(0.0f, 0.0f));
		} catch (IOException e) {
			System.err.println("err at enemy");
			System.exit(1);
			e.printStackTrace();
		}
	}
	public int getAmountOfParts() {
		return me.size();
	}
	public void finish(IntBuffer vboids, IntBuffer vaoids) {
		this.explosion.finish(vboids.get(0), vaoids.get(0));
		this.bullet.finish(vboids.get(1), vaoids.get(1));
		for(int i=0; i<me.size(); i++) {
			me.get(i).finish(vboids.get(i+1), vaoids.get(i+1));
		}
	}
	public void update(DisplaySetup d) {
		Vector4f p = new Vector4f((float)playersprite.getPos().x, (float)playersprite.getPos().y, 0.0f, 1.0f);
		p = Matrix4f.transform(d.getModelViewMatrixAsMatrix(), p, p);
		
		this.player.setRect(p.x, p.y, 
				(float)playersprite.getWidth()/Display.getWidth(), 
				(float)playersprite.getHeight()/Display.getHeight());
		for(int i=0; i<myrect.size(); i++) {
			Vector4f p2 = new Vector4f((float)me.get(i).getPos().x, (float)me.get(i).getPos().y, 0.0f, 1.0f);
			p2 = Matrix4f.transform(d.getModelViewMatrixAsMatrix(), p2, p2);
			
			this.myrect.get(i).setRect(p2.x, p2.y, 
					(float)me.get(i).getWidth()/Display.getWidth(), 
					(float)me.get(i).getHeight()/Display.getHeight());
		}
	}
	public void addSprite(Sprite s, int lowesttexid, int highesttexid, int health, int pattern, boolean hittable, 
			int threadID) {
		me.add(s);
		myrect.add(new Rectangle2D.Float());
		this.texids.add(new TexID(lowesttexid, highesttexid, texid));
		healths.add(health);
		bps.add(new BossPart(pattern, hittable));
		hit.add(0);
		threadids.add(threadID);
	}
	public void animate() {
		for(int i=0; i<me.size(); i++) {
			if(this.texids.get(i).isUp()) {
				if(this.texids.get(i).getHighesttexid()>texids.get(i).getCurrenttexid()+1) {
					me.get(i).changeTexture(texids.get(i).getCurrenttexid()+1);
					this.texids.get(i).setCurrenttexid(texids.get(i).getCurrenttexid()+1);
				} else {
					this.texids.get(i).setUp(false);
				}
			} else {
				if(this.texids.get(i).getLowesttexid()<texids.get(i).getCurrenttexid()-1) {
					me.get(i).changeTexture(texids.get(i).getCurrenttexid()-1);
					this.texids.get(i).setCurrenttexid(texids.get(i).getCurrenttexid()-1);
				} else {
					this.texids.get(i).setUp(true);
				}
			}
		}
	}
	public void resetBlinking(int index) {
		hit.set(threadids.indexOf(index), 0);
	}
	public ArrayList<Integer> getThreadIDs() {
		return threadids;
	}
	public void shoot() {
		for(int i=0; i<me.size(); i++) {
			if(bps.get(i).isHittable()) {
				if(bps.get(i).getPattern() == 1) {
					for(float j=0.0f; j<2*Math.PI; j+= Math.PI/4) {
						bullets.add(new EnemyBullet(me.get(i).getPos(), j, 40));
					}
				}
			}
		}
	}
	public void render(ShaderHandler sh, DisplaySetup d, DataUtils util) {
		update(d);
		if(anstage == 5) {
			animate();
			anstage = 0;
		} else {
			anstage += 1;
		}
		if(!stopped) {
			for(int i=0; i<playerbullets.size(); i++) {
				for(int j=0; j<myrect.size(); j++) {
					if(playerbullets.get(i).contains(me.get(j).getPos(), (float)myrect.get(j).getWidth(), 
							(float)myrect.get(j).getHeight(), d) && bps.get(j).isHittable()) {
						if(!playerbullets.get(i).getDestroying()) {
							parent.bulletexplode(i);
							this.healths.set(j, healths.get(j)-5);
							hit.set(j, 1);
							if(healths.get(j)<=0) {
								me.remove(j);
								myrect.remove(j);
								healths.remove(j);
								if(me.size() == 0) {
									stopped = true;
								}
								j-=1;
							}
						}
					}
				}
			}
			for(int i=0; i<me.size(); i++) {
				me.get(i).render(sh, util, hit.get(i));
			}
			for(int i=0; i<explosions.size(); i++) {
				explosion.changeTexture((explosions.get(i).getAge()/5));
				explosions.get(i).age();
				explosion.changePos(explosions.get(i).getPos().x-epos.x, explosions.get(i).getPos().y-epos.y);
				epos = new Vector2f(explosions.get(i).getPos().x, explosions.get(i).getPos().y);
				explosion.render(sh, util, 0);
				if(explosions.get(i).getAge()>24) {
					explosions.remove(i);
					i-=1;
				}
			}
			boolean changed = true;
			for(int i=0; i<bullets.size(); i++) {
				if(changed) {
					bullet.changeTexture((textstage/5));
					changed = false;
				}
				boolean stopped = false;
				if(!bullets.get(i).getDestroying()) {
					bullets.get(i).setPos(new Vector2f((float)(bullets.get(i).getPos().x - (Math.sin(bullets.get(i).getRot())/100.0f)), 
						(float)(bullets.get(i).getPos().y - (Math.cos(bullets.get(i).getRot())/100.0f))));
					Vector4f bulletp = new Vector4f(bullets.get(i).getPos().x, bullets.get(i).getPos().y, 0.0f, 1.0f);
					Matrix4f.transform(d.getModelViewMatrixAsMatrix(), bulletp, bulletp);
					if(bulletp.x >= 1.1f || bulletp.x <= -1.1f || bulletp.y >= 1.1f || bulletp.y <= -1.1f) {
						bullets.get(i).setDestroyingSelf(true);
						explosions.add(new EnemyBullet(bullets.get(i).getPos(), 0.0f, 70));
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
					bullet.render(sh, util, 0);
					if(!bullets.get(i).getDestroying()) {
						if(bullets.get(i).contains(playersprite.getPos(), 
								(float)player.getWidth(), (float)player.getHeight(), d)) {
							bullets.get(i).setDestroyingSelf(true);
							explosions.add(new EnemyBullet(bullets.get(i).getPos(), 0.0f, 70));
							explosions.add(new EnemyBullet(new Vector2f((float)(bullets.get(i).getPos().x + (Math.random()*0.1f)-0.05f), (float)(bullets.get(i).getPos().y + (Math.random()*0.04f)-0.02f)), 0.0f, 70));
							explosions.add(new EnemyBullet(new Vector2f((float)(bullets.get(i).getPos().x + (Math.random()*0.1f)-0.05f), (float)(bullets.get(i).getPos().y + (Math.random()*0.04f)-0.02f)), 0.0f, 70));
							parent.damage(5);
						}
					}
				}
			}
		}
	}
}
