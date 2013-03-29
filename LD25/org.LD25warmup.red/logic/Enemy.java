package logic;

import java.util.ArrayList;

import level.Block;

public interface Enemy{
	public void render();
	public void	changeTexture();
	public void move(float x, float y);
	public void move(float x, float y, int index, int texid);
	public void setPos(float x, float y, int index, int texid);
	public void setPos(float x, float y);
	public void addEnemy(float x, float y, ArrayList<Block> blocks);
	public void update();
	public void deleteAll();
	public boolean empty();
}
