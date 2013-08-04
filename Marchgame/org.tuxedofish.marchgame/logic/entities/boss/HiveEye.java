package logic.entities.boss;

import java.io.IOException;
import java.util.ArrayList;

import logic.GridParser;
import logic.entities.Player;
import images.ImageReturn;
import object.Sprite;

import org.lwjgl.util.vector.Vector2f;

import start.Controller;
import start.ControllerTimer;
import texture.TextureHolder;

public class HiveEye extends BossType{
	private int ticks = 0;
	private int shootingstage = -1;
	
	public HiveEye(Controller parent, Player player, ControllerTimer ct) {
		super(new Vector2f(0.0f, 0.0f), 0, parent, null, player, player.getBullets(), ct.addTimeStep(200));
		ImageReturn images = new ImageReturn(); GridParser gp = new GridParser();
		try {
			TextureHolder texture = gp.parseGrid(images.getImage("bosses/ship.png"), 256, 67);
			Sprite boss1 = new Sprite(images.getImage("bosses/ship.png"), parent, 1028, 268, texture, 0, new Vector2f(-1.0f, 8.75f));
			
			ArrayList<ShootType> shoottypes = new ArrayList<>();
			shoottypes.add(new ShootType(new int[]{ct.addTimeStep(2000), ct.addTimeStep(100)}, new int[]{1, 10}, new int[]{0, 2}));
			shoottypes.add(new ShootType(new int[]{ct.addTimeStep(100), ct.addTimeStep(2000)}, new int[]{20, 1}, new int[]{3, 0}));
			
			addSprite(boss1, 0, 0, 500, 1, true, ct.addTimeStep(200), shoottypes);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void resetTicks() {
		ticks = 0;
		shootingstage = -1;
	}
	@Override
	public void update() {
		if(shootingstage == -1) {
			shootingstage = (int)(Math.random()*100.0f);
			if(shootingstage >= 50) {
				shootingstage = 1;
			} else {
				shootingstage = 0;
			}
			getBossPart(0).setShootType(shootingstage);
		}
		
		ticks += 1;
		
		if(ticks >= 40) {
			resetTicks();
		}
	}
}
