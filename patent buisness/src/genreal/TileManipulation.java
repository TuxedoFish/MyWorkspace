package genreal;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;

import startup.Data;

public class TileManipulation {

	public Data data;
	public int Height;
	public int Width;
	
	public TileManipulation(Data parent)
	{
		
		data = parent;
		
	}
	
	public BufferedImage gettile(BufferedImage tileset,int tilesize,int x,int y)
	{
		
		Width = tileset.getWidth();
		Height = tileset.getHeight();
		
		int red;
		int green;
		int blue;
		Color TemporaryColor;
		
		BufferedImage Tile = new BufferedImage(tilesize-1,tilesize-1, BufferedImage.TYPE_INT_ARGB);

		for(int Donex = (x*tilesize) + 1;Donex<((x*tilesize)) + (tilesize-1);Donex++)
		{
			for(int Doney = (y*tilesize) + 1;Doney<((y*tilesize)) + (tilesize-1);Doney++)
			{
				
				TemporaryColor = new Color (tileset.getRGB(Donex,Doney));
				red = TemporaryColor.getRed();
				green = TemporaryColor.getGreen();
				blue = TemporaryColor.getBlue();
				//If it isnt the invisible pink chosen by me in my tileset
				if(red!=255 || green!=148 || blue!=250)
				{
					  Tile.setRGB(Donex - (x*tilesize),Doney - (y*tilesize),TemporaryColor.getRGB());
					
				}
				
			}
		}
		
		
		return Tile;
		
	}
	
	public int gettileWidth(BufferedImage tileset,int tilesize,int x,int y,int KeySpacing)
	{
		
		Width = 0;
		
		int red;
		int green;
		int blue;
		Color TemporaryColor;

		for(int Donex = (x*tilesize) + 1;Donex<((x*tilesize)) + (tilesize-1);Donex++)
		{
			for(int Doney = (y*tilesize) + 1;Doney<((y*tilesize)) + (tilesize-1);Doney++)
			{
				
				TemporaryColor = new Color (tileset.getRGB(Donex,Doney));
				red = TemporaryColor.getRed();
				green = TemporaryColor.getGreen();
				blue = TemporaryColor.getBlue();
				//If it isn't the invisible pink chosen by me in my tile set
				if(red!=255 || green!=148 || blue!=250)
				{
					 
					if(Donex - (x*tilesize) + 1 > Width)
					{
						
						Width = Donex - (x*tilesize) + KeySpacing;
						
					}
					
				}
				
			}
		}
		
		
		return Width;
		
	}
	
}
