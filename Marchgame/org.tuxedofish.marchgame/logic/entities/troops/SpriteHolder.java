package logic.entities.troops;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import logic.GridParser;

import texture.TextureHolder;
import utils.TextureUtils;

public class SpriteHolder {
	private ArrayList<String> indexs = new ArrayList<String>();
	private ArrayList<BufferedImage> textures = new ArrayList<BufferedImage>();
	private ArrayList<TextureHolder> grids = new ArrayList<TextureHolder>();
	private ArrayList<Integer> texids = new ArrayList<Integer>();
	private ArrayList<Integer> sizes = new ArrayList<Integer>();
	
	public void addTexture(BufferedImage img, String index, int size) {
		textures.add(img);
		indexs.add(index);
		sizes.add(size);
	}
	public Integer getTextureID(String index) {
		return texids.get(indexs.indexOf(index));
	}
	public TextureHolder getTexture(String index) {
		return grids.get(indexs.indexOf(index));
	}
	public BufferedImage getImage(String index) {
		return textures.get(indexs.indexOf(index));
	}
	public int getSize() {
		return textures.size();
	}
	public void finish() {
		TextureUtils util = new TextureUtils();
		GridParser gp = new GridParser();
		
		for(int i=0; i<textures.size(); i++) {
			texids.add(util.binddata(textures.get(i)));
			grids.add(gp.parseGrid(textures.get(i), sizes.get(i)));
		}
	}
}
