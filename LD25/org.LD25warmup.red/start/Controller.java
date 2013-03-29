package start;

import images.ImageReturn;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import level.Block;
import level.GridMaker;
import level.GridParser;
import level.LevelHolder;
import level.LevelRenderer;
import level.Parser;
import level.TextureHolder;
import logic.Buttons;
import logic.Crab;
import logic.Enemy;
import logic.Player;
import logic.PlayerAnimation;
import logic.Snake;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import shader.Program;
import shader.ShaderHandler;
import start.input.InputHandler;
import tdobject.Sprite;
import utils.DataUtils;
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

public class Controller{
	private DisplaySetup d;
	
	private int testprogram;
	
	private InputHandler ih = new InputHandler(this);
	
	private ShaderHandler sh = new ShaderHandler();
	
	private Player player;
	
	private String type = "title";
	
	private int leveln = 1;
	
	private Vector2f mtrctmousepos = new Vector2f();
	private Buttons buttons;
	private ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	private ArrayList<Block> blocks;
	private boolean stopped = false;
	
	private boolean paused = false;
	private int stage;
	
	public void start() throws IOException{
		d = new DisplaySetup(640, 480, 1.0f, 1000f);
		
		ImageReturn images = new ImageReturn();
		
		Parser parser = new Parser();
		blocks = parser.parseLevel(images.getImage("Level.png"), 
				new Vector2f(-1.0f, 1.0f));
		System.err.println("YOU ran it from .... the command line >;O.. sweet ;D");
		
		GridMaker gm = new GridMaker();
		gm.makeGrid(30, 30, 10);
		
		GridParser gp = new GridParser();
		TextureHolder th = gp.parseGrid(images.getImage("LAND.png"), 20);
			
		LevelRenderer lr = new LevelRenderer();
		LevelHolder data = lr.getLevelData(blocks, th);
		
		BufferedImage level = images.getImage("Level.png");
		
		this.setupshaders(sh);
		
		buttons = new Buttons(this, blocks, d);
		
		TextureUtils util = new TextureUtils();
		
		int bgtex = util.binddata(images.getImage("BG.png"));
		int losescreen = util.binddata(images.getImage("Lose.png"));
		int winscreen = util.binddata(images.getImage("Win.png"));
		int titlescreen = util.binddata(images.getImage("Title.png"));
		
		int[] bgindice = {
				0, 1, 2,
				3, 4, 5
		};
		
		IntBuffer bgindices = BufferUtils.createIntBuffer(bgindice.length);
		bgindices.put(bgindice);
		bgindices.flip();
		
		int bgvbo = glGenBuffers();
		int bgvao = glGenVertexArrays();
		
		player = new Player(this);
		enemies.add(new Crab(this, new Vector2f(0.0f, 0.0f), 20, 20, blocks, d));
		enemies.add(new Snake(this, new Vector2f(0.0f, 0.0f), 20, 20, blocks, d));
		
		FloatBuffer bgdatafb = updateBg(new Vector2f(0.0f, 0.0f));
		gm.makeGrid(20, 10);
		
		while(!Display.isCloseRequested() && !stopped) {
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
				
				int modelmatrixloc = glGetUniformLocation(sh.getPrograms().get(0).getId(), "ModelMatrix");
				glUniformMatrix4(modelmatrixloc, false, d.getModelMatrix());
				
				ih.update(d);
				
				if(!paused) {
					int opacityloc = glGetUniformLocation(sh.getPrograms().get(0).getId(), "opacity");
					glUniform1i(opacityloc, -1);
					
					bgdatafb = updateBg(new Vector2f(d.getPos().x * -1, d.getPos().y * -1));
					DataUtils utils = new DataUtils();
					utils.setup(bgdatafb, bgvbo, bgvao, getSh(), bgtex, 2, bgindices);
					glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
					
					for(int i = 0; i < enemies.size(); i++) {
						enemies.get(i).render();
					}
					player.render(blocks);
					player.changePos(0.01f, 0.0f);
					
					lr.render(data, sh, th, blocks, d);
					buttons.render();
					
					if(ih.getLeftDown()) {
						if(!(d.getPos().x + 0.01f > 0.0f)) {
							d.changepos(0.03f, 0.0f, 0.0f);
							buttons.move(-0.03f, 0.0f);
							enemies.get(0).setPos(mtrctmousepos.x/(Display.getWidth()/2) - 1.0f - d.getPos().x, 
									mtrctmousepos.y/(Display.getHeight()/2) - 1.0f - d.getPos().y, 0,  buttons.getEnemySelected()*8);
						}
					}
					if(ih.getRightDown()) {
						if(!(d.getPos().x - 0.03f < (float)(level.getWidth()*new Block().getWidth()-(Display.getWidth()*2))/
								(Display.getWidth())*-1.0f)) {
							d.changepos(-0.03f, 0.0f, 0.0f);
							buttons.move(0.03f, 0.0f);
							enemies.get(0).setPos(mtrctmousepos.x/(Display.getWidth()/2) - 1.0f - d.getPos().x, 
									mtrctmousepos.y/(Display.getHeight()/2) - 1.0f - d.getPos().y, 0,  buttons.getEnemySelected()*8);
						}
					}
				} else {
					int opacityloc = glGetUniformLocation(sh.getPrograms().get(0).getId(), "opacity");
					glUniform1i(opacityloc, -1);
					if(type.equals("won")) {
						bgdatafb = updateBg(new Vector2f(d.getPos().x * -1, d.getPos().y * -1));
						DataUtils utils = new DataUtils();
						utils.setup(bgdatafb, bgvbo, bgvao, getSh(), winscreen, 2, bgindices);
						glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
						if(stage == 0) {
							leveln += 1;
							if(leveln == 3) {
								stopped = true;
							} else {
								player.respawn(leveln);
								enemies.get(0).addEnemy(0.0f, 0.0f, blocks);
								paused = false;
								d.setPos(0.0f, d.getPos().y, d.getPos().z);
								buttons.move(0.81f - buttons.getPos().x, 0.0f);
								
								blocks.clear();
								blocks = parser.parseLevel(images.getImage("Level" + leveln + ".png"), 
										new Vector2f(-1.0f, 1.0f));
								
								buttons.levelUpdate();
								data = lr.getLevelData(blocks, th);
							}
						}
					} else if(type.equals("lost")) {
						bgdatafb = updateBg(new Vector2f(d.getPos().x * -1, d.getPos().y * -1));
						DataUtils utils = new DataUtils();
						utils.setup(bgdatafb, bgvbo, bgvao, getSh(), losescreen, 2, bgindices);
						glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
						if(stage == 0) {
							stopped = true;
						}
					}
				}
				if(!stopped) {
					Display.update();
					Display.sync(60);
				}
		}
		stopped = true;
		Display.destroy();
	}
	public int getLevel() {
		return leveln;
	}
	public boolean getStopped() {
		return stopped;
	}
	public void lose() {
		for(int i = 0; i < enemies.size(); i++) {
			enemies.get(i).deleteAll();
		}
		type = "lost";
		stage = 10;
		paused = true;
		Runnable timer = new TransparencyTimer(this);
		Thread mytimerthread = new Thread(timer);
		mytimerthread.start();
	}
	public void updateStage() {
		stage -= 1;
	}
	public void win() {
		for(int i = 0; i < enemies.size(); i++) {
			enemies.get(i).deleteAll();
		}
		type = "won";
		stage = 10;
		paused = true;
		Runnable timer = new TransparencyTimer(this);
		Thread mytimerthread = new Thread(timer);
		mytimerthread.start();
	}
	public void playerJump() {
		player.jump();
	}
	public ShaderHandler getSh() {
		return sh;
	}
	public Player getEnemy() {
		return player;
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
	public void mouseUpdate(String type, Vector2f pos) {
		if(!(type.equals("Moved"))) {
			if(!(buttons.mouseUpdate(type, pos, d)) && type.equals("Clicked")) {
				enemies.get(buttons.getEnemySelected()).addEnemy(pos.x - (((d.getPos().x + 1.0f)*(Display.getWidth()/2)) - (Display.getWidth()/2)), 
						pos.y + (((d.getPos().y + 1.0f)*(Display.getHeight()/2)) - (Display.getHeight()/2)), blocks);
			}
		}
		if(type.equals("Moved")) {
			if(!(enemies.get(0).empty())) {
				enemies.get(0).setPos(pos.x/(Display.getWidth()/2) - 1.0f - d.getPos().x, 
						pos.y/(Display.getHeight()/2) - 1.0f - d.getPos().y, 0, buttons.getEnemySelected()*8);
				mtrctmousepos = pos;
			}
		}
	}
	private void setupshaders(ShaderHandler s) throws FileNotFoundException, UnsupportedEncodingException {
		testprogram = s.createprogram();
		
		s.addshader(testprogram, new BufferedReader(new InputStreamReader((getClass().getResourceAsStream("testshader.vert")), "UTF-8")),ARBVertexShader.GL_VERTEX_SHADER_ARB);
		s.addshader(testprogram, new BufferedReader(new InputStreamReader((getClass().getResourceAsStream("testshader.frag")), "UTF-8")),ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);
	    s.finishprogram(testprogram);
	}
}
