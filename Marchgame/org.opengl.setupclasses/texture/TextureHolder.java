package texture;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.lwjgl.util.vector.Vector2f;

import utils.TextureUtils;

public class TextureHolder {
	private int parentwidth;
	private int parentheight;
	private ArrayList<Texture> tex = new ArrayList<Texture>();
	private int currenttexid = 0;
	private BufferedImage img;
	
	public TextureHolder(int parentsize, BufferedImage img) {
		this.parentwidth = parentsize;
		this.parentheight = parentsize;
		this.img = img;
	}
	public TextureHolder(int parentwidth, int parentheight, BufferedImage img) {
		this.parentwidth = parentwidth;
		this.parentheight = parentheight;
		
		TextureUtils util = new TextureUtils();
	}
	public void addTexture(Vector2f topleft, int size) {
		tex.add(new Texture(currenttexid, topleft, size, parentwidth));
		currenttexid += 1;
	}
	public void addTexture(Vector2f topleft, int width, int height) {
		tex.add(new Texture(currenttexid, topleft, width, height, parentwidth, parentheight));
		currenttexid += 1;
	}
	public int getTexid() {
		TextureUtils util = new TextureUtils();
		return util.binddata(img);
	}
	public BufferedImage getImg() {
		return img;
	}
	public Vector2f[] getTextureCoords(int texid) {
		boolean done = false;
		
		for(int i = 0; i < tex.size() && done == false; i++) {
			if(tex.get(i).getTexid() == texid) {
				done = true;
				return tex.get(i).getTextureCoords();
			}
		}
		
		System.err.println("cant find texid : " + texid);
		return null;
	}
}
