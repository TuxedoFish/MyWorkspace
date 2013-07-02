package logic.entities;

import images.ImageReturn;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

import logic.GridParser;

import object.Sprite;
import shader.ShaderHandler;
import sounds.SoundHandler;
import start.Controller;
import start.DisplaySetup;
import texture.TextureHolder;
import utils.DataUtils;

public class Building extends Sprite{
	private ArrayList<Bullet> playerbullets;
	private int hit;
	private Controller parent;
	private BufferedImage img;
	private int threadid;
	private int health;
	private boolean stopped = false;
	private boolean shoot = false;
	
	public Building(BufferedImage img, ArrayList<Bullet> playerbullets, Controller parent, int width, 
			int height, Vector2f pos, int health, TextureHolder texture, boolean shootable) {
		super(img, parent, width, height, texture, 0, pos);
		
		this.playerbullets = playerbullets;
		this.parent = parent;
		this.health = health;
		this.shoot = shootable;
	}
	public void finish(int vboid, int vaoid, int threadid) {
		finish(vboid, vaoid);
		this.threadid = threadid;
	}
	public int getThreadID() {
		return threadid;
	}
	public void resetBlinking() {
		hit = 0;
	}
	public void render(ShaderHandler sh, DisplaySetup d, DataUtils util) {
		if(!stopped && shoot) {
			for(int i=0; i<playerbullets.size(); i++) {
				if(playerbullets.get(i).contains(getPos(), (float)getWidth()/Display.getWidth(), 
						(float)getHeight()/Display.getHeight(), d) && !playerbullets.get(i).getDestroying()) {
					parent.bulletexplode(i);
					health -= 5;
					parent.resetThread(threadid);
					hit = 1;
					if(health <= 0) {
						for(int j=0; j<2; j++) {
							parent.addScorePellet(new Vector2f((float)(getPos().x + (Math.random()*((float)getWidth()/Display.getWidth()))), 
									(float)(getPos().y - (Math.random()*((float)getHeight()/Display.getHeight())))), 5);
						}
						changeTexture(1);
						stopped = true;
					}
				}
			}
		}
		render(sh, util, hit);
	}
}
