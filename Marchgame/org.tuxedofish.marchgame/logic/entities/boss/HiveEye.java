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
			TextureHolder texture = gp.parseGrid(images.getImage("bosspart1.png"), 19);
			TextureHolder eye = gp.parseGrid(images.getImage("bosspart2.png"), 19);
			Sprite boss1 = new Sprite(images.getImage("bosspart1.png"), parent, 100, 100, texture, 0, new Vector2f(0.5f, 8.75f));
			Sprite boss2 = new Sprite(images.getImage("bosspart1.png"), parent, 100, 100, texture, 0, new Vector2f(-0.5f, 8.75f));
			Sprite boss3 = new Sprite(images.getImage("bosspart2.png"), parent, 50, 50, eye, 0, new Vector2f(-0.4f, 8.9f));
			Sprite boss4 = new Sprite(images.getImage("bosspart2.png"), parent, 50, 50, eye, 0, new Vector2f(0.0f, 8.9f));
			Sprite boss5 = new Sprite(images.getImage("bosspart2.png"), parent, 50, 50, eye, 0, new Vector2f(0.4f, 8.9f));
			Sprite boss6 = new Sprite(images.getImage("bosspart1.png"), parent, 800, 800, texture, 0, new Vector2f(-0.5f, 9.7f));
			
			addSprite(boss6, 0, 3, 1000, 1, false, ct.addTimeStep(200), new int[]{ct.addTimeStep(2000), ct.addTimeStep(100)}, 
					new int[]{1, 4});
			addSprite(boss1, 0, 3, 50, 1, true, ct.addTimeStep(200), new int[]{ct.addTimeStep(2000), ct.addTimeStep(100)}, 
					new int[]{1, 4});
			addSprite(boss2, 0, 3, 50, 1, true, ct.addTimeStep(200), new int[]{ct.addTimeStep(2000), ct.addTimeStep(100)}, 
					new int[]{1, 4});
			addSprite(boss3, 0, 6, 10, 1, true, ct.addTimeStep(200), new int[]{ct.addTimeStep(2000), ct.addTimeStep(100)}, 
					new int[]{1, 4});
			addSprite(boss4, 0, 6, 10, 1, true, ct.addTimeStep(200), new int[]{ct.addTimeStep(2000), ct.addTimeStep(100)}, 
					new int[]{1, 4});
			addSprite(boss5, 0, 6, 10, 1, true, ct.addTimeStep(200), new int[]{ct.addTimeStep(2000), ct.addTimeStep(100)}, 
					new int[]{1, 4});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
