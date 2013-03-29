package org.LD24.evolution.GUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Screen {
	public BufferedImage gui = new BufferedImage(640,480,BufferedImage.TYPE_INT_ARGB);
	public Graphics guigraphics = gui.getGraphics();
	public ArrayList<ScreenComponent> Components = new ArrayList<ScreenComponent>();
	
	public void setBackground(){}
	
	public void setForeground(){}
	
	public void update(){}
	
	public void addComponent(BufferedImage src, int xpos, int ypos){
		Components.add(new ScreenComponent(xpos,ypos,src));
	}
}
