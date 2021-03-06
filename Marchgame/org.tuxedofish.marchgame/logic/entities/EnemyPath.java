package logic.entities;

import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.util.vector.Vector2f;

public class EnemyPath {
	ArrayList<PathPoint> points = new ArrayList<PathPoint>();
	private int noofpoints = 0;
	
	public void addPoint(PathPoint pp) {
		points.add(pp);
		noofpoints += 1;
	}
	public int getSize() {
		return noofpoints;
	}
	public PathPoint getPoint(int index) {
		for(int i=0; i<points.size(); i++) {
			if(points.get(i).getIndex() == index) {
				return points.get(i);
			}
		}
		StackTraceElement[] stack = Thread.currentThread().getStackTrace();
		for(int j=0; j<stack.length; j++) {
			System.err.println(stack[j].toString());
		}
		System.err.println("err at EnemyPath");
		System.exit(1);
		return(null);
	}
}
