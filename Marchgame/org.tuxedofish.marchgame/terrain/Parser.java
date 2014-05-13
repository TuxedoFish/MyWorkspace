package terrain;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

import texture.TextureHolder;
import utils.MathUtils;

public class Parser {
	public ArrayList<Block> parseFile(File lvl, Vector2f pos, TextureHolder th) {
		try {
			return parseLevel(ImageIO.read(lvl), pos, th);
		} catch (IOException e) {
			System.err.println("err reading lvl file");
			e.printStackTrace();
		}
		System.exit(1);
		return null;
	}
	public void drawLevel(BufferedImage level, TextureHolder th) {
		BufferedImage img = new BufferedImage(level.getWidth()*50, level.getHeight()*50, BufferedImage.TYPE_INT_ARGB);
		
		Graphics g = img.getGraphics();
		
		for(int i = 0; i < level.getWidth(); i++) {
			for(int j = 0; j < level.getHeight(); j++) {
//				if(new Color(level.getRGB(j, i)).equals(Color.green)) {
//					g.drawImage()
//				}
//				//grass-pinkflower
//				if(new Color(level.getRGB(j, i)).equals(new Color(255, 0, 255))) {
//					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 15, 50, 50));
//				}
				//path
				if(new Color(level.getRGB(i, j)).equals(new Color(255, 120, 0))) {
					g.drawImage((Image)th.getImg().getSubimage((0*51)+1, (0*51)+1, 50, 50), i*50, j*50, null);
				}
				//rightpath
				if(new Color(level.getRGB(i, j)).equals(new Color(100, 100, 100))) {
					g.drawImage((Image)th.getImg().getSubimage((5*51)+1, (0*51)+1, 50, 50), i*50, j*50, null);
				}
				//bottompath
				if(new Color(level.getRGB(i, j)).equals(new Color(100, 20, 20))) {
					g.drawImage((Image)th.getImg().getSubimage((4*51)+1, (5*51)+1, 50, 50), i*50, j*50, null);
				}
//				//port
//				if(new Color(level.getRGB(j, i)).equals(new Color(255, 50, 50))) {
//					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 46, 50, 50));
//				}
				//flowerpot
				if(new Color(level.getRGB(i, j)).equals(new Color(255, 200, 160))) {
					g.drawImage((Image)th.getImg().getSubimage((6*51)+1, (4*51)+1, 50, 50), i*50, j*50, null);
				}
				//differentpath
				if(new Color(level.getRGB(i, j)).equals(new Color(200, 200, 200))) {
					g.drawImage((Image)th.getImg().getSubimage((5*51)+1, (4*51)+1, 50, 50), i*50, j*50, null);
				}
				//bottompath
				if(new Color(level.getRGB(i, j)).equals(new Color(100, 20, 20))) {
					g.drawImage((Image)th.getImg().getSubimage((4*51)+1, (4*51)+1, 50, 50), i*50, j*50, null);
				}
				//				//water
				if(new Color(level.getRGB(i, j)).equals(new Color(0, 0, 255))) {
					g.drawImage((Image)th.getImg().getSubimage((1*51)+1, (0*51)+1, 50, 50), i*50, j*50, null);
				}
				//grass
				if(new Color(level.getRGB(i, j)).equals(Color.green)) {
					g.drawImage((Image)th.getImg().getSubimage((6*51)+1, (0*51)+1, 50, 50), i*50, j*50, null);
				}
				//dirty path
				if(new Color(level.getRGB(i, j)).equals(new Color(60, 60, 60))) {
					g.drawImage((Image)th.getImg().getSubimage((7*51)+1, (4*51)+1, 50, 50), i*50, j*50, null);
				}
			}
		}
		
		g.dispose();
		
		try {
			ImageIO.write(img, "png", new File("currentlevel.png"));
		} catch (IOException e) {
			System.err.println("err writing image");
			e.printStackTrace();
			System.exit(1);
		}
	}
	public ArrayList<Block> parseLevel(BufferedImage lvl, Vector2f pos, TextureHolder th) {
		ArrayList<Block> blocks = new ArrayList<Block>();
		final Block b = new Block(0.0f, 0.0f, 0, 50, 50);
		
		drawLevel(lvl, th);
		
		for(int i = 0; i < lvl.getHeight(); i++) {
			for(int j = 0; j < lvl.getWidth(); j++) {
				//grass
				if(new Color(lvl.getRGB(j, i)).equals(Color.green)) {
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 6, 50, 50));
				}
				//grass-pinkflower
				if(new Color(lvl.getRGB(j, i)).equals(new Color(255, 0, 255))) {
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 15, 50, 50));
				}
				//path
				if(new Color(lvl.getRGB(j, i)).equals(new Color(255, 120, 0))) {
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 0, 50, 50));
				}
				//flowerpot
				if(new Color(lvl.getRGB(j, i)).equals(new Color(255, 200, 160))) {
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 46, 50, 50));
				}
				//dirty path
				if(new Color(lvl.getRGB(j, i)).equals(new Color(100, 100, 100))) {
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 5, 50, 50));
				}
				//cleaner path
				if(new Color(lvl.getRGB(j, i)).equals(new Color(60, 60, 60))) {
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 47, 50, 50));
				}
				//differentpath
				if(new Color(lvl.getRGB(j, i)).equals(new Color(200, 200, 200))) {
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 45, 50, 50));
				}
				//bottompath
				if(new Color(lvl.getRGB(j, i)).equals(new Color(100, 20, 20))) {
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 44, 50, 50));
				}
				//water
				if(new Color(lvl.getRGB(j, i)).equals(new Color(0, 0, 255))) {
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 1, 50, 50));
				}
				//cliff right
				if(new Color(lvl.getRGB(j, i)).equals(new Color(0, 200, 0))) {
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 7, 50, 50));
				}
				//cliff left
				if(new Color(lvl.getRGB(j, i)).equals(new Color(0, 150, 0))) {
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 8, 50, 50));
				}
				//cliff top
				if(new Color(lvl.getRGB(j, i)).equals(new Color(0, 100, 0))) {
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 9, 50, 50));
				}
				//cliff bottom
				if(new Color(lvl.getRGB(j, i)).equals(new Color(0, 50, 0))) {
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 10, 50, 50));
				}
				//cliff bottom-right
				if(new Color(lvl.getRGB(j, i)).equals(new Color(20, 200, 20))) {
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 11, 50, 50));
				}
				//cliff bottom-left
				if(new Color(lvl.getRGB(j, i)).equals(new Color(20, 150, 20))) {
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 12, 50, 50));
				}
				//cliff top-right
				if(new Color(lvl.getRGB(j, i)).equals(new Color(10, 200, 10))) {
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 13, 50, 50));
				}
				//cliff top-left
				if(new Color(lvl.getRGB(j, i)).equals(new Color(10, 150, 10))) {
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 14, 50, 50));
				}
				if(new Color(lvl.getRGB(j, i)).equals(new Color(185, 255, 255))) {
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 22, 50, 50));
				}
				if(new Color(lvl.getRGB(j, i)).equals(new Color(200, 255, 255))) {
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 23, 50, 50));
				}
				if(new Color(lvl.getRGB(j, i)).equals(new Color(240, 255, 255))) {
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 24, 50, 50));
				}
				if(new Color(lvl.getRGB(j, i)).equals(new Color(180, 255, 255))) {
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 25, 50, 50));
				}
				if(new Color(lvl.getRGB(j, i)).equals(new Color(160, 255, 255))) {
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 26, 50, 50));
				}
				if(new Color(lvl.getRGB(j, i)).equals(new Color(120, 255, 255))) {
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 27, 50, 50));
				}
				if(new Color(lvl.getRGB(j, i)).equals(new Color(80, 255, 255))) {
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 28, 50, 50));
				}
				if(new Color(lvl.getRGB(j, i)).equals(new Color(50, 255, 255))) {
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 29, 50, 50));
				}
				if(new Color(lvl.getRGB(j, i)).equals(new Color(20, 255, 255))) {
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 30, 50, 50));
				}
				if(new Color(lvl.getRGB(j, i)).equals(new Color(255, 160, 160))) {
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 31, 50, 50));
				}
				if(new Color(lvl.getRGB(j, i)).equals(new Color(112, 35, 84))) {
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 1, 50, 50));
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 32, 50, 50));
				}
				if(new Color(lvl.getRGB(j, i)).equals(new Color(255, 216, 241))) {
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 1, 50, 50));
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 33, 50, 50));
				}
				if(new Color(lvl.getRGB(j, i)).equals(new Color(248, 124, 144))) {
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 1, 50, 50));
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 34, 50, 50));
				}
				if(new Color(lvl.getRGB(j, i)).equals(new Color(255, 6, 241))) {
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 1, 50, 50));
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 35, 50, 50));
				}
				if(new Color(lvl.getRGB(j, i)).equals(new Color(135, 0, 86))) {
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 1, 50, 50));
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 43, 50, 50));
				}
				if(new Color(lvl.getRGB(j, i)).equals(new Color(106, 0, 67))) {
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 1, 50, 50));
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 41, 50, 50));
				}
				if(new Color(lvl.getRGB(j, i)).equals(new Color(255, 0, 160))) {
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 1, 50, 50));
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 42, 50, 50));
				}
				if(new Color(lvl.getRGB(j, i)).equals(new Color(255, 146, 215))) {
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 1, 50, 50));
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 40, 50, 50));
				}
			}
		}
		float w = 50.0f/Display.getWidth();
		float h = 50.0f/Display.getHeight();
		MathUtils u = new MathUtils();
		for(int i=0; i<blocks.size(); i++) {
			if(blocks.get(i).getTexid() == 15) {
				int texid = blocks.get(i).getTexid();
				boolean left = false, right = false, top = false, bottom = false;
				for(int k=0; k<blocks.size(); k++) {
					if(blocks.get(k).getTexid() >= texid && blocks.get(k).getTexid() <= texid+6) {
						if(u.closeEnough(blocks.get(k).y, blocks.get(i).y, 0.001f)) {
							if(u.closeEnough(blocks.get(k).x, blocks.get(i).x + w, 0.001f)) right = true;
							if(u.closeEnough(blocks.get(k).x, blocks.get(i).x - w, 0.001f)) left = true;
						}
						if(u.closeEnough(blocks.get(k).x, blocks.get(i).x, 0.001f)) {
							if(u.closeEnough(blocks.get(k).y, blocks.get(i).y - h, 0.001f)) bottom = true;
							if(u.closeEnough(blocks.get(k).y, blocks.get(i).y + h, 0.001f)) top = true;
						}
					}
				}
				if(!right && left && bottom && top) {blocks.get(i).setTexID(texid+1);}
				if(right && !left && bottom && top) {blocks.get(i).setTexID(texid+2);}
				if(right && !left && !top && bottom) {blocks.get(i).setTexID(texid+3);}
				if(!right && left && !top && bottom) {blocks.get(i).setTexID(texid+4);}
				if(!right && left && top && !bottom) {blocks.get(i).setTexID(texid+5);}
				if(right && !left && top && !bottom) {blocks.get(i).setTexID(texid+6);}
			}
		}
		return blocks;
	}
}
