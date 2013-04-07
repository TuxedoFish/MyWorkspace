package logic.enemies;

import images.ImageReturn;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

import logic.GridParser;

import object.Sprite;
import shader.ShaderHandler;
import start.Controller;
import start.DisplaySetup;
import texture.TextureHolder;
import utils.DataUtils;

public class Building {
	private ArrayList<EnemyBullet> playerbullets;
	private Sprite me;
	private int hit;
	private Controller parent;
	private BufferedImage img;
	private int threadid;
	private int health;
	private boolean stopped = false;
	
	public Building(String loc, ArrayList<EnemyBullet> playerbullets, Controller parent, int width, 
			int height, Vector2f pos, int health) {
		this.playerbullets = playerbullets;
		this.parent = parent;
		this.health = health;
		ImageReturn images = new ImageReturn();
		GridParser gp = new GridParser();
		try {
			img = images.getImage(loc);
		} catch (IOException e) {
			System.err.println("err loading building");
			e.printStackTrace();
		}
		
		TextureHolder texture = gp.parseGrid(img, img.getWidth(), img.getHeight());
		me = new Sprite(img, parent, width, height, texture, 0, pos);
	}
	public void finish(int vboid, int vaoid, int threadid) {
		me.finish(vboid, vaoid);
		this.threadid = threadid;
	}
	public int getThreadID() {
		return threadid;
	}
	public void resetBlinking() {
		hit = 0;
	}
	public void render(ShaderHandler sh, DisplaySetup d, DataUtils util) {
		if(!stopped) {
			for(int i=0; i<playerbullets.size(); i++) {
				if(playerbullets.get(i).contains(me.getPos(), (float)me.getWidth()/Display.getWidth(), 
						(float)me.getHeight()/Display.getHeight(), d) && !playerbullets.get(i).getDestroying()) {
					parent.bulletexplode(i);
					health -= 5;
					parent.resetThread(threadid);
					hit = 1;
					if(health <= 0) {
						stopped = true;
					}
				}
			}
			me.render(sh, util, hit);
		}
	}
}
