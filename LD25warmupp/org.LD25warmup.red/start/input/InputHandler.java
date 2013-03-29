package start.input;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

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
		if(Mouse.isButtonDown(0)) {
			
		}
		while (Keyboard.next()) {
		    if (Keyboard.getEventKeyState()) {
		    	if (Keyboard.getEventKey() == Keyboard.KEY_D) rightdown = true;
		    	if (Keyboard.getEventKey() == Keyboard.KEY_A) leftdown = true;
		    	if (Keyboard.getEventKey() == Keyboard.KEY_W) parent.playerJump();
		    	
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
