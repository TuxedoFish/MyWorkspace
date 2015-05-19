package start.gui;

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


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import javax.imageio.ImageIO;

import object.VertexHandler;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import shader.ShaderHandler;
import start.Controller;
import start.DisplaySetup;
import utils.TextureUtils;

public class Gui {
	private DisplaySetup d;
	private VertexHandler vh;
	private ShaderHandler sh;
	
	private Window window;
	
	private boolean left; private boolean right; private boolean down; private boolean up;private boolean move;
	
	private Controller parent;
	
	private Dimension mindimension = new Dimension(10,10);
	
	private float changex; private float changey;
	
	private int done;
	
	public Gui(DisplaySetup d, VertexHandler vh, ShaderHandler sh, Controller parent, float size){
		this.d = d; this.vh = vh; this.sh = sh; this.parent = parent;
		
		this.window = new Window(10, 300, 300, 300, sh, size);
	}
	public boolean istouching(int mousex, int mousey) {
		return window.isTouching(mousex, mousey);
	}
	public void mouseTests(int mousex, int mousey) {
		window.mouseTests(mousex, mousey, changex, changey);
	}
	public void draw() {
		window.draw();
	}
	public void moveWindow() {
		window.moveit(changex, changey);
	}
	public void setChangeX(float changex) {
		this.changex = changex;
	}
	public void setChangeY(float changey) {
		this.changey = changey;
	}
  }
