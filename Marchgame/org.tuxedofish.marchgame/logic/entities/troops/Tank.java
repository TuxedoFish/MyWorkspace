package logic.entities.troops;

import images.ImageReturn;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import logic.GridParser;
import logic.entities.Enemy;
import logic.entities.Bullet;
import logic.entities.EnemyPath;
import logic.entities.Gun;
import logic.entities.Player;
import object.Sprite;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

import shader.ShaderHandler;
import start.Controller;
import start.DisplaySetup;
import texture.TextureHolder;
import utils.DataUtils;

public class Tank extends Enemy implements Troop {
	public Tank(Vector2f pos, int texid, Controller parent, EnemyPath ep,
			Player player, ArrayList<Bullet> playerbullets, String texloc,
			int lowesttexid, int highesttexid, int width, int pattern,
			TextureHolder[] ts, int health, int shootspeed, ImageReturn images, String name, ByteBuffer imgdata) throws IOException {
		super(pos, texid, parent, ep, player, playerbullets, images.getImage(texloc), lowesttexid,
				highesttexid, width, pattern, ts, health, shootspeed, name, imgdata);
		//setScroll(false);
		setAnimationSpeed(10);
		setAnimationStyle(3);
		
		GridParser gp = new GridParser();
		TextureHolder guntex = gp.parseGrid(images.getImage("gun1.png"), 20.0f);
		addGun(new Gun(images.getImage("gun1.png"), parent, 50, 50, guntex, 0, new Vector2f(0.0f, 0.0f), 
				getEnemy().getBulletHandler(), 2, player), true);
		//setPos((float)(-0.6f + (Math.random()*1.2f)), pos.y/(Display.getHeight()/2)-1.0f);
	}
	public Tank(Vector2f pos, int texid, Controller parent, EnemyPath ep,
			Player player, ArrayList<Bullet> playerbullets, String texloc,
			int lowesttexid, int highesttexid, int width, int pattern, int health, 
			int shootspeed, ImageReturn images, String name) throws IOException {
		super(pos, texid, parent, ep, player, playerbullets, images.getImage(texloc), lowesttexid,
				highesttexid, width, pattern, new GridParser(), health, shootspeed, name);
		//setScroll(false);
		setAnimationSpeed(10);
		setAnimationStyle(3);
		
		GridParser gp = new GridParser();
		TextureHolder guntex = gp.parseGrid(images.getImage("gun1.png"), 20.0f);
		addGun(new Gun(images.getImage("gun1.png"), parent, 50, 50, guntex, 0, new Vector2f(0.0f, 0.0f), 
				getEnemy().getBulletHandler(), 2, player), true);
		//setPos((float)(-0.6f + (Math.random()*1.2f)), pos.y/(Display.getHeight()/2)-1.0f);
	}
	@Override
	public Enemy getEnemy() {
		return this;
	}
	@Override
	public void shoot(DisplaySetup d) {
		this.fire(d);
	}
	@Override
	public void render(ShaderHandler sh, DisplaySetup d, DataUtils util) {
		update(sh, d, util);
		followPath(d);
	}
}
