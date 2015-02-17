package start;

import images.ImageReturn;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import logic.GridMaker;
import logic.GridParser;
import logic.LevelLoader;
import logic.entities.Building;
import logic.entities.Enemy;
import logic.entities.Bullet;
import logic.entities.EnemyLoader;
import logic.entities.Player;
import logic.entities.ScoreHandler;
import logic.entities.ScorePellet;
import logic.entities.Spawner;
import logic.entities.boss.Boss;
import logic.entities.boss.BossType;
import logic.entities.troops.SpriteHolder;
import logic.entities.troops.Troop;

import object.ColorHandler;
import object.ObjectLoader;
import object.Polygon;
import object.Shape;
import object.Sprite;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import data.DataHandler;

import physics.OctreeCollision;
import physics.Rectangle3D;

import shader.ShaderHandler;
import start.gui.Gui;
import start.gui.GuiButton;
import start.gui.GuiElementHandler;
import start.gui.GuiMarker;
import start.gui.GuiString;
import start.input.InputHandler;
import terrain.Block;
import terrain.LevelHolder;
import terrain.LevelRenderer;
import terrain.Parser;
import texture.TextureHandler;
import texture.TextureHolder;
import utils.DataUtils;
import utils.GenrealRenderer;
import utils.Line;
import utils.LineCollection;
import utils.MathUtils;
import utils.TextureUtils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL21.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL41.*;
import static org.lwjgl.opengl.GL42.*;
import static org.lwjgl.opengl.GL31.*;
import static org.lwjgl.opengl.GL32.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.util.glu.GLU.*;

public class Controller {
	private int testprogram;
	
	private GuiElementHandler gui;
	
	private float xthreshold;
	private float currentx = 0.0f;
	
	private int height=6400;
	private int prevhealth = 0;
	
	private boolean end = false;
	private boolean levelmap = false;
	
	private ArrayList<Spawner> spawners;
	
	private Player player;
	private BossType boss;
	private float levelheight;
	private EnemyLoader el;
	private boolean pausedcombat = false;
	private Sprite ultimatelevel;
	
	private int ultimateleveltex;
	
	private int highscore;
	private int highscoreid;
	
	private int texid;
	
	private boolean elements = false;
	
	private ArrayList<Integer> texids = new ArrayList<Integer>(); 
	private ArrayList<Float> stops = new ArrayList<Float>(); 
	private ArrayList<Color> colors = new ArrayList<Color>(); 
	private ArrayList<Block> blocks = new ArrayList<Block>();
	private ArrayList<Troop> enemies = new ArrayList<Troop>();
	private ArrayList<Building> buildings = new ArrayList<Building>();
	private ArrayList<Integer> scoretextids = new ArrayList<Integer>();
	private ArrayList<Integer> scorethreadids = new ArrayList<Integer>();
	private ArrayList<Vector2f> enemycollisionpoints = new ArrayList<Vector2f>();
	
	private boolean update = false;
	
	private EnemyAutoGenerator eag = new EnemyAutoGenerator(this);
	
	private Vector4f lastpoint = null;
	
	private ScoreHandler sch;
	private int score = 0;
	private int prevscore = 0;
	private Vector2f enemysize;
	
	private int healthid;
	private int scoreid;
	
	private int stage = 0;
	
	private int loadpercent = 0;
	private int prevpercent = 0;
	
	private InputHandler ih;
	
	private TextureHolder blocktex;
	private LevelHolder blockdata;
	private LevelRenderer lr;
	private Sprite currentlevel;
	private int level;
	
	private ShaderHandler shaderhandler;
	
	private boolean started = false;
	
	private DataHandler dh = new DataHandler();
	
	private ControllerTimer ct;
	
	private ColorHandler ch = new ColorHandler(); private GenrealRenderer gh; 
	private ObjectLoader ol = new ObjectLoader();
	
	private DisplaySetup display;
	
	private int playerthread;
	private int playershootthreadid;

	private ArrayList<GuiMarker> markers = new ArrayList<GuiMarker>();
	
	private LineCollection enemypolygonslines;
	private LineCollection enemypathlines;

