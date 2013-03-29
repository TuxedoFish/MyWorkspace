package org.LD24.evolution.enemies;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import org.LD24.evolution.graphics.Data;

public class Job {
	private String trigger = "none";
	private String todo = "none";
	private Data data;
	private int myupdatekey;
	private int mypressedkey;
	private Enemy myparent;
	
	public Job(Data data, Enemy parent) {
		this.data = data;
		myparent = parent;
		myupdatekey = this.data.onupdate.addlistener(this);
	}
	public Enemy getparent() {
		return myparent;
	}
	public void setTrigger(String trigger) {
		this.trigger = trigger;
	}
	public void setToDo(String todo) {
		this.todo = todo;
	}
	public void triggered(String trigger) {
		if(trigger == this.trigger) {
			if(this.todo == "move") {
				myparent.move();
			}
		}
	}
	public void triggered(String trigger, MouseEvent e) {
		if(myparent.getshape() == null) {
			if(myparent.displayble()) {
				myparent.die();
			}
		}else {
			if(myparent.getshape().contains(new Rectangle(e.getX() - 10,e.getY() - 30,20,20))) {
				if(data.timerup) {
					myparent.die();
				}
		    }
	    }
	}
	public void todo() {
		
	}
}
