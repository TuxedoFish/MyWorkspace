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
	public TextureHolder parseFile(File lvl, float blocksize) {
		try {
			return parseGrid(ImageIO.read(lvl.getAbsoluteFile()), blocksize);
		} catch (IOException e) {
			System.err.println("err reading lvl file");
			e.printStackTrace();
		}
		System.exit(1);
		return null;
	}
	public TextureHolder parseGrid(BufferedImage lvl, float blocksize) {
		TextureHolder th = new TextureHolder(lvl.getHeight(), lvl);
		
		for(float i = 0; i < (lvl.getHeight()/(blocksize))-1.0f; i++) {
			for(float j = 0; j < (lvl.getWidth()/(blocksize))-1.0f; j++) {
				th.addTexture(new Vector2f((j*(blocksize+1.0f)) + 1.0f, (i*(blocksize+1.0f)) + 1.0f), blocksize);
			}
		}
		
		return th;
	}
	public TextureHolder parseGrid(BufferedImage lvl, float blockwidth, float blockheight) {
		TextureHolder th = new TextureHolder(lvl.getWidth(), lvl.getHeight(), lvl);
		
		for(float i = 0; i < (lvl.getHeight()/blockheight)-1.0f; i++) {
			for(float j = 0; j < (lvl.getWidth()/blockwidth)-1.0f; j++) {
				th.addTexture(new Vector2f(j*(blockwidth+1.0f) + 1.0f, i*(blockheight+1.0f) + 1.0f), blockwidth, blockheight);
			}
		}
		
		return th;
	}
}
