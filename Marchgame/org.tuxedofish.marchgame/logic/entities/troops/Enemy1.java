package logic.entities.troops; 

import images.ImageReturn;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import object.Sprite;

import org.lwjgl.util.vector.Vector2f;

import shader.ShaderHandler;
import start.Controller;
import start.DisplaySetup;
import texture.TextureHolder;
import utils.DataUtils;
import logic.GridParser;
import logic.entities.Enemy;
import logic.entities.Bullet;
import logic.entities.EnemyPath;
import logic.entities.Player;

public class Enemy1 extends Enemy implements Troop{
	public Enemy1(Vector2f pos, int texid, Controller parent, EnemyPath ep,
			Player player, ArrayList<Bullet> playerbullets, String texloc,
			int lowesttexid, int highesttexid, int width, int pattern,
			TextureHolder[] ts, int health, int shootspeed, ImageReturn images, String name, ByteBuffer imgdata) throws IOException {
		super(pos, texid, parent, ep, player, playerbullets, images.getImage(texloc), lowesttexid,
				highesttexid, width, pattern, ts, health, shootspeed, name, imgdata);
	}
	public Enemy1(Vector2f pos, int texid, Controller parent, EnemyPath ep,
			Player player, ArrayList<Bullet> playerbullets, String texloc,
			int lowesttexid, int highesttexid, int width, int pattern, int health, 
			int shootspeed, ImageReturn images, String name) throws IOException {
		super(pos, texid, parent, ep, player, playerbullets, images.getImage(texloc), lowesttexid,
				highesttexid, width, pattern, new GridParser(), health, shootspeed, name);
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
	}
}
