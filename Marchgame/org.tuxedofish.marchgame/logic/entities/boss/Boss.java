package logic.entities.boss;

import images.ImageReturn;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.ArrayList;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;

import logic.GridParser;
import logic.entities.Bullet;
import logic.entities.BulletHandler;
import logic.entities.EnemyPath;
import logic.entities.PathPoint;
import logic.entities.Player;
import logic.entities.TexID;
import object.Sprite;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import shader.ShaderHandler;
import sounds.SoundHandler;
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
	private int pattern = 1;
	private boolean stopped = false;
	private BulletHandler bullets;
	private ArrayList<TexID> texids = new ArrayList<TexID>();
	private ArrayList<BossPart> bps = new ArrayList<BossPart>();
	private Rectangle2D player;
	private ArrayList<Rectangle2D> myrect = new ArrayList<Rectangle2D>();
	private Player playersprite;
	private Controller parent;
	private ArrayList<Integer> healths = new ArrayList<Integer>();
	private ArrayList<Bullet> playerbullets = new ArrayList<Bullet>();
	private ArrayList<Integer> hit = new ArrayList<Integer>();
	private ArrayList<Integer> threadids = new ArrayList<Integer>();
	private SoundHandler sounds = new SoundHandler();
	private Clip explosionsound;
	private ArrayList<Integer> shootthreadids = new ArrayList<Integer>();
	private int shootstage = 0;
	private int shootindex = 0;
	
	public Boss(Vector2f pos, int texid, Controller parent, EnemyPath ep, Player player, 
			ArrayList<Bullet> playerbullets) {
		try {
			explosionsound = sounds.loadClip("explosion.wav");
			explosionsound.open(sounds.getAudioStream("explosion.wav"));
		} catch (LineUnavailableException | IOException e) {
			e.printStackTrace();
		}
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
			explosiontex = gp.parseGrid(images.getImage("SegaExplosions.png"), 100.0f);
			bullettex = gp.parseGrid(images.getImage("bullets2.png"), 19.0f);
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
	public void rotate()  {
		for(int i=0; i<me.size(); i++) {
			me.get(i).rotate(0.01f);
		}
	}
	public void addSprite(Sprite s, int lowesttexid, int highesttexid, int health, int pattern, boolean hittable, 
			int threadID, ArrayList<ShootType> shoottypes) {
		me.add(s);
		myrect.add(new Rectangle2D.Float());
		this.texids.add(new TexID(lowesttexid, highesttexid, texid));
		healths.add(health);
		bps.add(new BossPart(hittable, shoottypes));
		hit.add(0);
		threadids.add(threadID);
		for(int i=0; i<shoottypes.size(); i++) {
			for(int j=0; j<shoottypes.get(i).getShootthreads().length; j++) {
				shootthreadids.add(shoottypes.get(i).getShootthreads()[j]);
			}
		}
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
	public BossPart getBossPart(int index) {
		return bps.get(index);
	}
	public void resetBlinking(int index) {
		hit.set(threadids.indexOf(index), 0);
	}
	public ArrayList<Integer> getThreadIDs() {
		return threadids;
	}
	public void shoot(int index) {
		if(bps.get(0).getShootThreads()[shootindex] == index) {
			if(bps.get(0).isHittable()) {
				shootstage += 1;
				if(bps.get(0).getPatterns()[shootindex] == 1) {
					double compassBearing=Math.atan2(me.get(0).getPos().y - playersprite.getPos().y, 
							me.get(0).getPos().x - playersprite.getPos().x);
					shoot((float)compassBearing, 0);
					System.out.println("b " + compassBearing);
				}
				if(shootstage >= bps.get(0).getShootLengths()[shootindex]) {
					shootindex += 1;
					shootstage = 0;
					if(shootindex >= bps.get(0).getShootLengths().length) {
						shootindex = 0;
					}
					parent.resetThread(bps.get(0).getShootThreads()[shootindex]);
				}
			}
		}
	}
	private void shoot(float rot, int index) {
		bullets.add(new Bullet(new Vector2f(me.get(index).getPos().x+(me.get(index).getWidth()/(Display.getWidth()*2.0f)), 
				me.get(index).getPos().y), rot, 40, "normal"));
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
								if(me.size() > 1) {
									bps.remove(j);
								}
								SoundHandler.playSound(explosionsound);
							}
						}
					}
				}
			}
			for(int i=0; i<me.size(); i++) {
				me.get(i).render(sh, util, hit.get(i));
			}
			bullets.render(sh, d, util);
			
			boolean dead = true;
			for(int i=0; i<bps.size(); i++) {
				if(bps.get(i).isHittable() == true) {
					dead = false;
				}
			}
			if(dead && bullets.explosions.size() <= 10) {
				if(deathexplosionsleft == 0) {
					parent.toHome();
					stopped = true;
				} else {
					deathexplosionsleft -= 1;
					SoundHandler.playSound(explosionsound);
					for(int k=0; k<20; k++) {
						bullets.explosions.add(new Bullet(new Vector2f((float)(me.get(0).getPos().x + 
								(Math.random()*myrect.get(0).getWidth())), (float)(me.get(0).getPos().y - 
								(Math.random()*myrect.get(0).getHeight()))), 0, 70, "explosion"));
					}
				}
			}
		}
	}
}
