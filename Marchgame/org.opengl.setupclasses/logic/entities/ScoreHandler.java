package logic.entities;

import images.ImageReturn;

import java.awt.GradientPaint;
import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

import logic.GridParser;

import object.Sprite;
import shader.ShaderHandler;
import start.Controller;
import start.DisplaySetup;
import texture.TextureHolder;
import utils.DataUtils;

public class ScoreHandler extends ArrayList<ScorePellet>{
	private Sprite pellet;
	private Vector2f currentpos = new Vector2f(0.0f, 0.0f);
	private Player player;
	private Controller parent;
	
	public ScoreHandler(Controller parent, Player player) {
		ImageReturn images = new ImageReturn(); GridParser gp = new GridParser();
		
		this.parent = parent;
		this.player = player;
		
		TextureHolder pellettex;
		try {
			pellettex = gp.parseGrid(images.getImage("score.png"), 20.0f);
			pellet = new Sprite(images.getImage("score.png"), parent, 50, 50, pellettex, 0, new Vector2f(0.0f, 0.0f));
		} catch (IOException e) {
			System.err.println("err at scrollhandler");
			e.printStackTrace();
		}
	}
	public void finish(int vboid, int vaoid) {
		pellet.finish(vboid, vaoid);
	}
	public void render(ShaderHandler sh, DisplaySetup d, DataUtils util) {
		for(int i=0; i<size(); i++) {
			pellet.changePos(get(i).getPos().x-currentpos.x, get(i).getPos().y-currentpos.y);
			pellet.changeTexture(get(i).getTexid());
			if(get(i).getAnimationWait() > 3) {
				if(get(i).getTexid()<11) {
					get(i).changeTexid(1);
				} else {
					get(i).setTexid(0);
				}
				get(i).resetWait();
			} else {
				get(i).animationWait();
			}
			currentpos = new Vector2f(get(i).getPos().x, get(i).getPos().y);
			pellet.render(sh, util, 0);
			if(get(i).contains(player.getPos(), (float)player.getWidth()/Display.getWidth(), 
					(float)player.getHeight()/Display.getHeight(), d)) {
				parent.score(get(i).getScore());
				remove(i);
			}
		}
	}
}
