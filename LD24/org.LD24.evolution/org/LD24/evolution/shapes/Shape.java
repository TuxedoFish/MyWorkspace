package org.LD24.evolution.shapes;

import java.awt.Polygon;

public class Shape {
	private Polygon me = new Polygon();
	private int startx;
	private int starty;
	private int currentangle;
	private int tempx;
	private int tempy;
	private int length;
	
	public Shape(int points, int startx, int starty, int lengthofsides) {
		this.startx = startx;
		this.starty = starty;
		this.length = lengthofsides;
		
		me.addPoint(this.startx, this.starty);
		create(points);
	}
	public void create(int npoints) {
		currentangle = 90;
		int angle = (180*(npoints-2))/npoints;
		
		for(int i=1;i<npoints;i++) {
			this.startx += Math.sin(Math.toRadians(currentangle)) * this.length;
			this.starty += Math.cos(Math.toRadians(currentangle)) * this.length;
			me.addPoint(this.startx, this.starty);
			currentangle += 180-angle;
		}
	}
	public Polygon getpolygon() {
		return me;
	}
}
