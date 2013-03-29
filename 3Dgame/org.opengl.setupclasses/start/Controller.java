package start;

import images.ImageReturn;

import java.awt.Color;
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

import logic.LevelLoader;
import logic.enemies.Enemy;

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
import start.input.InputHandler;
import texture.TextureHandler;
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
	
	private float changex; private float changey; private float changez;
	
	private Shape player;
	private Vector3f movement = new Vector3f(0.0f, 0.0f, 0.0f);
	private Vector3f collision = new Vector3f(0.0f, 0.0f, 0.0f);
	private boolean move = false;
	
	public ArrayList<Integer> texids = new ArrayList<Integer>(); 
	private ArrayList<Color> colors = new ArrayList<Color>(); 
	private ArrayList<LineCollection> debug = new ArrayList<LineCollection>();
	
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
	public void rotateSelected(float xrot, float yrot, float zrot) {
		shapes.get(selected).rotate(xrot, yrot, zrot);
	}
	private void moveObject() {
		shapes.get(selected).move(changex/Display.getWidth(), changey/Display.getHeight(), changez/100.0f);
	}
	public void mouseUpdate(int xpos, int ypos) {
		int mouseX = xpos;
	    int mouseY = Display.getHeight() - ypos;

	    float windowWidth = Display.getWidth();
	    float windowHeight = Display.getHeight();

	    //get the mouse position in screenSpace coords
	    double screenSpaceX = ((float) mouseX / (windowWidth / 2) - 1.0f) * 1.0f;
	    double screenSpaceY = (1.0f - (float) mouseY / (windowHeight / 2));

	    double viewRatio = Math.tan(((float) Math.PI / (180.f/display.getFov())/2.00f));

	    screenSpaceX = screenSpaceX * viewRatio;
	    screenSpaceY = screenSpaceY * viewRatio;

	    //Find the far and near camera spaces
	    float far = 10.0f;
	    Vector4f cameraSpaceNear = new Vector4f((float) (screenSpaceX * display.getNear()), (float) (screenSpaceY * display.getNear()), (float) (-display.getNear()), 1);
	    Vector4f cameraSpaceFar = new Vector4f((float) (screenSpaceX * far), (float) (screenSpaceY * far), (float) (-far), 1);


	    //Unproject the 2D window into 3D to see where in 3D we're actually clicking
	    Matrix4f tmpView = Matrix4f.mul(display.getModelViewMatrixAsMatrix(), display.getProjectionMatrixAsMatrix(), new Matrix4f());
	    Matrix4f invView = (Matrix4f) tmpView.invert();
	    Vector4f worldSpaceNear = new Vector4f();
	    Matrix4f.transform(invView, cameraSpaceNear, worldSpaceNear);

	    Vector4f worldSpaceFar = new Vector4f();

	    Matrix4f.transform(invView, cameraSpaceFar, worldSpaceFar);

	    //calculate the ray position and direction
	    Vector3f rayPosition = new Vector3f(worldSpaceNear.x, worldSpaceNear.y, worldSpaceNear.z);
	    Vector3f rayDirection = new Vector3f(worldSpaceFar.x - worldSpaceNear.x, worldSpaceFar.y - worldSpaceNear.y, worldSpaceFar.z - worldSpaceNear.z);
	    
//	    bullets.add(ol.loadShape(getClass().getResourceAsStream("bullet" + "/texture.png"),
//	    		getClass().getResourceAsStream("bullet" + "/data.obj"), 
//				getClass().getResourceAsStream("bullet"+"/matrix.txt"), -1).createUsable());
//	    Vector3f bulletpos3f = bullets.get(bullets.size()-1).getPos();
//	    Vector4f bulletpos4f = new Vector4f(bulletpos3f.x, bulletpos3f.y, bulletpos3f.z, 1.0f);
//	    bulletpos4f = Matrix4f.transform(bullets.get(bullets.size()-1).getModelMatrix(), bulletpos4f, bulletpos4f);
//	    Vector4f playerpos = new Vector4f(player.getPos().x, player.getPos().y, player.getPos().z, 1.0f);
//	    Matrix4f.transform(player.getModelMatrix(), playerpos, playerpos);
//	    bullets.get(bullets.size()-1).move(playerpos.x-bulletpos4f.x, playerpos.y-bulletpos4f.y, playerpos.z-bulletpos4f.z);
//	    MathUtils util = new MathUtils();
//	    rayDirection = util.scaleVector(rayDirection, 100.0f);
//	    
//	    bulletdest.add(rayDirection);
	    
		for(int i = 0; i < shapes.size(); i++) {
			if(shapes.get(i).getCollision().testCollision(new Vector4f(rayPosition.x, rayPosition.y, rayPosition.z, 1.0f),
				new Vector4f(rayDirection.x, rayDirection.y, rayDirection.z, 1.0f))) {
				selected = i;
			}
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
		
		ContextAttribs contextAtrributes = new ContextAttribs(3, 2);
		contextAtrributes.withProfileCompatibility(false);
		contextAtrributes.withProfileCore(true);
		
		display = new DisplaySetup(640, 480, 1.0f, 1000f, contextAtrributes);
		display.changepos(-0.4f, -0.3f, -0.4f);
		//display.rotate(0.0f, .0f, 0.0f);
		gh = new GenrealRenderer();
		
		shaderhandler = new ShaderHandler();
		player = ol.loadShape(getClass().getResourceAsStream("player" + "/texture.png"),
				getClass().getResourceAsStream("player" + "/data.obj"), 
				getClass().getResourceAsStream("player"+"/matrix.txt"), -1).createUsable();
		player.move(0.0f, -0.2f, 1.0f);
		display.changepos(0.0f, 0.0f, -1.0f);
		InputHandler ih = new InputHandler(this);
		
		ColorHandler ch = new ColorHandler();
		texids.add(ch.newCol(Color.BLUE, colors));
		
		TextureUtils util = new TextureUtils();
		ImageReturn images = new ImageReturn();
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
		
		setupshaders(shaderhandler);
		
		glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		
		pos = 0.0f;
		
		LevelLoader ll = new LevelLoader();
		shapes = ll.loadLevel("level1");
		
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
				
				DataUtils utils = new DataUtils();
				utils.setup(bgdatafb, bgvbo, bgvao, shaderhandler, titlescreen, 2, bgindicesb);
				glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
				ih.update(display);
			}
			Display.update();
			Display.sync(60);
		}
	}
