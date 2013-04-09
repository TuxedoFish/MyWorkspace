package logic.entities.troops;

import images.ImageReturn;

import java.io.IOException;
import java.util.ArrayList;

import object.Sprite;

import org.lwjgl.util.vector.Vector2f;

import start.Controller;
import texture.TextureHolder;
import logic.GridParser;
import logic.entities.Enemy;
import logic.entities.EnemyBullet;
import logic.entities.EnemyPath;

public class Enemy2 extends Enemy{
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
}
