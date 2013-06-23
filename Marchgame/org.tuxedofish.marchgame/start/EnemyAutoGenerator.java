package start;

import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import images.ImageReturn;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import logic.GridParser;
import object.Sprite;

import org.lwjgl.util.vector.Vector2f;

import utils.LineCollection;

public class EnemyAutoGenerator extends Thread{
	private Controller parent;
	private int enemytexid;
	private String enemytexloc;
	private Vector2f enemysize;
	
	public EnemyAutoGenerator(Controller parent) {
		this.parent = parent;
		this.start();
	}
	@Override
	public void run() {
		while(true) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			if(parent.isUpdate()) {
				if(parent.getStage() == 0) {
					parent.resetPolygonLines();
					try {
						BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
						System.out.println("texloc : ");
						enemytexloc = (bufferRead.readLine());
						System.out.println("width : ");
					    int width = Integer.valueOf(bufferRead.readLine());
						System.out.println("height : ");
						int height = Integer.valueOf(bufferRead.readLine());
						enemytexid = 0;
						enemysize = new Vector2f(width, height);
						parent.changeEnemyBg(enemytexloc, enemytexid, enemysize);
					} catch (IOException e) {
						e.printStackTrace();
					}
					parent.setStage(1);
				} else if(parent.getStage() == 1){
					try {
						BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
						enemytexid += 1;
						parent.changeEnemyBg(enemytexloc, enemytexid, enemysize);
						System.out.println("carry on? 0/1 true/false : ");
						String carryon = (bufferRead.readLine());
						if(carryon.contains("0")) {
							while(carryon.contains("0")) {
								System.out.println("use same collision? 0/1 true/false : ");
								carryon = (bufferRead.readLine());
								if(carryon.contains("0")) {
									parent.copy(enemytexid-1);
									enemytexid += 1;
									parent.changeEnemyBg(enemytexloc, enemytexid, enemysize);
								}
							}
						} else {
							parent.resetPathPoints();
							parent.setStage(2);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					try {
						ArrayList<String> guns = new ArrayList<String>();
						BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
						System.out.println("texloc : ");
						String texloc = (bufferRead.readLine());
						System.out.println("lti : ");
						String lti = (bufferRead.readLine());
						System.out.println("hti : ");
						String hti = (bufferRead.readLine());
						System.out.println("width : ");
						String width = (bufferRead.readLine());
						System.out.println("pattern : ");
						String pattern = (bufferRead.readLine());
						System.out.println("health : ");
						String health = (bufferRead.readLine());
						System.out.println("shoottime : ");
						String shoottime = (bufferRead.readLine());
						boolean finished = false;
						while(!finished) {
							System.out.println("new gun? 0/1 true/false : ");
							int newgun = Integer.valueOf(bufferRead.readLine());
							if(newgun == 0) {
								System.out.println("name : ");
								String gunname = bufferRead.readLine(); guns.add(gunname);
								System.out.println("width : ");
								String gunwidth = (bufferRead.readLine()); guns.add(gunwidth);
								System.out.println("height : ");
								String gunheight = (bufferRead.readLine()); guns.add(gunheight);
								System.out.println("visible : ");
								String visible = (bufferRead.readLine()); guns.add(visible);
							} else if(newgun == 1){
								finished = true;
							}
						}
						System.out.println("size : ");
						String size = (bufferRead.readLine());
						System.out.println("movementtype : ");
						String movementtype = bufferRead.readLine();
						System.out.println("animationtype : ");
						String animationtype = (bufferRead.readLine());
						
						PrintWriter out
						   = new PrintWriter(new BufferedWriter(new FileWriter("example.txt")));
						for(int i=0; i<parent.getEnemyPathPoints().size(); i++) {
							out.println("ep " + parent.getEnemyPathPoints().get(i).x + " " + parent.getEnemyPathPoints().get(i).y + " " + i);
						}
						out.println(texloc); out.println(lti); out.println(hti); out.println(width); out.println(pattern); out.println(health); out.println(shoottime); 
						out.println(size);  
						for(int i=0; i<guns.size(); i+=4) {
							out.println("gun " + guns.get(i*4) + " " + guns.get((i*4) + 1) + " " + guns.get((i*4) + 2) + " " + guns.get((i*4) + 3));
						}
						out.println(movementtype); out.println(animationtype);
						for(int i=0; i<parent.getEnemyCollisionPoints().size(); i++) {
							out.println("collision " + ((parent.getEnemyCollisionPoints().get(i).x+1.0f)/2.0f)*Integer.valueOf(size) + " "+
						((parent.getEnemyCollisionPoints().get(i).y+1.0f)/2.0f)*Integer.valueOf(size) + " " + parent.getEnemyCollisonTexids().get(i));
						}
						out.close();
						parent.setStage(0);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				parent.doneUpdate();
			}
		}
	}
}
