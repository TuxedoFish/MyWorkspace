package logic.entities.troops;

import images.ImageReturn;

import java.io.IOException;
import java.util.ArrayList;

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
import logic.GridParser;
import logic.entities.Enemy;
import logic.entities.Bullet;
import logic.entities.EnemyPath;
import logic.entities.Gun;
import logic.entities.Player;

public class Enemy3 extends Enemy implements Troop{
	public Enemy3(Vector2f pos, int texid, Controller parent, EnemyPath ep,
			Player player, ArrayList<Bullet> playerbullets, String texloc,
			int lowesttexid, int highesttexid, int width, int pattern,
			TextureHolder[] ts, int health, int shootspeed, ImageReturn images) throws IOException {
		super(pos, texid, parent, ep, player, playerbullets, images.getImage(texloc), lowesttexid,
				highesttexid, width, pattern, ts, health, shootspeed);
		setScroll(false);
		setPos((float)(-0.6f + (Math.random()*1.2f)), pos.y/(Display.getHeight()/2)-1.0f);
		
		GridParser gp = new GridParser();
		TextureHolder guntex = gp.parseGrid(images.getImage("gun1.png"), 20.0f);
		addGun(new Gun(images.getImage("gun1.png"), parent, 50, 50, guntex, 0, new Vector2f(0.0f, 0.0f), 
				getEnemy().getBulletHandler(), 2, player), false);
	}
	public Enemy3(Vector2f pos, int texid, Controller parent, EnemyPath ep,
			Player player, ArrayList<Bullet> playerbullets, String texloc,
			int lowesttexid, int highesttexid, int width, int pattern, int health, 
			int shootspeed, ImageReturn images) throws IOException {
		super(pos, texid, parent, ep, player, playerbullets, images.getImage(texloc), lowesttexid,
				highesttexid, width, pattern, new GridParser(), health, shootspeed);
		setScroll(false);
		setPos((float)(-0.6f + (Math.random()*1.2f)), pos.y/(Display.getHeight()/2)-1.0f);
		
		GridParser gp = new GridParser();
		TextureHolder guntex = gp.parseGrid(images.getImage("gun1.png"), 20.0f);
		addGun(new Gun(images.getImage("gun1.png"), parent, 50, 50, guntex, 0, new Vector2f(0.0f, 0.0f), 
				getEnemy().getBulletHandler(), 2, player), false);
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
		swirlAround();
	}
}