	private ArrayList<Vector2f> enemypathpoints = new ArrayList<Vector2f>();
	private ArrayList<Integer> enemycollisiontexids = new ArrayList<Integer>();
	private int enemytexid;
	
	private Sprite enemybg;

	private boolean finished = true;
	
	public void bulletexplode(int index) {
		player.bulletexplode(index);
	}
	public void addColor(Color c) {
		this.texids.add(ch.newCol(c, colors));
	}
	public void resetThread(int index) {
		ct.resetTimeStep(index);
	}
	public void startup(String level) {
		if(stage == 0) {
			display.changepos(-display.getPos().x, -display.getPos().y, 0.0f);
			levelmap = false;
			elements = false;
			gui.clearElements();
			ih.clearElements();
			gui.newString("loading : 0", Color.red, 100, 20, new Vector2f(0.1f, 0.95f));
			SpriteHolder sph = new SpriteHolder(); ImageReturn images = new ImageReturn();
			try {
				sph.addTexture(images.getImage("Enemy2.png"), "Enemy2.png", 49);
				sph.addTexture(images.getImage("Enemy3.png"), "Enemy3.png", 49);
				sph.addTexture(images.getImage("Spinninglaser.png"), "Spinninglaser.png", 49);
				sph.addTexture(images.getImage("cyborg.png"), "cyborg.png", 32);
				sph.addTexture(images.getImage("missile.png"), "missile.png", 32);
				sph.addTexture(images.getImage("alientank.png"), "alientank.png", 59);
				sph.addTexture(images.getImage("SegaExplosions.png"), "explosion", 100);
				sph.addTexture(images.getImage("bullets2.png"), "bullets", 19);
				sph.addTexture(images.getImage("alientankgun.png"), "alientankgun.png", 29);
				sph.addTexture(images.getImage("gun1.png"), "gun1.png", 20);
				sph.addTexture(images.getImage("missileheads.png"), "missileheads.png", 99, 59);
				sph.addTexture(images.getImage("samurai.png"), "samurai.png", 99, 199);
				sph.addTexture(images.getImage("ship.png"), "ship.png", 99, 199);
			} catch (IOException e) {
				e.printStackTrace();
			}
			sph.finish();
			
			GridParser gp = new GridParser();
			try {
				blocktex = gp.parseGrid(images.getImage("LAND2.png"), 50);
			} catch (IOException e) {
				System.out.println("u done fucked up");
				e.printStackTrace();
			}
			blocktex.finish();
			
			el = new EnemyLoader(true, level, this, null, ct, display, sph, blocktex);
		}
	}
	public void startupfinish() throws IOException {
		enemies = el.getEnemies();
		currentx = 0;
		
		for(int i=0; i<enemies.size(); i++) {
			IntBuffer vboids = BufferUtils.createIntBuffer(3+enemies.get(i).getEnemy().getAmountOfGuns());
			glGenBuffers(vboids);
			IntBuffer vaoids = BufferUtils.createIntBuffer(3+enemies.get(i).getEnemy().getAmountOfGuns());
			glGenVertexArrays(vaoids);
			
			int[] threadids = new int[enemies.get(i).getShootSpeeds().size()+1];
			if(threadids.length==1){
				threadids = new int[2];
				threadids[0]=ct.addTimeStep(200);
				threadids[0]=ct.addTimeStep(enemies.get(i).getShootSpeed());
			} else {
				threadids[0]=ct.addTimeStep(200);
				
				for(int j=1; j<threadids.length; j++) {
					threadids[j] = ct.addTimeStep(enemies.get(i).getShootSpeeds().get(j-1)); 
				}
			}
			
			enemies.get(i).finish(vboids, vaoids, threadids);
		}
		
		blocks = el.getBlocks();
		
		playershootthreadid = ct.addTimeStep(200);
		player = el.getPlayer();
		IntBuffer vboids = BufferUtils.createIntBuffer(3);
		glGenBuffers(vboids);
		IntBuffer vaoids = BufferUtils.createIntBuffer(3);
		glGenVertexArrays(vaoids);
		player.finish(vboids, vaoids);
		
		boss = el.getBoss();
		vboids = BufferUtils.createIntBuffer(2+boss.getAmountOfParts());
		glGenBuffers(vboids);
		vaoids = BufferUtils.createIntBuffer(2+boss.getAmountOfParts());
		glGenVertexArrays(vaoids);
		boss.finish(vboids, vaoids);
		
		stops = el.getStops();
		
		buildings = el.getBuildings();
		for(int i=0; i<buildings.size(); i++) {
			buildings.get(i).finish(glGenBuffers(), glGenVertexArrays(), ct.addTimeStep(200));
		}
		
		sch = new ScoreHandler(this, player);
		sch.finish(glGenBuffers(), glGenVertexArrays());
	
		ImageReturn images = new ImageReturn();
		TextureUtils util = new TextureUtils();
		
		levelheight = (float) ((height) / (Display.getHeight()/2)-1.6);
		lr = new LevelRenderer(ct.addTimeStep(600));
		blockdata = lr.getLevelData(blocks, blocktex);
		lr.update(blocks, display);
		
		elements = false;
		gui = new GuiElementHandler();
		ih.clearElements();
		
		spawners = el.getSpawners();
		for(int i=0; i<spawners.size(); i++) {
			IntBuffer vboids2 = BufferUtils.createIntBuffer(spawners.get(i).getBuffersNeeded());
			glGenBuffers(vboids2);
			IntBuffer vaoids2 = BufferUtils.createIntBuffer(spawners.get(i).getBuffersNeeded());
			glGenVertexArrays(vaoids2);
			
			spawners.get(i).setTiming(ct.addTimeStep(spawners.get(i).getTiming()));
			spawners.get(i).setBuffers(vboids2, vaoids2);
		}
		
		prevhealth = player.getHealth();
		prevscore = score;
		scoreid = gui.newString("score : " + score, Color.red, 100, 50, new Vector2f(0.1f, 0.95f));
		highscoreid = gui.newString("highscore : " + el.getHighScore(), Color.red, 100, 50, new Vector2f(0.1f, 0.85f));
		elements = true;
		highscore = el.getHighScore();
		
		started = true;
	}
	public ArrayList<Integer> getColorsID() {
		return texids;
	}
	public ArrayList<Color> getColors() {
		return colors;
	}
	public void loadupdate(int percent) {
		loadpercent += percent;
	}
	public void addScorePellet(Vector2f pos, int score) {
		sch.add(new ScorePellet(score, pos, 0));
	}
	public void start() {
		ImageReturn images = new ImageReturn();
		
		ContextAttribs contextAtrributes = new ContextAttribs(3, 2);
		contextAtrributes.withProfileCompatibility(false);
		contextAtrributes.withProfileCore(true);
		
		display = new DisplaySetup(640, 480, 1.0f, 1000f, contextAtrributes);
		gh = new GenrealRenderer();
		
		GridMaker gm = new GridMaker();
		gm.makeGrid(190, 180, 3);
		
		shaderhandler = new ShaderHandler();
		setupshaders(shaderhandler);
		
		Sprite map = null;
		
		try {
			map = new Sprite(images.getImage("map.png"), this, 3000, 1500, 
					new GridParser().parseGrid(images.getImage("map.png"), 1429, 831), 0, new Vector2f(-1500.0f/Display.getWidth(), 
							750.0f/Display.getHeight()));
			map.finish(glGenBuffers(), glGenVertexArrays());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		ih = new InputHandler(this, map);
		
		ct = new ControllerTimer(this);
		playerthread = ct.addTimeStep(200);
		
		ColorHandler ch = new ColorHandler();
		texids.add(ch.newCol(Color.BLUE, colors));
		TextureUtils util = new TextureUtils();
		int titlescreen = 0;
		int loadscreen = 0;
		
		try {
			titlescreen = util.binddata(images.getImage("title2.png"));
			loadscreen = util.binddata(images.getImage("loadscreen.png"));
			
			//NEEDS TO BE MOVED TO LOADING
			
			ultimateleveltex = util.binddata(images.getImage("level1draft.png").getSubimage(0, 0, 1600, 13000));
		} catch (IOException e) {
			System.err.println("err finding title screen img"); e.printStackTrace();
		}
		int bgvbo = glGenBuffers();
		int bgvao = glGenVertexArrays();
		DataUtils utils = new DataUtils();
		IntBuffer bgindicesb = utils.getScreemnIndices();
		FloatBuffer bgdatafb = utils.getScreen(new Vector2f(0.0f, 1.0f), 640, 480);
		/*
		 * This is important because it is the way we work out how to draw the level based on length
		 * NEEDS TO BE MOVED TO LOADING SO IT ISNT SLOW AT START
		 */
		height = 6400;
		int width = 800;
		FloatBuffer leveldatafb = utils.getScreen(new Vector2f(0.0f, (height/240.0f)-1f), width, 6400);
		int levelvbo = glGenBuffers();
		int levelvao = glGenVertexArrays();
		xthreshold = ((width-645)/2.0f) /(Display.getWidth()/2.0f);
		
		glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		
		gui = new GuiElementHandler();
		gui.newButton("button2", new Vector2f(-0.45f, -0.75f), 200.0f, 50.0f, ih, this, "start");
		gui.newString("Click To Play", Color.RED, 200.0f, 50.0f, new Vector2f(-0.3f, -0.85f));
		elements = true;
		
		DataUtils dutils = new DataUtils();
		GenrealRenderer gr = new GenrealRenderer();
		
		while(!Display.isCloseRequested()) {
			if(stage == 0) {
				FloatBuffer matrix = BufferUtils.createFloatBuffer(16);
				Matrix4f mat = new Matrix4f(); mat.store(matrix); matrix.flip();
				if(started) {
					utils.begin(shaderhandler, display);
					utils.setup(leveldatafb, levelvbo, levelvao, shaderhandler, ultimateleveltex, 2, bgindicesb, matrix, 0);
					glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
					rendergl(shaderhandler, display);
					ih.update(display);
				} else {
					if(el != null && el.getFinished()) {
						try {
							this.startupfinish();
						} catch (IOException e) {
							System.err.println("failed to startup");
							e.printStackTrace();
						}
					}
					if(el != null && prevpercent != loadpercent) {
						if(blocks.size() == 0) {
							prevpercent = loadpercent;
							elements = false;
							gui.clearElements();
							ih.clearElements();
							gui.newBar("bar", new Vector2f(-0.5f, 0.95f), loadpercent);
							gui.newString("loading : " + loadpercent, Color.red, 100, 20, new Vector2f(0.1f, 0.95f));
						}
					}
					
					utils.begin(shaderhandler, display);
					if(!levelmap) {
						if(el!=null) {
							utils.setup(bgdatafb, bgvbo, bgvao, shaderhandler, loadscreen, 1, bgindicesb, matrix, 0);
						} else {
							utils.setup(bgdatafb, bgvbo, bgvao, shaderhandler, titlescreen, 1, bgindicesb, matrix, 0);
						}
						glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
					} else {
						map.render(shaderhandler, dutils, 0);
					}
					gui.drawElements(shaderhandler);
					ih.update(display);
				}
			} else {
				if(stage == 1) {
					utils.begin(shaderhandler, display);
					ih.update(display);
					if(!finished) {
						enemybg.finish(glGenBuffers(), glGenVertexArrays());
						finished = true;
					}
					enemybg.render(shaderhandler, utils, 0);
					gr.renderLineCollection(enemypolygonslines, shaderhandler, display, this);
				} else {
					utils.begin(shaderhandler, display);
					ih.update(display);
					gr.renderLineCollection(enemypathlines, shaderhandler, display, this);
				}
			}
			Display.update();
			Display.sync(60);
		}
		ct.interrupt();
	}
	public boolean isLevelMap() {
		return levelmap;
	}
	public void action(String message) {
		if(message.equals("start")) {
			levelmap = true;
			ih.atLevelMap(true);
			this.levelselectscreen();
		}
		if(message.contains("level")) {
			levelmap = false;
			ih.atLevelMap(false);
			level=Integer.valueOf(message.substring(message.length()-1, message.length()));
			this.startup("level" + level);
			for(int i=0; i<markers.size(); i++) {
				markers.get(i).finish();
			}
		}
	}
	public void levelselectscreen() {
		elements = false;
		gui.clearElements();
		ih.clearElements();
		markers.add(gui.newMarker("marker", new Vector2f(0.5f, 0.0f), 20, 20, ih, this, "level 1", ct.addTimeStep(100), 4));
		markers.add(gui.newMarker("marker", new Vector2f(0.0f, 0.0f), 20, 20, ih, this, "level 2", ct.addTimeStep(100), 4));
		markers.add(gui.newMarker("marker", new Vector2f(-0.5f, 0.5f), 20, 20, ih, this, "level 3", ct.addTimeStep(100), 4));
		elements = true;
	}
	public void update(int update, int index) {
		if(update == 200) {
			if(playerthread == index && player != null) {
				player.resetBlinking();
			} else {
				if(started) {
					for(int i=0; i<enemies.size(); i++) {
						if(enemies.get(i).getThreadID() == index) {
							enemies.get(i).resetBlinking();
						}
					}
					for(int j=0; j<spawners.size(); j++) {
						for(int i=0; i<spawners.get(j).getEnemies().size(); i++) {
							if(spawners.get(j).getEnemies().get(i).getThreadID() == index) {
								spawners.get(j).getEnemies().get(i).resetBlinking();
							}
						}
					}
					ArrayList<Integer> bossthreadids = boss.getThreadIDs();
					if(bossthreadids.contains(index)) {
						boss.resetBlinking(index);
					}
					for(int i=0; i<buildings.size(); i++) {
						if(buildings.get(i).getThreadID() == index) {
							buildings.get(i).resetBlinking();
						}
					}
				}
			}
		}
		if(boss != null && index == boss.getUpdateThreadID()) {
			boss.update();
		}
		for(int i=0; i<markers.size(); i++) {
			if(markers.get(i).getAnimationThreadID() == index) {
				markers.get(i).animate();
			}
		}
		if(spawners != null) {
			for(int i=0; i<spawners.size(); i++) {
				if(spawners.get(i).getThreadID() == index) {
					int[] threadids = new int[enemies.get(i).getShootSpeeds().size()+1];
					if(threadids.length==1){
						threadids = new int[2];
						threadids[0]=ct.addTimeStep(200);
						threadids[0]=ct.addTimeStep(enemies.get(i).getShootSpeed());
					 } else {
						threadids[0]=ct.addTimeStep(200);
						
						for(int j=1; j<threadids.length-1; j++) {
							threadids[i] = ct.addTimeStep(enemies.get(i).getShootSpeeds().get(i)); 
						}
					}
					spawners.get(i).update(this, player, threadids);
				}
			}
		}
		if(index == playershootthreadid) {
			ih.shoot();
		}
		if(lr != null && index == lr.getThreadID()) {
			lr.animate(blocktex, blockdata);
		}
		for(int i=0; i<enemies.size(); i++) {
			if(enemies.get(i).getShootThreadID()!=null) {
				for(int j=0; j<enemies.get(i).getShootThreadID().length; j++) {
					if(enemies.get(i).getShootThreadID()[j] == index) {
						if(started) {
							enemies.get(i).shoot(display, index);
						}
					}
				}
			}
		}
//		if(started) {
//			ArrayList<Integer> bossthreadids2 = boss.getShootThreadIDs();
//			if(bossthreadids2.contains(index)) {
//				boss.shoot(index);
//			}
//		}
		if(boss != null) {
			boss.shoot(index);
		}
		for(int i=0; i<scorethreadids.size(); i++) {
			if(scorethreadids.get(i) == index) {
				if(started) {
					gui.removeElement(scoretextids.get(i));
					scoretextids.remove(i);
					scorethreadids.remove(i);
				}
			}
		}
	}
	public int getPlayerShootThreadId() {
		return playershootthreadid;
	}
	public void damage(int d) {
		ct.resetTimeStep(playerthread);
		player.damage(d);
	}
	public int getThreadDuration(int threadid) {
		return ct.getThreadDuration(threadid);
	}
	public void move(float x, float y) {
		if(started) {
			if(Math.abs(currentx) < xthreshold) {
				currentx += x;
				display.changepos(-x, 0.0f, 0.0f);
				player.move(x, y, xthreshold, display);
			} else {
				if((player.getPos().x<0.0f+(xthreshold) && currentx>0.0f) ||
						(player.getPos().x>0.0f-(xthreshold) && currentx<0.0f)) {
					currentx += x;
				}
				player.move(x, y, xthreshold, display);
			}
		}
	}
	public void toHome() {
		started = false;
		el = null;
		prevpercent = 0; loadpercent = 0;
		elements = false;
		gui = new GuiElementHandler();
		gui.newButton("button2", new Vector2f(-0.45f, -0.75f), 200.0f, 50.0f, ih, this, "start");
		gui.newString("Click To Play", Color.BLACK, 200.0f, 50.0f, new Vector2f(-0.3f, -0.82f));
		elements = true;
		display.changepos(0.0f, -display.getPos().y, 0.0f);
		player.reset();
		dh.update("highscores.txt", new String[] {Integer.toString(level)+" "+score});
		score = 0;
		scoretextids.clear();
		scorethreadids.clear();
		enemies.clear();
	}
	public void shoot() {
		if(started) {
			player.shoot();
		}
	}
	public void rendergl(ShaderHandler sh, DisplaySetup d) {
		DataUtils util = new DataUtils();
		for(int i=0; i<stops.size(); i++) {
			if(d.getPos().y*-480 > stops.get(i)) {
				pausedcombat = true;
			}
		}
		for(int i=0; i<spawners.size(); i++) { 
			spawners.get(i).render(sh, d, util);
		}
		if(end) {
			end = false;
			toHome();
		}
		if((d.getPos().y + 0.25f > levelheight*-1)) {
			if(!pausedcombat) {
				display.changepos(0.0f, -0.01f, 0.0f);
				player.changePos(0.0f, 0.01f);
				for(int i=0; i<enemies.size(); i++) {
					enemies.get(i).scroll();
				}
			} else {
				boolean stay=true;
				for(int i=0; i<enemies.size(); i++) {
					if(!enemies.get(i).isDead()) {
						if(enemies.get(i).getPos().y<(display.getPos().y*-1)+1.0f &&
								enemies.get(i).getPos().y>(display.getPos().y*-1)-1.0f) {
							stay=false;
						}
					}
				}
				if(stay==true) {
					stops.remove(stops.size()-1);
					pausedcombat=false;
				}
			}
		}
		//ultimatelevel.render(sh, util, 0);
		//lr.render(blockdata, sh, blocktex, blocks, d);
		for(int i=0; i<buildings.size(); i++) {
			buildings.get(i).render(sh, d, util);
		}
		boss.render(sh, d, util);
		
		player.render(sh, d, util);
		player.setMovement(ih.getMovement());
		
		for(int i=0; i<enemies.size(); i++) {
			enemies.get(i).update(shaderhandler, display, util);
		}
		if(prevscore != score && started) {
			prevscore = score;
			gui.removeElement(scoreid);
			scoreid = gui.newString("score : " + score, Color.red, 100, 50, new Vector2f(0.1f, 0.95f));
			if(score > highscore) {
				highscore = score;
				gui.removeElement(highscoreid);
				highscoreid = gui.newString("highscore : " + highscore, Color.red, 100, 50, new Vector2f(0.1f, 0.85f));
			}
		}
		sch.render(sh, display, util);
		gui.drawElements(sh);
	}
	public ShaderHandler getSh() {
		return shaderhandler;
	}
	public boolean isElements() {
		return elements;
	}
	public void score(int score) {
		this.score += score;

		Vector4f realplayerpos = new Vector4f(player.getPos().x, player.getPos().y, 0.0f, 1.0f);
		Matrix4f.transform(display.getModelViewMatrixAsMatrix(), realplayerpos, realplayerpos);

		scoretextids.add(gui.newString(Integer.toString(score), Color.BLUE, 50, 50, new Vector2f(realplayerpos.x, realplayerpos.y)));
		scorethreadids.add(ct.addTimeStep(500));
	}
	public void setupshaders(ShaderHandler s) {
		testprogram = s.createprogram();
		try {
			s.addshader(testprogram, new BufferedReader(new InputStreamReader((getClass().getResourceAsStream("testshader.vert")), "UTF-8")),ARBVertexShader.GL_VERTEX_SHADER_ARB);
			s.addshader(testprogram, new BufferedReader(new InputStreamReader((getClass().getResourceAsStream("testshader.frag")), "UTF-8")),ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);
		} catch (UnsupportedEncodingException e) {
			System.err.println("err loading shaders");
			System.exit(1);
			e.printStackTrace();
		}
		 s.finishprogram(testprogram);
	}
	public int addString(String str, Vector2f pos) {
		return gui.newStringAtPos(str, Color.RED, pos);
	}
	public void removeElement(Integer index) {
		gui.removeElement(index);
	}
	public void removeThread(Integer index) {
		ct.removeTimeStep(index);
	}
	public void addLine(float mousex, float mousey) {
		if(stage == 1) {
			if(lastpoint != null) {
				enemypolygonslines.addLine(new Line(lastpoint, new Vector4f(mousex, mousey, 0.0f, 1.0f)));
			} else {
				enemypolygonslines.addLine(new Line(new Vector4f(mousex, mousey, 0.0f, 1.0f), new Vector4f(mousex, mousey, 0.0f, 1.0f)));
			}
			lastpoint = new Vector4f(mousex, mousey, 0.0f, 1.0f);
			enemycollisionpoints.add(new Vector2f(mousex, mousey));
			enemycollisiontexids.add(enemytexid);
		} else if(stage == 2) {
			if(lastpoint != null) {
				enemypathlines.addLine(new Line(lastpoint, new Vector4f(mousex, mousey, 0.0f, 1.0f)));
			} else {
				enemypathlines.addLine(new Line(new Vector4f(mousex, mousey, 0.0f, 1.0f), new Vector4f(mousex, mousey, 0.0f, 1.0f)));
			}
			lastpoint = new Vector4f(mousex, mousey, 0.0f, 1.0f);
			enemypathpoints .add(new Vector2f(mousex, mousey));
		}
	}
	public void setStage(int stage) {
		this.stage = stage;
	}
	public int getStage() {
		return stage;
	}
	public void resetPolygonLines() {
		enemypolygonslines = new LineCollection();
	}
	public void nextStage() {
		update = true;
	}
	public void resetPathPoints() {
		enemypathlines = new LineCollection();
		lastpoint = null;
	}
	public ArrayList<Vector2f> getEnemyCollisionPoints() {
		return enemycollisionpoints;
	}
	public ArrayList<Vector2f> getEnemyPathPoints() {
		return enemypathpoints;
	}
	public ArrayList<Integer> getEnemyCollisonTexids() {
		return enemycollisiontexids;
	}
	public void changeEnemyBg(String enemytexloc, int enemytexid, Vector2f enemysize) {
		ImageReturn images = new ImageReturn(); GridParser gp = new GridParser();
		try {
			this.enemytexid = enemytexid;
			enemybg = new Sprite(images.getImage(enemytexloc), this, 1280, 960, gp.parseGrid(images.getImage(enemytexloc), enemysize.x, enemysize.y), 
					enemytexid, new Vector2f(-1.0f, 1.0f));
			finished  = false;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void copy(int texid) {
		for(int i=0; i<enemycollisiontexids.size(); i++) {
			if(enemycollisiontexids.get(i) == texid) {
				enemycollisionpoints.add(enemycollisionpoints.get(i));
				enemycollisiontexids.add(texid+1);
			}
		}
	}
	public void doneUpdate() {
		update = false;
	}
	public boolean isUpdate() {
		return update;
	}
	public void end() {
		end = true;
	}
}
