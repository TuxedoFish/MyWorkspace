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
		if(Mouse.isButtonDown(1)) {
			if(!mousescrolldown) {
				lastx = Mouse.getX();
				lasty = Mouse.getY();
				mousescrolldown = true;
			} else {
				float x = Mouse.getX();
				float y = Mouse.getY();
				
				if(x != lastx) {
					if(((lastx - x)/Display.getWidth()) * 360 > 0) {
						d.rotate(neg(((lastx - x)/Display.getWidth()) * 360), 0.0f, 0.0f);
					} else {
						d.rotate(Math.abs(((lastx - x)/Display.getWidth()) * 360), 0.0f, 0.0f);
					}
				}
				if(y != lasty) {
					d.rotate(0.0f, (((lasty - y)/Display.getHeight()) * 360), 0.0f);
				}
				lastx = x;
				lasty = y;
				while (Keyboard.next()) {
				    if (Keyboard.getEventKeyState()) {
				    	if (Keyboard.getEventKey() == Keyboard.KEY_A)  leftdown = true;
				    	if (Keyboard.getEventKey() == Keyboard.KEY_Q)  rotleftdown = true;
				    	if (Keyboard.getEventKey() == Keyboard.KEY_E)  rotrightdown = true;
				    	if (Keyboard.getEventKey() == Keyboard.KEY_S)  forwarddown = true;
				    	if (Keyboard.getEventKey() == Keyboard.KEY_W)  backdown = true;
						if (Keyboard.getEventKey() == Keyboard.KEY_D)  rightdown = true;
						
				    	} else {
				    		if (Keyboard.getEventKey() == Keyboard.KEY_A)  leftdown = false;
				    		if (Keyboard.getEventKey() == Keyboard.KEY_S)  forwarddown = false;
				    		if (Keyboard.getEventKey() == Keyboard.KEY_W)  backdown = false;
				    		if (Keyboard.getEventKey() == Keyboard.KEY_D)  rightdown = false;
				    		if (Keyboard.getEventKey() == Keyboard.KEY_Q)  rotleftdown = false;
				    		if (Keyboard.getEventKey() == Keyboard.KEY_E)  rotrightdown = false;
				      }	
				}
			}
		} else {
			mousescrolldown = false;
			lastx = Mouse.getX();
			lasty = Mouse.getY();
			leftdown = false; rightdown = false; backdown = false; forwarddown = false;
		}
		if(Mouse.isButtonDown(0)) {
			parent.getGui().istouching(Mouse.getX(), Mouse.getY());
			
			if(!mousedown) {
				mousedown = true;
				
				parent.getGui().mouseTests(Mouse.getX(), Mouse.getY());
				
				if(parent.getGui().istouching(Mouse.getX(), Mouse.getY())) {
					move = true;
				}
			}
			if(move) {
				parent.getGui().setChangeX(Mouse.getX() - lblastx);
				parent.getGui().setChangeY(Mouse.getY() - lblasty);
				
				parent.getGui().moveWindow();
				
				lblastx = Mouse.getX();
				lblasty = Mouse.getY();
			}
		} else {
			if(mousedown) {
				parent.getGui().setChangeX(0.0f);
				parent.getGui().setChangeY(0.0f);
				
				move = false;
				
				lblastx = Mouse.getX();
				lblasty = Mouse.getY();
			}
			
			mousedown = false;
			lblastx = Mouse.getX();
			lblasty = Mouse.getY();
		}
		while (Keyboard.next()) {
		    if (Keyboard.getEventKeyState()) {
		    	if (Keyboard.getEventKey() == Keyboard.KEY_DOWN)  parent.mouseChangeUpdate(-1, -5, -1);
		    	if (Keyboard.getEventKey() == Keyboard.KEY_UP)  parent.mouseChangeUpdate(-1, 5, -1);
		    	if (Keyboard.getEventKey() == Keyboard.KEY_LEFT)  parent.mouseChangeUpdate(-5, -1, -1);
		    	if (Keyboard.getEventKey() == Keyboard.KEY_RIGHT)  parent.mouseChangeUpdate(5, -1, -1);
		    	if (Keyboard.getEventKey() == Keyboard.KEY_LSHIFT)  parent.mouseChangeUpdate(-1, -1, -5);
		    	if (Keyboard.getEventKey() == Keyboard.KEY_TAB)  parent.mouseChangeUpdate(-1, -1, 5);
		    	
		    	} else {
		    		if (Keyboard.getEventKey() == Keyboard.KEY_DOWN)  parent.mouseChangeUpdate(-1, 0, -1);
			    	if (Keyboard.getEventKey() == Keyboard.KEY_UP)  parent.mouseChangeUpdate(-1, 0, -1);
			    	if (Keyboard.getEventKey() == Keyboard.KEY_LEFT)  parent.mouseChangeUpdate(0, -1, -1);
			    	if (Keyboard.getEventKey() == Keyboard.KEY_RIGHT)  parent.mouseChangeUpdate(0, -1, -1);
			    	if (Keyboard.getEventKey() == Keyboard.KEY_LSHIFT)	parent.mouseChangeUpdate(-1, -1, 0);
			    	if (Keyboard.getEventKey() == Keyboard.KEY_TAB)	parent.mouseChangeUpdate(-1, -1, 0);
		      }	
		}
		if(leftdown) {
			float rot = d.getxrot() - 90;
			rot = clamp(rot);
			
			if(d.getxrot() > 0) {
				d.changepos((float)Math.sin(Math.toRadians(neg(rot))) / 10, 0.0f, 
						(float) Math.cos(Math.toRadians(neg(rot))) / 10);
			} else {
				d.changepos((float)Math.sin(Math.toRadians(Math.abs(rot))) / 10, 0.0f, 
						(float) Math.cos(Math.toRadians(Math.abs(rot))) / 10);
			}
		}
		if(rightdown) {
			float rot = d.getxrot() + 90;
			rot = clamp(rot);
			
			if(d.getxrot() > 0) {
				d.changepos((float)Math.sin(Math.toRadians(neg(rot))) / 10, 0.0f, 
						(float) Math.cos(Math.toRadians(neg(rot))) / 10);
			} else {
				d.changepos((float)Math.sin(Math.toRadians(Math.abs(rot))) / 10, 0.0f, 
						(float) Math.cos(Math.toRadians(Math.abs(rot))) / 10);
			}
		}
		if(forwarddown) {
			float rot = d.getxrot() + 180;
			rot = clamp(rot);
			
			if(d.getxrot() > 0) {
				d.changepos((float)Math.sin(Math.toRadians(neg(rot))) / 10, 0.0f, 
						(float) Math.cos(Math.toRadians(neg(rot))) / 10);
			} else {
				d.changepos((float)Math.sin(Math.toRadians(Math.abs(rot))) / 10, 0.0f, 
						(float) Math.cos(Math.toRadians(Math.abs(rot))) / 10);
			}
		}
		if(backdown) {
			if(d.getxrot() > 0) {
				d.changepos((float)Math.sin(Math.toRadians(neg(d.getxrot()))) / 10, 0.0f, 
						(float) Math.cos(Math.toRadians(neg(d.getxrot()))) / 10);
			} else {
				d.changepos((float)Math.sin(Math.toRadians(Math.abs(d.getxrot()))) / 10, 0.0f, 
						(float) Math.cos(Math.toRadians(Math.abs(d.getxrot()))) / 10);
			}
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
