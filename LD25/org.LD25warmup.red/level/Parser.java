package level;

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
		final Block b = new Block();
		
		for(int i = 0; i < lvl.getHeight(); i++) {
			for(int j = 0; j < lvl.getWidth(); j++) {
				//grass
				if(new Color(lvl.getRGB(j, i)).equals(Color.green)) {
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y - ((float)(i*b.getHeight())/Display.getHeight()), 1));
				}
				//dirt
				if(new Color(lvl.getRGB(j, i)).equals(new Color(255, 120, 0))) {
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y - ((float)(i*b.getHeight())/Display.getHeight()), 0));
				}
				//stone
				if(new Color(lvl.getRGB(j, i)).equals(new Color(100, 100, 100))) {
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y - ((float)(i*b.getHeight())/Display.getHeight()), 2));
				}
				//wood
				if(new Color(lvl.getRGB(j, i)).equals(new Color(255, 100, 0))) {
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y - ((float)(i*b.getHeight())/Display.getHeight()), 3));
				}
				//peepy hole
				if(new Color(lvl.getRGB(j, i)).equals(new Color(50, 50, 50))) {
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y - ((float)(i*b.getHeight())/Display.getHeight()), 4));
				}
				//flag(1-3)
				if(new Color(lvl.getRGB(j, i)).equals(new Color(50, 50, 255))) {
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y - ((float)(i*b.getHeight())/Display.getHeight()), 5));
				}
				if(new Color(lvl.getRGB(j, i)).equals(new Color(50, 50, 100))) {
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y - ((float)(i*b.getHeight())/Display.getHeight()), 6));
				}
				if(new Color(lvl.getRGB(j, i)).equals(new Color(50, 50, 150))) {
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y - ((float)(i*b.getHeight())/Display.getHeight()), 7));
				}
			}
		}
		return blocks;
	}
}
