package logic.entities.troops;

import java.awt.image.BufferedImage;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.util.vector.Vector2f;

import logic.GridParser;
import logic.entities.Bullet;
import logic.entities.Enemy;
import logic.entities.EnemyPath;
import logic.entities.Player;

import shader.ShaderHandler;
import start.Controller;
import start.DisplaySetup;
import texture.TextureHolder;
import utils.DataUtils;

public class Troop extends Enemy{
	public Troop(Vector2f pos, int texid, Controller parent, EnemyPath ep,
			Player player, ArrayList<Bullet> playerbullets,  TextureHolder[] ts,
			int lowesttexid, int highesttexid, int width, int pattern, int health, int shootspeed,
			int bullettexid, int explosiontexid, String movementtype, int animationstyle, int size) {
		super(pos, texid, parent, ep, player, playerbullets, lowesttexid,
				highesttexid, width, pattern, ts, health, shootspeed, 
				bullettexid, explosiontexid, movementtype, animationstyle, size);
	}
	public Enemy getEnemy() {
		return this;
	}
	public void shoot(DisplaySetup d) {
		this.fire(d);
	}
}
