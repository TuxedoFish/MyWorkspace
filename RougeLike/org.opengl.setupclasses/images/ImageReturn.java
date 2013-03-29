package images;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

public class ImageReturn {
	public BufferedImage getImage(String loc) throws IOException {
		return (ImageIO.read(getClass().getResourceAsStream(loc)));
	}
	public File getImageLoc(String loc) throws IOException {
		return new File((getClass().getResource(loc)).getFile());
	}
	public BufferedReader getFile(String loc) throws IOException {
		return new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(loc)));
	}
}
