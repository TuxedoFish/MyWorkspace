package start.input;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

import start.Controller;
import start.DisplaySetup;

public class InputHandler {
	private boolean leftdown, rightdown, forwarddown, backdown, rotleftdown, rotrightdown;
	private boolean mousedown,  mousescrolldown;
	private float lastx, lasty;
	private float lblastx, lblasty;
	private float xhome, yhome;
	private boolean move;
	private Controller parent;
	private Vector2f mousepos = new Vector2f(0.0f, 0.0f);
	
	public InputHandler(Controller parent) {
		this.parent = parent;
	}
	public float neg(float f) {
		return f - (f*2);
	}
	public void update(DisplaySetup d) {
		if(Mouse.isButtonDown(2)) {
			
		} else {
			
		}
		Vector2f currentpos = new Vector2f(Mouse.getX(), Mouse.getY());
		if(Mouse.isButtonDown(0)) {
			mousedown = true;
			parent.mouseUpdate("Down", currentpos);
		} else {
			if(mousedown) {
				parent.mouseUpdate("Clicked", currentpos);
			}
			mousedown = false;
		}
		if(!(currentpos.x == mousepos.x && currentpos.y == mousepos.y)) {
			parent.mouseUpdate("Moved", currentpos);
		}
		mousepos = new Vector2f(Mouse.getX(), Mouse.getY());
		while (Keyboard.next()) {
		    if (Keyboard.getEventKeyState()) {
		    	if (Keyboard.getEventKey() == Keyboard.KEY_D) rightdown = true;
		    	if (Keyboard.getEventKey() == Keyboard.KEY_A) leftdown = true;
		    	
		    	} else {
		    		if (Keyboard.getEventKey() == Keyboard.KEY_D) rightdown = false;
		    		if (Keyboard.getEventKey() == Keyboard.KEY_A) leftdown = false;
		      }	
		}
		if(forwarddown) {
			
		}
		if(backdown) {
			
		}
	}
	public boolean getLeftDown() {
		return leftdown;
	}
	public boolean getRightDown() {
		return rightdown;
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
