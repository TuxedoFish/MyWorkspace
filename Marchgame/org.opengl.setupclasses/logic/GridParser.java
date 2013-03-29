package logic;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.lwjgl.util.vector.Vector2f;

import texture.TextureHolder;

public class GridParser {
	public TextureHolder parseFile(File lvl, int blocksize) {
		try {
			return parseGrid(ImageIO.read(lvl.getAbsoluteFile()), blocksize);
		} catch (IOException e) {
			System.err.println("err reading lvl file");
			e.printStackTrace();
		}
		System.exit(1);
		return null;
	}
	public TextureHolder parseGrid(BufferedImage lvl, int blocksize) {
		TextureHolder th = new TextureHolder(lvl.getHeight(), lvl);
		
		for(int i = 0; i < lvl.getHeight()/blocksize; i++) {
			for(int j = 0; j < lvl.getWidth()/blocksize; j++) {
				th.addTexture(new Vector2f((j*(blocksize+1)) + 1, (i*(blocksize+1)) + 1), blocksize);
			}
		}
		
		return th;
	}
	public TextureHolder parseGrid(BufferedImage lvl, int blockwidth, int blockheight) {
		TextureHolder th = new TextureHolder(lvl.getWidth(), lvl.getHeight(), lvl);
		
		for(int i = 0; i < lvl.getHeight()/blockheight; i++) {
			for(int j = 0; j < lvl.getWidth()/blockwidth; j++) {
				th.addTexture(new Vector2f(j*(blockwidth+1) + 1, i*(blockheight+1) + 1), blockwidth, blockheight);
			}
		}
		
		return th;
	}
}
