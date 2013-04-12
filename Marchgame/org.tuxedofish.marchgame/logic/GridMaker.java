package logic;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GridMaker {
	public void makeGrid(int blocksize, int length) {
		BufferedImage img = new BufferedImage(blocksize*length+1, blocksize*length+1, BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();
		
		for(int i = 0; i < length; i++) {
			for(int j = 0; j < length; j++) {
				g.setColor(Color.black);
				g.drawRect(i*blocksize, j*blocksize, blocksize, blocksize);
				
				g.setColor(new Color(255, 130, 240));
				g.fillRect(i*blocksize+1, j*blocksize+1, blocksize - 1, blocksize - 1);
			}
		}
		
		g.dispose();
		
		try {
			ImageIO.write(img, "png", new File("MYFILE.png"));
		} catch (IOException e) {
			System.err.println("err writing image");
			e.printStackTrace();
			System.exit(1);
		}
	}
	public void makeGrid(int blockwidth, int blockheight, int length) {
		BufferedImage img = new BufferedImage(blockwidth*length+1, blockheight*length+1, BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();
		
		for(int i = 0; i < length; i++) {
			for(int j = 0; j < length; j++) {
				g.setColor(Color.black);
				g.drawRect(i*blockwidth, j*blockheight, blockwidth, blockheight);
				
				g.setColor(new Color(255, 130, 240));
				g.fillRect(i*blockwidth+1, j*blockheight+1, blockwidth - 1, blockheight - 1);
			}
		}
		
		g.dispose();
		
		try {
			ImageIO.write(img, "png", new File("CHARACTER.png"));
		} catch (IOException e) {
			System.err.println("err writing image");
			e.printStackTrace();
			System.exit(1);
		}
	}
}
