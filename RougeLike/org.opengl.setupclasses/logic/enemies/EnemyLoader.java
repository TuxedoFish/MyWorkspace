package logic.enemies;

import images.ImageReturn;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import object.Sprite;

import org.lwjgl.util.vector.Vector2f;

import start.Controller;

public class EnemyLoader {
	public EnemyLoader() {
		
	}
	public ArrayList<Enemy> load(BufferedImage enemies, Controller parent, Sprite player, ArrayList<EnemyBullet> bullets) {
		ArrayList<Enemy> allenemies = new ArrayList<Enemy>();
		ImageReturn images = new ImageReturn();
		
		for(int i=0; i<enemies.getWidth(); i++) {
			for(int j=0; j<enemies.getHeight(); j++) {
				Color c = new Color(enemies.getRGB(i, j));
				if(c.getRed() != 0 || c.getGreen() != 0 || c.getBlue() != 0) {
					try {
						BufferedImage path = images.getImage(c.getRed() + " " + c.getGreen() + " " + 
								c.getBlue() + ".png");
						BufferedReader reader = images.getFile(c.getRed() + " " + c.getGreen() + " " + 
								c.getBlue() + ".txt");
						String texture = reader.readLine();
						Integer lti = Integer.parseInt(reader.readLine());
						Integer hti = Integer.parseInt(reader.readLine());
						Integer width = Integer.parseInt(reader.readLine());
						Integer pattern = Integer.parseInt(reader.readLine());
						EnemyPath ep = new EnemyPath();
						for(int k=0; k<path.getWidth(); k++) {
							for(int l=0; l<path.getHeight(); l++) {
								Color c2 = new Color(path.getRGB(k, l));
								if(c2.getRed() != 255 || c2.getGreen() != 255 || c2.getBlue() != 255) {
									ep.addPoint(new PathPoint(new Vector2f(k, l), c2.getRed()/10));
								}
							}
						}
						allenemies.add(new Enemy(new Vector2f(i*25, (enemies.getHeight()*25)-(j*25)), 0, parent, 
								ep, player, bullets, texture, lti, hti, width, pattern));
					} catch (IOException e) {
						System.err.println("err getting img");
						System.exit(1);
						e.printStackTrace();
					}
				}
			}
		}
		return allenemies;
	}
}
