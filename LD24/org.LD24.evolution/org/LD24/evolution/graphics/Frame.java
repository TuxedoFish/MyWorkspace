package org.LD24.evolution.graphics;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.LD24.evolution.graphics.Data;
import org.LD24.evolution.graphics.Drawer;
import org.LD24.evolution.input.Keylistener;
import org.LD24.evolution.input.Mouselistener;
/**
 * Sets up JFrame
 * @author harry
 *
 */
public class Frame {
	public static void main(String args[])
	{
		JFrame frame = new JFrame();
		
		Data data = new Data();
		Drawer main = new Drawer(data);
		Mouselistener mouseinput = new Mouselistener(frame,data);
		Keylistener keyinput = new Keylistener(frame,data);
		
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
        frame.add(main);
		frame.setSize(640,480);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setIconImage(data.icon);
		frame.setTitle("shapelution");
	}
}
