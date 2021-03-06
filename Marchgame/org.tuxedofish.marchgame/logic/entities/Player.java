package logic.entities;

import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import images.ImageReturn;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;

import logic.GridParser;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import object.Sprite;
import shader.ShaderHandler;
import sounds.SoundHandler;
import start.Controller;
import start.DisplaySetup;
import texture.TextureHolder;
import utils.DataUtils;

public class Player extends Sprite{
	private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	private ArrayList<Bullet> explosions = new ArrayList<Bullet>();
	private Sprite explosion;
	private Sprite bullet;
	private Controller parent;
	private int stage = 0;
	private Vector2f bpos = new Vector2f(0.0f, 0.0f); private Vector2f epos = new Vector2f(0.0f, 0.0f);
	private int hit = 0; 
	private int health = 500;
	private int currenttex = 0;
	private int movement = 0;
	private SoundHandler sounds = new SoundHandler();
	private Clip shootsound;
	private Clip explosionsound;
	private boolean inposition = true;
	private int speed = 2;
	private int animastage = 0;
	private int lives = 3;
	private int ticks = 0;
	private boolean show = true;
	
	public Player(BufferedImage img, int width, int height,
			TextureHolder th, int currenttexid, Vector2f pos, Controller parent) {
		super(img, parent, width, height, th, currenttexid, pos);
		
		this.parent  = parent;
		GridParser gp = new GridParser(); ImageReturn images = new ImageReturn();
		
		try {
			shootsound = sounds.loadClip("shoot.wav");
			shootsound.open(sounds.getAudioStream("shoot.wav"));
			explosionsound = sounds.loadClip("explosion.wav");
			explosionsound.open(sounds.getAudioStream("explosion.wav"));
		} catch (LineUnavailableException | IOException e) {
			e.printStackTrace();
		}
		
		try {
			TextureHolder explosiontex = gp.parseGrid(images.getImage("SegaExplosions.png"), 100.0f);
			this.explosion = new Sprite(images.getImage("SegaExplosions.png"), parent, 100, 100, explosiontex, 
					0, new Vector2f(0.0f, 0.0f));
			
			TextureHolder bullettex = gp.parseGrid(images.getImage("bullets.png"), 19);
			this.bullet = new Sprite(images.getImage("bullets.png"), parent, 40, 40, bullettex, 0, 
					new Vector2f(0.0f, 0.0f));
		} catch (IOException e) {
			System.err.println("err generating player sprites");
			e.printStackTrace();
		}
	}
	public void finish(IntBuffer vboids, IntBuffer vaoids) {
		finish(vboids.get(0), vaoids.get(0));
		this.explosion.finish(vboids.get(1), vaoids.get(1));
		this.bullet.finish(vboids.get(2), vaoids.get(2));
	}
	public void reset() {
		setPos(0.0f, -0.75f);
		health = 500;
	}
	public int getHealth() {
		return health;
	}
	public void resetBlinking() {
		if(hit == 1 && ticks == 0) {
			lives -= 1;
			SoundHandler.playSound(explosionsound);
			for(int k=0; k<3; k++) {
				explosions.add(new Bullet(new Vector2f((float)(getPos().x + 
						(Math.random()*getWidth())), (float)(getPos().y - 
						(Math.random()*getHeight()))), 0, 70, "explosion"));
			}
			if(lives == 0) {
				parent.end();
			}
		}
		ticks += 1;
		if(hit == 1) {
			if(show) {
				show = false;
			} else { 
				show = true;
			}
		} else {
			show = true;
		}
		if(ticks >= 5) {
			hit = 0;
		}
	}
	public void damage(int d) {
		if(hit == 0) {
			hit = 1;
			ticks = 0;
		}
	}
	public int getHit() {
		return hit;
	}
	public void move(float x, float y, float xoffset, DisplaySetup display) {
		if(!(getPos().x + (getWidth()/Display.getWidth()) + x > 0.8f+(xoffset)) || x <= 0) {
			if(!(getPos().x - x < -0.95f-(xoffset)) || x >= 0) {
				if(!(getPos().y + y > -display.getPos().y + 1.0f) || y <= 0) {
					if(!(getPos().y - (getHeight()/Display.getHeight()) + y < -display.getPos().y - 0.8f) || y >= 0) {
							changePos(x, y);
					}
				}
			}
		}
	}
	public void bulletexplode(int index) {
		bullets.get(index).setDestroyingSelf(true);
		explosions.add(new Bullet(bullets.get(index).getPos(), 0.0f, 70, "explosion"));
		explosions.add(new Bullet(new Vector2f((float)(bullets.get(index).getPos().x + 
				(Math.random()*0.2f)-0.1f), (float)(bullets.get(index).getPos().y + (Math.random()*0.08f)-0.04f)), 0.0f, 70, "explosion"));
		explosions.add(new Bullet(new Vector2f((float)(bullets.get(index).getPos().x + 
				(Math.random()*0.2f)-0.1f), (float)(bullets.get(index).getPos().y + (Math.random()*0.08f)-0.04f)), 0.0f, 70, "explosion"));
	}
	public void setMovement(int movement) {
		if(this.movement != movement) {
			this.inposition = false;
			currenttex = 0;
		}
		this.movement = movement;
	}
	public void render(ShaderHandler sh, DisplaySetup d, DataUtils util) {
		if(animastage >= speed) {
			if(movement == 0) {
				if(currenttex<4) {
					changeTexture(currenttex);
					currenttex+=1;
				} else {
					changeTexture(currenttex);
					currenttex = 0;
				}
			} else if(movement == 2) {
				if(!inposition) {
					changeTexture(5 + currenttex);
					currenttex += 1;
					if(currenttex == 3) {
						inposition = true;
						currenttex = 0;
					}
				} else {
					if(currenttex < 6) {
						changeTexture(8 + currenttex);
						currenttex += 1;
					} else {
						changeTexture(8 + currenttex);
						currenttex = 0;
					}
				}
			} else {
				if(!inposition) {
					changeTexture(15 + currenttex);
					currenttex += 1;
					if(currenttex == 3) {
						inposition = true;
						currenttex = 0;
					}
				} else {
					if(currenttex < 4) {
						changeTexture(17 + currenttex);
						currenttex += 1;
					} else {
						changeTexture(17 + currenttex);
						currenttex = 0;
					}
				}
			}
			animastage = 0;
		} else {
			animastage += 1;
		}
		if(stage >= 35) {
			stage = 0;
		}
		stage += 1;
		bullet.changeTexture((stage/5));
		boolean changed = true;
		for(int i=0; i<bullets.size(); i++) {
			if(changed) {
				bullet.changeTexture((stage/5));
				changed = false;
			}
			boolean stopped = false;
			if(!bullets.get(i).getDestroying()) {
				bullets.get(i).setPos(new Vector2f((float)(bullets.get(i).getPos().x - (Math.sin(bullets.get(i).getRot())/20.0f)), 
					(float)(bullets.get(i).getPos().y - (Math.cos(bullets.get(i).getRot())/20.0f))));
				
				Vector4f bulletp = new Vector4f(bullets.get(i).getPos().x, bullets.get(i).getPos().y, 0.0f, 1.0f);
				Matrix4f.transform(d.getModelViewMatrixAsMatrix(), bulletp, bulletp);
				if(bulletp.x >= 1.1f || bulletp.x <= -1.1f || bulletp.y >= 1.1f || bulletp.y <= -1.1f) {
					bullets.remove(i);
					stopped = true;
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
			}
		}
		for(int i=0; i<explosions.size(); i++) {
			explosion.changeTexture((explosions.get(i).getAge()/5));
			explosions.get(i).age();
			explosion.changePos(explosions.get(i).getPos().x-epos.x, explosions.get(i).getPos().y-epos.y);
			epos = new Vector2f(explosions.get(i).getPos().x, explosions.get(i).getPos().y);
			explosion.render(sh, util, 0);
			if(explosions.get(i).getAge()>39) {
				explosions.remove(i);
				i-=1;
			}
		}
		if(show) {
			render(sh, util, hit);
		}
	}
	public void shoot() {
		SoundHandler.playSound(shootsound);
		bullets.add(new Bullet(new Vector2f(getPos().x + (getWidth()/(Display.getWidth()*2.0f)), getPos().y)
			, (float)Math.PI, 40, "normal"));
		bullets.add(new Bullet(new Vector2f(getPos().x + (getWidth()/(Display.getWidth()*2.0f)), getPos().y)
		, (float)(Math.PI + (Math.PI/25)), 40, "normal"));
		bullets.add(new Bullet(new Vector2f(getPos().x + (getWidth()/(Display.getWidth()*2.0f)), getPos().y)
		, (float)(Math.PI - (Math.PI/25)), 40, "normal"));
	}
	public ArrayList<Bullet> getBullets() {
		return bullets;
	}
}
