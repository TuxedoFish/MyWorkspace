package start.input;

import images.ImageReturn;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.ArrayList;

import object.Sprite;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

import start.Controller;
import start.DisplaySetup;
import start.gui.GuiButton;
import start.gui.GuiElement;
import start.gui.GuiMarker;

public class InputHandler {
	private boolean leftdown, rightdown, up, down;
	private boolean mousedown = false;
	private Controller parent;
	private ArrayList<GuiButton> buttons = new ArrayList<GuiButton>();
	private ArrayList<GuiMarker> markers = new ArrayList<GuiMarker>();
	private ArrayList<Integer> indexs = new ArrayList<Integer>();
	private ArrayList<Integer> indexsmarker = new ArrayList<Integer>();
	private int shootduration;
	private Cursor pointing;
	private Cursor pulling;
	private Vector2f lastpos = new Vector2f(-1.0f, -1.0f);
	private boolean levelmap = false;
	private Sprite map;
	private boolean shootbuttondown=false;
	private boolean spacedown=false;
	
	public InputHandler(Controller parent, Sprite map) {
		this.parent = parent;
		leftdown = false; rightdown = false;
		pointing = newCursor("cursor.png");
		pulling = newCursor("cursorpulling.png");
		this.map = map;
	}
	public void addMarker(GuiMarker m, int index) {
		markers.add(m);
		indexsmarker.add(index);
	}
	public void atLevelMap(boolean b) {
		levelmap = b;
	}
	public Cursor newCursor(String cursor) {
		ImageReturn images = new ImageReturn();
		try {
			int[] data=images.getImage("gui/" + cursor).getRaster().getPixels(0,0,16,16,(int[])null);
			    
			IntBuffer ib=BufferUtils.createIntBuffer(16*16);
			
			for(int i=0;i<data.length;i+=4) {
				ib.put(data[i] | data[i+1]<<8 | data[i+2]<<16 | data[i+3]<<24);
			}
			ib.flip();
			
			return new Cursor(16, 16, 15, 1, 1, ib, null);
		} catch (LWJGLException | IOException e) {
			e.printStackTrace();
			return null; 
		}
	}
	public float neg(float f) {
		return f * -1;
	}
	public void addButton(GuiButton button, int index) {
		buttons.add(button);
		indexs.add(index);
	}
	public void removeElement(int index) {
		buttons.remove(indexs.indexOf(index));
		indexs.remove(indexs.indexOf(index));
	}
	public boolean contains(Vector2f mouse, Rectangle2D bounds) {
		if(mouse.x > bounds.getX() && mouse.x < bounds.getX() + bounds.getWidth()) {
			if(mouse.y < bounds.getY() && mouse.y > bounds.getY() - bounds.getHeight()) {
				return true;
			}
		}
		return false;
	}
	public void clearElements() {
		buttons.clear();
	}
	public void shoot() {
		if(shootbuttondown) {
			parent.shoot();
			shootduration = 0;
		} else {
			shootduration += 200;
		}
	}
	public int getMovement() {
		if((rightdown && leftdown) || (!rightdown && !leftdown)) {
			return 0;
		}
		if(rightdown) {
			return 2;
		}
		if(leftdown) {
			return 1;
		}
		return 0;
	}
	public void update(DisplaySetup d) {
		Vector2f mousepos = new Vector2f(Mouse.getX(), Mouse.getY());
		for(int i=0; i<buttons.size(); i++) {
			if(contains(mousepos, buttons.get(i).getBounds())) {
				if(Mouse.isButtonDown(0) && !mousedown) {
					buttons.get(i).setState(2);
				} else {
					buttons.get(i).setState(1);
				}
			} else {
				buttons.get(i).setState(0);
			}
		}
		for(int i=0; i<markers.size(); i++) {
			Rectangle2D bounds = markers.get(i).getBounds();
			Rectangle2D accbounds = new Rectangle2D.Float();
			accbounds.setRect(bounds.getX()+(d.getPos().x*Display.getWidth()/2.0f), bounds.getY()+(d.getPos().y*Display.getHeight()/2.0f), bounds.getWidth(), bounds.getHeight());
			
			if(contains(mousepos, accbounds)) {
				if(Mouse.isButtonDown(0) && !mousedown) {
					markers.get(i).pressed();
				} else {
					markers.get(i).mouseupdate(true, d);
				}
			} else {
				markers.get(i).mouseupdate(false, d);
			}
		}
		if(Mouse.isButtonDown(0)) {
			shootbuttondown=true;
			if(!mousedown) {
				parent.addLine((float)Mouse.getX()/Display.getWidth()*2.0f-1.0f, (float)Mouse.getY()/Display.getHeight()*2.0f-1.0f);
				if(shootduration >= 200) {
					parent.shoot();
					parent.resetThread(parent.getPlayerShootThreadId());
					shootduration = 0;
				}
				try {
					Mouse.setNativeCursor(pulling);
				} catch (LWJGLException e) {
					e.printStackTrace();
				}
				mousedown = true;
			}
			if((lastpos.x < 0.0f)) {
				lastpos = new Vector2f(Mouse.getX(), Mouse.getY());
			} else {
				if(levelmap) {
					float xchange = ((float)Mouse.getX()-(float)lastpos.x)/(float)Display.getWidth();
					float ychange = ((float)Mouse.getY()-(float)lastpos.y)/(float)Display.getHeight();
					
					if(!(d.getPos().x+xchange+1.15f < map.getPos().x + (float)map.getWidth()/Display.getWidth())) {
						xchange = 0.0f;
					}
					if(!(d.getPos().x+xchange-1.1f > map.getPos().x)) {
						xchange = 0.0f;
					}
					if(!(d.getPos().y+ychange+1.1f < map.getPos().y)) {
						ychange = 0.0f;
					}
					if(!(d.getPos().y+ychange-1.1f > map.getPos().y - (float)map.getHeight()/Display.getHeight())) {
						ychange = 0.0f;
					}
					d.changepos(xchange, ychange, 0.0f);
				}
				lastpos.x = Mouse.getX();
				lastpos.y = Mouse.getY();
			}
		} else {
			try {
				Mouse.setNativeCursor(pointing);
			} catch (LWJGLException e) {
				e.printStackTrace();
			}
			lastpos = new Vector2f(-1.0f, -1.0f);
			mousedown = false;
		}
		while (Keyboard.next()) {
		    if (Keyboard.getEventKeyState()) {
		    	if (Keyboard.getEventKey() == Keyboard.KEY_A)  leftdown = true;
				if (Keyboard.getEventKey() == Keyboard.KEY_D)  rightdown = true;
				if (Keyboard.getEventKey() == Keyboard.KEY_W)  up = true;
				if (Keyboard.getEventKey() == Keyboard.KEY_S)  down = true;
				if (Keyboard.getEventKey() == Keyboard.KEY_SPACE)  {spacedown = true; shootbuttondown=true;}
				if (Keyboard.getEventKey() == Keyboard.KEY_X)  parent.nextStage();
		    	} else {
		    		if (Keyboard.getEventKey() == Keyboard.KEY_A)  leftdown = false;
		    		if (Keyboard.getEventKey() == Keyboard.KEY_D)  rightdown = false;
		    		if (Keyboard.getEventKey() == Keyboard.KEY_W)  up = false;
		    		if (Keyboard.getEventKey() == Keyboard.KEY_S)  down = false;
					if (Keyboard.getEventKey() == Keyboard.KEY_SPACE)  spacedown = false;
		      }	
		}
		if(!spacedown && !mousedown) {
			shootbuttondown=false;
		}
		if(rightdown) {
			parent.move(0.02f, 0.0f);
		}
		if(leftdown) {
			parent.move(-0.02f, 0.0f);
		}
		if(up) {
			parent.move(0.0f, 0.02f);
		}
		if(down) {
			parent.move(0.0f, -0.02f);
		}
	}
	public float clamp(float rot) {
		if(rot < 0) {
			rot += 360;
		}
		if(rot > 360) {
			rot -= 360;
		}
		
		return rot;
	}
}
