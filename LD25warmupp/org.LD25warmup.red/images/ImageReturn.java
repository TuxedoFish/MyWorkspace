package images;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageReturn {
	public BufferedImage getImage(String loc) throws IOException {
		return (ImageIO.read(getClass().getResource(loc)));
	}
	public File getImageLoc(String loc) throws IOException {
		return new File((getClass().getResource(loc)).getFile());
	}
}
