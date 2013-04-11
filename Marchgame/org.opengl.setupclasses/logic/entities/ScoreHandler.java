package logic.entities;

import images.ImageReturn;

import java.awt.GradientPaint;
import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.util.vector.Vector2f;

import logic.GridParser;

import object.Sprite;
import shader.ShaderHandler;
import start.Controller;
import texture.TextureHolder;
import utils.DataUtils;

public class ScoreHandler extends ArrayList<ScorePellet>{
	private Sprite pellet;
	private Vector2f currentpos = new Vector2f(0.0f, 0.0f);
	
	public ScoreHandler(Controller parent) {
		ImageReturn images = new ImageReturn(); GridParser gp = new GridParser();
		
		//needs to be updated
		TextureHolder pellettex;
		try {
			pellettex = gp.parseGrid(images.getImage("score.png"), 18.0f);
			pellet = new Sprite(images.getImage("score.png"), parent, 50, 50, pellettex, 0, new Vector2f(0.0f, 0.0f));
		} catch (IOException e) {
			System.err.println("err at scrollhandler");
			e.printStackTrace();
		}
	}
	public void finish(int vboid, int vaoid) {
		pellet.finish(vboid, vaoid);
	}
	public void render(ShaderHandler sh, DataUtils util) {
		for(int i=0; i<size(); i++) {
			pellet.changePos(get(i).getPos().x-currentpos.x, get(i).getPos().y-currentpos.y);
			currentpos = new Vector2f(get(i).getPos().x, get(i).getPos().y);
			pellet.render(sh, util, 0);
		}
	}
}
