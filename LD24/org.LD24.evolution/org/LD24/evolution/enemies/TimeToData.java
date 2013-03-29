package org.LD24.evolution.enemies;

public class TimeToData {
	private int total;
	private int piecesofinfo;
	public TimeToData(int time) {
		total += time;
		piecesofinfo += 1;
	}
	
	public int getaverage() {
		if(piecesofinfo == 0) {
			return -1;
		}else {
			return total/piecesofinfo;
		}
	}
	public void addtime(int time) {
		total += time;
		piecesofinfo += 1;
	}
}
