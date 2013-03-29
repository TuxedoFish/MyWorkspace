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
import logic.enemies.Boss;
import logic.enemies.Enemy;
import logic.enemies.EnemyBullet;
import logic.enemies.EnemyLoader;

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
import start.gui.GuiElementHandler;
import start.gui.GuiString;
import start.input.InputHandler;
import terrain.Block;
import terrain.DungeonCreator;
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
	private int textureID = 0;
	private Gui gui;
	private ArrayList<Shape> shapes = new ArrayList<Shape>();
	private boolean jump;
	private int stage; private int ministage;
	private int score;
	private DungeonCreator dc;
	
	private float changex; private float changey; private float changez;
	private int health = 500;
	
	private Sprite player;
	private Sprite bullet;
	private Sprite explosion;
	private boolean move = false;
	private Boss boss;
	private int levelheight;
	
	private ArrayList<Integer> texids = new ArrayList<Integer>(); 
	private ArrayList<Color> colors = new ArrayList<Color>(); 
	private ArrayList<LineCollection> debug = new ArrayList<LineCollection>();
	private ArrayList<Block> blocks = new ArrayList<Block>();
	private ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	private ArrayList<EnemyBullet> explosions = new ArrayList<EnemyBullet>();
	
	private Vector2f bpos = new Vector2f(0.0f, 0.0f);
	private Vector2f epos = new Vector2f(0.0f, 0.0f);
	
	private TextureHolder blocktex;
	private LevelHolder blockdata;
	private LevelRenderer lr;
	
	private ShaderHandler shaderhandler;
	
	private boolean started = false;
	
	private float pos;
	private int selected;
	private int vely;
	
	private ColorHandler ch = new ColorHandler(); private GenrealRenderer gh; 
	private ObjectLoader ol = new ObjectLoader();
	
	private DisplaySetup display;
	
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
	public void addColor(Color c) {
		this.texids.add(ch.newCol(c, colors));
	}
	public void startup() {
		started = true;
	}
	public ArrayList<Integer> getColorsID() {
		return texids;
	}
	public ArrayList<Color> getColors() {
		return colors;
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
		gm.makeGrid(20, 10);
		Parser parser = new Parser();
		shaderhandler = new ShaderHandler();
		setupshaders(shaderhandler);
		EnemyLoader el = new EnemyLoader();
		
		try {
			dc = new DungeonCreator();
			blocks = dc.getBlocks();
			GridParser gp = new GridParser();
			
			TextureHolder texture = gp.parseGrid(images.getImage("spaceship.png"), 30.0f);
			player = new Sprite(images.getImage("spaceship.png"), this, 50, 50, texture, 0, new Vector2f(0.0f, 0.0f));
			
			blocktex = gp.parseGrid(images.getImage("LAND.png"), 21);
			levelheight = (images.getImage("LAND.png").getHeight() * 20) / (Display.getHeight());
			lr = new LevelRenderer();
			blockdata = lr.getLevelData(blocks, blocktex);
			
			lr.update(blocks, display);
		} catch (IOException e1) {
			System.err.println("err loading img");
			System.exit(1);
			e1.printStackTrace();
		}
		InputHandler ih = new InputHandler(this);
		
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
		
		pos = 0.0f;
		
		ControllerTimer ct = new ControllerTimer(this);
		
//		for(int i=0; i<10; i++) {
//			enemies.add(new Enemy(player, bullets));
//		}
		
		while(!Display.isCloseRequested()) {
			if(started) {
				rendergl(shaderhandler, display, lc);
				ih.update(display);
			} else {
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
				
				int modelmatrixloc = glGetUniformLocation(shaderhandler.getPrograms().get(0).getId(), "ModelMatrix");
				FloatBuffer matrix = BufferUtils.createFloatBuffer(16);
				Matrix4f mat = new Matrix4f(); mat.store(matrix); matrix.flip();
				glUniformMatrix4(modelmatrixloc, false, matrix);
				
				DataUtils utils = new DataUtils();
				utils.setup(bgdatafb, bgvbo, bgvao, shaderhandler, titlescreen, 2, bgindicesb);
				glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
				ih.update(display);
			}
			Display.update();
			Display.sync(60);
		}
		ct.interrupt();
	}
	public void drawLoadScreen(int percent, ShaderHandler sh) {
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
		
		int modelmatrixloc = glGetUniformLocation(shaderhandler.getPrograms().get(0).getId(), "ModelMatrix");
		FloatBuffer matrix = BufferUtils.createFloatBuffer(16);
		Matrix4f mat = new Matrix4f(); mat.store(matrix); matrix.flip();
		glUniformMatrix4(modelmatrixloc, false, matrix);
		
		GuiElementHandler gui = new GuiElementHandler();
		gui.newString("loading - " + percent, Color.white, 100, 20, new Vector2f(0.1f, 0.95f));
		gui.drawElements(sh);
		
		Display.update();
		Display.sync(60);
	}
	public void update() {
		for(int i=0; i<enemies.size(); i++) {
			enemies.get(i).fire(display);
		}
	}
	public void damage(int d) {
		health -= d;
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
		if(contains(new Vector2f(player.getPos().x + x, player.getPos().y + y)) != 1) {
			player.changePos(x, y);
			display.changepos(-x, -y, 0.0f);
		}
	}
	public int contains(Vector2f pos) {
		for(int i=0; i<blocks.size(); i++) {
			if(closeEnough(blocks.get(i).getX(), pos.x, 0.01f) && closeEnough(blocks.get(i).getY(), pos.y, 0.01f)) {
				if(blocks.get(i).getTexid() == 1) {
					return blocks.get(i).getTexid();
				}
			}
		}
		return -1;
	}
	public boolean closeEnough(float f, float f2, float got) {
		if(f-got < f2 && f+got > f2) {
			return true;
		}
		return false;
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
		
//		GuiElementHandler gui = new GuiElementHandler();
//		gui.newString("score : " + health, Color.red, 100, 20, new Vector2f(0.1f, 0.95f));
//		gui.drawElements(sh);
		
//		if((d.getPos().y + 0.25f > levelheight*-1)) {
//			display.changepos(0.0f, -0.005f, 0.0f);
//			player.changePos(0.0f, 0.005f);
//			for(int i=0; i<enemies.size(); i++) {
//				enemies.get(i).scroll();
//			}
//		} else {
//			boss.render(sh, d);
//		}
		lr.render(blockdata, sh, blocktex, blocks, d);
		player.render(sh);
		
		int tempsize = blocks.size();
		dc.update(display);
		if(blocks.size() != tempsize) {
			blockdata = lr.getLevelData(blocks, blocktex);
			lr.update(blocks, display);
		}
		
		if(stage >= 35) {
			stage = 0;
		}
		for(int i=0; i<enemies.size(); i++) {
			enemies.get(i).render(shaderhandler, display);
		}
	}
	public ShaderHandler getSh() {
		return shaderhandler;
	}
	public Gui getGui() {
		return gui;
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
}
