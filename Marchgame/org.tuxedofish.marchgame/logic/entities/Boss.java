package logic.entities;

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
	private int deathexplosionsleft = 5;
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
	private BulletHandler bullets;
	private ArrayList<TexID> texids = new ArrayList<TexID>();
	private ArrayList<BossPart> bps = new ArrayList<BossPart>();
	private Rectangle2D player;
	private ArrayList<Rectangle2D> myrect = new ArrayList<Rectangle2D>();
	private Sprite playersprite;
	private Controller parent;
	private ArrayList<Integer> healths = new ArrayList<Integer>();
	private ArrayList<Bullet> playerbullets = new ArrayList<Bullet>();
	private ArrayList<Integer> hit = new ArrayList<Integer>();
	private ArrayList<Integer> threadids = new ArrayList<Integer>();
	private ArrayList<Integer> shootthreadids = new ArrayList<Integer>();
	
	public Boss(Vector2f pos, int texid, Controller parent, EnemyPath ep, Player player, 
			ArrayList<Bullet> playerbullets) {
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
		TextureHolder bullettex, explosiontex;
		try {
			explosiontex = gp.parseGrid(images.getImage("explosion.png"), 29);
			bullettex = gp.parseGrid(images.getImage("bullets2.png"), 19);
			bullets = new BulletHandler(bullettex, explosiontex, "bullets2", parent, player);
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
		bullets.finish(vboids, vaoids);
		for(int i=0; i<me.size(); i++) {
			me.get(i).finish(vboids.get(i+1), vaoids.get(i+1));
		}
	}
	public ArrayList<Integer> getShootThreadIDs() {
		return shootthreadids;
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
			int threadID, int shootthreadid) {
		me.add(s);
		myrect.add(new Rectangle2D.Float());
		this.texids.add(new TexID(lowesttexid, highesttexid, texid));
		healths.add(health);
		bps.add(new BossPart(pattern, hittable));
		hit.add(0);
		threadids.add(threadID);
		shootthreadids.add(shootthreadid);
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
	public void shoot(int index) {
		int i = shootthreadids.indexOf(index);
		if(bps.get(i).isHittable()) {
			if(bps.get(i).getPattern() == 1) {
				for(float j=0.0f; j<2*Math.PI; j+= Math.PI/4) {
					if(i < me.size()) {
						bullets.add(new Bullet(me.get(i).getPos(), j, 40));
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
								parent.removeThread(shootthreadids.get(j));
								me.remove(j);
								myrect.remove(j);
								healths.remove(j);
								if(me.size() == 1) {
									for(int k=0; k<50; k++) {
										bullets.addExplosion(new Bullet(new Vector2f((float)(me.get(0).getPos().x + 
												(Math.random()*myrect.get(0).getWidth())), (float)(me.get(0).getPos().y - 
												(Math.random()*myrect.get(0).getHeight()))), 0, 70));
									}
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
			bullets.render(sh, d, util);
			if(me.size() == 1 && bullets.explosions.size() <= 10) {
				if(deathexplosionsleft == 0) {
					parent.toHome();
					stopped = true;
				} else {
					deathexplosionsleft -= 1;
					for(int k=0; k<50; k++) {
						bullets.explosions.add(new Bullet(new Vector2f((float)(me.get(0).getPos().x + 
								(Math.random()*myrect.get(0).getWidth())), (float)(me.get(0).getPos().y - 
								(Math.random()*myrect.get(0).getHeight()))), 0, 70));
					}
				}
			}
		}
	}
}