package logic.enemies;

import java.io.File;
import java.util.ArrayList;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import object.ObjectLoader;
import object.Shape;
import physics.OctreeCollision;
import physics.Rectangle3D;
import shader.ShaderHandler;
import start.DisplaySetup;

public class Enemy {
	private Shape player;
	private Shape me;
	private OctreeCollision r1, r2;
	private ArrayList<Shape> bullets;
	private boolean alive = true;
	
	public Enemy(Shape player, ArrayList<Shape> bullets) {
		this.player = player;
		this.bullets = bullets;
		
		ObjectLoader ol = new ObjectLoader();
		me = ol.loadShape(getClass().getResourceAsStream("enemy2" + "/texture.png"),
				getClass().getResourceAsStream("enemy2" + "/data.obj"), 
				getClass().getResourceAsStream("enemy2"+"/matrix.txt"), -1).createUsable();
		me.move((float)(Math.random()*6.0f)-3.0f, 0.0f, (float)(Math.random()*-3.0f));
		r1 = new OctreeCollision(player);
		r2 = new OctreeCollision(me);
	}
	public void draw(ShaderHandler sh, DisplaySetup d, float changex, float changey, float changez) {
		if(alive) {
			me.draw(sh, d, changex, changey, changez);
			move();
			testCollisions();
		}
	}
	private void testCollisions() {
		for(int i=0; i<bullets.size(); i++) {
			if(r2.testCollision(bullets.get(i).getCollision()) != null) {
				alive = false;
			}
		}
	}
	public void move() {
		Vector4f playerpos = new Vector4f(); 
		Matrix4f.transform(player.getMatrix(), new Vector4f(player.getPos().x, player.getPos().y, 
				player.getPos().z ,1.0f), playerpos);
			
		Vector4f mypos = new Vector4f(); 
		Matrix4f.transform(me.getMatrix(), new Vector4f(me.getPos().x, me.getPos().y, 
				me.getPos().z ,1.0f), mypos);	
			
		float deltaZ = playerpos.z - mypos.z;
		float deltaX = playerpos.x - mypos.x;
		float angle = (float)(Math.atan2(deltaX, deltaZ) * (180 / Math.PI));
		me.move((float)Math.sin(Math.toRadians(angle))/100.0f, 0.0f, (float)Math.cos(Math.toRadians(angle))/100.0f);
	}
	public Shape getShape() {
		return me;
	}
}
