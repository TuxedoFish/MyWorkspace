package logic.entities.boss;

import java.io.IOException;

import logic.GridParser;
import logic.entities.Player;
import images.ImageReturn;
import object.Sprite;

import org.lwjgl.util.vector.Vector2f;

import start.Controller;
import start.ControllerTimer;
import texture.TextureHolder;

public class HiveEye extends Boss{
	public HiveEye(Controller parent, Player player, ControllerTimer ct) {
		super(new Vector2f(0.0f, 0.0f), 0, parent, null, player, player.getBullets());
		ImageReturn images = new ImageReturn(); GridParser gp = new GridParser();
		try {
			TextureHolder texture = gp.parseGrid(images.getImage("bosses/ship.png"), 256, 67);
			Sprite boss1 = new Sprite(images.getImage("bosses/ship.png"), parent, 768, 201, texture, 0, new Vector2f(-1.0f, 8.75f));
			
			addSprite(boss1, 0, 0, 50, 1, true, ct.addTimeStep(200), new int[]{ct.addTimeStep(2000), ct.addTimeStep(100)}, 
					new int[]{1, 4});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
