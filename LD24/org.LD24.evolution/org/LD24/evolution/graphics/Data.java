package org.LD24.evolution.graphics;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.LD24.evolution.GUI.LevelScreen;
import org.LD24.evolution.GUI.TitleScreen;
import org.LD24.evolution.Projectiles.PlayerBullet;
import org.LD24.evolution.enemies.Enemy;
import org.LD24.evolution.enemies.OnUpdate;
import org.LD24.evolution.enemies.Particle;
import org.LD24.evolution.shapes.ShapeMaker;
/**
 * Contains all public variables
 * @author harry
 *
 */
public class Data {

	public BufferedImage bufferimage = new BufferedImage(640,480,BufferedImage.TYPE_INT_ARGB);
	public Graphics bdgraphics = bufferimage.getGraphics();
	public BufferedImage bg;
	public BufferedImage icon;
	public boolean done;
	public int timespent;
	public int level = 1;
	public ShapeMaker shpmker = new ShapeMaker();
	public Runnable mytimer = new MyTimer(this);
	public Thread mytimerthread = new Thread(mytimer);
	public TitleScreen titlescreen;
	public LevelScreen levelscreen = new LevelScreen(level,this);
	public int stage;
	public int playerx = 320;
	public ArrayList<Particle> particles = new ArrayList<Particle>();
	public ArrayList<PlayerBullet> playerbullets = new ArrayList<PlayerBullet>();
	public ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	public ArrayList<BufferedImage> player = new ArrayList<BufferedImage>();
	public ArrayList<Integer> bulletstodelete = new ArrayList<Integer>();
	public OnUpdate onupdate = new OnUpdate();
	public boolean timerup = true;
	public boolean leftdown;
	public boolean rightdown;
	public boolean started = false;
	public int playerdir;
	public int left = KeyEvent.VK_LEFT;
	public int right = KeyEvent.VK_RIGHT;
	public int optleft = KeyEvent.VK_A;
	public int optright = KeyEvent.VK_D;
	public int startbutton = KeyEvent.VK_X;
	public int playerimage = 0;
	public int mousex;
	public int mousey;
	public boolean delete;
	
	public Data() {
		try {
			titlescreen = new TitleScreen(ImageIO.read(getClass().getResource("TitleScreen.png")),ImageIO.read(getClass().getResource("Startbutton.png")),this);
			for(int i=0;i<10;i++) {
				enemies.add(new Enemy(this));
			}
			icon = (ImageIO.read(getClass().getResource("Icon2.png")));
			bg = (ImageIO.read(getClass().getResource("backgroundLD24.png")));
			player.add(ImageIO.read(getClass().getResource("leftstationary.png")));
			player.add(ImageIO.read(getClass().getResource("leftstationarymousedown.png")));
			player.add(ImageIO.read(getClass().getResource("leftstationaryfire.png")));
			player.add(ImageIO.read(getClass().getResource("rightstationary.png")));
			player.add(ImageIO.read(getClass().getResource("rightstationarymousedown.png")));
			player.add(ImageIO.read(getClass().getResource("rightstationaryfire.png")));
		} catch (IOException e) {
			System.err.println("error : " + e.getLocalizedMessage());
			System.exit(1);
		}
	}

}
