package images;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.imageio.ImageIO;

public class ImageReturn {
	public BufferedImage getImage(String loc) throws IOException {
		return (ImageIO.read(getClass().getResourceAsStream(loc)));
	}
	public InputStream getFont(String loc) throws IOException {
		return ((getClass().getResourceAsStream("fonts/" + loc)));
	}
	public File getImageLoc(String loc) throws IOException {
		return new File((getClass().getResource(loc)).getFile());
	}
	public BufferedReader getFile(String loc) throws IOException {
		return new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(loc)));
	}
	public PrintWriter getWriter(String loc) {
		try {
			return new PrintWriter(new BufferedWriter(new FileWriter(getClass().getResource("").getFile() + loc)));
		} catch (IOException e) {
			System.err.println("cannot find location");
			return null;
		}
	}
}
