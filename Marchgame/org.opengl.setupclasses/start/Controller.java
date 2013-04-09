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
import logic.entities.EnemyBullet;
import logic.entities.EnemyLoader;
import logic.entities.Player;

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
	
	private float changex; private float changey; private float changez;
	private int prevhealth = 0;
	
	private Player player;
	private Boss boss;
	private int levelheight;
	private EnemyLoader el;
	
	private ArrayList<Integer> texids = new ArrayList<Integer>(); 
	private ArrayList<Color> colors = new ArrayList<Color>(); 
	private ArrayList<Block> blocks = new ArrayList<Block>();
	private ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	private ArrayList<Building> buildings = new ArrayList<Building>();
	
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
	
	public void mouseChangeUpdate(float changex, float changey, float changez) {
		if(changex != -1) {
			this.changex = changex;
		}
		if(changey != -1) {
			this.changey = changey;
		}
		if(changez != -1) {
			this.changez = changez;
		}
	}
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
		el = new EnemyLoader(true, "level1", this, player, player.getBullets(), null);
	}
	public void startupfinish() {
		enemies = el.getEnemies();
		for(int i=0; i<enemies.size(); i++) {
			IntBuffer vboids = BufferUtils.createIntBuffer(3);
			glGenBuffers(vboids);
			IntBuffer vaoids = BufferUtils.createIntBuffer(3);
			glGenVertexArrays(vaoids);
			enemies.get(i).finish(vboids, vaoids, new int[]{ct.addTimeStep(200), 
					ct.addTimeStep(enemies.get(i).getShootSpeed())});
		}
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
	public void start() {
		LineCollection lc = new LineCollection();
		Vector4f p1;
		Vector4f p2;
		Vector4f p3;
		Vector4f p4;
		
		float sizeofsquare = 1.0f;
		
		for(float z = -10; z < 0; z += sizeofsquare) {
			for(float x = -10; x < 10; x += sizeofsquare) {
				p1 = new Vector4f(x, -0.5f, z, 1.0f);
				p2 = new Vector4f(x + sizeofsquare, -0.5f, z, 1.0f);
				p3 = new Vector4f(x + sizeofsquare, -0.5f, z + sizeofsquare, 1.0f);
				p4 = new Vector4f(x, -0.5f, z + sizeofsquare, 1.0f);
				
				lc.addLine(new Line(p1, p2));
				lc.addLine(new Line(p2, p3));
				lc.addLine(new Line(p3, p4));
				lc.addLine(new Line(p4, p1));
			}
		}
		
		ImageReturn images = new ImageReturn();
		
		ContextAttribs contextAtrributes = new ContextAttribs(3, 2);
		contextAtrributes.withProfileCompatibility(false);
		contextAtrributes.withProfileCore(true);
		
		display = new DisplaySetup(640, 480, 1.0f, 1000f, contextAtrributes);
		gh = new GenrealRenderer();
		
		GridMaker gm = new GridMaker();
		//gm.makeGrid(51, 10);
		Parser parser = new Parser();
		shaderhandler = new ShaderHandler();
		setupshaders(shaderhandler);
		
		ct = new ControllerTimer(this);
		playerthread = ct.addTimeStep(200);
		
		try {
			blocks = parser.parseLevel(images.getImage("level1.png"), 
					new Vector2f(-1.0f, -1.0f));
			GridParser gp = new GridParser();
			blocktex = gp.parseGrid(images.getImage("LAND2.png"), 50);
			blocktex.finish();
			levelheight = (images.getImage("LEVEL1.png").getHeight() * 50) / (Display.getHeight())-2;
			lr = new LevelRenderer(ct.addTimeStep(600));
			blockdata = lr.getLevelData(blocks, blocktex);
			
			TextureHolder texture = gp.parseGrid(images.getImage("spaceship.png"), 29);
			player = new Player(images.getImage("spaceship.png"), this, 100, 100, texture, 0,
					new Vector2f(0.0f, -0.75f), this);
			this.player.finish(glGenBuffers(), glGenVertexArrays());
			texture = gp.parseGrid(images.getImage("bosspart1.png"), 19);
			TextureHolder eye = gp.parseGrid(images.getImage("bosspart2.png"), 19);
			Sprite boss1 = new Sprite(images.getImage("bosspart1.png"), this, 100, 100, texture, 0, new Vector2f(0.5f, 8.75f));
			Sprite boss2 = new Sprite(images.getImage("bosspart1.png"), this, 100, 100, texture, 0, new Vector2f(-0.5f, 8.75f));
			Sprite boss3 = new Sprite(images.getImage("bosspart2.png"), this, 50, 50, eye, 0, new Vector2f(-0.4f, 8.9f));
			Sprite boss4 = new Sprite(images.getImage("bosspart2.png"), this, 50, 50, eye, 0, new Vector2f(0.0f, 8.9f));
			Sprite boss5 = new Sprite(images.getImage("bosspart2.png"), this, 50, 50, eye, 0, new Vector2f(0.4f, 8.9f));
			Sprite boss6 = new Sprite(images.getImage("bosspart1.png"), this, 800, 800, texture, 0, new Vector2f(-0.5f, 9.7f));
			
			lr.update(blocks, display);
			Building b = new Building("building3.png", player.getBullets(), this, 340, 500, new Vector2f(-0.9f, 5.0f), 100, 167, 250);
			b.finish(glGenBuffers(), glGenVertexArrays(), ct.addTimeStep(200));
			Building b2 = new Building("building3.png", player.getBullets(), this, 340, 500, new Vector2f(-0.9f, 2.0f), 100, 167, 250);
			b2.finish(glGenBuffers(), glGenVertexArrays(), ct.addTimeStep(200));
			buildings.add(b); buildings.add(b2);
			
			boss = new Boss(new Vector2f(0.0f, 0.0f), 0, this, null, player, player.getBullets());
			boss.addSprite(boss6, 0, 3, 1000, 1, false, ct.addTimeStep(200), ct.addTimeStep(800));
			boss.addSprite(boss1, 0, 3, 50, 1, true, ct.addTimeStep(200), ct.addTimeStep(800));
			boss.addSprite(boss2, 0, 3, 50, 1, true, ct.addTimeStep(200), ct.addTimeStep(800));
			boss.addSprite(boss3, 0, 6, 10, 1, true, ct.addTimeStep(200), ct.addTimeStep(800));
			boss.addSprite(boss4, 0, 6, 10, 1, true, ct.addTimeStep(200), ct.addTimeStep(800));
			boss.addSprite(boss5, 0, 6, 10, 1, true, ct.addTimeStep(200), ct.addTimeStep(800));
			IntBuffer vboids = BufferUtils.createIntBuffer(2+boss.getAmountOfParts());
			glGenBuffers(vboids);
			IntBuffer vaoids = BufferUtils.createIntBuffer(2+boss.getAmountOfParts());
			glGenVertexArrays(vaoids);
			boss.finish(vboids, vaoids);
		} catch (IOException e1) {
			System.err.println("err loading img");
			System.exit(1);
			e1.printStackTrace();
		}
		ih = new InputHandler(this);
		
		ColorHandler ch = new ColorHandler();
		texids.add(ch.newCol(Color.BLUE, colors));
		TextureUtils util = new TextureUtils();
		int titlescreen = 0;
		
		try {
			titlescreen = util.binddata(images.getImage("title.png"));
		} catch (IOException e) {
			System.err.println("err finding title screen img");
			System.exit(1);
			e.printStackTrace();
		}
		int[] bgindices = {
				0, 1, 2,
				3, 4, 5
		};
		IntBuffer bgindicesb = BufferUtils.createIntBuffer(bgindices.length);
		bgindicesb.put(bgindices);
		bgindicesb.flip();
		int bgvbo = glGenBuffers();
		int bgvao = glGenVertexArrays();
		FloatBuffer bgdatafb = updateBg(new Vector2f(0.0f, 0.0f));
		
		glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		
		gui = new GuiElementHandler();
		gui.newButton("button", new Vector2f(-0.45f, -0.75f), 200.0f, 50.0f, ih, this, "start");
		gui.newString("Click To Play", Color.BLACK, 200.0f, 50.0f, new Vector2f(-0.3f, -0.82f));
		
		while(!Display.isCloseRequested()) {
			if(started) {
				rendergl(shaderhandler, display, lc);
				ih.update(display);
			} else {
				if(el != null && el.getFinished()) {
					this.startupfinish();
				}
				if(el != null && prevpercent != loadpercent) {
					prevpercent = loadpercent;
					gui.clearElements();
					ih.clearElements();
					gui.newBar("bar", new Vector2f(-0.5f, 0.95f), loadpercent);
					gui.newString("loading : " + loadpercent, Color.red, 100, 20, new Vector2f(0.1f, 0.95f));
				}
				glEnable(GL_BLEND);
				glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
				
				glViewport(0, 0, Display.getWidth(), Display.getHeight());
				//Clears display
				glClear(GL_COLOR_BUFFER_BIT |
					GL_DEPTH_BUFFER_BIT);
				glLoadIdentity();
				glPushMatrix();

				ARBShaderObjects.glUseProgramObjectARB(shaderhandler.getPrograms().get(0).getId());
				
				int viewmatrixloc = glGetUniformLocation(shaderhandler.getPrograms().get(0).getId(), "ViewMatrix");
				glUniformMatrix4(viewmatrixloc, false, display.getModelViewMatrix());
				
				int perspmatrixloc = glGetUniformLocation(shaderhandler.getPrograms().get(0).getId(), "PerspectiveMatrix");
				glUniformMatrix4(perspmatrixloc, false, display.getProjectionMatrix());
				
				FloatBuffer matrix = BufferUtils.createFloatBuffer(16);
				Matrix4f mat = new Matrix4f(); mat.store(matrix); matrix.flip();
				
				DataUtils utils = new DataUtils();
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
						if(enemies.get(i).getThreadID() == index) {
							enemies.get(i).resetBlinking();
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
			if(enemies.get(i).getShootThreadID() == index) {
				if(started) {
					enemies.get(i).fire(display);
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
	public FloatBuffer updateBg(Vector2f pos) {
		float[] bgdata = {
				//0
				pos.x-1.0f, pos.y+1.0f, 0.0f, 1.0f,
				0.0f, 0.0f,
				0.0f, 0.0f, 0.0f, 0.0f,
				//1
				pos.x+1.0f, pos.y+1.0f, 0.0f, 1.0f,
				1.0f, 0.0f,
				0.0f, 0.0f, 0.0f, 0.0f,
				//2
				pos.x+1.0f, pos.y-1.0f, 0.0f, 1.0f,
				1.0f, 1.0f,
				0.0f, 0.0f, 0.0f, 0.0f,
				//0
				pos.x-1.0f, pos.y+1.0f, 0.0f, 1.0f,
				0.0f, 0.0f,
				0.0f, 0.0f, 0.0f, 0.0f,
				//2
				pos.x+1.0f, pos.y-1.0f, 0.0f, 1.0f,
				1.0f, 1.0f,
				0.0f, 0.0f, 0.0f, 0.0f,
				//3
				pos.x-1.0f, pos.y-1.0f, 0.0f, 1.0f,
				0.0f, 1.0f,
				0.0f, 0.0f, 0.0f, 0.0f
		};
		FloatBuffer bgdatafb = BufferUtils.createFloatBuffer(bgdata.length);
		bgdatafb.put(bgdata);
		bgdatafb.flip();
		
		return bgdatafb;
	}
	public void move(float x, float y) {
		player.move(x, y, display);
	}
	public void toHome() {
		el = null;
		prevpercent = 0; loadpercent = 0;
		started = false;
		gui = new GuiElementHandler();
		gui.newButton("button", new Vector2f(-0.45f, -0.75f), 200.0f, 50.0f, ih, this, "start");
		gui.newString("Click To Play", Color.BLACK, 200.0f, 50.0f, new Vector2f(-0.3f, -0.82f));
		player.reset();
		display.changepos(0.0f, player.getPos().y, 0.0f);
	}
	public void shoot() {
		if(started) {
			player.shoot();
		}
	}
	public void rendergl(ShaderHandler sh, DisplaySetup d, LineCollection lc) {
		//Enable Transparency
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		glViewport(0, 0, Display.getWidth(), Display.getHeight());
		//Clears display
		glClear(GL_COLOR_BUFFER_BIT |
			GL_DEPTH_BUFFER_BIT);
		glLoadIdentity();
		glPushMatrix();

		ARBShaderObjects.glUseProgramObjectARB(sh.getPrograms().get(0).getId());
		
		int projectionmatrixloc = glGetUniformLocation(sh.getPrograms().get(0).getId(), "PerspectiveMatrix");
		glUniformMatrix4(projectionmatrixloc, false, d.getProjectionMatrix());
		
		int viewmatrixloc = glGetUniformLocation(sh.getPrograms().get(0).getId(), "ViewMatrix");
		glUniformMatrix4(viewmatrixloc, false, d.getModelViewMatrix());
		
		int lightloc = glGetUniformLocation(sh.getPrograms().get(0).getId(), "lightpos");
		int viewdirloc = glGetUniformLocation(sh.getPrograms().get(0).getId(), "viewDirection");
		
		Vector4f viewdir = new Vector4f(0.0f, 0.0f, -1.0f, 1.0f);
		viewdir = Matrix4f.transform(display.getRotMatrix(), viewdir, viewdir);
		viewdir.normalise();
		Vector4f light = new Vector4f();
		glUniform4f(lightloc, light.x, light.y, light.z, light.w);
		glUniform4f(viewdirloc, viewdir.x, viewdir.y, viewdir.z, viewdir.w);
		
		if((d.getPos().y + 0.25f > levelheight*-1)) {
			display.changepos(0.0f, -0.005f, 0.0f);
			player.changePos(0.0f, 0.005f);
			for(int i=0; i<enemies.size(); i++) {
				enemies.get(i).scroll();
			}
		}
		lr.render(blockdata, sh, blocktex, blocks, d);
		DataUtils util = new DataUtils();
		for(int i=0; i<buildings.size(); i++) {
			buildings.get(i).render(sh, d, util);
		}
		boss.render(sh, d, util);
		
		player.render(sh, d, util);
		
		for(int i=0; i<enemies.size(); i++) {
			enemies.get(i).update(shaderhandler, display, util);
		}
		if(prevhealth != player.getHealth()) {
			if(started) {
				prevhealth = player.getHealth();
				gui.clearElements();
				ih.clearElements();
				gui.newString("score : " + player.getHealth(), Color.red, 100, 50, new Vector2f(0.1f, 0.95f));
			}
		}
		gui.drawElements(sh);
	}
	public ShaderHandler getSh() {
		return shaderhandler;
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
	public float getChangeX() {
		return changex;
	}
	public float getChangeY() {
		return changey;
	}
	public void removeThread(Integer index) {
		ct.removeTimeStep(index);
	}
}
