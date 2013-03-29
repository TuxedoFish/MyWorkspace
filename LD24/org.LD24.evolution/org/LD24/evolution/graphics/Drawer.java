package org.LD24.evolution.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JPanel;

import org.LD24.evolution.GUI.LevelScreen;

public class Drawer extends JPanel{
	Data data;
	
	public Drawer(Data parent) {
		this.data = parent;
		repaint();
	}
	/**
	 * Draws Everything
	 * bad practice probably but i am learning.
	 */
	public void paint(Graphics g) {
		if(!data.started) {
			g.drawImage(data.titlescreen.gui,0,0,this);
			data.titlescreen.update();
		}else {
			if(!data.timerup) {
				g.drawImage(data.levelscreen.gui,0,0,this);
				data.levelscreen.update();
		    }else {
				data.bdgraphics.drawImage(data.bg,0,0,this);
				
				data.bdgraphics.setColor(Color.orange);
				for(int i=0;i<data.enemies.size();i++) {
					data.bdgraphics.fillPolygon(data.enemies.get(i).getshape());
				}
				if(data.onupdate.endoflevel()) {
					data.level += 1;
					data.playerbullets.clear();
				}
				data.onupdate.update();
				data.bdgraphics.drawImage(data.player.get(data.playerdir + data.playerimage),data.playerx,430,this);
	
				data.bdgraphics.setColor(Color.RED);
				
				for(int i=0;i<data.particles.size();i++) {
					if(data.stage < 48) {
					data.bdgraphics.drawRect(data.particles.get(i).getXpos(), data.particles.get(i).getYpos(), 5, 5);
					}
				}
				
				data.bdgraphics.setColor(Color.BLUE);
				data.delete = false;
				for(int i=0;i<data.playerbullets.size();) {
					data.bdgraphics.drawRect(data.playerbullets.get(i).getXpos(), data.playerbullets.get(i).getYpos(), 5, 5);
				    data.playerbullets.get(i).update();
				    
				    for(int j=0;j<data.enemies.size();j++) {
				    	if(data.enemies.get(j).getshape().contains(new Rectangle(data.playerbullets.get(i).getXpos() - 5, data.playerbullets.get(i).getYpos(), 5, 10))) {
				    		data.enemies.get(j).die();
				    		data.delete = true;
				    	}
				    }
				    if(data.delete) {
				    	data.playerbullets.remove(i);
				    	data.delete = false;
				    }else {
				    	i += 1;
				    }
				}
				g.drawImage(data.bufferimage,0,0,this);
			  }
		}
		repaint();
	}
	
}
