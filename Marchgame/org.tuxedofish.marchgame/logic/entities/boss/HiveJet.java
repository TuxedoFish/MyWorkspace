package logic.entities.boss;

import images.ImageReturn;

import java.io.IOException;
import java.util.ArrayList;

import logic.GridParser;
import logic.entities.Player;
import object.Sprite;

import org.lwjgl.util.vector.Vector2f;

import start.Controller;
import start.ControllerTimer;
import texture.TextureHolder;

public class HiveJet extends BossType{
	public HiveJet(Controller parent, Player player, ControllerTimer ct) {
		super(new Vector2f(0.0f, 0.0f), 0, parent, null, player, player.getBullets(), ct.addTimeStep(200));
		ImageReturn images = new ImageReturn(); GridParser gp = new GridParser();
		try {
			TextureHolder texture = gp.parseGrid(images.getImage("bosses/hivejet.png"), 128, 128);
			TextureHolder jet = gp.parseGrid(images.getImage("bosses/jet.png"), 32);
			Sprite boss1 = new Sprite(images.getImage("bosses/hivejet.png"), parent, 256, 256, texture, 0, new Vector2f(0.0f, 8.75f));
			Sprite boss2 = new Sprite(images.getImage("bosses/jet.png"), parent, 32, 32, jet, 0, new Vector2f(0.1f, 8.4f));	
			Sprite boss3 = new Sprite(images.getImage("bosses/jet.png"), parent, 32, 32, jet, 0, new Vector2f(0.2f, 8.4f));
			
			ArrayList<ShootType> shoottypes = new ArrayList<>();
			shoottypes.add(new ShootType(new int[]{ct.addTimeStep(2000), ct.addTimeStep(100)}, new int[]{1, 4}, 1));
			
			addSprite(boss2, 0, 4, 10, 1, true, ct.addTimeStep(200), shoottypes);
			addSprite(boss3, 0, 4, 10, 1, true, ct.addTimeStep(200), shoottypes);
			addSprite(boss1, 0, 0, 10, 1, false, ct.addTimeStep(200), shoottypes);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void update() {
		shoot(getShootThreadIDs().get(0));
		shoot(getShootThreadIDs().get(1));
		shoot(getShootThreadIDs().get(2));
	}
}
