package logic.entities;

import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import images.ImageReturn;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.ArrayList;

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
	
	public Player(BufferedImage img, int width, int height,
			TextureHolder th, int currenttexid, Vector2f pos, Controller parent) {
		super(img, parent, width, height, th, currenttexid, pos);
		
		this.parent  = parent;
		GridParser gp = new GridParser(); ImageReturn images = new ImageReturn();

		try {
			TextureHolder explosiontex = gp.parseGrid(images.getImage("explosion.png"), 29);
			this.explosion = new Sprite(images.getImage("explosion.png"), parent, 70, 70, explosiontex, 
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
		hit = 0;
	}
	public void damage(int d) {
		hit = 1;
		health -= d;
	}
	public void move(float x, float y, DisplaySetup display) {
		if(!(getPos().x + (getWidth()/Display.getWidth()) + x > 0.8f) || x <= 0) {
			if(!(getPos().x - x < -0.95f) || x >= 0) {
				if(!(getPos().y + y > -display.getPos().y + 1.0f) || y <= 0) {
					if(!(getPos().y - (getHeight()/Display.getHeight()) + y < -display.getPos().y - 0.8f) || y >= 0) {
							changePos(x, y);
							changeTexture(5);
							//display.changepos(-x, -y, 0.0f);
					}
				}
			}
		}
	}
	public void bulletexplode(int index) {
		bullets.get(index).setDestroyingSelf(true);
		explosions.add(new Bullet(bullets.get(index).getPos(), 0.0f, 70));
		explosions.add(new Bullet(new Vector2f((float)(bullets.get(index).getPos().x + 
				(Math.random()*0.2f)-0.1f), (float)(bullets.get(index).getPos().y + (Math.random()*0.08f)-0.04f)), 0.0f, 70));
		explosions.add(new Bullet(new Vector2f((float)(bullets.get(index).getPos().x + 
				(Math.random()*0.2f)-0.1f), (float)(bullets.get(index).getPos().y + (Math.random()*0.08f)-0.04f)), 0.0f, 70));
	}
	public void setMovement(int movement) {
		this.movement = movement;
	}
	public void render(ShaderHandler sh, DisplaySetup d, DataUtils util) {
		if(currenttex<4) {
			changeTexture((movement*5)+currenttex);
			currenttex+=1;
		} else {
			changeTexture((movement*5)+currenttex);
			currenttex = 0;
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
			if(explosions.get(i).getAge()>24) {
				explosions.remove(i);
				i-=1;
			}
		}
		render(sh, util, hit);
	}
	public void shoot() {
		sounds.playSound("shoot.wav");
		bullets.add(new Bullet(new Vector2f(getPos().x + (getWidth()/(Display.getWidth()*2.0f)), getPos().y)
			, (float)Math.PI, 40));
		bullets.add(new Bullet(new Vector2f(getPos().x + (getWidth()/(Display.getWidth()*2.0f)), getPos().y)
		, (float)(Math.PI + (Math.PI/25)), 40));
		bullets.add(new Bullet(new Vector2f(getPos().x + (getWidth()/(Display.getWidth()*2.0f)), getPos().y)
		, (float)(Math.PI - (Math.PI/25)), 40));
	}
	public ArrayList<Bullet> getBullets() {
		return bullets;
	}
}
