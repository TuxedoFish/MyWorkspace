package logic.entities;

import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import images.ImageReturn;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import logic.GridParser;
import logic.entities.troops.SpriteHolder;
import logic.entities.troops.Troop;

import object.Sprite;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

import data.DataHandler;

import start.Controller;
import start.ControllerTimer;
import start.DisplaySetup;
import terrain.Block;
import terrain.LevelRenderer;
import terrain.Parser;
import texture.TextureHolder;

public class EnemyLoader extends Thread{
	private ArrayList<Troop> enemies = new ArrayList<Troop>();
	private ArrayList<Building> buildings = new ArrayList<Building>();
	private ArrayList<Block> blocks;
	
	private Boss boss;
	private boolean cached;
	private String level;
	private Controller parent;
	private Player player;
	private BufferedImage enemy;
	private boolean finished = false;
	private int lines;
	private ControllerTimer ct;
	private DisplaySetup display;
	private int highscore;
	private SpriteHolder spriteholder;
	
	public EnemyLoader(boolean cached, String level, Controller parent
			, BufferedImage enemy, ControllerTimer ct, DisplaySetup display, SpriteHolder spriteholder) {
		this.level = level;
		this.parent = parent;
		this.enemy = enemy;
		this.cached = cached;
		this.ct = ct;
		this.spriteholder = spriteholder;
		this.display = display;
		this.start();
	}
	public ArrayList<Troop> getEnemies() {
		return enemies;
	}
	public int getHighScore() {
		return highscore;
	}
	public ArrayList<Building> getBuildings() {
		return buildings;
	}
	public Player getPlayer() {
		return player;
	}
	public ArrayList<Block> getBlocks() {
		return blocks;
	}
	public Boss getBoss() {
		return boss;
	}
	@Override
	public void run() {
		ArrayList<Troop> allenemies = new ArrayList<Troop>();
		if(cached) {
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
			
			int levelno = Integer.parseInt((String)level.subSequence(5, level.length()));
			DataHandler dh = new DataHandler();
			ArrayList<String> scores = dh.read("highscores.txt");
			highscore = 0;
			
			for(int i=0; i<scores.size(); i++) {
				if(scores.get(i).startsWith(Integer.toString(levelno))) {
					String[] parts = scores.get(i).split(" ");
					if(Integer.parseInt(parts[1]) > highscore) {
						highscore = Integer.parseInt(parts[1]);
					}
				}
			}
			
			try {
				Parser parser = new Parser();
				blocks = parser.parseLevel(images.getImage(level + ".png"), 
						new Vector2f(-1.0f, -1.0f));
				GridParser gp = new GridParser();
				
				TextureHolder texture = gp.parseGrid(images.getImage("spaceship.png"), 50);
				player = new Player(images.getImage("spaceship.png"), 100, 100, texture, 0,
						new Vector2f(0.0f, -0.75f), parent);
				texture = gp.parseGrid(images.getImage("bosspart1.png"), 19);
				TextureHolder eye = gp.parseGrid(images.getImage("bosspart2.png"), 19);
				Sprite boss1 = new Sprite(images.getImage("bosspart1.png"), parent, 100, 100, texture, 0, new Vector2f(0.5f, 8.75f));
				Sprite boss2 = new Sprite(images.getImage("bosspart1.png"), parent, 100, 100, texture, 0, new Vector2f(-0.5f, 8.75f));
				Sprite boss3 = new Sprite(images.getImage("bosspart2.png"), parent, 50, 50, eye, 0, new Vector2f(-0.4f, 8.9f));
				Sprite boss4 = new Sprite(images.getImage("bosspart2.png"), parent, 50, 50, eye, 0, new Vector2f(0.0f, 8.9f));
				Sprite boss5 = new Sprite(images.getImage("bosspart2.png"), parent, 50, 50, eye, 0, new Vector2f(0.4f, 8.9f));
				Sprite boss6 = new Sprite(images.getImage("bosspart1.png"), parent, 800, 800, texture, 0, new Vector2f(-0.5f, 9.7f));
				
//				Vector2f p = new Vector2f(-1.0f, 0.0f);
//				BufferedImage img = images.getImage("trees.png");
//				PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("foo.out")));
//				
//				for(int i=0; i<img.getWidth()-1; i++) {
//					for(int j=0; j<img.getHeight()-1; j++) {
//						if(new Color(img.getRGB(i, j)).equals(new Color(26, 128, 23))) {
//							 out.write("tree.png " + ((p.x)+((50.0f/Display.getWidth())*i)) + " " +  ((p.y)+(50.0f/Display.getHeight())*j) + " 200 " + "200");
//							 out.println();
//						}
//					}
//				}
//				
//				out.close();
				reader2 = images.getFile("buildings/" + level + ".txt");
				String line = null;
				
				while((line = reader2.readLine()) != null) {
					String[] parts = line.split(" ");
					
					buildings.add(new Building(images.getImage(parts[0]), player.getBullets(), parent, Integer.valueOf(parts[3]),
							Integer.valueOf(parts[4]), new Vector2f(Float.valueOf(parts[1]), Float.valueOf(parts[2])), 35, 
							gp.parseGrid(images.getImage(parts[0]), Integer.valueOf(parts[5]), Integer.valueOf(parts[6])), 
							Boolean.valueOf(parts[7])));
				}
				
				reader2.close();
				
				boss = new Boss(new Vector2f(0.0f, 0.0f), 0, parent, null, player, player.getBullets());
				boss.addSprite(boss6, 0, 3, 1000, 1, false, ct.addTimeStep(200), new int[]{ct.addTimeStep(2000), ct.addTimeStep(100)}, 
						new int[]{1, 4});
				boss.addSprite(boss1, 0, 3, 50, 1, true, ct.addTimeStep(200), new int[]{ct.addTimeStep(2000), ct.addTimeStep(100)}, 
						new int[]{1, 4});
				boss.addSprite(boss2, 0, 3, 50, 1, true, ct.addTimeStep(200), new int[]{ct.addTimeStep(2000), ct.addTimeStep(100)}, 
						new int[]{1, 4});
				boss.addSprite(boss3, 0, 6, 10, 1, true, ct.addTimeStep(200), new int[]{ct.addTimeStep(2000), ct.addTimeStep(100)}, 
						new int[]{1, 4});
				boss.addSprite(boss4, 0, 6, 10, 1, true, ct.addTimeStep(200), new int[]{ct.addTimeStep(2000), ct.addTimeStep(100)}, 
						new int[]{1, 4});
				boss.addSprite(boss5, 0, 6, 10, 1, true, ct.addTimeStep(200), new int[]{ct.addTimeStep(2000), ct.addTimeStep(100)}, 
						new int[]{1, 4});
			} catch (IOException e1) {
				System.err.println("err loading img");
				System.exit(1);
				e1.printStackTrace();
			}
			try {
				reader2 = images.getFile("enemies/" + level + ".txt");
				String line = null;
				
				Class<?> troop = null;
				troop = Class.forName("logic.entities.troops.Troop");
				
				Constructor<?> troopcon = null;
				try {
					troopcon = troop.getConstructor(Vector2f.class, int.class, Controller.class, EnemyPath.class,
							Player.class, ArrayList.class,  TextureHolder[].class,
							int.class, int.class, int.class, int.class, int.class, int.class, int.class, int.class,
							String.class, int.class, int.class, ArrayList.class); 
				} catch (NoSuchMethodException | SecurityException e1) {
					e1.printStackTrace();
				}
				
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
					int size = Integer.valueOf(reader.readLine());
					
					line2 = "";
					ArrayList<Gun> guns = new ArrayList<Gun>();
					ArrayList<Boolean> visible = new ArrayList<Boolean>();
					while((line2=reader.readLine())!= null && line2.startsWith("gun")) {
						String[] data = line2.split(" ");
						guns.add(new Gun(spriteholder.getImage(data[1]), parent, Integer.valueOf(data[2]), Integer.valueOf(data[3]), 
								spriteholder.getTexture(data[1]), 0, new Vector2f(0.0f, 0.0f), pattern, player));
						visible.add(Boolean.valueOf(data[4]));
					}
					
					String movementtype = line2;
					int animationtype = Integer.valueOf(reader.readLine());
					
					
					int id = 0;
					ArrayList<Polygon> polygons = new ArrayList<Polygon>();
					ArrayList<Vector2f> points = new ArrayList<Vector2f>();
					
					while((line2=reader.readLine())!= null && line2.startsWith("collision")) {
						String line3 = null;
						String[] data = line2.split(" ");
						points.add(new Vector2f(Float.valueOf(data[1]), Float.valueOf(data[2])));
						while((line3=reader.readLine())!= null && line3.endsWith(Integer.toString(id))) {
							data = line3.split(" ");
							points.add(new Vector2f(Float.valueOf(data[1]), Float.valueOf(data[2])));
						}
						id += 1;
						polygons.add(new Polygon());
						for(int i=0; i<points.size(); i++) {
							polygons.get(polygons.size()-1).addPoint((int)points.get(i).x, (int)points.get(i).y);
						}
						points.clear();
					}
					reader.close();
					try {
						allenemies.add((Troop) troopcon.newInstance(new Vector2f(Float.valueOf(parts[1]), Float.valueOf(parts[2])), 
								spriteholder.getTextureID(texloc), parent, ep, player, 
								player.getBullets(), new TextureHolder[]{spriteholder.getTexture("explosion"), 
							spriteholder.getTexture("bullets"), spriteholder.getTexture(texloc)}, 
								lti, hti, width, pattern, health, shootspeed, spriteholder.getTextureID("bullets"), 
								spriteholder.getTextureID("explosion"), movementtype, animationtype, size, polygons));
						for(int i=0; i<guns.size(); i++) {
							allenemies.get(allenemies.size()-1).addGun(guns.get(i), visible.get(i));
						}
					} catch (SecurityException | InstantiationException | IllegalAccessException | 
							IllegalArgumentException | 
							InvocationTargetException e) {
						System.err.println("err finding class / constructor");
						e.printStackTrace();
					}
				}
				reader2.close();
			} catch (IOException e) {
				System.err.println("err getting img");
				System.exit(1);
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
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
							//allenemies.add(new Enemy(new Vector2f(i*25, (enemy.getHeight()*25)-(j*25)), 0, parent, 
									//ep, player, bullets, texture, lti, hti, width, pattern, null, health, shootspeed));
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
