package data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class DataHandler {
	public void update(String filename, String[] additions) {
		PrintWriter out;
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter("org.tuxedofish.marchgame\\data\\" + filename, true)));
			for(int i=0; i<additions.length; i++) {
				out.println(additions[i]);
			}
		    out.close();
		} catch (IOException e) {
			System.err.println("err at DataHandler");
			e.printStackTrace();
		}
	}
	public ArrayList<String> read(String filename) {
		ArrayList<String> data = new ArrayList<String>();
		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader("org.tuxedofish.marchgame\\data\\" + filename));
			String line;
			while((line=in.readLine()) != null) {
				data.add(line);
			}
			in.close();
		} catch (IOException e) {
			System.err.println("err at DataHandler");
			e.printStackTrace();
		}
		return data;
	}
}
