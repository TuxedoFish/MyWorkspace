package logic.entities;

import java.awt.image.BufferedImage;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

import object.Sprite;
import start.Controller;
import texture.TextureHolder;

public class Gun extends Sprite {
	private BulletHandler bullets;
	private int pattern = 1;
	private Player player;
	private boolean visible = true;
	private Vector2f pos;
	private int shootspeed;
	
	public Gun(BufferedImage img, Controller parent, int width, int height,
			TextureHolder th, int currenttexid, Vector2f pos,
			int pattern, Player player, int speedlength) {
		super(img, parent, width, height, th, currenttexid, pos);
		this.shootspeed = speedlength;
		this.pos = pos;
		this.pattern = pattern;
		this.player = player;
	}
	public void finish(BulletHandler bullets) {
		this.bullets = bullets;
	}
	public void fire() {
		if(pattern == 1) {
			for(float i=0; i<2*Math.PI; i+=Math.PI) {
				shoot((float)(i+(2*Math.PI)));
			}
		}
		if(pattern == 2) {
			double compassBearing=Math.atan2(this.getPos().y - player.getPos().y, 
					this.getPos().x - player.getPos().x);
			shoot((float)compassBearing);
		}
		if(pattern == 3) {
			laser(getPos());
		}
		if(pattern == 4) {
			shoot((float)(Math.PI)/2);
		}
	}
	public void clear() {
		bullets.stoplaser();
	}
	private void laser(Vector2f pos) {
		bullets.stoplaser();
		bullets.shootlaser(pos);
	}
	private void shoot(float rot) {
		bullets.add(new Bullet(new Vector2f(this.getPos().x+(this.getWidth()/(Display.getWidth()*2.0f)), this.getPos().y), rot, 40, "normal"));
	}
	public boolean isVisible() {
		return visible;
	}
	public void update(Vector2f parentpos, float width, float height) {
		this.setPos(parentpos.x + (width/2) + pos.x, parentpos.y - (height/2) + pos.y);
	}
	public void animate() {
		float direction = (float) Math.atan2(player.getPos().y - getPos().y, player.getPos().x - getPos().x);
		int texture = 6-(int)(direction/(Math.PI/4));
		if(texture < 0) {
			texture += 8;
		}
		if(texture > 7) {
			texture -= 8;
		}
		this.changeTexture(texture);
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}
