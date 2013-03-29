package org.LD24.evolution.GUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import org.LD24.evolution.graphics.Data;

public class LevelScreen extends Screen{
	Data data;
	private int stage;
	private BufferedImage writing = new BufferedImage(640,40,BufferedImage.TYPE_INT_ARGB);
	
	public LevelScreen(int level, Data parent) {
		data = parent;
		setBackground(Color.BLACK);
		Graphics g = writing.getGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 640, 480);
		g.setColor(Color.WHITE);
		g.drawString("Level " + Integer.toString(level), 300, 20);
		g.dispose();
		guigraphics.drawImage(writing,0,200,null);
	}
	public void setBackground(Color c) {
		guigraphics.setColor(c);
		guigraphics.fillRect(0, 0, 640, 480);
	}
	public void update() {
		setBackground(Color.BLACK);
		if(stage <= 200) {
			guigraphics.drawImage(changeAlpha((255/200) * stage,this.writing),0,200,null);
		}else {
			guigraphics.drawImage(changeAlpha(255 - ((255/200) * (stage - 200)),this.writing),0,200,null);
		}
		stage += 1;
		
	    if(stage > 400) {
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
