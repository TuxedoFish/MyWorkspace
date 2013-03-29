package texture;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.lwjgl.util.vector.Vector2f;

import utils.Pixel;

public class TextureGrid {
	ArrayList<Pixel> pixels = new ArrayList<Pixel>();
	private int width, height, prevwidth = 0, prevheight = 0;
	
	public TextureGrid(int width, int height) {
		for(int i = 0;i < height;i++) {
			for(int j = 0;j < width;j++) {
				pixels.add(new Pixel(i, j));
			}
		}
		this.width = width;
		this.height = height;
	}
	public void enlarge(BufferedImage atlas) {
		this.prevwidth = this.width;
		this.prevheight = this.height;
		
		this.width = atlas.getWidth();
		this.height = atlas.getHeight();
		
		pixels.clear();
		
		for(int i = 0;i < height;i++) {
			for(int j = 0;j < width;j++) {
				pixels.add(new Pixel(i, j));
			}
		}
	}
	public Vector2f nearestSpace(BufferedImage img) throws Exception{
		if(img.getWidth() > this.width || img.getHeight() > this.height) {
			throw new Exception("enlarge");
		}
		for(int i = 0;i < height;i++) {
			for(int j = 0;j < width;j++) {
				if(pixels.get((i*height) + j).getAllocation() == false) {
					boolean done = false;
					
					if(!(j + img.getWidth() < width && i + img.getHeight() < height)) {
						done = true;
					}
					for(int k = 0; k < img.getHeight() && !done;k++) {
						for(int l = 0; l < img.getWidth() && !done;l++) {
							if(pixels.get(((i + k)*height) + (j + l)).getAllocation() == true) {
								done = true;
							}
						}
					}
					if(!done) {
						if(!(i+img.getWidth() >= width) && !(j+img.getHeight() >= height)) {
							return new Vector2f(i, j);
						}
					}
				}
			}
		}
		throw new Exception("enlarge");
	}
	public void useup(Vector2f drawloc, BufferedImage img) {
		for(int i = 0; i < img.getHeight(); i ++) {
			for(int j = 0; j < img.getWidth(); j ++) {
				pixels.get(get((int)(drawloc.x + j), (int)(drawloc.y + i))).usedup();
			}
		}
	}
	public int get(int x, int y) {
		for(int i = 0; i < pixels.size();i++) {
			if(pixels.get(i).getX() == x && pixels.get(i).getY() == y) {
				return i;
			}
		}
		
		System.err.println("cant find specified x+y @ texturegrid.get(" + x + "," + y + ")");
		System.exit(1);
		return 0;
	}
}
