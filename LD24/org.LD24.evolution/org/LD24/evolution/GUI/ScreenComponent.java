package org.LD24.evolution.GUI;

import java.awt.image.BufferedImage;

public class ScreenComponent {
	private int xpos;
	private int ypos;
	private BufferedImage src;
	
	public ScreenComponent(int xpos, int ypos, BufferedImage src) {
		this.xpos = xpos;
		this.ypos = ypos;
		this.src = src;
	}

	public int getXpos() {
		return xpos;
	}

	public void setXpos(int xpos) {
		this.xpos = xpos;
	}

	public int getYpos() {
		return ypos;
	}

	public void setYpos(int ypos) {
		this.ypos = ypos;
	}
	public BufferedImage getSrc() {
		return src;
	}

	public void setSrc(BufferedImage src) {
		this.src = src;
	}

}
