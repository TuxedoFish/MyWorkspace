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
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.TextAttribute;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.text.AttributedString;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import object.VertexHandler;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import shader.ShaderHandler;
import start.Controller;
import start.input.InputHandler;
import texture.TextureAtlas;
import utils.DataUtils;
import utils.TextureUtils;

public class GuiElementHandler {
	private TextureAtlas ta = new TextureAtlas();
	private int texid;
	
	private float nextx = 0;
	private float nexty = 0;
	
	private int currentindex = 0;
	
	private float maxheight = 0;
	
	private int vboId = glGenBuffers();
	private int vaoId = glGenVertexArrays();
	
	private ArrayList<Integer> keys = new ArrayList<Integer>();
	private ArrayList<GuiElement> elements = new ArrayList<GuiElement>();
	
	private ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
	private ArrayList<Rectangle2D> bounds = new ArrayList<Rectangle2D>();
	private ArrayList<String> content = new ArrayList<String>();
	
	public GuiElementHandler() {
		texid = glGenTextures();
	}
	public int newString(String str, Color c, float  width, float height, Vector2f topleft, float size) {
		if(!content.contains(str)) {
			elements.add(new GuiString(str, c, new Vector2f(topleft.x, topleft.y), 
				width, height, topleft, size));
			content.add(str);
			images.add(elements.get(elements.size()-1).getImg());
			bounds.add(elements.get(elements.size()-1).getBounds());
		} else {
			elements.add(new GuiString(images.get(content.indexOf(str)), bounds.get(content.indexOf(str)), c, 
					new Vector2f(topleft.x, topleft.y), width, height, topleft));
		}
		
		currentindex += 1;
		keys.add(currentindex);
		return currentindex;
	}
	public int newStringAtPos(String str, Color c, Vector2f pos, float size) {
		elements.add(new GuiString(str, c, new Vector2f(pos.x, pos.y), 
			(size)*str.length(), size*2, pos, size));
		content.add(str);
		images.add(elements.get(elements.size()-1).getImg());
		bounds.add(elements.get(elements.size()-1).getBounds());
	
		currentindex += 1;
		keys.add(currentindex);
		
		Rectangle2D r = elements.get(elements.size()-1).getBounds();
		nextx += ((float)r.getWidth()/(Display.getWidth()/3.0f));
		
		if(r.getHeight()/(Display.getHeight()/2.0f) > maxheight) {
			maxheight = (float)r.getHeight()/(Display.getHeight()/2.0f);
		}
		return currentindex;
	}
	public int newButton(String loc, Vector2f pos, float width, float height, InputHandler ih, Controller parent,
			String eventmessage) {
		GuiButton button = new GuiButton(loc, pos, width, height, parent, eventmessage);
		System.out.println(width + " : " + height);
		elements.add(button);
		ih.addButton(button, elements.size()-1);
		
		currentindex += 1;
		keys.add(currentindex);
		return currentindex;
	}
	public GuiMarker newMarker(String loc, Vector2f pos, float width, float height, InputHandler ih, Controller parent,
			String eventmessage, int animationid, int animationlength) {
		GuiMarker marker = new GuiMarker(loc, pos, width, height, parent, eventmessage, animationid, animationlength);
		elements.add(marker);
		ih.addMarker(marker, elements.size()-1);
		
		currentindex += 1;
		keys.add(currentindex);
		return marker;
	}
	public int newBar(String barname, Vector2f pos, int percent) {
		elements.add(new GuiBar(barname, pos, percent));
		
		currentindex += 1;
		keys.add(currentindex);
		return currentindex;
	}
	public void removeElement(int index) {
		elements.remove(keys.indexOf(index));
		keys.remove(keys.indexOf(index));
	}
	public void newLine() {
		nextx = 0.0f;
		nexty -= maxheight;
		maxheight = 0;
	}
	public void clearElements() {
		nextx = 0.0f;
		nexty = 0.0f;
		maxheight = 0.0f;
		elements.clear();
		keys.clear();
		content.clear();
	}
	public void drawElements(ShaderHandler sh) {
		for(int i=0; i<elements.size();i++) {
			DataUtils datautil = new DataUtils();
			if(elements.size() > i) {
				if(elements.get(i).getType().equals("Marker")) {
					datautil.setup(elements.get(i).getData(), vboId, vaoId, sh, elements.get(i).getTextureId(), 2, elements.get(i).getIndices(), elements.get(i).getMatrix(), 0);
					glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
				} else {
					datautil.setup(elements.get(i).getData(), vboId, vaoId, sh, elements.get(i).getTextureId(), 1, elements.get(i).getIndices(), null, 0);
					glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
				}
			}
		}
	}
}
