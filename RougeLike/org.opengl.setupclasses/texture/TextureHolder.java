package texture;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.lwjgl.util.vector.Vector2f;

import utils.TextureUtils;

public class TextureHolder {
	float parentwidth;
	float parentheight;
	ArrayList<Texture> tex = new ArrayList<Texture>();
	int currenttexid = 0;
	
	int textureid;
	
	public TextureHolder(float parentsize, BufferedImage img) {
		this.parentwidth = parentsize;
		this.parentheight = parentsize;
		
		TextureUtils util = new TextureUtils();
		textureid = util.binddata(img);
	}
	public TextureHolder(float parentwidth, float parentheight, BufferedImage img) {
		this.parentwidth = parentwidth;
		this.parentheight = parentheight;
		
		TextureUtils util = new TextureUtils();
		textureid = util.binddata(img);
	}
	public void addTexture(Vector2f topleft, float size) {
		tex.add(new Texture(currenttexid, topleft, size, parentwidth));
		currenttexid += 1;
	}
	public void addTexture(Vector2f topleft, float width, float height) {
		tex.add(new Texture(currenttexid, topleft, width, height, parentwidth, parentheight));
		currenttexid += 1;
	}
	public int getTexid() {
		return textureid;
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
