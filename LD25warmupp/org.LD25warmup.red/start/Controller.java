package start;

import images.ImageReturn;

import java.awt.Color;
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
import java.util.ArrayList;

import level.Block;
import level.GridMaker;
import level.GridParser;
import level.LevelHolder;
import level.LevelRenderer;
import level.Parser;
import level.TextureHolder;
import logic.Player;

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
	DisplaySetup d;
	
	int testprogram;
	
	InputHandler ih = new InputHandler(this);
	
	ShaderHandler sh = new ShaderHandler();
	
	Player player;
	
	public void start() throws IOException{
		d = new DisplaySetup(640, 480, 1.0f, 1000f);
		
		ImageReturn images = new ImageReturn();
		
		player = new Player(this);
		
		Parser parser = new Parser();
		ArrayList<Block> blocks = parser.parseLevel(images.getImage("Level2.png"), 
				new Vector2f(-1.0f, 1.0f));
		System.err.println("YOU ran it from .... the command line >;O.. sweet ;D");
			
		GridParser gp = new GridParser();
		TextureHolder th = gp.parseGrid(images.getImage("LAND.png"), 20);
			
		LevelRenderer lr = new LevelRenderer();
		LevelHolder data = lr.getLevelData(blocks, th);
		
		this.setupshaders(sh);
		
		while(!Display.isCloseRequested()) {
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
			
			rendergl();
			
			ih.update(d);
			player.render(blocks);
			
			if(ih.getLeftDown()) {
				player.changePos(-0.01f, 0.0f, d);
			}
			if(ih.getRightDown()) {
				player.changePos(0.01f, 0.0f, d);
			}
			
			lr.render(data, sh, th, blocks, d);
			
			Display.update();
			Display.sync(60);
		}
	}
	public void playerJump() {
		player.jump();
	}
	public ShaderHandler getSh() {
		return sh;
	}
	public void rendergl() {
		
	}
	private void setupshaders(ShaderHandler s) throws FileNotFoundException, UnsupportedEncodingException {
		testprogram = s.createprogram();
		
		s.addshader(testprogram, new BufferedReader(new InputStreamReader((getClass().getResourceAsStream("testshader.vert")), "UTF-8")),ARBVertexShader.GL_VERTEX_SHADER_ARB);
		s.addshader(testprogram, new BufferedReader(new InputStreamReader((getClass().getResourceAsStream("testshader.frag")), "UTF-8")),ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);
	    s.finishprogram(testprogram);
	}
}
