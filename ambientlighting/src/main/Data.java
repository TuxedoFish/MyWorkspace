package main;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;


public class Data {

	public String string = "hello";
	public int mousexpos = 200;
	public int mouseypos = 200;
	public CustomGraphics graphics = new CustomGraphics(this);
	public BufferedImage Offscreen = new BufferedImage (640,480,BufferedImage.TYPE_INT_RGB );
	public Graphics buffergraphics = Offscreen.getGraphics();
	public BufferedImage testimage = new BufferedImage(640,480,BufferedImage.TYPE_INT_ARGB);
	public BufferedImage background;
	public int shadownlength = 1;
	public ArrayList<Integer> lightxpos = new ArrayList<Integer>();
	public ArrayList<Integer> lightypos = new ArrayList<Integer>();
	public ArrayList<Integer> lightbrightness = new ArrayList<Integer>();
	public ArrayList<Integer> lightintensity = new ArrayList<Integer>();
	public ArrayList<Integer> lightred = new ArrayList<Integer>();
	public ArrayList<Integer> lightgreen = new ArrayList<Integer>();
	public ArrayList<Integer> lightblue = new ArrayList<Integer>();
	public ArrayList<Integer> playerxpos = new ArrayList<Integer>();
	public ArrayList<Integer> playerypos = new ArrayList<Integer>();

	
	public Data()
	{
		for(int add=0;add<4;add++)
		{
			playerxpos.add(0);
		    playerypos.add(0);
		}
	}
	
}
