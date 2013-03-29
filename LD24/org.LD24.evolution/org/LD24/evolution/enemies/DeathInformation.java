package org.LD24.evolution.enemies;

public class DeathInformation {
	private int survivaltime;
	private int amountofshapes;
	private int sizeofsides;
	private int amountofsides;
	
	public DeathInformation(int survivaltime, int amountofshapes, int sizeofsides, int amountofsides) {
		this.survivaltime = survivaltime;
		this.amountofsides = amountofshapes;
		this.sizeofsides = sizeofsides;
		this.amountofsides = amountofsides;
	}

	public int getSurvivaltime() {
		return survivaltime;
	}

	public void setSurvivaltime(int survivaltime) {
		this.survivaltime = survivaltime;
	}

	public int getAmountofshapes() {
		return amountofshapes;
	}

	public void setAmountofshapes(int amountofshapes) {
		this.amountofshapes = amountofshapes;
	}

	public int getSizeofsides() {
		return sizeofsides;
	}

	public void setSizeofsides(int sizeofsides) {
		this.sizeofsides = sizeofsides;
	}

	public int getAmountofsides() {
		return amountofsides;
	}

	public void setAmountofsides(int amountofsides) {
		this.amountofsides = amountofsides;
	}
}
