package org.LD24.evolution.graphics;

public class MyTimer extends Thread{
	Data data;
	
	public MyTimer(Data parent) {
		data = parent;
	}
	
	public void run() {
		while(true) {
			if(!data.timerup) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
					System.exit(1);
				}
				data.timerup = true;
			}else {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
					System.exit(1);
				}
				data.timespent += 10;
				if(data.rightdown) {
					data.playerx += 2;
					data.playerdir = 3;
				}
				if(data.leftdown) {
					data.playerx -= 2;
					data.playerdir = 0;
				}
			}
		}
	}
}
