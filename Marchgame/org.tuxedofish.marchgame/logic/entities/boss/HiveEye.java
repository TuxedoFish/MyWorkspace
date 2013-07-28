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
	public HiveEye(Controller parent, Player player, ControllerTimer ct) {
		super(new Vector2f(0.0f, 0.0f), 0, parent, null, player, player.getBullets(), ct.addTimeStep(200));
		ImageReturn images = new ImageReturn(); GridParser gp = new GridParser();
		try {
			TextureHolder texture = gp.parseGrid(images.getImage("bosses/ship.png"), 256, 67);
			Sprite boss1 = new Sprite(images.getImage("bosses/ship.png"), parent, 768, 201, texture, 0, new Vector2f(-1.0f, 8.75f));
			
			ArrayList<ShootType> shoottypes = new ArrayList<>();
			shoottypes.add(new ShootType( new int[]{ct.addTimeStep(2000), ct.addTimeStep(100)}, new int[]{1, 4}, 1));
			
			addSprite(boss1, 0, 9, 50, 1, true, ct.addTimeStep(200), shoottypes);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void update() {
		shoot(getShootThreadIDs().get(0));
	}
}
