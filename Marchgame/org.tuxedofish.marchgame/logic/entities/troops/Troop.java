package logic.entities.troops;

import java.awt.Polygon;
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
			int lowesttexid, int highesttexid, String[] sizes, int pattern, int health, int shootspeed,
			int bullettexid, int explosiontexid, String movementtype, int animationstyle, ArrayList<Polygon> collision) {
		super(pos, texid, parent, ep, player, playerbullets, lowesttexid,
				highesttexid, sizes, pattern, ts, health, shootspeed, 
				bullettexid, explosiontexid, movementtype, animationstyle, collision);
	}
	public Enemy getEnemy() {
		return this;
	}
	public void shoot(DisplaySetup d, int index) {
		this.fire(d, index);
	}
}
