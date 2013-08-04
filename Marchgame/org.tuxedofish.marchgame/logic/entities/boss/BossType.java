package logic.entities.boss;

import java.util.ArrayList;

import logic.entities.Bullet;
import logic.entities.EnemyPath;
import logic.entities.Player;

import org.lwjgl.util.vector.Vector2f;

import start.Controller;

public class BossType extends Boss{
	private int threadid;
	
	public BossType(Vector2f pos, int texid, Controller parent, EnemyPath ep,
			Player player, ArrayList<Bullet> playerbullets, int threadid) {
		super(pos, texid, parent, ep, player, playerbullets);
		this.threadid = threadid;
	}
	public void update() {}
	public int getUpdateThreadID() {
		return threadid;
	}
}
