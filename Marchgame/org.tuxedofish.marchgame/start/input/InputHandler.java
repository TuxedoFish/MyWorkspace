package start.input;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

import start.Controller;
import start.DisplaySetup;
import start.gui.GuiButton;
import start.gui.GuiElement;

public class InputHandler {
	private boolean leftdown, rightdown, forwarddown, backdown, rotleftdown, rotrightdown, up, down;
	private boolean mousedown = false,  mousescrolldown;
	private float lastx, lasty;
	private float lblastx, lblasty;
	private float xhome, yhome;
	private boolean move;
	private Controller parent;
	private boolean started = false;
	private ArrayList<GuiButton> buttons = new ArrayList<GuiButton>();
	private ArrayList<Integer> indexs = new ArrayList<Integer>();
	
	public InputHandler(Controller parent) {
		this.parent = parent;
		leftdown = false; rightdown = false; backdown = false; forwarddown = false;
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
		if(mousedown) {
			parent.shoot();
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
				if(Mouse.isButtonDown(0)) {
					buttons.get(i).setState(2);
				} else {
					buttons.get(i).setState(1);
				}
			} else {
				buttons.get(i).setState(0);
			}
		}
		if(Mouse.isButtonDown(0)) {
			if(!mousedown) {
				mousedown = true;
			}
		} else {
			mousedown = false;
		}
		while (Keyboard.next()) {
		    if (Keyboard.getEventKeyState()) {
		    	if (Keyboard.getEventKey() == Keyboard.KEY_A)  leftdown = true;
				if (Keyboard.getEventKey() == Keyboard.KEY_D)  rightdown = true;
				if (Keyboard.getEventKey() == Keyboard.KEY_W)  up = true;
				if (Keyboard.getEventKey() == Keyboard.KEY_S)  down = true;
		    	} else {
		    		if (Keyboard.getEventKey() == Keyboard.KEY_A)  leftdown = false;
		    		if (Keyboard.getEventKey() == Keyboard.KEY_D)  rightdown = false;
		    		if (Keyboard.getEventKey() == Keyboard.KEY_W)  up = false;
		    		if (Keyboard.getEventKey() == Keyboard.KEY_S)  down = false;
		      }	
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
