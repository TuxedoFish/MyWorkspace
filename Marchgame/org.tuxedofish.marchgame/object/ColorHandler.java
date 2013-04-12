package object;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.lwjgl.util.vector.Vector2f;

import utils.TextureUtils;

public class ColorHandler {
	private static Vector2f colcood = new Vector2f(0.5f, 0.5f);
	
	public Vector2f getColCood() {
		return colcood;
	}
	public int newCol(Color c, ArrayList<Color> cols) {
		BufferedImage b = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics g = b.getGraphics();
		
		g.setColor(c);
		g.fillRect(0, 0, 1, 1);
		
		cols.add(c);
		
		TextureUtils util = new TextureUtils();
		return util.binddata(b);
	}
}
