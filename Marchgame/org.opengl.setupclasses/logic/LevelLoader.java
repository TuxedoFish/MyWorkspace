package logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import object.ObjectLoader;
import object.Shape;

public class LevelLoader {
	public ArrayList<Shape> loadLevel(String levelname) {
		ArrayList<Shape> shapes = new ArrayList<Shape>();
		ObjectLoader ol = new ObjectLoader();
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(levelname+"/index.txt")));
			String line = "";
			ArrayList<String> names = new ArrayList<String>();
			ArrayList<Integer> texids = new ArrayList<Integer>();
			
			while(line != null) {
				line = reader.readLine();
				if(line != null) {
					String[] parts = line.split(" ");
					String name = parts[0];
					String matrixloc = parts[1];
					
					if(!(names.contains(name))) {
						shapes.add(ol.loadShape(getClass().getResourceAsStream(levelname + "/" + name + "/texture.png"),
								getClass().getResourceAsStream(levelname + "/" + name + "/data.obj"), 
								getClass().getResourceAsStream(levelname + "/" + matrixloc +".txt"), -1).createUsable());
					} else {
						shapes.add(ol.loadShape(getClass().getResourceAsStream(levelname + "/" + name + "/texture.png"),
								getClass().getResourceAsStream(levelname + "/" + name + "/data.obj"), 
								getClass().getResourceAsStream(levelname + "/" + name +"/matrix.txt"), texids.get(names.indexOf(name))).createUsable());
					}
					shapes.get(shapes.size()-1).name(name);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return shapes;
	}
}
