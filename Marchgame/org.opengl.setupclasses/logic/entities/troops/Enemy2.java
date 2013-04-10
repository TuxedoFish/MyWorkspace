package logic.entities.troops;

import images.ImageReturn;

import java.io.IOException;
import java.util.ArrayList;

import object.Sprite;

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
import logic.entities.EnemyBullet;
import logic.entities.EnemyPath;

public class Enemy2 extends Enemy implements Troop{
	public Enemy2(Vector2f pos, int texid, Controller parent, EnemyPath ep,
			Sprite player, ArrayList<EnemyBullet> playerbullets, String texloc,
			int lowesttexid, int highesttexid, int width, int pattern,
			TextureHolder[] ts, int health, int shootspeed, ImageReturn images) throws IOException {
		super(pos, texid, parent, ep, player, playerbullets, images.getImage(texloc), lowesttexid,
				highesttexid, width, pattern, ts, health, shootspeed);
	}
	public Enemy2(Vector2f pos, int texid, Controller parent, EnemyPath ep,
			Sprite player, ArrayList<EnemyBullet> playerbullets, String texloc,
			int lowesttexid, int highesttexid, int width, int pattern, int health, 
			int shootspeed, ImageReturn images) throws IOException {
		super(pos, texid, parent, ep, player, playerbullets, images.getImage(texloc), lowesttexid,
				highesttexid, width, pattern, new GridParser(), health, shootspeed);
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