//	public void jump() {
//		jump = true;
//		stage = 0; ministage = 0;
//		vely = 1;
//	}
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
	public void move(float x, float y, float z) {
		movement.x = 0.0f; movement.y = 0.0f; movement.z = 0.0f;
		if(collision.x != 1.0f && x > 0.0f) {
			movement.x = 1.0f;
			player.move(-x, 0.0f, 0.0f);
			display.changepos(x, 0.0f, 0.0f);
			move = true;
		}
		if(collision.x != -1.0f && x < 0.0f) {
			movement.x = -1.0f;
			player.move(-x, 0.0f, 0.0f);
			display.changepos(x, 0.0f, 0.0f);
			move = true;
		}
		if(collision.z != 1.0f && z > 0.0f) {
			movement.z = 1.0f;
			player.move(0.0f, 0.0f, -z);
			display.changepos(0.0f, 0.0f, z);
			move = true;
		}
		if(collision.z != -1.0f && z < 0.0f) {
			movement.z = -1.0f;
			player.move(0.0f, 0.0f, -z);
			display.changepos(0.0f, 0.0f, z);
			move = true;
		}
		player.move(0.0f, y, 0.0f);
		display.changepos(0.0f, -y, 0.0f);
	}
	public Vector3f getPlayerPos() {
		return player.getPos();
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
		Vector4f light = Matrix4f.transform(player.getModelMatrix(), new Vector4f(player.getPos().x, player.getPos().y, 
				player.getPos().z, 1.0f), new Vector4f());
		glUniform4f(lightloc, light.x, light.y, light.z, light.w);
		glUniform4f(viewdirloc, viewdir.x, viewdir.y, viewdir.z, viewdir.w);
		
		//gh.renderLineCollection(lc, sh, d, this);
		
		boolean anycoll = false;
		for(int i=0; i<shapes.size(); i++) {
			shapes.get(i).draw(sh, d, changex, changey, changez);
			Vector4f collpoint = player.getCollision().testCollision(shapes.get(i).getCollision());
			if(collpoint != null && move) {
				anycoll = true;
				if(collision.x == 0.0f) collision.x = movement.x;
				if(collision.y == 0.0f) collision.y = movement.y;
				if(collision.z == 0.0f) collision.z = movement.z;
				move = false;
			}
		}
		if(!anycoll && move) {
			collision = new Vector3f(0.0f, 0.0f, 0.0f);
		}
		player.draw(sh, d, changex, changey, changez);
		renderLineCollection(null);
		moveObject();
	}
	public void cloneSelected() {
		shapes.add(new Shape(shapes.get(selected)));
	}
	public void save() {
		boolean done = new File(getClass().getResource("").getFile() + "level1").mkdir();
		ArrayList<String> namesadded = new ArrayList<String>();
		try {
			System.out.println("heya");
			BufferedWriter writer = new BufferedWriter(new FileWriter(getClass().getResource("").getFile() + 
					"level1" + "/index.txt"));
			for(int i=0; i<shapes.size(); i++) {
				writer.write(shapes.get(i).getName() + " " + i);
				writer.newLine();
				if(!(namesadded.contains(shapes.get(i).getName()))) {
					shapes.get(i).save();
				}
				BufferedWriter writer2 = new BufferedWriter(new FileWriter(getClass().getResource("").getFile()
						+ "level1" + "/" + i + ".txt"));
				Matrix4f modelmatrix = shapes.get(i).getModelMatrix();
				Matrix4f rotationmatrix = shapes.get(i).getRotMatrix();
				Quaternion permangle = shapes.get(i).getAxis();
				Vector3f pos = shapes.get(i).getPos();
				
				writer2.write("mm " + modelmatrix.m00 +  " " + modelmatrix.m10 + " " + modelmatrix.m20 + " " + modelmatrix.m30
						+ " " + modelmatrix.m01 + " " + modelmatrix.m11 + " " + modelmatrix.m21 + " " + modelmatrix.m31
						+ " " + modelmatrix.m02 + " " + modelmatrix.m12 + " " + modelmatrix.m22 + " " + modelmatrix.m32
						+ " " + modelmatrix.m03 + " " + modelmatrix.m13 + " " + modelmatrix.m23 + " " + modelmatrix.m33);
				writer2.newLine();
				writer2.write("rm " + rotationmatrix.m00 +  " " + rotationmatrix.m10 + " " + rotationmatrix.m20 + " " + rotationmatrix.m30
						+ " " + rotationmatrix.m01 + " " + rotationmatrix.m11 + " " + rotationmatrix.m21 + " " + rotationmatrix.m31
						+ " " + rotationmatrix.m02 + " " + rotationmatrix.m12 + " " + rotationmatrix.m22 + " " + rotationmatrix.m32
						+ " " + rotationmatrix.m03 + " " + rotationmatrix.m13 + " " + rotationmatrix.m23 + " " + rotationmatrix.m33);
				writer2.newLine();
				writer2.write("q " + permangle.x + " " + permangle.y + " " + permangle.z + " " + permangle.w);
				writer2.newLine();
				writer2.write("p " + pos.x + " " + pos.y + " " + pos.z);
				writer2.close();
			}
			
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void renderLineCollection(LineCollection lc) {
		if(lc != null) {
			debug.add(lc);
		}
		for(int i=0; i<debug.size(); i++) {
			gh.renderLineCollection(debug.get(i), shaderhandler, display, this);
		}
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
