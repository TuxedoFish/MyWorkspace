package logic.entities;

import java.awt.Polygon;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector2f;

import shader.ShaderHandler;
import start.Controller;
import start.DisplaySetup;
import texture.TextureHolder;
import utils.DataUtils;

import logic.entities.troops.Troop;
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

public class Spawner {
	private Troop enemytype;
	private ArrayList<Troop> enemies = new ArrayList<>();
	private TextureHolder[] th;
	private String[] parts;
	private int bullettextureid;
	private int explosiontextureid;
	private int textureid;
	private int threadid;
	private int timing;
	private IntBuffer vbos;
	private IntBuffer vaos;
	private int maxspawnamount;
	private int amountspawned;
	private int buffersneeded;
	
	public int getThreadID() {
		if(amountspawned>= maxspawnamount) {
			return -1;
		} else {
			return threadid;
		}
	}
	public int getBuffersNeeded() {
		return buffersneeded;
	}
	public int getAmount() {
		return maxspawnamount;
	}
	public int getTiming() {
		return timing;
	}
	public void setBuffers(IntBuffer vbos, IntBuffer vaos) {
		this.vbos = vbos;
		this.vaos = vaos;
	}
	public Spawner(Troop type, TextureHolder[] th, String[] parts, int textureid, int bullettextureid, int explosiontextureid) {
		this.th = th;
		this.parts =parts;
		this.textureid = textureid;
		this.bullettextureid =bullettextureid;
		this.explosiontextureid = explosiontextureid;
		this.enemytype = type;
		this.timing = Integer.valueOf(parts[3]);
		this.maxspawnamount = Integer.valueOf(parts[4]);
		this.buffersneeded = 3+enemytype.getEnemy().getAmountOfGuns();
	}
	public void update(Controller parent, Player player, int[] timings) {
		enemies.add(((Troop) new Troop(new Vector2f(Float.valueOf(parts[1]), Float.valueOf(parts[2])), 
				enemytype.gettexid(), parent, enemytype.getEnemy().getEnemyPath(), player, player.getBullets(), th, enemytype.getEnemy().getLti(),
				enemytype.getEnemy().getHti(), enemytype.getEnemy().getSizes(), enemytype.getEnemy().getPattern(), 
				enemytype.getEnemy().getHealth(), 200, 
				bullettextureid, explosiontextureid, enemytype.getEnemy().getMovementStyle(), 
				enemytype.getEnemy().getAnimationType(), enemytype.getEnemy().getCollisions())));
		enemies.get(enemies.size()-1).finish(vbos, vaos, timings);
		amountspawned+=1;
	}
	public ArrayList<Troop> getEnemies() {
		return enemies;
	}
	public void setTiming(int index) {
		threadid=index;
	}
	public void render(ShaderHandler sh, DisplaySetup d, DataUtils util) {
		for(int i=0; i<enemies.size(); i++) {
			enemies.get(i).update(sh, d, util);
			enemies.get(i).getHealth();
		}
	}
}
