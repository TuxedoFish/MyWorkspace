package org.LD24.evolution.shapes;

import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;

public class ShapeMaker {
	public ShapeMaker() {
		
	}
	public Polygon combine2shapes(Shape shape1, Shape shape2) {
		ArrayList index1 = new ArrayList<Integer>();
		ArrayList index2 = new ArrayList<Integer>();
		ArrayList lengths = new ArrayList<Integer>();
		Polygon result = new Polygon();
		
		for(int i=0;i<shape1.getpolygon().npoints;i++) {
			for(int j=0;j<shape2.getpolygon().npoints;j++) {
				index1.add(i);
				index2.add(j);
				lengths.add((int) Math.hypot(shape1.getpolygon().xpoints[i] - shape2.getpolygon().xpoints[j], shape1.getpolygon().ypoints[i] - shape2.getpolygon().ypoints[j]));
			}
		}	
		int shortestindex = 900000;//large number so distance will usually be shorter ;)
		
		for(int k=0;k<lengths.size();k++) {
			if((int)lengths.get(k) < shortestindex) {
				shortestindex = (int) lengths.get(k);
			}
		}
		shortestindex = lengths.indexOf(shortestindex);
		for(int l=0;l<shape1.getpolygon().npoints;l++) {
			
			if(l==(int) index1.get(shortestindex)) {
				
				for(int o=(int) index2.get(shortestindex) + 1;o<shape2.getpolygon().npoints;o++) {
					result.addPoint(shape2.getpolygon().xpoints[o], shape2.getpolygon().ypoints[o]);
				}
				if((int) index2.get(shortestindex) > 0) {
					for(int p=0;p<(int) index2.get(shortestindex);p++) {
						result.addPoint(shape2.getpolygon().xpoints[p], shape2.getpolygon().ypoints[p]);
					}
				}
			}else {
				result.addPoint(shape1.getpolygon().xpoints[l], shape1.getpolygon().ypoints[l]);
			}
		}
		
		return result;
	}
	public Polygon combine2shapes(Polygon shape1, Shape shape2) {
		ArrayList index1 = new ArrayList<Integer>();
		ArrayList index2 = new ArrayList<Integer>();
		ArrayList lengths = new ArrayList<Integer>();
		Polygon result = new Polygon();
		
		for(int i=0;i<shape1.npoints;i++) {
			for(int j=0;j<shape2.getpolygon().npoints;j++) {
				index1.add(i);
				index2.add(j);
				lengths.add((int) Math.hypot(shape1.xpoints[i] - shape2.getpolygon().xpoints[j], shape1.ypoints[i] - shape2.getpolygon().ypoints[j]));
			}
		}	
		int shortestindex = 900000;//large number so distance will usually be shorter ;)
		
		for(int k=0;k<lengths.size();k++) {
			if((int)lengths.get(k) < shortestindex) {
				shortestindex = (int) lengths.get(k);
			}
		}
		shortestindex = lengths.indexOf(shortestindex);
		for(int l=0;l<shape1.npoints;l++) {
			
			if(l==(int) index1.get(shortestindex)) {
				
				for(int o=(int) index2.get(shortestindex) + 1;o<shape2.getpolygon().npoints;o++) {
					if(shape1.contains(new Point(shape2.getpolygon().xpoints[o],shape2.getpolygon().ypoints[o]))) {
						
					}else {
						result.addPoint(shape2.getpolygon().xpoints[o], shape2.getpolygon().ypoints[o]);
					}
				}
				if((int) index2.get(shortestindex) > 0) {
					for(int p=0;p<(int) index2.get(shortestindex);p++) {
						if(shape1.contains(new Point(shape2.getpolygon().xpoints[p],shape2.getpolygon().ypoints[p]))) {
							
						}else {
							result.addPoint(shape2.getpolygon().xpoints[p], shape2.getpolygon().ypoints[p]);
						}
						
					}
				}
			}else {
				if(shape2.getpolygon().contains(new Point(shape1.xpoints[l],shape1.ypoints[l]))) {
					
				}else {
				result.addPoint(shape1.xpoints[l], shape1.ypoints[l]);
				}
			}
		}
		
		return result;
	}
}
