package start;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import object.ColorHandler;
import object.ObjectLoader;
import object.Polygon;
import object.Shape;

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

import shader.ShaderHandler;
import start.gui.Gui;
import start.input.InputHandler;
import texture.TextureHandler;
import utils.GenrealRenderer;
import utils.Line;
import utils.LineCollection;

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
	
	private float changex; private float changey; private float changez;
	
	public ArrayList<Integer> coltexids = new ArrayList<Integer>(); 
	private ArrayList<Color> colors = new ArrayList<Color>(); 
	
	private float pos;
	
	private ColorHandler ch = new ColorHandler(); private GenrealRenderer gh; 
	private ObjectLoader ol = new ObjectLoader();
	
	private DisplaySetup display;
	
	public Controller() {
		
	}
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
		this.coltexids.add(ch.newCol(c, colors));
	}
	public ArrayList<Integer> getColorsID() {
		return coltexids;
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
		
		display = new DisplaySetup(640, 480, 1.0f, 1000f);
		gh = new GenrealRenderer();
		
		ShaderHandler shaderhandler = new ShaderHandler();
		Shape triangle = new Shape("");
		InputHandler ih = new InputHandler(this);
		
		ColorHandler ch = new ColorHandler();

		coltexids.add(ch.newCol(Color.BLUE, colors));
		
		gui = new Gui(display, triangle.getVh(), shaderhandler, this);

		shapes.add(triangle);
		try {
			shapes.add(ol.loadModel("cube.obj").createUsable());
		} catch (IOException e) {
			System.err.println("ERROROROROOR : " + e.getLocalizedMessage());
			System.exit(1);
		}
		
		triangle.newTriangle(new Vector4f[]{new Vector4f(0.0f, 0.0f, -1.5f, 1.0f)}, null, null, 100, 
				new Color(0.5f, 0.5f, 0.5f, 1.0f), false);
		triangle.newTriangle(new Vector4f[]{new Vector4f(0.5f, 0.0f, -1.5f, 1.0f)}, null, null, 100, 
				new Color(0.5f, 0.5f, 0.5f, 1.0f), false);
		triangle.finish();
		
		setupshaders(shaderhandler);
		
		glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		
		pos = 0.0f;
		
		while(!Display.isCloseRequested()) {
			rendergl(shaderhandler, display, lc);
			
			ih.update(display);
			
			Display.update();
			Display.sync(60);
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
		
		int modelmatrixloc = glGetUniformLocation(sh.getPrograms().get(0).getId(), "ModelMatrix");
		glUniformMatrix4(modelmatrixloc, false, d.getModelMatrix());
		
		gh.renderLineCollection(lc, sh, d, this);
		shapes.get(0).draw(sh, d, changex, changey, changez);
		shapes.get(1).draw(sh, d, changex, changey, changez);
		gui.draw();
	}
	public Gui getGui() {
		return gui;
	}
	public void setupshaders(ShaderHandler s) {
		testprogram = s.createprogram();
		
		s.addshader(testprogram,"testshader.vert",ARBVertexShader.GL_VERTEX_SHADER_ARB);
		s.addshader(testprogram,"testshader.frag",ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);
	    s.finishprogram(testprogram);
	}
	public float getChangeX() {
		return changex;
	}
	public float getChangeY() {
		return changey;
	}
}
