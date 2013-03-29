package startup;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

import gameelements.HiringButtons;
import gamehub.*;
import genreal.*;
import input.*;
import resources.*;

public class Data {
	
	//Classes
	public Buttons buttons = new Buttons(this);
	public Phone phone = new Phone(this);
	public EmployeeReader reader = new EmployeeReader(this);
	public TileManipulation tile = new TileManipulation(this);
	public Strings string = new Strings(this);
	public HiringButtons hiringbuttons = new HiringButtons(this);
	public Runnable timecalculator = new TimeCalculator(this);
	public Transition transition = new Transition(this);
	public HelpScreen helpclass = new HelpScreen(this);
	public TitleScreen titleclass = new TitleScreen(this);
	public Employee employeeclass = new Employee(this);
	//Threads
	Thread timecalculatorthread = new Thread(timecalculator);
	//Graphics stuff
	public BufferedImage Offscreen = new BufferedImage (640,480,BufferedImage.TYPE_INT_ARGB );
	public BufferedImage Overlayscreen = new BufferedImage (640,480,BufferedImage.TYPE_INT_ARGB );
	public BufferedImage BufferedOverlayscreen = new BufferedImage (640,480,BufferedImage.TYPE_INT_ARGB );
	public Graphics buffergraphics = Offscreen.getGraphics();
	public Graphics2D buffergraphics2D = Offscreen.createGraphics ();
	public Graphics2D graphicsstuffgraphics = Overlayscreen.createGraphics ();
	public Graphics2D graphicsstuffgraphicsbuffered = BufferedOverlayscreen.createGraphics ();
	//Ints
	public int waituntilshow;
	public int mousexpos;
	public int mouseypos;
	public int transparencystage = 101;
	public int inttransparency;
    public int MinToSearchFor;
    public int MaxToSearchFor;
    public int button = 0;
    public int sliderxpos = 301;
    public int time = 0;
	//Booleans
    public boolean wait;
	public boolean down;
	public boolean released = true;
	public boolean sliderdown = false;
	public boolean hiringtocome = false;
	//Images
	public Image masterbutton;
	public Image masterbuttonmouseover;
	public Image masterbuttonpressed;
	public BufferedImage startscreen;
	public Image cutscene;
	public Image gamehub;
	public Image helpscreen;
	public Image hiringscreen;
	public Image closebutton;
	public Image closetouchedbutton;
	public Image closepressedbutton;
	public Image clickbox;
	public Image clickboxtouched;
	public Image clickboxpressed;
	public Image Tick;
	public Image employee;
	//-sub category 
	//BufferedImages
	public BufferedImage Tileset;
	public BufferedImage LetterA;
    //Rectangles
    public Rectangle play = new Rectangle(295, 150,100, 20);
    public Rectangle help = new Rectangle(295, 250,100, 20);
    public Rectangle back = new Rectangle(500, 350,100, 20);
    public Rectangle close = new Rectangle(510,90,40,40);
    public Rectangle close2 = new Rectangle(400,2,40,40);
    public Rectangle Slider = new Rectangle(274,143,20,20);
    public Rectangle ProductionStaff = new Rectangle(222,270,20,20);
    public Rectangle Expeditioners = new Rectangle(206,333,20,20);
    public Rectangle BrilliantExpeditioners = new Rectangle(246,392,20,20);
    public Rectangle Confirm = new Rectangle(450,380,100,20);
    //Floats
    public double transparency;
	public double fadespeed = 40;
    //Strings
    public String stage = "title"; 
    //ArrayList
    public ArrayList<BufferedImage> Keys = new ArrayList<BufferedImage>();
    public ArrayList<String> KeyLocations = new ArrayList<String>();
    public ArrayList<Integer> KeyWidths = new ArrayList<Integer>();
    //Array
    
    public Data()
    {
    	
    	KeyLocations.add("a");
    	KeyLocations.add("b");
    	KeyLocations.add("c");
    	KeyLocations.add("d");
    	KeyLocations.add("e");
    	KeyLocations.add("f");
    	KeyLocations.add("g");
    	KeyLocations.add("h");
    	KeyLocations.add("i");
    	KeyLocations.add("j");
    	KeyLocations.add("k");
    	KeyLocations.add("l");
    	KeyLocations.add("m");
    	KeyLocations.add("n");
    	KeyLocations.add("o");
    	KeyLocations.add("p");
    	KeyLocations.add("q");
    	KeyLocations.add("r");
    	KeyLocations.add("s");
    	KeyLocations.add("t");
    	KeyLocations.add("u");
    	KeyLocations.add("v");
    	KeyLocations.add("w");
    	KeyLocations.add("x");
    	KeyLocations.add("y");
    	KeyLocations.add("z");
    	KeyLocations.add("!");
    	KeyLocations.add("\"");
    	KeyLocations.add("£");
    	KeyLocations.add("$");
    	KeyLocations.add("%");
    	KeyLocations.add("^");
    	KeyLocations.add("&");
    	KeyLocations.add("*");
    	KeyLocations.add("(");
    	KeyLocations.add(")");
    	KeyLocations.add("A");
    	KeyLocations.add("B");
    	KeyLocations.add("C");
    	KeyLocations.add("D");
    	KeyLocations.add("E");
    	KeyLocations.add("F");
    	KeyLocations.add("G");
    	KeyLocations.add("H");
    	KeyLocations.add("I");
    	KeyLocations.add("J");
    	KeyLocations.add("K");
    	KeyLocations.add("L");
    	KeyLocations.add("M");
    	KeyLocations.add("N");
    	KeyLocations.add("O");
    	KeyLocations.add("P");
    	KeyLocations.add("Q");
    	KeyLocations.add("R");
    	KeyLocations.add("S");
    	KeyLocations.add("T");
    	KeyLocations.add("U");
    	KeyLocations.add("V");
    	KeyLocations.add("W");
    	KeyLocations.add("X");
    	KeyLocations.add("Y");
    	KeyLocations.add("Z");
    	KeyLocations.add(":");
    	KeyLocations.add("1");
    	KeyLocations.add("2");
    	KeyLocations.add("3");
    	KeyLocations.add("4");
    	KeyLocations.add("5");
    	KeyLocations.add("6");
    	KeyLocations.add("7");
    	KeyLocations.add("8");
    	KeyLocations.add("9");
    	KeyLocations.add(".");
    	KeyLocations.add("0");  	
    	
    }
}
