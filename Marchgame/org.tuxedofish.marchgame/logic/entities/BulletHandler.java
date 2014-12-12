package logic.entities;

import images.ImageReturn;

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
import start.Controller;
import start.DisplaySetup;
import texture.TextureHolder;
import utils.DataUtils;

public class BulletHandler extends ArrayList<Bullet>{
	private Sprite bullet; private Sprite explosion;
	private Controller parent;
	private int textstage = 0;
	private Vector2f bpos = new Vector2f(0.0f, 0.0f); private Vector2f epos = new Vector2f(0.0f, 0.0f);
	public ArrayList<Bullet> explosions = new ArrayList<Bullet>();
	private Player player;
	
	public BulletHandler(TextureHolder bullettex, TextureHolder explosiontex, String bulletname, Controller parent, Player player) {
		ImageReturn images = new ImageReturn();
		this.parent = parent;
		this.player = player;
		try {
			bullet = new Sprite(images.getImage(bulletname + ".png"), parent, 40, 40, bullettex, 0, new Vector2f(0.0f, 0.0f));
			explosion = new Sprite(images.getImage("SegaExplosions.png"), parent, 100, 100, explosiontex, 0, new Vector2f(0.0f, 0.0f));
		} catch (IOException e) {
			System.err.println("err in BulletHandler");
			e.printStackTrace();
		}
	}
	public BulletHandler(TextureHolder bullettex, TextureHolder explosiontex, Controller parent, Player player, 
			int bullettexid, int explosiontexid) {
		this.parent = parent;
		this.player = player;
		
		bullet = new Sprite(parent, 40, 40, bullettex, 0, new Vector2f(0.0f, 0.0f), bullettexid);
		explosion = new Sprite(parent, 100, 100, explosiontex, 0, new Vector2f(0.0f, 0.0f), explosiontexid);
	}
	public void finishwithouttex(IntBuffer vboids, IntBuffer vaoids) {
		this.explosion.finishwithouttex(vboids.get(0), vaoids.get(0));
		this.bullet.finishwithouttex(vboids.get(1), vaoids.get(1));
	}
	public void finish(IntBuffer vboids, IntBuffer vaoids) {
		this.explosion.finish(vboids.get(0), vaoids.get(0));
		this.bullet.finish(vboids.get(1), vaoids.get(1));
	}
	public void finishwithouttex(int[] vbo, int[] vao) {
		this.explosion.finishwithouttex(vbo[0], vao[0]);
		this.bullet.finishwithouttex(vbo[1], vao[1]);
	}
	public void addExplosion(Bullet explosion) {
		explosions.add(explosion);
	}
	public void render(ShaderHandler sh, DisplaySetup d, DataUtils util) {
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
		if(textstage >= 35) {
			textstage = 0;
		}
		textstage += 1;
		boolean changed = true;
		for(int i=0; i<size(); i++) {
			if(!get(i).getType().contains("laser")) {
				if(changed) {
					bullet.changeTexture((textstage/5));
					changed = false;
				}
				boolean stopped = false;
				if(!get(i).getDestroying()) {
					get(i).setPos(new Vector2f((float)(get(i).getPos().x - (Math.cos(get(i).getRot())/100.0f)), 
						(float)(get(i).getPos().y - (Math.sin(get(i).getRot())/100.0f))));
					Vector4f bulletp = new Vector4f(get(i).getPos().x, get(i).getPos().y, 0.0f, 1.0f);
					Matrix4f.transform(d.getModelViewMatrixAsMatrix(), bulletp, bulletp);
					if(bulletp.x >= 1.1f || bulletp.x <= -1.1f || bulletp.y >= 1.1f || bulletp.y <= -1.1f) {
						remove(i);
						stopped = true;
					}
				} else {
					if(get(i).getAge() < get(i).getLastAge()+20) {
						bullet.changeTexture(8 + ((get(i).getAge()-get(i).getLastAge())/5));
						changed = true;
					} else {
						stopped = true;
						remove(i);
						i-=1;
					}
				}
				if(!stopped) {
					get(i).age();
					bullet.changePos(get(i).getPos().x-bpos.x, get(i).getPos().y-bpos.y);
					bpos = new Vector2f(get(i).getPos().x, get(i).getPos().y);
					bullet.render(sh, util, 0);
					if(!get(i).getDestroying() && player.getHit() == 0) {
						if(get(i).contains(new Vector2f(player.getPos().x+toCoordsWidth(20), player.getPos().y-toCoordsHeight(20)), (float)14/Display.getWidth(), (float)6/Display.getHeight(), d)) {
							get(i).setDestroyingSelf(true);
							explosions.add(new Bullet(get(i).getPos(), 0.0f, 70, "explosion"));
							explosions.add(new Bullet(new Vector2f((float)(get(i).getPos().x + (Math.random()*0.2f)-0.1f), (float)(get(i).getPos().y + (Math.random()*0.08f)-0.04f)), 0.0f, 70, "explosion"));
							explosions.add(new Bullet(new Vector2f((float)(get(i).getPos().x + (Math.random()*0.2f)-0.1f), (float)(get(i).getPos().y + (Math.random()*0.08f)-0.04f)), 0.0f, 70, "explosion"));
							parent.damage(5);
						}
					}
				}
			} else {
				bullet.changePos(get(i).getPos().x-bpos.x, get(i).getPos().y-bpos.y);
				bpos = new Vector2f(get(i).getPos().x, get(i).getPos().y);
				bullet.render(sh, util, 0);
				if(!get(i).getDestroying() && player.getHit() == 0) {
					if(get(i).contains(new Vector2f(player.getPos().x+toCoordsWidth(20), player.getPos().y-toCoordsHeight(20)), (float)14/Display.getWidth(), (float)6/Display.getHeight(), d)) {
						get(i).setDestroyingSelf(true);
						explosions.add(new Bullet(get(i).getPos(), 0.0f, 70, "explosion"));
						explosions.add(new Bullet(new Vector2f((float)(get(i).getPos().x + (Math.random()*0.2f)-0.1f), (float)(get(i).getPos().y + (Math.random()*0.08f)-0.04f)), 0.0f, 70, "explosion"));
						explosions.add(new Bullet(new Vector2f((float)(get(i).getPos().x + (Math.random()*0.2f)-0.1f), (float)(get(i).getPos().y + (Math.random()*0.08f)-0.04f)), 0.0f, 70, "explosion"));
						parent.damage(5);
					}
				}
			}
		}
	}
	public float toCoordsWidth(int i) {
		return (i*2.0f)/(Display.getWidth());
	}
	public float toCoordsHeight(int i) {
		return (i*2.0f)/(Display.getHeight());
	}
	public void shootlaser(Vector2f pos) {
		for(int i=0; i<20; i++) {
			add(new Bullet(new Vector2f(pos.x+0.03f, pos.y - 0.05f - i*0.08f), 0, 20, "laser"));
			bullet.changeTexture(12);
		}
	}
	public void stoplaser() {
		clear();
	}
}
