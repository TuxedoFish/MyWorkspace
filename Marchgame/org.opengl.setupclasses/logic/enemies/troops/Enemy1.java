package logic.enemies.troops; 

import images.ImageReturn;

import java.io.IOException;
import java.util.ArrayList;

import object.Sprite;

import org.lwjgl.util.vector.Vector2f;

import start.Controller;
import texture.TextureHolder;
import logic.GridParser;
import logic.enemies.Enemy;
import logic.enemies.EnemyBullet;
import logic.enemies.EnemyPath;

public class Enemy1 extends Enemy{
	public Enemy1(Vector2f pos, int texid, Controller parent, EnemyPath ep,
			Sprite player, ArrayList<EnemyBullet> playerbullets, String texloc,
			int lowesttexid, int highesttexid, int width, int pattern,
			TextureHolder[] ts, int health, int shootspeed, ImageReturn images) throws IOException {
		super(pos, texid, parent, ep, player, playerbullets, images.getImage(texloc), lowesttexid,
				highesttexid, width, pattern, ts, health, shootspeed);
		System.out.println("i am enemy1");
	}
	public Enemy1(Vector2f pos, int texid, Controller parent, EnemyPath ep,
			Sprite player, ArrayList<EnemyBullet> playerbullets, String texloc,
			int lowesttexid, int highesttexid, int width, int pattern, int health, 
			int shootspeed, ImageReturn images) throws IOException {
		super(pos, texid, parent, ep, player, playerbullets, images.getImage(texloc), lowesttexid,
				highesttexid, width, pattern, new GridParser(), health, shootspeed);
		System.out.println("i am enemy1");
	}
}
