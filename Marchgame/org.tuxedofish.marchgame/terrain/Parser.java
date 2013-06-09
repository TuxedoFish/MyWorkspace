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
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 6, 50, 50));
				}
				//path
				if(new Color(lvl.getRGB(j, i)).equals(new Color(255, 120, 0))) {
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 0, 50, 50));
				}
				//rightpath
				if(new Color(lvl.getRGB(j, i)).equals(new Color(100, 100, 100))) {
					blocks.add(new Block(pos.x + ((float)(j*b.getWidth())/Display.getWidth()), pos.y + ((float)(i*b.getHeight())/Display.getHeight()), 5, 50, 50));
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
			}
		}
		return blocks;
	}
}
