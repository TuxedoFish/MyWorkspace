package start;

import java.applet.Applet;
import java.io.IOException;
import java.net.URISyntaxException;

public class EntryPoint extends Applet{
	public static void main(String args[]) {
		Controller c = new Controller();
		try {
			c.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
