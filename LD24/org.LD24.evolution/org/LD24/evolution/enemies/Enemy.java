package org.LD24.evolution.enemies;

import java.applet.AudioClip;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.sound.midi.SoundbankResource;
import javax.sound.midi.spi.SoundbankReader;
import javax.sound.sampled.*;


import org.LD24.evolution.graphics.Data;
import org.LD24.evolution.shapes.*;
/**
 * Enemies base methods
 * @author harry
 *
 */
public class Enemy {
	private int xpos;
	private int ypos;
	private int direction;
	private int stage = 0;
	private Polygon myshape;
	private Polygon mastershape;
	private Polygon mydisplayshape;
	private int rotationcentrex;
	private int rotationcentrey;
	private int rotationangle = 0;
	private ArrayList<Job> behaviours = new ArrayList<Job>();
	private Data data;
	private double xmove;
	private double ymove;
	private boolean created = false;
	private boolean visible = true;
	private ArrayList<DeathInformation> deathlog = new ArrayList<DeathInformation>();
	private Polygon result = new Polygon();
	private int amountofshapes;
	private ArrayList<Integer> sidesize = new ArrayList<Integer>();
	private ArrayList<Integer> nsides = new ArrayList<Integer>();
	private int directiontochangeto;
	/**
	 * Base enemy constructor
	 */
	public Enemy(Data parent) {
		data = parent;
		show();
	}
	/**
	 * Creates a different class which calls this one every "update"/time the Drawer draws
	 */
	public void addTriggers() {
		Job jobmove = new Job(data,this);
		jobmove.setToDo("move");
		jobmove.setTrigger("on_update");
		behaviours.add(jobmove);
		setrotationcentre();
		direction = (int)(Math.random() * 360);
	}
	/**
	 * This method moves the enemy using its direction + trigonometry and then calls the rotate method.
	 * It only works once every 2 calls and is the screen collision detector too.
	 */
	public void move() {
		if(stage < 1) {
			stage += 1;
		}else {
		xmove = Math.sin(Math.toRadians(this.direction)) * 1;
		ymove = Math.cos(Math.toRadians(this.direction)) * 1;
		xpos += xmove;
		ypos += ymove;
		this.rotationcentrex += xmove;
		this.rotationcentrey += ymove;
		this.directiontochangeto = this.direction;
		
		for(int i=0;i<this.myshape.npoints;i++) {
			myshape.xpoints[i] += xmove;
			myshape.ypoints[i] += ymove;
			if(this.directiontochangeto == this.direction) {
				if(mydisplayshape.xpoints[i] <= 50) {
					this.directiontochangeto = (int) (90);
				}
				if(mydisplayshape.xpoints[i] >= 610) {
					this.directiontochangeto = (int) (270);
				}
				if(mydisplayshape.ypoints[i] <= 50) {
				    this.directiontochangeto = (int) (0);
				}
				if(mydisplayshape.ypoints[i] >= 400) {
				    this.directiontochangeto = (int) (180);
			    }
			}
		}
			this.direction = this.directiontochangeto;
			this.mydisplayshape = rotate(1);
			stage = 0;
		}
	}
	/**
	 * rotates the Polygon and returns the rotated version
	 * @param angle
	 * @return Polygon-rotated version
	 */
	public Polygon rotate(int angle) {
		Polygon result = new Polygon();
		rotationangle += angle;
		if(rotationangle >= 360) {
			rotationangle -= 360;
		}
		int tempx;
		int tempy;
		for(int i=0;i<myshape.npoints;i++) {
			tempx = (int) ((Math.cos(Math.toRadians(rotationangle))*(myshape.xpoints[i] - this.rotationcentrex)) - (Math.sin(Math.toRadians(rotationangle))*(myshape.ypoints[i] - this.rotationcentrey)) + this.rotationcentrex);
			tempy = (int) ((Math.sin(Math.toRadians(rotationangle))*(myshape.xpoints[i] - this.rotationcentrex)) + (Math.cos(Math.toRadians(rotationangle))*(myshape.ypoints[i] - this.rotationcentrey)) + this.rotationcentrey);
			result.addPoint((int)(tempx), (int)(tempy));
			System.out.println(rotationcentrex + " " + rotationcentrey);
		}
		return result;
	}
	/**
	 * finds the centre most point of the polygon
	 */
	public void setrotationcentre() {
		int lowestx = myshape.xpoints[0];
		int highestx = myshape.xpoints[0];
		int highesty = myshape.ypoints[0];
		int lowesty = myshape.ypoints[0];
		
		for(int i=1;i<myshape.npoints;i++) {
			
			if(myshape.xpoints[i] < lowestx) {
				lowestx = myshape.xpoints[i];
			}
			if(myshape.xpoints[i] > highestx) {
				highestx = myshape.xpoints[i];
			}
			if(myshape.ypoints[i] < lowesty) {
				lowesty = myshape.xpoints[i];
			}
			if(myshape.ypoints[i] > highesty) {
				highesty = myshape.xpoints[i];
			}
		}
		Rectangle r = myshape.getBounds();
		rotationcentrex = lowestx + ((highestx - lowestx)/2);
		rotationcentrey = lowesty + ((highesty - lowesty)/2);
		rotationcentrex = lowestx;
		rotationcentrey = highesty;
	}
	/**
	 * 
	 * @return Polygon-this instances shape
	 */
	public Polygon getshape() {
		if(visible) {
			return this.mydisplayshape;
		}else {
			return new Polygon();
		}
	}
	/**
	 * 
	 * @return Boolean-Whether it is visible
	 */
	public Boolean displayble() {
		return visible;
	}
	/**
	 * makes it invisible and plays the death sound and calls an explosion class
	 */
	public void die() {
	    playSound("explosion.wav");
		visible = false;
		data.timespent = 0;
		Runnable explosion = new Explosion(this.rotationcentrex,this.rotationcentrey,data);
		Thread explosionthread = new Thread(explosion);
	}
	/**
	 * Plays a .wav sound that is in the same directory as this class
	 * @param url
	 */
	public static synchronized void playSound(final String url) {
	    new Thread(new Runnable() { // the wrapper thread is unnecessary, unless it blocks on the Clip finishing, see comments
	      public void run() {
	        try {
	          InputStream myStream = new BufferedInputStream(getClass().getResourceAsStream(url)); 
	          AudioInputStream audio2 = AudioSystem.getAudioInputStream(myStream);
	          
	          Clip clip = AudioSystem.getClip();
	          clip.open(audio2);
	          clip.start(); 
	        } catch (Exception e) {
	          System.err.println(e.getMessage());
	        }
	      }
	    }).start();
	  }
	/**
	 * Called to re-make the enemy each update and "evolve" it
	 */
	public void show() {
		if(data.level!=1) {
		//evolve it
		int avgsidesize = 0;
		int avgamntsides = 0;
		for(int i=0;i<this.sidesize.size();i++) {
			avgsidesize += this.sidesize.get(i);
		}
		for(int j=0;j<this.nsides.size();j++) {
			avgamntsides += this.nsides.get(j);
		}
		avgsidesize = avgsidesize/this.sidesize.size();
		avgamntsides = avgamntsides/this.nsides.size();
		this.deathlog.add(new DeathInformation(data.timespent, this.amountofshapes, avgsidesize, avgamntsides));
		}
		if(this.deathlog.size() < 3) {
			
			this.nsides.clear();
			this.sidesize.clear();
			int tempnsides;
			int tempsidesize;
			int startx = (int) (140 + Math.random()*300);
			int starty = (int) (100 + Math.random()*250);
			tempnsides = 4 + (int)(Math.random()*5);
			tempsidesize = (int) (30 + Math.random()*10);
			this.nsides.add(tempnsides);		
			this.sidesize.add(tempsidesize);
			result = new Polygon();
			this.amountofshapes = (int) (1 + (Math.random()*6));
			result = new Shape(tempnsides,startx,starty,tempsidesize).getpolygon();
			for(int i=1;i<amountofshapes;i++) {
				startx += -30 + Math.random()*60;
				starty += -30 + Math.random()*60;
				tempnsides = 4 + (int)(Math.random()*5);
				tempsidesize = (int) (30 + Math.random()*10);
				this.nsides.add(tempnsides);		
				this.sidesize.add(tempsidesize);
				result = data.shpmker.combine2shapes(result, new Shape(tempnsides,startx,starty,tempsidesize));
			  }
		    }else {
			ArrayList<TimeToData> amountofshapes = new ArrayList<TimeToData>();
			ArrayList<TimeToData> sizeofshapes = new ArrayList<TimeToData>();
			ArrayList<TimeToData> amountofsides = new ArrayList<TimeToData>();
			ArrayList<Integer> amountofshapesname = new ArrayList<Integer>();
			ArrayList<Integer> sizeofshapesname = new ArrayList<Integer>();
			ArrayList<Integer> amountofsidesname = new ArrayList<Integer>();
			
		    for(int i=0;i<this.deathlog.size();i++) {
		    	if(amountofshapesname.indexOf(this.deathlog.get(i).getAmountofshapes()) == -1) {
		    		
		    		amountofshapesname.add(this.deathlog.get(i).getAmountofshapes());
		    		amountofshapes.add(new TimeToData(this.deathlog.get(i).getSurvivaltime()));
		    	}else {
		    		amountofshapes.get(amountofshapesname.indexOf(this.deathlog.get(i).getAmountofshapes())).addtime(this.deathlog.get(i).getSurvivaltime());
		    	}
		    	
		    	if(amountofsidesname.indexOf(this.deathlog.get(i).getAmountofsides()) == -1) {
		    		
		    		amountofsidesname.add(this.deathlog.get(i).getAmountofsides());
		    		amountofsides.add(new TimeToData(this.deathlog.get(i).getSurvivaltime()));
		    	}else {
		    		amountofsides.get(amountofsidesname.indexOf(this.deathlog.get(i).getAmountofsides())).addtime(this.deathlog.get(i).getSurvivaltime());
		    	}
		    	
		    	if(sizeofshapesname.indexOf(this.deathlog.get(i).getSizeofsides()) == -1) {
		    		
		    		sizeofshapesname.add(this.deathlog.get(i).getSizeofsides());
		    		sizeofshapes.add(new TimeToData(this.deathlog.get(i).getSurvivaltime()));
		    	}else {
		    		sizeofshapes.get(sizeofshapesname.indexOf(this.deathlog.get(i).getSizeofsides())).addtime(this.deathlog.get(i).getSurvivaltime());
		    	}
		    }
		    
		    int lowestavgtimesize = sizeofshapes.get(0).getaverage();
		    int lowestavgtimeamnt = amountofshapes.get(0).getaverage();
		    int lowestavgtimeamntsdes = amountofsides.get(0).getaverage();
		    int lowestavgtimesizeindex = sizeofshapes.get(0).getaverage();
		    int lowestavgtimeamntindex = amountofshapes.get(0).getaverage();
		    int lowestavgtimeamntsdesindex = amountofsides.get(0).getaverage();
		    for(int j=1;j<sizeofshapes.size();j++) {
		    	if(sizeofshapes.get(j).getaverage() > lowestavgtimesize && sizeofshapes.get(j).getaverage() != -1) {
		    		lowestavgtimesize = sizeofshapes.get(j).getaverage();
		    		lowestavgtimesizeindex = sizeofshapesname.get(j);
		    	}
		    }
		    for(int k=1;k<amountofshapes.size();k++) {
		    	if(amountofshapes.get(k).getaverage() > lowestavgtimeamnt && amountofshapes.get(k).getaverage() != -1) {
		    		lowestavgtimeamnt = amountofshapes.get(k).getaverage();
		    		lowestavgtimeamntindex = amountofshapesname.get(k);
		    	}
		    }
		    for(int l=1;l<amountofsides.size();l++) {
		    	if(amountofsides.get(l).getaverage() > lowestavgtimeamntsdes && amountofsides.get(l).getaverage() != -1) {
		    		lowestavgtimeamntsdes = amountofsides.get(l).getaverage();
		    		lowestavgtimeamntsdesindex = amountofsidesname.get(l);
		    	}
		    }
		    
		    this.nsides.clear();
			this.sidesize.clear();
			int tempnsides;
			int tempsidesize;
			int startx = (int) (140 + Math.random()*400);
			int starty = (int) (100 + Math.random()*280);
			if(lowestavgtimeamntsdesindex != 0) {
				tempnsides = (int) ((lowestavgtimeamntsdesindex) - 2 + (Math.random()*4));
			}else{
				tempnsides = (int) (4 + (Math.random()*5));
			}
			if(lowestavgtimeamntsdesindex != 0) {
				tempsidesize = (int) ((lowestavgtimesizeindex) - 2 + (Math.random()*4));
			}else {
				tempsidesize = (int) (30 + (Math.random()*10));
			}
			this.nsides.add(tempnsides);		
			this.sidesize.add(tempsidesize);
			result = new Polygon();
			if(lowestavgtimeamntsdesindex != 0) {
				this.amountofshapes = (int) ((lowestavgtimeamntindex) - 2 + (Math.random()*4));
			}else {
				this.amountofshapes = (int) (1 + (Math.random()*6));
			}
			Shape shapea = new Shape(tempnsides,startx,starty,tempsidesize);
			for(int i=1;i<this.amountofshapes;i++) {
				startx += -30 + Math.random()*60;
				starty += -30 + Math.random()*60;
				if(lowestavgtimeamntsdesindex != 0) {
					tempnsides = (int) ((lowestavgtimeamntsdesindex) - 2 + (Math.random()*4));
				}else{
					tempnsides = (int) (4 + (Math.random()*5));
				}
				if(lowestavgtimeamntsdesindex != 0) {
					tempsidesize = (int) ((lowestavgtimesizeindex) - 2 + (Math.random()*4));
				}else {
					tempsidesize = (int) (30 + (Math.random()*10));
				}
				this.nsides.add(tempnsides);		
				this.sidesize.add(tempsidesize);
				
				if(i == 1) {
					result = data.shpmker.combine2shapes(shapea.getpolygon(), new Shape(tempnsides,startx,starty,tempsidesize));
				}else{
					result = data.shpmker.combine2shapes(result, new Shape(tempnsides,startx,starty,tempsidesize));
				}
			  }
		}
		this.myshape = result;
		this.mydisplayshape = result;
		this.mastershape = result;
		this.setrotationcentre();
		visible = true;
		if(created == false) {
			addTriggers();
			created = true;
		}
	}
}
