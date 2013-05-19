package terrain;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

public class Parser {
	public ArrayList<Block> parseFile(File lvl, Vector2f pos) {
		try {
			return parseLevel(ImageIO.read(lvl), pos);
		} catch (IOException e) {
			System.err.println("err reading lvl file");
			e.printStackTrace();
		}
		System.exit(1);
		return null;
	}
	public ArrayList<Block> parseLevel(BufferedImage lvl, Vector2f pos) {
		ArrayList<Block> blocks = new ArrayList<Block>();
		final Block b = new Block(0.0f, 0.0f, 0, 50, 50);
		
		for(int i = 0; i < lvl.getHeight(); i++) {
			for(int j = 0; j < lvl.getWidth(); j++) {
				//grass
				if(new Color(lvl.getRGB(j, i)).equals(Color.green)) {
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 1, 50, 50));
				}
				//dirt
				if(new Color(lvl.getRGB(j, i)).equals(new Color(255, 120, 0))) {
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 0, 50, 50));
				}
				//stone
				if(new Color(lvl.getRGB(j, i)).equals(new Color(100, 100, 100))) {
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 5, 50, 50));
				}
				//wood
				if(new Color(lvl.getRGB(j, i)).equals(new Color(0, 0, 0))) {
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 6, 50, 50));
				}
				//peepy hole
				if(new Color(lvl.getRGB(j, i)).equals(new Color(10, 10, 10))) {
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 7, 50, 50));
				}
				//flag(1-3)
				if(new Color(lvl.getRGB(j, i)).equals(new Color(20, 20, 20))) {
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 8, 50, 50));
				}
				if(new Color(lvl.getRGB(j, i)).equals(new Color(30, 30, 30))) {
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 9, 50, 50));
				}
				if(new Color(lvl.getRGB(j, i)).equals(new Color(40, 40, 40))) {
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 10, 50, 50));
				}
				if(new Color(lvl.getRGB(j, i)).equals(new Color(50, 50, 50))) {
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 11, 50, 50));
				}
				if(new Color(lvl.getRGB(j, i)).equals(new Color(60, 60, 60))) {
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 12, 50, 50));
				}
				if(new Color(lvl.getRGB(j, i)).equals(new Color(70, 70, 70))) {
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 13, 50, 50));
				}
				if(new Color(lvl.getRGB(j, i)).equals(new Color(80, 80, 80))) {
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 14, 50, 50));
				}
			}
		}
		return blocks;
	}
}
