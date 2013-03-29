package start.input;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import start.Controller;
import start.DisplaySetup;

public class InputHandler {
	private boolean leftdown, rightdown, forwarddown, backdown, rotleftdown, rotrightdown, up, down;
	private boolean mousedown = false,  mousescrolldown;
	private float lastx, lasty;
	private float lblastx, lblasty;
	private float xhome, yhome;
	private boolean move;
	private Controller parent;
	private boolean started = false;
	
	public InputHandler(Controller parent) {
		this.parent = parent;
		leftdown = false; rightdown = false; backdown = false; forwarddown = false;
	}
	public float neg(float f) {
		return f * -1;
	}
	public void update(DisplaySetup d) {
		if(!started) {
			while (Keyboard.next()) {
			    if (Keyboard.getEventKeyState()) {
			    	if (Keyboard.getEventKey() == Keyboard.KEY_X)  {parent.startup(); started = true;}
			    }
			}
		} else {
			if(Mouse.isButtonDown(0)) {
				if(!mousedown) {
					mousedown = true;
				}
			} else {
				mousedown = false;
			}
			while (Keyboard.next()) {
			    if (Keyboard.getEventKeyState()) {
			    	if (Keyboard.getEventKey() == Keyboard.KEY_A)  parent.move(-50.0f/Display.getWidth(), 0.0f);
					if (Keyboard.getEventKey() == Keyboard.KEY_D)  parent.move(50.0f/Display.getWidth(), 0.0f);
					if (Keyboard.getEventKey() == Keyboard.KEY_W)  parent.move(0.0f, 50.0f/Display.getHeight());
					if (Keyboard.getEventKey() == Keyboard.KEY_S)  parent.move(0.0f, -50.0f/Display.getHeight());
			    	} else {
			    		if (Keyboard.getEventKey() == Keyboard.KEY_A)  leftdown = false;
			    		if (Keyboard.getEventKey() == Keyboard.KEY_D)  rightdown = false;
			    		if (Keyboard.getEventKey() == Keyboard.KEY_W)  up = false;
			    		if (Keyboard.getEventKey() == Keyboard.KEY_S)  down = false;
			      }	
			}
//			if(rightdown) {
//				parent.move(0.02f, 0.0f);
//			}
//			if(leftdown) {
//				parent.move(-0.02f, 0.0f);
//			}
//			if(up) {
//				parent.move(0.0f, 0.02f);
//			}
//			if(down) {
//				parent.move(0.0f, -0.02f);
//			}
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
