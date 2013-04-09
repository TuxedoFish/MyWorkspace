package logic.enemies.troops;

import java.util.ArrayList;

import object.Sprite;

import org.lwjgl.util.vector.Vector2f;

import start.Controller;
import texture.TextureHolder;
import logic.enemies.Enemy;
import logic.enemies.EnemyBullet;
import logic.enemies.EnemyPath;

public class Fly extends Enemy{

	public Fly(Vector2f pos, int texid, Controller parent, EnemyPath ep,
			Sprite player, ArrayList<EnemyBullet> playerbullets, String texloc,
			int lowesttexid, int highesttexid, int width, int pattern,
			TextureHolder[] ts, int health, int shootspeed) {
		super(pos, texid, parent, ep, player, playerbullets, texloc, lowesttexid,
				highesttexid, width, pattern, ts, health, shootspeed);
	}

}
