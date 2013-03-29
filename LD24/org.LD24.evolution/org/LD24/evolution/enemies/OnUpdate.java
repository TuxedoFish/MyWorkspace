package org.LD24.evolution.enemies;

import java.util.ArrayList;

public class OnUpdate {
	private ArrayList<Job> listeners = new ArrayList<Job>();
	private ArrayList<Integer> keys = new ArrayList<Integer>();
	private Boolean done;
	private int key;
	
	public void update () {
		if(this.endoflevel()) {
			for(int i=0;i<listeners.size();i++) {
				listeners.get(i).getparent().show();
			}
		}
		for(int i=0;i<listeners.size();i++) {
			listeners.get(i).triggered("on_update");
		}
	}
	public int addlistener(Job j) {
		listeners.add(j);
		done = false;
		
		while(!done) {
			key = (int) Math.round((Math.random() * 10000));
			if(keys.indexOf(key) == -1) {
				keys.add(key);
				return key;
			}
		}
		return 0;
	}
	public void deletelistener(int key) {
		
	}
	public boolean endoflevel() {
		for(int j=0;j<listeners.size();j++) {
			if(listeners.get(j).getparent().displayble()) {
				return false;
			}
		}
		return true;
	}
}
