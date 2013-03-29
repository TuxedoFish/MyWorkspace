package org.LD24.evolution.enemies;

import java.util.ArrayList;

import org.LD24.evolution.graphics.Data;

public class Explosion extends Thread{
	private ArrayList<Particle> particles= new ArrayList<Particle>();
	private Data data;
	private int stage = 0;
	
	public Explosion(int xpos, int ypos, Data parent) {
		data = parent;
		for(int i=0;i<20;i++) {
			particles.add(new Particle((int)(xpos - 100 + (Math.random()*200)),(int)(ypos - 100 + (Math.random()*200))));
		}
		data.particles = this.particles;
		this.start();
	}
	@Override
	public void run() {
		while(stage < 50) {
			for(int i=0;i<this.particles.size();i++) {
				particles.get(i).update();
			}
			data.particles = this.particles;
			data.stage = this.stage;
			stage += 1;
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		try {
			this.finalize();
		} catch (Throwable e) {
			System.exit(1);
		}
		data.particles.clear();
		
		}
	}

