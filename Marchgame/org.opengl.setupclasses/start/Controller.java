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
import java.io.UnsupportedEncodingException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import logic.GridMaker;
import logic.GridParser;
import logic.LevelLoader;
import logic.entities.Boss;
import logic.entities.Building;
import logic.entities.Enemy;
import logic.entities.Bullet;
import logic.entities.EnemyLoader;
import logic.entities.Player;
import logic.entities.ScoreHandler;
import logic.entities.ScorePellet;
import logic.entities.troops.Troop;

import object.ColorHandler;
import object.ObjectLoader;
import object.Polygon;
import object.Shape;
import object.Sprite;

import org.lwjgl.BufferUtils;
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

import physics.OctreeCollision;
import physics.Rectangle3D;

import shader.ShaderHandler;
import start.gui.Gui;
import start.gui.GuiButton;
import start.gui.GuiElementHandler;
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
	
	private int prevhealth = 0;
	
	private Player player;
	private Boss boss;
	private int levelheight;
	private EnemyLoader el;
	
	private ArrayList<Integer> texids = new ArrayList<Integer>(); 
	private ArrayList<Color> colors = new ArrayList<Color>(); 
	private ArrayList<Block> blocks = new ArrayList<Block>();
	private ArrayList<Troop> enemies = new ArrayList<Troop>();
	private ArrayList<Building> buildings = new ArrayList<Building>();
	
	private ScoreHandler sch;
	private int score = 0;
	private int prevscore = 0;
	
	private int healthid;
	private int scoreid;
	
	private int loadpercent = 0;
	private int prevpercent = 0;
	
	private InputHandler ih;
	
	private TextureHolder blocktex;
	private LevelHolder blockdata;
	private LevelRenderer lr;
	private Sprite currentlevel;
	
	private ShaderHandler shaderhandler;
	
	private boolean started = false;
	
	private ControllerTimer ct;
	
	private ColorHandler ch = new ColorHandler(); private GenrealRenderer gh; 
	private ObjectLoader ol = new ObjectLoader();
	
	private DisplaySetup display;
	
	private int playerthread;
	
	public void bulletexplode(int index) {
		player.bulletexplode(index);
	}
	public void addColor(Color c) {
		this.texids.add(ch.newCol(c, colors));
	}
	public void resetThread(int index) {
		ct.resetTimeStep(index);
	}
	public void startup() {
		gui.clearElements();
		ih.clearElements();
		gui.newString("loading : 0", Color.red, 100, 20, new Vector2f(0.1f, 0.95f));
		el = new EnemyLoader(true, "level1", this, null, ct, display);
	}
	public void startupfinish() throws IOException {
		enemies = el.getEnemies();
		for(int i=0; i<enemies.size(); i++) {
			IntBuffer vboids = BufferUtils.createIntBuffer(3);
			glGenBuffers(vboids);
			IntBuffer vaoids = BufferUtils.createIntBuffer(3);
			glGenVertexArrays(vaoids);
			enemies.get(i).getEnemy().finish(vboids, vaoids, new int[]{ct.addTimeStep(200), 
					ct.addTimeStep(enemies.get(i).getEnemy().getShootSpeed())});
		}
		blocks = el.getBlocks();
		
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
		
		buildings = el.getBuildings();
		for(int i=0; i<buildings.size(); i++) {
			buildings.get(i).finish(glGenBuffers(), glGenVertexArrays(), ct.addTimeStep(200));
		}
		
		sch = new ScoreHandler(this, player);
		sch.finish(glGenBuffers(), glGenVertexArrays());
		
		GridParser gp = new GridParser(); ImageReturn images = new ImageReturn();
		blocktex = gp.parseGrid(images.getImage("LAND2.png"), 50);
		blocktex.finish();
		levelheight = (images.getImage("LEVEL1.png").getHeight() * 50) / (Display.getHeight())-2;
		lr = new LevelRenderer(ct.addTimeStep(600));
		blockdata = lr.getLevelData(blocks, blocktex);
		lr.update(blocks, display);
		
		prevhealth = player.getHealth();
		prevscore = score;
		gui.clearElements();
		ih.clearElements();
		scoreid = gui.newString("score : " + score, Color.red, 100, 50, new Vector2f(0.1f, 0.95f));
		healthid = gui.newBar("bar", new Vector2f(0.1f, 0.75f), (int)(((float)player.getHealth()/500.0f)*100.0f));
		
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
		gm.makeGrid(21, 10);
		
		shaderhandler = new ShaderHandler();
		setupshaders(shaderhandler);
		
		ct = new ControllerTimer(this);
		playerthread = ct.addTimeStep(200);
		
		ih = new InputHandler(this);
		
		ColorHandler ch = new ColorHandler();
		texids.add(ch.newCol(Color.BLUE, colors));
		TextureUtils util = new TextureUtils();
		int titlescreen = 0;
		
		try {
			titlescreen = util.binddata(images.getImage("title.png"));
		} catch (IOException e) {
			System.err.println("err finding title screen img"); e.printStackTrace();
		}
		int bgvbo = glGenBuffers();
		int bgvao = glGenVertexArrays();
		DataUtils utils = new DataUtils();
		IntBuffer bgindicesb = utils.getScreemnIndices();
		FloatBuffer bgdatafb = utils.getScreen(new Vector2f(0.0f, 0.0f));
		
		glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		
		gui = new GuiElementHandler();
		gui.newButton("button", new Vector2f(-0.45f, -0.75f), 200.0f, 50.0f, ih, this, "start");
		gui.newString("Click To Play", Color.BLACK, 200.0f, 50.0f, new Vector2f(-0.3f, -0.82f));
		
		while(!Display.isCloseRequested()) {
			if(started) {
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
					prevpercent = loadpercent;
					gui.clearElements();
					ih.clearElements();
					gui.newBar("bar", new Vector2f(-0.5f, 0.95f), loadpercent);
					gui.newString("loading : " + loadpercent, Color.red, 100, 20, new Vector2f(0.1f, 0.95f));
				}
				FloatBuffer matrix = BufferUtils.createFloatBuffer(16);
				Matrix4f mat = new Matrix4f(); mat.store(matrix); matrix.flip();
				
				utils.begin(shaderhandler, display);
				utils.setup(bgdatafb, bgvbo, bgvao, shaderhandler, titlescreen, 1, bgindicesb, matrix, 0);
				glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
				gui.drawElements(shaderhandler);
				ih.update(display);
			}
			Display.update();
			Display.sync(60);
		}
		ct.interrupt();
	}
	public void action(String message) {
		if(message.equals("start")) {
			this.levelselectscreen();
		}
		if(message.equals("levelselected")) {
			this.startup();
		}
	}
	public void levelselectscreen() {
		gui.clearElements();
		ih.clearElements();
		gui.newButton("button", new Vector2f(-1.0f + (100.0f/(float)Display.getWidth()*2), 1.0f - (20.0f/(float)Display.getHeight()*2)), 
				100.0f, 20.0f, ih, this, "levelselected");
		gui.newString("level1", Color.red, 100, 20, 
				new Vector2f(-1.0f + (100.0f/(float)Display.getWidth()*2), 1.0f - (20.0f/(float)Display.getHeight()*2)));
			
	}
	public void update(int update, int index) {
		if(update == 200) {
			if(playerthread == index && player != null) {
				player.resetBlinking();
			} else {
				if(started) {
					for(int i=0; i<enemies.size(); i++) {
						if(enemies.get(i).getEnemy().getThreadID() == index) {
							enemies.get(i).getEnemy().resetBlinking();
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
		if(lr != null && index == lr.getThreadID()) {
			lr.animate(blocktex, blockdata);
		}
		for(int i=0; i<enemies.size(); i++) {
			if(enemies.get(i).getEnemy().getShootThreadID() == index) {
				if(started) {
					enemies.get(i).shoot(display);
				}
			}
		}
		if(started) {
			ArrayList<Integer> bossthreadids2 = boss.getShootThreadIDs();
			if(bossthreadids2.contains(index)) {
				boss.shoot(index);
			}
		}
	}
	public void damage(int d) {
		ct.resetTimeStep(playerthread);
		player.damage(d);
	}
	public void move(float x, float y) {
		if(started) {
			player.move(x, y, display);
		}
	}
	public void toHome() {
		el = null;
		prevpercent = 0; loadpercent = 0;
		started = false;
		gui = new GuiElementHandler();
		gui.newButton("button", new Vector2f(-0.45f, -0.75f), 200.0f, 50.0f, ih, this, "start");
		gui.newString("Click To Play", Color.BLACK, 200.0f, 50.0f, new Vector2f(-0.3f, -0.82f));
		display.changepos(0.0f, -display.getPos().y, 0.0f);
		player.reset();
		score = 0;
	}
	public void shoot() {
		if(started) {
			player.shoot();
		}
	}
	public void rendergl(ShaderHandler sh, DisplaySetup d) {
		DataUtils util = new DataUtils();
		util.begin(sh, d);
		
		if((d.getPos().y + 0.25f > levelheight*-1)) {
			display.changepos(0.0f, -0.005f, 0.0f);
			player.changePos(0.0f, 0.005f);
			for(int i=0; i<enemies.size(); i++) {
				enemies.get(i).getEnemy().scroll();
			}
		}
		lr.render(blockdata, sh, blocktex, blocks, d);
		for(int i=0; i<buildings.size(); i++) {
			buildings.get(i).render(sh, d, util);
		}
		boss.render(sh, d, util);
		
		player.render(sh, d, util);
		
		for(int i=0; i<enemies.size(); i++) {
			enemies.get(i).render(shaderhandler, display, util);
		}
		if(prevhealth != player.getHealth()) {
			prevhealth = player.getHealth();
			gui.removeElement(healthid);
			healthid = gui.newBar("bar", new Vector2f(0.1f, 0.75f), (int)(((float)player.getHealth()/500.0f)*100.0f));
		}
		if(prevscore != score) {
			prevscore = score;
			gui.removeElement(scoreid);
			scoreid = gui.newString("score : " + score, Color.red, 100, 50, new Vector2f(0.1f, 0.95f));
		}
		sch.render(sh, display, util);
		gui.drawElements(sh);
	}
	public ShaderHandler getSh() {
		return shaderhandler;
	}
	public void score(int score) {
		this.score += score;
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
	public void removeThread(Integer index) {
		ct.removeTimeStep(index);
	}
}
