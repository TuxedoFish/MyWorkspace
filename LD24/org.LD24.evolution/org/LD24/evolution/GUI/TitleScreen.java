package org.LD24.evolution.GUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

import org.LD24.evolution.graphics.Data;

public class TitleScreen extends Screen{
	Data data;
	BufferedImage bg;
	BufferedImage masterbutton;
	BufferedImage tempbutton;
	private int stage = 0;
	private Graphics tempg;
	private Color tempcolor;
	private int tempint;
	private int tempr;
	private int tempgr;
	private int tempb;
	private int tempa;
	
	public TitleScreen(BufferedImage bg,BufferedImage button, Data parent) {
		data = parent;
		this.bg = bg;
		this.masterbutton = button;
		this.tempbutton = button;
		tempg = tempbutton.getGraphics();
		setBackground(this.bg);
		this.addComponent(button, 320 - (button.getWidth()/2), 240 - (button.getHeight()/2));
	}
	public void setBackground(BufferedImage bg) {
		guigraphics.drawImage(bg,0,0,null);
	}
	public void update() {
		setBackground(this.bg);
		if(stage <= 200) {
			this.Components.get(0).setSrc(changeAlpha(((255/200) * stage),this.masterbutton));
		}else {
			if(stage > 300) {
				this.Components.get(0).setSrc(changeAlpha(255 - ((255/200) * (stage - 300)),this.masterbutton));
			}
		}
		guigraphics.drawImage(this.Components.get(0).getSrc(), this.Components.get(0).getXpos(), this.Components.get(0).getYpos(),null);
	    stage += 1;
	    if(stage > 500) {
	    	stage = 0;
	    }	
	}
	public BufferedImage changeAlpha(int alpha, BufferedImage button) {
		final BufferedImage result = new BufferedImage(button.getWidth(),button.getHeight(), BufferedImage.TYPE_INT_ARGB);
		int b;
		int g;
		int r;
		final Color c;
		final int cc;
		cc = new Color(255,255,255,alpha).getRGB();
		
		for(int i=0;i<button.getWidth();i++) {
			for(int j=0;j<button.getHeight();j++) {
				b = (button.getRGB(i, j))&0xFF;
				g = (button.getRGB(i, j)>>8)&0xFF;
				r = (button.getRGB(i, j)>>16)&0xFF;
				if(r >= 240 && g >= 240 && b >= 240) {
					result.setRGB(i, j, cc);
				}
			}
		}
		return result;
	}
}
