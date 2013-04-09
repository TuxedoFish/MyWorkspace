package logic.enemies;

import images.ImageReturn;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import logic.enemies.troops.Fly;

import object.Sprite;

import org.lwjgl.util.vector.Vector2f;

import start.Controller;
import texture.TextureHolder;

public class EnemyLoader extends Thread{
	private ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	private boolean cached;
	private String level;
	private Controller parent;
	private Sprite player;
	private ArrayList<EnemyBullet> bullets;
	private BufferedImage enemy;
	private boolean finished = false;
	private int lines;
	
	public EnemyLoader(boolean cached, String level, Controller parent, Sprite player, ArrayList<EnemyBullet> bullets
			, BufferedImage enemy) {
		this.level = level;
		this.parent = parent;
		this.player = player;
		this.bullets = bullets;
		this.enemy = enemy;
		this.cached = cached;
		this.start();
	}
	public ArrayList<Enemy> getEnemies() {
		return enemies;
	}
	@Override
	public void run() {
		ArrayList<Enemy> allenemies = new ArrayList<Enemy>();
		if(cached) {
			ArrayList<String> keys = new ArrayList<String>();
			ArrayList<TextureHolder[]> textures = new ArrayList<TextureHolder[]>();
			
			ImageReturn images = new ImageReturn();
			BufferedReader reader2;
			BufferedReader readertest;
			try {
				readertest = images.getFile("enemies/" + level + ".txt");
				String linetest = null;
				while((linetest=readertest.readLine()) != null) {lines+=1;}
			} catch (IOException e1) {
				System.err.println("err at EnemyLoader");
			}
			
			try {
				reader2 = images.getFile("enemies/" + level + ".txt");
				String line = null;
				while((line=reader2.readLine()) != null) {
					parent.loadupdate(100/lines);
					String[] parts = line.split(" ");
					BufferedReader reader = images.getFile("enemies/" + parts[0] + ".txt");
					String line2 = "";
					EnemyPath ep = new EnemyPath();
					while((line2=reader.readLine()).startsWith("ep")) {
						String[] data = line2.split(" ");
						ep.addPoint(new PathPoint(new Vector2f(Float.valueOf(data[1]), Float.valueOf(data[2])), 
								Integer.valueOf(data[3])));
					}
					String texloc = line2;
					int lti = Integer.valueOf(reader.readLine());
					int hti = Integer.valueOf(reader.readLine());
					int width = Integer.valueOf(reader.readLine());
					int pattern = Integer.valueOf(reader.readLine());
					int health = Integer.valueOf(reader.readLine());
					int shootspeed = Integer.valueOf(reader.readLine());
					
					if(!keys.contains(parts[0])) {
						allenemies.add(new Enemy(new Vector2f(Float.valueOf(parts[1]), Float.valueOf(parts[2])), 0, parent, ep, player, 
								bullets, texloc, lti, hti, width, pattern, null, health, shootspeed));
						keys.add(parts[0]);
						textures.add(allenemies.get(allenemies.size()-1).getTextures());
					} else {
						allenemies.add(new Enemy(new Vector2f(Float.valueOf(parts[1]), Float.valueOf(parts[2])), 0, parent, ep, player, 
								bullets, texloc, lti, hti, width, pattern, textures.get(keys.indexOf(parts[0])), health, shootspeed));
					}
				}
			} catch (IOException e) {
				System.err.println("err getting img");
				System.exit(1);
				e.printStackTrace();
			}
		} else {
			ImageReturn images = new ImageReturn();
			try {
				File f = new File(images.getImageLoc("") + "\\enemies");
				f.mkdir();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			PrintWriter writer2 = images.getWriter("enemies/" + "level1.txt");
			ArrayList<Color> colors = new ArrayList<Color>();
			ArrayList<Integer> names = new ArrayList<Integer>();
			int name = 0;
			
			for(int i=0; i<enemy.getWidth(); i++) {
				for(int j=0; j<enemy.getHeight(); j++) {
					Color c = new Color(enemy.getRGB(i, j));
					if(c.getRed() != 0 || c.getGreen() != 0 || c.getBlue() != 0) {
						try {
							BufferedImage path = images.getImage(c.getRed() + " " + c.getGreen() + " " + 
									c.getBlue() + ".png");
							BufferedReader reader = images.getFile(c.getRed() + " " + c.getGreen() + " " + 
									c.getBlue() + ".txt");
							PrintWriter writer = null;
							if(!colors.contains(c)) {
								writer = images.getWriter("enemies/" + name + ".txt");
							}
							String texture = reader.readLine();
							int lti = Integer.parseInt(reader.readLine());
							int hti = Integer.parseInt(reader.readLine());
							int width = Integer.parseInt(reader.readLine());
							int pattern = Integer.parseInt(reader.readLine());
							int health = Integer.parseInt(reader.readLine());
							int shootspeed = Integer.parseInt(reader.readLine());
							
							EnemyPath ep = new EnemyPath();
							for(int k=0; k<path.getWidth(); k++) {
								for(int l=0; l<path.getHeight(); l++) {
									Color c2 = new Color(path.getRGB(k, l));
									if(c2.getRed() != 255 || c2.getGreen() != 255 || c2.getBlue() != 255) {
										ep.addPoint(new PathPoint(new Vector2f(k, l), c2.getRed()/10));
										if(!colors.contains(c)) {
											writer.write("ep " + k + " " + l + " " + c2.getRed()/10); writer.println();
										}
									}
								}
							}
							if(!colors.contains(c)) {
								writer.println(texture);
								writer.println(lti);
								writer.println(hti);
								writer.println(width);
								writer.println(pattern);
								writer.println(health);
								writer.println(shootspeed);
								writer.close();
							}
							allenemies.add(new Enemy(new Vector2f(i*25, (enemy.getHeight()*25)-(j*25)), 0, parent, 
									ep, player, bullets, texture, lti, hti, width, pattern, null, health, shootspeed));
							if(!colors.contains(c)) {
								writer2.println(name + " " + (i*25) + " " + ((enemy.getHeight()*25)-(j*25)));
								names.add(name);
								colors.add(c);
								name += 1;
							} else {
								writer2.println(names.get(colors.indexOf(c)) + " " + (i*25) + " " + ((enemy.getHeight()*25)-(j*25)));
								names.add(name);
								colors.add(c);
							}
						} catch (IOException e) {
							System.err.println("err getting img");
							System.exit(1);
							e.printStackTrace();
						}
					}
				}
			}
			writer2.close();
		}
		this.enemies = allenemies;
		this.finished = true;
	}
	public boolean getFinished() {
		return finished;
	}
}
